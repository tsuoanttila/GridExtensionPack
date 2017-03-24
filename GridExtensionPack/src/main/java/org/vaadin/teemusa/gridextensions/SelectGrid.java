
package org.vaadin.teemusa.gridextensions;

import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.GridSelectionModel;

/**
 * Grid Wrapper that exposes selection model setting API
 */
public class SelectGrid<T> extends Grid<T> {

	@Override
	public void setSelectionModel(GridSelectionModel<T> model) {
		super.setSelectionModel(model);
	}
}
