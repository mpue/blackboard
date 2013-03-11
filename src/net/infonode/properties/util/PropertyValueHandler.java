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


// $Id: PropertyValueHandler.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.properties.util;

import net.infonode.properties.base.Property;

/**
 * Sets and gets property values to and from value objects.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public interface PropertyValueHandler {
  /**
   * Gets the value of a property from a value container.
   *
   * @param property       the property
   * @param valueContainer the object containing the value
   * @return the property value, null if the container doesn't contain the value
   */
  Object getValue(Property property, Object valueContainer);

  /**
   * Sets the value of a property in a value container.
   *
   * @param property       the property
   * @param valueContainer the object that will contain the value
   * @param value          the property value
   */
  void setValue(Property property, Object valueContainer, Object value);

  /**
   * Removes a property value from a value container.
   *
   * @param property       the property
   * @param valueContainer the value container
   */
  void removeValue(Property property, Object valueContainer);

  /**
   * Returns true if a value for the property is set in the value container.
   *
   * @param property       the property
   * @param valueContainer the value container
   * @return true if a value for the property is set in the value container
   */
  boolean getValueIsSet(Property property, Object valueContainer);

  /**
   * Returns true if the property value is removable from the value container.
   *
   * @param property       the property
   * @param valueContainer the value container
   * @return true if the property value is removable from the value container
   */
  boolean getValueIsRemovable(Property property, Object valueContainer);
}
