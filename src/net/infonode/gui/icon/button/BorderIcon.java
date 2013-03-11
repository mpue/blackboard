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


// $Id: BorderIcon.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui.icon.button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;

public class BorderIcon implements Icon {
  private Icon icon;
  private Color color;
  private Insets insets;

  public BorderIcon(Icon icon, int borderSize) {
    this(icon, null, new Insets(borderSize, borderSize, borderSize, borderSize));
  }

  public BorderIcon(Icon icon, Color color, Insets insets) {
    this.icon = icon;
    this.color = color;
    this.insets = insets;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    if (color != null) {
      Color oldColor = g.getColor();
      g.setColor(color);
      g.fillRect(x, y, getIconWidth(), insets.top);
      g.fillRect(x, y + getIconHeight() - insets.bottom, getIconWidth(), insets.bottom);
      g.fillRect(x, y + insets.top, insets.left, getIconHeight() - insets.top - insets.bottom);
      g.fillRect(x + getIconWidth() - insets.right,
                 y + insets.top,
                 insets.right,
                 getIconHeight() - insets.top - insets.bottom);
      g.setColor(oldColor);
    }

    icon.paintIcon(c, g, x + insets.left, y + insets.top);
  }

  public int getIconWidth() {
    return insets.left + insets.right + icon.getIconWidth();
  }

  public int getIconHeight() {
    return insets.top + insets.bottom + icon.getIconHeight();
  }
}
