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


// $Id: InfoNodeLookAndFeelThemes.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.laf;

import java.awt.Color;

import net.infonode.gui.Colors;
import net.infonode.util.ColorUtil;

/**
 * Contains some predefined InfoNode look and feel themes.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class InfoNodeLookAndFeelThemes {
  private InfoNodeLookAndFeelThemes() {
  }

  /**
   * A theme with dark blue controls and green selection.
   *
   * @return the theme
   */
  public static InfoNodeLookAndFeelTheme getDarkBlueGreenTheme() {
    float hue = Colors.ROYAL_BLUE_HUE;
    float saturation = 0.20f;

    return new InfoNodeLookAndFeelTheme("Dark Blue Green Theme",
                                        Color.getHSBColor(hue, saturation, 0.5f),
                                        Color.getHSBColor(hue, saturation + 0.1f, 0.9f),
                                        Color.getHSBColor(Colors.SAND_HUE, 0.04f, 0.5f),
                                        Color.WHITE,
                                        new Color(0, 170, 0),
                                        Color.WHITE,
                                        0.8);
  }

  /**
   * A theme with light gray controls and blue selection.
   *
   * @return the theme
   */
  public static InfoNodeLookAndFeelTheme getGrayTheme() {
    float hue = Colors.ROYAL_BLUE_HUE;
    float saturation = 0.4f;

    return new InfoNodeLookAndFeelTheme("Gray Theme",
                                        new Color(220, 220, 220),
                                        Color.getHSBColor(hue, saturation + 0.3f, 1f),
                                        InfoNodeLookAndFeelTheme.DEFAULT_BACKGROUND_COLOR,
                                        InfoNodeLookAndFeelTheme.DEFAULT_TEXT_COLOR,
                                        Color.getHSBColor(hue, saturation, 1.0f),
                                        Color.BLACK);
  }

  /**
   * A theme with light blue controls and blue selection.
   *
   * @return the theme
   */
  public static InfoNodeLookAndFeelTheme getBlueIceTheme() {
    float hue = Colors.ROYAL_BLUE_HUE;
    float saturation = 0.4f;

    Color color = new Color(197, 206, 218);
    InfoNodeLookAndFeelTheme theme = new InfoNodeLookAndFeelTheme("Blue Ice Theme",
                                                                  color,
                                                                  Color.getHSBColor(hue, saturation + 0.3f, 1f),
                                                                  InfoNodeLookAndFeelTheme.DEFAULT_BACKGROUND_COLOR,
                                                                  InfoNodeLookAndFeelTheme.DEFAULT_TEXT_COLOR,
                                                                  Color.getHSBColor(hue, saturation - 0.15f, 0.9f),
                                                                  Color.BLACK,
                                                                  0.3);
    theme.setPrimaryControlColor(ColorUtil.mult(color, 0.9));
    return theme;
  }

  /**
   * A low contrast theme with a softer tone.
   *
   * @return the theme
   */
  public static InfoNodeLookAndFeelTheme getSoftGrayTheme() {
    InfoNodeLookAndFeelTheme theme = new InfoNodeLookAndFeelTheme("Soft Gray Theme",
                                                                  /*new Color(230, 225, 217)*/new Color(230, 230, 233),
                                                                  new Color(212, 220, 236),
                                                                  new Color(255, 255, 255),
                                                                  new Color(0, 0, 0),
                                                                  new Color(76, 113, 188),
                                                                  new Color(255, 255, 255),
                                                                  0.2);
    theme.setActiveInternalFrameTitleBackgroundColor(new Color(76, 113, 188));
    //theme.setActiveInternalFrameTitleBackgroundColor(new Color(118, 146, 204));
    theme.setActiveInternalFrameTitleForegroundColor(Color.WHITE);
    theme.setActiveInternalFrameTitleGradientColor(new Color(80, 135, 248));
    //theme.setActiveInternalFrameTitleGradientColor(new Color(149, 182, 251));
    theme.setInactiveInternalFrameTitleBackgroundColor(new Color(220, 220, 222)/*new Color(210, 206, 198)*/);
    theme.setInactiveInternalFrameTitleForegroundColor(Color.BLACK);
    theme.setInactiveInternalFrameTitleGradientColor(new Color(240, 240, 243));

    return theme;
  }


}
