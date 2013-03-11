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


// $Id: TabbedUIDefaults.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.UIManager;

import net.infonode.gui.UIManagerUtil;
import net.infonode.util.ColorUtil;

/**
 * Methods for retrieving UI defaults for the current "Look and Feel" from the
 * UIManager. The values are adapted to be used with classes in the TabbedPanel package.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class TabbedUIDefaults {
  private static final int BUTTON_ICON_SIZE = 11;

  private TabbedUIDefaults() {
  }

  /**
   * Gets the content area background color
   *
   * @return a copy of the color
   */
  public static Color getContentAreaBackground() {
    return UIManagerUtil.getColor("Panel.background", "control", Color.LIGHT_GRAY);
  }

  /**
   * Gets the tab normal state foreground color
   *
   * @return a copy of the color
   */
  public static Color getNormalStateForeground() {
    return UIManagerUtil.getColor("TabbedPane.foreground", "controlText", Color.BLACK);
  }

  /**
   * Gets the tab normal state background color
   *
   * @return a copy of the color
   */
  public static Color getNormalStateBackground() {
    return UIManagerUtil.getColor("TabbedPane.background", "control", Color.LIGHT_GRAY);
  }

  /**
   * Gets the tab highlighted state foreground color
   *
   * @return a copy of the color
   */
  public static Color getHighlightedStateForeground() {
    return getNormalStateForeground();
  }

  /**
   * Gets the tab highlighted state backgound color
   *
   * @return a copy of the color
   */
  public static Color getHighlightedStateBackground() {
    return UIManagerUtil.getColor("Panel.background", "control", Color.LIGHT_GRAY);
  }

  /**
   * Gets the tab disabled state foreground color
   *
   * @return a copy of the color
   */
  public static Color getDisabledForeground() {
    return UIManagerUtil.getColor("inactiveCaptionText", "controlText", Color.WHITE);
  }

  /**
   * Gets the tab disabled state background color
   *
   * @return a copy of the color
   */
  public static Color getDisabledBackground() {
    return ColorUtil.mult(getNormalStateBackground(), 0.9);
  }

  /**
   * Gets the (border) dark shadow color
   *
   * @return a copy of the color
   */
  public static Color getDarkShadow() {
    return UIManagerUtil.getColor("TabbedPane.darkShadow", "controlDkShadow", Color.BLACK);
  }

  /**
   * Gets the (border) highlight color
   *
   * @return a copy of the color
   */
  public static Color getHighlight() {
    return UIManagerUtil.getColor("TabbedPane.highlight", "controlHighlight", Color.WHITE);
  }

  /**
   * Gets the font
   *
   * @return a copy of the font
   */
  public static Font getFont() {
    return UIManagerUtil.getFont("TabbedPane.font");
  }

  /**
   * Gets the icon text gap
   *
   * @return the icon text gap
   */
  public static int getIconTextGap() {
    return UIManager.getInt("TabbedPane.textIconGap");
  }

  /**
   * Gets the tab insets
   *
   * @return a copy of the insets
   */
  public static Insets getTabInsets() {
    return UIManagerUtil.getInsets("TabbedPane.tabInsets", new Insets(2, 2, 0, 2));
  }

  /**
   * Gets the insets for the content area
   *
   * @return a copy of the insets
   */
  public static Insets getContentAreaInsets() {
    return UIManagerUtil.getInsets("TabbedPane.contentBorderInsets", new Insets(2, 2, 2, 2));
  }

  /**
   * Gets the default icon size for buttons
   *
   * @return icon size in pixels
   */
  public static int getButtonIconSize() {
    return BUTTON_ICON_SIZE;
  }
}
