package org.vaadin.teemusa.gridextensions.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.vaadin.teemusa.gridextensions.SelectGrid;
import org.vaadin.teemusa.gridextensions.cachestrategy.CacheStrategyExtension;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;
import org.vaadin.teemusa.gridextensions.tableselection.TableSelectionModel;
import org.vaadin.teemusa.gridextensions.wrappinggrid.WrappingGrid;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;

public class HeaderWrapExtensionLayout extends VerticalLayout {

	private static final String BUTTON_WRAPPING_ENABLED_TEXT = "Turn wrapping off";
	private static final String BUTTON_WRAPPING_DISABLED_TEXT = "Turn wrapping on";

	public HeaderWrapExtensionLayout() {

		setMargin(true);

		final SelectGrid<RowData> grid = new SelectGrid<>();
		final WrappingGrid wrap = WrappingGrid.extend(grid);

		TableSelectionModel<RowData> selectionModel = new TableSelectionModel<>();
		selectionModel.setMode(TableSelectionMode.SHIFT);
		grid.setSelectionModel(selectionModel);

		generateData(grid, 5, 100);

		HeaderRow headerRow = grid.prependHeaderRow();
		headerRow.join(grid.getColumns().get(1), grid.getColumns().get(2));

		HeaderRow headerRow1 = grid.appendHeaderRow();
		headerRow1.join(grid.getColumns().get(2), grid.getColumns().get(3));

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

	private void generateData(Grid<RowData> g, int cols, int rows) {
		g.addColumn(RowData::getRowNumber).setCaption("#");
		for (int x = 0; x < cols; ++x) {
			int row = x;
			g.addColumn(t -> row < t.getValues().length ? t.getValues()[row] : "Empty")
					.setCaption("Yet another dummy column with extremely long and pointless title " + (x + 1));
		}

		List<RowData> data = new ArrayList<>();
		Random r = new Random();
		for (int y = 0; y < rows; ++y) {
			String[] values = new String[cols];
			for (int x = 0; x < cols; ++x) {
				values[x] = "" + r.nextInt() + " babies born last year";
			}
			data.add(new RowData(y, values));
		}
		g.setItems(data);
	}

	public static class RowData {
		private final Integer rowNumber;
		private final String[] values;

		RowData(Integer rowNumber, String[] values) {
			this.rowNumber = rowNumber;
			this.values = values;
		}

		public String[] getValues() {
			return values;
		}

		public Integer getRowNumber() {
			return rowNumber;
		}

	}

}
