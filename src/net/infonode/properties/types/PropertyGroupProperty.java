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


// $Id: PropertyGroupProperty.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.properties.types;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;

/**
 * A property that can be assigned a value container as value.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class PropertyGroupProperty extends ValueHandlerProperty {
  private PropertyGroup propertyGroup;

  /**
   * Constructor.
   *
   * @param group         the property group
   * @param name          the property name
   * @param type          the property type
   * @param description   the property description
   * @param valueHandler  handles values for this property
   * @param propertyGroup the property group. Values for properties in this group can be stored in the value containers
   *                      that are this properties values.
   */
  public PropertyGroupProperty(PropertyGroup group, String name, Class type, String description, PropertyValueHandler valueHandler,
                               PropertyGroup propertyGroup) {
    super(group, name, type, description, valueHandler);
    this.propertyGroup = propertyGroup;
  }

  /**
   * Returns the property group.
   * Values for properties in this group can be stored in the value containers that are this properties values.
   *
   * @return the property group
   */
  public PropertyGroup getPropertyGroup() {
    return propertyGroup;
  }
}
