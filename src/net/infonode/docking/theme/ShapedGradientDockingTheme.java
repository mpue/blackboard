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


// $Id: ShapedGradientDockingTheme.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.docking.theme;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Polygon;

import javax.swing.border.Border;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.gui.colorprovider.ColorBlender;
import net.infonode.gui.colorprovider.ColorMultiplier;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.gui.componentpainter.SolidColorComponentPainter;
import net.infonode.gui.shaped.border.RoundedCornerBorder;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.tabbedpanel.theme.ShapedGradientTheme;
import net.infonode.tabbedpanel.titledtab.TitledTabBorderSizePolicy;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * A theme with tabs with rounded edges, gradient backgrounds and support for
 * slopes on left/right side of tab.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.2.0
 */
public class ShapedGradientDockingTheme extends DockingWindowsTheme {
  private RootWindowProperties rootWindowProperties = new RootWindowProperties();
  private String name;

  /**
   * Creates a default theme with sloped border on the right side of the tab
   * (excluding tabs on window bars) and with colors based on the active look
   * and feel
   */
  public ShapedGradientDockingTheme() {
    this(0f, 0.5f);
  }

  /**
   * Creates a theme with the given slopes on the left and right side of the tab
   * (excluding tabs on window bars) and with colors based on the active look and feel
   *
   * @param leftSlope  leaning of left slope defined as left slope width divided by left slope height
   * @param rightSlope leaning of right slope defined as right slope width divided by right slope height
   */
  public ShapedGradientDockingTheme(float leftSlope, float rightSlope) {
    this(leftSlope,
         rightSlope,
         UIManagerColorProvider.TABBED_PANE_DARK_SHADOW,
         UIManagerColorProvider.TABBED_PANE_HIGHLIGHT,
         true);
  }

  /**
   * Creates a theme with the given slopes on the left and right side of the tab
   * (excluding tabs on window bars) and with the given colors
   *
   * @param leftSlope               leaning of left slope defined as left slope width divided
   *                                by left slope height
   * @param rightSlope              leaning of right slope defined as right slope width divided
   *                                by right slope height
   * @param lineColor               color provider for the lines
   * @param highlightColor          color provider for the highlighting, null for no highlighting
   * @param focusHighlighterEnabled if true the currently focused tab is highlighted
   */
  public ShapedGradientDockingTheme(float leftSlope, float rightSlope, ColorProvider lineColor,
                                    ColorProvider highlightColor, boolean focusHighlighterEnabled) {
    this(leftSlope, rightSlope, 25, lineColor, highlightColor, focusHighlighterEnabled);
  }

