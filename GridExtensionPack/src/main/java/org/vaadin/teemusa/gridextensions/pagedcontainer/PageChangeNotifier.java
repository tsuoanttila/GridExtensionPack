package org.vaadin.teemusa.gridextensions.pagedcontainer;

/**
 * Interface for registering listeners for {@link PageChangeEvent}s.
 * 
 * @author Teemu Suo-Anttila
 */
public interface PageChangeNotifier {

    /**
     * Register a listener to handle {@link PageChangeEvent}s.
     * 
     * @param listener
     *            PageChangeListener to be registered
     */
    public void addPageChangeListener(PageChangeListener listener);

    /**
     * Removes a {@link PageChangeListener}.
     * 
     * @param listener
     *            PageChangeListener to be removed
     */
    public void removePageChangeListener(PageChangeListener listener);
}
