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


// $Id: DockingWindowAction.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.action;

import java.io.Serializable;

import javax.swing.Icon;

import net.infonode.docking.DockingWindow;
import net.infonode.gui.action.SimpleAction;
import net.infonode.gui.icon.IconProvider;

/**
 * An action that can be performed on a {@link DockingWindow}. It has a name and an optional icon.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
abstract public class DockingWindowAction implements Serializable, IconProvider {
  private static final long serialVersionUID = 1;

  /**
   * Returns the name of this action.
   *
   * @return the name of this action
   */
  abstract public String getName();

  /**
   * Performs this action on a window.
   *
   * @param window the window on which to perform the action
   */
  abstract public void perform(DockingWindow window);

  /**
   * Returns true if this action is performable on a window.
   *
   * @param window the window on which the action will be performed
   * @return true if this action is performable on the window
   */
  abstract public boolean isPerformable(DockingWindow window);

  /**
   * Creates a simple action that performs this action on a window.
   *
   * @param window the window on which to perform the action
   * @return the action that performs this action on a window.
   */
  public SimpleAction getAction(final DockingWindow window) {
    return new SimpleAction() {
      public String getName() {
        return DockingWindowAction.this.getName();
      }

      public boolean isEnabled() {
        return DockingWindowAction.this.isPerformable(window);
      }

      public void perform() {
        DockingWindowAction.this.perform(window);
      }

      public Icon getIcon() {
        return DockingWindowAction.this.getIcon();
      }
    };
  }

  /**
   * Returns the optional icon of this action.
   *
   * @return the optional icon of this action, null if there is no icon
   */
  public Icon getIcon() {
    return null;
  }

  public String toString() {
    return getName();
  }

}
