/**
 * Copyright (c) 2009, Piet Blok
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

package org.pbjar.jxlayer.repaint;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import org.jdesktop.swingx.ForwardingRepaintManager;

/**
 * A fall back class for when the SwingX class {@link ForwardingRepaintManager}
 * is not available on the class path.
 * <p>
 * A {@link RepaintManager} that preserves functionality of a wrapped {@code
 * RepaintManager}. All methods will delegate to the wrapped {@code
 * RepaintManager}.
 * </p>
 * <p>
 * When sub classing this class, one must in all overridden methods call the
 * {@code super} method.
 * </p>
 * 
 * @see RepaintManagerUtils
 * @see RepaintManagerProvider
 * @see ForwardingRepaintManager
 * @author Piet Blok
 */
public class WrappedRepaintManager extends RepaintManager {

    /**
     * The wrapped manager.
     */
    private final RepaintManager delegate;

    /**
     * Construct a {@code RepaintManager} wrapping an existing {@code
     * RepaintManager}.
     * 
     * @param delegate
     *            an existing RepaintManager
     */
    public WrappedRepaintManager(RepaintManager delegate) {
	if (delegate == null) {
	    throw new NullPointerException();
	}
	this.delegate = delegate;
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void addDirtyRegion(Applet applet, int x, int y, int w, int h) {
	delegate.addDirtyRegion(applet, x, y, w, h);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
	delegate.addDirtyRegion(c, x, y, w, h);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void addDirtyRegion(Window window, int x, int y, int w, int h) {
	delegate.addDirtyRegion(window, x, y, w, h);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void addInvalidComponent(JComponent invalidComponent) {
	delegate.addInvalidComponent(invalidComponent);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public Rectangle getDirtyRegion(JComponent aComponent) {
	return delegate.getDirtyRegion(aComponent);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public Dimension getDoubleBufferMaximumSize() {
	return delegate.getDoubleBufferMaximumSize();
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public Image getOffscreenBuffer(Component c, int proposedWidth,
	    int proposedHeight) {
	return delegate.getOffscreenBuffer(c, proposedWidth, proposedHeight);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public Image getVolatileOffscreenBuffer(Component c, int proposedWidth,
	    int proposedHeight) {
	return delegate.getVolatileOffscreenBuffer(c, proposedWidth,
		proposedHeight);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public boolean isCompletelyDirty(JComponent aComponent) {
	return delegate.isCompletelyDirty(aComponent);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public boolean isDoubleBufferingEnabled() {
	return delegate.isDoubleBufferingEnabled();
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void markCompletelyClean(JComponent aComponent) {
	delegate.markCompletelyClean(aComponent);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void markCompletelyDirty(JComponent aComponent) {
	delegate.markCompletelyDirty(aComponent);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void paintDirtyRegions() {
	delegate.paintDirtyRegions();
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void removeInvalidComponent(JComponent component) {
	delegate.removeInvalidComponent(component);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void setDoubleBufferingEnabled(boolean aFlag) {
	delegate.setDoubleBufferingEnabled(aFlag);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void setDoubleBufferMaximumSize(Dimension d) {
	delegate.setDoubleBufferMaximumSize(d);
    }

    /**
     * Just delegates. {@inheritDoc}
     */
    @Override
    public void validateInvalidComponents() {
	delegate.validateInvalidComponents();
    }

    /**
     * Get the delegate.
     * 
     * @return the delegate
     */
    public RepaintManager getDelegateManager() {
	return delegate;
    }

}
