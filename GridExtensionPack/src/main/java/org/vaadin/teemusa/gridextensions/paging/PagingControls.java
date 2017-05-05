package org.vaadin.teemusa.gridextensions.paging;

import com.vaadin.data.provider.Query;

public class PagingControls<T> {

	private PagedDataProvider<T, ?> pagedDataProvider;
	private int pageLength;
	private int pageNumber = 0;

	PagingControls(PagedDataProvider<T, ?> pagedDataProvider, int pageLength) {
		this.pagedDataProvider = pagedDataProvider;
		setPageLength(pageLength);
	}

	/**
	 * Moves to next page, if next page exists.
	 */
	public void nextPage() {
		if (pageNumber + 1 < getPageCount()) {
			++pageNumber;
			pagedDataProvider.refreshAll();
		}
	}

	/**
	 * Moves to previous page, if previous page exists.
	 */
	public void previousPage() {
		if ((pageNumber - 1) >= 0) {
			--pageNumber;
			pagedDataProvider.refreshAll();
		}
	}

	/**
	 * Sets the page length. Because the page length might cause some
	 * misalignment in paging, the page number is set to 0 when setting page
	 * length. The {@link setPageNumber} can be used to restore position
	 * according to application preferences.
	 * 
	 * @param newPageLength
	 *            the new page length
	 * 
	 * @throws IllegalArgumentException
	 *             if the new page length is less than 1
	 */
	public void setPageLength(int newPageLength) {
		if (newPageLength <= 0) {
			throw new IllegalArgumentException("Illegal page length.");
		}
		if (pageLength != newPageLength) {
			pageNumber = 0;
			pageLength = newPageLength;
			pagedDataProvider.refreshAll();
		}
	}

	/**
	 * Sets the current page number.
	 * 
	 * @param newPageNumber
	 *            the new page number
	 */
	public void setPageNumber(int newPageNumber) {
		if (newPageNumber >= 0 && newPageNumber < getPageCount()) {
			pageNumber = newPageNumber;
			this.pagedDataProvider.refreshAll();
		} else {
			throw new IllegalArgumentException("Illegal page number.");
		}
	}

	/**
	 * Gets the current page count. If this method is called in init, it is
	 * assumed that no filters are used.
	 * 
	 * @return page count
	 */
	public int getPageCount() {
		int backendSize = pagedDataProvider.getBackendSize();
		int lastPage = backendSize / pageLength;
		return backendSize % pageLength == 0 ? lastPage : lastPage + 1;
	}

	/**
	 * Gets the current page number.
	 * 
	 * @return current page number
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * Gets the current page length.
	 * 
	 * @return current page length
	 */
	public int getPageLength() {
		return pageLength;
	}

	void updatePageNumber() {
		while (pageNumber * pageLength >= pagedDataProvider.getBackendSize()) {
			--pageNumber;
		}
	}

	<F> Query<T, F> alignQuery(Query<T, F> query) {
		return new Query<>(pageNumber * pageLength + query.getOffset(), query.getLimit(), query.getSortOrders(),
				query.getInMemorySorting(), query.getFilter().orElse(null));
	}

	int getSizeOfPage(Query<T, ?> query) {
		int limit = pageLength;
		if (pageNumber == getPageCount() - 1) {
			limit = pagedDataProvider.getBackendSize() - pageLength * pageNumber;
		}
		return limit;
	}
}