package org.vaadin.teemusa.gridextensions.demo;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemusa.gridextensions.renderer.ClickableComponentRenderer;

import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ClickableComponentRendererLayout extends VerticalLayout {

	public ClickableComponentRendererLayout() {
		super();
		
		Grid<RowData> grid = new Grid<>();
		grid.addColumn(RowData::getId);
		grid.addColumn(RowData::getName);
		grid.addColumn(this::createColumnComponent, getRenderer(grid));
		
		List<RowData> data = new ArrayList<RowData>();
		data.add(new RowData("test1", "One", "Label One"));
		data.add(new RowData("test2", "Two", "Label Two"));
		data.add(new RowData("test3", "Three", "Label Three"));
		data.add(new RowData("test4", "Four", "Label Four"));
		grid.setItems(data);
		
		addComponent(grid);
	}
	
	private ClickableComponentRenderer<RowData> getRenderer(Grid<RowData> grid) {
		ClickableComponentRenderer<RowData> renderer = new ClickableComponentRenderer<>();
		renderer.forwardSelection(grid);
		renderer.addClickListener(event -> event.getItem().setLabelBool(MouseButton.LEFT.equals(event.getButton())));
		return renderer;
	}
	
	private Component createColumnComponent(RowData rowData) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(new Label(rowData.getLabelText()));
		return layout;
	}
	
	public class RowData {
		
		private final String id;
		private final String name;
		private final String labelText;
		
		private boolean labelBool = false;
		
		public RowData(String id, String name, String labelText) {
			this.id = id;
			this.name = name;
			this.labelText = labelText;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getLabelText() {
			return labelText;
		}

		public boolean isLabelBool() {
			return labelBool;
		}

		public void setLabelBool(boolean labelBool) {
			this.labelBool = labelBool;
		}
		
	}

}
