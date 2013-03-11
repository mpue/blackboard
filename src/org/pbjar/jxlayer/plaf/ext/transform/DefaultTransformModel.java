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

package org.pbjar.jxlayer.plaf.ext.transform;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.jxlayer.JXLayer;

/**
 * This is an implementation of {@link TransformModel} with methods to
 * explicitly set transformation values.
 * 
 * @author Piet Blok
 */
public class DefaultTransformModel implements TransformModel {

    /**
     * Enum for internal convenience.
     * 
     * Describes the values that on change trigger recalculation of the
     * transform. All have a default value, used for initializing arrays.
     * 
     * These enums are used for two purposes:
     * 
     * 1: To easily detect a change that requires renewed calculation of the
     * transform (both program values and user options).
     * 
     * 2: To generalize setters (both program values and user options) and
     * getters (only userOptions) for the various values.
     * 
     * There are two groups:
     * 
     * 1: Program values that reflect the current size etc. of affected
     * components
     * 
     * 2: User options
     */
    private enum Type {
	/*
	 * Program values
	 */
	LayerWidth(0),

	LayerHeight(0),

	ViewWidth(0),

	ViewHeight(0),

	/*
	 * User options
	 */
	PreferredScale(1.0),

	Rotation(0.0),

	ShearX(0.0),

	ShearY(0.0),

	QuadrantRotation(0),

	PreserveAspectRatio(Boolean.TRUE),

	ScaleToPreferredSize(Boolean.FALSE),

	Mirror(Boolean.FALSE),

	;

	public static Object[] createArray() {
	    Object[] array = new Object[values().length];
	    for (Type type : values()) {
		array[type.ordinal()] = type.defaultValue;
	    }
	    return array;
	}

	private Object defaultValue;

	private Type(Object defaultValue) {
	    this.defaultValue = defaultValue;
	}
    }

    private final Map<ChangeListener, Object> listeners = new WeakHashMap<ChangeListener, Object>();

    /**
     * The transform object that will be recalculated upon any change.
     */
    private final AffineTransform transform = new AffineTransform();

    /**
     * Is populated with the current values.
     */
    private final Object[] values = Type.createArray();

    /**
     * Is populated with the previous values.
     */
    private final Object[] prevValues = Type.createArray();

    @Override
    public void addChangeListener(ChangeListener listener) {
	listeners.put(listener, null);
    }

    /**
     * Get the scale.
     * 
     * @return the scale
     * @see #setScale(double)
     */    
    public double getScale() {        
    	return (Double)getValue(Type.PreferredScale);
    }

    @Override
    public AffineTransform getPreferredTransform(Dimension size,
	    JXLayer<?> layer) {
	double centerX = size == null ? 0 : size.getWidth() / 2.0;
	double centerY = size == null ? 0 : size.getHeight() / 2.0;
	AffineTransform transform = transformNoScale(centerX, centerY);
    @SuppressWarnings("unchecked")
	double scaleX = (Double)getValue(Type.PreferredScale);
	double scaleY = scaleX;
	transform.translate(centerX, centerY);
	
	double val1 = (Double)((Boolean)getValue(Type.Mirror) ? -scaleX : scaleX);
	double val2 = (Double) scaleY;
	transform.scale(val1,val2);
	transform.translate(-centerX, -centerY);
	return transform;
    }

    /**
     * Get the quadrant rotation value. The default value is {@code 0}.
     * 
     * @return the quadrant rotation value
     * @see #setQuadrantRotation(int)
     */
    public int getQuadrantRotation() {
	return (Integer)getValue(Type.QuadrantRotation);
    }

    /**
     * Get the rotation value in radians as set by {@link #setRotation(double)}.
     * The default value is {@code 0}.
     * 
     * @return the rotation value.
     * @see #setRotation(double)
     */
    public double getRotation() {
	return (Double)getValue(Type.Rotation);
    }

    /**
     * Get the shearX value as set by {@link #setShearX(double)}; The default
     * value is {@code 0}.
     * 
     * @return the shear x value
     * @see #setShearX(double)
     */
    public double getShearX() {
	return (Double)getValue(Type.ShearX);
    }

