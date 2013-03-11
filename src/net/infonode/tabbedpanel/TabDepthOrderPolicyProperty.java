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


// $Id: TabDepthOrderPolicyProperty.java,v 1.2 2011-08-26 15:10:41 mpue Exp $

package net.infonode.tabbedpanel;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.types.EnumProperty;
import net.infonode.properties.util.PropertyValueHandler;

/**
 * Property for TabDepthOrderPolicy
 *
 * @author johan
 * @version $Revision: 1.2 $
 * @see TabDepthOrderPolicy
 * @since ITP 1.2.0
 */
public class TabDepthOrderPolicyProperty extends EnumProperty {
  /**
   * Constructs a TabDepothOrderPolicyProperty object
   *
   * @param group        property group
   * @param name         property name
   * @param description  property description
   * @param valueStorage storage for property
   */
  public TabDepthOrderPolicyProperty(PropertyGroup group,
                                     String name,
                                     String description,
                                     PropertyValueHandler valueStorage) {
    super(group,
          name,
          TabDepthOrderPolicy.class,
          description,
          valueStorage,
          TabDepthOrderPolicy.getDepthOrderPolicies());
  }

  /**
   * Gets the TabDepthOrderPolicy
   *
   * @param object storage object for property
   * @return the TabDepthOrderPolicy
   */
  public TabDepthOrderPolicy get(Object object) {
    return (TabDepthOrderPolicy) getValue(object);
  }

  /**
   * Sets the TabDepthOrderPolicy
   *
   * @param object storage object for property
   * @param policy the TabDepthOrderPolicy
   */
  public void set(Object object, TabDepthOrderPolicy policy) {
    setValue(object, policy);
  }
}