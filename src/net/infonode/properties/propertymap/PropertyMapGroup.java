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


// $Id: PropertyMapGroup.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.properties.propertymap;

import net.infonode.properties.base.PropertyGroup;

/**
 * A property group containing properties for which values can be set in a property map.
 * The property map group has a property map containing default values for the properties in this group.
 * If no default value is set, the default value of the property will be used.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class PropertyMapGroup extends PropertyGroup {
  private PropertyMapImpl defaultMap;

  /**
   * Constructor.
   *
   * @param name        the name of this group
   * @param description the description for this group
   */
  public PropertyMapGroup(String name, String description) {
    super(name, description);
  }

  /**
   * Creates a group with a super group.
   *
   * @param superGroup  the super group from which to inherit properties
   * @param name        the name of this group
   * @param description the description for this group
   */
  public PropertyMapGroup(PropertyMapGroup superGroup, String name, String description) {
    super(superGroup, name, description);
  }

  /**
   * Returns the property map containing the default values for properties in this group.
   *
   * @return the property map containing the default values for properties in this group
   */
  public PropertyMap getDefaultMap() {
    if (defaultMap == null)
      defaultMap = new PropertyMapImpl(this);

    return defaultMap;
  }
}
