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


// $Id: TabDropDownListVisiblePolicyProperty.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.types.EnumProperty;
import net.infonode.properties.util.PropertyValueHandler;

/**
 * Property for TabDropDownListVisiblePolicy
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabDropDownListVisiblePolicy
 * @since ITP 1.1.0
 */
public class TabDropDownListVisiblePolicyProperty extends EnumProperty {
  /**
   * Constructs a TabDropDownListVisiblePolicyProperty object
   *
   * @param group        property group
   * @param name         property name
   * @param description  property description
   * @param valueStorage storage for property
   */
  public TabDropDownListVisiblePolicyProperty(PropertyGroup group,
                                              String name,
                                              String description,
                                              PropertyValueHandler valueStorage) {
    super(group,
          name,
          TabDropDownListVisiblePolicy.class,
          description,
          valueStorage,
          TabDropDownListVisiblePolicy.getDropDownListVisiblePolicies());
  }

  /**
   * Gets the TabDropDownListVisiblePolicy
   *
   * @param object storage object for property
   * @return the TabDropDownListVisiblePolicy
   */
  public TabDropDownListVisiblePolicy get(Object object) {
    return (TabDropDownListVisiblePolicy) getValue(object);
  }

  /**
   * Sets the TabDropDownListVisiblePolicy
   *
   * @param object storage object for property
   * @param policy the TabDropDownListVisiblePolicy
   */
  public void set(Object object, TabDropDownListVisiblePolicy policy) {
    setValue(object, policy);
  }
}