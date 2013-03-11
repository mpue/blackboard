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


// $Id: FloatingWindowProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;

/**
 * Properties and property values for floating windows.
 *
 * @author $Author: mpue $
 * @since IDW 1.4.0
 */
public class FloatingWindowProperties extends PropertyMapContainer {
  /**
   * Property group containing all floating window properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Floating Window Properties", "");

  /**
   * Properties for the component
   *
   * @see #getComponentProperties
   */
  public static final PropertyMapProperty COMPONENT_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                         "Component Properties",
                                                                                         "Component properties for floating window.",
                                                                                         ComponentProperties.PROPERTIES);

  /**
   * Properties for the shaped panel
   *
   * @see #getShapedPanelProperties
   */
  public static final PropertyMapProperty SHAPED_PANEL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                            "Shaped Panel Properties",
                                                                                            "Properties for floating window internal shape.",
                                                                                            ShapedPanelProperties.PROPERTIES);

  /**
   * Auto close enabled
   */
  public static final BooleanProperty AUTO_CLOSE_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Auto Close Enabled",
                          "Enables/disables if the floating window should be automatically closed when it doesn't contain any child window.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * If true the floating window will be created as a JFrame, otherwise a JDialog will be created.
   * Note that the value of this property only takes effect when the FloatingWindow is created, it doesn't affect
   * existing FloatingWindows (but this might change in future versions).
   *
   * @since IDW 1.5.0
   */
  public static final BooleanProperty USE_FRAME =
      new BooleanProperty(PROPERTIES,
                          "Use Frame",
                          "If true the floating window will be created as a JFrame, otherwise a JDialog will be created.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Creates an empty property object.
   */
  public FloatingWindowProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property map containing the map.
   *
   * @param map the property map
   */
  public FloatingWindowProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public FloatingWindowProperties(FloatingWindowProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public FloatingWindowProperties addSuperObject(FloatingWindowProperties properties) {
    getMap().addSuperMap(properties.getMap());
    return this;
  }

  /**
   * Removes a super object.
   *
   * @param superObject the super object to remove
   * @return this
   */
  public FloatingWindowProperties removeSuperObject(FloatingWindowProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Gets the component properties
   *
   * @return component properties
   */
  public ComponentProperties getComponentProperties() {
    return new ComponentProperties(COMPONENT_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the shaped panel properties
   *
   * @return shaped panel properties
   */
  public ShapedPanelProperties getShapedPanelProperties() {
    return new ShapedPanelProperties(SHAPED_PANEL_PROPERTIES.get(getMap()));
  }

  /**
   * Returns true if the floating window should be automatically closed when it doesn't
   * contain any child window.
   *
   * @return true if auto close is enabled, otherwise false
   */
  public boolean getAutoCloseEnabled() {
    return AUTO_CLOSE_ENABLED.get(getMap());
  }

  /**
   * Enables/disables if the floating window should be automatically closed when it doesn't contain
   * any child window.
   *
   * @param enabled true for auto close, otherwise disabled
   * @return this
   */
  public FloatingWindowProperties setAutoCloseEnabled(boolean enabled) {
    AUTO_CLOSE_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns true if the floating window should be created as a JFrame, otherwise a JDialog is used.
   *
   * @return true if a JFrame should be used
   * @since IDW 1.5.0
   */
  public boolean getUseFrame() {
    return USE_FRAME.get(getMap());
  }

  /**
   * Set to true if the floating window should be created as a JFrame, otherwise a JDialog is used.
   * Note that the value of this property only takes effect when the FloatingWindow is created, it doesn't affect
   * existing FloatingWindows (but this might change in future versions).
   *
   * @param enabled true if a JFrame should be used
   * @return this
   * @since IDW 1.5.0
   */
  public FloatingWindowProperties setUseFrame(boolean enabled) {
    USE_FRAME.set(getMap(), enabled);
    return this;
  }
}
