package org.vaadin.teemusa.gridextensions.client.wrappinggrid;

import com.vaadin.shared.communication.ClientRpc;

public interface WrappingClientRPC extends ClientRpc  {

	public void enableWrapping();
	
	public void disableWrapping();
	
}
