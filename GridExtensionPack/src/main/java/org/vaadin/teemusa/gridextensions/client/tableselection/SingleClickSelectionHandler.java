package org.vaadin.teemusa.gridextensions.client.tableselection;

import com.vaadin.client.widget.grid.events.BodyClickHandler;
import com.vaadin.client.widget.grid.events.GridClickEvent;
import com.vaadin.client.widgets.Grid;

import elemental.json.JsonObject;

public class SingleClickSelectionHandler implements BodyClickHandler {

	protected Grid<JsonObject> grid;
	protected JsonObject currentSelectedRow;

	public SingleClickSelectionHandler(Grid<JsonObject> grid) {
		this.grid = grid;
	}

	@Override
	public void onClick(GridClickEvent event) {
		JsonObject row = this.grid.getEventCell().getRow();
		
		if(this.grid.isSelected(row)) {
			this.grid.deselectAll();
		} else {
			this.grid.deselectAll();
			this.grid.select(row);
		}
	}
}