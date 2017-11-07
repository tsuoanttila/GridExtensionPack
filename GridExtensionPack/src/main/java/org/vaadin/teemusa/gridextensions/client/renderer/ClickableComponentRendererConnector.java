package org.vaadin.teemusa.gridextensions.client.renderer;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.connectors.ClickableRendererConnector;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(org.vaadin.teemusa.gridextensions.renderer.ClickableComponentRenderer.class)
public class ClickableComponentRendererConnector extends ClickableRendererConnector<String> {

	@Override
	public ClickableComponentRenderer getRenderer() {
		return (ClickableComponentRenderer) super.getRenderer();
	}

	@Override
	protected HandlerRegistration addClickHandler(ClickableRenderer.RendererClickHandler<JsonObject> handler) {
		return getRenderer().addClickHandler(handler);
	}

	@Override
	protected ClickableRenderer<String, SimplePanel> createRenderer() {
		return new ClickableComponentRenderer() {
			@Override
			public ApplicationConnection getConnectorConnection() {
				return getConnection();
			}
		};
	}

	@Override
	public ClickableComponentRendererState getState() {
		return (ClickableComponentRendererState) super.getState();
	}
}