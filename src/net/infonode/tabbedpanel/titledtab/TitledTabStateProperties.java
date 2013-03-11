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


// $Id: TitledTabStateProperties.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.tabbedpanel.titledtab;

import javax.swing.Icon;

import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.AlignmentProperty;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.DirectionProperty;
import net.infonode.properties.types.IconProperty;
import net.infonode.properties.types.IntegerProperty;
import net.infonode.properties.types.StringProperty;
import net.infonode.util.Alignment;
import net.infonode.util.Direction;

/**
 * TitledTabStateProperties holds all properties that are unique for a titled tab state.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TitledTab
 * @see TitledTabProperties
 */
public class TitledTabStateProperties extends PropertyMapContainer {
  /**
   * A property group for all properties in TitledTabStateProperties
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("State Properties", "");

  /**
   * Icon property
   *
   * @see #setIcon
   * @see #getIcon
   */
  public static final IconProperty ICON = new IconProperty(PROPERTIES,
                                                           "Icon",
                                                           "Icon",
                                                           PropertyMapValueHandler.INSTANCE);

  /**
   * Text property
   *
   * @see #setText
   * @see #getText
   */
  public static final StringProperty TEXT = new StringProperty(PROPERTIES,
                                                               "Text",
                                                               "Text",
                                                               PropertyMapValueHandler.INSTANCE);

  /**
   * Icon text gap property
   *
   * @see #setIconTextGap
   * @see #getIconTextGap
   */
  public static final IntegerProperty ICON_TEXT_GAP = IntegerProperty.createPositive(PROPERTIES,
                                                                                     "Icon Text Gap",
                                                                                     "Number of pixels between icon and text.",
                                                                                     2,
                                                                                     PropertyMapValueHandler.INSTANCE);

  /**
   * Tool tip text property
   *
   * @see #setToolTipText
   * @see #getToolTipText
   */
  public static final StringProperty TOOL_TIP_TEXT = new StringProperty(PROPERTIES,
                                                                        "Tool Tip Text",
                                                                        "Tool tip text",
                                                                        PropertyMapValueHandler.INSTANCE);

  /**
   * Tool tip enabled property
   *
   * @see #setToolTipEnabled
   * @see #getToolTipEnabled
   */
  public static final BooleanProperty TOOL_TIP_ENABLED = new BooleanProperty(PROPERTIES,
                                                                             "Tool Tip Enabled",
                                                                             "Tool tip enabled or disabled",
                                                                             PropertyMapValueHandler.INSTANCE);


  /**
   * Icon visible property
   *
   * @see #setIconVisible
   * @see #getIconVisible
   * @since ITP 1.1.0
   */
  public static final BooleanProperty ICON_VISIBLE = new BooleanProperty(PROPERTIES,
                                                                         "Icon Visible",
                                                                         "Icon visible or not visible",
                                                                         PropertyMapValueHandler.INSTANCE);

  /**
   * Text visible property
   *
   * @see #setTextVisible
   * @see #getTextVisible
   * @since ITP 1.1.0
   */
  public static final BooleanProperty TEXT_VISIBLE = new BooleanProperty(PROPERTIES,
                                                                         "Text Visible",
                                                                         "Text visible or not visible",
                                                                         PropertyMapValueHandler.INSTANCE);

  /**
   * Title component visible property
   *
   * @see #setTitleComponentVisible
   * @see #getTitleComponentVisible
   * @since ITP 1.1.0
   */
  public static final BooleanProperty TITLE_COMPONENT_VISIBLE = new BooleanProperty(PROPERTIES,
                                                                                    "Title Component Visible",
                                                                                    "Title component visible or not visible",
                                                                                    PropertyMapValueHandler.INSTANCE);

  /**
   * Horizontal alignment property
   *
   * @see #setHorizontalAlignment
   * @see #getHorizontalAlignment
   */
  public static final AlignmentProperty HORIZONTAL_ALIGNMENT = new AlignmentProperty(PROPERTIES,
                                                                                     "Horizontal Alignment",
                                                                                     "Horizontal alignment for the icon and text.",
                                                                                     PropertyMapValueHandler.INSTANCE,
                                                                                     Alignment.getHorizontalAlignments());

  /**
   * Vertical alignment property
   *
   * @see #setVerticalAlignment
   * @see #getVerticalAlignment
   */
  public static final AlignmentProperty VERTICAL_ALIGNMENT = new AlignmentProperty(PROPERTIES,
                                                                                   "Vertical Alignment",
                                                                                   "Vertical alignment for the icon and text.",
                                                                                   PropertyMapValueHandler.INSTANCE,
                                                                                   Alignment.getVerticalAlignments());

  /**
   * Icon text relative alignment property
   *
   * @see #setIconTextRelativeAlignment
   * @see #getIconTextRelativeAlignment
   */
  public static final AlignmentProperty ICON_TEXT_RELATIVE_ALIGNMENT = new AlignmentProperty(PROPERTIES,
                                                                                             "Icon Text Relative Alignment",
                                                                                             "Icon horizontal alignment relative to text.",
                                                                                             PropertyMapValueHandler.INSTANCE,
                                                                                             new Alignment[]{
                                                                                               Alignment.LEFT,
                                                                                               Alignment.RIGHT});

