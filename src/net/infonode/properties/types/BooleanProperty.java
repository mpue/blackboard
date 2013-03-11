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


// $Id: BooleanProperty.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.properties.types;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;

/**
 * A boolean property.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class BooleanProperty extends ValueHandlerProperty {
  /**
   * Constructor.
   *
   * @param group        the property group
   * @param name         the property name
   * @param description  the property description
   * @param valueHandler handles values for this property
   */
  public BooleanProperty(PropertyGroup group, String name, String description, PropertyValueHandler valueHandler) {
    super(group, name, Boolean.class, description, valueHandler);
  }

  /**
   * Returns the boolean value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the boolean value of this property
   */
  public boolean get(Object valueContainer) {
    Object value = getValue(valueContainer);
    return value == null ? false : ((Boolean) value).booleanValue();
  }

  /**
   * Sets the boolean value of this property in a value container.
   *
   * @param valueContainer the value container
   * @param value          the boolean value
   */
  public void set(Object valueContainer, boolean value) {
    setValue(valueContainer, Boolean.valueOf(value));
  }

}
