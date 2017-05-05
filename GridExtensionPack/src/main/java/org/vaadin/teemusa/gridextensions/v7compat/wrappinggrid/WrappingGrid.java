package org.vaadin.teemusa.gridextensions.v7compat.wrappinggrid;

import org.vaadin.teemusa.gridextensions.client.v7compat.wrappinggrid.WrappingClientRPC;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.v7.ui.Grid;

/**
 * @deprecated Replaced by
 *             {@link org.vaadin.teemusa.gridextensions.wrappinggrid.WrappingGrid}
 */
@SuppressWarnings("serial")
@Deprecated
public class WrappingGrid extends AbstractExtension {

	static int defaultRowHeight = 38;

	/**
	 * Constructor to create header wrapping extension for given Grid, uses
	 * header row height of the Valo theme
	 * 
	 * @param grid
	 *            The Grid where you want to apply header wrapping to
	 * @return the extension instance
	 */
	public static WrappingGrid extend(Grid grid) {
		WrappingGrid g = new WrappingGrid();
		g.extend((AbstractClientConnector) grid);
		return g;
	}

	/**
	 * Constructor to create header wrapping extension for given Grid
	 * 
	 * @param grid
	 *            The Grid where you want to apply header wrapping to
	 * @param newDefaultRowHeight
	 *            Header row height of the theme in px, e.g. Valo = 38, Reindeer
	 *            = 21
	 * @return the extension instance
	 */
	public static WrappingGrid extend(Grid grid, int newDefaultRowHeight) {
		WrappingGrid g = new WrappingGrid();
		defaultRowHeight = newDefaultRowHeight;
		g.extend((AbstractClientConnector) grid);
		return g;
	}

	/**
	 * Enables or disables wrapping of the header.
	 * 
	 * @param enable
	 *            Set header wrapping on/off true = on, false = off
	 */
	public void setWrapping(boolean enable) {
		getRpcProxy(WrappingClientRPC.class).setWrapping(enable, defaultRowHeight);
	}

}