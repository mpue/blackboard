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


// $Id: WindowBarProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import java.awt.Color;

import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.ColorProperty;
import net.infonode.properties.types.IntegerProperty;
import net.infonode.util.Direction;

/**
 * Properties and property values for window bars.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class WindowBarProperties extends PropertyMapContainer {
  /**
   * Property group containing all window bar properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Window Bar Properties", "");

  /**
   * The window bar component property values.
   */
  public static final PropertyMapProperty COMPONENT_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                         "Component Properties",
                                                                                         "The WindowBar component properties.",
                                                                                         ComponentProperties.PROPERTIES);

  /**
   * Inside this distance from the content panel edge the user can resize the content panel.
   */
  public static final IntegerProperty CONTENT_PANEL_EDGE_RESIZE_DISTANCE =
      IntegerProperty.createPositive(PROPERTIES,
                                     "Content Panel Edge Resize Distance",
                                     "Inside this distance from the content panel edge the user can resize the content panel.",
                                     2,
                                     PropertyMapValueHandler.INSTANCE);

  /**
   * The minimum width of the window bar. If greater than 0, the window bar will always be visible and the user can drag
   * windows to it.
   */
  public static final IntegerProperty MINIMUM_WIDTH =
      IntegerProperty.createPositive(PROPERTIES,
                                     "Minimum Width",
                                     "The minimum width of the window bar. If greater than 0, the window bar will " +
                                     "always be visible and the user can drag windows to it.",
                                     2,
                                     PropertyMapValueHandler.INSTANCE);

  /**
   * When enabled causes the windows to change size continuously while dragging the split window divider.
   *
   * @since IDW 1.4.0
   */
  public static final BooleanProperty CONTINUOUS_LAYOUT_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Continuous Layout Enabled",
                          "When enabled causes the selected tab's content to change size continuously while resizing it.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * The drag indicator color.
   *
   * @since IDW 1.4.0
   */
  public static final ColorProperty DRAG_INDICATOR_COLOR =
      new ColorProperty(PROPERTIES,
                        "Drag Indicator Color",
                        "The color for the resizer's drag indicator that is shown when continuous layout is disabled.",
                        PropertyMapValueHandler.INSTANCE);

  /**
   * Properties for the tab window used by this window bar.
   */
  public static final PropertyMapProperty TAB_WINDOW_PROPERTIES =
      new PropertyMapProperty(PROPERTIES, "Tab Window Properties", "", TabWindowProperties.PROPERTIES);

  private static WindowBarProperties[] DEFAULT_VALUES = new WindowBarProperties[4];

  static {
    final Direction[] directions = Direction.getDirections();

    for (int i = 0; i < directions.length; i++) {
      Direction dir = directions[i];
      WindowBarProperties properties = new WindowBarProperties();
      properties.getTabWindowProperties().getTabbedPanelProperties().setTabAreaOrientation(dir);
      properties.getTabWindowProperties().getTabProperties().getTitledTabProperties().
          getNormalProperties().setDirection(dir.isHorizontal() ? Direction.DOWN : Direction.RIGHT);
/*      properties.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties().
          getComponentProperties().setInsets(new Insets(dir == Direction.DOWN ? 40 : 0,
                                                        dir == Direction.RIGHT ? 40 : 0,
                                                        dir == Direction.UP ? 40 : 0,
                                                        dir == Direction. LEFT ? 40 : 0));*/
      DEFAULT_VALUES[dir.getValue()] = properties;
    }
  }

  /**
   * Creates a property object which inherits the default property values.
   *
   * @param location the location of the window bar
   * @return a property object which inherits the default property values
   */
  public static WindowBarProperties createDefault(Direction location) {
    return new WindowBarProperties(DEFAULT_VALUES[location.getValue()]);
  }

  /**
   * Creates an empty property object.
   */
  public WindowBarProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public WindowBarProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public WindowBarProperties(WindowBarProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public WindowBarProperties addSuperObject(WindowBarProperties properties) {
    getMap().addSuperMap(properties.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   * @since IDW 1.1.0
   * @deprecated Use {@link #removeSuperObject(WindowBarProperties)} instead.
   */
  public WindowBarProperties removeSuperObject() {
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
  public WindowBarProperties removeSuperObject(WindowBarProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Returns the distance from the content panel edge which inside the user can resize the content panel.
   *
   * @return the distance from the content panel edge which inside the user can resize the content panel
   */
  public int getContentPanelEdgeResizeDistance() {
    return CONTENT_PANEL_EDGE_RESIZE_DISTANCE.get(getMap());
  }

  /**
   * Sets the distance from the content panel edge which inside the user can resize the content panel.
   *
   * @param width the distance from the content panel edge which inside the user can resize the content panel
   * @return this
   */
  public WindowBarProperties setContentPanelEdgeResizeEdgeDistance(int width) {
    CONTENT_PANEL_EDGE_RESIZE_DISTANCE.set(getMap(), width);
    return this;
  }

  /**
   * Returns the minimum width of the window bar.
   *
   * @return the minimum width of the window bar
   */
  public int getMinimumWidth() {
    return MINIMUM_WIDTH.get(getMap());
  }

  /**
   * Sets the minimum width of the window bar.
   *
   * @param width the minimum width of the window bar
   * @return this
   */
  public WindowBarProperties setMinimumWidth(int width) {
    MINIMUM_WIDTH.set(getMap(), width);
    return this;
  }

  /**
   * Returns the tab window property values.
   *
   * @return the tab window property values
   */
  public TabWindowProperties getTabWindowProperties() {
    return new TabWindowProperties(TAB_WINDOW_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for the window bar component.
   *
   * @return the property values for the window bar component
   */
  public ComponentProperties getComponentProperties() {
    return new ComponentProperties(COMPONENT_PROPERTIES.get(getMap()));
  }

  /**
   * Sets the resizer's drag indicator color.
   *
   * @param color the color for the drag indicator
   * @return this
   * @since IDW 1.4.0
   */
  public WindowBarProperties setDragIndicatorColor(Color color) {
    DRAG_INDICATOR_COLOR.set(getMap(), color);
    return this;
  }

  /**
   * Returns the resizer's drag indicator color.
   *
   * @return the drag indicator color
   * @since IDW 1.4.0
   */
  public Color getDragIndicatorColor() {
    return DRAG_INDICATOR_COLOR.get(getMap());
  }

  /**
   * Returns true if continuous layout is enabled.
   *
   * @return true if continuous layout is enabled
   * @since IDW 1.4.0
   */
  public boolean getContinuousLayoutEnabled() {
    return CONTINUOUS_LAYOUT_ENABLED.get(getMap());
  }

  /**
   * Enables/disables continuous layout.
   *
   * @param enabled if true continuous layout is enabled
   * @return this
   * @since IDW 1.4.0
   */
  public WindowBarProperties setContinuousLayoutEnabled(boolean enabled) {
    CONTINUOUS_LAYOUT_ENABLED.set(getMap(), enabled);
    return this;
  }
}
