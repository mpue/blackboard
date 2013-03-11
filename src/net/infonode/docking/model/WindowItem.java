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


// $Id: WindowItem.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.docking.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.internal.ReadContext;
import net.infonode.docking.internal.WriteContext;
import net.infonode.docking.properties.DockingWindowProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapUtil;
import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
abstract public class WindowItem {
  public static final DockingWindowProperties emptyProperties = new DockingWindowProperties();

  abstract protected DockingWindow createWindow(ViewReader viewReader, ArrayList childWindows);

  abstract public WindowItem copy();

  private WindowItem parent;
  private WeakReference connectedWindow = new WeakReference(null);
  private final ArrayList windows = new ArrayList();
  private DockingWindowProperties dockingWindowProperties;
  private DockingWindowProperties parentProperties = emptyProperties;
  private Direction lastMinimizedDirection;

  protected WindowItem() {
    dockingWindowProperties = new DockingWindowProperties(emptyProperties);
  }

  protected WindowItem(WindowItem windowItem) {
    dockingWindowProperties =
      new DockingWindowProperties(windowItem.getDockingWindowProperties().getMap().copy(true, true));
    dockingWindowProperties.getMap().replaceSuperMap(windowItem.getParentDockingWindowProperties().getMap(),
        emptyProperties.getMap());
    lastMinimizedDirection = windowItem.getLastMinimizedDirection();
  }

  public boolean isRestoreWindow() {
    return parent != null && parent.isRestoreWindow();
  }

  public void addWindow(WindowItem item) {
    if (item.parent != this)
      addWindow(item, windows.size());
  }

  public void addWindow(WindowItem item, int index) {
    index = index == -1 ? windows.size() : index;

    if (item.parent == this) {
      int currentIndex = windows.indexOf(item);

      if (currentIndex != index) {
        windows.remove(currentIndex);
        windows.add(currentIndex < index ? index - 1 : index, item);
      }
    }
    else {
      item.setParent(this);
      windows.add(index, item);
    }
  }

  public void removeWindow(WindowItem item) {
    if (windows.remove(item))
      item.parent = null;
  }

  public void removeWindowRefs(DockingWindow window) {
    if (connectedWindow.get() == window)
      connectedWindow = new WeakReference(null);

    for (int i = 0; i < getWindowCount(); i++)
      getWindow(i).removeWindowRefs(window);
  }

  public void replaceWith(WindowItem item) {
    if (item == this || parent == null)
      return;

    item.setParent(parent);
    int index = parent.windows.indexOf(this);
    parent.windows.set(index, item);
    parent = null;
  }

  public int getWindowIndex(WindowItem item) {
    return windows.indexOf(item);
  }

  private void setParent(WindowItem parent) {
    if (this.parent == parent)
      return;

    if (this.parent != null)
      this.parent.removeWindow(this);

    this.parent = parent;
  }

  public final int getWindowCount() {
    return windows.size();
  }

  public final WindowItem getWindow(int index) {
    return (WindowItem) windows.get(index);
  }

  public WindowItem getParent() {
    return parent;
  }

  public void setConnectedWindow(DockingWindow window) {
    connectedWindow = new WeakReference(window);
  }

  public DockingWindow getConnectedWindow() {
    return (DockingWindow) connectedWindow.get();
  }

  public RootWindowItem getRootItem() {
    return parent == null ? null : parent.getRootItem();
  }

  public DockingWindow getVisibleDockingWindow() {
    DockingWindow window = getConnectedWindow();

    if (window != null && window.getRootWindow() != null && !window.isMinimized() && !window.isUndocked())
      return window;

    for (int i = 0; i < getWindowCount(); i++) {
      WindowItem item = getWindow(i);
      window = item.getVisibleDockingWindow();

      if (window != null)
        return window;
    }

    return null;
  }

  public DockingWindow getInsideDockingWindow() {
    if (getParent() == null)
      return null;

    DockingWindow dockingWindow = getParent().getConnectedWindow();

    if (dockingWindow != null)
      return dockingWindow;

    return getParent().getInsideDockingWindow();
  }

  public void removeAll() {
    while (getWindowCount() > 0)
      removeWindow(getWindow(0));
  }

  public boolean cleanUp() {
    for (int i = getWindowCount() - 1; i >= 0; i--) {
      if (getWindow(i).cleanUp())
        windows.remove(i);
    }

    return getWindowCount() == 0 && getConnectedWindow() == null;
  }

