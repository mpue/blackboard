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


// $Id: GradientTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel.theme;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import net.infonode.gui.Colors;
import net.infonode.gui.colorprovider.ColorMultiplier;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.ColorProviderUtil;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.border.GradientTabAreaBorder;
import net.infonode.tabbedpanel.border.OpenContentBorder;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * A theme that draws gradient tab backgrounds.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since ITP 1.1.0
 */
public class GradientTheme extends TabbedPanelTitledTabTheme {
  private static final float HUE = Colors.ROYAL_BLUE_HUE;
  private static final float SATURATION = 0.06f;
  private static final float BRIGHTNESS = 0.72f;

  /**
   * The tab area background color used if no color is specified in the constructor.
   */
  public static final Color DEFAULT_TAB_AREA_BACKGROUND_COLOR = Color.getHSBColor(HUE, SATURATION, BRIGHTNESS);

  private static final Border HIGHLIGHTED_TAB_GRADIENT_BORDER = new GradientTabAreaBorder(Color.WHITE);

  private static final Border TAB_AREA_COMPONENTS_GRADIENT_BORDER =
      new GradientTabAreaBorder(new ColorMultiplier(UIManagerColorProvider.CONTROL_COLOR, 0.88),
                                UIManagerColorProvider.CONTROL_COLOR);

  private boolean opaqueTabArea;
  private boolean shadowEnabled;
  private Color borderColor;
  private Color tabAreaBackgroundColor;
  private Border normalTabGradientBorder;
  private TitledTabProperties titledTabProperties = new TitledTabProperties();
  private TabbedPanelProperties tabbedPanelProperties = new TabbedPanelProperties();

  /**
   * Creates a default theme with transparent tab area and shadows.
   */
  public GradientTheme() {
    this(false, true);
  }

  /**
   * Constructor.
   *
   * @param opaqueTabArea if true a gradient background is drawn for the tab area, otherwise it's transparent
   * @param shadowEnabled if true the shadow is enabled
   */
  public GradientTheme(boolean opaqueTabArea, boolean shadowEnabled) {
    this(opaqueTabArea, shadowEnabled, null);
  }

  /**
   * Constructor.
   *
   * @param opaqueTabArea if true a gradient background is drawn for the tab area, otherwise it's transparent
   * @param shadowEnabled if true the shadow is enabled
   * @param borderColor   the border color, null means default border color
   */
  public GradientTheme(boolean opaqueTabArea, boolean shadowEnabled, Color borderColor) {
    this(opaqueTabArea, shadowEnabled, borderColor, DEFAULT_TAB_AREA_BACKGROUND_COLOR);
  }

  /**
   * Constructor.
   *
   * @param opaqueTabArea          if true a gradient background is drawn for the tab area, otherwise it's transparent
   * @param shadowEnabled          if true the shadow is enabled
   * @param borderColor            the border color, null means default border color
   * @param tabAreaBackgroundColor the background color for the tab area and normal tabs, null means use the default tab
   *                               background
   */
  public GradientTheme(boolean opaqueTabArea, boolean shadowEnabled, Color borderColor, Color tabAreaBackgroundColor) {
    this.opaqueTabArea = opaqueTabArea;
    this.shadowEnabled = shadowEnabled;
    this.borderColor = borderColor;
    this.tabAreaBackgroundColor = tabAreaBackgroundColor;

    ColorProvider cp = ColorProviderUtil.getColorProvider(tabAreaBackgroundColor,
                                                          UIManagerColorProvider.TABBED_PANE_BACKGROUND);

    normalTabGradientBorder = new GradientTabAreaBorder(cp, new ColorMultiplier(cp, 1.1));
    initTabbedPanelProperties();
    initTitledTabProperties();
  }

  /**
   * Gets the name for this theme
   *
   * @return the name
   */
  public String getName() {
    return "Gradient Theme" + (opaqueTabArea ? " - Opaque Tab Area" : "");
  }

  private void initTabbedPanelProperties() {

    tabbedPanelProperties.getContentPanelProperties().getComponentProperties()
        .setInsets(new Insets(3, 3, 3, 3))
        .setBorder(new OpenContentBorder(borderColor, opaqueTabArea ? 0 : 1));

    tabbedPanelProperties
        .setShadowEnabled(shadowEnabled)
        .setPaintTabAreaShadow(opaqueTabArea)
        .setTabSpacing(opaqueTabArea ? 0 : -1);

    if (opaqueTabArea) {
      if (tabAreaBackgroundColor != null)
        tabbedPanelProperties.getTabAreaProperties().getComponentProperties()
            .setBackgroundColor(tabAreaBackgroundColor);

      tabbedPanelProperties.getTabAreaProperties().getComponentProperties()
          .setBorder(new CompoundBorder(new TabAreaLineBorder(borderColor), normalTabGradientBorder));
    }

    tabbedPanelProperties.getTabAreaComponentsProperties()
        .setStretchEnabled(opaqueTabArea)

        .getComponentProperties()
        .setBorder(new CompoundBorder(new TabAreaLineBorder(opaqueTabArea ? null : borderColor,
                                                            !opaqueTabArea,
                                                            true,
                                                            !opaqueTabArea,
                                                            true),
                                      TAB_AREA_COMPONENTS_GRADIENT_BORDER))
        .setInsets(opaqueTabArea ? new Insets(0, 3, 0, 3) : new Insets(1, 3, 1, 3));
  }

  private void initTitledTabProperties() {
    if (opaqueTabArea)
      titledTabProperties.setHighlightedRaised(0);

    titledTabProperties.getNormalProperties()
        .getComponentProperties()
        .setBorder(opaqueTabArea ?
                   (Border) new TabAreaLineBorder(false, false, true, true) :
                   new CompoundBorder(new TabAreaLineBorder(), normalTabGradientBorder));

    if (opaqueTabArea)
      titledTabProperties.getNormalProperties()
          .getComponentProperties().setBackgroundColor(null);

    if (!opaqueTabArea && tabAreaBackgroundColor != null)
      titledTabProperties.getNormalProperties()
          .getComponentProperties().setBackgroundColor(tabAreaBackgroundColor);

    titledTabProperties.getHighlightedProperties()
        .setIconVisible(true)

        .getComponentProperties()
        .setBorder(new CompoundBorder(opaqueTabArea ?
                                      (Border) new TabAreaLineBorder(false, false, true, true) :
                                      new TabAreaLineBorder(borderColor),
                                      HIGHLIGHTED_TAB_GRADIENT_BORDER));
  }

  public TitledTabProperties getTitledTabProperties() {
    return titledTabProperties;
  }

  public TabbedPanelProperties getTabbedPanelProperties() {
    return tabbedPanelProperties;
  }

  /**
   * Returns the gradient border for the highlighted tab.
   *
   * @return the gradient border for the highlighted tab
   */
  public Border getHighlightedTabGradientBorder() {
    return HIGHLIGHTED_TAB_GRADIENT_BORDER;
  }

  /**
   * Returns the gradient border for the tab area components.
   *
   * @return the gradient border for the tab area components
   */
  public Border getTabAreaComponentsGradientBorder() {
    return TAB_AREA_COMPONENTS_GRADIENT_BORDER;
  }

  /**
   * Returns the gradient border for the normal tab or the tab area if it's opaque.
   *
   * @return the gradient border for the normal tab or the tab area if it's opaque
   */
  public Border getNormalTabGradientBorder() {
    return normalTabGradientBorder;
  }
}
