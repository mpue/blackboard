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


// $Id: ShadowPainter.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.internal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.infonode.gui.ComponentUtil;
import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.UIManagerUtil;
import net.infonode.util.ColorUtil;
import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since ITP 1.1.0
 */
public class ShadowPainter {
  private Color panelBackgroundColor;
  private Color tabBackgroundColor;
  private Component component;
  private JComponent componentsPanel;
  private JComponent highlightedTab;
  private JComponent contentPanel;
  private JComponent tabAreaComponentsPanel;
  private JComponent tabAreaContainer;
  private JComponent tabBox;
  private Direction tabOrientation;
  private boolean paintTabAreaShadow;
  private int shadowSize;
  private int shadowBlendSize;
  private Color shadowColor;
  private float shadowStrength;
  private boolean highlightedTabIsLast;

  public ShadowPainter(Component component, JComponent componentsPanel, JComponent highlightedTab,
                       JComponent contentPanel, JComponent tabAreaComponentsPanel, JComponent tabAreaContainer,
                       JComponent tabBox, Direction tabOrientation, boolean paintTabAreaShadow,
                       int shadowSize, int shadowBlendSize, Color shadowColor,
                       float shadowStrength, boolean highlightedTabIsLast) {
    this.component = component;
    this.componentsPanel = componentsPanel;
    this.highlightedTab = highlightedTab == null ?
                          null : !highlightedTab.isVisible() || !tabAreaContainer.isVisible() ? null : highlightedTab;
    this.contentPanel = contentPanel;
    this.tabAreaComponentsPanel = tabAreaComponentsPanel;
    this.tabAreaContainer = tabAreaContainer;
    this.tabBox = tabBox;
    this.tabOrientation = tabOrientation;
    this.paintTabAreaShadow = paintTabAreaShadow && tabAreaContainer.isVisible();
    this.shadowSize = shadowSize;
    this.shadowBlendSize = shadowBlendSize;
    this.shadowColor = shadowColor;
    this.shadowStrength = shadowStrength;
    this.highlightedTabIsLast = highlightedTabIsLast;
  }

  public void paint(Graphics g) {
    panelBackgroundColor = ComponentUtil.getBackgroundColor(component);
    panelBackgroundColor = panelBackgroundColor == null ?
                           UIManagerUtil.getColor("Panel.background", "control") : panelBackgroundColor;

    if (paintTabAreaShadow) {
      Rectangle bounds = SwingUtilities.calculateInnerArea(componentsPanel, new Rectangle());

      // Right shadow
      drawBottomRightEdgeShadow(g, bounds.y, bounds.x + bounds.width, bounds.height, true, panelBackgroundColor);

      // Bottom shadow
      drawBottomRightEdgeShadow(g, bounds.x, bounds.y + bounds.height, bounds.width, false, panelBackgroundColor);

      // Bottom right corner
      drawRightCornerShadow(g, bounds.x + bounds.width, bounds.y + bounds.height, false, panelBackgroundColor);
    }
    else {
      tabBackgroundColor = highlightedTab == null ?
                           panelBackgroundColor :
                           ComponentUtil.getBackgroundColor(highlightedTab.getParent());

      tabBackgroundColor = tabBackgroundColor == null ? panelBackgroundColor : tabBackgroundColor;

      Rectangle contentPanelBounds = contentPanel.getBounds();
      int len = 0;

      if (highlightedTab != null)
        len = paintHighlightedTabShadow(g, tabOrientation, contentPanelBounds);

      if (tabAreaComponentsPanel.isVisible()) {
        len = tabOrientation.isHorizontal() ?
              (tabAreaContainer.getInsets().bottom == 0 ? tabAreaComponentsPanel.getWidth() : 0) :
              (tabAreaContainer.getInsets().right == 0 ? tabAreaComponentsPanel.getHeight() : 0);
      }

      if (!tabAreaContainer.isVisible())
        len = 0;

      if (tabOrientation != Direction.RIGHT || highlightedTab == null)
        drawBottomRightEdgeShadow(g,
                                  contentPanelBounds.y - (tabOrientation == Direction.UP ? len : 0),
                                  contentPanelBounds.x + contentPanelBounds.width,
                                  contentPanelBounds.height + (!tabOrientation.isHorizontal() ? len : 0),
                                  true,
                                  highlightedTab == null ? null : panelBackgroundColor);

      if (tabOrientation != Direction.DOWN || highlightedTab == null)
        drawBottomRightEdgeShadow(g,
                                  contentPanelBounds.x - (tabOrientation == Direction.LEFT ? len : 0),
                                  contentPanelBounds.y + contentPanelBounds.height,
                                  contentPanelBounds.width + (tabOrientation.isHorizontal() ? len : 0),
                                  false,
                                  highlightedTab == null ? null : panelBackgroundColor);

      drawRightCornerShadow(g,
                            contentPanelBounds.x + contentPanelBounds.width + (tabOrientation == Direction.RIGHT ?
                                                                               len : 0),
                            contentPanelBounds.y + contentPanelBounds.height + (tabOrientation == Direction.DOWN ?
                                                                                len : 0),
                            false,
                            panelBackgroundColor);
    }

  }

