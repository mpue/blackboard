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


// $Id: TabListener.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

/**
 * <p>TabListener interface for receiving events from a TabbedPanel or a Tab.</p>
 *
 * <p>Adding a TabListener to a tabbed panel or a tab makes it possible to receive
 * events when a tab component is added, removed, moved, highlighted, dehighlighted,
 * selected, deselected, dragged, dropped or drag aborted.</p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see Tab
 */
public interface TabListener {
  /**
   * Called when a tab is added or inserted to a TabbedPanel
   *
   * @param event the event
   */
  void tabAdded(TabEvent event);

  /**
   * Called when a tab is removed from a TabbedPanel
   *
   * @param event the event
   */
  void tabRemoved(TabRemovedEvent event);

  /**
   * Called when a tab is dragged.
   *
   * @param event the event
   */
  void tabDragged(TabDragEvent event);

  /**
   * Called when a tab is dropped.
   *
   * @param event the event
   */
  void tabDropped(TabDragEvent event);

  /**
   * Called when an ongoing tab drag is aborted.
   *
   * @param event the event
   */
  void tabDragAborted(TabEvent event);

  /**
   * Called when a tab is selected
   *
   * @param event the event
   */
  void tabSelected(TabStateChangedEvent event);

  /**
   * <p>Called when a tab is deselected.</p>
   *
   * <p><strong>Note:</strong> The event contains information about the previously
   * selected tab and the current selected tab.</p>
   *
   * @param event the event
   */
  void tabDeselected(TabStateChangedEvent event);

  /**
   * Called when a tab is highlighted
   *
   * @param event the event
   */
  void tabHighlighted(TabStateChangedEvent event);

  /**
   * <p>Called when a tab is dehighlighted.</p>
   *
   * <p><strong>Note:</strong> The event contains information about the previously
   * highlighted tab and the current selected tab.</p>
   *
   * @param event the event
   */
  void tabDehighlighted(TabStateChangedEvent event);

  /**
   * Called when a tab is moved, i.e. dragged to another position in
   * the tab area
   *
   * @param event the event
   */
  void tabMoved(TabEvent event);
}
