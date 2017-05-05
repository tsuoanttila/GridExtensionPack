package org.vaadin.teemusa.gridextensions.client.v7compat;

import org.vaadin.teemusa.gridextensions.v7compat.refresher.GridRefresher;

import com.vaadin.client.ServerConnector;
import com.vaadin.shared.ui.Connect;

@Deprecated
@Connect(GridRefresher.class)
public class RefresherConnector extends AbstractGridExtensionConnector {

	@Override
	protected void extend(ServerConnector target) {
		// NO-OP
	}

}
