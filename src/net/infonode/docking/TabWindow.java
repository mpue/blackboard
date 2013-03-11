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


// $Id: TabWindow.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.infonode.docking.drag.DockingWindowDragSource;
import net.infonode.docking.drag.DockingWindowDragger;
import net.infonode.docking.drag.DockingWindowDraggerProvider;
import net.infonode.docking.internal.WriteContext;
import net.infonode.docking.internalutil.ButtonInfo;
import net.infonode.docking.internalutil.CloseButtonInfo;
import net.infonode.docking.internalutil.DockButtonInfo;
import net.infonode.docking.internalutil.InternalDockingUtil;
import net.infonode.docking.internalutil.MaximizeButtonInfo;
import net.infonode.docking.internalutil.MinimizeButtonInfo;
import net.infonode.docking.internalutil.RestoreButtonInfo;
import net.infonode.docking.internalutil.UndockButtonInfo;
import net.infonode.docking.model.TabWindowItem;
import net.infonode.docking.model.ViewWriter;
import net.infonode.docking.properties.TabWindowProperties;
import net.infonode.properties.base.Property;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;
import net.infonode.properties.util.PropertyChangeListener;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabEvent;
import net.infonode.tabbedpanel.TabRemovedEvent;
import net.infonode.util.ArrayUtil;
import net.infonode.util.Direction;

