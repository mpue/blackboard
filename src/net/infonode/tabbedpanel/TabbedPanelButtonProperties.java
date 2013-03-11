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


// $Id: TabbedPanelButtonProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $

package net.infonode.tabbedpanel;

import net.infonode.properties.gui.util.ButtonProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;

/**
 * Tabbed panel button properties contains properties objects for all buttons in a tabbed panel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since ITP 1.3.0
 */
public class TabbedPanelButtonProperties extends PropertyMapContainer {

  /**
   * A property group for all button properties in a tabbed panel
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Tabbed Panel Button Properties",
                                                                         "Properties for a Tabbed Panel's buttons.");

  /**
   * Properties for scroll up button
   */
  public static final PropertyMapProperty SCROLL_UP_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                                "Scroll Up Button Properties",
                                                                                                "Properties for scroll up button.",
                                                                                                ButtonProperties.PROPERTIES);

  /**
   * Properties for scroll left button
   */
  public static final PropertyMapProperty SCROLL_LEFT_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                                  "Scroll Left Button Properties",
                                                                                                  "Properties for scroll left button.",
                                                                                                  ButtonProperties.PROPERTIES);

  /**
   * Properties for scroll down button
   */
  public static final PropertyMapProperty SCROLL_DOWN_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                                  "Scroll Down Button Properties",
                                                                                                  "Properties for scroll down button.",
                                                                                                  ButtonProperties.PROPERTIES);

  /**
   * Properties for scroll right button
   */
  public static final PropertyMapProperty SCROLL_RIGHT_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                                   "Scroll Right Button Properties",
                                                                                                   "Properties for scroll right button.",
                                                                                                   ButtonProperties.PROPERTIES);

  /**
   * Properties for tab drop down list button
   */
  public static final PropertyMapProperty TAB_DROP_DOWN_LIST_BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                                         "Tab Drop Down List Button Properties",
                                                                                                         "Properties for the tab drop down list button.",
                                                                                                         ButtonProperties.PROPERTIES);

  /**
   * Constructs an empty TabbedPanelButtonProperties object
   */
  public TabbedPanelButtonProperties() {
    super(PROPERTIES);
  }

  /**
   * Constructs a TabbedPanelButtonProperties object with the given object
   * as property storage
   *
   * @param object object to store properties in
   */
  public TabbedPanelButtonProperties(PropertyMap object) {
    super(object);
  }

  /**
   * Constructs a TabbedPanelButtonProperties object that inherits its properties
   * from the given TabbedPanelButtonProperties object
   *
   * @param inheritFrom TabbedPanelButtonProperties object to inherit properties from
   */
  public TabbedPanelButtonProperties(TabbedPanelButtonProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param superObject the object from which to inherit property values
   * @return this
   */
  public TabbedPanelButtonProperties addSuperObject(TabbedPanelButtonProperties superObject) {
    getMap().addSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public TabbedPanelButtonProperties removeSuperObject() {
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
  public TabbedPanelButtonProperties removeSuperObject(TabbedPanelButtonProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Gets the scroll up button properties
   *
   * @return the scroll up button properties
   */
  public ButtonProperties getScrollUpButtonProperties() {
    return new ButtonProperties(SCROLL_UP_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the scroll down button properties
   *
   * @return the scroll down button properties
   */
  public ButtonProperties getScrollDownButtonProperties() {
    return new ButtonProperties(SCROLL_DOWN_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the scroll left button properties
   *
   * @return the scroll up button properties
   */
  public ButtonProperties getScrollLeftButtonProperties() {
    return new ButtonProperties(SCROLL_LEFT_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the scroll right button properties
   *
   * @return the scroll right button properties
   */
  public ButtonProperties getScrollRightButtonProperties() {
    return new ButtonProperties(SCROLL_RIGHT_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the tab drop down list button properties
   *
   * @return the tab drop down list button properties
   */
  public ButtonProperties getTabDropDownListButtonProperties() {
    return new ButtonProperties(TAB_DROP_DOWN_LIST_BUTTON_PROPERTIES.get(getMap()));
  }
}