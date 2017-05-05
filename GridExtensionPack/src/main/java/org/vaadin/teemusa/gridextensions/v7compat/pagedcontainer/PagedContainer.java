package org.vaadin.teemusa.gridextensions.v7compat.pagedcontainer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.shared.Range;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Container.Indexed;
import com.vaadin.v7.data.Container.ItemSetChangeNotifier;
import com.vaadin.v7.data.Container.Sortable;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.AbstractContainer;
import com.vaadin.v7.shared.ui.grid.HeightMode;
import com.vaadin.v7.ui.Grid;

/**
 * Container for Grid that provides paging. Replaced by
 * {@link org.vaadin.teemusa.gridextensions.paging.PagedDataProvider}.
 * 
 * @author Teemu Suo-Anttila
 * 
 * @deprecated since 2.0.0
 */
@Deprecated
public class PagedContainer extends AbstractContainer implements Container, Indexed, Sortable, ItemSetChangeNotifier {

	/**
	 * Helper class containing the paging related functionality. Fires
	 * {@link PageChangeEvent}s when page or page count changes.
	 */
	public class PagingControls implements PageChangeNotifier {

		private Set<PageChangeListener> pageChangeListeners = new HashSet<PageChangeListener>();
		private int pageCount;
		private Grid grid;

		public PagingControls(Grid grid) {
			this.grid = grid;
			setPageLength(25);
			pageCount = calculatePageCount();
		}

		/**
		 * Gets the current page length.
		 * 
		 * @return current page length
		 */
		public int getPageLength() {
			return pageLength;
		}

		/**
		 * Sets the current page length. This should be the same as amount of
		 * displayed rows.
		 * 
		 * @param pLength
		 *            amount of rows to show
		 */
		public void setPageLength(int pLength) {
			if (pageLength != pLength) {
				pageLength = pLength;
				page = startIndex / pLength;
				checkPageCount();

				if (grid != null) {
					// Update Grid state
					grid.setHeightByRows(pLength);
					grid.setHeightMode(HeightMode.ROW);
				}

				// Fire event if needed.
				if (!setStartIndex()) {
					fireItemSetChange();
				}
			}
		}

		/**
		 * Gets the current page
		 * 
		 * @return current page
		 */
		public int getPage() {
			return page;
		}

		/**
		 * Sets the current page. Page is 0-based index.
		 * 
		 * @param pageNumber
		 * @throws IllegalArgumentException
		 *             if given page number is not between [0, getPageCount()[
		 */
		public void setPage(int pageNumber) throws IllegalArgumentException {
			if (pageNumber >= 0 && pageNumber < pageCount) {
				page = pageNumber;
				setStartIndex();
			} else {
				throw new IllegalArgumentException("Page index " + pageNumber + " is invalid. Correct page range is "
						+ Range.between(0, pageCount).toString());
			}
		}

		/**
		 * Move to next page
		 */
		public void nextPage() {
			if (page + 1 < pageCount) {
				++page;
				setStartIndex();
			}
		}

		/**
		 * Move to previous page
		 */
		public void previousPage() {
			if (page > 0) {
				--page;
				setStartIndex();
			}
		}

		/**
		 * Gets the current page count.
		 * 
		 * @return page count
		 */
		public int getPageCount() {
			return pageCount;
		}

		@Override
		public void addPageChangeListener(PageChangeListener listener) {
			pageChangeListeners.add(listener);
		}

		@Override
		public void removePageChangeListener(PageChangeListener listener) {
			pageChangeListeners.remove(listener);
		}

		private int calculatePageCount() {
			return (int) Math.ceil(((double) container.size()) / pageLength);
		}

		private boolean setStartIndex() {
			int newStartIndex = page * pageLength;
			if (startIndex != newStartIndex) {
				startIndex = newStartIndex;
				fireItemSetChange();
				firePageChangeEvent();
				return true;
			}
			return false;
		}

		private void checkPageCount() {
			// Fix page if we're on a non-existent page.
			while (startIndex >= container.size() && page > 0) {
				--page;
				startIndex -= pageLength;
			}

			int currentPageCount = calculatePageCount();
			if (pageCount != currentPageCount) {
				pageCount = currentPageCount;
				firePageCountChangeEvent();
			}
		}

		private void firePageCountChangeEvent() {
			PageChangeEvent event = new PageChangeEvent(this);
			for (PageChangeListener l : pageChangeListeners) {
				l.pageCountChange(event);
			}
		}

		private void firePageChangeEvent() {
			PageChangeEvent event = new PageChangeEvent(this);
			for (PageChangeListener l : pageChangeListeners) {
				l.pageChange(event);
			}
		}
	}

	private final Container.Indexed container;
	private PagingControls controls;
	private int pageLength = 0;
	private int startIndex = 0;
	private int page = 0;

