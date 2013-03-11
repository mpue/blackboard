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


// $Id: FixedTransformComponentPainter.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.componentpainter;

import java.awt.Component;
import java.awt.Graphics;

import net.infonode.util.Direction;

/**
 * A painter that paints its wrapped painter using the same fixed values for direction, horizontal flip and
 * vertical flip.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class FixedTransformComponentPainter extends AbstractComponentPainterWrapper {
  private static final long serialVersionUID = 1;

  private Direction direction;
  private boolean horizontalFlip;
  private boolean verticalFlip;

  public FixedTransformComponentPainter(ComponentPainter painter) {
    this(painter, Direction.RIGHT);
  }

  public FixedTransformComponentPainter(ComponentPainter painter, Direction direction) {
    this(painter, direction, false, false);
  }

  public FixedTransformComponentPainter(ComponentPainter painter,
                                        Direction direction,
                                        boolean horizontalFlip,
                                        boolean verticalFlip) {
    super(painter);
    this.direction = direction;
    this.horizontalFlip = horizontalFlip;
    this.verticalFlip = verticalFlip;
  }

  public void paint(Component component, Graphics g, int x, int y, int width, int height) {
    super.paint(component, g, x, y, width, height, direction, horizontalFlip, verticalFlip);
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
    paint(component, g, x, y, width, height);
  }

}
