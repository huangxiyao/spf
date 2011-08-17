package com.hp.spp.groups;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.spp.common.ResourceHistory;
import com.hp.spp.common.ResourceHistoryManager;
import com.hp.spp.common.util.parsers.XmlParsingErrorHandler;
import com.hp.spp.common.util.xml.XmlUtils;
import com.hp.spp.groups.exception.XmlImportException;

/**
 * Class to manager Sites for a user interface (import of an Xml File).
 * 
 * @author ageymond
 * 
 */
public class SiteManager {

	private static Logger logger = Logger.getLogger(SiteManager.class);

	private final static boolean xsdValidationEnable = true;

	public SiteManager(Site pSite) {
		mSite = pSite;
	}

	Site mSite = null;

	public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	/**
	 * Load the xml file of the group definitions. This method check the syntax, if there is an
	 * error in the XML file, raise an exception
	 * 
	 * @param userProfile
	 * 
	 * @param byteArray
	 * @return a Map containing the name and the status of each group (new or already existing
	 *         in the database)
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XmlImportException
	 */
	public List loadFromByteArray(Map userProfile, byte[] byteArray)
			throws ParserConfigurationException, SAXException, IOException, XmlImportException {

		InputStream is = new ByteArrayInputStream(byteArray);

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		Document xmlDocument = null;

		if (xsdValidationEnable) {
			if (logger.isDebugEnabled()) {
				logger.debug("XML Validation is NOT ENABLE");
			}

			URL xmlUrl = getClass().getClassLoader().getResource("xsd/SiteDefinition.xsd");

			docBuilderFactory.setNamespaceAware(true);
			docBuilderFactory.setValidating(true);

			docBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
			docBuilderFactory.setAttribute(JAXP_SCHEMA_SOURCE, xmlUrl.openStream());

			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			XmlParsingErrorHandler errorHandler = new XmlParsingErrorHandler();
			docBuilder.setErrorHandler(errorHandler);

			xmlDocument = docBuilder.parse(is);

			if (errorHandler.isExistErrors()) {
				throw new XmlImportException("Problem during Xml File parsing");
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("XML Validation is NOT ENABLE");
			}
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			xmlDocument = docBuilder.parse(is);
		}

		validateAttributes(userProfile, xmlDocument);
		return loadFromXml(xmlDocument);
	}

	private void validateAttributes(Map userProfile, Document xmlDocument)
			throws XmlImportException {
		HashSet invalidAttributes = new HashSet();
		
		// use jdom & XPath to get all the nodes with the 'attribute' attribute
		org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
		org.dom4j.Document dom4jDoc = xmlReader.read(xmlDocument);
		Iterator it = dom4jDoc.selectNodes("//@attribute").iterator();
		while (it.hasNext()) {
			org.dom4j.Attribute attribute = (org.dom4j.Attribute) it.next();
			String attributeValue = attribute.getValue();
			if (!userProfile.containsKey(attributeValue)) {
				invalidAttributes.add(attributeValue);
			}
		}

		if (invalidAttributes.size() > 0) {
			throw new XmlImportException("The following attribute(s) "
					+ "do(es) not exist in the user profile: " + invalidAttributes);
		}
	}

