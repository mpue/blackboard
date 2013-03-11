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


// $Id: SoftBlueIceTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel.theme;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.border.Border;

import net.infonode.gui.colorprovider.ColorBlender;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.gui.componentpainter.FixedTransformComponentPainter;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.gui.shaped.border.RoundedCornerBorder;
import net.infonode.properties.base.Property;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.util.ColorUtil;

/**
 * A light blue theme with gradients and rounded corners.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since ITP 1.2.0
 */
public class SoftBlueIceTheme extends TabbedPanelTitledTabTheme {
  public static final FixedColorProvider DEFAULT_DARK_COLOR = new FixedColorProvider(
      ColorUtil.mult(new Color(160, 170, 190), 0.90));
  public static final FixedColorProvider DEFAULT_LIGHT_COLOR = new FixedColorProvider(new Color(220, 230, 240));

  private ColorProvider darkColor;
  private ColorProvider lightColor;
  private TabbedPanelProperties tabbedPanelProperties = new TabbedPanelProperties();
  private TitledTabProperties titledTabProperties = new TitledTabProperties();

  /**
   * Creates a theme with default colors and rounded corners.
   */
  public SoftBlueIceTheme() {
    this(DEFAULT_DARK_COLOR, DEFAULT_LIGHT_COLOR, 4);
  }

  /**
   * Constructor.
   *
   * @param darkColor  the dark color used in gradients
   * @param lightColor the light color used in gradients
   * @param cornerType the amount of rounding to use for corners, 0-4
   */
  public SoftBlueIceTheme(ColorProvider darkColor, ColorProvider lightColor, int cornerType) {
    this.darkColor = darkColor;
    this.lightColor = lightColor;

    ColorProvider light = lightColor;
    ColorProvider dark = new ColorBlender(darkColor, lightColor, 0.3f);
    ColorProvider dark2 = new ColorBlender(darkColor, FixedColorProvider.WHITE, 0.1f);
    ColorProvider dark3 = darkColor;

    Border roundedBorder = new RoundedCornerBorder(dark3,
                                                   light,
                                                   cornerType,
                                                   cornerType,
                                                   cornerType,
                                                   cornerType,
                                                   true,
                                                   true,
                                                   true,
                                                   true);

    Border tabNormalBorder = roundedBorder;

    Border contentBorder = new RoundedCornerBorder(dark3,
                                                   light,
                                                   cornerType,
                                                   cornerType,
                                                   cornerType,
                                                   cornerType,
                                                   false,
                                                   true,
                                                   true,
                                                   true);

    ComponentPainter areaPainter = new FixedTransformComponentPainter(
        new GradientComponentPainter(dark2, light, light, dark2));
    ComponentPainter contentPainter = new FixedTransformComponentPainter(
        new GradientComponentPainter(light, dark2, dark2, light));
    ComponentPainter highlightPainter = new FixedTransformComponentPainter(
        new GradientComponentPainter(FixedColorProvider.WHITE, light, light, light));
    ComponentPainter normalPainter = new FixedTransformComponentPainter(
        new GradientComponentPainter(light, dark, dark, dark));

    tabbedPanelProperties
        .setPaintTabAreaShadow(true)
        .setTabSpacing(2)
        .setShadowEnabled(false);

    tabbedPanelProperties.getTabAreaProperties()
        .getComponentProperties()
        .setBorder(roundedBorder)
        .setInsets(new Insets(2, 2, 3, 3));

    tabbedPanelProperties.getTabAreaProperties()
        .getShapedPanelProperties()
        .setClipChildren(true)
        .setComponentPainter(areaPainter)
        .setOpaque(false);

    tabbedPanelProperties.getTabAreaComponentsProperties()
        .setStretchEnabled(true)
        .getComponentProperties()
        .setBorder(null)
        .setInsets(new Insets(0, 0, 0, 0));

    tabbedPanelProperties.getTabAreaComponentsProperties()
        .getShapedPanelProperties().setOpaque(false);

    tabbedPanelProperties.getContentPanelProperties()
        .getComponentProperties()
        .setBorder(contentBorder)
        .setInsets(new Insets(3, 3, 4, 4));

    tabbedPanelProperties.getContentPanelProperties()
        .getShapedPanelProperties()
        .setComponentPainter(contentPainter)
        .setClipChildren(true)
        .setOpaque(false);

    titledTabProperties.setHighlightedRaised(0);

    Font font = titledTabProperties.getNormalProperties().getComponentProperties().getFont();

    if (font != null)
      font = font.deriveFont(Font.PLAIN).deriveFont(11f);

    titledTabProperties.getNormalProperties()
        .getComponentProperties()
        .setBorder(tabNormalBorder)
        .setInsets(new Insets(1, 4, 2, 5))
        .setBackgroundColor(
            titledTabProperties.getHighlightedProperties().getComponentProperties().getBackgroundColor())
        .setFont(font);

    titledTabProperties.getNormalProperties()
        .getShapedPanelProperties()
        .setComponentPainter(normalPainter).setOpaque(false);

    Property[] linkedProperties = {ComponentProperties.BORDER, ComponentProperties.INSETS, ComponentProperties.FONT};

    for (int i = 0; i < linkedProperties.length; i++) {
      titledTabProperties.getHighlightedProperties().getComponentProperties().getMap().
          createRelativeRef(linkedProperties[i],
                            titledTabProperties.getNormalProperties().getComponentProperties().getMap(),
                            linkedProperties[i]);
    }

    titledTabProperties.getHighlightedProperties()
        .getShapedPanelProperties()
        .setComponentPainter(highlightPainter);
  }

  public String getName() {
    return "Soft Blue Ice Theme";
  }

  public TabbedPanelProperties getTabbedPanelProperties() {
    return tabbedPanelProperties;
  }

  public TitledTabProperties getTitledTabProperties() {
    return titledTabProperties;
  }

  /**
   * Returns the dark gradient color.
   *
   * @return the dark gradient color
   */
  public ColorProvider getDarkColor() {
    return darkColor;
  }

  /**
   * Returns the light gradient color.
   *
   * @return the light gradient color
   */
  public ColorProvider getLightColor() {
    return lightColor;
  }
}
