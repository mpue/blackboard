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


// $Id: ComponentUtil.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.gui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.util.Direction;

public class ComponentUtil {
  private ComponentUtil() {
  }

  public static final Component getChildAt(Container container, Point p) {
    Component c = container.getComponentAt(p);
    return c == null || c.getParent() != container ? null : c;
  }

  public static final Component getVisibleChildAt(Container container, Point p) {
    for (int i = 0; i < container.getComponentCount(); i++) {
      Component c = container.getComponent(i);
      if (c.isVisible() && c.contains(p.x - c.getX(), p.y - c.getY()))
        return c;
    }

    return null;
  }

  public static final Component getChildAtLine(Container container, Point p, boolean horizontal) {
    if (horizontal) {
      for (int i = 0; i < container.getComponentCount(); i++) {
        Component c = container.getComponent(i);
        if (p.x >= c.getX() && p.x < c.getX() + c.getWidth())
          return c;
      }
    }
    else {
      for (int i = 0; i < container.getComponentCount(); i++) {
        Component c = container.getComponent(i);
        if (p.y >= c.getY() && p.y < c.getY() + c.getHeight())
          return c;
      }
    }

    return null;
  }

  public static void getComponentTreePosition(Component c, ArrayList pos) {
    if (c.getParent() == null) {
      return;
    }

    getComponentTreePosition(c.getParent(), pos);

    pos.add(new Integer(c.getParent().getComponentCount() - ComponentUtil.getComponentIndex(c)));
  }

  public static Component findComponentUnderGlassPaneAt(Point p, Component top) {
    Component c = null;

    if (top.isShowing()) {
      if (top instanceof RootPaneContainer)
        c =
        ((RootPaneContainer) top).getLayeredPane().findComponentAt(
            SwingUtilities.convertPoint(top, p, ((RootPaneContainer) top).getLayeredPane()));
      else
        c = ((Container) top).findComponentAt(p);
    }

    return c;
  }

  public static final int getComponentIndex(Component component) {
    if (component != null && component.getParent() != null) {
      Container c = component.getParent();
      for (int i = 0; i < c.getComponentCount(); i++) {
        if (c.getComponent(i) == component)
          return i;
      }
    }

    return -1;
  }

  public static final String getBorderLayoutOrientation(Direction d) {
    return d == Direction.UP ?
           BorderLayout.NORTH :
           d == Direction.LEFT ? BorderLayout.WEST : d == Direction.DOWN ? BorderLayout.SOUTH : BorderLayout.EAST;
  }

  public static Color getBackgroundColor(Component component) {
    if (component == null)
      return null;

    if (component instanceof BackgroundPainter) {
      ComponentPainter painter = ((BackgroundPainter) component).getComponentPainter();

      if (painter != null) {
        Color c = painter.getColor(component);

        if (c != null)
          return c;
      }
    }

    return component.isOpaque() ? component.getBackground() : getBackgroundColor(component.getParent());
  }

  public static int countComponents(Container c) {
    int num = 1;
    for (int i = 0; i < c.getComponentCount(); i++) {
      Component comp = c.getComponent(i);
      if (comp instanceof Container)
        num += countComponents((Container) comp);
      else
        num++;
    }

    return num;
  }

  public static int getVisibleChildrenCount(Component c) {
    if (c == null || !(c instanceof Container))
      return 0;

    int count = 0;
    Container container = (Container) c;

    for (int i = 0; i < container.getComponentCount(); i++)
      if (container.getComponent(i).isVisible())
        count++;

    return count;
  }

  public static Component getTopLevelAncestor(Component c) {
    while (c != null) {
      if (c instanceof Window || c instanceof Applet)
        break;
      c = c.getParent();
    }
    return c;
  }

  public static boolean hasVisibleChildren(Component c) {
    return getVisibleChildrenCount(c) > 0;
  }

  public static boolean isOnlyVisibleComponent(Component c) {
    return c != null && c.isVisible() && getVisibleChildrenCount(c.getParent()) == 1;
  }

