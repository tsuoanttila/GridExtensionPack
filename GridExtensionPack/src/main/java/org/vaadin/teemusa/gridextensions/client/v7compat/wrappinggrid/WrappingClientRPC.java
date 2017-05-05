package org.vaadin.teemusa.gridextensions.client.v7compat.wrappinggrid;

import com.vaadin.shared.communication.ClientRpc;

@Deprecated
public interface WrappingClientRPC extends ClientRpc  {

	public void setWrapping(boolean enable, int defaultRowHeight);
	
}
