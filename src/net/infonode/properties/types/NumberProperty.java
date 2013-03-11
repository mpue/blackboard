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


// $Id: NumberProperty.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.properties.types;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;

/**
 * Base class for number properties.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class NumberProperty extends ValueHandlerProperty {
  private long minValue;
  private long maxValue;
  private int preferredDigitCount;

  /**
   * Constructor.
   *
   * @param group               the property group
   * @param name                the property name
   * @param cl                  the property type
   * @param description         the property description
   * @param minValue            the smallest value that this property can have
   * @param maxValue            the largest value that this property can have
   * @param preferredDigitCount the preferred number of digits to allocate space for in an editor for a property value
   * @param valueHandler        handles values for this property
   */
  public NumberProperty(PropertyGroup group, String name, Class cl, String description, long minValue, long maxValue,
                        int preferredDigitCount, PropertyValueHandler valueHandler) {
    super(group, name, cl, description, valueHandler);
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.preferredDigitCount = preferredDigitCount;
  }

  /**
   * Returns the preferred number of digits to allocate space for in an editor for a property value
   *
   * @return the preferred number of digits to allocate space for in an editor for a property value
   */
  public int getPreferredDigitCount() {
    return preferredDigitCount;
  }

  /**
   * Returns the smallest value that this property can have.
   *
   * @return the smallest value that this property can have
   */
  public long getMinValue() {
    return minValue;
  }

  /**
   * Returns the largest value that this property can have.
   *
   * @return the largest value that this property can have
   */
  public long getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the long value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the long value of this property
   */
  public long getLongValue(Object valueContainer) {
    Object value = getValue(valueContainer);
    return value == null ? Math.max(0, minValue) : ((Number) getValue(valueContainer)).longValue();
  }

  public boolean canBeAssiged(Object value) {
    if (!super.canBeAssiged(value))
      return false;

    long v = ((Number) value).longValue();
    return minValue <= v && maxValue >= v;
  }

}
