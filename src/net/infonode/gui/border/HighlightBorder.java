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


// $Id: HighlightBorder.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.InsetsUtil;
import net.infonode.gui.colorprovider.BackgroundPainterColorProvider;
import net.infonode.gui.colorprovider.ColorMultiplier;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.ColorProviderUtil;
import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class HighlightBorder implements Border, Serializable {
  private static final long serialVersionUID = 1;

  private static final Insets INSETS = new Insets(1, 1, 0, 0);
  private boolean lowered;
  private boolean pressed;
  private ColorProvider colorProvider;

  public HighlightBorder() {
    this(false);
  }

  public HighlightBorder(boolean lowered) {
    this(lowered, null);
  }

  public HighlightBorder(boolean lowered, Color color) {
    this(lowered, false, color);
  }

  public HighlightBorder(boolean lowered, boolean pressed, Color color) {
    this(lowered, pressed, ColorProviderUtil.getColorProvider(color,
                                                              new ColorMultiplier(
                                                                  BackgroundPainterColorProvider.INSTANCE,
                                                                  lowered ? 0.7 : 1.70)));
  }

  public HighlightBorder(boolean lowered, boolean pressed, ColorProvider colorProvider) {
    this.lowered = lowered;
    this.pressed = pressed;
    this.colorProvider = colorProvider;
  }

  public Insets getBorderInsets(Component c) {
    return pressed ? InsetsUtil.rotate(Direction.LEFT, INSETS) : INSETS;
  }

  public boolean isBorderOpaque() {
    return false;
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    g.setColor(colorProvider.getColor(c));

    if (pressed) {
      GraphicsUtil.drawOptimizedLine(g, x + (lowered ? 0 : 1), y + height - 1, x + width - 1, y + height - 1);
      GraphicsUtil.drawOptimizedLine(g, x + width - 1, y + (lowered ? 0 : 1), x + width - 1, y + height - 2);
    }
    else {
      GraphicsUtil.drawOptimizedLine(g, x, y, x + width - (lowered ? 1 : 2), y);
      GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - (lowered ? 1 : 2));
    }
  }
}
