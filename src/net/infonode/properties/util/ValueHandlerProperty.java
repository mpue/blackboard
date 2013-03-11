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


// $Id: ValueHandlerProperty.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.properties.util;

import net.infonode.properties.base.PropertyGroup;

/**
 * Base class for properties that use a {@link PropertyValueHandler}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class ValueHandlerProperty extends AbstractProperty {
  private PropertyValueHandler valueHandler;

  /**
   * Constructor.
   *
   * @param group        the property group
   * @param name         the property name
   * @param type         the property type
   * @param description  the property description
   * @param valueHandler handles values for this property
   */
  public ValueHandlerProperty(PropertyGroup group,
                              String name,
                              Class type,
                              String description,
                              PropertyValueHandler valueHandler) {
    super(group, name, type, description);
    this.valueHandler = valueHandler;
  }

  public void setValue(Object object, Object value) {
    super.setValue(object, value);
    valueHandler.setValue(this, object, value);
  }

  public Object getValue(Object object) {
    return valueHandler.getValue(this, object);
  }

  public boolean valueIsRemovable(Object object) {
    return valueHandler.getValueIsRemovable(this, object);
  }

  public void removeValue(Object object) {
    valueHandler.removeValue(this, object);
  }

  public boolean valueIsSet(Object object) {
    return valueHandler.getValueIsSet(this, object);
  }
}
