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


// $Id: EdgeBorder.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;

import net.infonode.gui.ComponentUtil;
import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.util.ColorUtil;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class EdgeBorder implements Border, Serializable {
  private static final long serialVersionUID = 1;

  private ColorProvider topLeftColor;
  private ColorProvider bottomRightColor;
  private boolean drawTop;
  private boolean drawBottom;
  private boolean drawLeft;
  private boolean drawRight;
  private Insets insets;

  public EdgeBorder() {
    this(true, true, true, true);
  }

  public EdgeBorder(boolean drawTop, boolean drawBottom, boolean drawLeft, boolean drawRight) {
    this(null, drawTop, drawBottom, drawLeft, drawRight);
  }

  public EdgeBorder(Color color, boolean drawTop, boolean drawBottom, boolean drawLeft, boolean drawRight) {
    ColorProvider c = color == null ? null : new FixedColorProvider(color);
    init(c, c, drawTop, drawBottom, drawLeft, drawRight);
  }

  public EdgeBorder(ColorProvider color) {
    init(color, color, true, true, true, true);
  }

  public EdgeBorder(ColorProvider topLeftColor,
                    ColorProvider bottomRightColor,
                    boolean drawTop,
                    boolean drawBottom,
                    boolean drawLeft,
                    boolean drawRight) {
    init(topLeftColor, bottomRightColor, drawTop, drawBottom, drawLeft, drawRight);
  }

  private void init(ColorProvider topLeftColor,
                    ColorProvider bottomRightColor,
                    boolean drawTop,
                    boolean drawBottom,
                    boolean drawLeft,
                    boolean drawRight) {
    this.topLeftColor = topLeftColor;
    this.bottomRightColor = bottomRightColor;
    this.drawTop = drawTop;
    this.drawBottom = drawBottom;
    this.drawLeft = drawLeft;
    this.drawRight = drawRight;
    insets = new Insets(drawTop ? 1 : 0, drawLeft ? 1 : 0, drawBottom ? 1 : 0, drawRight ? 1 : 0);
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Color topLeft = getColor(topLeftColor, c);
    Color bottomRight = getColor(bottomRightColor, c);

    if (topLeft != null && bottomRight != null) {
      g.setColor(topLeft);

      if (drawTop)
        GraphicsUtil.drawOptimizedLine(g, x, y, x + width - 1, y);

      if (drawLeft)
        GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - 1);

      g.setColor(bottomRight);
      if (drawRight)
        GraphicsUtil.drawOptimizedLine(g, x + width - 1, y, x + width - 1, y + height - 1);

      if (drawBottom)
        GraphicsUtil.drawOptimizedLine(g, x, y + height - 1, x + width - 1, y + height - 1);
    }
  }

  public Insets getBorderInsets(Component c) {
    return insets;
  }

  public boolean isBorderOpaque() {
    return false;
  }

  private Color getColor(ColorProvider color, Component c) {
    Color col = color != null ? color.getColor() : null;

    if (col == null) {
      Color background = ComponentUtil.getBackgroundColor(c);
      return background == null ? null : ColorUtil.mult(background, 0.7f);
    }

    return col;
  }
}