  private int paintHighlightedTabShadow(Graphics g, Direction tabOrientation, Rectangle contentPanelBounds) {
    Point p = SwingUtilities.convertPoint(highlightedTab.getParent(), highlightedTab.getLocation(), component);

//    JComponent tabBox = draggableComponentBox;
    Dimension tabsSize = tabBox.getSize();
    Rectangle bounds = tabAreaComponentsPanel.isVisible() ?
                       SwingUtilities.convertRectangle(tabAreaComponentsPanel.getParent(),
                                                       tabAreaComponentsPanel.getBounds(),
                                                       component) :
                       new Rectangle(contentPanelBounds.x + contentPanelBounds.width,
                                     contentPanelBounds.y + contentPanelBounds.height,
                                     0,
                                     0);
    Point tabsPos = SwingUtilities.convertPoint(tabBox, 0, 0, component);

    // Set tab clip
    int width = (tabOrientation.isHorizontal() ? 0 : tabsPos.x) + tabsSize.width;
    int height = (tabOrientation.isHorizontal() ? tabsPos.y : 0) + tabsSize.height;

    if (tabOrientation == Direction.DOWN)
      drawBottomRightTabShadow(g,
                               contentPanelBounds.x,
                               contentPanelBounds.y + contentPanelBounds.height,
                               contentPanelBounds.width,
                               p.x,
                               highlightedTab.getWidth(),
                               highlightedTab.getHeight(),
                               bounds.x,
                               bounds.width,
                               bounds.height,
                               false,
                               highlightedTabIsLast);
    else if (tabOrientation == Direction.RIGHT)
      drawBottomRightTabShadow(g,
                               contentPanelBounds.y,
                               contentPanelBounds.x + contentPanelBounds.width,
                               contentPanelBounds.height,
                               p.y,
                               highlightedTab.getHeight(),
                               highlightedTab.getWidth(),
                               bounds.y,
                               bounds.height,
                               bounds.width,
                               true,
                               highlightedTabIsLast);
    else if (tabOrientation == Direction.UP) {
      drawTopLeftTabShadow(g,
                           p.x + highlightedTab.getWidth(),
                           p.y, highlightedTab.getHeight(),
                           bounds,
                           contentPanelBounds.width,
                           false,
                           highlightedTabIsLast);
    }
    else
      drawTopLeftTabShadow(g,
                           p.y + highlightedTab.getHeight(),
                           p.x,
                           highlightedTab.getWidth(),
                           flipRectangle(bounds),
                           contentPanelBounds.height,
                           true,
                           highlightedTabIsLast);

    if (highlightedTabIsLast) {
      return tabOrientation.isHorizontal() ? (p.y + highlightedTab.getHeight() >= contentPanelBounds.height &&
                                              contentPanelBounds.height == height ? highlightedTab.getWidth() : 0) :
             (p.x + highlightedTab.getWidth() >= contentPanelBounds.width &&
              contentPanelBounds.width == width ? highlightedTab.getHeight() : 0);
    }
    else
      return 0;
  }

  private static Rectangle flipRectangle(Rectangle bounds) {
    return new Rectangle(bounds.y, bounds.x, bounds.height, bounds.width);
  }

  private static Rectangle createRectangle(int x, int y, int width, int height, boolean flip) {
    return flip ? new Rectangle(y, x, height, width) : new Rectangle(x, y, width, height);
  }

