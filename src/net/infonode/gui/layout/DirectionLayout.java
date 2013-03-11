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


// $Id: DirectionLayout.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.HashMap;

import net.infonode.util.Direction;

public class DirectionLayout implements LayoutManager2 {
  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

  private Direction direction;
  private HashMap componentInsets;
  private int componentSpacing;
  private boolean compressing;

  private ArrayList layoutOrderList;

  public DirectionLayout() {
    this(Direction.RIGHT);
  }

  public DirectionLayout(int componentSpacing) {
    this(Direction.RIGHT, componentSpacing);
  }

  public DirectionLayout(Direction direction) {
    this(direction, 0);
  }

  public DirectionLayout(Direction direction, int componentSpacing) {
    this.direction = direction;
    this.componentSpacing = componentSpacing;
  }

  public int getComponentSpacing() {
    return componentSpacing;
  }

  public void setComponentSpacing(int componentSpacing) {
    this.componentSpacing = componentSpacing;
  }

  public void addLayoutComponent(String name, Component comp) {
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public boolean isVertical() {
    return !direction.isHorizontal();
  }

  public boolean isCompressing() {
    return compressing;
  }

  public void setCompressing(boolean compressing) {
    this.compressing = compressing;
  }

  public void setLayoutOrderList(ArrayList layoutOrderList) {
    this.layoutOrderList = layoutOrderList;
  }

  private int getSize(Dimension d) {
    return (int) (isVertical() ? d.getHeight() : d.getWidth());
  }

  private static int getBeforeSpacing(Insets insets) {
    return insets.left;
  }

  private int getAfterSpacing(Component component, boolean isLast) {
    return getInsets(component).right + (isLast ? 0 : componentSpacing);
  }

  private Dimension createSize(int size, int otherSize) {
    return isVertical() ? new Dimension(otherSize, size) : new Dimension(size, otherSize);
  }

  private static Dimension getSize(Dimension interiorSize, Container component) {
    Insets insets = component.getInsets();
    return new Dimension(interiorSize.width + insets.left + insets.right,
                         interiorSize.height + insets.top + insets.bottom);
  }

  private int getOtherSize(Dimension dim) {
    return (int) (isVertical() ? dim.getWidth() : dim.getHeight());
  }

  private void setSize(Component component, int size, int otherSize) {
    int maxOtherSize = getOtherSize(component.getMaximumSize());
    component.setSize(createSize(size, Math.min(maxOtherSize, otherSize)));
  }

  public void layoutContainer(Container parent) {
    Component[] components = getVisibleChildren(parent);
    
    /*System.out.println("Parent: " + parent.getComponentCount() + "  List: " + components.length);
    for (int i = 0; i < components.length; i++) {
    	System.out.println("Parent: " + parent.getComponent(i) + "  List: " + components[i]);
    }*/
    
    int count = components.length;

    if (components.length == 0)
      return;

    int totalSpacing = 0;

    for (int i = 0; i < components.length; i++)
      totalSpacing += getSpacing(components[i], i == components.length - 1);

    boolean[] discarded = new boolean[components.length];
    Dimension parentInteriorSize = LayoutUtil.getInteriorSize(parent);
    int componentsTotalSize = getSize(parentInteriorSize) - totalSpacing;
    int maxComponentSize = componentsTotalSize / components.length;
    int lastCount;

    int otherSize = getOtherSize(parentInteriorSize);
    //System.out.println(parentInteriorSize);
//    int otherSize = Math.min(getOtherSize(parentInteriorSize), getOtherSize(LayoutUtil.getMaxPreferredSize(components)));

    // First, set componentsTotalSize of all components that fit inside the limit
    do {
      lastCount = count;

      for (int i = 0; i < components.length; i++) {
        if (!discarded[i]) {
          int prefSize = getSize(components[i].getPreferredSize());

          if (prefSize <= maxComponentSize) {
            setSize(components[i], prefSize, otherSize);
            componentsTotalSize -= prefSize;
            discarded[i] = true;
            count--;

            if (count == 0)
              break;

            maxComponentSize = componentsTotalSize / count;
          }
        }
      }
    } while (lastCount > count);

    if (count > 0) {
      do {
        lastCount = count;

        // Now fit all that have a larger minimum componentsTotalSize
        for (int i = 0; i < components.length; i++) {
          if (!discarded[i]) {
            int minSize = getSize(components[i].getMinimumSize());

            if (minSize >= maxComponentSize) {
              setSize(components[i], minSize, otherSize);
              componentsTotalSize -= minSize;
              discarded[i] = true;
              count--;

              if (count == 0)
                break;

              maxComponentSize = componentsTotalSize / count;
            }
          }
        }
      } while (lastCount > count);
    }

    Insets insets = parent.getInsets();

    int pos = direction == Direction.RIGHT ? insets.left :
              direction == Direction.DOWN ? insets.top :
              direction == Direction.LEFT ? insets.right :
              insets.bottom;

    int yOffset = isVertical() ? insets.left : insets.top;

    for (int i = 0; i < components.length; i++) {
      pos += getBeforeSpacing(getInsets(components[i]));

      if (!discarded[i]) {
        int componentSize = Math.max(getSize(components[i].getMinimumSize()), componentsTotalSize / count);
        setSize(components[i], componentSize, otherSize);
        count--;
        componentsTotalSize -= componentSize;
      }

      int yPos = yOffset + (int) ((otherSize - getOtherSize(components[i].getSize())) *
                                  (direction == Direction.DOWN || direction == Direction.LEFT ?
                                   1 - components[i].getAlignmentY() :
                                   components[i].getAlignmentY()));

      if (isVertical()) {
        components[i].setLocation(yPos,
                                  direction == Direction.DOWN ?
                                  pos :
                                  parent.getHeight() - pos - components[i].getHeight());
      }
      else {
        components[i].setLocation(direction == Direction.RIGHT ?
                                  pos :
                                  parent.getWidth() - pos - components[i].getWidth(),
                                  yPos);
      }

      pos += getSize(components[i].getSize()) + getAfterSpacing(components[i], i == components.length - 1);
      //System.out.println("\n" + components[i] + " " + components[i].getLocation() + "  " + components[i].getSize() + "  " + ((JComponent)components[i]).getInsets());
    }
  }

  public void setComponentInsets(Component c, Insets i) {
    if (i == null) {
      removeLayoutComponent(c);
    }
    else {
      if (componentInsets == null)
        componentInsets = new HashMap(4);

      componentInsets.put(c, i);
    }
  }

  private Component[] getVisibleChildren(Container parent) {
    if (layoutOrderList != null) {
      Component[] components = new Component[layoutOrderList.size()];
      for (int i = 0; i < layoutOrderList.size(); i++)
        components[i] = (Component) layoutOrderList.get(i);

      return LayoutUtil.getVisibleChildren(components);
    }

    return LayoutUtil.getVisibleChildren(parent);
  }

  private int getSpacing(Component component, boolean isLast) {
    Insets insets = getInsets(component);
    return insets.left + insets.right + (isLast ? 0 : componentSpacing);
  }

  private Insets getInsets(Component component) {
    Object o = componentInsets == null ? null : componentInsets.get(component);
    return o == null ? EMPTY_INSETS : (Insets) o;
  }

  public Dimension minimumLayoutSize(Container parent) {
    Component[] c = getVisibleChildren(parent);
    int size = 0;
    int maxHeight = 0;

    for (int i = 0; i < c.length; i++) {
      size += getSize(c[i].getMinimumSize()) + getSpacing(c[i], i == c.length - 1);
      maxHeight = Math.max(getOtherSize(c[i].getMinimumSize()), maxHeight);
    }

    Dimension d = getSize(isVertical() ? new Dimension(maxHeight, size) : new Dimension(size, maxHeight), parent);
    //System.out.println("Minimum size: " + d);
    return d;
  }

  public Dimension preferredLayoutSize(Container parent) {
    Component[] c = getVisibleChildren(parent);
    int size = 0;
    int maxHeight = 0;

    for (int i = 0; i < c.length; i++) {
      if (!compressing)
        size += getSize(c[i].getPreferredSize()) + getSpacing(c[i], i == c.length - 1);

      maxHeight = Math.max(getOtherSize(c[i].getPreferredSize()), maxHeight);
    }

    Dimension d = getSize(isVertical() ? new Dimension(maxHeight, size) : new Dimension(size, maxHeight), parent);
    //System.out.println("Preferred size: " + d);
    return d;
  }

  public void removeLayoutComponent(Component comp) {
    if (componentInsets != null) {
      componentInsets.remove(comp);

      if (componentInsets.size() == 0)
        componentInsets = null;
    }
  }

  public void addLayoutComponent(Component comp, Object constraints) {
    setComponentInsets(comp, (Insets) constraints);
  }

  public float getLayoutAlignmentX(Container target) {
    return 0;
  }

  public float getLayoutAlignmentY(Container target) {
    return 0;
  }

  public void invalidateLayout(Container target) {
  }

  public Dimension maximumLayoutSize(Container parent) {
    Component[] c = getVisibleChildren(parent);
    int size = 0;
    int maxHeight = Integer.MAX_VALUE;

    for (int i = 0; i < c.length; i++) {
      size += getSize(c[i].getMaximumSize()) + getSpacing(c[i], i == c.length - 1);
//      maxHeight = Math.min(getOtherSize(c[i].getMaximumSize()), maxHeight);
    }

    Dimension d = getSize(isVertical() ? new Dimension(maxHeight, size) : new Dimension(size, maxHeight), parent);
    //System.out.println("Maximum size: " + d);
    return d;
  }
}
