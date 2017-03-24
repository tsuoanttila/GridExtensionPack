package org.vaadin.teemusa.gridextensions.client.cachestrategy;

import com.vaadin.shared.ui.grid.AbstractGridExtensionState;

public class CacheStrategyState extends AbstractGridExtensionState {

	/**
	 * Max cache size = minSize + pageSize * pageMultiplier
	 */
	public double pageMultiplier;
	
	/**
	 * Min size
	 */
	public int minSize;

	
}
