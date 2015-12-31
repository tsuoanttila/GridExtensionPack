package org.vaadin.teemusa.gridextensions.client.tableselection;

import com.vaadin.shared.ui.grid.selection.MultiSelectionModelState;

public class TableSelectionState extends MultiSelectionModelState {

	public enum TableSelectionMode {
		NONE, SIMPLE, CTRL
	}

	public TableSelectionMode selectionMode = TableSelectionMode.NONE;

}
