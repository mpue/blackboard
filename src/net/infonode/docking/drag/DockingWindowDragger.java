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


// $Id: DockingWindowDragger.java,v 1.3 2011-09-07 19:56:11 mpue Exp $
package net.infonode.docking.drag;

import java.awt.event.MouseEvent;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;

/**
 * Handles the drag and drop of a {@link DockingWindow}. Note the the drag operation MUST be terminated using either
 * {@link #abortDrag()} or {@link #dropWindow(MouseEvent)}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
public interface DockingWindowDragger {
  /**
   * Returns the {@link RootWindow} where the window can be dropped.
   *
   * @return the {@link RootWindow} where the window can be dropped
   */
  RootWindow getDropTarget();

  /**
   * The window that is dragged and dropped.
   *
   * @return the window that is dragged and dropped.
   */
  DockingWindow getDragWindow();

  /**
   * Drags the window to a new location. The location is relative to the {@link RootWindow} in where it should be
   * dropped, see {@link #getDropTarget()}.
   *
   * @param mouseEvent the mouse event that caused the drag
   */
  void dragWindow(MouseEvent mouseEvent);

  /**
   * Aborts this drag operation.
   */
  void abortDrag();

  /**
   * Drops the window at the specified location.
   *
   * @param mouseEvent the mouse event that caused the drop
   */
  void dropWindow(MouseEvent mouseEvent);
}
