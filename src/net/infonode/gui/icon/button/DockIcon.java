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


// $Id: DockIcon.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui.icon.button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import net.infonode.gui.GraphicsUtil;

/**
 * @author johan
 */
public class DockIcon extends AbstractButtonIcon {
  private static final long serialVersionUID = 1;

  public DockIcon() {
    super();
  }

  public DockIcon(Color c) {
    super(c);
  }

  public DockIcon(Color c, int size) {
    super(c, size);
  }

  public DockIcon(int size) {
    super(size);
  }

  protected void paintIcon(Component c, final Graphics g, final int x1, final int y1, final int x2, final int y2) {
    int xOffs = (x2 - x1) > 6 ? 1 : 0;
    int yOffs = xOffs;

    // Bottom left
    GraphicsUtil.drawOptimizedLine(g, x1 + xOffs, y2 - yOffs, x1 + xOffs, y2 - yOffs - 3);
    GraphicsUtil.drawOptimizedLine(g, x1 + xOffs, y2 - yOffs, x1 + xOffs + 3, y2 - yOffs);

    // Top right
    GraphicsUtil.drawOptimizedLine(g, x2 - xOffs - 1, y1 + yOffs, x2 - xOffs - 1, y1 + yOffs + 2);
    GraphicsUtil.drawOptimizedLine(g, x2 - xOffs - 2, y1 + yOffs - 1, x2 - xOffs - 2, y1 + yOffs - 1);

    // Lines
    GraphicsUtil.drawOptimizedLine(g, x2 - xOffs - 1, y1 + yOffs, x1 + xOffs, y2 - yOffs - 1);
    GraphicsUtil.drawOptimizedLine(g, x2 - xOffs - 1, y1 + yOffs + 1, x1 + xOffs, y2 - yOffs);
    GraphicsUtil.drawOptimizedLine(g, x2 - xOffs - 1, y1 + yOffs + 2, x1 + xOffs + 1, y2 - yOffs);
  }
}
