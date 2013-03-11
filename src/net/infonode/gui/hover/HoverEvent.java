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


// $Id: HoverEvent.java,v 1.3 2011-09-07 19:56:11 mpue Exp $

package net.infonode.gui.hover;

import java.awt.Component;

/**
 * HoverEvent contains information about a component hovered by the
 * mouse
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class HoverEvent {
  private Component source;

  /**
   * Creates a HoverEvent
   *
   * @param source the hoverable component that is the source for this event
   */
  public HoverEvent(Component source) {
    this.source = source;
  }

  /**
   * Gets the hoverable component that is the source for this event
   *
   * @return the hoverable component
   */
  public Component getSource() {
    return source;
  }
}