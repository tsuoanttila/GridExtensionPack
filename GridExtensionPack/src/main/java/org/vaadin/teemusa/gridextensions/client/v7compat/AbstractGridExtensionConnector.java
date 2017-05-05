package org.vaadin.teemusa.gridextensions.client.v7compat;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.v7.client.connectors.GridConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.v7.client.widgets.Grid;
import com.vaadin.v7.client.widgets.Grid.Column;
import com.vaadin.shared.MouseEventDetails;

import elemental.json.JsonObject;

/**
 * Generic abstract base class for providing some helpers for Grid extension
 * connectors.
 * 
 * @author Teemu Suo-Anttila
 */
@Deprecated
public abstract class AbstractGridExtensionConnector extends AbstractExtensionConnector {

	/**
	 * Gets the parent connector as GridConnector.
	 * 
	 * @return grid connector
	 */
	public GridConnector getParent() {
		return (GridConnector) super.getParent();
	}

	/**
	 * Gets the Grid widget from parent connector.
	 * 
	 * @return grid
	 */
	public Grid<JsonObject> getGrid() {
		return getParent().getWidget();
	}

	/**
	 * Gets a row key from parent connector for mapping client-side rows to
	 * server-side items.
	 * 
	 * @param row
	 *            row object
	 * @return row key
	 */
	public String getRowKey(JsonObject row) {
		return getParent().getRowKey(row);
	}

	/**
	 * Gets a column id from parent connector for mapping client-side columns to
	 * server-side columns.
	 * 
	 * @param column
	 *            grid column object
	 * @return column id
	 */
	public String getColumnId(Column<?, JsonObject> column) {
		return getParent().getColumnId(column);
	}

	/**
	 * Gets a {@link MouseEventDetails} object from native mouse event.
	 * 
	 * @param event
	 *            native mouse event
	 * @return mouse event details
	 */
	protected MouseEventDetails getMouseEventDetails(NativeEvent event) {
		return MouseEventDetailsBuilder.buildMouseEventDetails(event);
	}
}
