package org.vaadin.teemusa.gridextensions.client.cachestrategy;

import org.vaadin.teemusa.gridextensions.cachestrategy.CacheStrategyExtension;
import org.vaadin.teemusa.gridextensions.client.AbstractGridExtensionConnector;

import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.data.AbstractRemoteDataSource;
import com.vaadin.client.data.CacheStrategy;
import com.vaadin.client.data.DataSource;
import com.vaadin.client.widget.grid.DataAvailableEvent;
import com.vaadin.client.widget.grid.DataAvailableHandler;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(CacheStrategyExtension.class)
public class CacheStrategyExtensionConnector extends AbstractGridExtensionConnector {

	private Grid<JsonObject> grid;
	private HandlerRegistration registration;
	private int minSize;
	private double pageMultiplier;

	@Override
	protected void extend(ServerConnector target) {
		grid = getGrid();

		// Probably a better way to change cache strategy, but the datasource
		// isn't available yet.
		registration = grid.addDataAvailableHandler(new DataAvailableHandler() {

			@Override
			public void onDataAvailable(DataAvailableEvent event) {
				setCacheStrategy();
				registration.removeHandler();
			}
		});
	}

	protected void setCacheStrategy() {
		DataSource<JsonObject> dataSource = grid.getDataSource();
		if (!(dataSource instanceof AbstractRemoteDataSource)) {
			// No CacheStrategy to set..
			return;
		}
		AbstractRemoteDataSource<JsonObject> ds = (AbstractRemoteDataSource<JsonObject>) dataSource;
		ds.setCacheStrategy(new CacheStrategy.AbstractBasicSymmetricalCacheStrategy() {

			@Override
			public int getMinimumCacheSize(int pageSize) {
				return minSize;
			}

			@Override
			public int getMaximumCacheSize(int pageSize) {
				// These values actually come from the extensions state,
				// so can be set server side
				return minSize + (int) (pageMultiplier * pageSize);
			}
		});
	}
	
	@OnStateChange("minSize")
	void updateMinSize() {
		minSize = getState().minSize;
	}
	
	@OnStateChange("minSize")
	void updatePageMultiplier() {
		pageMultiplier = getState().pageMultiplier;
	}	

	@Override
	public CacheStrategyState getState() {
		return (CacheStrategyState) super.getState();
	}
}
