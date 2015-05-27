package org.vaadin.teemusa.gridextensions.client.tableselection;

import org.vaadin.teemusa.gridextensions.client.AbstractGridExtensionConnector;
import org.vaadin.teemusa.gridextensions.tableselection.TableSelectionExtension;

import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.widget.grid.events.BodyClickHandler;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(TableSelectionExtension.class)
public class TableSelectionExtensionConnector extends
        AbstractGridExtensionConnector {

    private HandlerRegistration clickHandler;

    @Override
    protected void extend(ServerConnector target) {
        setSelectionMode();
    }

    @OnStateChange("selectionMode")
    void setSelectionMode() {
        if (clickHandler != null) {
            clickHandler.removeHandler();
            clickHandler = null;
        }

        BodyClickHandler handler;
        Grid<JsonObject> grid = getGrid();
        switch (getState().selectionMode) {
        case CTRL:
            handler = new CtrlClickSelectionHandler(grid);
            break;
        case SIMPLE:
            handler = new SimpleClickSelectionHandler(grid);
            break;
        case NONE:
        default:
            return;
        }

        clickHandler = grid.addBodyClickHandler(handler);
    }

    public TableSelectionState getState() {
        return (TableSelectionState) super.getState();
    }
}
