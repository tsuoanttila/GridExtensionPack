package org.vaadin.teemusa.gridextensions.client.v7compat.wrappinggrid;

import java.util.Arrays;

import org.vaadin.teemusa.gridextensions.v7compat.wrappinggrid.WrappingGrid;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.v7.client.widget.grid.events.ColumnResizeEvent;
import com.vaadin.v7.client.widget.grid.events.ColumnResizeHandler;
import com.vaadin.v7.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;

/*
 * To the person reading this: this is a horrible, post-facto layouting
 * hack being applied to the Grid. The illusion WILL break unless you
 * are really careful about what you're doing with it!
 */

@SuppressWarnings("serial")
@Connect(WrappingGrid.class)
@Deprecated
public class WrappingGridConnector extends AbstractExtensionConnector {

	protected static int DEFAULT_HEIGHT = 38;

	private static native double getWidth(Element e) /*-{
														return e.offsetWidth;
														}-*/;

	private static native double getHeight(Element e) /*-{
														return e.offsetHeight;
														}-*/;

	private static native double getNaturalHeight(Element e) /*-{
																var cssh = e.style.height;
																e.style.height = "";
																var h = 0;
																if(e.children.length > 0) {
																h = e.children[0].offsetHeight;
																} else {
																h = e.offsetHeight;
																}
																e.style.height = cssh;
																return h;
																}-*/;

	private static native void addWrappingRules(Element e) /*-{
															e.style.height = "100%";
															e.style.wordWrap = "break-word";
															e.style.whiteSpace = "normal";
															e.style.overflow = "visible";
															e.style.texOverflow = "clip";
															}-*/;

	private static native void removeWrappingRules(Element e) /*-{
																e.style.height = @org.vaadin.teemusa.gridextensions.client.wrappinggrid.WrappingGridConnector::DEFAULT_HEIGHT;
																e.style.wordWrap = "";
																e.style.whiteSpace = "";
																e.style.overflow = "";
																e.style.texOverflow = "";
																}-*/;

	private Grid<?> grid;
	private boolean wrappingEnabled;
	private HandlerRegistration resizeHandler;

	public WrappingGridConnector() {
		grid = null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void extend(ServerConnector target) {
		grid = (Grid<?>) ((ComponentConnector) target).getWidget();

		wrappingEnabled = false;
		WrappingClientRPC rpc = new WrappingClientRPC() {
			@Override
			public void setWrapping(boolean enable, int defaultRowHeight) {
				if (wrappingEnabled != enable) {
					wrappingEnabled = enable;
					DEFAULT_HEIGHT = defaultRowHeight;
					if (enable) {
						// Figure out default header height
						applyStyle.execute(0);
					} else {
						disableWrapping();
					}
				}
			}
		};

		registerRpc(WrappingClientRPC.class, rpc);

		resizeHandler = grid.addColumnResizeHandler(new ColumnResizeHandler() {
			@Override
			public void onColumnResize(ColumnResizeEvent event) {
				Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						AnimationScheduler.get().requestAnimationFrame(applyStyle);
					}
				});
			}
		});
	}

	@Override
	public void onUnregister() {
		assert (resizeHandler != null);
		resizeHandler.removeHandler();
		disableWrapping();
		super.onUnregister();
	}

	/**
	 * Go through all elements and remove wrapping rules, restoring normalcy
	 */
	private void disableWrapping() {
		for (Element e : getGridParts("th")) {
			removeWrappingRules(e);
		}

		for (Element row : getGridParts("tr", getGridPart("thead"))) {
			row.getStyle().setHeight(DEFAULT_HEIGHT, Unit.PX);
		}

		setBodyStartY(grid.getHeaderRowCount() * DEFAULT_HEIGHT);
	}

	/**
	 * Assume we're in a state where we can do all the necessary measurement and
	 * CSS changes to facilitate wrapping style application
	 */
	private AnimationCallback applyStyle = new AnimationCallback() {
		@Override
		public void execute(double timestamp) {
			if (!wrappingEnabled) {
				return;
			}

			for (Element e : getGridParts("th")) {
				addWrappingRules(e);
			}

			double[] heights = measureRowHeights();
			double startY = setHeaderHeight(heights);
			setBodyStartY(startY);
			AnimationScheduler.get().requestAnimationFrame(applyScrollBarHeight);
		}
	};

	/**
	 * Scroll bar height adjustment cannot be done in same animation frame hence
	 * create own animation frame for it
	 */
	private AnimationCallback applyScrollBarHeight = new AnimationCallback() {
		@Override
		public void execute(double timestamp) {
			if (!wrappingEnabled) {
				return;
			}

			double[] heights = measureRowHeights();
			double startY = setHeaderHeight(heights);
			adjustScrollBarHeight(startY);
		}
	};

	/**
	 * Find maximum cell height per header row. A header row is at least as high
	 * as defined by {@link #DEFAULT_HEIGHT}.
	 * 
	 * @return
	 */
	private double[] measureRowHeights() {
		int rownum = 0;
		double[] rowHeight = new double[grid.getHeaderRowCount()];
		Arrays.fill(rowHeight, DEFAULT_HEIGHT);
		for (Element row : getGridParts("tr", getGridPart("thead"))) {
			for (Element cell : getGridParts("th", row)) {
				if (!cell.getStyle().getDisplay().equals("none")) {
					double h = getNaturalHeight(cell);
					if (h > rowHeight[rownum]) {
						rowHeight[rownum] = h;
					}
				}
			}
			++rownum;
		}
		return rowHeight;
	}

	/**
	 * Apply header height
	 * 
	 * @param rowHeights
	 * @return
	 */
	private double setHeaderHeight(double[] rowHeights) {
		double totalHeaderHeight = 0;
		int rownum = 0;
		for (Element row : getGridParts("tr", getGridPart("thead"))) {
			row.getStyle().setHeight(rowHeights[rownum], Unit.PX);
			totalHeaderHeight += getHeight(row);
			++rownum;
		}
		return totalHeaderHeight;
	}

	/**
	 * 
	 * @param startY
	 * @return
	 */
	private void setBodyStartY(double startY) {

		// Adjust body position
		Element body = getGridPart("tbody");
		body.getStyle().setMarginTop(startY, Unit.PX);

		// Adjust deco position
		for (Element e : getGridParts("div")) {
			if (e.getClassName().contains("v-grid-header-deco")) {
				e.getStyle().setHeight(startY, Unit.PX);
				break;
			}
		}

		// Adjust scrollbar position
		for (Element e : getGridParts("div")) {
			if (e.getClassName().contains("v-grid-scroller-vertical")) {
				e.getStyle().setTop(startY, Unit.PX);
				break;
			}
		}

	}

	private void adjustScrollBarHeight(double startY) {
		// Adjust scrollbar position
		double scrollHeight = 0;

		for (Element e : getGridParts("div")) {
			if (e.getClassName().contains("v-grid-scroller-vertical")) {
				double gridh = getGridPart("table").getParentElement().getOffsetHeight();
				scrollHeight = gridh - startY;
				e.getStyle().setHeight(scrollHeight, Unit.PX);
				break;
			}
		}
	}

	// Get elements in Grid by tag name relative to parent element
	private Element[] getGridParts(String elem, Element parent) {
		NodeList<Element> elems = parent.getElementsByTagName(elem);
		Element[] ary = new Element[elems.getLength()];
		for (int i = 0; i < ary.length; ++i) {
			ary[i] = elems.getItem(i);
		}
		return ary;
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

}
