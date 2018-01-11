package org.vaadin.teemusa.gridextensions.demo;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.ContentMode;
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
        createInfoLabel();
        createGrid();
	}

    private void createInfoLabel() {
	    String infoText = "<i>Info: The grey background in the Label column represents the root component of the custom component</i>";
	    Label infoLabel = new Label(infoText, ContentMode.HTML);
        infoLabel.setWidth("50%");

        addComponent(infoLabel);
    }

    private void createGrid() {
        Grid<RowData> grid = new Grid<>();
        grid.addColumn(RowData::getId);
        grid.addColumn(RowData::getName);

        Grid.Column<RowData, Component> labelColumn = grid.addColumn(this::createColumnComponent, getRenderer(grid));
        labelColumn.setCaption("Label in hlayout");

        Grid.Column<RowData, Boolean> canForwardClickColumn = grid.addColumn(RowData::getForwardLabelClicksToGrid, booleanValue -> booleanValue ? "Yes" : "No");
        canForwardClickColumn.setCaption("Label column will forward selection to grid");

        List<RowData> data = new ArrayList<RowData>();
        data.add(new RowData("test1", "One", "Label One", true));
        data.add(new RowData("test2", "Two", "Label Two", false));
        data.add(new RowData("test3", "Three", "Label Three", true));
        data.add(new RowData("test4", "Four", "Label Four", false));
        grid.setItems(data);

        grid.setWidth("60%");

        addStyleName("clickableComponentRendererForGridTest");
        addComponent(grid);
    }

    private ClickableComponentRenderer<RowData> getRenderer(Grid<RowData> grid) {
		ClickableComponentRenderer<RowData> renderer = new ClickableComponentRenderer<>();
		renderer.forwardSelection(grid, item -> item.forwardLabelClicksToGrid);
		return renderer;
	}
	
	private Component createColumnComponent(RowData rowData) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(new Label(rowData.getLabelText()));
        layout.addStyleName("clickableLabel");
		return layout;
	}
	
	public class RowData {
		
		private final String id;
		private final String name;
		private final String labelText;
		private final boolean forwardLabelClicksToGrid;
		
		public RowData(String id, String name, String labelText, boolean forwardLabelClicksToGrid) {
			this.id = id;
			this.name = name;
			this.labelText = labelText;
			this.forwardLabelClicksToGrid = forwardLabelClicksToGrid;
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

		public boolean getForwardLabelClicksToGrid() {
			return forwardLabelClicksToGrid;
		}

	}

}
