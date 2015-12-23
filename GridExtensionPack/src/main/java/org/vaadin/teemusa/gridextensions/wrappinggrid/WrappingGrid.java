package org.vaadin.teemusa.gridextensions.wrappinggrid;

import org.vaadin.teemusa.gridextensions.client.wrappinggrid.WrappingClientRPC;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Grid;

@SuppressWarnings("serial")
public class WrappingGrid extends AbstractExtension {
	
	public static WrappingGrid extend(Grid grid) {
		WrappingGrid g = new WrappingGrid();
		g.extend((AbstractClientConnector)grid);
		return g;
	}
	
	public void setWrapping(boolean enable) {
		getRpcProxy(WrappingClientRPC.class).setWrapping(enable);
	}
	
}