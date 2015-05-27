package org.vaadin.teemusa.gridextensions.client.tableselection;

import com.vaadin.shared.communication.SharedState;

public class TableSelectionState extends SharedState {

    public enum TableSelectionMode {
        NONE, SIMPLE, CTRL
    }

    public TableSelectionMode selectionMode = TableSelectionMode.NONE;

}
