package org.vaadin.teemusa.gridextensions.client.renderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorMap;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;

/**
 * Combination of client component renderer contained inside {@link com.vaadin.client.connectors.grid.ComponentRendererConnector} and current super class {@link ClickableRenderer}
 * @since 2.1
 */
public abstract class ClickableComponentRenderer extends ClickableRenderer<String, SimplePanel> {

    @Override
    public SimplePanel createWidget() {
        SimplePanel panel = GWT.create(SimplePanel.class);
        panel.setStyleName("component-wrap");
        addClickHandler(panel);
        return panel;
    }

    @Override
    public void render(RendererCellReference cell, String connectorId,
                       SimplePanel widget) {
        if (connectorId != null) {
            ComponentConnector connector = (ComponentConnector) ConnectorMap
                    .get(this.getConnectorConnection()).getConnector(connectorId);
            widget.setWidget(connector.getWidget());
        } else if (widget.getWidget() != null) {
            widget.remove(widget.getWidget());
        }
        addClickHandler(widget);
    }

    public abstract ApplicationConnection getConnectorConnection();

    private void addClickHandler(SimplePanel panel) {
        // Panel itself
        panel.addDomHandler(this, ClickEvent.getType());
        // Iterate over children
        panel.iterator().forEachRemaining(widget -> widget.addDomHandler(this, ClickEvent.getType()));
    }

}