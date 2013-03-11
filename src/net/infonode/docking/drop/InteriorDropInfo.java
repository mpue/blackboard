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


// $Id: InteriorDropInfo.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.docking.drop;

import java.awt.Point;

import net.infonode.docking.DockingWindow;

/**
 * <p>
 * Information about a drop into a window.
 * </p>
 *
 * <p>
 * This drop type is performed on a {@link net.infonode.docking.RootWindow}
 * or a {@link net.infonode.docking.FloatingWindow} when they don't have any
 * top-level windows i.e. they appear empty. It is also perfomed on a
 * {@link net.infonode.docking.SplitWindow} when the drop will result in a
 * replace of the split window with a tab window containing the left window,
 * right window and the window that was dropped.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class InteriorDropInfo extends DropInfo {
  public InteriorDropInfo(DockingWindow window, DockingWindow dropWindow, Point point) {
    super(window, dropWindow, point);
  }
}
