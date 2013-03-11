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


// $Id: TabStateChangedEvent.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

/**
 * <p>TabStateChangedEvent is a state changed event. A change could mean that the
 * selected tab has been deselcted and another tab has been selected.</p>
 *
 * <p>Example:  Tab 1 is the selected tab. The user selects tab 2 and tab 1 will be
 * deselected. A change event will then be triggered where tab 1 will
 * be the previous tab (getPreviousTab()) and tab 2 will be the curent
 * tab (getCurrentTab()).</p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabListener
 * @see TabbedPanel
 * @see Tab
 */
public class TabStateChangedEvent extends TabEvent {
  private TabbedPanel tabbedPanel;
  private Tab previousTab;
  private Tab currentTab;

  /**
   * Constructs a TabStateChangedEvent
   *
   * @param source      the tabbed panel or tab that is the source for this
   *                    event
   * @param tabbedPanel the tabbep panel in which the state change occured
   * @param tab         the tab that is effectd by this event
   * @param previousTab the tab that was previously in this state
   * @param currentTab  the tab that is now in this state
   */
  public TabStateChangedEvent(Object source, TabbedPanel tabbedPanel, Tab tab, Tab previousTab, Tab currentTab) {
    super(source, tab);
    this.tabbedPanel = tabbedPanel;
    this.previousTab = previousTab;
    this.currentTab = currentTab;
  }

  /**
   * Gets the TabbedPanel in which the state change occured
   *
   * @return the TabbedPanel in which the state change occured
   */
  public TabbedPanel getTabbedPanel() {
    return tabbedPanel;
  }

  /**
   * Gets the previous Tab
   *
   * @return the previous Tab before the change or null if no previous tab was in
   *         that state before the change
   */
  public Tab getPreviousTab() {
    return previousTab;
  }

  /**
   * Gets the current Tab
   *
   * @return the current Tab after the change or null if no current tab is in the
   *         that state after the change
   */
  public Tab getCurrentTab() {
    return currentTab;
  }
}
