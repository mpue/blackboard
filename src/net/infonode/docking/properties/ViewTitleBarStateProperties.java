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


// $Id: ViewTitleBarStateProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

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
import net.infonode.properties.types.IconProperty;
import net.infonode.properties.types.IntegerProperty;
import net.infonode.properties.types.StringProperty;
import net.infonode.util.Alignment;

/**
 * Properties and property values for a view title bar state.
 *
 * @author johan
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class ViewTitleBarStateProperties extends PropertyMapContainer {
  /**
   * Property group containing all view title bar state properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("View Title Bar State Properties", "");

  /**
   * Properties for the component
   *
   * @see #getComponentProperties
   */
  public static final PropertyMapProperty COMPONENT_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                         "Component Properties",
                                                                                         "Properties for title bar.",
                                                                                         ComponentProperties.PROPERTIES);

  /**
   * Properties for the shaped panel
   *
   * @see #getShapedPanelProperties
   */
  public static final PropertyMapProperty SHAPED_PANEL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                            "Shaped Panel Properties",
                                                                                            "Properties for shaped title bar.",
                                                                                            ShapedPanelProperties.PROPERTIES);
  /**
   * The minimize button property values.
   *
   * @see #getMinimizeButtonProperties
   */
  public static final PropertyMapProperty MINIMIZE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                               "Minimize Button Properties",
                                                                                               "The minimize button property values.",
                                                                                               WindowTabButtonProperties.PROPERTIES);


  /**
   * The minimize button property values.
   *
   * @see #getMinimizeButtonProperties
   */
  public static final PropertyMapProperty MAXIMIZE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                               "Maximize Button Properties",
                                                                                               "The maximizee button property values.",
                                                                                               WindowTabButtonProperties.PROPERTIES);
  /**
   * The restore button property values.
   *
   * @see #getRestoreButtonProperties
   */
  public static final PropertyMapProperty RESTORE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                              "Restore Button Properties",
                                                                                              "The restore button property values.",
                                                                                              WindowTabButtonProperties.PROPERTIES);

  /**
   * The close button property values.
   *
   * @see #getCloseButtonProperties
   */
  public static final PropertyMapProperty CLOSE_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                            "Close Button Properties",
                                                                                            "The close button property values.",
                                                                                            WindowTabButtonProperties.PROPERTIES);

  /**
   * The undock button property values.
   *
   * @see #getUndockButtonProperties
   */
  public static final PropertyMapProperty UNDOCK_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                             "Undock Button Properties",
                                                                                             "The undock button property values.",
                                                                                             WindowTabButtonProperties.PROPERTIES);

  /**
   * The dock button property values.
   *
   * @see #getDockButtonProperties
   */
  public static final PropertyMapProperty DOCK_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                           "Dock Button Properties",
                                                                                           "The dockbutton property values.",
                                                                                           WindowTabButtonProperties.PROPERTIES);

  /**
   * The title bar title.
   */
  public static final StringProperty TITLE = new StringProperty(PROPERTIES,
                                                                "Title",
                                                                "The title bar title.",
                                                                PropertyMapValueHandler.INSTANCE);

  /**
   * Title visible property
   *
   * @see #setTitleVisible
   * @see #getTitleVisible
   */
  public static final BooleanProperty TITLE_VISIBLE = new BooleanProperty(PROPERTIES,
                                                                          "Title Visible",
                                                                          "Controls if the title should be visible or not.",
                                                                          PropertyMapValueHandler.INSTANCE);

  /**
   * The title bar icon.
   */
  public static final IconProperty ICON = new IconProperty(PROPERTIES,
                                                           "Icon",
                                                           "The title bar icon.",
                                                           PropertyMapValueHandler.INSTANCE);

  /**
   * Icon visible property
   *
   * @see #setIconVisible
   * @see #getIconVisible
   */
  public static final BooleanProperty ICON_VISIBLE = new BooleanProperty(PROPERTIES,
                                                                         "Icon Visible",
                                                                         "Controls if the icon should be visible or not.",
                                                                         PropertyMapValueHandler.INSTANCE);
  /**
   * Icon text gap property
   *
   * @see #setIconTextGap
   * @see #getIconTextGap
   */
  public static final IntegerProperty ICON_TEXT_GAP = IntegerProperty.createPositive(PROPERTIES,
                                                                                     "Icon Text Gap",
                                                                                     "Gap in pixels between the icon and the title",
                                                                                     2,
                                                                                     PropertyMapValueHandler.INSTANCE);

  /**
   * Icon Text Horizontal alignment property
   *
   * @see #setIconTextHorizontalAlignment
   * @see #getIconTextHorizontalAlignment
   */
  public static final AlignmentProperty ICON_TEXT_HORIZONTAL_ALIGNMENT = new AlignmentProperty(PROPERTIES,
                                                                                               "Icon Text Horizontal Alignment",
                                                                                               "Horizontal alignment for the icon and title text.",
                                                                                               PropertyMapValueHandler.INSTANCE,
                                                                                               Alignment.getHorizontalAlignments());

  /**
   * Button spacing
   *
   * @see #setButtonSpacing
   * @see #getButtonSpacing
   */
  public static final IntegerProperty BUTTON_SPACING = IntegerProperty.createPositive(PROPERTIES,
                                                                                      "Button Spacing",
                                                                                      "Spacing in pixels between the buttons on the title bar",
                                                                                      2,
                                                                                      PropertyMapValueHandler.INSTANCE);

  /**
   * Creates an empty property object.
   */
  public ViewTitleBarStateProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public ViewTitleBarStateProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public ViewTitleBarStateProperties(ViewTitleBarStateProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public ViewTitleBarStateProperties addSuperObject(ViewTitleBarStateProperties properties) {
    getMap().addSuperMap(properties.getMap());

    return this;
  }

  /**
   * Removes a super object.
   *
   * @param superObject the super object to remove
   * @return this
   */
  public ViewTitleBarStateProperties removeSuperObject(ViewTitleBarStateProperties superObject) {
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
   * Returns the minimize button property values.
   *
   * @return the minimize button property values
   */
  public WindowTabButtonProperties getMinimizeButtonProperties() {
    return new WindowTabButtonProperties(MINIMIZE_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the maximize button property values.
   *
   * @return the maximize button property values
   */
  public WindowTabButtonProperties getMaximizeButtonProperties() {
    return new WindowTabButtonProperties(MAXIMIZE_BUTTON_PROPERTIES.get(getMap()));
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
   */
  public WindowTabButtonProperties getUndockButtonProperties() {
    return new WindowTabButtonProperties(UNDOCK_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the dock button property values.
   *
   * @return the dock button property values
   */
  public WindowTabButtonProperties getDockButtonProperties() {
    return new WindowTabButtonProperties(DOCK_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Sets the spacing between the buttons on the title bar
   *
   * @param spacing spacing in pixels
   * @return this
   */
  public ViewTitleBarStateProperties setButtonSpacing(int spacing) {
    BUTTON_SPACING.set(getMap(), spacing);
    return this;
  }

  /**
   * Returns the spacing between the buttons on the title bar
   *
   * @return spacing in pixels
   */
  public int getButtonSpacing() {
    return BUTTON_SPACING.get(getMap());
  }


  /**
   * Sets the title.
   *
   * @param title the title
   * @return this
   */
  public ViewTitleBarStateProperties setTitle(String title) {
    TITLE.set(getMap(), title);

    return this;
  }

  /**
   * Returns the view title.
   *
   * @return the view title
   */
  public String getTitle() {
    return TITLE.get(getMap());
  }

  /**
   * Sets if the title should be visible or not
   *
   * @param visible True for visible, otherwise false
   * @return this
   */
  public ViewTitleBarStateProperties setTitleVisible(boolean visible) {
    TITLE_VISIBLE.set(getMap(), visible);
    return this;
  }

  /**
   * Returns if the title should be visible or not
   *
   * @return True if visible, otherwise false
   */
  public boolean getTitleVisible() {
    return TITLE_VISIBLE.get(getMap());
  }

  /**
   * Sets the icon.
   *
   * @param icon the icon
   * @return this
   */
  public ViewTitleBarStateProperties setIcon(Icon icon) {
    ICON.set(getMap(), icon);

    return this;
  }

  /**
   * Returns the view icon.
   *
   * @return the view icon
   */
  public Icon getIcon() {
    return ICON.get(getMap());
  }

  /**
   * Sets if the icon should be visible or not
   *
   * @param visible True for visible, otherwise false
   * @return this
   */
  public ViewTitleBarStateProperties setIconVisible(boolean visible) {
    ICON_VISIBLE.set(getMap(), visible);
    return this;
  }

  /**
   * Returns if the icon should be visible or not
   *
   * @return True if visible, otherwise false
   */
  public boolean getIconVisible() {
    return ICON_VISIBLE.get(getMap());
  }

  /**
   * Sets the gap between the icon and the title in the title bar
   *
   * @param gap gap in pixels
   * @return this
   */
  public ViewTitleBarStateProperties setIconTextGap(int gap) {
    ICON_TEXT_GAP.set(getMap(), gap);
    return this;
  }

  /**
   * Returns the gap between the icon and the title in the title bar
   *
   * @return gap in pixels
   */
  public int getIconTextGap() {
    return ICON_TEXT_GAP.get(getMap());
  }

  /**
   * Sets the text's and icon's horizontal alignment
   *
   * @param alignment text and icon alignment
   * @return this
   */
  public ViewTitleBarStateProperties setIconTextHorizontalAlignment(Alignment alignment) {
    ICON_TEXT_HORIZONTAL_ALIGNMENT.set(getMap(), alignment);
    return this;
  }

  /**
   * Gets the text's and icon's horizontal alignment
   *
   * @return text and icon alignment
   */
  public Alignment getIconTextHorizontalAlignment() {
    return ICON_TEXT_HORIZONTAL_ALIGNMENT.get(getMap());
  }
}
