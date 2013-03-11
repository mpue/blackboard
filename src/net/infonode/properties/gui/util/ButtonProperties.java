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


// $Id: ButtonProperties.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.properties.gui.util;

import javax.swing.AbstractButton;
import javax.swing.Icon;

import net.infonode.gui.button.ButtonFactory;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.ButtonFactoryProperty;
import net.infonode.properties.types.IconProperty;
import net.infonode.properties.types.StringProperty;

/**
 * Properties and property values for a button.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class ButtonProperties extends PropertyMapContainer {
  /**
   * Property group for all button properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Button Properties", "");

  /**
   * The button icon.
   */
  public static final IconProperty ICON = new IconProperty(PROPERTIES, "Icon", "Icon for the enabled button state.",
                                                           PropertyMapValueHandler.INSTANCE);

  /**
   * The disabled button icon.
   */
  public static final IconProperty DISABLED_ICON = new IconProperty(PROPERTIES, "Disabled Icon",
                                                                    "Icon for the disabled button state.",
                                                                    PropertyMapValueHandler.INSTANCE);

  /**
   * The enabled button tool tip text.
   */
  public static final StringProperty TOOL_TIP_TEXT = new StringProperty(PROPERTIES,
                                                                        "Tool Tip Text",
                                                                        "The button tool tip text.",
                                                                        PropertyMapValueHandler.INSTANCE);

  /**
   * <p>The button factory.</p>
   *
   * <p>The created button will be assigned the icon from
   * {@link #ICON} or {@link #DISABLED_ICON} and the tool tip from {@link #TOOL_TIP_TEXT}. An action listener is also added to the button.
   * </p>
   */
  public static final ButtonFactoryProperty FACTORY = new ButtonFactoryProperty(PROPERTIES,
                                                                                "Factory",
                                                                                "The button factory. This factory is used to create a button. The " +
                                                                                "created button will be assigned the icon from the '" +
                                                                                ICON.getName() + "' property or the '" +
                                                                                DISABLED_ICON.getName() +
                                                                                "' property and the tool tip from the '" +
                                                                                TOOL_TIP_TEXT.getName() +
                                                                                "' " +
                                                                                "property. An action listener is also added to the button.",
                                                                                PropertyMapValueHandler.INSTANCE);

  static {
    ButtonProperties properties = new ButtonProperties(PROPERTIES.getDefaultMap());
    properties.setIcon(null).setDisabledIcon(null).setToolTipText(null);
  }

  /**
   * Creates an empty property object.
   */
  public ButtonProperties() {
    super(PROPERTIES);
  }

  /**
   * Creates a property map containing the map.
   *
   * @param map the property map
   */
  public ButtonProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public ButtonProperties(ButtonProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public ButtonProperties addSuperObject(ButtonProperties properties) {
    getMap().addSuperMap(properties.getMap());

    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public ButtonProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes the given super object.
   *
   * @param superObject super object to remove
   * @return this
   */
  public ButtonProperties removeSuperObject(ButtonProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Sets the button icon.
   *
   * @param icon the button icon
   * @return this
   */
  public ButtonProperties setIcon(Icon icon) {
    ICON.set(getMap(), icon);
    return this;
  }

  /**
   * Returns the button icon.
   *
   * @return the button icon
   */
  public Icon getIcon() {
    return ICON.get(getMap());
  }

  /**
   * Sets the disabled button icon.
   *
   * @param icon the disabled button icon
   * @return this
   */
  public ButtonProperties setDisabledIcon(Icon icon) {
    DISABLED_ICON.set(getMap(), icon);
    return this;
  }

  /**
   * Returns the disabled button icon.
   *
   * @return the disabled button icon
   */
  public Icon getDisabledIcon() {
    return DISABLED_ICON.get(getMap());
  }

  /**
   * Returns the button tool tip text.
   *
   * @return the button tool tip text
   */
  public String getToolTipText() {
    return TOOL_TIP_TEXT.get(getMap());
  }

  /**
   * Sets the button tool tip text.
   *
   * @param text the button tool tip text
   * @return this
   */
  public ButtonProperties setToolTipText(String text) {
    TOOL_TIP_TEXT.set(getMap(), text);
    return this;
  }

  /**
   * <p>Gets the button factory.</p>
   *
   * <p>The created button will be assigned the icon from
   * {@link #ICON} or {@link #DISABLED_ICON} and the tool tip from {@link #TOOL_TIP_TEXT}.
   * An action listener is also added to the button.
   * </p>
   *
   * @return the button factory
   */
  public ButtonFactory getFactory() {
    return FACTORY.get(getMap());
  }

  /**
   * <p>Sets the button factory.</p>
   *
   * <p>The created button will be assigned the icon from
   * {@link #ICON} or {@link #DISABLED_ICON} and the tool tip from {@link #TOOL_TIP_TEXT}.
   * An action listener is also added to the button.
   * </p>
   *
   * @param factory the button factory
   * @return this
   */
  public ButtonProperties setFactory(ButtonFactory factory) {
    FACTORY.set(getMap(), factory);
    return this;
  }

  /**
   * Applies the icon, disabled icon and tool tip to the given button
   *
   * @param button botton
   * @return the button
   */
  public AbstractButton applyTo(AbstractButton button) {
    button.setIcon(getIcon());
    button.setDisabledIcon(getDisabledIcon());
    button.setToolTipText(getToolTipText());

    return button;
  }
}
