package org.vaadin.teemusa.gridextensions.client.tableselection;

import org.vaadin.teemusa.gridextensions.tableselection.TableSelectionModel;

import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.connectors.grid.MultiSelectionModelConnector;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.events.BodyClickHandler;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.Range;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(TableSelectionModel.class)
public class TableSelectionModelConnector extends MultiSelectionModelConnector {

	protected class CustomSelectionModel extends MultiSelectionModel {

		@Override
		public Renderer<Boolean> getRenderer() {
			return null;
		}

	}

	private HandlerRegistration clickHandler;

	@Override
	protected void initSelectionModel() {
		super.initSelectionModel();

		getGrid().setSelectionModel(new CustomSelectionModel());
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
