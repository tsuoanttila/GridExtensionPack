package org.vaadin.teemusa.gridextensions.client.sidebarmenuextension;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.shared.ui.grid.AbstractGridExtensionState;

/**
 * State class for SidebarMenuExtension.
 *
 * @author Anna Koskinen
 *
 */
public class SidebarMenuExtensionState extends AbstractGridExtensionState {

    public Map<Integer, String> captionMap = new HashMap<Integer, String>();
    public Map<Integer, String> styleMap = new HashMap<Integer, String>();
	public boolean autoClose;

}
