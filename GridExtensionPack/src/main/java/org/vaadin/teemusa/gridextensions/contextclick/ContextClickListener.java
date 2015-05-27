package org.vaadin.teemusa.gridextensions.contextclick;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.vaadin.util.ReflectTools;

/**
 * Listener interface for {@link ContextClickEvent}s.
 * 
 * @author Teemu Suo-Anttila
 */
public interface ContextClickListener extends Serializable {

    public static final Method CONTEXT_CLICK_METHOD = ReflectTools
            .findMethod(ContextClickListener.class, "contextClick",
                    ContextClickEvent.class);

    public void contextClick(ContextClickEvent event);
}
