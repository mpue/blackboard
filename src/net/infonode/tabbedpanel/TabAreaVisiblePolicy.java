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


// $Id: TabAreaVisiblePolicy.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

import net.infonode.util.Enum;

/**
 * TabAreaVisiblePolicy defines the visibility policies for the tab area of a tabbed panel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @since ITP 1.4.0
 */
public class TabAreaVisiblePolicy extends Enum {
  private static final long serialVersionUID = 1L;

  /**
   * Always visible policy. This means that the tab area is always visible.
   */
  public static final TabAreaVisiblePolicy ALWAYS = new TabAreaVisiblePolicy(0, "Always");

  /**
   * Never visible policy. This means that the tab area is never visible.
   */
  public static final TabAreaVisiblePolicy NEVER = new TabAreaVisiblePolicy(1, "Never");

  /**
   * Tabs exist visible policy. This means that the tab area will only be visible if it contains tabs.
   */
  public static final TabAreaVisiblePolicy TABS_EXIST = new TabAreaVisiblePolicy(2, "Tabs Exist in Tab Area");

  /**
   * More than one visible policy. This means that the tab area is visible when the tabbed
   * panel contains more than one tab.
   */
  public static final TabAreaVisiblePolicy MORE_THAN_ONE_TAB = new TabAreaVisiblePolicy(3, "More than One Tab");

  private static final TabAreaVisiblePolicy[] VISIBLE_POLICIES = new TabAreaVisiblePolicy[]{ALWAYS, NEVER, TABS_EXIST, MORE_THAN_ONE_TAB};

  private TabAreaVisiblePolicy(int value, String name) {
    super(value, name);
  }

  /**
   * Gets the tab area visible policies.
   *
   * @return the tab layout policies
   */
  public static TabAreaVisiblePolicy[] getVisiblePolicies() {
    return (TabAreaVisiblePolicy[]) VISIBLE_POLICIES.clone();
  }
}
