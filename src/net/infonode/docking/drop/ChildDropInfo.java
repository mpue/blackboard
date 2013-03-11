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


// $Id: ChildDropInfo.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.docking.drop;

import java.awt.Point;

import net.infonode.docking.DockingWindow;

/**
 * <p>
 * Information about an ongoing child drop i.e. when a child window
 * of the drop window is asked if it accepts the drop.
 * </p>
 *
 * <p>
 * A drop is started at the root of the window tree and then passed
 * down the tree to the appropriate child. Rejecting a child drop
 * makes it possible to disable all types of drops in that subtree
 * of the drop window.
 * </p>
 *
 * <p>
 * A child drop can be performed on windows that can have children,
 * i.e. {@link net.infonode.docking.RootWindow},
 * {@link net.infonode.docking.SplitWindow},
 * {@link net.infonode.docking.TabWindow},
 * {@link net.infonode.docking.WindowBar} or
 * {@link net.infonode.docking.FloatingWindow}.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class ChildDropInfo extends DropInfo {
  private DockingWindow childWindow;

  public ChildDropInfo(DockingWindow window, DockingWindow dropWindow, Point point, DockingWindow childWindow) {
    super(window, dropWindow, point);
    this.childWindow = childWindow;
  }

  /**
   * Returns the current child window in the drop window that will
   * be asked if it accepts any type of drops.
   *
   * @return the child window
   */
  public DockingWindow getChildWindow() {
    return childWindow;
  }
}