    /**
     * Get the shearY value as set by {@link #setShearY(double)}; The default
     * value is {@code 0}.
     * 
     * @return the shear y value
     * @see #setShearY(double)
     */
    public double getShearY() {
	return (Double)getValue(Type.ShearY);
    }

    /**
     * Return the currently active {@link AffineTransform}. Recalculate if
     * needed.
     * 
     * @return the currently active {@link AffineTransform}
     */
    @Override
    public AffineTransform getTransform(JXLayer<? extends JComponent> layer) {
	JComponent view = layer.getView();
	/*
	 * Set the current actual program values in addition to the user
	 * options.
	 */
	setValue(Type.LayerWidth, layer == null ? 0 : layer.getWidth());
	setValue(Type.LayerHeight, layer == null ? 0 : layer.getHeight());
	setValue(Type.ViewWidth, view == null ? 0 : view.getWidth());
	setValue(Type.ViewHeight, view == null ? 0 : view.getHeight());
	/*
	 * If any change to previous values, recompute the transform.
	 */
	if (!Arrays.equals(prevValues, values)) {
	    System.arraycopy(values, 0, prevValues, 0, values.length);
	    transform.setToIdentity();
	    if (view != null) {
		double centerX = layer == null ? 0 : layer.getWidth() / 2.0;
		double centerY = layer == null ? 0 : layer.getHeight() / 2.0;

		AffineTransform nonScaledTransform = transformNoScale(centerX,
			centerY);

		double scaleX, scaleY;
		if ((Boolean)getValue(Type.ScaleToPreferredSize)) {
		    scaleX = (Double)getValue(Type.PreferredScale);
		    scaleY = scaleX;
		} else {
		    Area area = new Area(new Rectangle2D.Double(0, 0, view
			    .getWidth(), view.getHeight()));
		    area.transform(nonScaledTransform);
		    Rectangle2D bounds = area.getBounds2D();
		    scaleX = layer == null ? 0 : layer.getWidth()
			    / bounds.getWidth();
		    scaleY = layer == null ? 0 : layer.getHeight()
			    / bounds.getHeight();

		    if ((Boolean)getValue(Type.PreserveAspectRatio)) {
			scaleX = Math.min(scaleX, scaleY);
			scaleY = scaleX;
		    }
		}

		transform.translate(centerX, centerY);
		transform.scale((Boolean)getValue(Type.Mirror) ? -scaleX : scaleX,
			scaleY);
		transform.translate(-centerX, -centerY);

		transform.concatenate(nonScaledTransform);
	    }
	}
	return transform;
    }

    /**
     * Get the mirror property.
     * <p>
     * The default value is {@code false}.
     * </p>
     * 
     * @return {@code true} if the transformation will mirror the view.
     * @see #setMirror(boolean)
     */
    public boolean isMirror() {
	return (Boolean)getValue(Type.Mirror);
    }

    /**
     * Get the preserve aspect ratio value.
     * <p>
     * The default value is {@code true}.
     * </p>
     * 
     * @return {@code true} if preserving aspect ratio, {@code false} otherwise
     * @see #setPreserveAspectRatio(boolean)
     */
    public boolean isPreserveAspectRatio() {
	return (Boolean)getValue(Type.PreserveAspectRatio);
    }

    /**
     * Get the scale to preferred size value.
     * <p>
     * The default value is {@code false}.
     * </p>
     * <p>
     * When {@code true}, the view is scaled according to the preferred scale,
     * regardless of the size of the {@link JXLayer}.
     * </p>
     * <p>
     * When {@code false}, the view is scaled to occupy as much as possible of
     * the size of the {@link JXLayer}.
     * </p>
     * 
     * @return {@code true} if scale to preferred size, {@code false} otherwise
     * @see #setScaleToPreferredSize(boolean)
     */
    public boolean isScaleToPreferredSize() {
	return (Boolean)getValue(Type.ScaleToPreferredSize);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
	listeners.remove(listener);
    }

    /**
     * Set the mirror property.
     * <p>
     * The default value is {@code false}
     * </p>
     * 
     * @param newValue
     *            the new value
     * @see #isMirror()
     */
    public void setMirror(boolean newValue) {
	setValue(Type.Mirror, newValue);
    }

