package org.vaadin.teemusa.gridextensions.client.v7compat.tableselection;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.v7.client.widget.grid.CellReference;
import com.vaadin.v7.client.widget.grid.events.BodyClickHandler;
import com.vaadin.v7.client.widget.grid.events.GridClickEvent;
import com.vaadin.v7.client.widget.grid.selection.SelectionModel;
import com.vaadin.v7.client.widget.grid.selection.SelectionModel.Multi;
import com.vaadin.v7.client.widgets.Grid;

import elemental.json.JsonObject;

@Deprecated
public class CtrlClickSelectionHandler implements BodyClickHandler {

	protected Grid<JsonObject> grid;

	public CtrlClickSelectionHandler(Grid<JsonObject> grid) {
		this.grid = grid;
	}

	@Override
	public void onClick(GridClickEvent event) {
		SelectionModel<JsonObject> selectionModel = grid.getSelectionModel();
		if (!(selectionModel instanceof Multi)) {
			// Not multiselecting.
			return;
		}

		Multi<JsonObject> model = (Multi<JsonObject>) selectionModel;
		CellReference<JsonObject> cell = grid.getEventCell();

		ctrlClickSelect(model, cell, event);
	}

	protected void ctrlClickSelect(Multi<JsonObject> model, CellReference<JsonObject> cell, GridClickEvent event) {
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
