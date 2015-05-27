package org.vaadin.teemusa.gridextensions.contextclick;

import java.io.Serializable;

/**
 * Interface for registering listeners for {@link ContextClickEvent}s.
 * 
 * @author Teemu Suo-Anttila
 */
public interface ContextClickNotifier extends Serializable {

    /**
     * Register a listener to handle {@link ContextClickEvent}s.
     * 
     * @param listener
     *            ContextClickListener to be registered
     */
    public void addContextClickListener(ContextClickListener listener);

    /**
     * Removes a {@link ContextClickListener}.
     * 
     * @param listener
     *            ContextClickListener to be removed
     */
    public void removeItemClickListener(ContextClickListener listener);
}
