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


// $Id: TabAreaProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel;

import net.infonode.gui.hover.HoverListener;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.HoverListenerProperty;

/**
 * TabAreaProperties holds all visual properties for a tabbed panel's tab area.
 * TabbedPanelProperties contains TabAreaProperties.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 */
public class TabAreaProperties extends PropertyMapContainer {
  /**
   * A property group for all properties in TabAreaProperties
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Tab Area Properties",
                                                                         "Properties for the TabbedPanel class.");

  /**
   * Properties for the component
   *
   * @see #getComponentProperties
   */
  public static final PropertyMapProperty COMPONENT_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                         "Component Properties",
                                                                                         "Properties for tab area component.",
                                                                                         ComponentProperties.PROPERTIES);

  /**
   * Properties for the shaped panel
   *
   * @see #getShapedPanelProperties
   * @since ITP 1.2.0
   */
  public static final PropertyMapProperty SHAPED_PANEL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                            "Shaped Panel Properties",
                                                                                            "Properties for shaped tab area.",
                                                                                            ShapedPanelProperties.PROPERTIES);

  /**
   * Hover listener property
   *
   * @see #setHoverListener
   * @see #getHoverListener
   * @since ITP 1.3.0
   */
  public static final HoverListenerProperty HOVER_LISTENER = new HoverListenerProperty(PROPERTIES,
                                                                                       "Hover Listener",
                                                                                       "Hover Listener to be used for tracking mouse hovering over the tab area components area.",
                                                                                       PropertyMapValueHandler.INSTANCE);
  /**
   * Tab area visible property
   *
   * @see #setTabAreaVisiblePolicy(TabAreaVisiblePolicy)
   * @see #getTabAreaVisiblePolicy()
   * @since ITP 1.4.0
   */
  public static final TabAreaVisiblePolicyProperty TAB_AREA_VISIBLE_POLICY = new TabAreaVisiblePolicyProperty(
      PROPERTIES,
      "Visible Policy",
      "Visiblity for the tab area.",
      PropertyMapValueHandler.INSTANCE);

  /**
   * Constructs an empty TabAreaProperties object
   */
  public TabAreaProperties() {
    super(PROPERTIES);
  }

  /**
   * Constructs a TabAreaProperties object with the given object
   * as property storage
   *
   * @param object object to store properties in
   */
  public TabAreaProperties(PropertyMap object) {
    super(object);
  }

  /**
   * Constructs a TabAreaProperties object that inherits its properties
   * from the given TabAreaProperties object
   *
   * @param inheritFrom TabAreaProperties object to inherit properties from
   */
  public TabAreaProperties(TabAreaProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param superObject the object from which to inherit property values
   * @return this
   */
  public TabAreaProperties addSuperObject(TabAreaProperties superObject) {
    getMap().addSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public TabAreaProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes the given super object.
   *
   * @param superObject super object to remove
   * @return this
   * @since ITP 1.3.0
   */
  public TabAreaProperties removeSuperObject(TabAreaProperties superObject) {
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
   * @since ITP 1.2.0
   */
  public ShapedPanelProperties getShapedPanelProperties() {
    return new ShapedPanelProperties(SHAPED_PANEL_PROPERTIES.get(getMap()));
  }

  /**
   * <p>Sets the hover listener that will be triggered when the tab area is hoverd by the mouse.</p>
   *
   * <p>The tabbed panel that the hovered tab area is part of will be the source of the hover event sent to the
   * hover listener.</p>
   *
   * @param listener the hover listener
   * @return this TabAreaProperties
   * @since ITP 1.3.0
   */
  public TabAreaProperties setHoverListener(HoverListener listener) {
    HOVER_LISTENER.set(getMap(), listener);
    return this;
  }

  /**
   * <p>Sets the hover listener that will be triggered when the tab area is hovered by the mouse.</p>
   *
   * <p>The tabbed panel that the hovered tab area is part of will be the source of the hover event sent to the
   * hover listener.</p>
   *
   * @return the hover listener
   * @since ITP 1.3.0
   */
  public HoverListener getHoverListener() {
    return HOVER_LISTENER.get(getMap());
  }

  /**
   * Sets the tab area visible policy for the tab area, i.e. when the tab area is to be visible
   *
   * @param policy the tab area visible policy
   * @return this TabAreaProperties
   * @since ITP 1.4.0
   */
  public TabAreaProperties setTabAreaVisiblePolicy(TabAreaVisiblePolicy policy) {
    TAB_AREA_VISIBLE_POLICY.set(getMap(), policy);
    return this;
  }

  /**
   * Gets the tab area visible policy for the tab area, i.e. when the tab area is to be visible
   *
   * @return the tab area visible policy
   * @since ITP 1.4.0
   */
  public TabAreaVisiblePolicy getTabAreaVisiblePolicy() {
    return TAB_AREA_VISIBLE_POLICY.get(getMap());
  }
}
