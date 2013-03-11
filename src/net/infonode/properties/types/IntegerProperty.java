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


// $Id: IntegerProperty.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.properties.types;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;

/**
 * An integer property.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class IntegerProperty extends NumberProperty {
  /**
   * Constructor.
   * Creates an unbounded integer property.
   *
   * @param group        the property group
   * @param name         the property name
   * @param description  the property description
   * @param valueHandler handles values for this property
   */
  public IntegerProperty(PropertyGroup group, String name, String description, PropertyValueHandler valueHandler) {
    this(group, name, description, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, valueHandler);
  }

  /**
   * Constructor.
   *
   * @param group               the property group
   * @param name                the property name
   * @param description         the property description
   * @param min                 the smallest value that this property can have
   * @param max                 the largest value that this property can have
   * @param preferredDigitCount the preferred number of digits to allocate space for in an editor for a property value
   * @param valueHandler        handles values for this property
   */
  public IntegerProperty(PropertyGroup group, String name, String description, int min, int max, int preferredDigitCount,
                         PropertyValueHandler valueHandler) {
    super(group, name, Integer.class, description, min, max, preferredDigitCount, valueHandler);
  }

  /**
   * Creates an integer property that can only be set to zero and positive integers.
   *
   * @param group               the property group
   * @param name                the property name
   * @param description         the property description
   * @param preferredDigitCount the preferred number of digits to allocate space for in an editor for a property value
   * @param valueHandler        handles values for this property
   * @return an an integer property that can only be set to zero and positive integers
   */
  public static IntegerProperty createPositive(PropertyGroup group, String name, String description, int preferredDigitCount,
                                               PropertyValueHandler valueHandler) {
    return new IntegerProperty(group, name, description, 0, Integer.MAX_VALUE, preferredDigitCount, valueHandler);
  }

  /**
   * Returns the integer value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the integer value of this property
   */
  public int get(Object valueContainer) {
    return (int) getLongValue(valueContainer);
  }

  /**
   * Sets the integer value of this property in a value container.
   *
   * @param valueContainer the value container
   * @param value          the float value
   */
  public void set(Object valueContainer, int value) {
    setValue(valueContainer, new Integer(value));
  }

}
