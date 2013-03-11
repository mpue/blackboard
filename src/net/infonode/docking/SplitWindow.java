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


// $Id: SplitWindow.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Icon;

import net.infonode.docking.drop.InteriorDropInfo;
import net.infonode.docking.drop.SplitDropInfo;
import net.infonode.docking.internal.ReadContext;
import net.infonode.docking.internal.WindowAncestors;
import net.infonode.docking.internal.WriteContext;
import net.infonode.docking.internalutil.DropAction;
import net.infonode.docking.model.SplitWindowItem;
import net.infonode.docking.model.ViewReader;
import net.infonode.docking.model.ViewWriter;
import net.infonode.docking.properties.SplitWindowProperties;
import net.infonode.gui.ComponentUtil;
import net.infonode.gui.SimpleSplitPane;
import net.infonode.gui.SimpleSplitPaneListener;
import net.infonode.gui.panel.BaseContainerUtil;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.util.Direction;

/**
 * A window with a split pane that contains two child windows.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class SplitWindow extends DockingWindow {
  private SimpleSplitPane splitPane;
  private DockingWindow leftWindow;
  private DockingWindow rightWindow;

  /**
   * Creates a split window.
   *
   * @param horizontal true if the split is horizontal
   */
  public SplitWindow(boolean horizontal) {
    this(horizontal, null, null);
  }

  /**
   * Creates a split window with with the given child windows.
   *
   * @param horizontal  true if the split is horizontal
   * @param leftWindow  the left/upper window
   * @param rightWindow the right/lower window
   */
  public SplitWindow(boolean horizontal, DockingWindow leftWindow, DockingWindow rightWindow) {
    this(horizontal, 0.5f, leftWindow, rightWindow);
  }

  /**
   * Creates a split window with with the given child windows.
   *
   * @param horizontal      true if the split is horizontal
   * @param dividerLocation the divider location, 0 - 1
   * @param leftWindow      the left/upper window
   * @param rightWindow     the right/lower window
   */
  public SplitWindow(boolean horizontal, float dividerLocation, DockingWindow leftWindow, DockingWindow rightWindow) {
    this(horizontal, dividerLocation, leftWindow, rightWindow, null);
  }

  protected SplitWindow(boolean horizontal, float dividerLocation, DockingWindow leftWindow,
                        DockingWindow rightWindow, SplitWindowItem windowItem) {
    super(windowItem == null ? new SplitWindowItem() : windowItem);

    splitPane = new SimpleSplitPane(horizontal);
    BaseContainerUtil.setForcedOpaque(splitPane, false);
    //splitPane.setForcedOpaque(false);
    splitPane.addListener(new SimpleSplitPaneListener() {
      public void dividerLocationChanged(SimpleSplitPane simpleSplitPane) {
        ((SplitWindowItem) getWindowItem()).setDividerLocation(simpleSplitPane.getDividerLocation());
      }
    });
    setComponent(splitPane);
    setWindows(leftWindow, rightWindow);
    setHorizontal(horizontal);
    setDividerLocation(dividerLocation);
    splitPane.getDividerPanel().addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showPopupMenu(e);
        }
      }

      public void mouseReleased(MouseEvent e) {
        mousePressed(e);
      }
    });
    init();
  }

  /**
   * Returns the property values for this split window.
   *
   * @return the property values for this split window
   */
  public SplitWindowProperties getSplitWindowProperties() {
    return ((SplitWindowItem) getWindowItem()).getSplitWindowProperties();
  }

  /**
   * Returns the left/upper child window.
   *
   * @return the left/upper child window
   */
  public DockingWindow getLeftWindow() {
    return leftWindow;
  }

  /**
   * Returns the right/lower child window.
   *
   * @return the right/lower child window
   */
  public DockingWindow getRightWindow() {
    return rightWindow;
  }

  /**
   * Sets the divider location as a fraction of this split window's size.
   *
   * @param dividerLocation the divider location as a fraction of this split window's size
   */
  public void setDividerLocation(float dividerLocation) {
    splitPane.setDividerLocation(dividerLocation);
  }

  /**
   * Returns the divider location as a fraction of this split window's size.
   *
   * @return the divider location as a fraction of this split window's size
   */
  public float getDividerLocation() {
    return splitPane.getDividerLocation();
  }

  /**
   * Sets the child windows of this split window.
   *
   * @param leftWindow  the left/upper child window
   * @param rightWindow the right/lower child window
   */
  public void setWindows(final DockingWindow leftWindow, final DockingWindow rightWindow) {
    if (leftWindow == getLeftWindow() && rightWindow == getRightWindow())
      return;

    optimizeAfter(null, new Runnable() {
      public void run() {
        WindowAncestors leftAncestors = leftWindow.storeAncestors();
        WindowAncestors rightAncestors = rightWindow.storeAncestors();

        DockingWindow lw = leftWindow.getContentWindow(SplitWindow.this);
        DockingWindow rw = rightWindow.getContentWindow(SplitWindow.this);
        lw.detach();
        rw.detach();

        if (getLeftWindow() != null)
          removeWindow(getLeftWindow());

        if (getRightWindow() != null)
          removeWindow(getRightWindow());

        SplitWindow.this.leftWindow = lw;
        SplitWindow.this.rightWindow = rw;
        splitPane.setComponents(lw, rw);
        addWindow(lw);
        addWindow(rw);

        if (getUpdateModel()) {
          addWindowItem(getLeftWindow(), -1);
          addWindowItem(getRightWindow(), -1);
          cleanUpModel();
        }

        leftWindow.notifyListeners(leftAncestors);
        rightWindow.notifyListeners(rightAncestors);
      }
    });
  }

  /**
   * Returns true if this SplitWindow is a horizontal split, otherwise it's vertical.
   *
   * @return true if this SplitWindow is a horizontal split, otherwise it's vertical
   * @since IDW 1.2.0
   */
  public boolean isHorizontal() {
    return splitPane.isHorizontal();
  }

  /**
   * Sets the split to horizontal or vertical.
   *
   * @param horizontal if true the split is set to horizontal, otherwise vertical
   * @since IDW 1.2.0
   */
  public void setHorizontal(boolean horizontal) {
    splitPane.setHorizontal(horizontal);
    ((SplitWindowItem) getWindowItem()).setHorizontal(horizontal);
  }

  protected void update() {
    splitPane.setDividerSize(getSplitWindowProperties().getDividerSize());
    splitPane.setContinuousLayout(getSplitWindowProperties().getContinuousLayoutEnabled());
    splitPane.setDividerDraggable(getSplitWindowProperties().getDividerLocationDragEnabled());
    splitPane.setDragIndicatorColor(getSplitWindowProperties().getDragIndicatorColor());
  }

  protected void optimizeWindowLayout() {
    DockingWindow parent = getWindowParent();

    if (parent != null && (getRightWindow() == null || getLeftWindow() == null)) {
      if (getRightWindow() == null && getLeftWindow() == null)
        parent.removeChildWindow(this);
      else {
        DockingWindow w = getRightWindow() == null ? getLeftWindow() : getRightWindow();
        parent.internalReplaceChildWindow(this, w.getBestFittedWindow(parent));
      }
    }
  }

  public DockingWindow getChildWindow(int index) {
    return getWindows()[index];
  }


  protected void rootChanged(RootWindow oldRoot, RootWindow newRoot) {
    super.rootChanged(oldRoot, newRoot);
    if (newRoot != null)
      splitPane.setHeavyWeightDragIndicator(newRoot.isHeavyweightSupported());
  }

  private DockingWindow[] getWindows() {
    return getLeftWindow() == null ?
           (getRightWindow() == null ? new DockingWindow[0] : new DockingWindow[]{getRightWindow()}) :
           getRightWindow() == null ?
           new DockingWindow[]{getLeftWindow()} :
           new DockingWindow[]{getLeftWindow(), getRightWindow()};
  }

  public int getChildWindowCount() {
    return getWindows().length;
  }

  public Icon getIcon() {
    return getLeftWindow() == null ?
           (getRightWindow() == null ? null : getRightWindow().getIcon()) : getLeftWindow().getIcon();
  }

  protected void doReplace(DockingWindow oldWindow, DockingWindow newWindow) {
    if (getLeftWindow() == oldWindow) {
      leftWindow = newWindow;
      splitPane.setLeftComponent(newWindow);
    }
    else {
      rightWindow = newWindow;
      splitPane.setRightComponent(newWindow);
    }

    ComponentUtil.validate(splitPane);
  }

  protected void doRemoveWindow(DockingWindow window) {
    if (window == getLeftWindow()) {
      leftWindow = null;
      splitPane.setLeftComponent(null);
    }
    else {
      rightWindow = null;
      splitPane.setRightComponent(null);
    }
  }

  protected DockingWindow oldRead(ObjectInputStream in, ReadContext context) throws IOException {
    splitPane.setHorizontal(in.readBoolean());
    splitPane.setDividerLocation(in.readFloat());
    DockingWindow leftWindow = WindowDecoder.decodeWindow(in, context);
    DockingWindow rightWindow = WindowDecoder.decodeWindow(in, context);
    super.oldRead(in, context);

    if (leftWindow != null && rightWindow != null) {
      setWindows(leftWindow, rightWindow);
      return this;
    }
    else
      return leftWindow != null ? leftWindow : rightWindow != null ? rightWindow : null;
  }


  protected void updateWindowItem(RootWindow rootWindow) {
    super.updateWindowItem(rootWindow);
    ((SplitWindowItem) getWindowItem()).setParentSplitWindowProperties(rootWindow == null ?
                                                                       SplitWindowItem.emptyProperties :
                                                                       rootWindow.getRootWindowProperties()
                                                                           .getSplitWindowProperties());
  }

  protected PropertyMap getPropertyObject() {
    return getSplitWindowProperties().getMap();
  }

  protected PropertyMap createPropertyObject() {
    return new SplitWindowProperties().getMap();
  }

  void removeWindowComponent(DockingWindow window) {
    if (window == getLeftWindow())
      splitPane.setLeftComponent(null);
    else
      splitPane.setRightComponent(null);
  }

  void restoreWindowComponent(DockingWindow window) {
    if (window == getLeftWindow())
      splitPane.setLeftComponent(leftWindow);
    else
      splitPane.setRightComponent(rightWindow);
  }

  protected int getChildEdgeDepth(DockingWindow window, Direction dir) {
    return (window == leftWindow ? dir : dir.getOpposite()) == (isHorizontal() ? Direction.RIGHT : Direction.DOWN) ? 0 :
           super.getChildEdgeDepth(window, dir);
  }

  protected DropAction doAcceptDrop(Point p, DockingWindow window) {
    DropAction da = acceptChildDrop(p, window);

    if (da != null)
      return da;

    float f = isHorizontal() ? (float) p.y / getHeight() : (float) p.x / getWidth();

    if (f <= 0.33f) {
      Direction splitDir = isHorizontal() ? Direction.UP : Direction.LEFT;
      return getSplitDropFilter().acceptDrop(new SplitDropInfo(window, this, p, splitDir)) ?
             split(window, splitDir) : null;
    }
    else if (f >= 0.66f) {
      Direction splitDir = isHorizontal() ? Direction.DOWN : Direction.RIGHT;
      return getSplitDropFilter().acceptDrop(new SplitDropInfo(window, this, p, splitDir)) ?
             split(window, splitDir) : null;
    }
    else {
      return getInteriorDropFilter().acceptDrop(new InteriorDropInfo(window, this, p)) ?
             createTabWindow(window) : null;
    }
  }

  protected void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
    out.writeInt(WindowIds.SPLIT);
    viewWriter.writeWindowItem(getWindowItem(), out, context);
    getLeftWindow().write(out, context, viewWriter);
    getRightWindow().write(out, context, viewWriter);
  }

  protected DockingWindow newRead(ObjectInputStream in, ReadContext context, ViewReader viewReader) throws IOException {
    DockingWindow leftWindow = WindowDecoder.decodeWindow(in, context, viewReader);
    DockingWindow rightWindow = WindowDecoder.decodeWindow(in, context, viewReader);

    if (leftWindow != null && rightWindow != null) {
      setWindows(leftWindow, rightWindow);
      return this;
    }
    else
      return leftWindow != null ? leftWindow : rightWindow != null ? rightWindow : null;
  }

}
