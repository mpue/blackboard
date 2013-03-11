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


// $Id: DropInfo.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.docking.drop;

import java.awt.Point;

import net.infonode.docking.DockingWindow;

/**
 * <p>
 * Super class for all drop infos
 * </p>
 *
 * <p>
 * A drop info is passed on to a {@link net.infonode.docking.drop.DropFilter}
 * when a drag and drop operation is in progress. This makes it possible for
 * the drop filter to decide if a drop is accepted or.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class DropInfo {
  private DockingWindow window;
  private DockingWindow dropWindow;
  private Point point;

  DropInfo(DockingWindow window, DockingWindow dropWindow, Point point) {
    this.window = window;
    this.dropWindow = dropWindow;
    this.point = point;
  }

  /**
   * Returns the window that is beeing dragged, i.e. the window that could be
   * dropped.
   *
   * @return the window beeing dragged
   */
  public DockingWindow getWindow() {
    return window;
  }

  /**
   * Returns the window that is asked (via the {@link DropFilter} if a drop of
   * the dragged window is accepted.
   *
   * @return the window that is asked if a drop of the dragged window is accepted
   */
  public DockingWindow getDropWindow() {
    return dropWindow;
  }

  /**
   * Returns the current mouse point relative to the drop window.
   *
   * @return the mouse point relative to the drop window
   */
  public Point getPoint() {
    return point;
  }
}
