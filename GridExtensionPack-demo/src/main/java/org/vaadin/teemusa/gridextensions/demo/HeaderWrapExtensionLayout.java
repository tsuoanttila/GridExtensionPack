package org.vaadin.teemusa.gridextensions.demo;

import java.util.Random;

import org.vaadin.teemusa.gridextensions.wrappinggrid.WrappingGrid;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

public class HeaderWrapExtensionLayout extends VerticalLayout {

    public HeaderWrapExtensionLayout() {

	setMargin(true);

	final Grid grid = new Grid();
	final WrappingGrid wrap = WrappingGrid.extend(grid);
	
	generateData(grid,5,100);
	
	grid.setWidth("100%");
	grid.setHeight("100%");

	Button button = new Button("Toggle grid column widths");
	button.addClickListener(new Button.ClickListener() {
		int state = 0;
		public void buttonClick(ClickEvent event) {
			state = (state + 1) % 2;
			switch(state) {
			case 0:
				// Disable wrapping, attempt to restore original behavior
				wrap.setWrapping(false);
				grid.getColumns().get(1).setWidthUndefined();
				grid.getColumns().get(2).setWidthUndefined();
				grid.getColumns().get(3).setWidthUndefined();
			break;
			case 1:
				// Apply wrapping rules
				wrap.setWrapping(true);
				grid.getColumns().get(1).setWidth(200);
				grid.getColumns().get(2).setWidth(150);
				grid.getColumns().get(3).setWidth(100);
			break;
			}
		}
	});

	addComponent(button);
	addComponent(grid);

}

private void generateData(Grid g, int cols, int rows) {
	g.addColumn("#");
	for(int x = 1; x < cols; ++x) {
		g.addColumn("Column with really long title " + (x + 1), String.class);
	}

	Random r = new Random();
	for(int y = 0; y < rows; ++y) {
		String[] values = new String[cols];
		values[0] = "" + (y + 1);
		for(int x = 1; x < cols; ++x) {
			values[x] = "" + r.nextInt() + " babies born last year";
		}
		g.addRow(values);
	}
}

}
