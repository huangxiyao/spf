package com.hp.spp.wsrp.export.component.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.hp.spp.wsrp.export.component.BadComponent;
import com.hp.spp.wsrp.export.exception.ComponentException;
import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class Actions extends AbstractComponentElement {

	public static final String DESCRIPTOR = "actions" ;
	public static final boolean FINAL_NODE = false ;
	
	public static final List ATTRIBUTES = Arrays.asList(new Object[] {}) ;
	public static final List MEMBERS = Arrays.asList(
			new Object[] {
					Action.DESCRIPTOR,
					GroupReference.DESCRIPTOR
			}) ;

	private List mActions = new ArrayList() ;
	private GroupReference mGroupReference = null ;
	
//	public Actions() {
//
//	}
	
	Actions(Node node) throws ComponentException, BadConstructorException {
		if(node == null)
			throw new ComponentException(new BadComponent("StyleInfo or TemplateInfo is NULL!"));

		node = DOMUtils.selectSingleNode(node, DESCRIPTOR) ;
		
		if(node == null) {
			throw new BadConstructorException() ;
		} else {
			Map attributes = null ;
			if(!ATTRIBUTES.isEmpty())
				attributes = readAttributes(node, ATTRIBUTES) ;

			Map members = null ;
			if (!MEMBERS.isEmpty()) {
				members = new HashMap();
				for (Iterator it = MEMBERS.iterator(); it.hasNext();) {
					String descriptor = (String) it.next();

					if (descriptor == null || "".equals(descriptor)) {
						// Do nothing...
					} else if (Action.DESCRIPTOR.equals(descriptor)) {
						Object object = DOMUtils.selectNodes(node, Action.DESCRIPTOR) ;
						if(object instanceof List) {
							List tmpList = (List) object ;
							List actionList = null;
							
							Node tmpNode = null ;
							for(Iterator itList = tmpList.iterator(); itList.hasNext(); ) {
								tmpNode = (Node) itList.next() ;
								
								if(tmpNode != null) {
									if(actionList == null)
										actionList = new ArrayList();
									
									try {
										Action action = new Action(tmpNode) ;
										actionList.add(action) ;
									} catch (BadConstructorException e) {
										// Do nothing...
									}
								}
							}
							tmpNode = null ;

							if(actionList != null)
								members.put(descriptor, actionList);
						}
					} else if (GroupReference.DESCRIPTOR.equals(descriptor)) {
						try {
							GroupReference groupReference = new GroupReference(node);
							members.put(descriptor, groupReference);
						} catch (BadConstructorException e) {
							// Do nothing...
						}
					}
				}
			}
			
			if(attributes != null && !attributes.isEmpty() 
					|| members != null && !members.isEmpty()) {
				initAttributes(attributes) ;
				initMembers(members) ;
			} else {
				throw new ComponentException(new BadComponent("Actions attributes or members is NULL!"));
			}
		}
	}

	void initAttributes(Map attributes) {

	}
	
	void initMembers(Map members) {
		if(members != null) {
			for(Iterator iterator = members.entrySet().iterator(); iterator.hasNext(); ) {
				Map.Entry param = (Map.Entry) iterator.next() ;
				String key = (String) param.getKey() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(Action.DESCRIPTOR.equals(key)) {
					addAll((List) param.getValue()) ;
				} else if(GroupReference.DESCRIPTOR.equals(key)) {
					setGroupReference((GroupReference) param.getValue()) ;
				}
			}
		}
	}
	
	public GroupReference getGroupReference() {
		return mGroupReference;
	}

	private void setGroupReference(GroupReference groupReference) {
		mGroupReference = groupReference;
	}

	public boolean add(Action o) {
		return mActions.add(o);
	}

	public void add(int index, Action element) {
		mActions.add(index, element);
	}

	public boolean addAll(Collection c) {
		return mActions.addAll(c);
	}

	public boolean addAll(int index, Collection c) {
		return mActions.addAll(index, c);
	}

	public void clear() {
		mActions.clear();
	}

	public boolean contains(Action o) {
		return mActions.contains(o);
	}

	public boolean containsAll(Collection c) {
		return mActions.containsAll(c);
	}

	public Action get(int index) {
		return (Action) mActions.get(index);
	}

	public int indexOf(Action o) {
		return mActions.indexOf(o);
	}

	public boolean isEmpty() {
		return mActions.isEmpty();
	}

	public Iterator iterator() {
		return mActions.iterator();
	}

	public int lastIndexOf(Action o) {
		return mActions.lastIndexOf(o);
	}

	public ListIterator listIterator() {
		return mActions.listIterator();
	}

	public ListIterator listIterator(int index) {
		return mActions.listIterator(index);
	}

	public Action remove(int index) {
		return (Action) mActions.remove(index);
	}

	public boolean remove(Action o) {
		return mActions.remove(o);
	}

	public boolean removeAll(Collection c) {
		return mActions.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return mActions.retainAll(c);
	}

	public Action set(int index, Action element) {
		return (Action) mActions.set(index, element);
	}

	public int size() {
		return mActions.size();
	}

	public List subList(int fromIndex, int toIndex) {
		return mActions.subList(fromIndex, toIndex);
	}

	public Action[] toArray() {
		Object[] o = mActions.toArray();
		return toCastedArray(o);
	}

	public Action[] toArray(Action[] a) {
		Object[] o = mActions.toArray(a);
		return toCastedArray(o);
	}

	private Action[] toCastedArray(Object[] o) {
		Action[] a = new Action[o.length];
		for(int i = 0; i < o.length; i++)
			a[i] = (Action) o[i];
		return a;
	}

	protected List getActions() {
		return mActions;
	}

	private String getValue() {
		return null;
	}

	public Element toElement(Document doc) {
		Element root = null;
		
		if(FINAL_NODE) {
			root = DOMUtils.createTextNode(doc, DESCRIPTOR, getValue()) ;
		} else {
			root = doc.createElement(DESCRIPTOR);
			
			for(Iterator it = ATTRIBUTES.iterator(); it.hasNext(); ) {
				// Do nothing...
			}
			
			for(Iterator it = MEMBERS.iterator(); it.hasNext(); ) {
				String key = (String) it.next() ;
				
				if(key == null || "".equals(key)) {
					// Do nothing...
				} else if(Action.DESCRIPTOR.equals(key)) {
					List actions = getActions() ;
					for(Iterator it2 = actions.iterator(); it2.hasNext(); ) {
						Action action = (Action) it2.next();
						if(action != null)
							root.appendChild(action.toElement(doc)) ;
					}
				} else if(GroupReference.DESCRIPTOR.equals(key)) {
					GroupReference groupReference = getGroupReference() ;
					if(groupReference != null)
						root.appendChild(groupReference.toElement(doc)) ;
				}
			}
		}
		
		return root;
	}
}
