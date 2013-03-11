/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */


// $Id: FloatProperty.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.properties.types;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;

/**
 * A float property.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class FloatProperty extends ValueHandlerProperty {
  private float minValue;
  private float maxValue;
  private int preferredDigitCount;
  private float preferredDelta;

  /**
   * Constructor.
   * Creates an unbounded float property.
   *
   * @param group        the property group
   * @param name         the property name
   * @param description  the property description
   * @param valueHandler handles values for this property
   */
  public FloatProperty(PropertyGroup group, String name, String description, PropertyValueHandler valueHandler) {
    this(group, name, description, valueHandler, Float.MIN_VALUE, Float.MAX_VALUE);
  }

  /**
   * Constructor.
   *
   * @param group        the property group
   * @param name         the property name
   * @param description  the property description
   * @param valueHandler handles values for this property
   * @param minValue     the smallest value that this property can have
   * @param maxValue     the largest value that this property can have
   */
  public FloatProperty(PropertyGroup group,
                       String name,
                       String description,
                       PropertyValueHandler valueHandler,
                       float minValue,
                       float maxValue) {
    this(group, name, description, valueHandler, minValue, maxValue, 6, 0.1f);
  }

  /**
   * Constructor.
   *
   * @param group               the property group
   * @param name                the property name
   * @param description         the property description
   * @param valueHandler        handles values for this property
   * @param minValue            the smallest value that this property can have
   * @param maxValue            the largest value that this property can have
   * @param preferredDigitCount the preferred number of digits to allocate space for in an editor for a property value
   * @param preferredDelta      the preferred amount to increase and decrease a property value by
   */
  public FloatProperty(PropertyGroup group,
                       String name,
                       String description,
                       PropertyValueHandler valueHandler,
                       float minValue,
                       float maxValue,
                       int preferredDigitCount,
                       float preferredDelta) {
    super(group, name, Float.class, description, valueHandler);
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.preferredDigitCount = preferredDigitCount;
    this.preferredDelta = preferredDelta;
  }

  /**
   * Returns the preferred amount to increase and decrease a property value by.
   *
   * @return the preferred amount to increase and decrease a property value by
   */
  public float getPreferredDelta() {
    return preferredDelta;
  }

  /**
   * Returns the smallest value that this property can have.
   *
   * @return the smallest value that this property can have
   */
  public float getMinValue() {
    return minValue;
  }

  /**
   * Returns the largest value that this property can have.
   *
   * @return the largest value that this property can have
   */
  public float getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the preferred number of digits to allocate space for in an editor for a property value.
   *
   * @return the preferred number of digits to allocate space for in an editor for a property value
   */
  public int getPreferredDigitCount() {
    return preferredDigitCount;
  }

  /**
   * Returns the float value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the float value of this property
   */
  public float get(Object valueContainer) {
    Object value = getValue(valueContainer);
    return value == null ? 0 : ((Number) getValue(valueContainer)).floatValue();
  }

  /**
   * Sets the float value of this property in a value container.
   *
   * @param valueContainer the value container
   * @param value          the float value
   */
  public void set(Object valueContainer, float value) {
    setValue(valueContainer, new Float(value));
  }

  public boolean canBeAssiged(Object value) {
    if (!super.canBeAssiged(value))
      return false;

    float v = ((Number) value).floatValue();
    return v >= minValue && v <= maxValue;
  }
}
