package org.vaadin.teemusa.gridextensions.demo;

import java.util.Random;

import org.vaadin.teemusa.gridextensions.cachestrategy.CacheStrategyExtension;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;
import org.vaadin.teemusa.gridextensions.tableselection.TableSelectionModel;
import org.vaadin.teemusa.gridextensions.wrappinggrid.WrappingGrid;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

public class HeaderWrapExtensionLayout extends VerticalLayout {

	private static final String BUTTON_WRAPPING_ENABLED_TEXT = "Turn wrapping off";
	private static final String BUTTON_WRAPPING_DISABLED_TEXT = "Turn wrapping on";

	public HeaderWrapExtensionLayout() {

		setMargin(true);

		final Grid grid = new Grid();
		final WrappingGrid wrap = WrappingGrid.extend(grid);

		TableSelectionModel selectionModel = new TableSelectionModel();
		selectionModel.setMode(TableSelectionMode.SHIFT);
		grid.setSelectionModel(selectionModel);

		generateData(grid, 5, 100);

		Grid.HeaderRow headerRow = grid.prependHeaderRow();
		headerRow.join(grid.getColumns().get(1).getPropertyId(), grid.getColumns().get(2).getPropertyId());

		Grid.HeaderRow headerRow1 = grid.appendHeaderRow();
		headerRow1.join(grid.getColumns().get(2).getPropertyId(), grid.getColumns().get(3).getPropertyId());

		grid.setWidth("100%");
		grid.setHeight("100%");

		final Button button = new Button(BUTTON_WRAPPING_DISABLED_TEXT);
		button.addClickListener(new Button.ClickListener() {
			int state = 0;

			public void buttonClick(ClickEvent event) {
				state = (state + 1) % 2;
				switch (state) {
				case 0:
					// Disable wrapping, attempt to restore original behavior
					wrap.setWrapping(false);
					button.setCaption(BUTTON_WRAPPING_DISABLED_TEXT);
					break;
				case 1:
					// Apply wrapping rules
					wrap.setWrapping(true);
					button.setCaption(BUTTON_WRAPPING_ENABLED_TEXT);
					break;
				}
			}
		});

		addComponent(button);
		addComponent(grid);

		CacheStrategyExtension.extend(grid, 5, 0.2d);

	}

	private void generateData(Grid g, int cols, int rows) {
		g.addColumn("#");
		for (int x = 1; x < cols; ++x) {
			g.addColumn("Column with really long title " + (x + 1), String.class);
		}

		Random r = new Random();
		for (int y = 0; y < rows; ++y) {
			String[] values = new String[cols];
			values[0] = "" + (y + 1);
			for (int x = 1; x < cols; ++x) {
				values[x] = "" + r.nextInt() + " babies born last year";
			}
			g.addRow(values);
		}
	}

}