	private ItemSetChangeListener listener = new ItemSetChangeListener() {
		@Override
		public void containerItemSetChange(ItemSetChangeEvent event) {
			if (event instanceof ItemAddEvent) {
				int firstIndex = ((ItemAddEvent) event).getFirstIndex();
				controls.checkPageCount();
				if (firstIndex < startIndex + pageLength) {
					fireItemSetChange();
				}
			} else if (event instanceof ItemRemoveEvent) {
				int firstIndex = ((ItemRemoveEvent) event).getFirstIndex();
				if (firstIndex < startIndex + pageLength) {
					controls.checkPageCount();
					fireItemSetChange();
				}
			} else {
				controls.checkPageCount();
				fireItemSetChange();
			}
		}
	};

	public PagedContainer(final Container.Indexed container) {
		this.container = container;

		if (container instanceof ItemSetChangeNotifier) {
			ItemSetChangeNotifier notifier = (ItemSetChangeNotifier) container;
			notifier.addItemSetChangeListener(listener);
		}

		controls = new PagingControls(null);
	}

	/**
	 * Sets the Grid that uses this container as its data source. This method
	 * generates a PagingControls for managing the Container along with the
	 * Grid.
	 * 
	 * @param grid
	 *            grid using this data source
	 * 
	 * @return paging controls
	 */
	public PagingControls setGrid(Grid grid) {
		controls = new PagingControls(grid);
		return getPagingControls();
	}

	/**
	 * Gets a {@link PagingControls} helper. Page changes are done through this
	 * helper.
	 * 
	 * @return paging controls
	 * 
	 * @throws IllegalStateException
	 *             if no Grid has been set for this container
	 */
	public PagingControls getPagingControls() throws IllegalStateException {
		return controls;
	}

	public Container.Indexed getContainer() {
		return container;
	}

	@Override
	public void addItemSetChangeListener(ItemSetChangeListener listener) {
		super.addItemSetChangeListener(listener);
	}

	@Override
	public void removeItemSetChangeListener(ItemSetChangeListener listener) {
		super.removeItemSetChangeListener(listener);
	}

	@Override
	public void addListener(ItemSetChangeListener listener) {
		super.addListener(listener);
	}

	@Override
	public void removeListener(ItemSetChangeListener listener) {
		super.removeListener(listener);
	}

	/*
	 * Overridden methods from the real container from here forward
	 */

	@Override
	public int size() {
		int rowsLeft = container.size() - startIndex;
		if (rowsLeft > pageLength) {
			return pageLength;
		} else {
			return rowsLeft;
		}
	}

	public int getRealSize() {
		return container.size();
	}

	public Object getIdByIndex(int index) {
		return container.getIdByIndex(index + startIndex);
	}

	/*
	 * Delegate methods to real container from here on
	 */

	@Override
	public Item getItem(Object itemId) {
		return container.getItem(itemId);
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		return container.getContainerPropertyIds();
	}

	@Override
	public Collection<?> getItemIds() {
		return container.getItemIds();
	}

	@Override
	public Property getContainerProperty(Object itemId, Object propertyId) {
		return container.getContainerProperty(itemId, propertyId);
	}

	@Override
	public Class<?> getType(Object propertyId) {
		return container.getType(propertyId);
	}

	@Override
	public boolean containsId(Object itemId) {
		return container.containsId(itemId);
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		return container.addItem(itemId);
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		return container.addItem();
	}

	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException {
		return container.removeItem(itemId);
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
			throws UnsupportedOperationException {
		return container.addContainerProperty(propertyId, type, defaultValue);
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
		return container.removeContainerProperty(propertyId);
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		return container.removeAllItems();
	}

	public Object nextItemId(Object itemId) {
		return container.nextItemId(itemId);
	}

	public Object prevItemId(Object itemId) {
		return container.prevItemId(itemId);
	}

	public Object firstItemId() {
		return container.firstItemId();
	}

	public Object lastItemId() {
		return container.lastItemId();
	}

	public boolean isFirstId(Object itemId) {
		return container.isFirstId(itemId);
	}

	public boolean isLastId(Object itemId) {
		return container.isLastId(itemId);
	}

	public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
		return container.addItemAfter(previousItemId);
	}

	public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
		return container.addItemAfter(previousItemId, newItemId);
	}

	public int indexOfId(Object itemId) {
		return container.indexOfId(itemId);
	}

	public Object addItemAt(int index) throws UnsupportedOperationException {
		return container.addItemAt(index);
	}

	public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
		return container.addItemAt(index, newItemId);
	}

	/*
	 * Sorting interface from here on
	 */

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (container instanceof Container.Sortable) {
			((Container.Sortable) container).sort(propertyId, ascending);
		}
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		if (container instanceof Container.Sortable) {
			return ((Container.Sortable) container).getSortableContainerPropertyIds();
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<?> getItemIds(final int startIndex, final int numberOfItems) {
		return container.getItemIds(this.startIndex, numberOfItems);
	}
}
