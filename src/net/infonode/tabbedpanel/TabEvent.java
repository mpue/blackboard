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


// $Id: TabEvent.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

import java.util.EventObject;

/**
 * TabEvent is the root event for all tab events. It contains
 * information about the source, i.e. the object (tab or tabbedPanel)
 * that generated this event and the tab that was affected by this event.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabListener
 * @see TabbedPanel
 * @see Tab
 */
public class TabEvent extends EventObject {
  private Tab tab;

  /**
   * Constructs a TabEvent
   *
   * @param source the Tab or TabbedPanel that is the source for this
   *               event
   * @param tab    the tab on which the event occurred
   */
  TabEvent(Object source, Tab tab) {
    super(source);
    this.tab = tab;
  }

  /**
   * Gets the tab that is the source for this event
   *
   * @return the Tab affected by this event or null if no tab
   *         was affected
   */
  public Tab getTab() {
    return tab;
  }
}
