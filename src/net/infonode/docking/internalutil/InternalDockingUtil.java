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


// $Id: InternalDockingUtil.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.docking.internalutil;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JPopupMenu;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.action.DockingWindowAction;
import net.infonode.docking.properties.WindowTabButtonProperties;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.util.IntList;
import net.infonode.util.Printer;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class InternalDockingUtil {
  private InternalDockingUtil() {
  }

  public static final int DEFAULT_BUTTON_ICON_SIZE = 10;

  public static void getViews(DockingWindow window, ArrayList views) {
    if (window == null)
      return;
    else if (window instanceof View)
      views.add(window);
    else {
      for (int i = 0; i < window.getChildWindowCount(); i++)
        getViews(window.getChildWindow(i), views);
    }
  }

  public static IntList getWindowPath(DockingWindow window) {
    return getWindowPath(window, IntList.EMPTY_LIST);
  }

  /**
   * Returns the window located at <tt>windowPath</tt>.
   *
   * @param relativeToWindow the window the path is relative to
   * @param windowPath       the window path
   * @return the window located at <tt>windowPath</tt>
   */
  public static DockingWindow getWindow(DockingWindow relativeToWindow, IntList windowPath) {
    return windowPath.isEmpty() ?
                                 relativeToWindow :
                                   windowPath.getValue() >= relativeToWindow.getChildWindowCount() ? null :
                                     getWindow(relativeToWindow.getChildWindow(windowPath.getValue()), windowPath.getNext());
  }

  private static IntList getWindowPath(DockingWindow window, IntList tail) {
    DockingWindow parent = window.getWindowParent();
    return parent == null ? tail : getWindowPath(parent, new IntList(parent.getChildWindowIndex(window), tail));
  }

  public static void addDebugMenuItems(JPopupMenu menu, final DockingWindow window) {
    menu.add("Dump Tree").addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dump(window, new Printer());
      }
    });
  }

  public static void dump(DockingWindow window, Printer printer) {
    DockingWindow parent = window.getWindowParent();

    String clName = window.getClass().getName();

    printer.println(clName.substring(clName.lastIndexOf('.') + 1) + ", " +
        System.identityHashCode(window) + " (" +
        (parent == null ? "null" : String.valueOf(System.identityHashCode(parent))) + "), '" +
        window.getTitle() + "', " +
        (window.isVisible() ? "visible" : "not visible") + ", " +
        (window.isMaximized() ? "maximized" : "not maximized") + ", " +
        (window.getChildWindowCount() > 0 ? ":" : ""));

    if (window.getChildWindowCount() > 0) {
      printer.beginSection();

      for (int i = 0; i < window.getChildWindowCount(); i++) {
        if (window.getChildWindow(i) == null)
          printer.println("null");
        else
          dump(window.getChildWindow(i), printer);
      }

      printer.endSection();
    }
  }

  public static RootWindow createInnerRootWindow(View[] views) {
    RootWindow rootWindow = DockingUtil.createRootWindow(new ViewMap(views), true);
    rootWindow.getRootWindowProperties().getWindowAreaProperties().setBackgroundColor(null);
    rootWindow.getRootWindowProperties().getWindowAreaShapedPanelProperties().setComponentPainter(null);
    rootWindow.getRootWindowProperties().getComponentProperties().setBackgroundColor(null);
    rootWindow.getRootWindowProperties().getComponentProperties().setBorder(null);
    //rootWindow.getRootWindowProperties().getWindowAreaProperties().setBorder(new LineBorder(Color.GRAY));
    return rootWindow;
  }

  public static boolean updateButtons(ButtonInfo[] buttonInfos,
                                      AbstractButton[] buttons,
                                      Container container,
                                      DockingWindow window,
                                      PropertyMap map,
                                      Map changes) {
    //    DockingWindow window = w.getOptimizedWindow();
    boolean updateContainer = false;

    for (int i = 0; i < buttonInfos.length; i++) {
      WindowTabButtonProperties p = new WindowTabButtonProperties(buttonInfos[i].getProperty().get(map));
      DockingWindowAction action = p.getAction();
      Map propertyChanges = changes == null ? null : (Map) changes.get(p.getMap());
      //      boolean v = p.isVisible();
      //      boolean b = action != null && action.isPerformable(window);
      boolean visible = p.isVisible() && action != null && action.getAction(window).isEnabled();

      if ((buttons[i] == null || (propertyChanges != null && propertyChanges.containsKey(
          WindowTabButtonProperties.FACTORY))) &&
          p.getFactory() != null &&
          action != null) {
        buttons[i] = p.getFactory().createButton(window);
        buttons[i].setFocusable(false);
        buttons[i].addActionListener(action.getAction(window).toSwingAction());
        updateContainer = true;
      }

      if (buttons[i] != null) {
        buttons[i].setToolTipText(p.getToolTipText());
        buttons[i].setIcon(p.getIcon());
        buttons[i].setVisible(visible);
      }
    }

    if (updateContainer && container != null) {
      container.removeAll();

      for (int j = 0; j < buttonInfos.length; j++) {
        if (buttons[j] != null)
          container.add(buttons[j]);
      }
    }

    return updateContainer;
  }

  public static void addToRootWindow(DockingWindow window, RootWindow rootWindow) {
    if (rootWindow == null)
      return;

    DockingWindow w = rootWindow.getWindow();

    if (w == null)
      rootWindow.setWindow(window);
    else if (w instanceof TabWindow)
      ((TabWindow) w).addTab(window);
    else
      rootWindow.setWindow(new TabWindow(new DockingWindow[]{w, window}));
  }
}
