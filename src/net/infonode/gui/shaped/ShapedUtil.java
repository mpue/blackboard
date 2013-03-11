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


// $Id: ShapedUtil.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.shaped;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;

import net.infonode.gui.InsetsUtil;
import net.infonode.gui.RectangleUtil;
import net.infonode.gui.shaped.panel.ShapedPanel;
import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class ShapedUtil {
  private ShapedUtil() {
  }

  public static Direction getDirection(Component c) {
    return c instanceof ShapedPanel ? ((ShapedPanel) c).getDirection() : Direction.RIGHT;
  }

  public static Insets transformInsets(Component c, Insets insets) {
    return InsetsUtil.rotate(getDirection(c), flipInsets(c, insets));
  }

  public static Insets flipInsets(Component c, Insets i) {
    if (c instanceof ShapedPanel) {
      if (((ShapedPanel) c).isHorizontalFlip())
        i = InsetsUtil.flipHorizontal(i);
      if (((ShapedPanel) c).isVerticalFlip())
        i = InsetsUtil.flipVertical(i);
    }

    return i;
  }

  public static void rotateCW(Polygon polygon, int height) {
    for (int i = 0; i < polygon.npoints; i++) {
      int tmp = polygon.ypoints[i];
      polygon.ypoints[i] = polygon.xpoints[i];
      polygon.xpoints[i] = height - 1 - tmp;
    }
  }

  public static void rotate(Polygon polygon, Direction d, int width, int height) {
    if (d == Direction.UP) {
      rotateCW(polygon, height);
      rotateCW(polygon, width);
      rotateCW(polygon, height);
    }
    else if (d == Direction.LEFT) {
      rotateCW(polygon, height);
      rotateCW(polygon, width);
    }
    else if (d == Direction.DOWN) {
      rotateCW(polygon, height);
    }
  }

  public static Rectangle transform(Component c, Rectangle rect) {
    if (c instanceof ShapedPanel) {
      ShapedPanel sp = (ShapedPanel) c;
      return RectangleUtil.transform(rect,
                                     sp.getDirection(),
                                     sp.isHorizontalFlip(),
                                     sp.isVerticalFlip(),
                                     c.getWidth(),
                                     c.getHeight());
    }
    else
      return rect;
  }

  public static Dimension transform(Component c, Dimension dim) {
    return getDirection(c).isHorizontal() ? dim : new Dimension(dim.height, dim.width);
  }

  public static int getWidth(Component c, int width, int height) {
    return getDirection(c).isHorizontal() ? width : height;
  }

  public static int getHeight(Component c, int width, int height) {
    return getDirection(c).isHorizontal() ? height : width;
  }

}
