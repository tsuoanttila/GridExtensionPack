package org.vaadin.teemusa.gridextensions.client.tableselection;

import com.vaadin.shared.ui.grid.MultiSelectionModelState;

public class TableSelectionState extends MultiSelectionModelState {

	public enum TableSelectionMode {
		NONE, SIMPLE, CTRL, SHIFT, SINGLE
	}

	public TableSelectionMode selectionMode = TableSelectionMode.NONE;

}
