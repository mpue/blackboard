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


// $Id: TabLayoutPolicy.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

import net.infonode.util.Enum;

/**
 * TabLayoutPolicy defines how the tabs in a tabbed panel's tab area can be laid out.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 */
public final class TabLayoutPolicy extends Enum {
  private static final long serialVersionUID = -1345037155950998515L;

  /**
   * Scrolling layout policy. This means that the tabs are laid out in a line. The
   * line of tabs will be scrollable if not all tabs can fit into the visible part
   * of the tabbed panel's tab area at the same time.
   */
  public static final TabLayoutPolicy SCROLLING = new TabLayoutPolicy(0, "Scrolling");

  /**
   * Compression layout policy. This means that the tabs are laid out in a line. The
   * tabs will be downsized (compressed) so that they fit into the visible part of the
   * tab area.
   */
  public static final TabLayoutPolicy COMPRESSION = new TabLayoutPolicy(1, "Compression");

  /**
   * Array with all available layout policies.
   */
  public static final TabLayoutPolicy[] LAYOUT_POLICIES = new TabLayoutPolicy[]{SCROLLING, COMPRESSION};

  private TabLayoutPolicy(int value, String name) {
    super(value, name);
  }

  /**
   * Gets the tab layout policies.
   *
   * @return the tab layout policies
   * @since ITP 1.1.0
   */
  public static TabLayoutPolicy[] getLayoutPolicies() {
    return (TabLayoutPolicy[]) LAYOUT_POLICIES.clone();
  }
}
