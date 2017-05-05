package org.vaadin.teemusa.gridextensions.demo.v7compat;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemusa.gridextensions.v7compat.sidebarmenuextension.SidebarMenuExtension;
import org.vaadin.teemusa.gridextensions.v7compat.sidebarmenuextension.SidebarMenuExtension.Command;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.Container.Indexed;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.renderers.HtmlRenderer;

/**
 * Test layout for SidebarMenuExtension
 *
 * @author Anna Koskinen
 *
 */
public class SidebarMenuExtensionLayoutV7 extends VerticalLayout {

	private SidebarMenuExtension extension;
	private int commandId = 0;
	private List<Command> commands = new ArrayList<Command>();

	public SidebarMenuExtensionLayoutV7() {
		setMargin(true);
		setSizeFull();
		setSpacing(true);
		addStyleName("sidebarExtensionLayout");

		HorizontalLayout buttonLayout = new HorizontalLayout();
		final VerticalLayout labelLayout = new VerticalLayout();

		Button addButton = new Button("Add command");
		addButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				++commandId;
				final Integer commandId = SidebarMenuExtensionLayoutV7.this.commandId;
				Command command = new Command() {

					@Override
					public void trigger() {
						labelLayout.addComponent(new Label("Command " + commandId + " clicked!"), 0);
					}
				};
				commands.add(command);
				extension.addCommand(command, "Command " + commandId);
			}
		});
		buttonLayout.addComponent(addButton);

		Button addDividerButton = new Button("Add divider");
		addDividerButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				++commandId;
				Command command = new Command() {

					@Override
					public void trigger() {
						// NOP
					}
				};
				commands.add(command);
				extension.addCommand(command, "");
				extension.setStyle(command, "divider");
			}
		});
		buttonLayout.addComponent(addDividerButton);

		Button removeButton = new Button("Remove command");
		removeButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Command command = commands.get(Double.valueOf(Math.ceil(commands.size() / 2)).intValue());
				extension.removeCommand(command);
				commands.remove(command);
			}
		});
		buttonLayout.addComponent(removeButton);

		Button styleButton = new Button("Set command styles");
		styleButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (commands.size() > 0) {
					for (Command command : commands) {
						if (!"divider".equals(extension.getStyle(command))) {
							extension.removeStyle(command);
						}
					}
					setStyle(Double.valueOf(Math.ceil(commands.size() / 2)).intValue(), "red");
					setStyle(0, "blue");
					setStyle(commands.size() - 1, "mixed");
				}
			}

			private void setStyle(int index, String style) {
				Command command = commands.get(index);
				while ("divider".equals(extension.getStyle(command)) && (commands.size() - 1) > index) {
					++index;
					command = commands.get(index);
				}
				extension.setStyle(command, style);
			}
		});
		buttonLayout.addComponent(styleButton);
		buttonLayout.addComponent(new Button("Open sidebar menu", new Button.ClickListener() {

			boolean open = false;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!open) {
					extension.openSidebarMenu();
				} else {
					extension.closeSidebarMenu();
				}
				open = !open;
				setCaption((open ? "Close" : "Open") + " sidebar menu");
			}
		}));

		addComponent(buttonLayout);
		addGrid();
		addComponent(labelLayout);
		setExpandRatio(labelLayout, 2);
	}

	@SuppressWarnings("unchecked")
	private void addGrid() {
		final Grid grid = new Grid("Attachment grid");
		grid.addColumn("filename");
		grid.addColumn("open");
		grid.addColumn("download");
		grid.getColumn("download").setRenderer(new HtmlRenderer());
		Indexed dataSource = grid.getContainerDataSource();
		for (int i = 1; i <= 10; ++i) {
			Object itemId = dataSource.addItem();
			Item item = dataSource.getItem(itemId);
			item.getItemProperty("filename").setValue("filename " + i + ".txt");
			item.getItemProperty("open").setValue("false");
			item.getItemProperty("download").setValue(createOpenLink());
		}
		grid.getColumn("open").setHidable(true);
		grid.getColumn("download").setHidable(true);
		grid.setColumnReorderingAllowed(true);
		grid.setColumnOrder("filename", "open", "download");
		addComponent(grid);
		extension = SidebarMenuExtension.extend(grid);
	}

	private Object createOpenLink() {
		return "<span class=\"v-button-link grid-open\">fake link</span>";
	}
}
