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


// $Id: FloatingWindow.java,v 1.4 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.infonode.docking.drop.ChildDropInfo;
import net.infonode.docking.drop.InteriorDropInfo;
import net.infonode.docking.internal.ReadContext;
import net.infonode.docking.internal.WindowAncestors;
import net.infonode.docking.internal.WriteContext;
import net.infonode.docking.internalutil.DropAction;
import net.infonode.docking.model.FloatingWindowItem;
import net.infonode.docking.model.ViewReader;
import net.infonode.docking.model.ViewWriter;
import net.infonode.docking.properties.DockingWindowProperties;
import net.infonode.docking.properties.FloatingWindowProperties;
import net.infonode.docking.properties.SplitWindowProperties;
import net.infonode.docking.util.DockingUtil;
import net.infonode.gui.ComponentUtil;
import net.infonode.gui.layout.StretchLayout;
import net.infonode.gui.panel.SimplePanel;
import net.infonode.gui.shaped.panel.ShapedPanel;
import net.infonode.properties.gui.InternalPropertiesUtil;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;
import net.infonode.util.Direction;

/**
 * <p>
 * A window that is floating on-top of the root window and containing another docking window.
 * </p>
 *
 * <p>
 * A window can be maximized inside the floating window just as in a root window.
 * </p>
 *
 * <p>
 * After a floating window has been closed it shouldn't be reused again.
 * </p>
 *
 * <p>
 * Floating window inherits its component properties and shaped panel properties from the root
 * window's window area. It is possible to set specific component and shaped panel properties for
 * a floating window in the {@link net.infonode.docking.properties.FloatingWindowProperties}, see
 * {@link FloatingWindow#getFloatingWindowProperties()}.
 * </p>
 *
 * <p>
 * A floating window is created by calling the
 * {@link net.infonode.docking.RootWindow#createFloatingWindow(Point, Dimension, DockingWindow)}
 * method or indirectly created by calling the
 * {@link net.infonode.docking.DockingWindow#undock(Point)} method.
 * </p>
 *
 * <p>
 * It's possible to add a menu bar to the floating window. Just call:
 * </p>
 * <pre>
 *   myFloatingWindow.getRootPane().setJMenuBar(myMenuBar);
 * </pre>
 *
 * <p>
 * The floating window is placed as the BorderLayout.CENTER component of the content pane of the
 * root pane. You can add additional components in the other BorderLayout positions. Example, add a
 * status label at the bottom:
 * </p>
 * <pre>
 *   myFloatingWindow.getRootPane().getContentPane().add(myStstusLabel, BroderLayout.SOUTH);
 * </pre>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.4 $
 * @since IDW 1.4.0
 */
public class FloatingWindow extends DockingWindow {
  private DockingWindow window;
  private Window dialog;
  private JPanel dragPanel = new SimplePanel();
  private ShapedPanel shapedPanel;
  private DockingWindow maximizedWindow;
  private Runnable titleUpdater;

  private AWTEventListener awtMouseEventListener;

  private PropertyMapTreeListener propertiesListener = new PropertyMapTreeListener() {
    public void propertyValuesChanged(Map changes) {
      updateFloatingWindow(changes);
    }
  };