  private void drawTopLeftTabShadow(Graphics g, int x, int y, int height, Rectangle componentsBounds,
                                    int totalWidth, boolean flip, boolean isLast) {
    boolean connected = x + shadowSize > componentsBounds.x;

    if (!connected || y + shadowSize + shadowBlendSize <= componentsBounds.y) {
      drawLeftCornerShadow(g, y, Math.min(x, componentsBounds.x), !flip, isLast ? tabBackgroundColor : null);
      drawEdgeShadow(g,
                     y,
                     connected ? componentsBounds.y : y + height,
                     Math.min(x, componentsBounds.x),
                     false,
                     !flip,
                     isLast ? tabBackgroundColor : null);
    }

    int endX = componentsBounds.x + componentsBounds.width;

    if (endX < totalWidth) {
      drawLeftCornerShadow(g, componentsBounds.y, endX, !flip, tabBackgroundColor);
      drawEdgeShadow(g,
                     componentsBounds.y,
                     componentsBounds.y + componentsBounds.height,
                     endX,
                     false,
                     !flip,
                     tabBackgroundColor);
    }
  }

  private void drawBottomRightTabShadow(Graphics g,
                                        int x,
                                        int y,
                                        int width,
                                        int tabX,
                                        int tabWidth,
                                        int tabHeight,
                                        int componentsX,
                                        int componentsWidth,
                                        int componentsHeight,
                                        boolean flip,
                                        boolean isLast) {
    Shape oldClipRect = g.getClip();

    {
      Rectangle clipRect = createRectangle(x, y, Math.min(tabX, componentsX) - x, 1000000, flip);
      g.clipRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
    }

    drawLeftCornerShadow(g, x, y, flip, null);
    drawEdgeShadow(g, x, tabX, y, false, flip, null);

    boolean connected = tabX < componentsX && tabX + tabWidth >= componentsX;
    g.setClip(oldClipRect);

    if (!connected) {
      Rectangle clipRect = createRectangle(x, y, componentsX - x, 1000000, flip);
      g.clipRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
    }

    if (tabX < componentsX) {
      int endX = connected && tabHeight == componentsHeight ? componentsX + componentsWidth :
                 Math.min(componentsX, tabX + tabWidth);

      if (tabX + shadowSize < endX)
        drawLeftCornerShadow(g, tabX, y + tabHeight, flip, panelBackgroundColor);

      drawEdgeShadow(g, tabX, endX, y + tabHeight, false, flip, panelBackgroundColor);

      if (endX < x + width && (!connected || componentsHeight < tabHeight)) {
        drawRightCornerShadow(g, endX, y + tabHeight, flip, panelBackgroundColor);
        drawEdgeShadow(g,
                       y + (connected ? componentsHeight : 0),
                       y + tabHeight,
                       endX,
                       true,
                       !flip,
                       isLast ? tabBackgroundColor : null);
      }
    }

    if (!connected) {
      drawEdgeShadow(g, tabX + tabWidth, componentsX, y, true, flip, isLast ? tabBackgroundColor : null);
      g.setClip(oldClipRect);
    }

    if (componentsHeight > 0 && (!connected || tabHeight != componentsHeight)) {
      if (!connected || componentsHeight > tabHeight) {
        drawLeftCornerShadow(g, componentsX, y + componentsHeight, flip, panelBackgroundColor);
        drawEdgeShadow(g,
                       componentsX,
                       componentsX + componentsWidth,
                       y + componentsHeight,
                       false,
                       flip,
                       panelBackgroundColor);
      }
      else
        drawEdgeShadow(g,
                       componentsX,
                       componentsX + componentsWidth,
                       y + componentsHeight,
                       true,
                       flip,
                       panelBackgroundColor);
    }

    if (componentsX + componentsWidth < x + width) {
      drawRightCornerShadow(g, componentsX + componentsWidth, y + componentsHeight, flip, panelBackgroundColor);
      drawEdgeShadow(g, y, y + componentsHeight, componentsX + componentsWidth, true, !flip, panelBackgroundColor);
      drawEdgeShadow(g, componentsX + componentsWidth, x + width, y, true, flip, panelBackgroundColor);
    }
  }

  private void drawBottomRightEdgeShadow(Graphics g, int x, int y, int width, boolean flip, Color backgroundColor) {
    drawLeftCornerShadow(g, x, y, flip, backgroundColor);
    drawEdgeShadow(g, x, x + width, y, false, flip, backgroundColor);
  }

