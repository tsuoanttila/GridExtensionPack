package org.vaadin.teemusa.gridextensions.paging;

import com.vaadin.data.provider.Query;

import java.io.Serializable;
import java.math.BigInteger;

public class PagingControls implements Serializable {

	private final PagedDataProvider<?, ?> pagedDataProvider;
	private int pageLength;
	private int pageNumber = 0;

	PagingControls(PagedDataProvider<?, ?> pagedDataProvider, int pageLength) {
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
	 * length. The {@link PagingControls#setPageNumber} can be used to restore position
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
     *             the desired page
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
		while (pageNumber > 0 && pageNumber * pageLength >= pagedDataProvider.getBackendSize()) {
			--pageNumber;
		}
	}

	<T, F> Query<T, F> alignQuery(Query<T, F> query) {
		BigInteger pageNumber = BigInteger.valueOf(this.pageNumber);
		BigInteger pageLength = BigInteger.valueOf(this.pageLength);
		BigInteger queryOffset = BigInteger.valueOf(query.getOffset());
		BigInteger queryLimit = BigInteger.valueOf(query.getLimit());

		BigInteger maxInteger = BigInteger
				.valueOf(Integer.MAX_VALUE);

		BigInteger offset = pageNumber
				.multiply(pageLength)
				.add(queryOffset)
				.max(BigInteger.ZERO);

		BigInteger limit = pageLength
				.min(queryLimit)
				.max(BigInteger.ZERO);

		offset = offset.min(maxInteger)
		 		.max(BigInteger.ZERO);
		limit = limit.subtract(queryOffset)
				.min(maxInteger)
		 		.max(BigInteger.ZERO);

		return new Query<>(
				offset.intValue(),
				limit.intValue(),
				query.getSortOrders(),
				query.getInMemorySorting(),
				query.getFilter().orElse(null));
	}
}