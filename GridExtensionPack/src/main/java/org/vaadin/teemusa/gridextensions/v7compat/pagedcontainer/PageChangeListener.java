package org.vaadin.teemusa.gridextensions.v7compat.pagedcontainer;

/**
 * Listener interface for {@link PageChangeEvent}s.
 * 
 * @author Teemu Suo-Anttila
 */
@Deprecated
public interface PageChangeListener {

    /**
     * Called when page count changes
     * 
     * @param event
     *            a page change event
     */
    public void pageCountChange(PageChangeEvent event);

    /**
     * Called when page changes
     * 
     * @param event
     *            a page change event
     */
    public void pageChange(PageChangeEvent event);

}
