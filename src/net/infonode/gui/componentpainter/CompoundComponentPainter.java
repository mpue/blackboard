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


// $Id: CompoundComponentPainter.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.componentpainter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import net.infonode.util.Direction;

/**
 * Paints the same area with two painters.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class CompoundComponentPainter extends AbstractComponentPainter {
  private static final long serialVersionUID = 1;

  private ComponentPainter bottomPainter;
  private ComponentPainter topPainter;

  public CompoundComponentPainter(ComponentPainter bottomPainter, ComponentPainter topPainter) {
    this.bottomPainter = bottomPainter;
    this.topPainter = topPainter;
  }

  public void paint(Component component,
                    Graphics g,
                    int x,
                    int y,
                    int width,
                    int height,
                    Direction direction,
                    boolean horizontalFlip,
                    boolean verticalFlip) {
    bottomPainter.paint(component, g, x, y, width, height, direction, horizontalFlip, verticalFlip);
    topPainter.paint(component, g, x, y, width, height, direction, horizontalFlip, verticalFlip);
  }

  public boolean isOpaque(Component component) {
    return bottomPainter.isOpaque(component) || topPainter.isOpaque(component);
  }

  public Color getColor(Component component) {
    return topPainter.getColor(component);
  }

}
