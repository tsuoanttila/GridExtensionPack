package org.vaadin.teemusa.gridextensions.sidebarmenuextension;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.teemusa.gridextensions.client.sidebarmenuextension.SidebarMenuExtensionServerRpc;
import org.vaadin.teemusa.gridextensions.client.sidebarmenuextension.SidebarMenuExtensionState;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.AbstractGridExtension;

/**
 * Server-side class for SidebarMenuExtension.
 *
 * @author Anna Koskinen
 *
 */
public class SidebarMenuExtension extends AbstractGridExtension {

    public Map<Integer, Command> idToCommandMap = new HashMap<Integer, Command>();
    public Map<Command, Integer> commandToIdMap = new HashMap<Command, Integer>();
    Integer nextId = 0;

    protected SidebarMenuExtension(Grid grid) {
        super(grid);
        registerRpc(new SidebarMenuExtensionServerRpc() {

            @Override
            public void click(Integer id) {
                Command command = idToCommandMap.get(id);
                if (command != null) {
                    command.trigger();
                }
            }
        });
    }

    public void addCommand(Command command, String caption) {
        Integer id;
        if (!commandToIdMap.containsKey(command)) {
            id = nextId;
            ++nextId;
            commandToIdMap.put(command, id);
            idToCommandMap.put(id, command);
        } else {
            id = commandToIdMap.get(command);
        }
        getState().captionMap.put(id, caption);
    }

    public void removeCommand(Command command) {
        Integer id = commandToIdMap.remove(command);
        if (id != null) {
            idToCommandMap.remove(id);
            getState().captionMap.remove(id);
            getState().styleMap.remove(id);
        }
    }

    public void setCaption(Command command, String caption) {
        Integer id = commandToIdMap.get(command);
        if (id != null) {
            getState().captionMap.put(id, caption);
        }
    }

    public void setStyle(Command command, String styleName) {
        Integer id = commandToIdMap.get(command);
        if (id != null) {
            getState().styleMap.put(id, styleName);
        }
    }

    public void removeStyle(Command command) {
        Integer id = commandToIdMap.get(command);
        if (id != null) {
            getState().styleMap.remove(id);
        }
    }

    public String getStyle(Command command) {
        Integer id = commandToIdMap.get(command);
        if (id != null) {
            return getState().styleMap.get(id);
        }
        return null;
    }

    @Override
    protected SidebarMenuExtensionState getState() {
        return (SidebarMenuExtensionState) super.getState();
    }

    public interface Command {
        public void trigger();
    }

    /**
     * Constructs a new SidebarMenuExtension and extend given Grid with it.
     * 
     * @param grid
     *            parent grid for extension
     * @return constructed extension
     */
    public static SidebarMenuExtension extend(Grid grid) {
        return new SidebarMenuExtension(grid);
    }
}
