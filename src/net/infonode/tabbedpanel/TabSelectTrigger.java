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


// $Id: TabSelectTrigger.java,v 1.2 2011-08-26 15:10:41 mpue Exp $

package net.infonode.tabbedpanel;

import net.infonode.util.Enum;

/**
 * TabSelectTrigger defines what triggers a tab selection in a TabbedPanel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @since ITP 1.1.0
 */
public final class TabSelectTrigger extends Enum {
  private static final long serialVersionUID = 1L;

  /**
   * Mouse press select trigger. This means that a tab will be selected on
   * mouse pressed (button down).
   */
  public static final TabSelectTrigger MOUSE_PRESS = new TabSelectTrigger(0, "Mouse Press");

  /**
   * Mouse release select trigger. This means that a tab will be selected on
   * mouse release (button up).
   */
  public static final TabSelectTrigger MOUSE_RELEASE = new TabSelectTrigger(1, "Mouse Release");

  private static final TabSelectTrigger[] SELECT_TRIGGERS = {MOUSE_PRESS, MOUSE_RELEASE};

  private TabSelectTrigger(int value, String name) {
    super(value, name);
  }

  /**
   * Gets the tab select triggers.
   *
   * @return the tab select triggers
   */
  public static TabSelectTrigger[] getSelectTriggers() {
    return (TabSelectTrigger[]) SELECT_TRIGGERS.clone();
  }
}