  /**
   * Text title component gap property
   *
   * @see #setTextTitleComponentGap
   * @see #getTextTitleComponentGap
   */
  public static final IntegerProperty TEXT_TITLE_COMPONENT_GAP = IntegerProperty.createPositive(PROPERTIES,
                                                                                                "Text Title Component Gap",
                                                                                                "Number of pixels between text and title component.",
                                                                                                2,
                                                                                                PropertyMapValueHandler.INSTANCE);

  /**
   * Title component text relative alignment property
   *
   * @see #setTitleComponentTextRelativeAlignment
   * @see #getTitleComponentTextRelativeAlignment
   */
  public static final AlignmentProperty TITLE_COMPONENT_TEXT_RELATIVE_ALIGNMENT = new AlignmentProperty(PROPERTIES,
                                                                                                        "Title Component Text Relative Alignment",
                                                                                                        "Title component horizontal alignment relative to text and icon.",
                                                                                                        PropertyMapValueHandler.INSTANCE,
                                                                                                        new Alignment[]{
                                                                                                          Alignment.LEFT,
                                                                                                          Alignment.RIGHT});

  /**
   * Direction property
   *
   * @see #setDirection
   * @see #getDirection
   */
  public static final DirectionProperty DIRECTION = new DirectionProperty(PROPERTIES,
                                                                          "Direction",
                                                                          "Direction for tab contents",
                                                                          PropertyMapValueHandler.INSTANCE);