    /**
     * Set a scale.
     * <p>
     * The scale is primarily used to calculate a preferred size. Unless {@code
     * ScaleToPreferredSize} is set to {@code true} (see
     * {@link #setScaleToPreferredSize(boolean)} and
     * {@link #isScaleToPreferredSize()}), actual scaling itself is calculated
     * such that the view occupies as much space as possible on the
     * {@link JXLayer}.
     * </p>
     * <p>
     * The default value is 1.
     * </p>
     * 
     * @param newValue
     *            the preferred scale
     * @throws IllegalArgumentException
     *             when the argument value is 0
     * @see #getScale()
     */
    public void setScale(double newValue) throws IllegalArgumentException {
	if (newValue == 0.0) {
	    throw new IllegalArgumentException(
		    "Preferred scale can not be set to 0");
	}
	setValue(Type.PreferredScale, newValue);
    }

    /**
     * Set preserve aspect ratio.
     * <p>
     * The default value is {@code true}.
     * </p>
     * 
     * @param newValue
     *            the new value
     * @see #isPreserveAspectRatio()
     */
    public void setPreserveAspectRatio(boolean newValue) {
	setValue(Type.PreserveAspectRatio, newValue);
    }

    /**
     * Set the rotation in quadrants. The default value is {@code 0}.
     * 
     * @param newValue
     *            the number of quadrants
     * @see #getQuadrantRotation()
     */
    public void setQuadrantRotation(int newValue) {
	setValue(Type.QuadrantRotation, newValue);
    }

    /**
     * Set the rotation in radians. The default value is {@code 0}.
     * 
     * @param newValue
     *            the rotation in radians
     * @see #getRotation()
     */
    public void setRotation(double newValue) {
	setValue(Type.Rotation, newValue);
    }

    /**
     * Set scaleToPreferredSize.
     * <p>
     * The default value is {@code false}.
     * </p>
     * <p>
     * When {@code true}, the view is scaled according to the preferred scale,
     * regardless of the size of the {@link JXLayer}.
     * </p>
     * <p>
     * When {@code false}, the view is scaled to occupy as much as possible of
     * the size of the {@link JXLayer}.
     * </p>
     * 
     * @param newValue
     *            the new value
     * @see #isScaleToPreferredSize()
     */
    public void setScaleToPreferredSize(boolean newValue) {
	setValue(Type.ScaleToPreferredSize, newValue);
    }

    /**
     * Set the shearX value. The default value is {@code 0}.
     * 
     * @param newValue
     *            the shear x
     * @see #getShearX()
     */
    public void setShearX(double newValue) {
	setValue(Type.ShearX, newValue);
    }

    /**
     * Set the shearY value. The default value is {@code 0}.
     * 
     * @param newValue
     *            the shear y
     * @see #getShearY()
     */
    public void setShearY(double newValue) {
	setValue(Type.ShearY, newValue);
    }

    /**
     * If {!oldValue.equals(newValue)}, a {@link ChangeEvent} will be fired.
     * 
     * @param oldValue
     *            an old value
     * @param newValue
     *            a new value
     */
    protected void fireChangeEvent(Object oldValue, Object newValue) {
	if (!oldValue.equals(newValue)) {
	    ChangeEvent event = new ChangeEvent(this);
	    for (ChangeListener listener : listeners.keySet()) {
		listener.stateChanged(event);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(Type type) {
    	return (T)values[type.ordinal()];
    }

    /**
     * Set a value and fire a PropertyChange.
     * 
     * @param type
     *            the value type
     * @param newValue
     *            the new value
     */
    private void setValue(Type type, Object newValue) {
	Object oldValue = values[type.ordinal()];
	values[type.ordinal()] = newValue;
	fireChangeEvent(oldValue, newValue);
    }

    /**
     * Apply the prescribed transformations, excluding the scale.
     * 
     * @param centerX
     *            a center X
     * @param centerY
     *            a center Y
     * @return a new {@link AffineTransform}
     */
    private AffineTransform transformNoScale(double centerX, double centerY) {
	AffineTransform at = new AffineTransform();
	at.translate(centerX, centerY);
	at.rotate(getRotation());
	at.quadrantRotate(getQuadrantRotation());
	at.shear(getShearX(), getShearY());
	at.translate(-centerX, -centerY);
	return at;
    }

}