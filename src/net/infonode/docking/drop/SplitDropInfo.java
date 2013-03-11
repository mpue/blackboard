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


// $Id: SplitDropInfo.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.docking.drop;

import java.awt.Point;

import net.infonode.docking.DockingWindow;
import net.infonode.util.Direction;

/**
 * <p>
 * Information about an ongoing split drop i.e. a drop that would result
 * in a split of the drop window.
 * </p>
 *
 * <p>
 * A split drop can be performed on a {@link net.infonode.docking.SplitWindow},
 * {@link net.infonode.docking.TabWindow} or a {@link net.infonode.docking.View}.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class SplitDropInfo extends DropInfo {
  private Direction splitDirection;

  public SplitDropInfo(DockingWindow window, DockingWindow dropWindow, Point point, Direction splitDirection) {
    super(window, dropWindow, point);
    this.splitDirection = splitDirection;
  }

  /**
   * Returns the current split direction i.e. in what direction the drop window
   * should be split.
   *
   * @return the split direction
   */
  public Direction getSplitDirection() {
    return splitDirection;
  }
}
