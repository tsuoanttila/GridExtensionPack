package org.vaadin.teemusa.gridextensions.client.tableselection;

import com.google.gwt.dom.client.NativeEvent;
import com.vaadin.client.widget.grid.CellReference;
import com.vaadin.client.widget.grid.events.BodyClickHandler;
import com.vaadin.client.widget.grid.events.GridClickEvent;
import com.vaadin.client.widget.grid.selection.SelectionModel;
import com.vaadin.client.widget.grid.selection.SelectionModel.Multi;
import com.vaadin.client.widgets.Grid;

import elemental.json.JsonObject;

public class CtrlClickSelectionHandler implements BodyClickHandler {

    private Grid<JsonObject> grid;

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
        JsonObject row = cell.getRow();

        NativeEvent e = event.getNativeEvent();
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
