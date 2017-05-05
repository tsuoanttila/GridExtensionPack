package org.vaadin.teemusa.gridextensions.client.v7compat.tableselection;

import org.vaadin.teemusa.gridextensions.v7compat.tableselection.TableSelectionModel;

import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.connectors.MultiSelectionModelConnector;
import com.vaadin.v7.client.renderers.ComplexRenderer;
import com.vaadin.v7.client.widget.grid.events.BodyClickHandler;
import com.vaadin.v7.client.widget.grid.selection.SelectionModel.Multi;
import com.vaadin.v7.client.widgets.Grid;

import elemental.json.JsonObject;

@Connect(TableSelectionModel.class)
@Deprecated
public class TableSelectionModelConnector extends MultiSelectionModelConnector {

	private HandlerRegistration clickHandler;

	@Override
	protected Multi<JsonObject> createSelectionModel() {
		return new MultiSelectionModel() {

			@Override
			protected ComplexRenderer<Boolean> createSelectionColumnRenderer(Grid<JsonObject> grid) {
				return null;
			}
		};
	}

	@Override
	protected void extend(ServerConnector target) {
		super.extend(target);

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
		case SHIFT:
			handler = new ShiftCtrlClickSelectionHandler(grid, getRpcProxy(ShiftSelectRpc.class));
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

	@Override
	public void onUnregister() {
		if (clickHandler != null) {
			clickHandler.removeHandler();
			clickHandler = null;
		}

		super.onUnregister();
	}
}
