package org.vaadin.teemusa.gridextensions.renderer;

import org.vaadin.teemusa.gridextensions.client.renderer.ClickableComponentRendererState;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ClickableRenderer;

import elemental.json.Json;
import elemental.json.JsonValue;

public class ClickableComponentRenderer<T> extends ClickableRenderer<T, Component> {

    /**
     * Constructor for ComponentRenderer.
     */
    public ClickableComponentRenderer() {
        super(Component.class);
    }

    /**
     * Adds a click listener to the renderer that will select the clicked row item in the grid.
     *
     * @param grid
     *             the grid to forward the selection to
     *
     * @see ClickableRenderer#addClickListener(RendererClickListener)
     */
    public void forwardSelection(Grid<T> grid) {
        addClickListener(event -> grid.select(event.getItem()));
    }

    @Override
    public JsonValue encode(Component value) {
        return value != null ? Json.create(value.getConnectorId()) : null;
    }

    @Override
    protected ClickableComponentRendererState getState(boolean markAsDirty) {
        return (ClickableComponentRendererState) super.getState(markAsDirty);
    }

    @Override
    protected ClickableComponentRendererState getState() {
        return (ClickableComponentRendererState) super.getState();
    }
    
}
