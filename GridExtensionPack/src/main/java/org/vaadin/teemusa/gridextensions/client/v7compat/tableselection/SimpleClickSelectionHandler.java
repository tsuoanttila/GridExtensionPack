package org.vaadin.teemusa.gridextensions.client.v7compat.tableselection;

import com.vaadin.v7.client.widget.grid.events.BodyClickHandler;
import com.vaadin.v7.client.widget.grid.events.GridClickEvent;
import com.vaadin.v7.client.widgets.Grid;

import elemental.json.JsonObject;

@Deprecated
public class SimpleClickSelectionHandler implements BodyClickHandler {

	private Grid<JsonObject> grid;

	public SimpleClickSelectionHandler(Grid<JsonObject> grid) {
		this.grid = grid;
	}

	@Override
	public void onClick(GridClickEvent event) {
		JsonObject row = grid.getEventCell().getRow();
		if (!grid.isSelected(row)) {
			grid.select(row);
		} else {
			grid.deselect(row);
		}
	}

}
