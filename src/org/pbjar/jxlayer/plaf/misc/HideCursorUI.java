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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

import org.jdesktop.jxlayer.JXLayer;

/**
 * A LayerUI that hides the cursor. After a MouseEvent or MouseMoveEvent the
 * cursor will reappear for some specified time.
 * 
 * @author Piet Blok
 */
public final class HideCursorUI extends
	GeneralLayerUI<JComponent, HideCursorUI.HideCursorState> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Holds state information.
     */
    protected static class HideCursorState {

	private final Timer timer;

	private int timeout;

	private final Cursor oldCursor;

	public HideCursorState(final JXLayer<? extends JComponent> layer, int timeout) {
	    this.timeout = timeout;
	    oldCursor = layer.getGlassPane().getCursor();
	    this.timer = new Timer(timeout, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent event) {
		    layer.getGlassPane().setCursor(nullCursor);
		}
	    });
	    timer.setRepeats(false);
	}

	public int getTimeout() {
	    return timeout;
	}

	public void resetCursor(JXLayer<? extends JComponent> layer) {
	    if (timeout > 0) {
		layer.getGlassPane().setCursor(oldCursor);
		timer.restart();
	    }
	}

	public void setTimeout(int timeout) {
	    this.timeout = timeout;
	    timer.setInitialDelay(timeout);
	}

    }

    private static final Cursor nullCursor = Toolkit.getDefaultToolkit()
	    .createCustomCursor(
		    new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
		    new Point(0, 0), "nullCursor");

    private final int timeout;

    /**
     * Equivalent to {@code HideCursorUI(0)}.
     * 
     * @see #HideCursorUI(int)
     */
    public HideCursorUI() {
	this(0);
    }

    /**
     * Create a HideCursorUI with a specified timeout.
     * If timeout is 0, the cursor will not be hidden.
     * @param timeout the timeout
     */
    public HideCursorUI(int timeout) {
	super();
	this.timeout = timeout;
    }

    /**
     * Get {@link Action}s that:
     * <ol>
     * <li>Set the cursor timeout value.<li>
     * </ol>
     */
    @Override
    public List<Action> getActions(final JXLayer<? extends JComponent> layer) {
	ArrayList<Action> actionList = new ArrayList<Action>();
	actionList.addAll(super.getActions(layer));
	/*
	 * Change the cursor timeout
	 */
	actionList.add(new AbstractAction("Set cursor timeout") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent event) {
		HideCursorState state = HideCursorUI.this.getStateObject(layer);
		Integer timeout = state.getTimeout();
		JSpinner spinner = new JSpinner();
		SpinnerNumberModel model = (SpinnerNumberModel) spinner
			.getModel();
		model.setStepSize(100);
		model.setMinimum(0);
		spinner.setValue(timeout);
		if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
			layer, spinner, "Change cursor timeout",
			JOptionPane.OK_CANCEL_OPTION)) {
		    timeout = (Integer) spinner.getValue();
		    state.setTimeout(timeout);
		}
	    }
	});

	return actionList;
    }

    private void resetCursor(JXLayer<? extends JComponent> layer) {
	getStateObject(layer).resetCursor(layer);
    }

    @Override
    protected void cleanupStateObject(HideCursorState stateObject) {
	stateObject.timer.stop();
    }

    @Override
    protected HideCursorState createStateObject(JXLayer<? extends JComponent> layer) {
	return new HideCursorState(layer, timeout);
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JXLayer<? extends JComponent> layer) {
	super.processMouseEvent(e, layer);
	resetCursor(layer);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e, JXLayer<? extends JComponent> l) {
	super.processMouseMotionEvent(e, l);
	resetCursor(l);
    }

}
