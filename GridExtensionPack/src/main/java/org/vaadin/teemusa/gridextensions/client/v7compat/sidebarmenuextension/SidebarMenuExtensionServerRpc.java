package org.vaadin.teemusa.gridextensions.client.v7compat.sidebarmenuextension;

import com.vaadin.shared.communication.ServerRpc;

/**
 * ServerRPC class for SidebarMenuExtension.
 *
 * @author Anna Koskinen
 *
 */
@Deprecated
public interface SidebarMenuExtensionServerRpc extends ServerRpc {

    public void click(Integer id);
}