/**
 * A docking window containing a tabbed panel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class TabWindow extends AbstractTabWindow {
  private static final ButtonInfo[] buttonInfos = {
      new UndockButtonInfo(TabWindowProperties.UNDOCK_BUTTON_PROPERTIES),
      new DockButtonInfo(TabWindowProperties.DOCK_BUTTON_PROPERTIES),
      new MinimizeButtonInfo(TabWindowProperties.MINIMIZE_BUTTON_PROPERTIES),
      new MaximizeButtonInfo(TabWindowProperties.MAXIMIZE_BUTTON_PROPERTIES),
      new RestoreButtonInfo(TabWindowProperties.RESTORE_BUTTON_PROPERTIES),
      new CloseButtonInfo(TabWindowProperties.CLOSE_BUTTON_PROPERTIES)
  };

  private AbstractButton[] buttons = new AbstractButton[buttonInfos.length];

  private PropertyChangeListener minimumSizePropertiesListener = new PropertyChangeListener() {
    public void propertyChanged(Property property, Object valueContainer, Object oldValue, Object newValue) {
      revalidate();
    }
  };
  private PropertyMapTreeListener buttonFactoryListener = new PropertyMapTreeListener() {
    public void propertyValuesChanged(Map changes) {
      doUpdateButtonVisibility(changes);
    }
  };

  /**
   * Creates an empty tab window.
   */
  public TabWindow() {
    this((DockingWindow) null);
  }

  /**
   * Creates a tab window with a tab containing the child window.
   *
   * @param window the child window
   */
  public TabWindow(DockingWindow window) {
    this(window == null ? null : new DockingWindow[]{window});
  }

  /**
   * Creates a tab window with tabs for the child windows.
   *
   * @param windows the child windows
   */
  public TabWindow(DockingWindow[] windows) {
    this(windows, null);
  }

  protected TabWindow(DockingWindow[] windows, TabWindowItem windowItem) {
    super(true, windowItem == null ? new TabWindowItem() : windowItem);

    setTabWindowProperties(((TabWindowItem) getWindowItem()).getTabWindowProperties());

    PropertyMapWeakListenerManager.addWeakPropertyChangeListener(getTabWindowProperties().getMap(),
                                                                 TabWindowProperties.RESPECT_CHILD_WINDOW_MINIMUM_SIZE,
                                                                 minimumSizePropertiesListener);


    new DockingWindowDragSource(getTabbedPanel(), new DockingWindowDraggerProvider() {
      public DockingWindowDragger getDragger(MouseEvent mouseEvent) {
        if (!getWindowProperties().getDragEnabled())
          return null;

        Point p = SwingUtilities.convertPoint((Component) mouseEvent.getSource(),
                                              mouseEvent.getPoint(),
                                              getTabbedPanel());

        return getTabbedPanel().tabAreaContainsPoint(p) ?
               (getChildWindowCount() == 1 ? getChildWindow(0) : TabWindow.this).startDrag(getRootWindow()) :
               null;
      }
    });

    initMouseListener();
    init();

    getTabbedPanel().addTabListener(new TabAdapter() {
      public void tabAdded(TabEvent event) {
        doUpdateButtonVisibility(null);
      }

      public void tabRemoved(TabRemovedEvent event) {
        doUpdateButtonVisibility(null);
      }
    });

    if (windows != null) {
      for (int i = 0; i < windows.length; i++)
        addTab(windows[i]);
    }

    PropertyMapWeakListenerManager.addWeakTreeListener(getTabWindowProperties().getMap(), buttonFactoryListener);
  }

  public TabWindowProperties getTabWindowProperties() {
    return ((TabWindowItem) getWindowItem()).getTabWindowProperties();
  }

  protected void tabSelected(WindowTab tab) {
    super.tabSelected(tab);

    if (getUpdateModel()) {
      ((TabWindowItem) getWindowItem()).setSelectedItem(
          tab == null ? null : getWindowItem().getChildWindowContaining(tab.getWindow().getWindowItem()));
    }
  }

  protected void update() {
  }

  protected void updateButtonVisibility() {
    doUpdateButtonVisibility(null);
  }

  private void doUpdateButtonVisibility(Map changes) {
    //System.out.println("%%  Updating tab window buttons!");
    //System.out.println(getTabbedPanel().getTabCount() + "  " + getTabbedPanel().getSelectedTab() + "  " + isMaximizable() + "  " + isUndockable() + "  " + isMinimizable() + "  " + isClosable());
    if (InternalDockingUtil.updateButtons(buttonInfos,
                                          buttons,
                                          null,
                                          this,
                                          getTabWindowProperties().getMap(),
                                          changes)) {
      updateTabAreaComponents();
    }

    super.updateButtonVisibility();
  }

  protected int getTabAreaComponentCount() {
    return ArrayUtil.countNotNull(buttons);
  }

  protected void getTabAreaComponents(int index, JComponent[] components) {
    for (int i = 0; i < buttons.length; i++)
      if (buttons[i] != null)
        components[index++] = buttons[i];
  }

  protected void optimizeWindowLayout() {
    if (getWindowParent() == null)
      return;

    if (getTabbedPanel().getTabCount() == 0)
      internalClose();
    else if (getTabbedPanel().getTabCount() == 1 &&
             (getWindowParent().showsWindowTitle() || !getChildWindow(0).needsTitleWindow())) {
      getWindowParent().internalReplaceChildWindow(this, getChildWindow(0).getBestFittedWindow(getWindowParent()));
    }
  }

  public int addTab(DockingWindow w, int index) {
    int actualIndex = super.addTab(w, index);
    setSelectedTab(actualIndex);
    return actualIndex;
  }

  protected int addTabNoSelect(DockingWindow window, int index) {
    DockingWindow beforeWindow = index == getChildWindowCount() ? null : getChildWindow(index);

    int i = super.addTabNoSelect(window, index);

    if (getUpdateModel()) {
      addWindowItem(window, beforeWindow == null ? -1 : getWindowItem().getWindowIndex(
          getWindowItem().getChildWindowContaining(beforeWindow.getWindowItem())));
    }

    return i;
  }

  protected void updateWindowItem(RootWindow rootWindow) {
    super.updateWindowItem(rootWindow);
    ((TabWindowItem) getWindowItem()).setParentTabWindowProperties(rootWindow == null ?
                                                                   TabWindowItem.emptyProperties :
                                                                   rootWindow.getRootWindowProperties()
                                                                       .getTabWindowProperties());
  }

  protected PropertyMap getPropertyObject() {
    return getTabWindowProperties().getMap();
  }

  protected PropertyMap createPropertyObject() {
    return new TabWindowProperties().getMap();
  }

  protected int getEdgeDepth(Direction dir) {
    return dir == getTabbedPanel().getProperties().getTabAreaOrientation() ?
           1 :
           super.getEdgeDepth(dir);
  }

  protected int getChildEdgeDepth(DockingWindow window, Direction dir) {
    return dir == getTabbedPanel().getProperties().getTabAreaOrientation() ?
           0 :
           1 + super.getChildEdgeDepth(window, dir);
  }

  protected DockingWindow getOptimizedWindow() {
    return getChildWindowCount() == 1 ? getChildWindow(0).getOptimizedWindow() : super.getOptimizedWindow();
  }

  protected boolean acceptsSplitWith(DockingWindow window) {
    return super.acceptsSplitWith(window) && (getChildWindowCount() != 1 || getChildWindow(0) != window);
  }

  protected DockingWindow getBestFittedWindow(DockingWindow parentWindow) {
    return getChildWindowCount() == 1 && (!getChildWindow(0).needsTitleWindow() || parentWindow.showsWindowTitle()) ?
           getChildWindow(0).getBestFittedWindow(parentWindow) : this;
  }

  protected void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
    out.writeInt(WindowIds.TAB);
    viewWriter.writeWindowItem(getWindowItem(), out, context);
    super.write(out, context, viewWriter);
  }


}
