package org.vaadin.teemusa.gridextensions.v7compat.refresher;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Grid.AbstractGridExtension;

/**
 * This class has been deprecated. To achieve the same functionality, use
 * {@link DataProvider#refreshItem(Object)} instead.
 * 
 * @deprecated since 2.0.0
 */
@Deprecated
public class GridRefresher extends AbstractGridExtension {

	protected GridRefresher(Grid grid) {
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
