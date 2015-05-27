package org.vaadin.teemusa.gridextensions.contextclick;

import org.vaadin.teemusa.gridextensions.client.contextclick.ContextClickRpc;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.AbstractGridExtension;

/**
 * ContextClickExtension for Grid. This extension listens to client-side context
 * menu events and informs them to the server side.
 * <p>
 * If this {@link ContextClickNotifier} has no listeners, the context menu
 * events in client will be ignored, not prevented.
 * 
 * @author Teemu Suo-Anttila
 */
public class ContextClickExtension extends AbstractGridExtension implements
        ContextClickNotifier {

    public ContextClickExtension(final Grid grid) {
        super(grid);
        registerRpc(new ContextClickRpc() {
            @Override
            public void contextClick(String rowKey, String columnId,
                    MouseEventDetails details) {
                fireEvent(new ContextClickEvent(grid, getItemId(rowKey),
                        getColumn(columnId), details));
            }
        });
    }

    @Override
    public void addContextClickListener(ContextClickListener listener) {
        addListener(ContextClickRpc.CONTEXT_CLICK_EVENT_ID,
                ContextClickEvent.class, listener,
                ContextClickListener.CONTEXT_CLICK_METHOD);
    }

    @Override
    public void removeItemClickListener(ContextClickListener listener) {
        removeListener(ContextClickRpc.CONTEXT_CLICK_EVENT_ID,
                ContextClickEvent.class, listener);
    }
}
