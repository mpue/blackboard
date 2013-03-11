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


// $Id: EnumProperty.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.properties.types;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;
import net.infonode.util.ArrayUtil;

/**
 * A property which value is one in a fixed set of values.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class EnumProperty extends ValueHandlerProperty {
  private Object[] validValues;

  /**
   * Constructor.
   *
   * @param group        the property group
   * @param name         the property name
   * @param type         the property type
   * @param description  the property description
   * @param valueHandler handles values for this property
   * @param validValues  valid values for this property
   */
  public EnumProperty(PropertyGroup group,
                      String name,
                      Class type,
                      String description,
                      PropertyValueHandler valueHandler,
                      Object[] validValues) {
    super(group, name, type, description, valueHandler);
    this.validValues = (Object[]) validValues.clone();
  }

  public void setValue(Object object, Object value) {
    if (!ArrayUtil.contains(validValues, value))
      throw new IllegalArgumentException("Invalid enum value!");

    super.setValue(object, value);
  }

  /**
   * Returns the valid values for this property.
   *
   * @return the valid values for this property
   */
  public Object[] getValidValues() {
    return (Object[]) validValues.clone();
  }

  public Object getValue(Object object) {
    Object value = super.getValue(object);
    return value == null ? validValues[0] : value;
  }
}
