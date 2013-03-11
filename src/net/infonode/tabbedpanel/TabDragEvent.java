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


// $Id: TabDragEvent.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * TabDragEvent is an mouseEvent that contains information about the tab that is
 * beeing dragged from a tabbed panel and a point specifying the mouse
 * coordinates.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TabbedPanel
 * @see Tab
 */
public class TabDragEvent extends TabEvent {
  private MouseEvent mouseEvent;

  /**
   * Constructs a TabDragEvent
   *
   * @param source the Tab or TabbedPanel that is the source for this
   *               mouseEvent
   * @param tab    the Tab that is being dragged
   * @param point  the mouse coordinates relative to the Tab that is being
   *               dragged
   * @deprecated Use {@link #TabDragEvent(Object, java.awt.event.MouseEvent)} instead.
   */
  public TabDragEvent(Object source, Tab tab, Point point) {
    this(source,
         new MouseEvent(tab, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, point.x, point.y, 0, false));
  }

  /**
   * Constructs a TabDragEvent
   *
   * @param source     the Tab or TabbedPanel that is the source for this
   * @param mouseEvent the mouse mouseEvent that triggered the drag, the event source should be the tab and the event
   *                   point should be relative to the tab
   * @since ITP 1.3.0
   */
  public TabDragEvent(Object source, MouseEvent mouseEvent) {
    super(source, (Tab) mouseEvent.getSource());
    this.mouseEvent = mouseEvent;
  }

  /**
   * Gets the mouse coordinates
   *
   * @return the mouse coordinats relative to the Tab that is beeing
   *         dragged
   */
  public Point getPoint() {
    return mouseEvent.getPoint();
  }

  /**
   * Returns the mouse event that triggered this drag. The event source is set to the tab and the event point is
   * relative to the tab.
   *
   * @return the mouse event that triggered this drag
   * @since ITP 1.3.0
   */
  public MouseEvent getMouseEvent() {
    return mouseEvent;
  }

}