  public FloatingWindow(RootWindow rootWindow) {
    super(new FloatingWindowItem());

    getFloatingWindowProperties().addSuperObject(rootWindow.getRootWindowProperties().getFloatingWindowProperties());

    setLayout(new StretchLayout(true, true));
    shapedPanel = new ShapedPanel();
    setComponent(shapedPanel);

    Component c = rootWindow.getTopLevelComponent();
    dialog = getFloatingWindowProperties().getUseFrame() ? (Window) new JFrame() :
             (Window) (c instanceof Frame ? new JDialog((Frame) c) : new JDialog((Dialog) c));
    ((RootPaneContainer) dialog).getContentPane().add(this, BorderLayout.CENTER);

    if (dialog instanceof JDialog)
      ((JDialog) dialog).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    else
      ((JFrame) dialog).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    dialog.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        try {
          if (getWindowProperties().getCloseEnabled())
            closeWithAbort();
        }
        catch (OperationAbortedException e1) {
          // Ignore
        }
      }
    });

    JRootPane rp = ((RootPaneContainer) dialog).getRootPane();
    rp.getLayeredPane().add(dragPanel);
    rp.getLayeredPane().setLayer(dragPanel, JLayeredPane.DRAG_LAYER.intValue());
    dragPanel.setVisible(false);

    dragPanel.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        getRootWindow().setCurrentDragRootPane(getRootPane());
      }

      public void mouseExited(MouseEvent e) {
        if (!dragPanel.contains(e.getPoint()) && getRootWindow().getCurrentDragRootPane() == getRootPane())
          getRootWindow().setCurrentDragRootPane(null);
      }
    });

    if (rootWindow.isHeavyweightSupported()) {
      try {
        awtMouseEventListener = new AWTEventListener() {
          public void eventDispatched(AWTEvent event) {
            if (event.getID() == MouseEvent.MOUSE_ENTERED) {
              Component c = (Component) event.getSource();
              if (ComponentUtil.getTopLevelAncestor(c) == dialog) {
                getRootWindow().setCurrentDragRootPane(getRootPane());
              }
            }
          }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(awtMouseEventListener, AWTEvent.MOUSE_EVENT_MASK);
      }
      catch (SecurityException e) {
        awtMouseEventListener = null;
        // Ignore
      }
    }

    PropertyMapWeakListenerManager.addWeakTreeListener(getFloatingWindowProperties().getMap(), propertiesListener);
    updateFloatingWindow(null);
  }

  FloatingWindow(RootWindow rootWindow, DockingWindow window, Point p, Dimension internalSize) {
    this(rootWindow);
    setWindow(window);
    setInternalSize(internalSize);
    dialog.setLocation(p.x, p.y);
  }

  /**
   * Sets the top level docking window inside this floating window.
   *
   * @param newWindow the top level docking window, null for no window i.e. empty floating window
   */
  public void setWindow(DockingWindow newWindow) {
    if (window == newWindow)
      return;

    if (window == null) {
      WindowAncestors ancestors = newWindow.storeAncestors();
      DockingWindow actualWindow = addWindow(newWindow);
      doReplace(null, actualWindow);
      newWindow.notifyListeners(ancestors);
    }
    else if (newWindow == null) {
      removeChildWindow(window);
      window = null;
    }
    else
      replaceChildWindow(window, newWindow);
  }

  /**
   * Returns the top level docking window inside this floating window.
   *
   * @return the top level docking window inside this floating window
   */
  public DockingWindow getWindow() {
    return window;
  }

  /**
   * Sets the maximized window in this floating window.
   *
   * @param window the window to maximize, must be a member of the window tree inside this floating window
   */
  public void setMaximizedWindow(DockingWindow window) {
    if (window == maximizedWindow)
      return;

    if (window instanceof FloatingWindow || window != null && !(DockingUtil.getFloatingWindowFor(window) == this))
      return;

    internalSetMaximizedWindow(window);
  }

  /**
   * Returns the maximized window in this floating window.
   *
   * @return maximized window or null if no window is maximized
   */
  public DockingWindow getMaximizedWindow() {
    return maximizedWindow;
  }

  /**
   * <p>
   * Returns the property values for this floating window.
   * </p>
   *
   * <p>
   * Floating window inherits its component properties and shaped panel properties from the root
   * window's window area. It is possible to set specific component and shaped panel properties for
   * a floating window in the {@link net.infonode.docking.properties.FloatingWindowProperties}.
   * </p>
   *
   * @return the property values for this floating window
   */
  public FloatingWindowProperties getFloatingWindowProperties() {
    return ((FloatingWindowItem) getWindowItem()).getFloatingWindowProperties();
  }

  /**
   * <p>
   * Returns the properties for this window.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> A floating window only uses the close enabled and title provider
   * properties of the docking window properties.
   * </p>
   *
   * @return the properties for this window
   */
  public DockingWindowProperties getWindowProperties() {
    return super.getWindowProperties();
  }

  /**
   * Floating window cannot be minimized
   */
  public void minimize() {
  }

  /**
   * Floating window cannot be minimized
   *
   * @param direction
   */
  public void minimize(Direction direction) {
  }

  public boolean isDockable() {
    return false;
  }

  public boolean isMaximizable() {
    return false;
  }

  public boolean isMinimizable() {
    return false;
  }

  public boolean isRestorable() {
    return false;
  }

  public boolean isUndockable() {
    return false;
  }

  public void close() {
    PropertyMapWeakListenerManager.removeWeakTreeListener(getFloatingWindowProperties().getMap(), propertiesListener);
    RootWindow rw = getRootWindow();

    super.close();

    dialog.dispose();
    if (rw != null)
      rw.removeFloatingWindow(this);

    try {
      if (awtMouseEventListener != null)
        Toolkit.getDefaultToolkit().removeAWTEventListener(awtMouseEventListener);
    }
    catch (SecurityException e) {
      // Ignore
    }
  }

  public Icon getIcon() {
    return window == null ? null : window.getIcon();
  }

  public DockingWindow getChildWindow(int index) {
    return window;
  }

  public int getChildWindowCount() {
    return window == null ? 0 : 1;
  }

  public boolean isUndocked() {
    return true;
  }

  void startDrag() {
    JRootPane rp = ((RootPaneContainer) dialog).getRootPane();
    dragPanel.setBounds(0, 0, rp.getWidth(), rp.getHeight());
    dragPanel.setVisible(true);
    //ComponentUtil.validate(rp.getLayeredPane());
    //rp.validate();
  }

  void stopDrag() {
//    JRootPane rp = dialog.getRootPane();
    dragPanel.setVisible(false);
    //rp.getLayeredPane().remove(dragPanel);
  }

  JPanel getDragPanel() {
    return dragPanel;
  }

  boolean windowContainsPoint(Point p) {
    return getTopLevelAncestor().contains(SwingUtilities.convertPoint(this, p, getTopLevelAncestor()));
  }

  private void internalSetMaximizedWindow(DockingWindow window) {
    if (window == maximizedWindow)
      return;

    DockingWindow focusWindow = null;

    if (maximizedWindow != null) {
      DockingWindow oldMaximized = maximizedWindow;
      maximizedWindow = null;

      if (oldMaximized.getWindowParent() != null)
        oldMaximized.getWindowParent().restoreWindowComponent(oldMaximized);

      if (oldMaximized != this.window)
        shapedPanel.remove(oldMaximized);

      focusWindow = oldMaximized;
      fireWindowRestored(oldMaximized);
    }

    maximizedWindow = window;

    if (maximizedWindow != null) {
      if (maximizedWindow.getWindowParent() != null)
        maximizedWindow.getWindowParent().removeWindowComponent(maximizedWindow);

      if (maximizedWindow != this.window) {
        shapedPanel.add(maximizedWindow);

        if (this.window != null)
          this.window.setVisible(false);
      }

      maximizedWindow.setVisible(true);

      focusWindow = maximizedWindow;
      fireWindowMaximized(maximizedWindow);
    }
    else if (this.window != null) {
      this.window.setVisible(true);
    }

    if (focusWindow != null)
      FocusManager.focusWindow(focusWindow);
  }

  protected void doReplace(DockingWindow oldWindow, DockingWindow newWindow) {
    if (oldWindow == window) {
      if (window != null) {
        shapedPanel.remove(window);
        window.setVisible(true);
      }

      window = newWindow;

      if (window != null) {
        if (maximizedWindow != null)
          window.setVisible(false);

        shapedPanel.add(window);
        doUpdateTitle();
        shapedPanel.revalidate();
      }
    }

    updateButtonVisibility();
  }

  protected void doRemoveWindow(DockingWindow window) {
    if (window != null) {
      shapedPanel.remove(window);
      this.window.setVisible(true);
      this.window = null;
      shapedPanel.repaint();
    }
  }

  protected void afterWindowRemoved(DockingWindow window) {
    if (getFloatingWindowProperties().getAutoCloseEnabled())
      close();
  }

  private void doUpdateTitle() {
    if (titleUpdater == null) {
      titleUpdater = new Runnable() {
        public void run() {
          if (dialog != null) {
            if (dialog instanceof Dialog)
              ((Dialog) dialog).setTitle(window == null ? "" : window.getTitle());
            else
              ((Frame) dialog).setTitle(window == null ? "" : window.getTitle());
          }

          titleUpdater = null;
        }
      };

      SwingUtilities.invokeLater(titleUpdater);
    }
  }

  protected boolean acceptsSplitWith(DockingWindow window) {
    return false;
  }

  protected DropAction doAcceptDrop(Point p, DockingWindow window) {
    DockingWindow dropWindow = maximizedWindow != null ? maximizedWindow : this.window;

    if (dropWindow != null) {
      Point p2 = SwingUtilities.convertPoint(this, p, dropWindow);

      if (dropWindow.contains(p2)) {
        return getChildDropFilter().acceptDrop(new ChildDropInfo(window, this, p, dropWindow)) ?
               dropWindow.acceptDrop(p2, window) :
               null;
      }
    }

    return super.doAcceptDrop(p, window);
  }

  protected DropAction acceptInteriorDrop(Point p, DockingWindow window) {
    if (this.window != null)
      return null;

    getRootWindow().setDragRectangle(null);

    if (getInteriorDropFilter().acceptDrop(new InteriorDropInfo(window, this, p)))
      return new DropAction() {
        public void execute(DockingWindow window, MouseEvent mouseEvent) {
          setWindow(window);
        }
      };

    return null;
  }

  protected void update() {
  }

  void removeWindowComponent(DockingWindow window) {
    // Do nothing
  }

  void restoreWindowComponent(DockingWindow window) {
    // Do nothing
  }

  protected void showChildWindow(DockingWindow window) {
    if (maximizedWindow != null && window == this.window)
      setMaximizedWindow(null);

    super.showChildWindow(window);
  }

  protected PropertyMap getPropertyObject() {
    return new SplitWindowProperties().getMap();
  }

  protected PropertyMap createPropertyObject() {
    return new SplitWindowProperties().getMap();
  }

  private void updateFloatingWindow(Map map) {
    FloatingWindowProperties properties = getFloatingWindowProperties();
    ComponentProperties componentProperties = map == null ||
                                              map.get(properties.getComponentProperties().getMap()) != null ?
                                                                                                            properties
                                                                                                                .getComponentProperties() :
                                                                                                                                          null;
    ShapedPanelProperties shapedProperties = map == null ||
                                             map.get(properties.getShapedPanelProperties().getMap()) != null ?
                                                                                                             properties
                                                                                                                 .getShapedPanelProperties() :
                                                                                                                                             null;

    if (componentProperties != null)
      componentProperties.applyTo(shapedPanel);

    if (shapedProperties != null)
      InternalPropertiesUtil.applyTo(shapedProperties, shapedPanel);
  }

  protected void fireTitleChanged() {
    super.fireTitleChanged();

    doUpdateTitle();
  }

  private void setInternalSize(Dimension size) {
    ((RootPaneContainer) dialog).getRootPane().setPreferredSize(size);
    dialog.pack();
    ((RootPaneContainer) dialog).getRootPane().setPreferredSize(null);
  }

  protected DockingWindow read(ObjectInputStream in, ReadContext context, ViewReader viewReader) throws IOException {
    dialog.setSize(new Dimension(in.readInt(), in.readInt()));
    dialog.setLocation(in.readInt(), in.readInt());

    dialog.setVisible(in.readBoolean());
    getWindowItem().readSettings(in, context);

    if (in.readBoolean())
      setWindow(WindowDecoder.decodeWindow(in, context, viewReader));

    return this;
  }

  protected void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
    out.writeInt(dialog.getWidth());
    out.writeInt(dialog.getHeight());
    out.writeInt(dialog.getX());
    out.writeInt(dialog.getY());
    out.writeBoolean(dialog.isVisible());
    getWindowItem().writeSettings(out, context);
    out.writeBoolean(window != null);

    if (window != null)
      window.write(out, context, viewWriter);
  }
}