  private void drawLeftCornerShadow(Graphics g, int x, int y, boolean upper, Color backgroundColor) {
    for (int i = 0; i < shadowBlendSize; i++) {
      g.setColor(getShadowBlendColor(i, backgroundColor));
      int x1 = x + shadowSize + shadowBlendSize - 1 - i;
      int y1 = y + shadowSize - shadowBlendSize;

      if (y1 > y)
        drawLine(g, x1, y, x1, y1 - 1, upper);

      drawLine(g, x1, y1, x + shadowSize + shadowBlendSize - 1, y + shadowSize - shadowBlendSize + i, upper);
    }
  }

  private void drawRightCornerShadow(Graphics g, int x, int y, boolean flip, Color backgroundColor) {
    g.setColor(getShadowColor(backgroundColor));

    for (int i = 0; i < shadowSize - shadowBlendSize; i++) {
      drawLine(g, x + i, y, x + i, y + shadowSize - shadowBlendSize, flip);
    }

    for (int i = 0; i < shadowBlendSize; i++) {
      g.setColor(getShadowBlendColor(i, backgroundColor));
      int d = shadowSize - shadowBlendSize + i;
      drawLine(g, x + d, y, x + d, y + shadowSize - shadowBlendSize, flip);
      drawLine(g, x, y + d, x + shadowSize - shadowBlendSize, y + d, flip);
      drawLine(g, x + d, y + shadowSize - shadowBlendSize, x + shadowSize - shadowBlendSize, y + d, flip);
    }
  }

  private void drawEdgeShadow(Graphics g, int startX, int endX, int y, boolean cornerStart, boolean vertical,
                              Color backgroundColor) {
    if (startX + (cornerStart ? 0 : shadowSize + shadowBlendSize) >= endX)
      return;

    g.setColor(getShadowColor(backgroundColor));

    for (int i = 0; i < shadowSize - shadowBlendSize; i++) {
      drawLine(g,
               startX + (cornerStart ? i + (vertical ? 1 : 0) : shadowSize + shadowBlendSize),
               y + i,
               endX - 1,
               y + i,
               vertical);
    }

    for (int i = 0; i < shadowBlendSize; i++) {
      g.setColor(getShadowBlendColor(i, backgroundColor));
      int d = shadowSize - shadowBlendSize + i;
      drawLine(g,
               startX + (cornerStart ? d + (vertical ? 1 : 0) : shadowSize + shadowBlendSize),
               y + d,
               endX - 1,
               y + d,
               vertical);
    }
  }

/*  private void drawShadowLine(Graphics g, int startX, int endX, int y, boolean vertical, Color backgroundColor) {
    if (startX >= endX)
      return;

    g.setColor(getShadowColor(backgroundColor));

    for (int i = 0; i < shadowSize - shadowBlendSize; i++) {
      drawLine(g, startX, y + i, endX - 1, y + i, vertical);
    }

    for (int i = 0; i < shadowBlendSize; i++) {
      g.setColor(getShadowBlendColor(i, backgroundColor));
      int d = shadowSize - shadowBlendSize + i;
      drawLine(g, startX, y + d, endX - 1, y + d, vertical);
    }
  }
*/
  private static void drawLine(Graphics g, int x1, int y1, int x2, int y2, boolean flip) {
    if (flip)
      GraphicsUtil.drawOptimizedLine(g, y1, x1, y2, x2);
    else
      GraphicsUtil.drawOptimizedLine(g, x1, y1, x2, y2);
  }


  private Color getShadowBlendColor(int offset, Color backgroundColor) {
    return backgroundColor == null ?
           new Color(shadowColor.getRed(),
                     shadowColor.getGreen(),
                     shadowColor.getBlue(),
                     (int) (255F * shadowStrength * (shadowBlendSize - offset) / (shadowBlendSize + 1))) :
           ColorUtil.blend(backgroundColor,
                           shadowColor,
                           shadowStrength * (shadowBlendSize - offset) / (shadowBlendSize + 1));
  }

  private Color getShadowColor(Color backgroundColor) {
    return backgroundColor == null ?
           new Color(shadowColor.getRed(),
                     shadowColor.getGreen(),
                     shadowColor.getBlue(),
                     (int) (255F * shadowStrength)) :
           ColorUtil.blend(backgroundColor,
                           shadowColor,
                           shadowStrength);
  }
}
