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


// $Id: WindowTabProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.tabbedpanel.titledtab.TitledTabStateProperties;

/**
 * Properties and property values for window tabs.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class WindowTabProperties extends PropertyMapContainer {
  /**
   * Property group containing all window tab properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Tab Properties", "");

  /**
   * Property values for the titled tab used in the tab.
   */
  public static final PropertyMapProperty TITLED_TAB_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Titled Tab Properties",
                              "Property values for the TitledTab used in the window tab.",
                              TitledTabProperties.PROPERTIES);

  /**
   * Property values for the titled tab when it is focused or a component in the tab's content component has focus.
   */
  public static final PropertyMapProperty FOCUSED_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Focused Properties",
                              "Property values for the TitledTab when the window is focused or a component in the tab's content component has focus.\n" +
                              "The " + TITLED_TAB_PROPERTIES + '.' +
                              TitledTabProperties.HIGHLIGHTED_PROPERTIES + " property values are inherited from.",
                              TitledTabStateProperties.PROPERTIES);

  /**
   * Property values for the tab buttons when the tab is in the normal state.
   */
  public static final PropertyMapProperty NORMAL_BUTTON_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Normal Button Properties",
                              "Property values for the tab buttons when the tab is in the normal state.",
                              WindowTabStateProperties.PROPERTIES);

  /**
   * Property values for the tab buttons when the tab is highlighted.
   */
  public static final PropertyMapProperty HIGHLIGHTED_BUTTON_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Highlighted Button Properties",
                              "Property values for the tab buttons when the tab is highlighted.",
                              WindowTabStateProperties.PROPERTIES);

  /**
   * Property values for the tab buttons when the tab is focused or a component in the tab's content component has focus.
   */
  public static final PropertyMapProperty FOCUSED_BUTTON_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Focused Button Properties",
                              "Property values for the tab buttons when the tab is focused or a component in the tab's content component has focus.",
                              WindowTabStateProperties.PROPERTIES);

  /**
   * Creates an empty property object.
   */
  public WindowTabProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public WindowTabProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public WindowTabProperties(WindowTabProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public WindowTabProperties addSuperObject(WindowTabProperties properties) {
    getMap().addSuperMap(properties.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   * @since IDW 1.1.0
   * @deprecated Use {@link #removeSuperObject(WindowTabProperties)} instead.
   */
  public WindowTabProperties removeSuperObject() {
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
  public WindowTabProperties removeSuperObject(WindowTabProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Returns the property values for the titled tab used in the tab.
   *
   * @return the property values for the titled tab used in the tab
   */
  public TitledTabProperties getTitledTabProperties() {
    return new TitledTabProperties(TITLED_TAB_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for the titled tab when it is focused or a component in the tab's content component has focus.
   *
   * @return the property values for the titled tab when it is focused or a component in the tab's content component has focus
   */
  public TitledTabStateProperties getFocusedProperties() {
    return new TitledTabStateProperties(FOCUSED_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for the tab buttons when the tab is in the normal state.
   *
   * @return the property values for the tab buttons when the tab is in the normal state
   */
  public WindowTabStateProperties getNormalButtonProperties() {
    return new WindowTabStateProperties(NORMAL_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for the tab buttons when the tab is highlighted.
   *
   * @return the property values for the tab buttons when the tab is highlighted
   */
  public WindowTabStateProperties getHighlightedButtonProperties() {
    return new WindowTabStateProperties(HIGHLIGHTED_BUTTON_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for the tab buttons when the tab is focused or a component in the tab's content
   * component has focus.
   *
   * @return the property values for the tab buttons when the tab is focused or a component in the tab's content
   *         component has focus
   */
  public WindowTabStateProperties getFocusedButtonProperties() {
    return new WindowTabStateProperties(FOCUSED_BUTTON_PROPERTIES.get(getMap()));
  }

}
