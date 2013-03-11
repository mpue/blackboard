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


// $Id: ShapedPanelProperties.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.properties.gui.util;

import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.ComponentPainterProperty;
import net.infonode.properties.types.DirectionProperty;
import net.infonode.util.Direction;

/**
 * Properties and property values for a shaped panel, which is a panel that can have a
 * {@link net.infonode.gui.shaped.border.ShapedBorder} and a {@link ComponentPainter}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class ShapedPanelProperties extends PropertyMapContainer {
  /**
   * Property group for all shaped panel properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Shaped Panel Properties", "");


  /**
   * If true the shaped panel is opaque.
   *
   * @since ITP 1.4.0
   */
  public static final BooleanProperty OPAQUE =
      new BooleanProperty(PROPERTIES,
                          "Opaque",
                          "If true the shaped panel is opaque. If false the shaped panel is transparent",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * If true the shaped panel is flipped horizontally.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   */
  public static final BooleanProperty HORIZONTAL_FLIP =
      new BooleanProperty(PROPERTIES,
                          "Horizontal Flip",
                          "If true the shaped panel is flipped horizontally. " +
                          "Used by ComponentPainter's, ShapedBorder's etc.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * If true the shaped panel is flipped vertically.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   */
  public static final BooleanProperty VERTICAL_FLIP =
      new BooleanProperty(PROPERTIES,
                          "Vertical Flip",
                          "If true the shaped panel is flipped vertically. " +
                          "Used by ComponentPainter's, ShapedBorder's etc.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * If true the child components of the shaped panel are clipped with the border shape.
   */
  public static final BooleanProperty CLIP_CHILDREN =
      new BooleanProperty(PROPERTIES,
                          "Clip Children",
                          "If true the child components of the shaped panel are clipped with the border shape.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * {@link ComponentPainter} that paints the shaped panel background.
   */
  public static final ComponentPainterProperty COMPONENT_PAINTER =
      new ComponentPainterProperty(PROPERTIES,
                                   "Component Painter",
                                   "The component painter that paints the shaped panel background.",
                                   PropertyMapValueHandler.INSTANCE);

  /**
   * The direction of the shaped panel.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   */
  public static final DirectionProperty DIRECTION =
      new DirectionProperty(PROPERTIES,
                            "Direction",
                            "The direction of the shaped panel. Used by ComponentPainter's, ShapedBorder's etc.",
                            PropertyMapValueHandler.INSTANCE);

  static {
    ShapedPanelProperties properties = new ShapedPanelProperties(PROPERTIES.getDefaultMap());

    properties.setHorizontalFlip(false).setVerticalFlip(false).setComponentPainter(null).setDirection(Direction.RIGHT);
  }

  /**
   * Creates an empty property object.
   */
  public ShapedPanelProperties() {
    super(PROPERTIES);
  }

  /**
   * Creates a property map containing the map.
   *
   * @param map the property map
   */
  public ShapedPanelProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property
   * object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public ShapedPanelProperties(ShapedPanelProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public ShapedPanelProperties addSuperObject(ShapedPanelProperties properties) {
    getMap().addSuperMap(properties.getMap());

    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public ShapedPanelProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes the given super object.
   *
   * @param superObject super object to remove
   * @return this
   */
  public ShapedPanelProperties removeSuperObject(ShapedPanelProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Set to true if the shaped panel should be opaque.
   *
   * @param opaque true for opaque, otherwise false
   * @since ITP 1.4.0
   */
  public ShapedPanelProperties setOpaque(boolean opaque) {
    OPAQUE.set(getMap(), opaque);

    return this;
  }

  /**
   * Returns true if the shaped panel should be opaque.
   *
   * @return true for opaque, otherwise false
   * @since ITP 1.4.0
   */
  public boolean getOpaque() {
    return OPAQUE.get(getMap());
  }

  /**
   * Set to true if the shaped panel should be flipped horizontally.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   *
   * @param flip true if the shaped panel should be flipped vertically
   * @return this
   */
  public ShapedPanelProperties setHorizontalFlip(boolean flip) {
    HORIZONTAL_FLIP.set(getMap(), flip);

    return this;
  }

  /**
   * Returns true if the shaped panel is flipped horizontally.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   *
   * @return true if the shaped panel is flipped horizontally
   */
  public boolean getHorizontalFlip() {
    return HORIZONTAL_FLIP.get(getMap());
  }

  /**
   * Set to true if the shaped panel should be flipped vertically.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   *
   * @param flip true if the shaped panel should be flipped horizontally
   * @return this
   */
  public ShapedPanelProperties setVerticalFlip(boolean flip) {
    VERTICAL_FLIP.set(getMap(), flip);

    return this;
  }

  /**
   * Returns true if the shaped panel is flipped vertically.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   *
   * @return true if the shaped panel is flipped vertically
   */
  public boolean getVerticalFlip() {
    return VERTICAL_FLIP.get(getMap());
  }

  /**
   * Set to true if the child components of the shaped panel should be clipped with the border shape.
   *
   * @param clipChildren true if the child components of the shaped panel should be clipped with the border shape
   * @return this
   */
  public ShapedPanelProperties setClipChildren(boolean clipChildren) {
    CLIP_CHILDREN.set(getMap(), clipChildren);
    return this;
  }

  /**
   * Returns true the child components of the shaped panel are clipped with the border shape.
   *
   * @return true the child components of the shaped panel are clipped with the border shape
   */
  public boolean getClipChildren() {
    return CLIP_CHILDREN.get(getMap());
  }

  /**
   * Sets the painter that paints the shaped panel background.
   *
   * @param painter the painter that paints the shaped panel background, null for none
   * @return this
   */
  public ShapedPanelProperties setComponentPainter(ComponentPainter painter) {
    COMPONENT_PAINTER.set(getMap(), painter);

    return this;
  }

  /**
   * Gets the painter that paints the shaped panel background.
   *
   * @return the painter that paints the shaped panel background, null if none
   */
  public ComponentPainter getComponentPainter() {
    return COMPONENT_PAINTER.get(getMap());
  }

  /**
   * Sets the direction of the shaped panel.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   *
   * @param direction the direction of the shaped panel
   * @return this
   */
  public ShapedPanelProperties setDirection(Direction direction) {
    DIRECTION.set(getMap(), direction);

    return this;
  }

  /**
   * Gets the direction of the shaped panel.
   * Used by {@link ComponentPainter}'s, {@link net.infonode.gui.shaped.border.ShapedBorder}'s etc.
   *
   * @return the direction of the shaped panel
   */
  public Direction getDirection() {
    return DIRECTION.get(getMap());
  }
}