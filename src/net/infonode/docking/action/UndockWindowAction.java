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


// $Id: UndockWindowAction.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.action;

import java.awt.Point;
import java.io.ObjectStreamException;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.FloatingWindow;
import net.infonode.docking.internalutil.InternalDockingUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.gui.icon.button.UndockIcon;

/**
 * Undocks a window using the {@link DockingWindow#undock(Point)} method.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class UndockWindowAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class
   */
  public static final UndockWindowAction INSTANCE = new UndockWindowAction();

  private static final Icon icon = new UndockIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

  private UndockWindowAction() {
  }

  public Icon getIcon() {
    return icon;
  }

  public String getName() {
    return "Undock";
  }

  public boolean isPerformable(DockingWindow window) {
    if (window.isUndockable()) {
      FloatingWindow fw = DockingUtil.getFloatingWindowFor(window);
      return fw == null || (fw.getChildWindowCount() > 0 && fw.getChildWindow(0) != window && fw.getChildWindow(0)
          .getChildWindowCount() > 1);
    }

    return false;
  }

  public void perform(DockingWindow window) {
    if (isPerformable(window)) {
      Point p = window.getLocation();
      SwingUtilities.convertPointToScreen(p, window.getParent());
      window.undock(p);
    }
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }
}
