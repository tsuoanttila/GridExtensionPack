package org.vaadin.teemusa.gridextensions.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;
import org.vaadin.teemusa.gridextensions.contextclick.ContextClickEvent;
import org.vaadin.teemusa.gridextensions.contextclick.ContextClickExtension;
import org.vaadin.teemusa.gridextensions.contextclick.ContextClickListener;
import org.vaadin.teemusa.gridextensions.tableselection.TableSelectionExtension;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("GridExtensionPack Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.teemusa.gridextensions.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        // Initialize our new UI component
        final Grid grid = new Grid();

        grid.addColumn("Name", String.class);
        grid.addColumn("Commits", Integer.class);
        grid.addRow("Teemu Suo-Anttila", 2);
        grid.addRow("Rudolf the Reindeer", 0);
        grid.addRow("Teemu Teekkari", 0);

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(grid);
        layout.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
        setContent(layout);

        new ContextClickExtension(grid)
                .addContextClickListener(new ContextClickListener() {

                    @Override
                    public void contextClick(ContextClickEvent event) {
                        Notification.show("Context click on "
                                + event.getItemId() + " on property "
                                + event.getPropertyId());
                    }
                });

        final TableSelectionExtension tableSelect = new TableSelectionExtension(
                grid);
        tableSelect.setMode(TableSelectionMode.CTRL);

        HorizontalLayout tableSelectionControls = new HorizontalLayout();
        tableSelectionControls.setCaption("Table Selection Controls");

        for (final TableSelectionMode t : TableSelectionMode.values()) {
            tableSelectionControls.addComponent(new Button(t.toString(),
                    new ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            tableSelect.setMode(t);
                        }
                    }));
        }

        layout.addComponent(tableSelectionControls);
        layout.setComponentAlignment(tableSelectionControls,
                Alignment.MIDDLE_CENTER);
    }
}
