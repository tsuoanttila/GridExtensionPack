package org.vaadin.teemusa.gridextensions.paging;

import java.util.stream.IntStream;

import com.vaadin.server.SerializablePredicate;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;

import junit.framework.Assert;

public class PagedDataProviderTest {

	DataProvider<String, SerializablePredicate<String>> dp;
	FilteredDataProvider<String, SerializablePredicate<String>> filteredDP;
	PagedDataProvider<String, SerializablePredicate<String>> pagedDP;

	@Before
	public void initialize() {
		dp = DataProvider.fromStream(IntStream.range(0, 300).boxed().map(i -> "Item " + i));
		filteredDP = new FilteredDataProvider<>(dp, this::and);
		pagedDP = new PagedDataProvider<>(filteredDP);
	}

	<T> SerializablePredicate<T> and(SerializablePredicate<T> left, SerializablePredicate<T> right) {
		if (left != null && right != null) {
			return x -> right.test(x) && left.test(x);
		} else if (left != null) {
			return left;
		} else if (right != null){
			return right;
		} else {
			return null;
		}
	}

	@Test
	public void testPageLengthAligns() {
		PagingControls controls = pagedDP.getPagingControls();
		controls.setPageLength(100);
		Assert.assertEquals("Page should be 0 when page length was changed.", 0, controls.getPageNumber());
		controls.nextPage();
		Assert.assertEquals(1, controls.getPageNumber());
		controls.nextPage();
		Assert.assertEquals(2, controls.getPageNumber());
		controls.nextPage();
		Assert.assertEquals("There is no page number 3", 2, controls.getPageNumber());
	}

	@Test
	public void testPageLengthNotAligned() {
		PagingControls controls = pagedDP.getPagingControls();
		controls.setPageLength(299);
		Assert.assertEquals("Page should be 0 when page length was changed.", 0, controls.getPageNumber());
		controls.nextPage();
		Assert.assertEquals(1, controls.getPageNumber());

		Assert.assertEquals("Last page should have only one item.", 1, pagedDP.size(new Query<>()));

		controls.setPageLength(297);
		Assert.assertEquals("Page should be 0 when page length was changed.", 0, controls.getPageNumber());
		controls.nextPage();
		Assert.assertEquals(1, controls.getPageNumber());

		Assert.assertEquals("Last page should have only three items.", 3, pagedDP.size(new Query<>()));
	}

	@Test
	public void testPageNumberGoingBelow0() {
		PagingControls controls = pagedDP.getPagingControls();
		Assert.assertEquals(0, controls.getPageNumber());
		controls.previousPage();
		Assert.assertEquals(0, controls.getPageNumber());
	}

	@Test
	public void testPageNumberGoingAbovePageCount() {
		PagingControls controls = pagedDP.getPagingControls();
		int pageCount = controls.getPageCount();
		controls.setPageNumber(pageCount - 1);
		Assert.assertEquals(pageCount - 1, controls.getPageNumber());
		controls.nextPage();
		Assert.assertEquals(pageCount - 1, controls.getPageNumber());
	}

	@Test
	public void testPageNumberGoingBelow0Issue() {
		PagingControls controls = pagedDP.getPagingControls();
		Assert.assertEquals(0, controls.getPageNumber());
		filteredDP.setFilter(x -> false);
		Assert.assertEquals(0, filteredDP.size(new Query<>()));
		Assert.assertEquals(0, controls.getPageCount());
		Assert.assertEquals(0, controls.getPageNumber());
	}

}
