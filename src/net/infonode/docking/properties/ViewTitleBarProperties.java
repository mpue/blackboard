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


// $Id: ViewTitleBarProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import net.infonode.gui.DimensionProvider;
import net.infonode.gui.hover.HoverListener;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.DimensionProviderProperty;
import net.infonode.properties.types.DirectionProperty;
import net.infonode.properties.types.HoverListenerProperty;
import net.infonode.properties.types.IntegerProperty;
import net.infonode.util.Direction;

/**
 * Properties and property values for a view title bar.
 *
 * @author johan
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class ViewTitleBarProperties extends PropertyMapContainer {
  /**
   * Property group containing all view title bar properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("View Title Bar Properties", "");

  /**
   * Normal properties
   *
   * @see #getNormalProperties
   */
  public static final PropertyMapProperty NORMAL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                      "Normal Properties",
                                                                                      "Properties for title bar.",
                                                                                      ViewTitleBarStateProperties.PROPERTIES);

  /**
   * Focused properties. Added as super object to normal properties when view has focus
   *
   * @see #getFocusedProperties
   */
  public static final PropertyMapProperty FOCUSED_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                       "Focused Properties",
                                                                                       "Properties for title bar.",
                                                                                       ViewTitleBarStateProperties.PROPERTIES);

  /**
   * Visible property
   *
   * @see #setVisible
   * @see #getVisible
   */
  public static final BooleanProperty VISIBLE = new BooleanProperty(PROPERTIES,
                                                                    "Visible",
                                                                    "Controls if the view title bar should be visible or not",
                                                                    PropertyMapValueHandler.INSTANCE);

  /**
   * Title bar minimum size property
   *
   * @see #setMinimumSizeProvider(DimensionProvider)
   * @see #getMinimumSizeProvider()
   */
  public static final DimensionProviderProperty MINIMUM_SIZE_PROVIDER = new DimensionProviderProperty(PROPERTIES,
                                                                                                      "Minimum Size",
                                                                                                      "Title bar minimum size.",
                                                                                                      PropertyMapValueHandler.INSTANCE);

  /**
   * Content title bar gap property
   *
   * @see #setContentTitleBarGap
   * @see #getContentTitleBarGap
   */
  public static final IntegerProperty CONTENT_TITLE_BAR_GAP = IntegerProperty.createPositive(PROPERTIES,
                                                                                             "Content Title Bar Gap",
                                                                                             "Gap in pixels between the view content and the title bar",
                                                                                             2,
                                                                                             PropertyMapValueHandler.INSTANCE);

  /**
   * Orientation property
   *
   * @see #setOrientation
   * @see #getOrientation
   */
  public static final DirectionProperty ORIENTATION =
      new DirectionProperty(PROPERTIES, "Orientation", "Title bar's orientation relative to the view's content area.",
                            PropertyMapValueHandler.INSTANCE);

  /**
   * Direction property
   *
   * @see #setDirection
   * @see #getDirection
   */
  public static final DirectionProperty DIRECTION =
      new DirectionProperty(PROPERTIES, "Direction", "Title bar's layout direction.",
                            PropertyMapValueHandler.INSTANCE);

  /**
   * Hover listener property
   *
   * @see #setHoverListener
   * @see #getHoverListener
   */
  public static final HoverListenerProperty HOVER_LISTENER = new HoverListenerProperty(PROPERTIES,
                                                                                       "Hover Listener",
                                                                                       "Hover Listener to be used for tracking mouse hovering over the title bar.",
                                                                                       PropertyMapValueHandler.INSTANCE);


  /**
   * Creates an empty property object.
   */
  public ViewTitleBarProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public ViewTitleBarProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public ViewTitleBarProperties(ViewTitleBarProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public ViewTitleBarProperties addSuperObject(ViewTitleBarProperties properties) {
    getMap().addSuperMap(properties.getMap());

    return this;
  }

  /**
   * Removes a super object.
   *
   * @param superObject the super object to remove
   * @return this
   */
  public ViewTitleBarProperties removeSuperObject(ViewTitleBarProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Returns the property values for the title bar's normal state
   *
   * @return the property values for the title bar's normal state
   */
  public ViewTitleBarStateProperties getNormalProperties() {
    return new ViewTitleBarStateProperties(NORMAL_PROPERTIES.get(getMap()));
  }

  /**
   * <p>
   * Returns the property values for the title bar's focused state
   * </p>
   *
   * <p>
   * <strong>Note:</strong>These properties are added as super object to the normal
   * properties when the view has focus.
   * </p>
   *
   * @return the property values for the title bar's focused state
   */
  public ViewTitleBarStateProperties getFocusedProperties() {
    return new ViewTitleBarStateProperties(FOCUSED_PROPERTIES.get(getMap()));
  }

  /**
   * Sets if the title bar should be visible or not
   *
   * @param visible True for visible, otherwise false
   * @return this
   */
  public ViewTitleBarProperties setVisible(boolean visible) {
    VISIBLE.set(getMap(), visible);
    return this;
  }

  /**
   * Returns if the title bar should be visible or not
   *
   * @return True if visible, otherwise false
   */
  public boolean getVisible() {
    return VISIBLE.get(getMap());
  }

  /**
   * Sets the title bar's minimum size dimension provider
   *
   * @param size the minimum size dimension provider or null if title bar's default minimum size should be used instead
   * @return this ViewTitleBarProperties
   */
  public ViewTitleBarProperties setMinimumSizeProvider(DimensionProvider size) {
    MINIMUM_SIZE_PROVIDER.set(getMap(), size);

    return this;
  }

  /**
   * Gets the dimension provider for the title bar's minimum size
   *
   * @return the minimum size provider or null if default title bar minimum size is to be used instead
   */
  public DimensionProvider getMinimumSizeProvider() {
    return MINIMUM_SIZE_PROVIDER.get(getMap());
  }

  /**
   * Sets the gap between the view's content and the title bar
   *
   * @param gap gap in pixels
   * @return this
   */
  public ViewTitleBarProperties setContentTitleBarGap(int gap) {
    CONTENT_TITLE_BAR_GAP.set(getMap(), gap);
    return this;
  }

  /**
   * Returns the gap between the view's content and the title bar
   *
   * @return gap in pixels
   */
  public int getContentTitleBarGap() {
    return CONTENT_TITLE_BAR_GAP.get(getMap());
  }

  /**
   * Sets the orientation i.e. on what side of the view's content the title bar will be placed
   *
   * @param orientation the orientation
   * @return this
   */
  public ViewTitleBarProperties setOrientation(Direction orientation) {
    ORIENTATION.set(getMap(), orientation);
    return this;
  }

  /**
   * Returns the orientation i.e. on what side of the view's content the title bar will be placed
   *
   * @return the orientation
   */
  public Direction getOrientation() {
    return ORIENTATION.get(getMap());
  }

  /**
   * <p>
   * Sets the layout direction
   * </p>
   *
   * <p>
   * The icon, text and components are laid out in a line that will be rotated in the given direction.
   * The text and icon is rotated and the components are only moved.
   * </p>
   *
   * @param direction the layout direction
   * @return this
   */
  public ViewTitleBarProperties setDirection(Direction direction) {
    DIRECTION.set(getMap(), direction);
    return this;
  }

  /**
   * <p>
   * Returns the layout direction
   * </p>
   *
   * <p>
   * The icon, text and components are laid out in a line that will be rotated in the given direction.
   * The text and icon is rotated and the components are only moved.
   * </p>
   *
   * @return the layout direction
   */
  public Direction getDirection() {
    return DIRECTION.get(getMap());
  }

  /**
   * <p>Sets the hover listener that will be triggered when the title bar is hoverd by the mouse.</p>
   *
   * <p>The view that contains the title bar will be the source of the hover event sent to the
   * hover listener.</p>
   *
   * @param listener the hover listener
   * @return this
   * @see net.infonode.gui.hover.HoverEvent
   */
  public ViewTitleBarProperties setHoverListener(HoverListener listener) {
    HOVER_LISTENER.set(getMap(), listener);
    return this;
  }

  /**
   * <p>Gets the hover listener that will be triggered when the title bar is hovered by the mouse.</p>
   *
   * <p>The view that contains the title bar will be the source of the hover event sent to the
   * hover listener.</p>
   *
   * @return the hover listener
   * @see net.infonode.gui.hover.HoverEvent
   */
  public HoverListener getHoverListener() {
    return HOVER_LISTENER.get(getMap());
  }
}
