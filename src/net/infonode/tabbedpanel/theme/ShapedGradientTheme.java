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


// $Id: ShapedGradientTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel.theme;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Polygon;

import javax.swing.border.Border;

import net.infonode.gui.HighlightPainter;
import net.infonode.gui.InsetsUtil;
import net.infonode.gui.colorprovider.BackgroundPainterColorProvider;
import net.infonode.gui.colorprovider.ColorBlender;
import net.infonode.gui.colorprovider.ColorMultiplier;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.tabbedpanel.border.OpenContentBorder;
import net.infonode.tabbedpanel.internal.SlopedTabLineBorder;
import net.infonode.tabbedpanel.titledtab.TitledTabBorderSizePolicy;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.tabbedpanel.titledtab.TitledTabStateProperties;

/**
 * A theme with tabs with rounded edges, gradient backgrounds and support for
 * slopes on left/right side of tab.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since ITP 1.2.0
 */
public class ShapedGradientTheme extends TabbedPanelTitledTabTheme {
  private static final int CORNER_INSET = 3;

  private ColorProvider highlightColor;
  private ColorProvider lineColor;
  private ColorProvider controlColor;
  private ColorProvider darkControlColor;
  private ColorProvider alternateHighlight;
  private int leftSlopeHeight;
  private int rightSlopeHeight;

  private static class TabBorder extends SlopedTabLineBorder {
    private boolean bottomLeftRounded;
    private boolean isNormal;
    private boolean hasLeftSlope;
    private int raised;
    private int cornerInset;

    TabBorder(ColorProvider lineColor, ColorProvider highlightColor,
              float leftSlope, float rightSlope, int leftHeight, int rightHeight,
              boolean bottomLeftRounded, boolean topLeftRounded, boolean topRightRounded,
              boolean bottomRightRounded, boolean isNormal, boolean highlightBottomLeftRounded, int raised) {
      super(lineColor, highlightColor, false, leftSlope, rightSlope, leftHeight,
            rightHeight, isNormal ? false : bottomLeftRounded, topLeftRounded, topRightRounded, bottomRightRounded);

      this.bottomLeftRounded = bottomLeftRounded;
      this.isNormal = isNormal;
      this.raised = raised;
      hasLeftSlope = leftSlope > 0;

      cornerInset = highlightBottomLeftRounded ? CORNER_INSET : 0;
    }

    protected Polygon createPolygon(Component c, int width, int height) {
      Polygon p = super.createPolygon(c, width, height);
      if (isNormal) {
        int leftX = width / 2;
        boolean first = isFirst(c);
        for (int i = 0; i < p.npoints; i++) {
          if (p.xpoints[i] < leftX)
            p.xpoints[i] = p.xpoints[i] + raised + (first ? 0 : cornerInset);
          else
            p.xpoints[i] = p.xpoints[i] - raised - cornerInset;
        }
      }

      return p;
    }

    protected Insets getShapedBorderInsets(Component c) {
      Insets i = super.getShapedBorderInsets(c);

      Insets addInsets = new Insets(0, 0, 0, 1 + raised);
      //Insets addInsets = new Insets(0, raised, 0, raised);
      if (isNormal && !isFirst(c))
        addInsets.left = addInsets.left + cornerInset;
      if (!isNormal)
        addInsets.right = addInsets.right - cornerInset;

      return InsetsUtil.add(i, addInsets);
    }

    private boolean isFirst(Component c) {
      if (!hasLeftSlope) {
        Tab tab = TabbedUtils.getParentTab(c);
        if (tab != null && tab.getTabbedPanel() != null) {
          return tab.getTabbedPanel().getTabAt(0) == tab;
        }
      }

      return false;
    }

    protected boolean isBottomLeftRounded(Component c) {
      return isFirst(c) ? false : bottomLeftRounded;
    }
  }

  private TabbedPanelProperties tabbedPanelProperties = new TabbedPanelProperties();
  private TitledTabProperties titledTabProperties = new TitledTabProperties();

  /**
   * Creates a default theme with sloped border on the right side of the tab
   * and with colors based on the active look and feel
   */
  public ShapedGradientTheme() {
    this(0f, 0.5f);
  }

  /**
   * Creates a theme with the given slopes on the left and right side of the tab
   * and with colors based on the active look and feel
   *
   * @param leftSlope  leaning of left slope defined as left slope width divided by left slope height
   * @param rightSlope leaning of right slope defined as right slope width divided by right slope height
   */
  public ShapedGradientTheme(float leftSlope, float rightSlope) {
    this(leftSlope,
         rightSlope,
         UIManagerColorProvider.TABBED_PANE_DARK_SHADOW,
         UIManagerColorProvider.TABBED_PANE_HIGHLIGHT);
  }

