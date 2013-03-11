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


// $Id: TwoColoredLineBorder.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.internal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Polygon;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.shaped.border.RoundedCornerBorder;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.util.Direction;

/**
 * TwoColoredLineBorder draws a 1 pixel wide line. The border can have
 * one color for the top and left line and another color for the bottom
 * and right line.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TitledTab
 * @see TabbedPanel
 * @since ITP 1.2.0
 */
public class TwoColoredLineBorder extends RoundedCornerBorder {
  private static final long serialVersionUID = 1;

  private ColorProvider topLeftColor;
  private ColorProvider bottomRightColor;
  private boolean roundCorners;
  private boolean open;

  /**
   * Constructs a TwoColoredLineBorder
   *
   * @param topLeftColor     the colorprovider for the top and left lines
   * @param bottomRightColor the colorprovider for the bottom and right lines
   * @param roundCorners     true for round corners on the side facing away from the tab area
   * @param open             true for no border on the side towards the tab area
   */
  public TwoColoredLineBorder(ColorProvider topLeftColor,
                              ColorProvider bottomRightColor,
                              boolean roundCorners,
                              boolean open) {
    super(topLeftColor,
          null,
          roundCorners ? 2 : 0,
          roundCorners ? 2 : 0,
          roundCorners && !open ? 2 : 0,
          roundCorners && !open ? 2 : 0,
          true,
          true,
          !open,
          true);
    this.topLeftColor = topLeftColor;
    this.bottomRightColor = bottomRightColor;
    this.roundCorners = roundCorners;
    this.open = open;
  }

  protected void paintPolygon(Component c, Graphics2D g, Polygon polygon, int width, int height) {
    TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
    if (tp != null) {
      Direction d = tp.getProperties().getTabAreaOrientation();

      int i = 0;

      Color c1 = topLeftColor.getColor();
      Color c2 = bottomRightColor.getColor();

      if (d == Direction.UP) {
        g.setColor(c1);
        while (i < (roundCorners ? 3 : 1)) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }

        g.setColor(c2);
        while (i < polygon.npoints - 1) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }

        g.setColor(c1);
        GraphicsUtil.drawOptimizedLine(g,
                                       polygon.xpoints[i],
                                       polygon.ypoints[i],
                                       polygon.xpoints[0],
                                       polygon.ypoints[0]);

      }
      else if (d == Direction.RIGHT) {
        g.setColor(c2);
        while (i < polygon.npoints - (open ? 2 : roundCorners ? 3 : 2)) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }

        g.setColor(c1);
        for (int k = i - 1; k < polygon.npoints - 2; k++) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }
        GraphicsUtil.drawOptimizedLine(g,
                                       polygon.xpoints[i],
                                       polygon.ypoints[i],
                                       polygon.xpoints[0],
                                       polygon.ypoints[0]);

      }
      else if (d == Direction.DOWN) {
        g.setColor(c2);
        while (i < (roundCorners ? 5 : 2)) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }

        g.setColor(c1);
        while (i < polygon.npoints - 1) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }

        GraphicsUtil.drawOptimizedLine(g,
                                       polygon.xpoints[i],
                                       polygon.ypoints[i],
                                       polygon.xpoints[0],
                                       polygon.ypoints[0]);
      }
      else {
        g.setColor(c1);
        while (i < (roundCorners ? 3 : 1)) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }

        g.setColor(c2);
        while (i < polygon.npoints - 1) {
          GraphicsUtil.drawOptimizedLine(g,
                                         polygon.xpoints[i],
                                         polygon.ypoints[i],
                                         polygon.xpoints[i + 1],
                                         polygon.ypoints[i + 1]);
          i++;
        }

        g.setColor(c1);

        GraphicsUtil.drawOptimizedLine(g,
                                       polygon.xpoints[i],
                                       polygon.ypoints[i],
                                       polygon.xpoints[0],
                                       polygon.ypoints[0]);
      }
    }
  }
}
