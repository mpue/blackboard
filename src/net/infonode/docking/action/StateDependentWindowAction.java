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


// $Id: StateDependentWindowAction.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.docking.action;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.util.DockingUtil;

/**
 * Performs different actions on a window depending on the state of the window.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.3.0
 */
public class StateDependentWindowAction extends DockingWindowAction {
  private static final long serialVersionUID = 1;

  /**
   * If the window is maximized or minimized it is restored, otherwise it is maximized.
   */
  public static final StateDependentWindowAction MAXIMIZE_RESTORE = new StateDependentWindowAction(
      MaximizeWindowAction.INSTANCE,
      RestoreParentWindowAction.INSTANCE,
      RestoreParentWindowAction.INSTANCE);

  /**
   * If the window is maximized or minimized it is restored, otherwise it is maximized. The operations
   * can be aborted by a {@link net.infonode.docking.DockingWindowListener}.
   *
   * @since IDW 1.4.0
   */
  public static final StateDependentWindowAction MAXIMIZE_RESTORE_WITH_ABORT = new StateDependentWindowAction(
      MaximizeWithAbortWindowAction.INSTANCE,
      RestoreParentWithAbortWindowAction.INSTANCE,
      RestoreParentWithAbortWindowAction.INSTANCE);

  private DockingWindowAction normalAction;
  private DockingWindowAction minimizedAction;
  private DockingWindowAction maximizedAction;

  /**
   * Constructor.
   *
   * @param normalAction    the action to perform if a window is in normal state
   * @param minimizedAction the action to perform if a window is minimized
   * @param maximizedAction the action to perform if a window is maximized
   */
  public StateDependentWindowAction(DockingWindowAction normalAction,
                                    DockingWindowAction minimizedAction,
                                    DockingWindowAction maximizedAction) {
    this.normalAction = normalAction;
    this.minimizedAction = minimizedAction;
    this.maximizedAction = maximizedAction;
  }

  public String getName() {
    return "State Dependent";
  }

  public boolean isPerformable(DockingWindow window) {
    return getActionProvider(window).isPerformable(window);
  }

  public void perform(DockingWindow window) {
    getActionProvider(window).perform(window);
  }

  private DockingWindowAction getActionProvider(DockingWindow window) {
    if (window.isMinimized())
      return minimizedAction;
    else {
      TabWindow tabWindow = DockingUtil.getTabWindowFor(window);
      return tabWindow != null && tabWindow.isMaximized() ? maximizedAction : normalAction;
    }
  }

}
