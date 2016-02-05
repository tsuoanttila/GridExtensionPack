package org.vaadin.teemusa.gridextensions.client;

import org.vaadin.teemusa.gridextensions.refresher.GridRefresher;

import com.vaadin.client.ServerConnector;
import com.vaadin.shared.ui.Connect;

@Connect(GridRefresher.class)
public class RefresherConnector extends AbstractGridExtensionConnector {

	@Override
	protected void extend(ServerConnector target) {
		// NO-OP
	}

}
