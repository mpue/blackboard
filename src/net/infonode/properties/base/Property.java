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


// $Id: Property.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.properties.base;

import net.infonode.properties.base.exception.CantRemoveValueException;
import net.infonode.properties.base.exception.ImmutablePropertyException;
import net.infonode.properties.base.exception.InvalidPropertyException;
import net.infonode.properties.base.exception.InvalidPropertyValueException;

/**
 * A property is belongs to a {@link PropertyGroup} and contains name, description, type etc.
 * A property can have multiple values which can be stored in any type of object.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public interface Property {
  /**
   * Returns the property name.
   *
   * @return the property name
   */
  String getName();

  /**
   * Returns a description of this property.
   *
   * @return a description of this property
   */
  String getDescription();

  /**
   * Returns the value type of this property.
   * The property can only be set to values that are of this class or a sub class of this class.
   *
   * @return the value type of this property
   */
  Class getType();

  /**
   * Returns the property group that this property belongs to.
   *
   * @return the property group that this property belongs to
   */
  PropertyGroup getGroup();

  /**
   * Returns the value of this property in a value container.
   *
   * @param valueContainer the object containing the value
   * @return the value of this property in an valueContainer, null if the container doesn't contain the value
   * @throws InvalidPropertyException if the property can not be read from the value container
   */
  Object getValue(Object valueContainer) throws InvalidPropertyException;

  /**
   * Sets the value of this property in an object.
   *
   * @param valueContainer the object to set the property value in
   * @param value          the value of the property
   * @throws ImmutablePropertyException    if this property is immutable
   * @throws InvalidPropertyException      if this property can't be set in the object
   * @throws InvalidPropertyValueException if the property value is invalid
   */
  void setValue(Object valueContainer, Object value) throws ImmutablePropertyException, InvalidPropertyException,
      InvalidPropertyValueException;

  /**
   * Returns true if the value can be assigned to this property.
   *
   * @param value the value to assign
   * @return true if the value can be assigned to this property
   */
  boolean canBeAssiged(Object value);

  /**
   * Returns true if this property is mutable.
   *
   * @return true if this property is mutable
   */
  boolean isMutable();

  /**
   * Returns true if the value of this property can be removed from the valueContainer.
   *
   * @param valueContainer the object from which to remove the value
   * @return true if the value of this property can be removed from the valueContainer
   */
  boolean valueIsRemovable(Object valueContainer);

  /**
   * Returns true if this property has a value in the valueContainer.
   *
   * @param valueContainer the object that might contain the value
   * @return true if this property has a value in the valueContainer
   */
  boolean valueIsSet(Object valueContainer);

  /**
   * Removes the value of this property from an valueContainer.
   *
   * @param valueContainer the object in which to remove the value
   * @throws ImmutablePropertyException if the property is immutable
   * @throws CantRemoveValueException   if the property value can't be removed from the valueContainer
   */
  void removeValue(Object valueContainer) throws ImmutablePropertyException, CantRemoveValueException;
}
