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


// $Id: DraggableComponentBoxEvent.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.draggable;

import java.awt.Point;

public class DraggableComponentBoxEvent {
  private DraggableComponentBox source;
  private DraggableComponent draggableComponent;
  private DraggableComponent oldDraggableComponent;
  private DraggableComponentEvent draggableComponentEvent;
  private Point draggableComponentBoxPoint;

  public DraggableComponentBoxEvent(DraggableComponentBox source) {
    this(source, null);
  }

  public DraggableComponentBoxEvent(DraggableComponentBox source, DraggableComponent component) {
    this(source, component, null, null);
  }

  public DraggableComponentBoxEvent(DraggableComponentBox source,
                                    DraggableComponent component,
                                    DraggableComponentEvent event) {
    this(source, component, event, null);
  }

  public DraggableComponentBoxEvent(DraggableComponentBox source,
                                    DraggableComponent component,
                                    DraggableComponentEvent event,
                                    Point point) {
    this.source = source;
    this.draggableComponent = component;
    this.draggableComponentEvent = event;
    this.draggableComponentBoxPoint = point;
  }

  public DraggableComponentBoxEvent(DraggableComponentBox source,
                                    DraggableComponent component,
                                    DraggableComponent oldDraggableComponent) {
    this(source, component);
    this.oldDraggableComponent = oldDraggableComponent;
  }

  public DraggableComponentBox getSource() {
    return source;
  }

  public DraggableComponent getDraggableComponent() {
    return draggableComponent;
  }

  public DraggableComponent getOldDraggableComponent() {
    return oldDraggableComponent;
  }

  public Point getDraggableComponentBoxPoint() {
    return draggableComponentBoxPoint;
  }

  public DraggableComponentEvent getDraggableComponentEvent() {
    return draggableComponentEvent;
  }
}
