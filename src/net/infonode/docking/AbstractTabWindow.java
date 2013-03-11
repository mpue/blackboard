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


// $Id: AbstractTabWindow.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.infonode.docking.drop.InsertTabDropInfo;
import net.infonode.docking.internal.ReadContext;
import net.infonode.docking.internal.WindowAncestors;
import net.infonode.docking.internal.WriteContext;
import net.infonode.docking.internalutil.DropAction;
import net.infonode.docking.model.AbstractTabWindowItem;
import net.infonode.docking.model.ViewReader;
import net.infonode.docking.model.ViewWriter;
import net.infonode.docking.model.WindowItem;
import net.infonode.docking.properties.TabWindowProperties;
import net.infonode.docking.properties.WindowTabProperties;
import net.infonode.properties.propertymap.PropertyMapManager;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabContentPanel;
import net.infonode.tabbedpanel.TabEvent;
import net.infonode.tabbedpanel.TabRemovedEvent;
import net.infonode.tabbedpanel.TabStateChangedEvent;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.util.ChangeNotifyList;

/**
 * Abstract base class for windows containing a tabbed panel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
abstract public class AbstractTabWindow extends DockingWindow {
  private static int MINIMUM_SIZE = 7;

  private final DropAction dropAction = new DropAction() {
    public boolean showTitle() {
      return false;
    }

    public void execute(DockingWindow window, MouseEvent mouseEvent) {
      if (window.getWindowParent() == AbstractTabWindow.this) {
        // Tab moved inside window
        updateWindowItem(window);
      }
      else {
        int index = tabbedPanel.getTabIndex(dragTab);
        stopDrag();

        try {
          window.beforeDrop(AbstractTabWindow.this);

          if (mouseEvent.isShiftDown())
            addTabNoSelect(window, index);
          else
            addTab(window, index);
        }
        catch (OperationAbortedException e) {
          // Ignore
        }
      }
    }

    public void clear(DockingWindow window, DropAction newDropAction) {
      if (newDropAction != this) {
        if (window.getWindowParent() == AbstractTabWindow.this) {
          WindowTab tab = window.getTab();
          boolean selected = tab.isSelected();
          tabbedPanel.removeTab(tab);
          tabbedPanel.insertTab(tab, draggedTabIndex);

          if (selected)
            tab.setSelected(true);
        }
        else {
          stopDrag();
        }
      }
    }
  };

  private TabbedPanel tabbedPanel;

  /**
   * Temporary drag tab.
   */
  private WindowTab dragTab;

  private int ignoreSelected;

  private int draggedTabIndex;

  private java.util.List tabAreaComponents;

  /**
   * Returns the properties for this tab window.
   *
   * @return the properties for this tab window
   */
  abstract public TabWindowProperties getTabWindowProperties();

  protected AbstractTabWindow(boolean showContent, WindowItem windowItem) {
    super(windowItem);
    if (showContent) {
      TabContentPanel contentPanel = new TabContentPanel() {
        public Dimension getMinimumSize() {
          if (getTabWindowProperties().getRespectChildWindowMinimumSize())
            return super.getMinimumSize();

          return new Dimension(0, 0);
        }
      };
      tabbedPanel = new TabbedPanel(contentPanel, true) {
        public Dimension getMinimumSize() {
          if (getTabWindowProperties().getRespectChildWindowMinimumSize())
            return super.getMinimumSize();

          return getTabbedPanelMinimumSize(super.getMinimumSize());
        }
      };
      contentPanel.setTabbedPanel(tabbedPanel);
    }
    else
      tabbedPanel = new TabbedPanel(null) {
      public Dimension getMinimumSize() {
        if (getTabWindowProperties().getRespectChildWindowMinimumSize())
          return super.getMinimumSize();

        return getTabbedPanelMinimumSize(super.getMinimumSize());
      }
    };

    tabbedPanel.addTabListener(new TabWindowMover(this, tabbedPanel));
    setComponent(tabbedPanel);

    getTabbedPanel().addTabListener(new TabAdapter() {
      public void tabAdded(final TabEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            updateButtonVisibility();
          }
        });
      }

      public void tabRemoved(final TabRemovedEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            updateButtonVisibility();
          }
        });
      }

      public void tabSelected(TabStateChangedEvent event) {
        AbstractTabWindow.this.tabSelected((WindowTab) event.getTab());
        DockingWindow selectedWindow = getSelectedWindow();

        if (!getIgnoreSelected() && selectedWindow != null)
          selectedWindow.fireWindowShown(selectedWindow);
      }

      public void tabDeselected(TabStateChangedEvent event) {
        WindowTab tab = (WindowTab) event.getTab();

        if (tab != null && !getIgnoreSelected())
          tab.getWindow().fireWindowHidden(tab.getWindow());
      }

      public void tabMoved(TabEvent event) {
        if (!getIgnoreSelected())
          fireTitleChanged();
      }
    });
  }

  protected void initMouseListener() {
    getTabbedPanel().addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showPopupMenu(e);
        }
      }

      public void mouseReleased(MouseEvent e) {
        mousePressed(e);
      }
    });
  }

  private Dimension getTabbedPanelMinimumSize(Dimension d) {
    if (tabbedPanel.getProperties().getTabAreaOrientation().isHorizontal())
      return new Dimension(d.width, MINIMUM_SIZE);
    else
      return new Dimension(MINIMUM_SIZE, d.height);
  }

  /**
   * <p>
   * Returns a list containing the custom tab area components. Changes to the
   * list will be propagated to the tab area.
   * </p>
   * <p>
   * The custom tab area components will between the scroll buttons and the
   * window buttons in the tab area components panel. The components are shown
   * in the same order as they appear in the list. The tab area components
   * container layout is rotated with the tab window tab orientation.
   * </p>
   *
   * @return a list containing the custom tab area components, list elements are
   *         of type {@link JComponent}
   * @since IDW 1.3.0
   */
  public final java.util.List getCustomTabAreaComponents() {
    if (tabAreaComponents == null)
      tabAreaComponents = new ChangeNotifyList() {
      protected void changed() {
        updateTabAreaComponents();
      }
    };

    return tabAreaComponents;
  }

  /**
   * Returns the currently selected window in the tabbed panel.
   *
   * @return the currently selected window in the tabbed panel
   */
  public DockingWindow getSelectedWindow() {
    WindowTab tab = (WindowTab) tabbedPanel.getSelectedTab();
    return tab == null ? null : tab.getWindow();
  }

  /**
   * Selects the tab with the index.
   *
   * @param index the tab index
   */
  public void setSelectedTab(int index) {
    beginIgnoreSelected();

    try {
      Tab tab = index == -1 ? null : tabbedPanel.getTabAt(index);
      Tab oldTab = tabbedPanel.getSelectedTab();

      if (tab != oldTab) {
        tabbedPanel.setSelectedTab(tab);
        fireTitleChanged();

        if (oldTab != null)
          ((WindowTab) oldTab).getWindow().fireWindowHidden(((WindowTab) oldTab).getWindow());

        if (tab != null)
          ((WindowTab) tab).getWindow().fireWindowShown(((WindowTab) tab).getWindow());
      }
    }
    finally {
      endIgnoreSelected();
    }
  }

  /**
   * Adds a window tab last in this tab window.
   *
   * @param window the window
   */
  public void addTab(DockingWindow window) {
    PropertyMapManager.getInstance().beginBatch();

    try {
      addTab(window, tabbedPanel.getTabCount());
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }
  }

  /**
   * Inserts a window tab at an index in this tab window.
   *
   * @param window the window
   * @param index  the index where to insert the tab
   * @return the index of the added tab, this might not be the same as
   *         <tt>index</tt> if the tab already is added to this tab window
   */
  public int addTab(DockingWindow window, int index) {
    PropertyMapManager.getInstance().beginBatch();

    try {
      return addTabNoSelect(window, index);
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }
  }

  protected int addTabNoSelect(DockingWindow window, int index) {
    WindowAncestors ancestors = window.storeAncestors();
    beginOptimize(window.getWindowParent());
    beginIgnoreSelected();

    try {
      Tab beforeTab = index >= tabbedPanel.getTabCount() ? null : tabbedPanel.getTabAt(index);
      DockingWindow w = window.getContentWindow(this);
      w.detach();
      updateTab(w);
      WindowTab tab = w.getTab();
      int actualIndex = beforeTab == null ? tabbedPanel.getTabCount() : tabbedPanel.getTabIndex(beforeTab);
      tabbedPanel.insertTab(tab, actualIndex);
      addWindow(w);
      window.notifyListeners(ancestors);
      return actualIndex;
    }
    finally {
      endIgnoreSelected();
      endOptimize();
    }
  }

  protected boolean isChildShowingInRootWindow(DockingWindow child) {
    return super.isChildShowingInRootWindow(child) && child == getSelectedWindow();
  }

  protected void showChildWindow(DockingWindow window) {
    setSelectedTab(getChildWindowIndex(window));
    super.showChildWindow(window);
  }

  protected boolean childInsideTab() {
    return true;
  }

  protected void setTabWindowProperties(TabWindowProperties properties) {
    getTabbedPanel().getProperties().addSuperObject(properties.getTabbedPanelProperties());
  }

  protected void clearFocus(View view) {
    if (getSelectedWindow() != null) {
      getSelectedWindow().clearFocus(view);
    }
  }

  protected DockingWindow getPreferredFocusChild() {
    return getSelectedWindow() == null ? super.getPreferredFocusChild() : getSelectedWindow();
  }

  protected void clearChildrenFocus(DockingWindow child, View view) {
    if (getSelectedWindow() != child)
      clearFocus(view);
  }

  protected int getTabAreaComponentCount() {
    return 0;
  }

  protected void updateTabAreaComponents() {
    int ls = tabAreaComponents == null ? 0 : tabAreaComponents.size();
    JComponent[] components = new JComponent[ls + getTabAreaComponentCount()];

    if (tabAreaComponents != null)
      tabAreaComponents.toArray(components);

    getTabAreaComponents(ls, components);
    getTabbedPanel().setTabAreaComponents(components);
  }

  protected void getTabAreaComponents(int index, JComponent[] components) {
  }

  protected final boolean getIgnoreSelected() {
    return ignoreSelected > 0;
  }

  protected void tabSelected(WindowTab tab) {
    if (!getIgnoreSelected() && tab != null) {
      final RootWindow root = getRootWindow();

      if (root != null) {
        // Anticipate the focus movement to avoid flicker
        tab.setFocused(true);
        root.addFocusedWindow(tab.getWindow());
        FocusManager.focusWindow(tab.getWindow());
      }
    }

    if (!getIgnoreSelected())
      fireTitleChanged();
  }

  protected TabbedPanel getTabbedPanel() {
    return tabbedPanel;
  }

  public DockingWindow getChildWindow(int index) {
    return ((WindowTab) tabbedPanel.getTabAt(index)).getWindow();
  }

  protected DockingWindow getLocationWindow() {
    return tabbedPanel.getTabCount() == 1 ? getChildWindow(0) : this;
  }

  public int getChildWindowCount() {
    return tabbedPanel.getTabCount();
  }

  public Icon getIcon() {
    DockingWindow window = getSelectedWindow();
    return window != null ? window.getIcon() : getChildWindowCount() > 0 ? getChildWindow(0).getIcon() : null;
  }

  private void updateTab(DockingWindow window) {
    window.getTab().setProperties(getTabProperties(window));
  }

  private WindowTabProperties getTabProperties(DockingWindow window) {
    WindowTabProperties properties = new WindowTabProperties(getTabWindowProperties().getTabProperties());
    properties.addSuperObject(window.getWindowProperties().getTabProperties());
    return properties;
  }

  protected void doReplace(DockingWindow oldWindow, DockingWindow newWindow) {
    beginIgnoreSelected();

    try {
      Tab tab = oldWindow.getTab();
      int tabIndex = tabbedPanel.getTabIndex(tab);

      boolean selected = tab.isSelected();
      tabbedPanel.removeTab(tab);
      tabbedPanel.insertTab(newWindow.getTab(), tabIndex);

      if (selected)
        tabbedPanel.setSelectedTab(newWindow.getTab());

      updateTab(newWindow);
    }
    finally {
      endIgnoreSelected();
    }
  }

  protected void doRemoveWindow(DockingWindow window) {
    beginIgnoreSelected();

    try {
      WindowTab tab = window.getTab();
      tab.unsetProperties();
      tabbedPanel.removeTab(tab);
    }
    finally {
      endIgnoreSelected();
    }
  }

  private void beginIgnoreSelected() {
    ignoreSelected++;
  }

  private void endIgnoreSelected() {
    ignoreSelected--;
  }

  protected boolean isInsideTabArea(Point p2) {
    return tabbedPanel.tabAreaContainsPoint(p2);
  }

  protected DropAction acceptInteriorDrop(Point p, DockingWindow window) {
    if (getChildWindowCount() == 1 && window == getChildWindow(0) && dragTab == null)
      return null;

    Point p2 = SwingUtilities.convertPoint(this, p, tabbedPanel);

    if ((getRootWindow().getRootWindowProperties().getRecursiveTabsEnabled() || window.getChildWindowCount() <= 1) &&
        isInsideTabArea(p2)) {
      getRootWindow().setDragRectangle(null);

      if (window.getWindowParent() == this) {
        tabbedPanel.moveTab(window.getTab(), p2);
      }
      else {
        if (!getInsertTabDropFilter().acceptDrop(new InsertTabDropInfo(window, this, p)))
          return null;

        if (dragTab == null) {
          dragTab = createGhostTab(window);
          tabbedPanel.insertTab(dragTab, p2);
        }
        else {
          tabbedPanel.moveTab(dragTab, p2);
        }
      }

      return dropAction;
    }

    return null;
  }

  WindowTab createGhostTab(DockingWindow window) {
    WindowTab tab = new WindowTab(window, true);
    tab.setProperties(getTabProperties(window));
    return tab;
  }

  private void stopDrag() {
    if (dragTab != null) {
      tabbedPanel.removeTab(dragTab);
      dragTab = null;
    }
  }

  protected boolean showsWindowTitle() {
    return true;
  }

  protected DockingWindow oldRead(ObjectInputStream in, ReadContext context) throws IOException {
    int size = in.readInt();
    int selectedIndex = in.readInt();

    while (getChildWindowCount() > 0)
      removeChildWindow(getChildWindow(0));

    for (int i = 0; i < size; i++) {
      DockingWindow window = WindowDecoder.decodeWindow(in, context);

      if (window != null)
        addTab(window);
      else if (i < selectedIndex)
        selectedIndex--;
    }

    super.oldRead(in, context);

    if (tabbedPanel.getTabCount() > 0) {
      if (selectedIndex >= 0)
        setSelectedTab(Math.min(tabbedPanel.getTabCount() - 1, selectedIndex));

      return this;
    }
    else
      return null;
  }

  protected void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
    out.writeInt(getChildWindowCount());

    for (int i = 0; i < getChildWindowCount(); i++)
      getChildWindow(i).write(out, context, viewWriter);
  }

  protected DockingWindow newRead(ObjectInputStream in, ReadContext context, ViewReader viewReader) throws IOException {
    int size = in.readInt();

    while (getChildWindowCount() > 0)
      removeChildWindow(getChildWindow(0));

    for (int i = 0; i < size; i++) {
      DockingWindow window = WindowDecoder.decodeWindow(in, context, viewReader);

      if (window != null)
        addTab(window);
    }

    updateSelectedTab();
    return getChildWindowCount() > 0 ? this : null;
  }

  protected void updateSelectedTab() {
    WindowItem selectedItem = ((AbstractTabWindowItem) getWindowItem()).getSelectedItem();

    for (int i = 0; i < getChildWindowCount(); i++) {
      if (getChildWindow(i).getWindowItem().hasAncestor(selectedItem)) {
        setSelectedTab(i);
        return;
      }
    }
  }

  void setDraggedTabIndex(int index) {
    draggedTabIndex = index;
  }

  void removeWindowComponent(DockingWindow window) {
    window.getTab().setContentComponent(null);
  }

  void restoreWindowComponent(DockingWindow window) {
    window.getTab().setContentComponent(window);
  }

}