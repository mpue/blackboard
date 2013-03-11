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


// $Id: AcceptAllDropFilter.java,v 1.2 2011-08-26 15:10:45 mpue Exp $
package net.infonode.docking.drop;

/**
 * A {@link net.infonode.docking.drop.DropFilter} that will accept drop of any
 * window.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.4.0
 */
public class AcceptAllDropFilter implements DropFilter {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class
   */
  public static AcceptAllDropFilter INSTANCE = new AcceptAllDropFilter();

  private AcceptAllDropFilter() {
  }

  public boolean acceptDrop(DropInfo dropInfo) {
    return true;
  }
}
