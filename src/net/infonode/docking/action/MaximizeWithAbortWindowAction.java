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


// $Id: MaximizeWithAbortWindowAction.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.action;

import java.io.ObjectStreamException;

import javax.swing.Icon;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.TabWindow;
import net.infonode.docking.internalutil.InternalDockingUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.gui.icon.button.MaximizeIcon;

/**
 * Maximizes a {@link TabWindow}. Finds the parent {@link TabWindow} if the window this action is performed on is not
 * a {@link TabWindow}. The action calls {@link net.infonode.docking.DockingWindow#maximizeWithAbort()}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public final class MaximizeWithAbortWindowAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class.
   */
  public static final MaximizeWithAbortWindowAction INSTANCE = new MaximizeWithAbortWindowAction();

  private static final Icon icon = new MaximizeIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

  private MaximizeWithAbortWindowAction() {
  }

  public Icon getIcon() {
    return icon;
  }

  public String getName() {
    return "Maximize";
  }

  public boolean isPerformable(DockingWindow window) {
    if (!window.isMaximizable())
      return false;

    TabWindow tabWindow = DockingUtil.getTabWindowFor(window);
    return tabWindow != null && !tabWindow.isMaximized() && tabWindow.isMaximizable();
  }

  public void perform(DockingWindow window) {
    try {
      TabWindow tabWindow = DockingUtil.getTabWindowFor(window);

      if (tabWindow != null && !tabWindow.isMaximized() && tabWindow.isMaximizable())
        tabWindow.maximizeWithAbort();
    }
    catch (OperationAbortedException e) {
      // Ignore
    }
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
