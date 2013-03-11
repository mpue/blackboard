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


// $Id: SimpleAction.java,v 1.3 2011-09-07 19:56:11 mpue Exp $
package net.infonode.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.infonode.gui.icon.IconProvider;

/**
 * An action with an icon and a title.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
abstract public class SimpleAction implements IconProvider {
  abstract public String getName();

  abstract public void perform();

  abstract public boolean isEnabled();

  protected SimpleAction() {
  }

  /**
   * Converts this action into a Swing {@link Action}.
   *
   * @return the Swing {@link Action}
   */
  public Action toSwingAction() {
    AbstractAction action = new AbstractAction(getName(), getIcon()) {
      public void actionPerformed(ActionEvent e) {
        perform();
      }
    };
    action.setEnabled(isEnabled());
    return action;
  }
}
