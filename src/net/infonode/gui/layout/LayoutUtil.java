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


// $Id: LayoutUtil.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import net.infonode.util.Direction;

public class LayoutUtil {
  private LayoutUtil() {
  }

  public static Component[] getVisibleChildren(Container parent) {
    return getVisibleChildren(parent.getComponents());
  }

  public static Component[] getVisibleChildren(Component[] components) {
    int count = 0;

    for (int i = 0; i < components.length; i++)
      if (components[i].isVisible())
        count++;

    Component[] c = new Component[count];
    int index = 0;

    for (int i = 0; i < components.length; i++)
      if (components[i].isVisible())
        c[index++] = components[i];

    return c;
  }

  public static Rectangle getInteriorArea(Container container) {
    Insets insets = container.getInsets();
    return new Rectangle(insets.left,
                         insets.top,
                         container.getWidth() - insets.left - insets.right,
                         container.getHeight() - insets.top - insets.bottom);
  }

  public static Dimension getInteriorSize(Container container) {
    Insets insets = container.getInsets();
    return new Dimension(container.getWidth() - insets.left - insets.right,
                         container.getHeight() - insets.top - insets.bottom);
  }

  public static Dimension rotate(Dimension dim, Direction dir) {
    return rotate(dim, dir.isHorizontal());
  }

  public static Dimension rotate(Dimension dim, boolean horizontal) {
    return dim == null ? null : horizontal ? dim : new Dimension(dim.height, dim.width);
  }

  public static boolean isDescendingFrom(Component component, Component parent) {
    return component == parent || (component != null && isDescendingFrom(component.getParent(), parent));
  }

  public static Dimension getMaxMinimumSize(Component[] components) {
    int maxWidth = 0;
    int maxHeight = 0;

    for (int i = 0; i < components.length; i++) {
      if (components[i] != null) {
        Dimension min = components[i].getMinimumSize();
        int w = min.width;
        int h = min.height;

        if (maxHeight < h)
          maxHeight = h;

        if (maxWidth < w)
          maxWidth = w;
      }
    }

    return new Dimension(maxWidth, maxHeight);
  }

  public static Dimension getMaxPreferredSize(Component[] components) {
    int maxWidth = 0;
    int maxHeight = 0;

    for (int i = 0; i < components.length; i++) {
      if (components[i] != null) {
        Dimension min = components[i].getPreferredSize();
        int w = min.width;
        int h = min.height;

        if (maxHeight < h)
          maxHeight = h;

        if (maxWidth < w)
          maxWidth = w;
      }
    }

    return new Dimension(maxWidth, maxHeight);
  }

  public static Dimension getMinMaximumSize(Component[] components) {
    int minWidth = Integer.MAX_VALUE;
    int minHeight = Integer.MAX_VALUE;

    for (int i = 0; i < components.length; i++) {
      if (components[i] != null) {
        Dimension min = components[i].getMaximumSize();
        int w = min.width;
        int h = min.height;

        if (minWidth > w)
          minWidth = w;

        if (minHeight > h)
          minHeight = h;
      }
    }

    return new Dimension(minWidth, minHeight);
  }

  public static Insets rotate(Direction dir, Insets insets) {
    return dir == Direction.RIGHT ? insets :
           dir == Direction.DOWN ? new Insets(insets.right, insets.top, insets.left, insets.bottom) :
           dir == Direction.LEFT ? new Insets(insets.bottom, insets.right, insets.top, insets.left) :
           new Insets(insets.left, insets.bottom, insets.right, insets.top);
  }

  public static Insets unrotate(Direction dir, Insets insets) {
    return dir == Direction.RIGHT ? insets :
           dir == Direction.DOWN ? new Insets(insets.left, insets.bottom, insets.right, insets.top) :
           dir == Direction.LEFT ? new Insets(insets.bottom, insets.right, insets.top, insets.left) :
           new Insets(insets.right, insets.top, insets.left, insets.bottom);
  }

  public static Dimension add(Dimension dim, Insets insets) {
    return new Dimension(dim.width + insets.left + insets.right, dim.height + insets.top + insets.bottom);
  }

  public static Dimension getValidSize(Dimension dim, Component component) {
    Dimension minSize = component.getMinimumSize();
    Dimension maxSize = component.getMaximumSize();
    return new Dimension(Math.max(minSize.width, Math.min(dim.width, maxSize.width)),
                         Math.max(minSize.height, Math.min(dim.height, maxSize.height)));
  }

  public static Component getChildContaining(Component parent, Component component) {
    return component == null ?
           null : component.getParent() == parent ? component : getChildContaining(parent, component.getParent());
  }

  public static String getBorderLayoutOrientation(Direction direction) {
    return direction == Direction.UP ? BorderLayout.NORTH :
           direction == Direction.DOWN ? BorderLayout.SOUTH :
           direction == Direction.LEFT ? BorderLayout.WEST :
           BorderLayout.EAST;
  }

}
