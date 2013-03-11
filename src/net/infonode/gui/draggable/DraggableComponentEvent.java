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


// $Id: DraggableComponentEvent.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.gui.draggable;

import java.awt.event.MouseEvent;

public class DraggableComponentEvent {
  public static final int TYPE_UNDEFINED = -1;
  public static final int TYPE_MOVED = 0;
  public static final int TYPE_PRESSED = 1;
  public static final int TYPE_RELEASED = 2;
  public static final int TYPE_ENABLED = 3;
  public static final int TYPE_DISABLED = 4;

  private DraggableComponent source;
  private int type = TYPE_UNDEFINED;
  private MouseEvent mouseEvent;

  public DraggableComponentEvent(DraggableComponent source) {
    this(source, null);
  }

  public DraggableComponentEvent(DraggableComponent source, MouseEvent mouseEvent) {
    this(source, TYPE_UNDEFINED, mouseEvent);
  }

  public DraggableComponentEvent(DraggableComponent source, int type) {
    this(source, type, null);
  }

  public DraggableComponentEvent(DraggableComponent source, int type, MouseEvent mouseEvent) {
    this.source = source;
    this.type = type;
    this.mouseEvent = mouseEvent;
  }

  public DraggableComponent getSource() {
    return source;
  }

  public int getType() {
    return type;
  }

  public MouseEvent getMouseEvent() {
    return mouseEvent;
  }

}
