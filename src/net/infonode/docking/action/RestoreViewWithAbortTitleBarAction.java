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


// $Id: RestoreViewWithAbortTitleBarAction.java,v 1.3 2011-09-07 19:56:08 mpue Exp $

package net.infonode.docking.action;

import java.io.ObjectStreamException;

import javax.swing.Icon;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.internalutil.InternalDockingUtil;
import net.infonode.gui.icon.button.RestoreIcon;

/**
 * <p>
 * Restores a window using the {@link net.infonode.docking.DockingWindow#restoreWithAbort()} method.
 * </p>
 *
 * <p>
 * This action is only meant to be used as restore action on a button on a view's title
 * bar.
 * </p>
 *
 * @author johan
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class RestoreViewWithAbortTitleBarAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class.
   */
  public static final RestoreViewWithAbortTitleBarAction INSTANCE = new RestoreViewWithAbortTitleBarAction();

  private static final Icon icon = new RestoreIcon(InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE);

  private RestoreViewWithAbortTitleBarAction() {
  }

  public String getName() {
    return "Restore";
  }

  public boolean isPerformable(DockingWindow window) {
    return window != null && (window.isMinimized() || window.isMaximized() ||
                              (window.getWindowParent() != null && window.getWindowParent()
                                  .isMaximized() && window.getWindowParent().isRestorable())) && window.isRestorable();
  }

  public void perform(DockingWindow window) {
    try {
      if (window != null && window.isRestorable()) {
        if (window.isMaximized() || window.isMinimized())
          window.restoreWithAbort();
        else {
          if (window.getWindowParent() != null)
            window.getWindowParent().restoreWithAbort();
        }
      }
    }
    catch (OperationAbortedException e) {
// Ignore
    }
  }

  public Icon getIcon() {
    return icon;
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }
}
