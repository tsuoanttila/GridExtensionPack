package org.vaadin.teemusa.gridextensions.client.v7compat.tableselection;

import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.WidgetUtil;
import com.vaadin.v7.client.widget.grid.CellReference;
import com.vaadin.v7.client.widget.grid.DataAvailableEvent;
import com.vaadin.v7.client.widget.grid.DataAvailableHandler;
import com.vaadin.v7.client.widget.grid.events.GridClickEvent;
import com.vaadin.v7.client.widget.grid.selection.SelectionModel.Multi;
import com.vaadin.v7.client.widget.grid.selection.SelectionModel.Multi.Batched;
import com.vaadin.v7.client.widgets.Grid;
import com.vaadin.shared.Range;

import elemental.json.JsonObject;

@Deprecated
public class ShiftCtrlClickSelectionHandler extends CtrlClickSelectionHandler {

	private final class ShiftSelector implements DataAvailableHandler {
		private final CellReference<JsonObject> cell;
		private final Multi<JsonObject> model;
		private boolean ctrlOrMeta;

		private ShiftSelector(CellReference<JsonObject> cell, Multi<JsonObject> model, boolean ctrlOrMeta) {
			this.cell = cell;
			this.model = model;
			this.ctrlOrMeta = ctrlOrMeta;
		}

		@Override
		public void onDataAvailable(DataAvailableEvent event) {

			// Perform shift selection

			int current = cell.getRowIndex();
			int min = Math.min(current, previous);
			int max = Math.max(current, previous);

			Batched<JsonObject> batched = null;
			if (model instanceof Batched) {
				batched = (Batched<JsonObject>) model;
				batched.startBatchSelect();
			}

			if (!ctrlOrMeta) {
				model.deselectAll();
			}

			Range dataAvailable = event.getAvailableRows();

			Range selected = Range.between(min, max + 1);
			Range[] partition = selected.partitionWith(dataAvailable);

			for (int i = partition[1].getStart(); i < partition[1].getEnd(); ++i) {
				model.select(grid.getDataSource().getRow(i));
			}

			if (batched != null) {
				batched.commitBatchSelect();
			}

			rpc.selectRange(partition[0].getStart(), partition[0].length());
			rpc.selectRange(partition[2].getStart(), partition[2].length());

			if (handler != null) {
				handler.removeHandler();
			}

			WidgetUtil.setTextSelectionEnabled(grid.getElement(), true);
		}
	}

	private int previous = -1;
	private HandlerRegistration handler;
	private ShiftSelectRpc rpc;

	public ShiftCtrlClickSelectionHandler(Grid<JsonObject> grid, ShiftSelectRpc rpc) {
		super(grid);
		this.rpc = rpc;
	}

	@Override
	protected void ctrlClickSelect(Multi<JsonObject> model, CellReference<JsonObject> cell, GridClickEvent e) {
		// Plain control click, or no previously selected.
		if (!e.isShiftKeyDown() || previous < 0) {
			super.ctrlClickSelect(model, cell, e);
			previous = cell.getRowIndex();
			return;
		}

		// Stop selecting text for now.
		WidgetUtil.setTextSelectionEnabled(grid.getElement(), false);
		WidgetUtil.clearTextSelection();

		// This works on the premise that grid fires the data available event to
		// any newly added handlers.
		boolean ctrlOrMeta = e.isControlKeyDown() || e.isMetaKeyDown();
		handler = grid.addDataAvailableHandler(new ShiftSelector(cell, model, ctrlOrMeta));
	}
}