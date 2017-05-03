package org.vaadin.teemusa.gridextensions.demo;

import org.vaadin.teemusa.gridextensions.SelectGrid;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;
import org.vaadin.teemusa.gridextensions.paging.PagedDataProvider;
import org.vaadin.teemusa.gridextensions.paging.PagingControls;
import org.vaadin.teemusa.gridextensions.tableselection.TableSelectionModel;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;

public class DemoContentLayout extends VerticalLayout {

	public DemoContentLayout() {
		final SelectGrid<TestObject> grid = new SelectGrid<>();
		grid.addColumn(TestObject::getFoo).setCaption("Foo");
		grid.addColumn(TestObject::getBar, new NumberRenderer()).setCaption("Bar");
		grid.addColumn(TestObject::getKm, new NumberRenderer()).setCaption("KM");
		grid.setHeightByRows(10);
		grid.setHeightMode(HeightMode.ROW);

		// Show it in the middle of the screen
		setStyleName("demoContentLayout");
		setSizeFull();
		addComponent(grid);
		setComponentAlignment(grid, Alignment.MIDDLE_CENTER);

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

		// TODO: PagingDataProvider

		PagedDataProvider<TestObject, SerializablePredicate<TestObject>> dataProvider = new PagedDataProvider<>(
				DataProvider.ofCollection(TestObject.generateTestData(995)));
		grid.setDataProvider(dataProvider);
		PagingControls<TestObject> pagingControls = dataProvider.getPagingControls();

		HorizontalLayout pages = new HorizontalLayout();
		pages.setCaption("Paging controls");
		pages.addComponent(new Button("First", e -> pagingControls.setPageNumber(0)));
		pages.addComponent(new Button("Previous", e -> pagingControls.previousPage()));
		pages.addComponent(new Button("Next", e -> pagingControls.nextPage()));
		pages.addComponent(new Button("Last", e -> pagingControls.setPageNumber(pagingControls.getPageCount() - 1)));
		VerticalLayout controls = new VerticalLayout();
		controls.addComponents(tableSelectionControls, pages);
		controls.setWidth("100%");
		controls.setHeightUndefined();
		controls.setComponentAlignment(tableSelectionControls, Alignment.MIDDLE_CENTER);
		controls.setComponentAlignment(pages, Alignment.BOTTOM_CENTER);
		addComponent(controls);
		setComponentAlignment(controls, Alignment.MIDDLE_CENTER);

		grid.getEditor().setEnabled(true);
		for (Column<TestObject, ?> c : grid.getColumns()) {
			c.setHidable(true);
		}
	}
}
