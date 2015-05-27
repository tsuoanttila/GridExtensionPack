package org.vaadin.teemusa.gridextensions.client.contextclick;

import org.vaadin.teemusa.gridextensions.client.AbstractGridExtensionConnector;
import org.vaadin.teemusa.gridextensions.contextclick.ContextClickExtension;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.widget.grid.EventCellReference;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(ContextClickExtension.class)
public class ContextClickConnector extends AbstractGridExtensionConnector {

    @Override
    protected void extend(ServerConnector target) {
        getGrid().sinkEvents(Event.ONCONTEXTMENU);
        getGrid().addDomHandler(new ContextMenuHandler() {

            @Override
            public void onContextMenu(ContextMenuEvent event) {
                EventCellReference<JsonObject> cell = getGrid().getEventCell();
                if (cell.isBody()
                        && hasEventListener(ContextClickRpc.CONTEXT_CLICK_EVENT_ID)) {
                    event.preventDefault();
                    ContextClickRpc rpc = getRpcProxy(ContextClickRpc.class);
                    rpc.contextClick(getRowKey(cell.getRow()),
                            getColumnId(cell.getColumn()),
                            getMouseEventDetails(event.getNativeEvent()));
                }
            }

        }, ContextMenuEvent.getType());
    }
}
