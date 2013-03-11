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


// $Id: TranslatingShape.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class TranslatingShape implements Shape {
  private Shape shape;
  private double dx;
  private double dy;

  public TranslatingShape(Shape shape, double dx, double dy) {
    this.shape = shape;
    this.dx = dx;
    this.dy = dy;
  }

  public Rectangle getBounds() {
    Rectangle r = shape.getBounds();
    r.translate((int) dx, (int) dy);
    return r;
  }

  public Rectangle2D getBounds2D() {
    Rectangle2D r = shape.getBounds2D();
    r.setRect(r.getMinX() + dx, r.getMinY() + dy, r.getWidth(), r.getHeight());
    return r;
  }

  public boolean contains(double x, double y) {
    return shape.contains(x - dx, y - dy);
  }

  public boolean contains(Point2D p) {
    return contains(p.getX(), p.getY());
  }

  public boolean intersects(double x, double y, double w, double h) {
    return shape.intersects(x - dx, y - dy, w, h);
  }

  public boolean intersects(Rectangle2D r) {
    return intersects(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
  }

  public boolean contains(double x, double y, double w, double h) {
    return shape.contains(x - dx, y - dy, w, h);
  }

  public boolean contains(Rectangle2D r) {
    return contains(r.getMinX() - dx, r.getMinY() - dy, r.getWidth(), r.getHeight());
  }

  public PathIterator getPathIterator(AffineTransform at) {
    return new Iterator(shape.getPathIterator(at));
  }

  public PathIterator getPathIterator(AffineTransform at, double flatness) {
    return new Iterator(shape.getPathIterator(at, flatness));
  }

  private class Iterator implements PathIterator {
    private PathIterator iterator;

    Iterator(PathIterator iterator) {
      this.iterator = iterator;
    }

    public int getWindingRule() {
      return iterator.getWindingRule();
    }

    public boolean isDone() {
      return iterator.isDone();
    }

    public void next() {
      iterator.next();
    }

    public int currentSegment(float[] coords) {
      int result = iterator.currentSegment(coords);

      for (int i = 0; i < coords.length; i++) {
        coords[i++] += dx;
        coords[i] += dy;
      }

      return result;
    }

    public int currentSegment(double[] coords) {
      int result = iterator.currentSegment(coords);

      for (int i = 0; i < coords.length; i++) {
        coords[i++] += dx;
        coords[i] += dy;
      }

      return result;
    }
  }
}
