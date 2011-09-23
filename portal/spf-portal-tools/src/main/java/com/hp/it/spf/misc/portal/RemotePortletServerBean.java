package com.hp.it.spf.misc.portal;

/**
 * @author Xu, Ke-Jun (Daniel,HPIT-GADSC) (ke-jun.xu@hp.com)
 */
public class RemotePortletServerBean
{
    private String title;
    private String description;
    private String friendlyID;
    private String serviceDescriptionEndpoint;

    /**
     * @param title
     *            the title of remote portlet server
     * @param description
     *            the description of remote portlet server
     * @param friendlyID
     *            the friendlyID of remote portlet server
     * @param serviceDescriptionEndpoint
     *            the url of remote portlet server
     */
    public RemotePortletServerBean(String title, String description, String friendlyID,
            String serviceDescriptionEndpoint)
    {
        if (title == null || title.trim().equals("")) {
            throw new IllegalArgumentException("Remote server title (name) cannot be null or empty");
        }
        if (friendlyID == null || friendlyID.trim().equals("")) {
            throw new IllegalArgumentException("Remote server friendlyID (import ID) cannot be null or empty");
        }
        if (serviceDescriptionEndpoint == null || serviceDescriptionEndpoint.trim().equals("")) {
            throw new IllegalArgumentException("Remote server service description endpoint (producer URL) cannot be null or empty");
        }

        this.title = title;
        this.description = description;
        this.friendlyID = friendlyID;
        this.serviceDescriptionEndpoint = serviceDescriptionEndpoint;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description of remote portlet server
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the friendlyID of remote portlet server
     */
    public String getFriendlyID() {
        return friendlyID;
    }

    /**
     * @return serviceDescriptionEndpoint the serviceDescriptionEndpoint of remote portlet server
     */
    public String getServiceDescriptionEndpoint() {
        return serviceDescriptionEndpoint;
    }

    @Override
    public String toString() {
        return (" title = [" + title + "] ; friendlyID = [" + friendlyID + "] ; description =[" + description
                + "] ; serviceDescriptionEndpoint = [" + serviceDescriptionEndpoint + "]");
    }
}
