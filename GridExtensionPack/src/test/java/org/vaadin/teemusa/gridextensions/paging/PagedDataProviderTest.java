package org.vaadin.teemusa.gridextensions.paging;

import java.io.Serializable;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializablePredicate;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;

import junit.framework.Assert;

public class PagedDataProviderTest {

	private ListDataProvider<String> dp;
	private PagedDataProvider<String, SerializablePredicate<String>> pagedDP;
	private PagingControls controls;

	@Before
	public void initialize() {
		Stream<String> stream = IntStream.range(0, 300).boxed().map(i -> "Item " + i);
		dp = DataProvider.fromStream(stream);
		pagedDP = new PagedDataProvider<>(dp);
		controls = pagedDP.getPagingControls();
	}

	@Test(expected = IllegalArgumentException.class)
	public  void testNegativePageLength() {
		new PagedDataProvider<>(dp, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public  void testZeroPageLength() {
		new PagedDataProvider<>(dp, 0);
	}

	@Test
	public void testPageLengthPropagation() {
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH, new PagedDataProvider<>(dp).getPagingControls().getPageLength());
		Assert.assertEquals(Integer.MAX_VALUE, new PagedDataProvider<>(dp, Integer.MAX_VALUE).getPagingControls().getPageLength());
	}

	@Test
	public void testPageLengthAligns() {
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
		controls = pagedDP.getPagingControls();
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
		int pageCount = controls.getPageCount();
		controls.setPageNumber(pageCount - 1);
		Assert.assertEquals(pageCount - 1, controls.getPageNumber());
		controls.nextPage();
		Assert.assertEquals(pageCount - 1, controls.getPageNumber());
	}

	@Test
	public void testPageNumberGoingBelow0Issue19() {
		Assert.assertEquals(0, controls.getPageNumber());
		dp.setFilter(x -> false);
		Assert.assertEquals(0, dp.size(new Query<>()));
		Assert.assertEquals(0, controls.getPageCount());
		Assert.assertEquals(0, controls.getPageNumber());
	}

	@Test
	public void testSizeUsesOffsetProvidedByQuery() {
		Query<String, SerializablePredicate<String>> zeroOffsetQuery = new Query<>();
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH, pagedDP.size(zeroOffsetQuery));


		Query<String, SerializablePredicate<String>> singleOffsetQuery = new Query<>(
				1,
				zeroOffsetQuery.getLimit(),
				zeroOffsetQuery.getSortOrders(),
				zeroOffsetQuery.getInMemorySorting(),
				zeroOffsetQuery.getFilter().orElse(null));
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH - 1, pagedDP.size(singleOffsetQuery));

		controls.nextPage();
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH - 1, pagedDP.size(singleOffsetQuery));
	}

	@Test
	public void testSizeUsesLimitProvidedByQuery() {
		Query<String, SerializablePredicate<String>> implicitLimitQuery = new Query<>();
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH, pagedDP.size(implicitLimitQuery));


		Query<String, SerializablePredicate<String>> explicitLimitQuery = new Query<>(
				implicitLimitQuery.getOffset(),
				PagedDataProvider.DEFAULT_PAGE_LENGTH - 1,
				implicitLimitQuery.getSortOrders(),
				implicitLimitQuery.getInMemorySorting(),
				implicitLimitQuery.getFilter().orElse(null));
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH - 1, pagedDP.size(explicitLimitQuery));

		controls.nextPage();
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH - 1, pagedDP.size(explicitLimitQuery));
	}

	@Test
	public void testSizeUsesOffsetAndLimitProvidedByQuery() {
		Query<String, SerializablePredicate<String>> implicitLimitQuery = new Query<>();
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH, pagedDP.size(implicitLimitQuery));


		Query<String, SerializablePredicate<String>> explicitLimitQuery = new Query<>(
				1,
				PagedDataProvider.DEFAULT_PAGE_LENGTH - 1,
				implicitLimitQuery.getSortOrders(),
				implicitLimitQuery.getInMemorySorting(),
				implicitLimitQuery.getFilter().orElse(null));
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH - 2, pagedDP.size(explicitLimitQuery));

		controls.nextPage();
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH - 2, pagedDP.size(explicitLimitQuery));
	}

	@Test
	public void testSizeUsesFilterProvidedByQuery() {
		Query<String, SerializablePredicate<String>> noFilterQuery = new Query<>();
		Assert.assertEquals(PagedDataProvider.DEFAULT_PAGE_LENGTH, pagedDP.size(noFilterQuery));


		Query<String, SerializablePredicate<String>> filterQuery = new Query<>(x -> false);
		Assert.assertEquals(0, pagedDP.size(filterQuery));
	}

	@Test
	public void testPagingControlsImplementSerializable() {
		Assert.assertTrue(Serializable.class.isAssignableFrom(controls.getClass()));
	}
}
