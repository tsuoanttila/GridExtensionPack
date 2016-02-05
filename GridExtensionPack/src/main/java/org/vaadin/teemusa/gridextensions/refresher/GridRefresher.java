package org.vaadin.teemusa.gridextensions.refresher;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.AbstractGridExtension;

public class GridRefresher extends AbstractGridExtension {

	private GridRefresher(Grid grid) {
		super(grid);
	}

	/**
	 * Forces a repaint of given item ID.
	 * 
	 * @param itemId
	 *            item to repaint
	 */
	public void refresh(Object itemId) {
		refreshRow(itemId);
	}

	public static GridRefresher extend(Grid grid) {
		return new GridRefresher(grid);
	}
}
