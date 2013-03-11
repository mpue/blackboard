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


// $Id: UIManagerColorProvider.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.gui.colorprovider;

import java.awt.Color;

import javax.swing.UIManager;

/**
 * A {@link ColorProvider} which returns a property color from the {@link UIManager}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class UIManagerColorProvider extends AbstractColorProvider {
  private static final long serialVersionUID = 1;

  /**
   * A provider for the 'control' color.
   */
  public static final UIManagerColorProvider CONTROL_COLOR = new UIManagerColorProvider("control", Color.LIGHT_GRAY);

  /**
   * A provider for the 'controlDkShadow' color.
   */
  public static final UIManagerColorProvider CONTROL_DARK_SHADOW = new UIManagerColorProvider("controlDkShadow",
      Color.BLACK);

  /**
   * A provider for the 'TabbedPane.highlight' color.
   */
  public static final UIManagerColorProvider TABBED_PANE_HIGHLIGHT = new UIManagerColorProvider("TabbedPane.highlight",
      Color.WHITE);

  /**
   * A provider for the 'TabbedPane.shadow' color.
   */
  public static final UIManagerColorProvider TABBED_PANE_SHADOW = new UIManagerColorProvider("TabbedPane.shadow",
      Color.BLACK);

  /**
   * A provider for the 'TabbedPane.darkShadow' color.
   */
  public static final UIManagerColorProvider TABBED_PANE_DARK_SHADOW = new UIManagerColorProvider(
      "TabbedPane.darkShadow", Color.BLACK);

  /**
   * A provider for the 'TabbedPane.background' color.
   */
  public static final UIManagerColorProvider TABBED_PANE_BACKGROUND = new UIManagerColorProvider(
      "TabbedPane.background", Color.LIGHT_GRAY);

  /**
   * A provider for the 'Desktop.background' color.
   */
  public static final UIManagerColorProvider DESKTOP_BACKGROUND = new UIManagerColorProvider("Desktop.background",
      Color.BLUE);

  private final String propertyName;
  private Color defaultColor;

  /**
   * Constructor.
   *
   * @param propertyName the name of the property which value will be retrieved from the {@link UIManager}.
   */
  public UIManagerColorProvider(String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   * Constructor.
   *
   * @param propertyName the name of the property which value will be retrieved from the {@link UIManager}.
   * @param defaultColor the color to be used if the specified color doesn't exist in the UIManager
   */
  public UIManagerColorProvider(String propertyName, Color defaultColor) {
    this.propertyName = propertyName;
    this.defaultColor = defaultColor;
  }

  public Color getColor() {
    Color color = UIManager.getColor(propertyName);

    return color == null ? defaultColor : color;
  }
}
