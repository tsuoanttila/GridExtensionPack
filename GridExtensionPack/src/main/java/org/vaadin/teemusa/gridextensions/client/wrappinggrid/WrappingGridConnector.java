package org.vaadin.teemusa.gridextensions.client.wrappinggrid;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.widgets.Grid;
import org.vaadin.teemusa.gridextensions.wrappinggrid.WrappingGrid;
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
			public void enableWrapping() {
				applyWrapping();
			}

			@Override
			public void disableWrapping() {
				restoreOriginalSizeRules();
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
		int rows = grid.getHeaderRowCount();
		int columns = grid.getColumnCount();

		// Remove all existing style from cells and set an explicit height
		for (Element e : getGridParts("th")) {
			e.setAttribute("style", "height: " + DEFAULT_HEIGHT + "px;");
		}

		// Restore header row style
		Element head = getGridPart("tr");   // <-- ONLY the first TR!
		String width = head.getStyle().getWidth();
		head.setAttribute("style", "width: " + width + ";");

		// Once reflow has taken place, use our function to measure a new
		// maximum height
		// for all header elements
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				measureAndApplyHeaderSize();

				// Finally, re-remove height from header so it doesn't derp out
				Element head = getGridPart("tr");
				String width = head.getStyle().getWidth();
				head.setAttribute("style", "width: " + width + ";");
			}
		});
	}

	// Apply CSS rules
	private void applyWrappingRules() {

		// Apply wrapping css rules to cells
		for (Element e : getGridParts("th")) {
			String width = e.getStyle().getWidth();
			e.setAttribute("style", ""
					+ "height: 100%;"
					+ "word-wrap: break-word;"
					+ "white-space: normal;"
					+ "overflow: visible;"
					+ "text-overflow: clip;");
		}
		
		// Remove any header row style
		Element head = getGridPart("tr");   // <-- ONLY the first TR!
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
		// Measure new cell sizes
		int maxHeight = 0;
		for (Element e : getGridParts("th")) {
			int h = e.getOffsetHeight();
			if (h > maxHeight) {
				maxHeight = h;
			}
		}

		// Set header height as a hard value
		Element head = getGridPart("tr");
		head.getStyle().setHeight(maxHeight, Unit.PX);

		// Adjust body start position so header does not cover the data
		Element body = getGridPart("tbody");
		body.getStyle().setMarginTop(maxHeight, Unit.PX);

		// Adjust scrollbar start position
		Element scroll = findVerticalScrollbar();
		scroll.getStyle().setTop(maxHeight, Unit.PX);
		
		// Adjust scrollbar height
		scroll.getStyle().setHeight(grid.getOffsetHeight()-maxHeight, Unit.PX);

		// Adjust header deco element
		Element deco = findHeaderDeco();
		deco.getStyle().setHeight(maxHeight, Unit.PX);
	}

	// Get elements in Grid by tag name
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
