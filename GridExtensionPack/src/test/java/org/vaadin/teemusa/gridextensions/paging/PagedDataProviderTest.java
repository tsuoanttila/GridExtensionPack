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
	public void testPageNumberGoingBelow0WhenBackendIsEmpty() {
		Assert.assertEquals(0, controls.getPageNumber());
		dp.setFilter(x -> false);
		Assert.assertEquals(0, dp.size(new Query<>()));
		Assert.assertEquals(0, dp.fetch(new Query<>()).count());
		Assert.assertEquals(0, controls.getPageCount());
		Assert.assertEquals(0, controls.getPageNumber());
	}

	@Test
	public void testOffsetProvidedByQuery() {
		Query<String, SerializablePredicate<String>> zeroOffsetQuery = new Query<>();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(zeroOffsetQuery));

		Query<String, SerializablePredicate<String>> singleOffsetQuery = new Query<>(
				1,
				zeroOffsetQuery.getLimit(),
				zeroOffsetQuery.getSortOrders(),
				zeroOffsetQuery.getInMemorySorting(),
				zeroOffsetQuery.getFilter().orElse(null));
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(singleOffsetQuery));
		Assert.assertEquals(controls.getPageLength() - 1, pagedDP.fetch(singleOffsetQuery).count());

		controls.nextPage();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(singleOffsetQuery));
		Assert.assertEquals(controls.getPageLength() - 1, pagedDP.fetch(singleOffsetQuery).count());
	}

	@Test
	public void testOffsetProvidedByQueryIsOnePageLength() {
		Query<String, SerializablePredicate<String>> zeroOffsetQuery = new Query<>();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(zeroOffsetQuery));

		Query<String, SerializablePredicate<String>> singlePageOffsetQuery = new Query<>(
				controls.getPageLength(),
				zeroOffsetQuery.getLimit(),
				zeroOffsetQuery.getSortOrders(),
				zeroOffsetQuery.getInMemorySorting(),
				zeroOffsetQuery.getFilter().orElse(null));
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(singlePageOffsetQuery));
		Assert.assertEquals(0, pagedDP.fetch(singlePageOffsetQuery).count());
	}

	@Test
	public void testOffsetProvidedByQueryIsTwoPageLengths() {
		Query<String, SerializablePredicate<String>> zeroOffsetQuery = new Query<>();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(zeroOffsetQuery));

		Query<String, SerializablePredicate<String>> twoPageOffsetQuery = new Query<>(
				controls.getPageLength() * 2,
				zeroOffsetQuery.getLimit(),
				zeroOffsetQuery.getSortOrders(),
				zeroOffsetQuery.getInMemorySorting(),
				zeroOffsetQuery.getFilter().orElse(null));
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(twoPageOffsetQuery));
		Assert.assertEquals(0, pagedDP.fetch(twoPageOffsetQuery).count());
	}

	@Test
	public void testLimitProvidedByQuery() {
		Query<String, SerializablePredicate<String>> implicitLimitQuery = new Query<>();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(implicitLimitQuery));

		Query<String, SerializablePredicate<String>> explicitLimitQuery = new Query<>(
				implicitLimitQuery.getOffset(),
				controls.getPageLength() - 1,
				implicitLimitQuery.getSortOrders(),
				implicitLimitQuery.getInMemorySorting(),
				implicitLimitQuery.getFilter().orElse(null));
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(explicitLimitQuery));
		Assert.assertEquals(controls.getPageLength() - 1, pagedDP.fetch(explicitLimitQuery).count());

		controls.nextPage();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(explicitLimitQuery));
		Assert.assertEquals(controls.getPageLength() - 1, pagedDP.fetch(explicitLimitQuery).count());
	}

	@Test
	public void testOffsetAndLimitProvidedByQuery() {
		Query<String, SerializablePredicate<String>> implicitLimitQuery = new Query<>();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(implicitLimitQuery));

		Query<String, SerializablePredicate<String>> explicitLimitQuery = new Query<>(
				1,
				controls.getPageLength() - 2,
				implicitLimitQuery.getSortOrders(),
				implicitLimitQuery.getInMemorySorting(),
				implicitLimitQuery.getFilter().orElse(null));
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(explicitLimitQuery));
		Assert.assertEquals(controls.getPageLength()- 2, pagedDP.fetch(explicitLimitQuery).count());

		controls.nextPage();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(explicitLimitQuery));
		Assert.assertEquals(controls.getPageLength() - 2, pagedDP.fetch(explicitLimitQuery).count());
	}

	@Test
	public void testFilterProvidedByQuery() {
		controls.setPageLength(15);

		Query<String, SerializablePredicate<String>> noFilterQuery = new Query<>();
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(noFilterQuery));
		Assert.assertEquals(controls.getPageLength(), pagedDP.fetch(noFilterQuery).count());

		Query<String, SerializablePredicate<String>> filterQuery = new Query<>(x -> x.startsWith("Item 2"));
		Assert.assertEquals(controls.getPageLength(), pagedDP.size(filterQuery));
		Assert.assertEquals(controls.getPageLength(), pagedDP.fetch(filterQuery).count());

		filterQuery = new Query<>(x -> x.startsWith("Item 3"));
		Assert.assertEquals(11, pagedDP.size(filterQuery));
		Assert.assertEquals(11, pagedDP.fetch(filterQuery).count());
	}

	@Test
	public void testPagingControlsImplementSerializable() {
		Assert.assertTrue(Serializable.class.isAssignableFrom(controls.getClass()));
	}
}