	/**
	 * read the XML file and check the rules : - 2 defintions for the same group - circular
	 * references - link to existing groups
	 * 
	 * @param pDocument
	 * @return a Map containing the name and the status of each group (new or already existing
	 *         in the database
	 * @throws XmlImportException
	 */
	public List loadFromXml(Document pDocument) throws XmlImportException {
		List results = new ArrayList();

		// 01 -> check if the Site name is the right one
		Element rootElement = pDocument.getDocumentElement();

		// create a map to count the number of times a group is present in
		// the XML file, at the end, if it is present more than once, raise an exception
		HashMap groupCounterMap = new HashMap();

		// create a map to store all the groups, both in DB and in the XML file
		// this map will be used to check thein Group condition (reference existings groups and
		// no circular references)
		// the key in the map is the groupName and the value is either the Group object if it
		// comes from DB (not found in XML)
		// or the Element (XML element) if the group is not found in DB but exists in XML
		HashMap groupMap = new HashMap();
		// initialize the content of the maps with all the groups stored in the DB
		Iterator it = mSite.getGroupList().iterator();
		while (it.hasNext()) {
			Group g = (Group) it.next();
			groupMap.put(g.getName(), g);
		}

		if (rootElement.getAttribute("name").equalsIgnoreCase(mSite.getName())) {

			// 02 -> Extract all group Node
			NodeList groupList = rootElement.getElementsByTagName("Group");

			String currentGroupName;
			GroupStatus status = null;
			for (int groupIter = 0; groupIter < groupList.getLength(); groupIter++) {
				Element currentGroupElement = (Element) groupList.item(groupIter);
				currentGroupName = (currentGroupElement).getAttribute("name");
				status = new GroupStatus(currentGroupName);
				status.setXmlFragment(currentGroupElement);
				if (isExistingGroup(currentGroupName)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Group name is " + currentGroupName + " already exist");
					}
					status.setExistingFlag(GroupStatus.EXISTING_GROUP);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Group name is " + currentGroupName + " new");
					}
					status.setExistingFlag(GroupStatus.NEW_GROUP);
				}
				results.add(status);
				// add the group in the counter map
				int count = 0;
				if (groupCounterMap.containsKey(currentGroupName)) {
					count = ((Integer) groupCounterMap.get(currentGroupName)).intValue();
				}
				count++;
				groupCounterMap.put(currentGroupName, new Integer(count));

				// add the group in the all group map with the "XML" value to indicate that the
				// latest group
				// definition must be taken from the XML file
				groupMap.put(currentGroupName, currentGroupElement);

			}

		} else {
			throw new XmlImportException(
					"Site name in the Xml file is different than current site");
		}

		// check existence of multiple groups
		boolean multipleDefinitionOK = true;
		if (logger.isDebugEnabled()) {
			logger.debug("Content of the groupCounterMap [" + groupCounterMap + "]");
		}

		Iterator it2 = groupCounterMap.keySet().iterator();
		StringBuffer multipleGroupErrorMessage = new StringBuffer();
		while (it2.hasNext()) {
			String key = (String) it2.next();
			Integer count = (Integer) groupCounterMap.get(key);
			if (count.intValue() > 1) {
				multipleDefinitionOK = false;
				multipleGroupErrorMessage.append("- Group [").append(key).
						append("] is defined more than once is the XML file ");
			}
		}
		if (!multipleDefinitionOK) {
			logger.error(multipleGroupErrorMessage.toString());
			throw new XmlImportException(multipleGroupErrorMessage.toString());
		}

		// --- check the group references (existence and no circular)
		boolean inGroupExistenceOK = true;
		if (logger.isDebugEnabled()) {
			logger.debug("Content of the groupMap [" + groupMap + "]");
		}
		Iterator it3 = groupMap.keySet().iterator();
		// create a new map that will contain the group name as key and the list of inGroups
		// name as value
		HashMap inGroupMap = new HashMap();
		while (it3.hasNext()) {
			String key = (String) it3.next();
			ArrayList inGroupList = getInGroupReferenceNames(groupMap.get(key));
			inGroupMap.put(key, inGroupList);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("The content of the inGroup map is [" + inGroupMap + "]");
		}

		// - browse the inGroupMap to detect if the inGroupsReferences point to existing groups
		StringBuffer inGroupExistenceErrorMessage = new StringBuffer();
		Iterator it4 = groupMap.keySet().iterator();
		while (it4.hasNext()) {
			String key = (String) it4.next();
			// we check only the new groups (coming from XML)
			if (groupMap.get(key) instanceof Element) {
				ArrayList inGrouplist = (ArrayList) inGroupMap.get(key);
				for (int i = 0; i < inGrouplist.size(); i++) {
					if (!groupMap.containsKey(inGrouplist.get(i))) {
						inGroupExistenceOK = false;
						inGroupExistenceErrorMessage.append("- The group [").append(key).
								append("] reference the group [").append(inGrouplist.get(i)).
								append("] which does not exist ");
					}
				}
			}
		}
		if (!inGroupExistenceOK) {
			logger.error(inGroupExistenceErrorMessage.toString());
			throw new XmlImportException(inGroupExistenceErrorMessage.toString());
		}

		// - browse the inGroupMap to detect if the inGroupsReferences generate circular refs
		boolean inGroupCircularOK = true;
		StringBuffer inGroupCircularErrorMessage = new StringBuffer();
		Iterator it5 = groupMap.keySet().iterator();
		while (it5.hasNext()) {
			String key = (String) it5.next();
			ArrayList circList = new ArrayList();
			// add the current group name as starting point of the circ group list
			circList.add(key);
			boolean circularRefExists = SiteManager.detectCircularGroupReferences(key,
					inGroupMap, circList);
			if (logger.isDebugEnabled()) {
				logger.debug("The content of the list of circular detection for the Group ["
						+ key + "] is [" + circList + "]");
			}
			if (circularRefExists) {
				logger.error("Circular reference detected in the Group [" + key + "]");
				inGroupCircularErrorMessage.append("- The group [").append(key).
						append("] contains a circular reference ").append(circList).append(" ");
				inGroupCircularOK = false;
				logger.error(inGroupCircularErrorMessage.toString());
				throw new XmlImportException(inGroupCircularErrorMessage.toString());
			}
		}

		// dont display all the circular detection, thats why the exception is raised above.
		// the code below is useless as long as the exception is raised above.
		if (!inGroupCircularOK) {
			logger.error(inGroupCircularErrorMessage.toString());
			throw new XmlImportException(inGroupCircularErrorMessage.toString());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("No error detected during the XML import");
		}
		return results;
	}

	/**
	 * @param groupName
	 * @return true if the groupName exist in the groupList of the attribute mSite
	 */
	private boolean isExistingGroup(String groupName) {
		boolean existingGroup = false;
		Group currentGroup = null;
		Iterator groupIter = mSite.getGroupList().iterator();
		while (!existingGroup && groupIter.hasNext()) {
			currentGroup = (Group) groupIter.next();
			if (groupName.equals(currentGroup.getName())) {
				existingGroup = true;
			}
		}
		return existingGroup;
	}

	public void updateResourceHistory(String changeOwner, String comment, String dataChange)
			throws Exception {

		byte[] backupXML = getBackupFile();
		new ResourceHistoryManager().updateResourceHistory(changeOwner, comment, dataChange,
				mSite.getId(), backupXML, ResourceHistory.USERGROUP_MODIFICATION);
	}

	private byte[] getBackupFile() throws Exception {
		Document backupDocument = getDefinitionsOfAllGroups();
		return XmlUtils.xmlToByteArray(backupDocument.getDocumentElement());
	}

	public Document getDefinitionsOfGroups(List GroupNameList)
			throws ParserConfigurationException, SQLException, SAXException, IOException {

		Document siteDocument = new DocumentImpl();
		Element siteElement = siteDocument.createElement("Site");
		siteElement.setAttribute("name", mSite.getName());
		siteDocument.appendChild(siteElement);

		Node groupNode;

		if (GroupNameList != null && !GroupNameList.isEmpty()) {

			for (int i = 0; i < GroupNameList.size(); i++) {
				Set s = mSite.getGroupList();
				Iterator it = s.iterator();
				while (it.hasNext()) {
					Group g = (Group) it.next();
					if (g.getName().equals(GroupNameList.get(i))) {
						groupNode = addGroupElement(siteDocument, g);
						siteElement.appendChild(groupNode);
						break;
					}

				}
			}

		} else {
			Set s = mSite.getGroupList();
			Iterator it = s.iterator();
			while (it.hasNext()) {
				Group g = (Group) it.next();
				groupNode = addGroupElement(siteDocument, g);
				siteElement.appendChild(groupNode);
			}
		}

		return siteDocument;
	}

	public Document getDefinitionsOfAllGroups() throws ParserConfigurationException,
			SQLException, SAXException, IOException {

		List GroupNameList = new ArrayList();
		Document siteDocument = getDefinitionsOfGroups(GroupNameList);
		return siteDocument;
	}

	public Node addGroupElement(Document siteDocument, Group group)
			throws ParserConfigurationException, SQLException, SAXException, IOException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		ByteArrayInputStream inputStream = null;
		Document groupDocument = null;
		try{
			inputStream = new ByteArrayInputStream(group.getRules());
			groupDocument = docBuilder.parse(inputStream);
		}
		finally {
			inputStream.close();
		}

		Node groupElement = siteDocument.importNode(groupDocument.getDocumentElement(), true);

		return groupElement;
	}

	/**
	 * This method is used to get the InGroup expression stored in the group. It returns the
	 * list of groups name refrecences by the group passed as parameter. The parameter can be
	 * an instance of Element (XML element), in this case, the inGroup Expression must be read
	 * from the XML file or can be a Group object. If this not one of this instance, return an
	 * empty list
	 * 
	 * @param group the group object (as Element ou Group)
	 * @return the list of names of the referecned groups
	 */
	private ArrayList getInGroupReferenceNames(Object group) {
		ArrayList results = new ArrayList();
		if (group instanceof Element) {
			try {
				Element elem = (Element) group;

				Group g = new Group();

				g.setRules(XmlUtils.xmlToByteArray(elem));
				g.unmarshallXml();
				if (g.getExpression() != null) {
					g.getExpression().getInGroupNames(results);
				}
			} catch (Exception e) {
				logger.warn("Unable to read the XML file to detect the inGroup");
			}
		} else if (group instanceof Group) {
			Group g = (Group) group;
			if (g.getExpression() != null) {
				g.getExpression().getInGroupNames(results);
			}
		} else {
			logger.warn("The instance of the group object is not correct, it s a ["
					+ group.getClass() + "]");
		}

		return results;
	}

	/**
	 * Recursive method to detect the circular references in the inGroup definitions
	 * 
	 * @param groupName name at which we start to create the list of child ingroup
	 * @param inGroupMap map that contains the list of inGroup for each group
	 * @param list list of child ingroup
	 * @return true if a circular ref has been detected, false otherwise
	 */
	private static boolean detectCircularGroupReferences(String groupName, HashMap inGroupMap,
			ArrayList list) {
		// browse all the InGroup referenced for the groupname given as parameter,
		// as soon as a Ingroup ref is detected, add it in the list
		// by analysing this list, if a group exists more than once, there is a circular ref :
		// ERROR must be raised
		ArrayList inGroupRefList = (ArrayList) inGroupMap.get(groupName);
		for (int i = 0; i < inGroupRefList.size(); i++) {
			String inGroupRefName = (String) inGroupRefList.get(i);
			if (!list.contains(inGroupRefName)) {
				list.add(inGroupRefName);
				return SiteManager.detectCircularGroupReferences(inGroupRefName, inGroupMap,
						list);
			} else {
				list.add(inGroupRefName);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to check that the groups that will be deleted are not referenced buy any other
	 * groups. If an error is detected, return an error message. If no error, return null
	 * 
	 * @param groupNameToDeleteList the list of group names that will be deleted
	 * @return the error message if an error is detected, null if no error
	 */
	public String checkGroupsToDelete(List groupNameToDeleteList) {
		// browse all groups of the site and for each, get all the referenced group.
		// if one of the group to delete is referenced, return the error message
		Iterator it = mSite.getGroupList().iterator();
		while (it.hasNext()) {
			Group g = (Group) it.next();
			ArrayList inGroupList = getInGroupReferenceNames(g);
			// particular case : we dont control the groups that will be deleted
			if (!groupNameToDeleteList.contains(g.getName())) {
				for (int i = 0; i < groupNameToDeleteList.size(); i++) {
					String groupNameToDelete = (String) groupNameToDeleteList.get(i);
					if (inGroupList.contains(groupNameToDelete)) {
						String errorMessage = "The group ["
								+ groupNameToDelete
								+ "] can not be deleted, because, it is referenced by the group ["
								+ g.getName() + "]";
						return errorMessage;
					}
				}
			}
		}
		return null;
	}

}
