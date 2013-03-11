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


// $Id: TabLayoutPolicyProperty.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.types.EnumProperty;
import net.infonode.properties.util.PropertyValueHandler;

/**
 * Property for TabLayoutPolicy
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabLayoutPolicy
 */
public class TabLayoutPolicyProperty extends EnumProperty {
  /**
   * Constructs a TabLayoutPolicyProperty object
   *
   * @param group        property group
   * @param name         property name
   * @param description  property description
   * @param valueStorage storage for property
   */
  public TabLayoutPolicyProperty(PropertyGroup group,
                                 String name,
                                 String description,
                                 PropertyValueHandler valueStorage) {
    super(group, name, TabLayoutPolicy.class, description, valueStorage, TabLayoutPolicy.getLayoutPolicies());
  }

  /**
   * Gets the TabLayoutPolicy
   *
   * @param object storage object for property
   * @return the TabLayoutPolicy
   */
  public TabLayoutPolicy get(Object object) {
    return (TabLayoutPolicy) getValue(object);
  }

  /**
   * Sets the TabLayoutPolicy
   *
   * @param object storage object for property
   * @param policy the TabLayoutPolicy
   */
  public void set(Object object, TabLayoutPolicy policy) {
    setValue(object, policy);
  }
}