  /**
   * Creates a theme with the given slopes on the left and right side of the tab
   * and with the given colors
   *
   * @param leftSlope      leaning of left slope defined as left slope width divided
   *                       by left slope height
   * @param rightSlope     leaning of right slope defined as right slope width divided
   *                       by right slope height
   * @param lineColor      color provider for the lines
   * @param highlightColor color provider for the highlighting, null for no highlighting
   */
  public ShapedGradientTheme(float leftSlope, float rightSlope, ColorProvider lineColor, ColorProvider highlightColor) {
    this(leftSlope, rightSlope, 25, lineColor, highlightColor);
  }

  /**
   * Creates a theme with the given slopes on the left and right side of
   * the tab and with the given colors
   *
   * @param leftSlope      leaning of left slope defined as left slope width divided
   *                       by left slope height
   * @param rightSlope     leaning of right slope defined as right slope width divided
   *                       by right slope height
   * @param slopeHeight    slope height in pixels, used when estimating slope width
   * @param lineColor      color provider for the lines
   * @param highlightColor color provider for the highlighting, null for no highlighting
   */
  public ShapedGradientTheme(float leftSlope,
                             float rightSlope,
                             int slopeHeight,
                             ColorProvider lineColor,
                             ColorProvider highlightColor) {
    this.leftSlopeHeight = slopeHeight;
    this.rightSlopeHeight = slopeHeight;
    this.highlightColor = highlightColor;
    this.lineColor = lineColor;
    this.controlColor = UIManagerColorProvider.CONTROL_COLOR;
    darkControlColor = UIManagerColorProvider.TABBED_PANE_BACKGROUND;
    alternateHighlight = highlightColor != null ?
                         new ColorBlender(highlightColor, controlColor, 0.3f) :
                         (ColorProvider) new ColorMultiplier(controlColor, 1.2);

    GradientComponentPainter blendedComponentPainter = new GradientComponentPainter(alternateHighlight,
                                                                                    alternateHighlight,
                                                                                    controlColor,
                                                                                    controlColor);

    int leftSlopeWidth = (int) (leftSlope * leftSlopeHeight);
    int rightSlopeWidth = (int) (rightSlope * rightSlopeHeight);
    int highlightedRaised = 2;

    boolean bottomLeftRounded = true;
    boolean topLeftRounded = true;
    boolean topRightRounded = true;
    boolean bottomRightRounded = true;

    titledTabProperties.setHighlightedRaised(highlightedRaised).setBorderSizePolicy(
        TitledTabBorderSizePolicy.EQUAL_SIZE);

    TitledTabStateProperties normalState = titledTabProperties.getNormalProperties();
    TitledTabStateProperties highlightState = titledTabProperties.getHighlightedProperties();
    TitledTabStateProperties disabledState = titledTabProperties.getDisabledProperties();

    Border normalBorder = new TabBorder(lineColor, null, leftSlope, rightSlope, leftSlopeHeight, rightSlopeHeight, false, topLeftRounded, topRightRounded,
                                        false, true, true, highlightedRaised);
    Border highlightBorder = new TabBorder(lineColor,
                                           highlightColor,
                                           leftSlope,
                                           rightSlope,
                                           leftSlopeHeight,
                                           rightSlopeHeight,
                                           bottomLeftRounded,
                                           topLeftRounded,
                                           topRightRounded,
                                           bottomRightRounded,
                                           false,
                                           true,
                                           highlightedRaised);

    normalState.getComponentProperties().setBorder(normalBorder).setInsets(new Insets(0, 0, 0, 0));
    highlightState.getComponentProperties().setBorder(highlightBorder);

    ColorProvider darkControlColor1 = new ColorMultiplier(darkControlColor, 1.1);
    ColorProvider darkControlColor2 = new ColorMultiplier(darkControlColor, 0.92);
    GradientComponentPainter normalComponentPainter = new GradientComponentPainter(darkControlColor1,
                                                                                   darkControlColor1,
                                                                                   darkControlColor2,
                                                                                   darkControlColor2);
    normalState.getShapedPanelProperties().setOpaque(false).setComponentPainter(normalComponentPainter);
    disabledState.getShapedPanelProperties().setComponentPainter(normalComponentPainter);

    if (highlightColor == null)
      highlightState.getShapedPanelProperties().setComponentPainter(blendedComponentPainter);
    else
      highlightState.getShapedPanelProperties().setComponentPainter(
          new GradientComponentPainter(highlightColor, highlightColor, controlColor, controlColor));

    Insets insets = normalBorder.getBorderInsets(null);
    int tabSpacing = 1 + insets.left + insets.right - (topLeftRounded ? CORNER_INSET : 0) - (topRightRounded ?
                                                                                             CORNER_INSET : 0) -
                     (int) (0.2 * (leftSlopeWidth + rightSlopeWidth));
    tabbedPanelProperties.setTabSpacing(-tabSpacing).setShadowEnabled(false);

    tabbedPanelProperties.getTabAreaComponentsProperties().getComponentProperties().setBorder(new SlopedTabLineBorder(
        lineColor,
        highlightColor,
        false,
        0f,
        0f,
        0,
        0,
        false,
        topLeftRounded,
        topRightRounded,
        false)).setInsets(new Insets(0, 0, 0, 0));

    tabbedPanelProperties.getTabAreaComponentsProperties().getShapedPanelProperties().setComponentPainter(
        blendedComponentPainter);

    tabbedPanelProperties.getTabAreaProperties().getShapedPanelProperties().setOpaque(false);

    tabbedPanelProperties.getContentPanelProperties().getComponentProperties()
        .setBorder(new OpenContentBorder(lineColor, lineColor,
                                         highlightColor == null ? null : new ColorBlender(highlightColor,
                                                                                          BackgroundPainterColorProvider.INSTANCE,
                                                                                          HighlightPainter.getBlendFactor(
                                                                                              1, 0)),
                                         1));
  }