  /**
   * Tab component properties.
   */
  public static final PropertyMapProperty COMPONENT_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                         "Component Properties",
                                                                                         "Tab component properties.",
                                                                                         ComponentProperties.PROPERTIES);

  /**
   * Tab shaped panel properties.
   *
   * @since ITP 1.2.0
   */
  public static final PropertyMapProperty SHAPED_PANEL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                            "Shaped Panel Properties",
                                                                                            "Tab shaped panel properties.",
                                                                                            ShapedPanelProperties.PROPERTIES);

  /**
   * Constructs an empty TitledTabStateProperties object
   */
  public TitledTabStateProperties() {
    super(PROPERTIES);
  }

  /**
   * Constructs a TitledTabStateProperties map with the give map as property storage
   *
   * @param map map to store properties in
   */
  public TitledTabStateProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Constructs a TitledTabStateProperties object that inherits its properties from the given TitledTabStateProperties object
   *
   * @param inheritFrom TitledTabStateProperties object to inherit properties from
   */
  public TitledTabStateProperties(TitledTabStateProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param superObject the object from which to inherit property values
   * @return this
   */
  public TitledTabStateProperties addSuperObject(TitledTabStateProperties superObject) {
    getMap().addSuperMap(superObject.getMap());
    return this;
  }


  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public TitledTabStateProperties removeSuperObject() {
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
  public TitledTabStateProperties removeSuperObject(TitledTabStateProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Sets the icon
   *
   * @param icon icon or null for no icon
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setIcon(Icon icon) {
    ICON.set(getMap(), icon);
    return this;
  }

  /**
   * Gets the icon
   *
   * @return icon or null if no icon
   */
  public Icon getIcon() {
    return ICON.get(getMap());
  }

  /**
   * Sets the text
   *
   * @param text text or null for no text
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setText(String text) {
    TEXT.set(getMap(), text);
    return this;
  }

  /**
   * Gets the text
   *
   * @return text or null if no text
   */
  public String getText() {
    return TEXT.get(getMap());
  }

  /**
   * Sets the gap in pixels between the icon and the text
   *
   * @param gap number of pixels
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setIconTextGap(int gap) {
    ICON_TEXT_GAP.set(getMap(), gap);
    return this;
  }

  /**
   * Gets the gap in pixels between the icon and the text
   *
   * @return number of pixels
   */
  public int getIconTextGap() {
    return ICON_TEXT_GAP.get(getMap());
  }

  /**
   * Sets the tool tip text
   *
   * @param text tool tip text
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setToolTipText(String text) {
    TOOL_TIP_TEXT.set(getMap(), text);
    return this;
  }

  /**
   * Gets the tool tip text
   *
   * @return tool tip text
   */
  public String getToolTipText() {
    return TOOL_TIP_TEXT.get(getMap());
  }

  /**
   * Sets if tool tip text is enabled or disabled
   *
   * @param enabled true for enabled, otherwise false
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setToolTipEnabled(boolean enabled) {
    TOOL_TIP_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Gets if tool tip text is enabled or disabled
   *
   * @return true if enabled, otherwise false
   */
  public boolean getToolTipEnabled() {
    return TOOL_TIP_ENABLED.get(getMap());
  }

  /**
   * Sets if icon is visible or not visible
   *
   * @param visible true for visible, otherwise false
   * @return this TitledTabStateProperties
   * @since ITP 1.1.0
   */
  public TitledTabStateProperties setIconVisible(boolean visible) {
    ICON_VISIBLE.set(getMap(), visible);
    return this;
  }

  /**
   * Gets if icon is visible or not visible
   *
   * @return true if visible, otherwise false
   * @since ITP 1.1.0
   */
  public boolean getIconVisible() {
    return ICON_VISIBLE.get(getMap());
  }

  /**
   * Sets if text is visible or not visible
   *
   * @param visible true for visible, otherwise false
   * @return this TitledTabStateProperties
   * @since ITP 1.1.0
   */
  public TitledTabStateProperties setTextVisible(boolean visible) {
    TEXT_VISIBLE.set(getMap(), visible);
    return this;
  }

  /**
   * Gets if text is visible or not visible
   *
   * @return true if visible, otherwise false
   * @since ITP 1.1.0
   */
  public boolean getTextVisible() {
    return TEXT_VISIBLE.get(getMap());
  }

  /**
   * Sets if title component is visible or not visible
   *
   * @param visible true for enabled, otherwise false
   * @return this TitledTabStateProperties
   * @since ITP 1.1.0
   */
  public TitledTabStateProperties setTitleComponentVisible(boolean visible) {
    TITLE_COMPONENT_VISIBLE.set(getMap(), visible);
    return this;
  }

  /**
   * Gets if title component is visible or not visible
   *
   * @return true if enabled, otherwise false
   * @since ITP 1.1.0
   */
  public boolean getTitleComponentVisible() {
    return TITLE_COMPONENT_VISIBLE.get(getMap());
  }

  /**
   * Sets the text's and icon's horizontal alignment
   *
   * @param alignment text and icon alignment
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setHorizontalAlignment(Alignment alignment) {
    HORIZONTAL_ALIGNMENT.set(getMap(), alignment);
    return this;
  }

  /**
   * Gets the text's and icon's horizontal alignment
   *
   * @return text and icon alignment
   */
  public Alignment getHorizontalAlignment() {
    return HORIZONTAL_ALIGNMENT.get(getMap());
  }

  /**
   * Sets the text's and icon's vertical alignment
   *
   * @param alignment text and icon horizontal alignment
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setVerticalAlignment(Alignment alignment) {
    VERTICAL_ALIGNMENT.set(getMap(), alignment);
    return this;
  }

  /**
   * Gets the text's and icon's vertical alignment
   *
   * @return text and icon vertical alignment
   */
  public Alignment getVerticalAlignment() {
    return VERTICAL_ALIGNMENT.get(getMap());
  }

  /**
   * Sets the icon alignment relative to the text. Makes it possible to switch
   * places between text and icon.
   *
   * @param alignment icon alignment relative to text
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setIconTextRelativeAlignment(Alignment alignment) {
    ICON_TEXT_RELATIVE_ALIGNMENT.set(getMap(), alignment);
    return this;
  }

  /**
   * Gets the icon alignment relative to the text.
   *
   * @return icon alignment relative to text
   */
  public Alignment getIconTextRelativeAlignment() {
    return ICON_TEXT_RELATIVE_ALIGNMENT.get(getMap());
  }

  /**
   * Sets the gap in pixels between the text/icon and the title component
   *
   * @param gap number of pixels
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setTextTitleComponentGap(int gap) {
    TEXT_TITLE_COMPONENT_GAP.set(getMap(), gap);
    return this;
  }

  /**
   * Gets the gap in pixels between the text/icon and the title component
   *
   * @return number of pixels
   */
  public int getTextTitleComponentGap() {
    return TEXT_TITLE_COMPONENT_GAP.get(getMap());
  }

  /**
   * Sets the title components alignment relative to the text/icon
   *
   * @param alignment title component alignment relative to text/icon
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setTitleComponentTextRelativeAlignment(Alignment alignment) {
    TITLE_COMPONENT_TEXT_RELATIVE_ALIGNMENT.set(getMap(), alignment);
    return this;
  }

  /**
   * Gets the title components alignment relative to the text/icon
   *
   * @return title component alignment relative to text/icon
   */
  public Alignment getTitleComponentTextRelativeAlignment() {
    return TITLE_COMPONENT_TEXT_RELATIVE_ALIGNMENT.get(getMap());
  }

  /**
   * Sets the direction, i.e. the line layout of the titled tab's components. The text
   * and icon will be rotated in the given direction and the title component will be
   * moved.
   *
   * @param direction direction
   * @return this TitledTabStateProperties
   */
  public TitledTabStateProperties setDirection(Direction direction) {
    DIRECTION.set(getMap(), direction);
    return this;
  }

  /**
   * Gets the direction, i.e. the line layout of the titled tab components. The text
   * and icon are rotated in the given direction and the title component will be moved.
   *
   * @return direction
   */
  public Direction getDirection() {
    return DIRECTION.get(getMap());
  }

  /**
   * Gets the component properties.
   *
   * @return component properties
   */
  public ComponentProperties getComponentProperties() {
    return new ComponentProperties(COMPONENT_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the shaped panel properties.
   *
   * @return shaped panel properties
   * @since ITP 1.2.0
   */
  public ShapedPanelProperties getShapedPanelProperties() {
    return new ShapedPanelProperties(SHAPED_PANEL_PROPERTIES.get(getMap()));
  }
}
