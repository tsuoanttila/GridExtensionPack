package org.vaadin.teemusa.gridextensions.demo;

import org.vaadin.teemusa.gridextensions.SelectGrid;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;
import org.vaadin.teemusa.gridextensions.tableselection.TableSelectionModel;

import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.GridContextClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;

public class DemoContentLayout extends VerticalLayout {

	public DemoContentLayout() {
		final SelectGrid<TestObject> grid = new SelectGrid<>();
		grid.addColumn(TestObject::getFoo).setCaption("Foo");
		grid.addColumn(TestObject::getBar, new NumberRenderer()).setCaption("Bar");
		grid.addColumn(TestObject::getKm, new NumberRenderer()).setCaption("KM");

		grid.setItems(TestObject.generateTestData(1000));

		// Show it in the middle of the screen
		setStyleName("demoContentLayout");
		setSizeFull();
		addComponent(grid);
		setComponentAlignment(grid, Alignment.MIDDLE_CENTER);

		// TODO: Context menu example.

		final TableSelectionModel<TestObject> tableSelect = new TableSelectionModel<>();
		grid.setSelectionModel(tableSelect);
		tableSelect.setMode(TableSelectionMode.CTRL);

		HorizontalLayout tableSelectionControls = new HorizontalLayout();
		tableSelectionControls.setCaption("Table Selection Controls");

		// Controls for testing different TableSelectionModes
		for (final TableSelectionMode t : TableSelectionMode.values()) {
			tableSelectionControls.addComponent(new Button(t.toString(), e -> tableSelect.setMode(t)));
		}

		addComponent(tableSelectionControls);
		setComponentAlignment(tableSelectionControls, Alignment.BOTTOM_CENTER);

		// TODO: PagingDataProvider

		grid.getEditor().setEnabled(true);
		for (Column<TestObject, ?> c : grid.getColumns()) {
			c.setHidable(true);
		}
	}
}
