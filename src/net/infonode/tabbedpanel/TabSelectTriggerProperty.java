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


// $Id: TabSelectTriggerProperty.java,v 1.2 2011-08-26 15:10:41 mpue Exp $

package net.infonode.tabbedpanel;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.types.EnumProperty;
import net.infonode.properties.util.PropertyValueHandler;

/**
 * Property for TabSelectTrigger
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @since ITP 1.1.0
 */
public class TabSelectTriggerProperty extends EnumProperty {
  /**
   * Constructs a TabSelectTriggerProperty object
   *
   * @param group        property group
   * @param name         property name
   * @param description  property description
   * @param valueStorage storage for property
   */
  public TabSelectTriggerProperty(PropertyGroup group,
                                  String name,
                                  String description,
                                  PropertyValueHandler valueStorage) {
    super(group, name, TabSelectTrigger.class, description, valueStorage, TabSelectTrigger.getSelectTriggers());
  }

  /**
   * Gets the TabSelectTrigger
   *
   * @param object storage object for property
   * @return the TabSelectTrigger
   */
  public TabSelectTrigger get(Object object) {
    return (TabSelectTrigger) getValue(object);
  }

  /**
   * Sets the TabSelectTrigger
   *
   * @param object  storage object for property
   * @param trigger the TabSelectTrigger
   */
  public void set(Object object, TabSelectTrigger trigger) {
    setValue(object, trigger);
  }
}