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


// $Id: CompoundHoverListener.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.gui.hover;


/**
 * CompoundHoverListener takes the two given hover listeners and calls the
 * first hover listener and then the second when the mouse is hovering.
 * When the mouse is no longer hovering, the second listener is called
 * and then the first listener is called.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class CompoundHoverListener implements HoverListener {
  private HoverListener firstListener;
  private HoverListener secondListener;

  /**
   * Creates a CompoundHoverListener
   *
   * @param firstListener  the first hover listener
   * @param secondListener the second hover listener
   */
  public CompoundHoverListener(HoverListener firstListener, HoverListener secondListener) {
    this.firstListener = firstListener;
    this.secondListener = secondListener;
  }

  /**
   * Gets the first hover listener
   *
   * @return the hover listener
   */
  public HoverListener getFirstListener() {
    return firstListener;
  }

  /**
   * Gets the second hover listener
   *
   * @return the hover listener
   */
  public HoverListener getSecondListener() {
    return secondListener;
  }

  public void mouseEntered(HoverEvent event) {
    firstListener.mouseEntered(event);
    secondListener.mouseEntered(event);
  }

  public void mouseExited(HoverEvent event) {
    secondListener.mouseExited(event);
    firstListener.mouseExited(event);
  }
}
