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


// $Id: TabHighlightBorder.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.ColorProviderUtil;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.util.Direction;


/**
 * TabHighlightBorder draws a 1 pixel wide highlight on the top and left side of the
 * tab. It will not draw highlight on the side towards a TabbedPanel's content area
 * if the border is constructed with open border.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see Tab
 * @see TabbedPanel
 * @see TabbedPanelProperties
 */
public class TabHighlightBorder implements Border, Serializable {
  private static final long serialVersionUID = 1;

  private ColorProvider color;
  private boolean openBorder;

  /**
   * Constructs a TabHighlightBorder that acts as an empty border, i.e. no highlight
   * is drawn but it will report the same insets as if the highlight was drawn
   */
  public TabHighlightBorder() {
    this((Color) null, false);
  }

  /**
   * Constructs a TabHighlightBorder with the given color as highlight color
   *
   * @param color      the highlight color
   * @param openBorder when true, no highlighting is drawn on the side towards a
   *                   TabbedPanel's content area, otherwise false
   */
  public TabHighlightBorder(Color color, boolean openBorder) {
    this(ColorProviderUtil.getColorProvider(color, UIManagerColorProvider.TABBED_PANE_HIGHLIGHT), openBorder);
  }

  /**
   * Constructs a TabHighlightBorder with the given color as highlight color
   *
   * @param colorProvider the highlight color provider
   * @param openBorder    when true, no highlighting is drawn on the side towards a
   *                      TabbedPanel's content area, otherwise false
   */
  public TabHighlightBorder(ColorProvider colorProvider, boolean openBorder) {
    this.color = colorProvider;
    this.openBorder = openBorder;
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    TabbedPanel tabbedPanel = TabbedUtils.getParentTabbedPanel(c);

    if (tabbedPanel != null) {
      Direction d = tabbedPanel.getProperties().getTabAreaOrientation();
      g.setColor(color.getColor(c));

      if (d == Direction.UP) {
        GraphicsUtil.drawOptimizedLine(g, x + 1, y, x + width - 2, y);
        GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - (openBorder ? 1 : 2));
      }
      else if (d == Direction.LEFT) {
        GraphicsUtil.drawOptimizedLine(g, x + 1, y, x + width - (openBorder ? 1 : 2), y);
        GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - 2);
      }
      else if (d == Direction.DOWN) {
        if (!openBorder)
          GraphicsUtil.drawOptimizedLine(g, x + 1, y, x + width - 2, y);
        GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - 2);
      }
      else {
        if (openBorder)
          GraphicsUtil.drawOptimizedLine(g, x, y, x + width - 2, y);
        else {
          GraphicsUtil.drawOptimizedLine(g, x + 1, y, x + width - 2, y);
          GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - 2);
        }
      }
    }
  }

  public Insets getBorderInsets(Component c) {
    return new Insets(1, 1, 0, 0);
  }

  public boolean isBorderOpaque() {
    return false;
  }
}
