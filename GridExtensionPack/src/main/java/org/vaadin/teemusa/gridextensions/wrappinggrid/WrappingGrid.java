package org.vaadin.teemusa.gridextensions.wrappinggrid;

import org.vaadin.teemusa.gridextensions.client.wrappinggrid.WrappingClientRPC;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Grid;

@SuppressWarnings("serial")
public class WrappingGrid extends AbstractExtension {

	/**
	 * Constructor to create header wrapping extension for given Grid
	 * 
	 * @param grid The Grid where you want to apply header wrapping to
	 * @return
	 */
	public static WrappingGrid extend(Grid grid) {
		WrappingGrid g = new WrappingGrid();
		g.extend((AbstractClientConnector)grid);
		return g;
	}

	/**
	 * 
	 * @param enable Set header wrapping on/off true = on, false = off
	 */
	public void setWrapping(boolean enable) {
		getRpcProxy(WrappingClientRPC.class).setWrapping(enable);
	}
	
}