  /**
   * Creates a theme with the given slopes on the left and right side of the tab
   * (excluding tabs on window bars) and with the given colors
   *
   * @param leftSlope               leaning of left slope defined as left slope width divided
   *                                by left slope height
   * @param rightSlope              leaning of right slope defined as right slope width divided
   *                                by right slope height
   * @param slopeHeight             slope height in pixels, used when estimating slope width
   * @param lineColor               color provider for the lines
   * @param highlightColor          color provider for the highlighting, null for no highlighting
   * @param focusHighlighterEnabled if true the currently focused tab is highlighted
   */
  public ShapedGradientDockingTheme(float leftSlope, float rightSlope, int slopeHeight, ColorProvider lineColor,
                                    ColorProvider highlightColor, boolean focusHighlighterEnabled) {
    final ShapedGradientTheme theme = new ShapedGradientTheme(leftSlope,
                                                              rightSlope,
                                                              slopeHeight,
                                                              lineColor,
                                                              highlightColor);
    name = theme.getName();

    int cornerType = 3;

    TabbedPanelProperties tabbedPanelProperties = theme.getTabbedPanelProperties();
    TitledTabProperties titledTabProperties = theme.getTitledTabProperties();

    // Tab window
    rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().addSuperObject(tabbedPanelProperties);
    rootWindowProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().addSuperObject(
        titledTabProperties);

    if (focusHighlighterEnabled) {
      rootWindowProperties.getTabWindowProperties().getTabProperties().getFocusedProperties().
          getShapedPanelProperties().setComponentPainter(rootWindowProperties.getTabWindowProperties().
          getTabProperties().getTitledTabProperties()
          .getHighlightedProperties()
          .getShapedPanelProperties()
          .getComponentPainter());
      ColorProvider topColor = new ColorMultiplier(theme.getControlColor(), 0.85f);
      rootWindowProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().
          getHighlightedProperties().getShapedPanelProperties().setComponentPainter(new GradientComponentPainter(
          topColor,
          theme.getControlColor(),
          theme.getControlColor(),
          theme.getControlColor()));
      rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().getTabAreaComponentsProperties().
          getShapedPanelProperties().setComponentPainter(new GradientComponentPainter(
          new ColorMultiplier(theme.getControlColor(), 1.1f),
          theme.getControlColor(),
          theme.getControlColor(),
          topColor));
    }

    Border highlightBorder = theme.createTabBorder(theme.getLineColor(),
                                                   theme.getHighlightColor(),
                                                   0f,
                                                   0f,
                                                   true,
                                                   true,
                                                   true,
                                                   true,
                                                   false,
                                                   true,
                                                   0);

    final int inset = 4;
    final int extraRaised = 1;

    Border normalBorder = new RoundedCornerBorder(theme.getLineColor(),
                                                  null,
                                                  cornerType,
                                                  cornerType,
                                                  cornerType,
                                                  cornerType,
                                                  true,
                                                  true,
                                                  true,
                                                  true) {
      private Border calculatedInsetsBorder = theme.createTabBorder(theme.getLineColor(),
                                                                    null,
                                                                    0f,
                                                                    0f,
                                                                    false,
                                                                    true,
                                                                    true,
                                                                    false,
                                                                    true,
                                                                    true,
                                                                    0);

      protected Polygon createPolygon(Component c, int width, int height) {
        Polygon p = super.createPolygon(c, width, height);
        for (int i = 0; i < p.npoints; i++)
          if (p.xpoints[i] < width / 2)
            p.xpoints[i] = p.xpoints[i] + (isFirst(c) ? 0 : inset) + extraRaised;
          else
            p.xpoints[i] = p.xpoints[i] - inset - extraRaised;

        return p;
      }

      public Insets getBorderInsets(Component c) {
        return calculatedInsetsBorder.getBorderInsets(c);
      }

      private boolean isFirst(Component c) {
        Tab tab = TabbedUtils.getParentTab(c);
        if (tab != null && tab.getTabbedPanel() != null)
          return tab.getTabbedPanel().getTabAt(0) == tab;

        return false;
      }
    };

    //Insets insets = new Insets(1, 2 + extraRaised, 1, 2 + extraRaised);
    TitledTabProperties tabProperties = rootWindowProperties.getWindowBarProperties().getTabWindowProperties()
        .getTabProperties()
        .getTitledTabProperties();
    tabProperties.getNormalProperties().getComponentProperties().setBorder(normalBorder).setInsets(
        new Insets(1, 0, 1, 2 + extraRaised));
    tabProperties.getHighlightedProperties().getComponentProperties().setBorder(highlightBorder);
    tabProperties.setHighlightedRaised(0).setBorderSizePolicy(TitledTabBorderSizePolicy.EQUAL_SIZE);

    rootWindowProperties.getWindowBarProperties().getTabWindowProperties().getTabbedPanelProperties().setTabSpacing(
        -inset * 2 + 2 - 2 * extraRaised)
        .getTabAreaComponentsProperties().getComponentProperties()
        .setBorder(new RoundedCornerBorder(theme.getLineColor(),
                                           theme.getHighlightColor(),
                                           cornerType,
                                           cornerType,
                                           cornerType,
                                           cornerType,
                                           true,
                                           true,
                                           true,
                                           true))
        .setInsets(new Insets(0, 3, 0, 3));

    rootWindowProperties
        .setDragRectangleBorderWidth(3)

        .getWindowBarProperties().getComponentProperties()
        .setInsets(new Insets(2, 0, 2, 0));

    rootWindowProperties.getWindowAreaProperties()
        .setBorder(null)
        .setInsets(new Insets(2, 2, 2, 2));

    rootWindowProperties.getComponentProperties().setBackgroundColor(null);
    rootWindowProperties.getShapedPanelProperties().setComponentPainter(new SolidColorComponentPainter(new ColorBlender(
        UIManagerColorProvider.TABBED_PANE_BACKGROUND,
        UIManagerColorProvider.CONTROL_COLOR,
        0.5f)));

    rootWindowProperties.getWindowAreaShapedPanelProperties().setComponentPainter(
        new SolidColorComponentPainter(UIManagerColorProvider.CONTROL_COLOR));

    Insets insets = rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties()
        .getComponentProperties()
        .getInsets();
    if (highlightColor == null)
      rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties()
          .getComponentProperties()
          .setInsets(new Insets(insets.top, insets.top, insets.top, insets.top));
  }

  /**
   * Gets the theme name
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the theme RootWindowProperties
   *
   * @return the RootWindowProperties
   */
  public RootWindowProperties getRootWindowProperties() {
    return rootWindowProperties;
  }
}