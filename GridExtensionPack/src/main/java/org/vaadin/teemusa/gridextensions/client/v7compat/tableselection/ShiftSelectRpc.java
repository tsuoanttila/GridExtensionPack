package org.vaadin.teemusa.gridextensions.client.v7compat.tableselection;

import com.vaadin.shared.communication.ServerRpc;

@Deprecated
public interface ShiftSelectRpc extends ServerRpc {

	/**
	 * Selects a range of rows.
	 * 
	 * @param start
	 *            start index
	 * @param length
	 *            row count
	 */
	void selectRange(int start, int length);

}
