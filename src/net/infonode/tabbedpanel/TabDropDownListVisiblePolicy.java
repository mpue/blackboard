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


// $Id: TabDropDownListVisiblePolicy.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

import net.infonode.util.Enum;

/**
 * TabDropDownListVisiblePolicy tells the tabbed panel when to show a drop down
 * list of tabs.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @since ITP 1.1.0
 */
public final class TabDropDownListVisiblePolicy extends Enum {
  private static final long serialVersionUID = 1;

  /**
   * Never drop down list policy. This means that no drop down list will be shown
   * in the tabbed panel.
   */
  public static final TabDropDownListVisiblePolicy NEVER = new TabDropDownListVisiblePolicy(0, "Never");

  /**
   * More than one tab list policy. This means that a drop down list will be shown
   * if there are more than one tab in the tabbed panel.
   */
  public static final TabDropDownListVisiblePolicy MORE_THAN_ONE_TAB = new TabDropDownListVisiblePolicy(1,
                                                                                                        "More than One Tab");

  /**
   * Tabs not visible list policy. This means that a drop down list will be shown when
   * there are tabs are not entirely visible, i.e. scrolled out.
   */
  public static final TabDropDownListVisiblePolicy TABS_NOT_VISIBLE = new TabDropDownListVisiblePolicy(1,
                                                                                                       "Some Tabs Not Visible");

  private static final TabDropDownListVisiblePolicy[] DROP_DOWN_LIST_VISIBLE_POLICIES = new TabDropDownListVisiblePolicy[]{
    NEVER, MORE_THAN_ONE_TAB, TABS_NOT_VISIBLE};

  private TabDropDownListVisiblePolicy(int value, String name) {
    super(value, name);
  }

  /**
   * Gets the tab drop down list visible policies.
   *
   * @return the tab drop down list visible policies
   */
  public static TabDropDownListVisiblePolicy[] getDropDownListVisiblePolicies() {
    return (TabDropDownListVisiblePolicy[]) DROP_DOWN_LIST_VISIBLE_POLICIES.clone();
  }
}
