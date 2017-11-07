package org.vaadin.teemusa.gridextensions.demo;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

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
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(new DemoContentLayout(), "main demo");
        tabSheet.addTab(new SidebarMenuExtensionLayout(), "sidebar extension demo");
        tabSheet.addTab(new HeaderWrapExtensionLayout(), "header wrap demo");
        tabSheet.addTab(new ClickableComponentRendererLayout(), "Clickable comp click demo");
        tabSheet.setSizeFull();
        setContent(tabSheet);
    }
}
