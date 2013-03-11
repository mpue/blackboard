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


// $Id: PopupMenuBorder.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;

import net.infonode.gui.GraphicsUtil;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class PopupMenuBorder implements Border, Serializable {
  private static final long serialVersionUID = 1;

  private static final Insets INSETS = new Insets(3, 1, 2, 1);

  private Color highlightColor;
  private Color shadowColor;

  public PopupMenuBorder(Color highlightColor, Color shadowColor) {
    this.highlightColor = highlightColor;
    this.shadowColor = shadowColor;
  }

  public boolean isBorderOpaque() {
    return false;
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    g.setColor(shadowColor);
    g.drawRect(x, y, width - 1, height - 1);

    g.setColor(highlightColor);
    GraphicsUtil.drawOptimizedLine(g, x + 1, y + 1, x + width - 2, y + 1);
    GraphicsUtil.drawOptimizedLine(g, x + 1, y + 2, x + 1, y + 2);
    GraphicsUtil.drawOptimizedLine(g, x + 1, y + height - 2, x + 1, y + height - 2);
  }

  public Insets getBorderInsets(Component c) {
    return INSETS;
  }
}
