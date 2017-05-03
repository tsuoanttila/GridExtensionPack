package org.vaadin.teemusa.gridextensions;

import java.util.stream.IntStream;

import org.junit.Test;
import org.vaadin.teemusa.gridextensions.paging.PagedDataProvider;
import org.vaadin.teemusa.gridextensions.paging.PagingControls;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;

import junit.framework.Assert;

public class PagedDataProviderTest {

	DataProvider<String, ?> dp = DataProvider.fromStream(IntStream.range(0, 300).boxed().map(i -> "Item " + i));
	PagedDataProvider<String, ?> pagedDP = new PagedDataProvider<>(dp);

	@Test
	public void testPageLengthAligns() {
		PagingControls controls = pagedDP.getPagingControls();
		controls.setPageLength(100);
		Assert.assertTrue("Page should be 0 when page length was changed.", controls.getPageNumber() == 0);
		controls.nextPage();
		Assert.assertTrue(controls.getPageNumber() == 1);
		controls.nextPage();
		Assert.assertTrue(controls.getPageNumber() == 2);
		controls.nextPage();
		Assert.assertTrue("There is no page number 3", controls.getPageNumber() == 2);
	}

	@Test
	public void testPageLengthNotAligned() {
		PagingControls controls = pagedDP.getPagingControls();
		controls.setPageLength(299);
		Assert.assertTrue("Page should be 0 when page length was changed.", controls.getPageNumber() == 0);
		controls.nextPage();
		Assert.assertTrue(controls.getPageNumber() == 1);

		Assert.assertEquals("Last page should have only one item.", 1, pagedDP.size(new Query<>()));

		controls.setPageLength(297);
		Assert.assertTrue("Page should be 0 when page length was changed.", controls.getPageNumber() == 0);
		controls.nextPage();
		Assert.assertTrue(controls.getPageNumber() == 1);

		Assert.assertEquals("Last page should have only three items.", 3, pagedDP.size(new Query<>()));
	}

	@Test
	public void testPageNumberGoingBelow0() {
		PagingControls controls = pagedDP.getPagingControls();
		Assert.assertTrue(controls.getPageNumber() == 0);
		controls.previousPage();
		Assert.assertTrue(controls.getPageNumber() == 0);
	}

	@Test
	public void testPageNumberGoingAbovePageCount() {
		PagingControls controls = pagedDP.getPagingControls();
		int pageCount = controls.getPageCount();
		controls.setPageNumber(pageCount - 1);
		Assert.assertTrue(controls.getPageNumber() == pageCount - 1);
		controls.nextPage();
		Assert.assertTrue(controls.getPageNumber() == pageCount - 1);
	}

}
