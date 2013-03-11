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


// $Id: TabWindowProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.tabbedpanel.TabbedPanelProperties;

/**
 * Properties and property values for tab windows.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class TabWindowProperties extends PropertyMapContainer {
  /**
   * Property group containing all tab window properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Tab Window Properties", "");

  /**
   * Property values for the tabbed panel in the tab window.
   */
  public static final PropertyMapProperty TABBED_PANEL_PROPERTIES =
    new PropertyMapProperty(PROPERTIES,
        "Tabbed Panel Properties",
        "Property values for the tabbed panel in the TabWindow.",
        TabbedPanelProperties.PROPERTIES);

  /**
   * Default property values for the window tabs in the tab window.
   */
  public static final PropertyMapProperty TAB_PROPERTIES =
    new PropertyMapProperty(PROPERTIES,
        "Tab Properties",
        "Default property values for the window tabs in the TabWindow.",
        WindowTabProperties.PROPERTIES);

  /**
   * The minimize button property values.
   *
   * @since IDW 1.1.0
   */
  public static final PropertyMapProperty MINIMIZE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
      "Minimize Button Properties",
      "The minimize button property values.",
      WindowTabButtonProperties.PROPERTIES);

  /**
   * The restore button property values.
   *
   * @since IDW 1.1.0
   */
  public static final PropertyMapProperty RESTORE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
      "Restore Button Properties",
      "The restore button property values.",
      WindowTabButtonProperties.PROPERTIES);

  /**
   * The close button property values.
   *
   * @since IDW 1.1.0
   */
  public static final PropertyMapProperty CLOSE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
      "Close Button Properties",
      "The close button property values.",
      WindowTabButtonProperties.PROPERTIES);

  /**
   * The maximize button property values.
   *
   * @since IDW 1.1.0
   */
  public static final PropertyMapProperty MAXIMIZE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
      "Maximize Button Properties",
      "The maximize button property values.",
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
   * The respect child windows minimum sizes property.
   *
   * @since IDW 1.5.0
   */
  public static final BooleanProperty RESPECT_CHILD_WINDOW_MINIMUM_SIZE =
    new BooleanProperty(PROPERTIES,
        "Respect Child Window Minimum Size",
        "When enabled the Tab Window will respect its child windows minimum sizes.",
        PropertyMapValueHandler.INSTANCE);

  /**
   * Creates an empty property object.
   */
  public TabWindowProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public TabWindowProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public TabWindowProperties(TabWindowProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public TabWindowProperties addSuperObject(TabWindowProperties properties) {
    getMap().addSuperMap(properties.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   * @since IDW 1.1.0
   * @deprecated Use {@link #removeSuperObject(TabWindowProperties)} instead.
   */
  public TabWindowProperties removeSuperObject() {
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
  public TabWindowProperties removeSuperObject(TabWindowProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Returns the property values for the tabbed panel in the tab window.
   *
   * @return the property values for the tabbed panel in the tab window
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return new TabbedPanelProperties(TABBED_PANEL_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the default property values for the window tabs in the tab window.
   *
   * @return the default property values for the window tabs in the tab window
   */
  public WindowTabProperties getTabProperties() {
    return new WindowTabProperties(TAB_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the minimize button property values.
   *
   * @return the minimize button property values
   * @since IDW 1.1.0
   */
  public WindowTabButtonProperties getMinimizeButtonProperties() {
    return new WindowTabButtonProperties(MINIMIZE_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the restore button property values.
   *
   * @return the restore button property values
   * @since IDW 1.1.0
   */
  public WindowTabButtonProperties getRestoreButtonProperties() {
    return new WindowTabButtonProperties(RESTORE_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the close button property values.
   *
   * @return the close button property values
   * @since IDW 1.1.0
   */
  public WindowTabButtonProperties getCloseButtonProperties() {
    return new WindowTabButtonProperties(CLOSE_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the maximize button property values.
   *
   * @return the maximize button property values
   * @since IDW 1.1.0
   */
  public WindowTabButtonProperties getMaximizeButtonProperties() {
    return new WindowTabButtonProperties(MAXIMIZE_BUTTON_PROPERTIES.get(getMap()));
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

  /**
   * <p>
   * Returns true if the TabWindow will respect its child windows minimum sizes.
   * </p>
   *
   * <p>
   * When true the content area of the TabWindow cannot be resized smaller than
   * the maximum of all its child windows minimum sizes.
   * </p>
   *
   * @return true if the TabWindow respects its child windows minimum sizes
   * @since IDW 1.5.0
   */
  public boolean getRespectChildWindowMinimumSize() {
    return RESPECT_CHILD_WINDOW_MINIMUM_SIZE.get(getMap());
  }

  /**
   * <p>
   * Enables/disables the TabWindow will respect its child windows minimum sizes.
   * </p>
   *
   * <p>
   * When true the content area of the TabWindow cannot be resized smaller than
   * the maximum of all its child windows minimum sizes.
   * </p>
   *
   * @param repsectMinimuSize if true the TabWindow will respect its child windows
   *                          minimum sizes
   * @return this
   * @since IDW 1.5.0
   */
  public TabWindowProperties setRespectChildWindowMinimumSize(boolean repsectMinimuSize) {
    RESPECT_CHILD_WINDOW_MINIMUM_SIZE.set(getMap(), repsectMinimuSize);
    return this;
  }

}
