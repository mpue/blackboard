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


// $Id: DockingUtil.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.util;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.FloatingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.internalutil.InternalDockingUtil;

/**
 * Class that contains utility methods for docking windows.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public final class DockingUtil {
  private DockingUtil() {
  }

  /**
   * Creates a root window with support for view serialization and popup menues.
   * All the views are added to a tab window which is placed in the root window.
   *
   * @param views                 the views that can be shown inside the root window
   * @param createWindowPopupMenu true if a standard window popup menu should be created
   * @return the created root window
   */
  public static RootWindow createRootWindow(AbstractViewMap views, boolean createWindowPopupMenu) {
    return createRootWindow(views, views, createWindowPopupMenu);
  }

  /**
   * <p>
   * Creates a root window with support for view serialization, popup menues and support for heavy
   * weight components inside the views.
   * </p>
   *
   * <p>
   * All the views are added to a tab window which is placed in the root window.
   * </p>
   *
   * @param views                 the views that can be shown inside the root window
   * @param createWindowPopupMenu true if a standard window popup menu should be created
   * @return the created root window
   * @since IDW 1.4.0
   */
  public static RootWindow createHeavyweightSupportedRootWindow(AbstractViewMap views, boolean createWindowPopupMenu) {
    return createRootWindow(true, views, views, createWindowPopupMenu);
  }

  /**
   * Creates a root window with support for view serialization and popup menues.
   * All the views are added to a tab window which is placed in the root window.
   *
   * @param views                 contains all the static views
   * @param viewSerializer        the view serializer used in the created {@link RootWindow}
   * @param createWindowPopupMenu true if a standard window popup menu should be created
   * @return the created root window
   */
  public static RootWindow createRootWindow(AbstractViewMap views,
                                            ViewSerializer viewSerializer,
                                            boolean createWindowPopupMenu) {

    return createRootWindow(false, views, viewSerializer, createWindowPopupMenu);
  }

  /**
   * <p>
   * Creates a root window with support for view serialization, popup menues and support for
   * heavyweight components inside the views.
   * </p>
   *
   * <p>
   * All the views are added to a tab window which is placed in the root window.
   * </p>
   *
   * @param views                 contains all the static views
   * @param viewSerializer        the view serializer used in the created {@link RootWindow}
   * @param createWindowPopupMenu true if a standard window popup menu should be created
   * @return the created root window
   * @since IDW 1.4.0
   */
  public static RootWindow createHeavyweightSupportedRootWindow(AbstractViewMap views,
                                                                ViewSerializer viewSerializer,
                                                                boolean createWindowPopupMenu) {

    return createRootWindow(true, views, viewSerializer, createWindowPopupMenu);
  }

  private static RootWindow createRootWindow(boolean heavyweightSupport,
                                             AbstractViewMap views,
                                             ViewSerializer viewSerializer,
                                             boolean createWindowPopupMenu) {
    TabWindow tabWindow = new TabWindow();

    for (int i = 0; i < views.getViewCount(); i++)
      tabWindow.addTab(views.getViewAtIndex(i));
    tabWindow.setSelectedTab(0);
    RootWindow rootWindow = new RootWindow(heavyweightSupport, viewSerializer, tabWindow);

    if (createWindowPopupMenu)
      rootWindow.setPopupMenuFactory(WindowMenuUtil.createWindowMenuFactory(views, true));

    return rootWindow;
  }

  /**
   * Returns true if <tt>ancestor</tt> is an ancestor of <tt>child</tt> or the windows are the same.
   *
   * @param ancestor the ancestor window
   * @param child    the child window
   * @return true if <tt>ancestor</tt> is an ancestor of <tt>child</tt> or the windows are the same
   */
  public static boolean isAncestor(DockingWindow ancestor, DockingWindow child) {
    return child != null && (ancestor == child || isAncestor(ancestor, child.getWindowParent()));
  }

  /**
   * <p>
   * Adds a window inside a root window. The following methods are tried in order:
   * </p>
   * <ol>
   * <li>If the window already is added inside the root window nothing happens.</li>
   * <li>The window is restored to it's last location if that location is inside the root window.</li>
   * <li>The window is added inside the root window.</li>
   * </ol>
   *
   * @param window     the window to add
   * @param rootWindow the root window in which to add it
   * @since IDW 1.1.0
   */
  public static void addWindow(DockingWindow window, RootWindow rootWindow) {
    if (rootWindow == null || window.getRootWindow() == rootWindow)
      return;

    if (window.getRootWindow() == null) {
      window.restore();

      if (window.getRootWindow() == rootWindow)
        return;
    }

    InternalDockingUtil.addToRootWindow(window, rootWindow);
  }

  /**
   * Returns the {@link TabWindow} for a window. This is either the window itself or the parent window.
   *
   * @param window the window
   * @return the {@link TabWindow} for the window
   * @since IDW 1.3.0
   */
  public static TabWindow getTabWindowFor(DockingWindow window) {
    return window instanceof TabWindow ? (TabWindow) window :
      window.getWindowParent() != null && window.getWindowParent() instanceof TabWindow ?
                                                                                         (TabWindow) window.getWindowParent() :
                                                                                           null;
  }

  /**
   * Returns the {@link FloatingWindow} for a window if the window is undocked.
   *
   * @param window the window
   * @return the {@link FloatingWindow} for the window or null if the window is not undocked
   * @since IDW 1.4.0
   */
  public static FloatingWindow getFloatingWindowFor(DockingWindow window) {
    if (window == null)
      return null;

    if (!window.isUndocked())
      return null;

    while (window != null && !(window instanceof FloatingWindow))
      window = window.getWindowParent();

    return (FloatingWindow) window;
  }
}
