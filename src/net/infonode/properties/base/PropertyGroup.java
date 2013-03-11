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


// $Id: PropertyGroup.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.properties.base;

import java.util.ArrayList;

/**
 * A group of properties. The group have a name and a description. It can also have a super group from which it inherit
 * all it's properties. You can think of a property group as similar to a Java class, and properties similar to class
 * fields.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class PropertyGroup {
  private PropertyGroup superGroup;
  private String name;
  private String description;
  private ArrayList properties = new ArrayList(10);

  /**
   * Creates a property group.
   *
   * @param name        the name of the group
   * @param description the group description
   */
  public PropertyGroup(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Creates a property group with a super group.
   * All properties in the super group will be inherited to this group.
   *
   * @param superGroup  the super group of this group
   * @param name        the name of the group
   * @param description the group description
   */
  public PropertyGroup(PropertyGroup superGroup, String name, String description) {
    this(name, description);
    this.superGroup = superGroup;
  }

  /**
   * Returns the super group of this group.
   *
   * @return the super group of this group, null if it has no super group
   */
  public PropertyGroup getSuperGroup() {
    return superGroup;
  }

  /**
   * Returns the description for this group.
   *
   * @return the description for this group
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the name of this group.
   *
   * @return the name of this group
   */
  public String getName() {
    return name;
  }

  /**
   * Add a property to this group.
   *
   * @param property the property to add
   */
  public void addProperty(Property property) {
    properties.add(property);
  }

  /**
   * Returns the number of properties in this group.
   * This does not include properties in super groups.
   *
   * @return the number of properties in this group
   */
  public int getPropertyCount() {
    return properties.size();
  }

  /**
   * Returns true if this group or one of it's super groups contains the property.
   *
   * @param property the property
   * @return true if this group or one of it's super groups contains the property
   */
  public boolean hasProperty(Property property) {
    return isA(property.getGroup());
  }

  /**
   * Returns the property at the index,
   * This does not include properties in super groups.
   *
   * @param index the property index
   * @return the property at the index
   */
  public Property getProperty(int index) {
    return (Property) properties.get(index);
  }

  /**
   * Returns an array with the properties in this group.
   * This does not include properties in super groups.
   *
   * @return an array with the properties in this group
   */
  public Property[] getProperties() {
    return (Property[]) properties.toArray(new Property[properties.size()]);
  }

  public String toString() {
    return getName();
  }

  /**
   * Returns the property with the given name.
   * This includes properties in super groups.
   *
   * @param name the property name
   * @return the property with the given name, null if no property was found
   */
  public Property getProperty(String name) {
    for (int i = 0; i < getPropertyCount(); i++) {
      if (getProperty(i).getName().equals(name))
        return getProperty(i);
    }

    return superGroup == null ? null : superGroup.getProperty(name);
  }

  /**
   * Returns true if the group is this group or one of it's super groups.
   *
   * @param group the group
   * @return true if the group is this group or one of it's super groups
   */
  private boolean isA(PropertyGroup group) {
    return group == this || (getSuperGroup() != null && getSuperGroup().isA(group));
  }

}
