package org.vaadin.teemusa.gridextensions.client.tableselection;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.widget.grid.CellReference;
import com.vaadin.client.widget.grid.events.BodyClickHandler;
import com.vaadin.client.widget.grid.events.GridClickEvent;
import com.vaadin.client.widget.grid.selection.SelectionModel;
import com.vaadin.client.widgets.Grid;

import elemental.json.JsonObject;

public class CtrlClickSelectionHandler implements BodyClickHandler {

	protected Grid<JsonObject> grid;

	public CtrlClickSelectionHandler(Grid<JsonObject> grid) {
		this.grid = grid;
	}

	@Override
	public void onClick(GridClickEvent event) {
		SelectionModel<JsonObject> model = grid.getSelectionModel();
		CellReference<JsonObject> cell = grid.getEventCell();

		ctrlClickSelect(model, cell, event);
	}

	protected void ctrlClickSelect(SelectionModel<JsonObject> model, CellReference<JsonObject> cell,
			GridClickEvent event) {
		NativeEvent e = event.getNativeEvent();
		JsonObject row = cell.getRow();
		if (!e.getCtrlKey() && !e.getMetaKey()) {
			model.deselectAll();
		}

		if (model.isSelected(row)) {
			model.deselect(row);
		} else {
			model.select(row);
		}
	}
}
