package org.vaadin.teemusa.gridextensions.client.wrappinggrid;

import java.util.Arrays;

import org.vaadin.teemusa.gridextensions.wrappinggrid.WrappingGrid;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;

/*
 * To the person reading this: this is a horrible, post-facto layouting
 * hack being applied to the Grid. The illusion WILL break unless you
 * are really careful about what you're doing with it!
 */

@SuppressWarnings("serial")
@Connect(WrappingGrid.class)
public class WrappingGridConnector extends AbstractExtensionConnector {

	private static final int DEFAULT_HEIGHT = 38;

	private Grid<?> grid;

	public WrappingGridConnector() {
		grid = null;
	}

	@Override
	protected void extend(ServerConnector target) {
		grid = (Grid<?>) ((ComponentConnector) target).getWidget();

		WrappingClientRPC rpc = new WrappingClientRPC() {
			@Override
			public void setWrapping(boolean enable) {
				if (enable) {
					applyWrapping();
				} else {
					restoreOriginalSizeRules();
				}
			}
		};

		registerRpc(WrappingClientRPC.class, rpc);
	}

	// Entry point for applying the wrapping cell style rules
	private void applyWrapping() {
		applyWrappingRules();
	}

	// Attempt to restore as much of the original state as possible
	private void restoreOriginalSizeRules() {

		// Remove all existing style from cells and set an explicit height
		for (Element e : getGridParts("th")) {
			String display = e.getStyle().getDisplay();
			e.setAttribute("style", "height: " + DEFAULT_HEIGHT + "px;"
					+ "display: " + display + ";");
		}

		// Restore header row style
		for (Element e : getGridParts("tr", getGridPart("thead"))) {
			String width = e.getStyle().getWidth();
			e.setAttribute("style", "width: " + width + ";");
		}

		// Once reflow has taken place, use our function to measure a new
		// maximum height
		// for all header elements
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				measureAndApplyHeaderSize();

				// Finally, re-remove height from header so it doesn't derp out
				for (Element e : getGridParts("tr", getGridPart("thead"))) {
					String width = e.getStyle().getWidth();
					String display = e.getStyle().getDisplay();
					e.setAttribute("style", "width: " + width + ";"
							+ "display:" + display + ";");
				}
			}
		});
	}

	// Apply CSS rules
	private void applyWrappingRules() {

		// Apply wrapping css rules to cells
		for (Element e : getGridParts("th")) {
			String width = e.getOffsetWidth() + "px";
			String display = e.getStyle().getDisplay();
			e.setAttribute("style", "width: " + width + ";height: 100%;"
					+ "word-wrap: break-word;" + "white-space: normal;"
					+ "overflow: visible;" + "text-overflow: clip;"
					+ "display:" + display + ";");
		}

		// Remove any header row style
		Element head = getGridPart("tr"); // <-- ONLY the first TR!
		String width = head.getStyle().getWidth();
		head.setAttribute("style", "width: " + width + ";");

		// Continue processing after reflow has taken place
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				measureAndApplyHeaderSize();
			}
		});
	}

	// Go through all cells, finding the largest one. Once that's done,
	// adjust all header elements to match that size, and adjust the rest
	// of Grid's parts so that they match the new header height
	private void measureAndApplyHeaderSize() {

		// Measure new cell sizes per header row
		int rownum = 0;
		int[] rowHeight = new int[grid.getHeaderRowCount()];
		Arrays.fill(rowHeight, DEFAULT_HEIGHT);
		for (Element row : getGridParts("tr", getGridPart("thead"))) {
			for (Element cell : getGridParts("th", row)) {
				if (!cell.getStyle().getDisplay().equals("none")) {
					int h = cell.getOffsetHeight();
					if (h > rowHeight[rownum]) {
						rowHeight[rownum] = h;
					}
				}
			}
			++rownum;
		}

		// Apply new header row heights
		int totalHeaderHeight = 0;
		rownum = 0;
		for (Element row : getGridParts("tr", getGridPart("thead"))) {
			row.getStyle().setHeight(rowHeight[rownum], Unit.PX);
			totalHeaderHeight += rowHeight[rownum];
			++rownum;
		}

		// Adjust body start position so header does not cover the data
		Element body = getGridPart("tbody");
		body.getStyle().setMarginTop(totalHeaderHeight, Unit.PX);

		// Adjust scrollbar start position
		Element scroll = findVerticalScrollbar();
		scroll.getStyle().setTop(totalHeaderHeight, Unit.PX);

		// Adjust scrollbar height
		scroll.getStyle().setHeight(grid.getOffsetHeight() - totalHeaderHeight,
				Unit.PX);

		// Adjust header deco element
		Element deco = findHeaderDeco();
		deco.getStyle().setHeight(totalHeaderHeight, Unit.PX);
	}

	// Get elements in Grid by tag name
	private Element[] getGridParts(String elem, Element parent) {
		NodeList<Element> elems = parent.getElementsByTagName(elem);
		Element[] ary = new Element[elems.getLength()];
		for (int i = 0; i < ary.length; ++i) {
			ary[i] = elems.getItem(i);
		}
		return ary;
	}

	private Element[] getGridParts(String elem) {
		NodeList<Element> elems = grid.getElement().getElementsByTagName(elem);
		Element[] ary = new Element[elems.getLength()];
		for (int i = 0; i < ary.length; ++i) {
			ary[i] = elems.getItem(i);
		}
		return ary;
	}

	// Get the first element by tag name
	private Element getGridPart(String elem) {
		return getGridParts(elem)[0];
	}

	// Go through grid parts until we find the so-called "header deco" element
	// This thing sits as the last part of the header and right above the
	// scrollbar
	// and needs to have its size adjusted to match the header.
	private Element findHeaderDeco() {
		for (Element e : getGridParts("div")) {
			if (e.getClassName().contains("v-grid-header-deco"))
				return e;
		}
		return null;
	}

	// Go through grid parts until we find the vertical scrollbar
	private Element findVerticalScrollbar() {
		for (Element e : getGridParts("div")) {
			if (e.getClassName().contains("v-grid-scroller-vertical"))
				return e;
		}
		return null;
	}

}
