package org.vaadin.teemusa.gridextensions.client.v7compat.tableselection;

import com.vaadin.v7.shared.ui.grid.selection.MultiSelectionModelState;

@Deprecated
public class TableSelectionState extends MultiSelectionModelState {

	public enum TableSelectionMode {
		NONE, SIMPLE, CTRL, SHIFT
	}

	public TableSelectionMode selectionMode = TableSelectionMode.NONE;

}
