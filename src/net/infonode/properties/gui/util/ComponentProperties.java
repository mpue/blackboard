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


// $Id: ComponentProperties.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.properties.gui.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import net.infonode.gui.InsetsUtil;
import net.infonode.gui.panel.BaseContainer;
import net.infonode.gui.panel.BaseContainerUtil;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BorderProperty;
import net.infonode.properties.types.ColorProperty;
import net.infonode.properties.types.FontProperty;
import net.infonode.properties.types.InsetsProperty;
import net.infonode.util.Direction;

/**
 * Properties and property values for a {@link JComponent}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class ComponentProperties extends PropertyMapContainer {
  /**
   * Property group for all component properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Component Properties", "");

  /**
   * Component border.
   */
  public static final BorderProperty BORDER = new BorderProperty(PROPERTIES, "Border", "Component border.",
                                                                 PropertyMapValueHandler.INSTANCE);

  /**
   * Component insets inside the border.
   */
  public static final InsetsProperty INSETS = new InsetsProperty(PROPERTIES, "Insets",
                                                                 "Component insets inside the border.",
                                                                 PropertyMapValueHandler.INSTANCE);

  /**
   * Component foreground color.
   */
  public static final ColorProperty FOREGROUND_COLOR = new ColorProperty(PROPERTIES, "Foreground Color",
                                                                         "Component foreground color.",
                                                                         PropertyMapValueHandler.INSTANCE);

  /**
   * Component text font.
   */
  public static final FontProperty FONT = new FontProperty(PROPERTIES, "Font", "Component text font.",
                                                           PropertyMapValueHandler.INSTANCE);

  /**
   * Component background color. A null value means that no background will be painted.
   */
  public static final ColorProperty BACKGROUND_COLOR = new ColorProperty(PROPERTIES, "Background Color",
                                                                         "Component background color. A null value means that no background will be painted.",
                                                                         PropertyMapValueHandler.INSTANCE);

  static {
    ComponentProperties properties = new ComponentProperties(PROPERTIES.getDefaultMap());

    properties.setBackgroundColor(null).setBorder(null).setInsets(null);
  }

  /**
   * Creates an empty property object.
   */
  public ComponentProperties() {
    super(PROPERTIES);
  }

  /**
   * Creates a property map containing the map.
   *
   * @param map the property map
   */
  public ComponentProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public ComponentProperties(ComponentProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public ComponentProperties addSuperObject(ComponentProperties properties) {
    getMap().addSuperMap(properties.getMap());

    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public ComponentProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes the given super object.
   *
   * @param superObject super object to remove
   * @return this
   */
  public ComponentProperties removeSuperObject(ComponentProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Sets the component border.
   *
   * @param border the component border
   * @return this
   */
  public ComponentProperties setBorder(Border border) {
    BORDER.set(getMap(), border);

    return this;
  }

  /**
   * Sets the component insets inside the border.
   *
   * @param insets the component insets
   * @return this
   */
  public ComponentProperties setInsets(Insets insets) {
    INSETS.set(getMap(), insets);

    return this;
  }

  /**
   * Sets the component background color.
   *
   * @param color the background color, null means no background
   * @return this
   */
  public ComponentProperties setBackgroundColor(Color color) {
    BACKGROUND_COLOR.set(getMap(), color);

    return this;
  }

  /**
   * Returns the component insets inside the border.
   *
   * @return the component insets inside the border
   */
  public Insets getInsets() {
    return INSETS.get(getMap());
  }

  /**
   * Returns the component border.
   *
   * @return the component border
   */
  public Border getBorder() {
    return BORDER.get(getMap());
  }

  /**
   * Returns the component background color.
   *
   * @return the component background color
   */
  public Color getBackgroundColor() {
    return BACKGROUND_COLOR.get(getMap());
  }

  /**
   * Returns the component text font.
   *
   * @return the component text font
   */
  public Font getFont() {
    return FONT.get(getMap());
  }

  /**
   * Returns the component foreground color.
   *
   * @return the component foreground color
   */
  public Color getForegroundColor() {
    return FOREGROUND_COLOR.get(getMap());
  }

  /**
   * Sets the component foreground color.
   *
   * @param foregroundColor the component foreground color
   * @return this
   */
  public ComponentProperties setForegroundColor(Color foregroundColor) {
    FOREGROUND_COLOR.set(getMap(), foregroundColor);
    return this;
  }

  /**
   * Sets the component text font.
   *
   * @param font the component text font
   * @return this
   */
  public ComponentProperties setFont(Font font) {
    FONT.set(getMap(), font);
    return this;
  }

  /**
   * Applies the property values to a component.
   *
   * @param component the component on which to apply the property values
   */
  public void applyTo(JComponent component) {
    applyTo(component, Direction.RIGHT);
  }

  /**
   * Applies the property values to a component and rotates the insets in the
   * given direction.
   *
   * @param component       the component on which to apply the property values
   * @param insetsDirection insets direction
   */
  public void applyTo(JComponent component, Direction insetsDirection) {
    Insets insets = getInsets() == null ? null : InsetsUtil.rotate(insetsDirection, getInsets());
    Border innerBorder = (insets == null) ? null : new EmptyBorder(insets);
    component.setBorder((getBorder() == null) ? innerBorder
                        : ((innerBorder == null) ? getBorder()
                           : new CompoundBorder(getBorder(), innerBorder)));
    //component.setOpaque(getBackgroundColor() != null);
    
    if (component instanceof BaseContainer) {
      BaseContainer c = (BaseContainer) component;
      BaseContainerUtil.setOverridedBackground(c, getBackgroundColor());
      BaseContainerUtil.setOverridedForeground(c, getForegroundColor());
      BaseContainerUtil.setOverridedFont(c, getFont());
    }
    else {
      component.setBackground(getBackgroundColor());
      component.setFont(getFont());
      component.setForeground(getForegroundColor());
    }
  }
}
