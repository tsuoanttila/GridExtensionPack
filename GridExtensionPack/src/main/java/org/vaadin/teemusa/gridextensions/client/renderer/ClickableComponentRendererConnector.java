package org.vaadin.teemusa.gridextensions.client.renderer;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.connectors.grid.ComponentRendererConnector;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.grid.renderers.RendererClickRpc;
import elemental.json.JsonObject;

@Connect(org.vaadin.teemusa.gridextensions.renderer.ClickableComponentRenderer.class)
public class ClickableComponentRendererConnector extends ComponentRendererConnector {

	@Override
	public ClickableComponentRenderer getRenderer() {
		return (ClickableComponentRenderer) super.getRenderer();
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

	private HandlerRegistration clickRegistration;

	@Override
	protected void init() {
		clickRegistration = getRenderer().addClickHandler(
				(ClickableRenderer.RendererClickHandler<JsonObject>) event ->
					getRpcProxy(RendererClickRpc.class).click(
                        getRowKey(event.getCell().getRow()),
                        getColumnId(event.getCell().getColumn()),
                        MouseEventDetailsBuilder.buildMouseEventDetails(
                                event.getNativeEvent())));
	}

	@Override
	public void onUnregister() {
		clickRegistration.removeHandler();
	}

	@Override
	public ClickableComponentRendererState getState() {
		return (ClickableComponentRendererState) super.getState();
	}
}