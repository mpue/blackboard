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


// $Id: DropFilter.java,v 1.2 2011-08-26 15:10:45 mpue Exp $
package net.infonode.docking.drop;

/**
 * <p>
 * Interface for filtering drops when a drag and drop is in progress.
 * </p>
 *
 * <p>
 * There are 4 kinds of drop types, see
 * {@link net.infonode.docking.drop.SplitDropInfo},
 * {@link net.infonode.docking.drop.ChildDropInfo},
 * {@link net.infonode.docking.drop.InteriorDropInfo} and
 * {@link net.infonode.docking.drop.InsertTabDropInfo}.
 * </p>
 *
 * <p>
 * A drop filter is used to filter drops. The filter may decide if a
 * drop of a window into another window is to be accepted or not. This
 * makes it possible to tailor the drop behavior of a
 * {@link net.infonode.docking.DockingWindow}. The window (called drop window)
 * into which another window is beeing dropped is asked if it will accept a
 * drop of that window. The filter is asked continuously during a drag operation
 * i.e. it may be asked many times during a drag operation.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.4.0
 */
public interface DropFilter {

  /**
   * Return true if the drop should be accepted, otherwise false.
   *
   * @param dropInfo information about the current drop
   * @return true if drop is to be accepted, otherwise false
   */
  boolean acceptDrop(DropInfo dropInfo);
}
