package org.vaadin.teemusa.gridextensions.client.v7compat.cachestrategy;

import com.vaadin.shared.communication.SharedState;

public class CacheStrategyState extends SharedState {

	/**
	 * Max cache size = minSize + pageSize * pageMultiplier
	 */
	public double pageMultiplier;
	
	/**
	 * Min size
	 */
	public int minSize;

	
}
