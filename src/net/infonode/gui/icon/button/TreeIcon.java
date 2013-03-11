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


// $Id: TreeIcon.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.gui.icon.button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import net.infonode.gui.GraphicsUtil;

/**
 * @author Jesper Nordenberg
 * @version $Revision: 1.3 $ $Date: 2011-09-07 19:56:09 $
 */
public class TreeIcon implements Icon {
  public static final int PLUS = 1;
  public static final int MINUS = 2;

  private int type;
  private int width;
  private int height;
  private Color color;
  private Color bgColor;
  private boolean border = true;

  public TreeIcon(int type, int width, int height, boolean border, Color color, Color bgColor) {
    this.type = type;
    this.width = width;
    this.height = height;
    this.border = border;
    this.color = color;
    this.bgColor = bgColor;
  }

  public TreeIcon(int type, int width, int height) {
    this(type, width, height, true, Color.BLACK, null);
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    if (bgColor != null) {
      g.setColor(bgColor);
      g.fillRect(x + 1, y + 1, width - 2, height - 2);
    }

    g.setColor(color);

    if (border) {
      g.drawRect(x + 1, y + 1, width - 2, height - 2);
    }

    GraphicsUtil.drawOptimizedLine(g, x + 3, y + height / 2, x + width - 3, y + height / 2);

    if (type == PLUS)
      GraphicsUtil.drawOptimizedLine(g, x + width / 2, y + 3, x + width / 2, y + height - 3);
  }

  public int getIconWidth() {
    return width;
  }

  public int getIconHeight() {
    return height;
  }
}
