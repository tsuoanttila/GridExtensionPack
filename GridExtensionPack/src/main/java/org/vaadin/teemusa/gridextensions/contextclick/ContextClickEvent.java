package org.vaadin.teemusa.gridextensions.contextclick;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;

/**
 * A class representing a context click in the body of Grid.
 * 
 * @author Teemu Suo-Anttila
 */
public class ContextClickEvent extends ItemClickEvent {

	ContextClickEvent(Grid source, Object itemId, Column column, MouseEventDetails details) {
		super(source, source.getContainerDataSource().getItem(itemId), itemId,
				column != null ? column.getPropertyId() : null, details);
	}

	/**
	 * Gets the column that was clicked.
	 * 
	 * @return column of Grid
	 */
	public Column getColumn() {
		return getSource().getColumn(getPropertyId());
	}

	/**
	 * Gets the source Grid.
	 * 
	 * @return Grid
	 */
	public Grid getSource() {
		return (Grid) super.getSource();
	}
}
