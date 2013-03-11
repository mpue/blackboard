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


// $Id: AbstractProperty.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.properties.util;

import net.infonode.properties.base.Property;
import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.base.exception.InvalidPropertyValueException;

/**
 * An abstract base class for properties.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
abstract public class AbstractProperty implements Property {
  private PropertyGroup group;
  private String name;
  private Class type;
  private String description;

  /**
   * Constructor.
   *
   * @param group       the property group
   * @param name        the property name
   * @param type        the property type
   * @param description the property description
   */
  protected AbstractProperty(PropertyGroup group, String name, Class type, String description) {
    this.group = group;
    this.name = name;
    this.type = type;
    this.description = description;

    if (group != null)
      group.addProperty(this);
  }

  public PropertyGroup getGroup() {
    return group;
  }

  public String getName() {
    return name;
  }

  public Class getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public boolean isMutable() {
    return true;
  }

  public void setValue(Object object, Object value) {
    if (!canBeAssiged(value))
      throw new InvalidPropertyValueException(this, value);
  }

  public String toString() {
    return getName();
  }

  public boolean canBeAssiged(Object value) {
    return isMutable() && (value == null || getType().isAssignableFrom(value.getClass()));
  }

}
