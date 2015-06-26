package org.vaadin.teemusa.gridextensions.demo;

import org.vaadin.peter.contextmenu.ContextMenu;

import com.vaadin.data.sort.Sort;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;

public class GridContextMenu extends ContextMenu {

    private Object itemId;
    private Column column;

    public GridContextMenu(final Grid grid) {
        setParent(grid);

        ContextMenuItem rowMenu = addItem("Rows");
        rowMenu.addItem("Edit row").addItemClickListener(
                new ContextMenuItemClickListener() {

                    @Override
                    public void contextMenuItemClicked(
                            ContextMenuItemClickEvent event) {
                        if (grid.isEditorEnabled()) {
                            grid.editItem(itemId);
                        }
                    }
                });
        rowMenu.addItem("Delete row").addItemClickListener(
                new ContextMenuItemClickListener() {

                    @Override
                    public void contextMenuItemClicked(
                            ContextMenuItemClickEvent event) {
                        grid.getContainerDataSource().removeItem(itemId);
                    }
                });

        ContextMenuItem columnMenu = addItem("Columns");
        columnMenu.addItem("Hide").addItemClickListener(
                new ContextMenuItemClickListener() {

                    @Override
                    public void contextMenuItemClicked(
                            ContextMenuItemClickEvent event) {
                        if (column.isHidable()) {
                            column.setHidden(true);
                        }
                    }
                });
        columnMenu.addItem("Sort").addItemClickListener(
                new ContextMenuItemClickListener() {

                    @Override
                    public void contextMenuItemClicked(
                            ContextMenuItemClickEvent event) {
                        if (column.isSortable()) {
                            grid.sort(Sort.by(column.getPropertyId()));
                        }
                    }
                });
    }

    /**
     * @param itemId
     *            the itemId to set
     */
    public void setItemId(Object itemId) {
        this.itemId = itemId;
    }

    /**
     * @param column
     *            the column to set
     */
    public void setColumn(Column column) {
        this.column = column;
    }
}