  /**
   * Gets the theme name
   *
   * @return name for this theme
   */
  public String getName() {
    return "Shaped Gradient Theme";
  }

  /**
   * Gets the TabbedPanelProperties for this theme
   *
   * @return the TabbedPanelProperties
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return tabbedPanelProperties;
  }

  /**
   * Gets the TitledTabProperties for this theme
   *
   * @return the TitledTabProperties
   */
  public TitledTabProperties getTitledTabProperties() {
    return titledTabProperties;
  }

  /**
   * Gets the line color provider
   *
   * @return the line color provider
   */
  public ColorProvider getLineColor() {
    return lineColor;
  }

  /**
   * Gets the highlight color provider
   *
   * @return the highlight color provider, null if no highlight
   */
  public ColorProvider getHighlightColor() {
    return highlightColor;
  }

  /**
   * Gets the alternate highlight color provider used for tab area
   * components gradient background and highlighted tab background
   * (when no highlight color is specified)
   *
   * @return the alternate highlight color provider
   */
  public ColorProvider getAlternateHighlightColor() {
    return alternateHighlight;
  }

  /**
   * Gets the control background color
   *
   * @return the control background color provider
   */
  public ColorProvider getControlColor() {
    return controlColor;
  }

  /**
   * Gets the dark control background color used for gradient for
   * normal tab and disabled tab
   *
   * @return the dark control background color provider
   */
  public ColorProvider getDarkControlColor() {
    return darkControlColor;
  }

  /**
   * Creates a tab border
   *
   * @param lineColor                  line color provider
   * @param highlightColor             highlight color provider, null for no highlight
   * @param leftSlope                  left slope
   * @param rightSlope                 right slope
   * @param bottomLeftRounded          true if bottom left should be rounded
   * @param topLeftRounded             true if top left should be rounded
   * @param topRightRounded            true if top right should be rounded
   * @param bottomRightRounded         true if bottom right should be rounded
   * @param isNormal                   true if this is a normal rendered border
   * @param highlightBottomLeftRounded true if highlight has bottom left rounded
   * @param raised                     raised
   * @return the created border
   */
  public Border createTabBorder(ColorProvider lineColor,
                                ColorProvider highlightColor,
                                float leftSlope,
                                float rightSlope,
                                boolean bottomLeftRounded,
                                boolean topLeftRounded,
                                boolean topRightRounded,
                                boolean bottomRightRounded,
                                boolean isNormal,
                                boolean highlightBottomLeftRounded,
                                int raised) {
    return new TabBorder(lineColor,
                         highlightColor,
                         leftSlope,
                         rightSlope,
                         25,
                         25,
                         bottomLeftRounded,
                         topLeftRounded,
                         topRightRounded,
                         bottomRightRounded,
                         isNormal,
                         highlightBottomLeftRounded,
                         raised);
  }
}