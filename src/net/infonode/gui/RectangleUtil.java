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


// $Id: RectangleUtil.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Rectangle;

import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class RectangleUtil {
  private RectangleUtil() {
  }

  public static Rectangle transform(Rectangle rectangle,
                                    Direction direction,
                                    boolean horizontalFlip,
                                    boolean verticalFlip,
                                    int width,
                                    int height) {
    int x = horizontalFlip ? (direction.isHorizontal() ? width : height) - rectangle.width - rectangle.x : rectangle.x;
    int y = verticalFlip ? (direction.isHorizontal() ? height : width) - rectangle.height - rectangle.y : rectangle.y;

    return direction == Direction.DOWN ? new Rectangle(width - y - rectangle.height,
                                                       x,
                                                       rectangle.height,
                                                       rectangle.width) :
           direction == Direction.LEFT ? new Rectangle(width - x - rectangle.width,
                                                       height - y - rectangle.height,
                                                       rectangle.width,
                                                       rectangle.height) :
           direction == Direction.UP ? new Rectangle(y,
                                                     height - x - rectangle.width,
                                                     rectangle.height,
                                                     rectangle.width) :
           new Rectangle(x, y, rectangle.width, rectangle.height);
  }
}
