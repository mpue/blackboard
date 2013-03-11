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


// $Id: HighlightPainter.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Color;
import java.awt.Graphics;

import net.infonode.util.ColorUtil;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class HighlightPainter {
  private HighlightPainter() {
  }

  public static void drawLine(Graphics g, int x1, int y1, int x2, int y2, boolean clockWise, boolean inside,
                              Color highlightColor, Color middleColor, Color shadowColor) {
    int mul = clockWise ? 1 : -1;
    int dx = (x2 - x1) * mul;
    int dy = (y2 - y1) * mul;
    int l2 = 2 * (dx * dx + dy * dy);
    int a = dx - dy;
    Color blendColor = a > 0 ? highlightColor : shadowColor;

    if (blendColor != null) {
      g.setColor(ColorUtil.blend(middleColor, blendColor, (float) a * a / l2));
      int hx = inside ? getHighlightOffsetX(dx, dy) : 0;
      int hy = inside ? getHighlightOffsetY(dx, dy) : 0;
      GraphicsUtil.drawOptimizedLine(g, x1 + hx, y1 + hy, x2 + hx, y2 + hy);
    }

  }

  public static float getBlendFactor(int dx, int dy) {
    int l2 = 2 * (dx * dx + dy * dy);
    int a = dx - dy;
    return 1 - (float) a * a / l2;
  }

  protected static int getHighlightOffsetX(int deltaX, int deltaY) {
    return deltaY - deltaX > 0 ? (deltaX + deltaY > 0 ? -1 : 0) : (deltaX + deltaY > 0 ? 0 : 1); //-deltaY > deltaX ? 1 : 0;
  }

  protected static int getHighlightOffsetY(int deltaX, int deltaY) {
    return deltaY - deltaX > 0 ? (deltaX + deltaY > 0 ? 0 : -1) : (deltaX + deltaY > 0 ? 1 : 0); //-deltaY > deltaX ? 1 : 0;
  }


}
