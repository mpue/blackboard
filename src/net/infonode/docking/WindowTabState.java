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


// $Id: WindowTabState.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.docking;

import net.infonode.util.Enum;

/**
 * The states that a window tab can be in.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
final class WindowTabState extends Enum {
  private static final long serialVersionUID = 1L;

  /**
   * Normal state means that the tab is not highlighted or focused.
   */
  static final WindowTabState NORMAL = new WindowTabState(0, "Normal");

  /**
   * Highlighted state occurs when the tab is selected or otherwise highlighted.
   */
  static final WindowTabState HIGHLIGHTED = new WindowTabState(1, "Highlighted");

  /**
   * Focused state is when the window that the tab is connected to contains the focus owner.
   */
  static final WindowTabState FOCUSED = new WindowTabState(2, "Focused");

  private static final WindowTabState[] STATES = {NORMAL, HIGHLIGHTED, FOCUSED};

  WindowTabState(int value, String name) {
    super(value, name);
  }

  static WindowTabState[] getStates() {
    return (WindowTabState[]) STATES.clone();
  }

  static int getStateCount() {
    return STATES.length;
  }
}
