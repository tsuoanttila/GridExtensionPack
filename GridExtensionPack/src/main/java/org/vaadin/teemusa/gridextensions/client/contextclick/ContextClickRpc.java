package org.vaadin.teemusa.gridextensions.client.contextclick;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

/**
 * Client to Server RPC interface for informing of context menu events.
 * 
 * @author Teemu Suo-Anttila
 */
public interface ContextClickRpc extends ServerRpc {

    public final static String CONTEXT_CLICK_EVENT_ID = "contextClick";

    public void contextClick(String rowKey, String columnId,
            MouseEventDetails mouseDetails);
}