  public static boolean isOnlyVisibleComponents(Component[] c) {
    if (c != null && c.length > 0) {
      boolean visible = getVisibleChildrenCount(c[0].getParent()) == c.length;
      if (visible)
        for (int i = 0; i < c.length; i++)
          visible = visible && c[i].isVisible();
      return visible;
    }
    return false;
  }

  public static Component findFirstComponentOfType(Component comp, Class c) {
    if (c.isInstance(comp))
      return comp;

    if (comp instanceof Container) {
      Container container = (Container) comp;
      for (int i = 0; i < container.getComponentCount(); i++) {
        Component comp2 = findFirstComponentOfType(container.getComponent(i), c);
        if (comp2 != null)
          return comp2;
      }
    }
    return null;
  }

  public static boolean isFocusable(Component c) {
    return c.isFocusable() && c.isDisplayable() && c.isVisible() && c.isEnabled();
  }

  /**
   * Requests focus unless the component already has focus. For some weird
   * reason calling {@link Component#requestFocusInWindow()}when the
   * component is focus owner changes focus owner to another component!
   *
   * @param component the component to request focus for
   * @return true if the component has focus or probably will get focus,
   *         otherwise false
   */
  public static boolean requestFocus(Component component) {
    /*
     * System.out.println("Owner: " +
     * System.identityHashCode(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()) + ", " +
     * System.identityHashCode(component) + ", " +
     * (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() ==
     * component));
     */
    return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == component ||
           component.requestFocusInWindow();
  }

  /**
   * Requests focus for a component. If that's not possible it's
   * {@link FocusTraversalPolicy}is checked. If that doesn't work all it's
   * children is recursively checked with this method.
   *
   * @param component the component to request focus for
   * @return the component which has focus or probably will obtain focus, null
   *         if no component will receive focus
   */
  public static Component smartRequestFocus(Component component) {
    if (requestFocus(component))
      return component;

    if (component instanceof JComponent) {
      FocusTraversalPolicy policy = ((JComponent) component).getFocusTraversalPolicy();

      if (policy != null) {
        Component focusComponent = policy.getDefaultComponent((Container) component);

        if (focusComponent != null && requestFocus(focusComponent)) {
          return focusComponent;
        }
      }
    }

    if (component instanceof Container) {
      Component[] children = ((Container) component).getComponents();

      for (int i = 0; i < children.length; i++) {
        component = smartRequestFocus(children[i]);

        if (component != null)
          return component;
      }
    }

    return null;
  }

  /**
   * Calculates preferred max height for the given components without checking
   * isVisible.
   *
   * @param components Components to check
   * @return max height
   */
  public static int getPreferredMaxHeight(Component[] components) {
    int height = 0;
    for (int i = 0; i < components.length; i++) {
      int k = (int) components[i].getPreferredSize().getHeight();
      if (k > height)
        height = k;
    }
    return height;
  }

  /**
   * Calculates preferred max width for the given components without checking
   * isVisible.
   *
   * @param components Components to check
   * @return max width
   */
  public static int getPreferredMaxWidth(Component[] components) {
    int width = 0;
    for (int i = 0; i < components.length; i++) {
      int k = (int) components[i].getPreferredSize().getWidth();
      if (k > width)
        width = k;
    }
    return width;
  }

  public static void setAllOpaque(Container c, boolean opaque) {
    if (c instanceof JComponent) {
      ((JComponent) c).setOpaque(opaque);
      for (int i = 0; i < c.getComponentCount(); i++) {
        Component comp = c.getComponent(i);
        if (comp instanceof Container)
          setAllOpaque((Container) comp, opaque);
      }
    }
  }

  public static void validate(JComponent c) {
    c.revalidate();
  }

  public static void validate(Component c) {
    if (c instanceof JComponent)
      ((JComponent) c).revalidate();
    else
      c.validate();
  }
}