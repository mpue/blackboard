/**
 * Copyright (c) 2008-2009, Piet Blok
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the copyright holder nor the names of the
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pbjar.jxlayer.plaf.misc;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

/**
 * A generalized implementation of LayerUI.
 * 
 * <p>
 * Some additional functionality:
 * <ol>
 * <li>It facilitates the use of a state object for {@code LayerUI}s that are
 * intended for shared use.</li>
 * <li>It supplies {@code Action}s that can be used for the controls of a GUI.</li>
 * <li>It re-dispatches {@code MouseWheelEvent}s to the first component up in
 * the hierarchy from the originating component that has a {@code
 * MouseWheelListener} registered.</li>
 * <li>Via the constructor one can enable {@link AWTEventListener}.</li>
 * </ol>
 * </p>
 * 
 * @author Piet Blok
 * 
 * @param <V>
 *            JXLayer's view
 * @param <S>
 *            A state object
 */
public class GeneralLayerUI<V extends JComponent, S extends Object> extends
	AbstractLayerUI<V> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String stateKey = this.getClass().getName() + ".stateKey";

    /**
     * Get actions that are applicable to this LayerUI. This implementation
     * returns an enable/disable action for this LayerUI
     * 
     * @return a list of applicable actions
     */
    public List<Action> getActions() {
	ArrayList<Action> actionList = new ArrayList<Action>();
	return actionList;
    }

    /**
     * Get actions that are applicable to this LayerUI and a specific JXLayer.
     * This implementation returns an empty list
     * 
     * @param layer
     *            the JXLayer
     * 
     * @return a list of applicable actions
     */
    public List<Action> getActions(JXLayer<? extends V> layer) {
	ArrayList<Action> actionList = new ArrayList<Action>();
	return actionList;
    }

    /**
     * Returns the simple class name.
     * 
     * @return a name
     */
    public String getName() {
	return this.getClass().getSimpleName();
    }

    /**
     * Invokes super.installUI. Then invokes createStateObject. User
     * installation actions may be coded in createStateObject.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void installUI(JComponent c) {
	super.installUI(c);
	JXLayer<? extends V> layer = (JXLayer<? extends V>) c;
	S stateObject = createStateObject(layer);
	if (stateObject != null) {
	    layer.putClientProperty(stateKey, stateObject);
	}
    }

    /**
     * Invokes super.uninstallUI. If a state object is available, the method
     * {@code GeneralLayerUI#cleanupStateObject(Object)} is invoked to cleanup
     * the state object.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void uninstallUI(JComponent c) {
	super.uninstallUI(c);
	JXLayer<? extends V> layer = (JXLayer<? extends V>) c;
	S stateObject = getStateObject(layer);
	if (stateObject != null) {
	    cleanupStateObject(stateObject);
	    layer.putClientProperty(stateKey, null);
	}
    }

    private Component findWheelListenerComponent(Component target) {
	if (target == null) {
	    return null;
	} else if (target.getMouseWheelListeners().length == 0) {
	    return findWheelListenerComponent(target.getParent());
	} else {
	    return target;
	}
    }

    /**
     * Cleanup the state object. The default implementation does nothing.
     * 
     * @param stateObject
     *            a state object
     */
    protected void cleanupStateObject(S stateObject) {

    }

    /**
     * Create a StateObject specific for this LayerUI and the JXLayer argument.
     * The default implementation returns {@code null}.
     * 
     * @param layer
     *            the JXLayer
     * @return a StateObject or {@code null}, if no state is maintained.
     */
    protected S createStateObject(JXLayer<? extends V> layer) {
	return null;
    }

    /**
     * Get the created StateObject specific to the JXLayer argument.
     * 
     * @param layer
     *            the JXLayer
     * @return the StateObject or {@code null}, if
     *         {@link #createStateObject(JXLayer)} returned null
     */
    @SuppressWarnings("unchecked")
    protected final S getStateObject(JXLayer<? extends V> layer) {
	return (S) layer.getClientProperty(stateKey);
    }

    /**
     * Re-dispatches the event to the first component in the hierarchy that has
     * a {@code MouseWheelEventListener} registered.
     */
    @Override
    protected void processMouseWheelEvent(MouseWheelEvent event,
	    JXLayer<? extends V> jxlayer) {
	/*
	 * Only process an event if it is not already consumed. This may be the
	 * case if this LayerUI is contained in a wrapped hierarchy.
	 */
	if (!event.isConsumed()) {
	    /*
	     * Since we will create a new event, the argument event must be
	     * consumed.
	     */
	    event.consume();
	    /*
	     * Find a target up in the hierarchy that has
	     * MouseWheelEventListeners registered.
	     */
	    Component target = event.getComponent();
	    Component newTarget = findWheelListenerComponent(target);
	    if (newTarget == null) {
		newTarget = jxlayer.getParent();
	    }
	    /*
	     * Convert the location relative to the new target
	     */
	    Point point = SwingUtilities.convertPoint(event.getComponent(),
		    event.getPoint(), newTarget);
	    /*
	     * Create a new event
	     */
	    MouseWheelEvent newEvent = new MouseWheelEvent(newTarget, //
		    event.getID(), //
		    event.getWhen(), //
		    event.getModifiers(), //
		    point.x, //
		    point.y, //
		    event.getClickCount(), //
		    event.isPopupTrigger(), //
		    event.getScrollType(), //
		    event.getScrollAmount(), //
		    event.getWheelRotation() //
	    );
	    /*
	     * Dispatch the new event.
	     */
	    newTarget.dispatchEvent(newEvent);
	}
    }

}
