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


// $Id: AbstractComponentPainter.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.componentpainter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import net.infonode.util.Direction;
import net.infonode.util.ImageUtils;

/**
 * An abstract base class for {@link ComponentPainter}'s. Default implementations for both paint methods are provided,
 * but becuase they call each other a sub class must override one or both methods.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.2.0
 */
abstract public class AbstractComponentPainter implements ComponentPainter, Serializable {
  private static final long serialVersionUID = 1;

  protected AbstractComponentPainter() {
  }

  public void paint(Component component, Graphics g, int x, int y, int width, int height) {
    paint(component, g, x, y, width, height, Direction.RIGHT, false, false);
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
    if (direction != Direction.RIGHT || horizontalFlip || verticalFlip) {
      Graphics2D g2 = (Graphics2D) g;
      AffineTransform t = g2.getTransform();

      try {
        int w = direction.isHorizontal() ? width : height;
        int h = direction.isHorizontal() ? height : width;
        AffineTransform nt = ImageUtils.createTransform(direction, horizontalFlip, verticalFlip, w, h);
        g2.translate(x, y);
        g2.transform(nt);
//        Point p = new Point(x, y);
//        nt.transform(p, p);
        paint(component, g, 0, 0, w, h);
      }
      finally {
        g2.setTransform(t);
      }
    }
    else {
      paint(component, g, x, y, width, height);
    }
  }

  public boolean isOpaque(Component component) {
    return true;
  }

}
