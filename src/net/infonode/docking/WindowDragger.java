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


// $Id: WindowDragger.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Window;
import java.awt.dnd.DragSource;
import java.awt.event.MouseEvent;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import net.infonode.docking.drag.DockingWindowDragger;
import net.infonode.docking.internalutil.DropAction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
class WindowDragger implements DockingWindowDragger {
  private DockingWindow dragWindow;
  private DropAction dropAction;
  private RootWindow rootWindow;

  WindowDragger(DockingWindow dragWindow) {
    this(dragWindow, dragWindow.getRootWindow());
  }

  WindowDragger(DockingWindow dragWindow, RootWindow rootWindow) {
    this.dragWindow = dragWindow;
    this.rootWindow = rootWindow;

    rootWindow.internalStartDrag(dragWindow);
  }

  public DockingWindow getDragWindow() {
    return dragWindow;
  }

  public RootWindow getDropTarget() {
    return rootWindow;
  }

  void undoDrag(DropAction newAction) {
    if (dropAction != null) {
      dropAction.clear(dragWindow, newAction);
      dropAction = null;
    }
  }

  private void stopDrag() {
    rootWindow.stopDrag();
  }

  public void abortDrag() {
    stopDrag();
    undoDrag(null);
  }

  public void dropWindow(MouseEvent mouseEvent) {
    stopDrag();

    if (dropAction == null)
      dropAction = dragWindow.getDefaultDropAction();

    if (dragWindow != null && dropAction != null) {
      dropAction.execute(dragWindow, mouseEvent);

      Component c = dragWindow.getTopLevelAncestor();
      if (c != null && c instanceof Window)
        ((Window) c).toFront();

      FocusManager.focusWindow(dragWindow);
    }
  }

  public void dragWindow(MouseEvent mouseEvent) {
    JRootPane root = rootWindow.getCurrentDragRootPane();
    
    Point point = SwingUtilities.convertPoint((Component) mouseEvent.getSource(),
                                              mouseEvent.getPoint(),
                                              root);

    if (root != rootWindow.getRootPane() && !root.contains(point)) {
      rootWindow.setCurrentDragRootPane(null);
      root = rootWindow.getCurrentDragRootPane();
      point = SwingUtilities.convertPoint((Component) mouseEvent.getSource(),
                                          mouseEvent.getPoint(),
                                          root);
    }

    DockingWindow dropWindow = getDeepestWindowAt(root, point.x, point.y);

    while (dropWindow != null && dropWindow.getWindowParent() != null &&
           !(dropWindow/*.getWindowParent()*/ instanceof FloatingWindow)) {
      Point p2 = SwingUtilities.convertPoint(root, point, dropWindow.getWindowParent());

      if (!dropWindow.getWindowParent().contains(p2))
        break;

      dropWindow = dropWindow.getWindowParent();
    }

    DropAction da = dropWindow != null ?
                    dropWindow.acceptDrop(SwingUtilities.convertPoint(root, point, dropWindow), dragWindow) : null;

    undoDrag(da);

    Cursor cursor = DragSource.DefaultMoveDrop;
    if (da == null)
      cursor = DragSource.DefaultMoveNoDrop;

    if (dropWindow == null && dragWindow.getWindowProperties().getUndockOnDropEnabled())
      cursor = DragSource.DefaultMoveDrop;

    rootWindow.setDragCursor(cursor);
    rootWindow.setDragText(da == null || da.showTitle() ? point : null, dragWindow.getTitle());
    dropAction = da;

    if (dropAction == null)
      rootWindow.setDragRectangle(null);
  }

  private DockingWindow getDeepestWindowAt(Component component, int x, int y) {
    if (component == null || !component.isVisible() || !component.contains(x, y))
      return null;

    if (component instanceof Container) {
      Component[] components = ((Container) component).getComponents();

      for (int i = 0; i < components.length; i++) {
        DockingWindow w = getDeepestWindowAt(components[i], x - components[i].getX(), y - components[i].getY());

        if (w != null)
          return w;
      }
    }

    if (component instanceof DockingWindow) {
      DockingWindow w = (DockingWindow) component;
      return w.getRootWindow() == rootWindow ? w : null;
    }
    else
      return null;
  }
}
