package org.vaadin.teemusa.gridextensions.tableselection;

import org.vaadin.teemusa.gridextensions.client.tableselection.ShiftSelectRpc;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.MultiSelectionModel;
import com.vaadin.ui.Grid.SelectionModel;

/**
 * TableSelectModel provides {@link Grid} selection UX options to make it behave
 * more like Table does.
 * <p>
 * Currently supports Multiple selection modes Simple and Ctrl + Click
 * <p>
 * This is a SelectionModel for Grid, use
 * {@link Grid#setSelectionModel(SelectionModel)} to take it into use.
 * 
 * @author Teemu Suo-Anttila
 */
public class TableSelectionModel extends MultiSelectionModel {

	/**
	 * Set the TableSelectionMode to use with this extension.
	 * 
	 * @param mode
	 *            table-like selection mode
	 */
	public void setMode(TableSelectionMode mode) {
		if (getState(false).selectionMode != mode) {
			getState().selectionMode = mode;
		}
	}

	@Override
	protected TableSelectionState getState() {
		return getState(true);
	}

	@Override
	protected TableSelectionState getState(boolean markAsDirty) {
		return (TableSelectionState) super.getState(markAsDirty);
	}

	@Override
	protected void extend(AbstractClientConnector target) {
		super.extend(target);
		registerRpc(new ShiftSelectRpc() {

			@Override
			public void selectRange(int start, int length) {
				if (length == 0) {
					return;
				}

				select(getParentGrid().getContainerDataSource().getItemIds(start, length), false);
			}
		});
	}
}
