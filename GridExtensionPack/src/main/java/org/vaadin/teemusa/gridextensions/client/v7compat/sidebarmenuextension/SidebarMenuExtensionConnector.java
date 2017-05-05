package org.vaadin.teemusa.gridextensions.client.v7compat.sidebarmenuextension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.vaadin.teemusa.gridextensions.v7compat.sidebarmenuextension.SidebarMenuExtension;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.v7.client.connectors.GridConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.v7.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

/**
 * Connector class for SidebarMenuExtension.
 *
 * @author Anna Koskinen
 *
 */
@Connect(SidebarMenuExtension.class)
@Deprecated
public class SidebarMenuExtensionConnector extends AbstractExtensionConnector {

	Map<Integer, MenuItem> menuItemMap = new HashMap<Integer, MenuItem>();
	Map<Integer, String> styleMap = new HashMap<Integer, String>();
	Grid<JsonObject> grid;
	boolean autoClose;

	protected Grid<JsonObject> getGrid() {
		return ((GridConnector) getParent()).getWidget();
	}

	@Override
	protected void extend(ServerConnector target) {
		grid = getGrid();
		registerRpc(SidebarMenuExtensionClientRpc.class, new SidebarMenuExtensionClientRpc() {

			@Override
			public void openSidebarMenu() {
				getGrid().setSidebarOpen(true);
			}

			@Override
			public void closeSidebarMenu() {
				getGrid().setSidebarOpen(false);
			}
		});
	}

	@OnStateChange("captionMap")
	void captionMapChanged() {
		MenuBar sidebarMenu = grid.getSidebarMenu();
		for (Entry<Integer, MenuItem> entry : new HashSet<Entry<Integer, MenuItem>>(menuItemMap.entrySet())) {
			sidebarMenu.removeItem(entry.getValue());
			if (!getState().captionMap.containsKey(entry.getKey())) {
				menuItemMap.remove(entry.getKey());
				styleMap.remove(entry.getKey());
			}
		}
		for (Entry<Integer, String> entry : getState().captionMap.entrySet()) {
			MenuItem menuItem;
			if (menuItemMap.containsKey(entry.getKey())) {
				menuItem = menuItemMap.get(entry.getKey());
				menuItem.setText(entry.getValue());
			} else {
				menuItem = createMenuItem(entry.getKey(), entry.getValue());
			}
			menuItemMap.put(entry.getKey(), menuItem);
			if (styleMap.containsKey(entry.getKey())) {
				menuItem.addStyleName(styleMap.get(entry.getKey()));
			}
			sidebarMenu.addItem(menuItem);
		}
	}

	@OnStateChange("styleMap")
	void styleMapChanged() {
		for (Entry<Integer, String> entry : new HashSet<Entry<Integer, String>>(styleMap.entrySet())) {
			if (menuItemMap.containsKey(entry.getKey())) {
				menuItemMap.get(entry.getKey()).removeStyleName(entry.getValue());
			}
			if (!getState().styleMap.containsKey(entry.getKey())
					|| !getState().styleMap.get(entry.getKey()).equals(entry.getValue())) {
				styleMap.remove(entry.getKey());
			}
		}
		for (Entry<Integer, String> entry : getState().styleMap.entrySet()) {
			styleMap.put(entry.getKey(), entry.getValue());
			if (!menuItemMap.containsKey(entry.getKey())) {
				MenuItem menuItem = createMenuItem(entry.getKey(), getState().captionMap.get(entry.getKey()));
				menuItemMap.put(entry.getKey(), menuItem);
			}
			menuItemMap.get(entry.getKey()).addStyleName(entry.getValue());
		}
	}

	protected MenuItem createMenuItem(final int id, String caption) {
		return new MenuItem(caption, new ScheduledCommand() {

			@Override
			public void execute() {
				getRpcProxy(SidebarMenuExtensionServerRpc.class).click(id);
				if (autoClose) {
					// Close the sidebar menu when item is clicked.
					getGrid().setSidebarOpen(false);
				}
			}
		});
	}

	@OnStateChange("autoClose")
	void updateAutoClose() {
		autoClose = getState().autoClose;
	}

	@Override
	public SidebarMenuExtensionState getState() {
		return (SidebarMenuExtensionState) super.getState();
	}

	@Override
	public void onUnregister() {
		for (MenuItem menuItem : menuItemMap.values()) {
			grid.getSidebarMenu().removeItem(menuItem);
		}

		super.onUnregister();
	}
}