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


// $Id: DockingWindowProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import net.infonode.docking.title.DockingWindowTitleProvider;
import net.infonode.docking.title.DockingWindowTitleProviderProperty;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;

/**
 * Properties and property values common for all docking windows.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class DockingWindowProperties extends PropertyMapContainer {
  /**
   * Property group containing all docking window properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Docking Window Properties", "");

  /**
   * Property values for the window tab when the window is located in a TabWindow or a WindowBar.
   */
  public static final PropertyMapProperty TAB_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Tab Properties",
                              "Property values for the window tab when the window is located in a TabWindow or a WindowBar.",
                              WindowTabProperties.PROPERTIES);

  /**
   * Property values for drop filters.
   *
   * @since IDW 1.4.0
   */
  public static final PropertyMapProperty DROP_FILTER_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Drop Filter Properties",
                              "Property values for drop filters.",
                              DockingWindowDropFilterProperties.PROPERTIES);

  /**
   * Enables/disables window drag by the user.
   *
   * @since IDW 1.2.0
   */
  public static final BooleanProperty DRAG_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Drag Enabled",
                          "Enables/disables window drag by the user.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Enables/disables undock to a floating window.
   *
   * @since IDW 1.4.0
   */
  public static final BooleanProperty UNDOCK_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Undock Enabled",
                          "Enables/disables if a window can be undocked to a floating window.",
                          PropertyMapValueHandler.INSTANCE);
  /**
   * <p>
   * Enables/disables undock when dropped outside root window.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> This property will only have effect if window drag is enabled and undocking is enabled.
   * </p>
   *
   * @since IDW 1.4.0
   */
  public static final BooleanProperty UNDOCK_ON_DROP =
      new BooleanProperty(PROPERTIES,
                          "Undock when Dropped",
                          "Enables/disables window undock to floating window when a drag and drop is performed outside the root window.",
                          PropertyMapValueHandler.INSTANCE);


  /**
   * Enables/disables undock to a floating window.
   *
   * @since IDW 1.4.0
   */
  public static final BooleanProperty DOCK_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Dock Enabled",
                          "Enables/disables if a window can be docked to the root window from a floating window.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Enables/disables window minimize by the user.
   *
   * @since IDW 1.2.0
   */
  public static final BooleanProperty MINIMIZE_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Minimize Enabled",
                          "Enables/disables window minimize by the user.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Enables/disables window close by the user.
   *
   * @since IDW 1.2.0
   */
  public static final BooleanProperty CLOSE_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Close Enabled",
                          "Enables/disables window close by the user.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Enables/disables window restore by the user.
   *
   * @since IDW 1.2.0
   */
  public static final BooleanProperty RESTORE_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Restore Enabled",
                          "Enables/disables window restore by the user.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Enables/disables window maximize by the user.
   *
   * @since IDW 1.2.0
   */
  public static final BooleanProperty MAXIMIZE_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Maximize Enabled",
                          "Enables/disables window maximize by the user.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Provides a title for a window.
   *
   * @since IDW 1.3.0
   */
  public static final DockingWindowTitleProviderProperty TITLE_PROVIDER =
      new DockingWindowTitleProviderProperty(PROPERTIES,
                                             "Title Provider",
                                             "Provides a title for a window.",
                                             PropertyMapValueHandler.INSTANCE);


  /**
   * Creates an empty property object.
   */
  public DockingWindowProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property map containing the map.
   *
   * @param map the property map
   */
  public DockingWindowProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public DockingWindowProperties(DockingWindowProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public DockingWindowProperties addSuperObject(DockingWindowProperties properties) {
    getMap().addSuperMap(properties.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   * @since IDW 1.1.0
   * @deprecated Use {@link #removeSuperObject(DockingWindowProperties)} instead.
   */
  public DockingWindowProperties removeSuperObject() {
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
  public DockingWindowProperties removeSuperObject(DockingWindowProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Returns the property values for the window tab when the window is located in a TabWindow or a WindowBar.
   *
   * @return the property values for the window tab when the window is located in a TabWindow or a WindowBar
   */
  public WindowTabProperties getTabProperties() {
    return new WindowTabProperties(TAB_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for drop filters.
   *
   * @return the property values for drop filters
   * @since IDW 1.4.0
   */
  public DockingWindowDropFilterProperties getDropFilterProperties() {
    return new DockingWindowDropFilterProperties(DROP_FILTER_PROPERTIES.get(getMap()));
  }

  /**
   * Returns true if the window drag by the user is enabled.
   *
   * @return true if the window drag is enabled
   * @since IDW 1.2.0
   */
  public boolean getDragEnabled() {
    return DRAG_ENABLED.get(getMap());
  }

  /**
   * Enables/disables window drag by the user.
   *
   * @param enabled if true, drag is enabled, otherwise it's disabled
   * @return this
   * @since IDW 1.2.0
   */
  public DockingWindowProperties setDragEnabled(boolean enabled) {
    DRAG_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns true if the window can be undocked to a floating window.
   *
   * @return true if undocking is enabled
   * @since IDW 1.4.0
   */
  public boolean getUndockEnabled() {
    return UNDOCK_ENABLED.get(getMap());
  }

  /**
   * Enables/disables undock to floating window.
   *
   * @param enabled if true, a window can be undocked to a floating window,
   *                otherwise it's disabled
   * @return this
   * @since IDW 1.4.0
   */
  public DockingWindowProperties setUndockEnabled(boolean enabled) {
    UNDOCK_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * <p>
   * Returns true if the window drag by the user and is dropped outside the root window should undock to a floating
   * window.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> This property will only have effect if drag is enabled.
   * </p>
   *
   * @return true if the dropped window should undock to a floating window
   * @since IDW 1.4.0
   */
  public boolean getUndockOnDropEnabled() {
    return UNDOCK_ON_DROP.get(getMap());
  }

  /**
   * <p>
   * Enables/disables if the window drag by the user and is dropped outside the root window should undock to a floating
   * window or not.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> This property will only have effect if drag is enabled.
   * </p>
   *
   * @param enabled if true, drop to floating window is enabled, otherwise it's disabled
   * @return this
   * @since IDW 1.4.0
   */
  public DockingWindowProperties setUndockOnDropEnabled(boolean enabled) {
    UNDOCK_ON_DROP.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns true if the window can be docked to the root window from a floating window.
   *
   * @return true if docking is enabled
   * @since IDW 1.4.0
   */
  public boolean getDockEnabled() {
    return DOCK_ENABLED.get(getMap());
  }

  /**
   * Enables/disables dock to the root window from a floating window.
   *
   * @param enabled if true, a window can be docked to the root window from a floating window,
   *                otherwise it's disabled
   * @return this
   * @since IDW 1.4.0
   */
  public DockingWindowProperties setDockEnabled(boolean enabled) {
    DOCK_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns true if the window minimize by the user is enabled.
   *
   * @return true if the window minimize is enabled
   * @since IDW 1.2.0
   */
  public boolean getMinimizeEnabled() {
    return MINIMIZE_ENABLED.get(getMap());
  }

  /**
   * Enables/disables window minimize by the user.
   *
   * @param enabled if true, minimize is enabled, otherwise it's disabled
   * @return this
   * @since IDW 1.2.0
   */
  public DockingWindowProperties setMinimizeEnabled(boolean enabled) {
    MINIMIZE_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns true if the window maximize by the user is enabled.
   *
   * @return true if the window maximize is enabled
   * @since IDW 1.2.0
   */
  public boolean getMaximizeEnabled() {
    return MAXIMIZE_ENABLED.get(getMap());
  }

  /**
   * Enables/disables window maximize by the user.
   *
   * @param enabled if true, maximize is enabled, otherwise it's disabled
   * @return this
   * @since IDW 1.2.0
   */
  public DockingWindowProperties setMaximizeEnabled(boolean enabled) {
    MAXIMIZE_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns true if the window close by the user is enabled.
   *
   * @return true if the window close is enabled
   * @since IDW 1.2.0
   */
  public boolean getCloseEnabled() {
    return CLOSE_ENABLED.get(getMap());
  }

  /**
   * Enables/disables window close by the user.
   *
   * @param enabled if true, close is enabled, otherwise it's disabled
   * @return this
   * @since IDW 1.2.0
   */
  public DockingWindowProperties setCloseEnabled(boolean enabled) {
    CLOSE_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns true if the window restore by the user is enabled.
   *
   * @return true if the window restore is enabled
   * @since IDW 1.2.0
   */
  public boolean getRestoreEnabled() {
    return RESTORE_ENABLED.get(getMap());
  }

  /**
   * Enables/disables window restore by the user.
   *
   * @param enabled if true, restore is enabled, otherwise it's disabled
   * @return this
   * @since IDW 1.2.0
   */
  public DockingWindowProperties setRestoreEnabled(boolean enabled) {
    RESTORE_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns the title provider for the window.
   *
   * @return the title provider for the window
   * @since IDW 1.3.0
   */
  public DockingWindowTitleProvider getTitleProvider() {
    return TITLE_PROVIDER.get(getMap());
  }

  /**
   * Sets the title provider for the window.
   *
   * @param titleProvider the title provider for the window
   * @since IDW 1.3.0
   */
  public DockingWindowProperties setTitleProvider(DockingWindowTitleProvider titleProvider) {
    TITLE_PROVIDER.set(getMap(), titleProvider);
    return this;
  }

}
