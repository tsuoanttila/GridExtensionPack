package org.vaadin.teemusa.gridextensions.client.tableselection;

import com.vaadin.shared.ui.grid.MultiSelectionModelState;

public class TableSelectionState extends MultiSelectionModelState {

	public enum TableSelectionMode {
		NONE, SIMPLE, CTRL, SHIFT
	}

	public TableSelectionMode selectionMode = TableSelectionMode.NONE;

}
