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


// $Id: WindowTabStateProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;

/**
 * Properties and property values for the window tab buttons.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class WindowTabStateProperties extends PropertyMapContainer {
  /**
   * Property group containing all window tab state properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("State Properties", "");

  /**
   * The minimize button property values.
   */
  public static final PropertyMapProperty MINIMIZE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                               "Minimize Button Properties",
                                                                                               "The minimize button property values.",
                                                                                               WindowTabButtonProperties.PROPERTIES);

  /**
   * The restore button property values.
   */
  public static final PropertyMapProperty RESTORE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                              "Restore Button Properties",
                                                                                              "The restore button property values.",
                                                                                              WindowTabButtonProperties.PROPERTIES);

  /**
   * The close button property values.
   */
  public static final PropertyMapProperty CLOSE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                            "Close Button Properties",
                                                                                            "The close button property values.",
                                                                                            WindowTabButtonProperties.PROPERTIES);

  /**
   * The undock button property values.
   *
   * @since IDW 1.4.0
   */
  public static final PropertyMapProperty UNDOCK_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                             "Undock Button Properties",
                                                                                             "The undock button property values.",
                                                                                             WindowTabButtonProperties.PROPERTIES);

  /**
   * The dock button property values.
   *
   * @since IDW 1.4.0
   */
  public static final PropertyMapProperty DOCK_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                           "Dock Button Properties",
                                                                                           "The dock button property values.",
                                                                                           WindowTabButtonProperties.PROPERTIES);

  /**
   * Creates an empty property object.
   */
  public WindowTabStateProperties() {
    super(PROPERTIES);
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public WindowTabStateProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public WindowTabStateProperties(WindowTabStateProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public WindowTabStateProperties addSuperObject(WindowTabStateProperties properties) {
    getMap().addSuperMap(properties.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   * @since IDW 1.1.0
   * @deprecated Use {@link #removeSuperObject(WindowTabStateProperties)} instead.
   */
  public WindowTabStateProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes a super object.
   *
   * @param superObject the super object to remove
   * @return this
   * @since IDW 1.3.0
   */
  public WindowTabStateProperties removeSuperObject(WindowTabStateProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Returns the minimize button property values.
   *
   * @return the minimize button property values
   */
  public WindowTabButtonProperties getMinimizeButtonProperties() {
    return new WindowTabButtonProperties(MINIMIZE_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the restore button property values.
   *
   * @return the restore button property values
   */
  public WindowTabButtonProperties getRestoreButtonProperties() {
    return new WindowTabButtonProperties(RESTORE_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the close button property values.
   *
   * @return the close button property values
   */
  public WindowTabButtonProperties getCloseButtonProperties() {
    return new WindowTabButtonProperties(CLOSE_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the undock button property values.
   *
   * @return the undock button property values
   * @since IDW 1.4.0
   */
  public WindowTabButtonProperties getUndockButtonProperties() {
    return new WindowTabButtonProperties(UNDOCK_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the dock button property values.
   *
   * @return the dock button property values
   * @since IDW 1.4.0
   */
  public WindowTabButtonProperties getDockButtonProperties() {
    return new WindowTabButtonProperties(DOCK_BUTTON_PROPERTIES.get(getMap()));
  }
}
