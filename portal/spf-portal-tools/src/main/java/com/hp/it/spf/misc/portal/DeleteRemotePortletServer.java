package com.hp.it.spf.misc.portal;

import com.vignette.portal.portlet.management.external.PortletException;
import com.vignette.portal.portlet.management.external.PortletPersistenceException;
import com.vignette.portal.portlet.management.external.extension.wsrp.ProducerNotReachableException;
import com.vignette.portal.portlet.management.external.extension.wsrp.ProducerOperationFailedException;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.MissingRegistrationParametersException;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.WsrpException;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.WsrpPortletApplicationManagerSpiImpl;
import com.vignette.portal.portlet.management.internal.implementation.provider.wsrp.WsrpPortletApplicationSpiImpl;

import java.sql.Timestamp;


/**
 * Command line tool to delete remote portlet server from Vignette Portal without removing the WSRP registration entry
 * from Producer. Run this class without any parameters to see its usage.
 *
 * @author Ye.Liu (HPIT-GADSC) (ye.liu@hp.com)
 */
public class DeleteRemotePortletServer {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.printf("java [options] %s %s%n", DeleteRemotePortletServer.class.getName(),
					"\"Remote-Portlet-Server Import ID\"");
			System.exit(0);
		}

		String importId = args[0];

		try {
			DeleteRemotePortletServer tool = new DeleteRemotePortletServer();
			tool.deleteRemotePortletServer(importId);
			System.out.printf("Successfully deleted the remote portlet server: " + importId);
		}
		catch (Exception e) {
			System.err.printf("Error occurred while deleting remote portlet server: %s%n", e);
			e.printStackTrace();
			System.exit(3);
		}
	}


	public void deleteRemotePortletServer(String importId) throws PortletException {
		WsrpPortletApplicationManagerSpiImpl wsrpPortletApplicationManager =
				WsrpPortletApplicationManagerSpiImpl.getInstance();

		WsrpPortletApplicationSpiImpl existingRemoteServer =
				wsrpPortletApplicationManager.getWsrpPortletApplicationByFriendlyID(importId);

		SPFWsrpPortletApplicationSpiImpl spfWsrpPortletApplicationSpi = new SPFWsrpPortletApplicationSpiImpl(existingRemoteServer);

		deleteRemotePortletServerFromVignette(spfWsrpPortletApplicationSpi);
	}


	private void deleteRemotePortletServerFromVignette(SPFWsrpPortletApplicationSpiImpl remotePortletServer)
			throws PortletException {
		remotePortletServer.deleteImpl();
	}


	private static class SPFWsrpPortletApplicationSpiImpl extends WsrpPortletApplicationSpiImpl {
		public SPFWsrpPortletApplicationSpiImpl(WsrpPortletApplicationSpiImpl wsrpPortletApplicationSpi) throws ProducerOperationFailedException,
				MissingRegistrationParametersException, PortletPersistenceException, ProducerNotReachableException {
			super(wsrpPortletApplicationSpi.getTitle(), wsrpPortletApplicationSpi.getDescription(),
					wsrpPortletApplicationSpi.getRegistrationHandle(), wsrpPortletApplicationSpi.getRegistrationProperties(),
					wsrpPortletApplicationSpi.getProducerType(), wsrpPortletApplicationSpi.getCookieInit(), wsrpPortletApplicationSpi.getProducerDataHolder(),
					wsrpPortletApplicationSpi.getFriendlyID(), wsrpPortletApplicationSpi.getUID(), new Timestamp(System.currentTimeMillis()));
		}

		@Override
		public void deregister() throws WsrpException {
			// Override this method and do nothing so that the WSRP registration entry will not
			// be removed from Producer database.
		}
	}
}