  public DockingWindow getFirstChildWindow() {
    for (int i = 0; i < getWindowCount(); i++) {
      DockingWindow window = getWindow(i).getFirstWindow();

      if (window != null)
        return window;
    }

    return null;
  }

  public DockingWindow getFirstWindow() {
    DockingWindow window = getConnectedWindow();
    return window != null ? window : getFirstChildWindow();
  }

  public WindowItem getChildWindowContaining(WindowItem windowItem) {
    while (windowItem.getParent() != this) {
      windowItem = windowItem.getParent();

      if (windowItem == null)
        return null;
    }

    return windowItem;
  }

  public boolean hasAncestor(WindowItem ancestor) {
    return this == ancestor || (parent != null && parent.hasAncestor(ancestor));
  }

  public WindowItem getTopItem() {
    return parent == null ? this : parent.getTopItem();
  }

  public DockingWindowProperties getDockingWindowProperties() {
    if (dockingWindowProperties == null) {
      dockingWindowProperties = new DockingWindowProperties(emptyProperties);
      parentProperties = emptyProperties;
    }

    return dockingWindowProperties;
  }

  public DockingWindowProperties getParentDockingWindowProperties() {
    return parentProperties == null ? emptyProperties : parentProperties;
  }

  public void setParentDockingWindowProperties(DockingWindowProperties parentProperties) {
    dockingWindowProperties.getMap().replaceSuperMap(this.parentProperties.getMap(),
        parentProperties.getMap());
    this.parentProperties = parentProperties;
  }

  public Direction getLastMinimizedDirection() {
    return lastMinimizedDirection;
  }

  public void setLastMinimizedDirection(Direction lastMinimizedDirection) {
    this.lastMinimizedDirection = lastMinimizedDirection;
  }

  public void writeSettings(ObjectOutputStream out, WriteContext context) throws IOException {
    out.writeInt(getLastMinimizedDirection() == null ? -1 : getLastMinimizedDirection().getValue());

    if (context.getWritePropertiesEnabled()) {
      dockingWindowProperties.getMap().write(out, true);
      getPropertyObject().write(out, true);
    }
  }

  public void readSettings(ObjectInputStream in, ReadContext context) throws IOException {
    if (context.getVersion() > 1) {
      int dir = in.readInt();
      setLastMinimizedDirection(dir == -1 ? null : Direction.getDirections()[dir]);
    }

    if (context.isPropertyValuesAvailable()) {
      if (context.getReadPropertiesEnabled()) {
        dockingWindowProperties.getMap().read(in);
        getPropertyObject().read(in);
      }
      else {
        PropertyMapUtil.skipMap(in);
        PropertyMapUtil.skipMap(in);
      }
    }
  }

  public void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
    out.writeInt(getWindowCount());

    for (int i = 0; i < getWindowCount(); i++)
      getWindow(i).write(out, context, viewWriter);

    DockingWindow window = getConnectedWindow();
    writeSettings(out, context);
    //    boolean b = window != null && !window.isMinimized() && !window.isUndocked() && window.getRootWindow() != null;
    out.writeBoolean(window != null && !window.isMinimized() && !window.isUndocked() && window.getRootWindow() != null);
  }

  public DockingWindow read(ObjectInputStream in, ReadContext context, ViewReader viewReader) throws IOException {
    ArrayList childWindows = readChildWindows(in, context, viewReader);
    readSettings(in, context);
    return in.readBoolean() ?
                             createWindow(viewReader, childWindows) :
                               childWindows.size() > 0 ? (DockingWindow) childWindows.get(0) : null;
  }

  public ArrayList readChildWindows(ObjectInputStream in, ReadContext context, ViewReader viewReader) throws
  IOException {
    int count = in.readInt();
    removeAll();
    ArrayList childWindows = new ArrayList();

    for (int i = 0; i < count; i++) {
      WindowItem childItem = WindowItemDecoder.decodeWindowItem(in, context, viewReader);
      addWindow(childItem);
      DockingWindow cw = childItem.read(in, context, viewReader);

      if (cw != null)
        childWindows.add(cw);
    }

    return childWindows;
  }

  protected PropertyMap getPropertyObject() {
    return null;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();
    DockingWindow dw = getConnectedWindow();
    s.append(dw + ":\n");

    for (int i = 0; i < windows.size(); i++)
      s.append("  " + windows.get(i).toString());

    return s.toString();
  }

  public void clearWindows() {
    removeAll();
  }

}
