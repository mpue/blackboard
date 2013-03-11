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


// $Id: TabRemovedEvent.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

/**
 * TabRemovedEvent is an event that contains information about the tab that was
 * removed from a tabbed panel and the tabbed panel it was removed from.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see Tab
 */
public class TabRemovedEvent extends TabEvent {
  private TabbedPanel tabbedPanel;

  /**
   * Constructs a TabDragEvent
   *
   * @param source      the Tab ot TabbedPanel that is the source for this
   *                    event
   * @param tab         the Tab that was removed
   * @param tabbedPanel the TabbedPanel that the Tab was removed from
   */
  public TabRemovedEvent(Object source, Tab tab, TabbedPanel tabbedPanel) {
    super(source, tab);
    this.tabbedPanel = tabbedPanel;
  }

  /**
   * Gets the TabbedPanel the Tab was removed from
   *
   * @return the TabbedPanel the Tab was removed from
   */
  public TabbedPanel getTabbedPanel() {
    return tabbedPanel;
  }
}
