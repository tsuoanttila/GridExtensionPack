package org.vaadin.teemusa.gridextensions.renderer;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.grid.renderers.RendererClickRpc;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.util.ReflectTools;
import org.vaadin.teemusa.gridextensions.client.renderer.ClickableComponentRendererState;

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * It has to extend from {@link ComponentRenderer} because there is some hardcoded logic in {@link Grid.Column} that checks for instanceof ComponentRenderer.
 * <p>
 * The logic handling the click events is copied from {@link ClickableRenderer}
 * @param <T> Grid row type
 * @since 2.1
 */

public class ClickableComponentRenderer<T extends Object> extends ComponentRenderer {

    private Registration forwardClickRegistration;

    /**
     * Constructor for ComponentRenderer.
     */
    public ClickableComponentRenderer() {
        super();
        registerRpc((RendererClickRpc) (String rowKey, String columnId,
                                        MouseEventDetails mouseDetails) -> {
            Grid<T> grid = (Grid<T>) getParentGrid();
            T item = grid.getDataCommunicator().getKeyMapper().get(rowKey);
            Grid.Column<T, Component> column = (Grid.Column<T, Component>) getParent();

            fireEvent(new RendererClickEvent(grid, item, column, mouseDetails));
        });
    }

    /**
     * Adds a click listener to the renderer that will select the clicked row item in the grid.
     *
     * @param grid
     *             the grid to forward the selection to
     *
     */
    public void forwardSelection(Grid<T> grid) {
       forwardSelection(grid, item -> true);
    }

    public void forwardSelection(Grid<T> grid, Predicate<T> predicateToForward) {
        this.forwardClickRegistration = addClickListener(event -> {
            if (predicateToForward.test(event.item)) {
                grid.select(event.getItem());
            }
        });
    }

    public Registration getForwardClickRegistration() {
        return forwardClickRegistration;
    }

    @Override
    protected ClickableComponentRendererState getState(boolean markAsDirty) {
        return (ClickableComponentRendererState) super.getState(markAsDirty);
    }

    @Override
    protected ClickableComponentRendererState getState() {
        return (ClickableComponentRendererState) super.getState();
    }

    /**
     * Adds a click listener to this button renderer. The listener is invoked
     * every time one of the buttons rendered by this renderer is clicked.
     *
     * @param listener
     *            the click listener to be added, not null
     * @return the Registration of the listener
     */
    public Registration addClickListener(RendererClickListener<T> listener) {
        return addListener(RendererClickEvent.class, listener, RendererClickListener.CLICK_METHOD);
    }

    /**
     * An interface for listening to {@link ClickableRenderer.RendererClickEvent renderer click
     * events}.
     *
     * @see ButtonRenderer#addClickListener(ClickableRenderer.RendererClickListener)
     */
    @FunctionalInterface
    public interface RendererClickListener<T> extends ConnectorEventListener {

        static final Method CLICK_METHOD = ReflectTools.findMethod(
                RendererClickListener.class, "click", RendererClickEvent.class);

        /**
         * Called when a rendered button is clicked.
         *
         * @param event
         *            the event representing the click
         */
        void click(RendererClickEvent<T> event);
    }

    /**
     * An event fired when a clickable widget rendered by a ClickableComponentRenderer is
     * clicked.
     */
    public static class RendererClickEvent<T> extends MouseEvents.ClickEvent {

        private final T item;
        private final Grid.Column<T, ?> column;

        protected RendererClickEvent(Grid<T> source, T item,
                                     Grid.Column<T, ?> column, MouseEventDetails mouseEventDetails) {
            super(source, mouseEventDetails);
            this.item = item;
            this.column = column;
        }

        /**
         * Returns the item of the row where the click event originated.
         *
         * @return the item of the clicked row
         */
        public T getItem() {
            return item;
        }

        /**
         * Returns the {@link Grid.Column} where the click event originated.
         *
         * @return the column of the click event
         */
        public Grid.Column<T, ?> getColumn() {
            return column;
        }
    }

}
