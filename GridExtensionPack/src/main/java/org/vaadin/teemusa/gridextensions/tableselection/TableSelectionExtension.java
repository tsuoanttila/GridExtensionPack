package org.vaadin.teemusa.gridextensions.tableselection;

import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;

import com.vaadin.shared.communication.SharedState;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.AbstractGridExtension;
import com.vaadin.ui.Grid.SelectionMode;

/**
 * TableSelectExtension provides Grid selection UX options to make it behave
 * more like Table does.
 * <p>
 * Currently supports Multiple selection modes Simple and Ctrl + Click
 * <p>
 * Do not change Grids SelectionMode while using this extension. Changing the
 * mode will result in unpredictable state.
 * 
 * @author Teemu Suo-Anttila
 */
public class TableSelectionExtension extends AbstractGridExtension {
    private TableSelectionMode mode = TableSelectionMode.NONE;

    private TableSelectionExtension(Grid grid) {
        super(grid);
        grid.setSelectionMode(SelectionMode.MULTI);
    }

    /**
     * Constructs a new TableSelectionExtensions and extend given Grid with it.
     * 
     * @param grid
     *            parent grid for extension
     * @return constructed extension
     */
    public static TableSelectionExtension extend(Grid grid) {
        return new TableSelectionExtension(grid);
    }

    /**
     * Set the TableSelectionMode to use with this extension.
     * 
     * @param mode
     *            table-like selection mode
     */
    public void setMode(TableSelectionMode mode) {
        if (this.mode != mode) {
            getState().selectionMode = mode;
            this.mode = mode;
        }
    }

    @Override
    public Class<? extends SharedState> getStateType() {
        return TableSelectionState.class;
    }

    @Override
    protected TableSelectionState getState() {
        return (TableSelectionState) super.getState();
    }
}
