package org.vaadin.teemusa.gridextensions.client.v7compat.sidebarmenuextension;

import com.vaadin.shared.communication.ClientRpc;

@Deprecated
public interface SidebarMenuExtensionClientRpc extends ClientRpc {

	/**
	 * Tells the connector to open the sidebar menu of extended grid.
	 */
	void openSidebarMenu();

	/**
	 * Tells the connector to close the sidebar menu of extended grid.
	 */
	void closeSidebarMenu();
}
