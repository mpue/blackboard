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


// $Id: RootWindow.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import net.infonode.docking.action.MaximizeWithAbortWindowAction;
import net.infonode.docking.action.NullWindowAction;
import net.infonode.docking.action.RestoreFocusWindowAction;
import net.infonode.docking.action.RestoreParentWithAbortWindowAction;
import net.infonode.docking.action.RestoreWithAbortWindowAction;
import net.infonode.docking.action.StateDependentWindowAction;
import net.infonode.docking.drop.ChildDropInfo;
import net.infonode.docking.drop.InteriorDropInfo;
import net.infonode.docking.internal.HeavyWeightContainer;
import net.infonode.docking.internal.HeavyWeightDragRectangle;
import net.infonode.docking.internal.ReadContext;
import net.infonode.docking.internal.WriteContext;
import net.infonode.docking.internalutil.DropAction;
import net.infonode.docking.model.RootWindowItem;
import net.infonode.docking.model.SplitWindowItem;
import net.infonode.docking.model.TabWindowItem;
import net.infonode.docking.model.ViewItem;
import net.infonode.docking.model.ViewReader;
import net.infonode.docking.model.ViewWriter;
import net.infonode.docking.model.WindowItem;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.util.DockingUtil;
import net.infonode.gui.CursorManager;
import net.infonode.gui.DragLabelWindow;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.gui.componentpainter.RectangleComponentPainter;
import net.infonode.gui.layout.BorderLayout2;
import net.infonode.gui.layout.LayoutUtil;
import net.infonode.gui.layout.StretchLayout;
import net.infonode.gui.mouse.MouseButtonListener;
import net.infonode.gui.panel.SimplePanel;
import net.infonode.gui.shaped.panel.ShapedPanel;
import net.infonode.properties.gui.InternalPropertiesUtil;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapManager;
import net.infonode.util.ArrayUtil;
import net.infonode.util.Direction;
import net.infonode.util.ReadWritable;

/**
 * The root window is a top level container for docking windows. Docking windows can't be dragged outside of their root
 * window. The property values of a root window is inherited to the docking windows inside it.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class RootWindow extends DockingWindow implements ReadWritable {
  private static final int SERIALIZE_VERSION = 4;

  private static final int FLOATING_WINDOW_MIN_WIDTH = 400;
  private static final int FLOATING_WINDOW_MIN_HEIGHT = 300;

  private boolean heavyweightSupport = false;

  private class SingleComponentLayout implements LayoutManager {
    public void addLayoutComponent(String name, Component comp) {
    }

    public void layoutContainer(Container parent) {
      Dimension size = LayoutUtil.getInteriorSize(parent);
      Insets insets = parent.getInsets();
      mainPanel.setBounds(insets.left, insets.top, size.width, size.height);

      int w1 = windowBars[Direction.LEFT.getValue()].getPreferredSize().width;
      int w2 = windowBars[Direction.RIGHT.getValue()].getPreferredSize().width;
      int h1 = windowBars[Direction.UP.getValue()].getPreferredSize().height;
      int h2 = windowBars[Direction.DOWN.getValue()].getPreferredSize().height;

      final Direction[] directions = Direction.getDirections();

      for (int i = 0; i < windowBars.length; i++) {
        Component panel = windowBars[i].getEdgePanel();

        if (panel.isVisible()) {
          Direction dir = directions[i];
          int maxWidth = size.width - w1 - w2;
          int maxHeight = size.height - h1 - h2;

          if (dir == Direction.RIGHT) {
            int rightX = parent.getWidth() - insets.right - w2 + windowBars[dir.getValue()].getInsets().left;
            int width = Math.min(panel.getPreferredSize().width,
                maxWidth + windowBars[dir.getValue()].getInsets().left);
            panel.setBounds(rightX - width, insets.top + h1, width, maxHeight);
          }
          else if (dir == Direction.LEFT) {
            int x = insets.left + w1 - windowBars[dir.getValue()].getInsets().right;
            int width = Math.min(panel.getPreferredSize().width,
                maxWidth + windowBars[dir.getValue()].getInsets().right);
            panel.setBounds(x, insets.top + h1, width, maxHeight);
          }
          else if (dir == Direction.DOWN) {
            int bottomY = parent.getHeight() - insets.bottom - h2 + windowBars[dir.getValue()].getInsets().top;
            int height = Math.min(panel.getPreferredSize().height,
                maxHeight + windowBars[dir.getValue()].getInsets().top);
            panel.setBounds(insets.left + w1, bottomY - height, maxWidth, height);
          }
          else {
            int y = insets.top + h1 - windowBars[dir.getValue()].getInsets().bottom;
            int height = Math.min(panel.getPreferredSize().height,
                maxHeight + windowBars[dir.getValue()].getInsets().bottom);
            panel.setBounds(insets.left + w1, y, maxWidth, height);
          }
        }
      }
    }

    public Dimension minimumLayoutSize(Container parent) {
      return LayoutUtil.add(mainPanel.getMinimumSize(), parent.getInsets());
    }

    public Dimension preferredLayoutSize(Container parent) {
      return LayoutUtil.add(mainPanel.getPreferredSize(), parent.getInsets());
    }

    public void removeLayoutComponent(Component comp) {
    }

  }

  private final ShapedPanel layeredPane = new ShapedPanel() {
    public boolean isOptimizedDrawingEnabled() {
      return false;
    }
  };

  private final ShapedPanel windowPanel = new ShapedPanel(new StretchLayout(true, true));

  private final SimplePanel mainPanel = new SimplePanel();

  private JFrame dummyFrame;

  private final ViewSerializer viewSerializer;
  private DockingWindow window;
  //  private View lastFocusedView;
  private final WindowBar[] windowBars = new WindowBar[Direction.getDirections().length];
  private final ArrayList floatingWindows = new ArrayList();
  private DockingWindow maximizedWindow;
  private View focusedView;
  private ArrayList lastFocusedWindows = new ArrayList(4);
  private ArrayList focusedWindows = new ArrayList(4);
  private final ArrayList views = new ArrayList();
  private boolean cleanUpModel;
  private final Runnable modelCleanUpEvent = new Runnable() {
    public void run() {
      if (cleanUpModel) {
        cleanUpModel = false;
        getWindowItem().cleanUp();
      }
    }
  };

  private final JLabel dragTextLabel = new JLabel();
  private Container dragTextContainer;

  private DragLabelWindow dragTextWindow;

  private final Component dragRectangle;
  private JRootPane currentDragRootPane;

  /**
   * Creates an empty root window.
   *
   * @param viewSerializer used when reading and writing views
   * @since IDW 1.1.0
   */
  public RootWindow(ViewSerializer viewSerializer) {
    this(false, viewSerializer);
  }

  /**
   * Creates an empty root window with support for heavyweight components inside the
   * views.
   *
   * @param heavyweightSupport true for heavy weight component support, otherwise false
   * @param viewSerializer     used when reading and writing views
   * @since IDW 1.4.0
   */
  public RootWindow(boolean heavyweightSupport, ViewSerializer viewSerializer) {
    super(new RootWindowItem());
    this.heavyweightSupport = heavyweightSupport;

    dragRectangle = heavyweightSupport ? new HeavyWeightDragRectangle() : (Component) new ShapedPanel();

    getWindowProperties().addSuperObject(getRootWindowProperties().getDockingWindowProperties());

    mainPanel.setLayout(new BorderLayout2());
    mainPanel.add(windowPanel, new Point(1, 1));

    createWindowBars();

    layeredPane.add(mainPanel);
    layeredPane.setLayout(new SingleComponentLayout());
    setComponent(layeredPane);

    this.viewSerializer = viewSerializer;

    dragTextLabel.setOpaque(true);

    if (heavyweightSupport) {
      dragTextContainer = new HeavyWeightContainer(dragTextLabel, true);
      dragTextContainer.validate();
    }
    else
      dragTextContainer = dragTextLabel;

    init();
    FocusManager.getInstance();

    addTabMouseButtonListener(new MouseButtonListener() {
      public void mouseButtonEvent(MouseEvent event) {
        if (event.isConsumed())
          return;

        DockingWindow window = (DockingWindow) event.getSource();

        if (event.getID() == MouseEvent.MOUSE_PRESSED &&
            event.getButton() == MouseEvent.BUTTON1 &&
            !event.isShiftDown() &&
            window.isShowing()) {
          RestoreFocusWindowAction.INSTANCE.perform(window);
        }
        else if (event.getID() == MouseEvent.MOUSE_CLICKED && event.getButton() == MouseEvent.BUTTON1) {
          if (event.getClickCount() == 2) {
            if ((window.getWindowParent() instanceof WindowBar) && getRootWindowProperties()
                .getDoubleClickRestoresWindow())
              RestoreWithAbortWindowAction.INSTANCE.perform(window);
            else {
              new StateDependentWindowAction(MaximizeWithAbortWindowAction.INSTANCE,
                  NullWindowAction.INSTANCE,
                  RestoreParentWithAbortWindowAction.INSTANCE).perform(window);
            }
          }
        }
      }
    });
  }

  /**
   * Creates a root window with the given window as window inside this root window.
   *
   * @param viewSerializer used when reading and writing views
   * @param window         the window that is placed inside the root window
   */
  public RootWindow(ViewSerializer viewSerializer, DockingWindow window) {
    this(false, viewSerializer, window);
  }

  /**
   * Creates a root window with support for heavyweight components inside the views and the
   * given window inside as window inside this root window.
   *
   * @param heavyweightSupport true for heavy weight component support, otherwise false
   * @param viewSerializer     used when reading and writing views
   * @param window             the window that is placed inside the root window
   * @since IDW 1.4.0
   */
  public RootWindow(boolean heavyweightSupport, ViewSerializer viewSerializer, DockingWindow window) {
    this(heavyweightSupport, viewSerializer);
    setWindow(window);
  }

  /**
   * Returns the view that currently contains the focus.
   *
   * @return The currently focused view, null if no view has focus
   */
  public View getFocusedView() {
    return focusedView;
  }

  void addFocusedWindow(DockingWindow window) {
    for (int i = 0; i < lastFocusedWindows.size(); i++) {
      if (((WeakReference) lastFocusedWindows.get(i)).get() == window)
        return;
    }

    lastFocusedWindows.add(new WeakReference(window));
  }

  void setFocusedView(View view) {
    if (view == focusedView)
      return;

    //    System.out.println(focusedView + " -> " + view);

    View previouslyFocusedView = focusedView;
    focusedView = view;

    for (DockingWindow w = view; w != null; w = w.getWindowParent()) {
      focusedWindows.add(new WeakReference(w));

      for (int i = 0; i < lastFocusedWindows.size(); i++) {
        if (((WeakReference) lastFocusedWindows.get(i)).get() == w) {
          lastFocusedWindows.remove(i);
          break;
        }
      }
    }

    for (int i = 0; i < lastFocusedWindows.size(); i++) {
      DockingWindow w = (DockingWindow) ((WeakReference) lastFocusedWindows.get(i)).get();

      if (w != null) {
        w.setFocused(false);
      }
    }

    ArrayList temp = lastFocusedWindows;
    lastFocusedWindows = focusedWindows;
    focusedWindows = temp;

    for (int i = 0; i < lastFocusedWindows.size(); i++) {
      DockingWindow w = (DockingWindow) ((WeakReference) lastFocusedWindows.get(i)).get();

      if (w != null)
        w.setFocused(true);
    }

    if (view != null) {
      view.childGainedFocus(null, view);
    }
    else {
      clearFocus(null);
    }

    // Notify windows that are not ancestors of the new focused view
    for (int i = 0; i < focusedWindows.size(); i++) {
      DockingWindow w = (DockingWindow) ((WeakReference) focusedWindows.get(i)).get();

      if (w != null) {
        w.fireViewFocusChanged(previouslyFocusedView, focusedView);
      }
    }

    // Notify windows that are ancestors of the new focused view
    for (int i = 0; i < lastFocusedWindows.size(); i++) {
      DockingWindow w = (DockingWindow) ((WeakReference) lastFocusedWindows.get(i)).get();

      if (w != null) {
        w.fireViewFocusChanged(previouslyFocusedView, focusedView);
      }
    }

    focusedWindows.clear();
  }

  /**
   * Returns the property values for this root window. The property values will be inherited to docking windows inside
   * this root window.
   *
   * @return the property values for this root window
   */
  public RootWindowProperties getRootWindowProperties() {
    return ((RootWindowItem) getWindowItem()).getRootWindowProperties();
  }

  /**
   * Returns the direction of the closest enabled window bar to a docking window. The distance is measured from the
   * window edge that is furthest away from the bar.
   *
   * @param window the docking window
   * @return the direction of the closest enabled window bar to a docking window
   */
  public Direction getClosestWindowBar(DockingWindow window) {
    Point pos = SwingUtilities.convertPoint(window.getParent(), window.getLocation(), this);

    int[] distances = new int[]{
                                getWindowBar(Direction.UP).isEnabled() ? pos.y + window.getHeight() : Integer.MAX_VALUE,
                                                                       getWindowBar(Direction.DOWN).isEnabled() ? getHeight() - pos.y : Integer.MAX_VALUE,
                                                                                                                getWindowBar(Direction.LEFT).isEnabled() ? pos.x + window.getWidth() : Integer.MAX_VALUE,
                                                                                                                                                         getWindowBar(Direction.RIGHT).isEnabled() ? getWidth() - pos.x : Integer.MAX_VALUE};

    Direction dir =
      new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}[ArrayUtil.findSmallest(
          distances)];
    return getWindowBar(dir).isEnabled() ? dir : null;
  }

  /**
   * Returns the window bar in the direction.
   *
   * @param direction the direction
   * @return the window bar in the direction
   */
  public WindowBar getWindowBar(Direction direction) {
    return windowBars[direction.getValue()];
  }

  /**
   * Sets the top level docking window inside this root window.
   *
   * @param newWindow the top level docking window
   */
  public void setWindow(DockingWindow newWindow) {
    if (window == newWindow)
      return;

    if (window == null) {
      DockingWindow actualWindow = addWindow(newWindow);
      doReplace(null, actualWindow);

      if (getUpdateModel() && actualWindow.getWindowItem().getRootItem() != getWindowItem()) {
        getWindowItem().removeAll();
        addWindowItem(actualWindow, -1);
      }
    }
    else if (newWindow == null) {
      removeChildWindow(window);
      window = null;
    }
    else
      replaceChildWindow(window, newWindow);
  }

  /**
   * Returns the top level docking window inside this root window.
   *
   * @return the top level docking window inside this root window
   */
  public DockingWindow getWindow() {
    return window;
  }

  /**
   * <p>
   * Creates and shows a floating window with the given window as top-level window in the floating window or without
   * any top-level window i.e. empty floating window.
   * </p>
   *
   * <p>
   * <strong>Note 1:</strong> The created floating window is not visible per default. To make it visible, call
   * {@link FloatingWindow}.getTopLevelAncestor().setVisible(true);
   * </p>
   *
   * <p>
   * <strong>Note 2:</strong> Floating windows are dynamically created when a window is undocked and closed/removed
   * when all windows inside the floating window has been removed (i.e. cloased/docked/undocked to another floating
   * window) from the floating window. The root window has a refernce to the floating window as long as the floating
   * window has windows inside it i.e. it is not necessary to keep references to the floating window because the root
   * window will handle this.
   * </p>
   *
   * @param location  the floating window's location on the screen
   * @param innerSize the inner dimension of the floating window's top level container i.e.the size of the root pane
   * @param window    the docking window that is the top level window in this floating window or null for no top-level
   *                  window i.e. empty floating window
   * @return the floating window
   * @since IDW 1.4.0
   */
  public FloatingWindow createFloatingWindow(Point location, Dimension innerSize, DockingWindow window) {
    FloatingWindow fw = new FloatingWindow(this, window, location, innerSize);
    floatingWindows.add(fw);
    addWindow(fw);
    return fw;
  }

  FloatingWindow createFloatingWindow() {
    FloatingWindow fw = new FloatingWindow(this);
    floatingWindows.add(fw);
    addWindow(fw);
    return fw;
  }

  FloatingWindow createFloatingWindow(DockingWindow window, Point p) {
    Dimension size = window.getSize();
    if (size.width <= 0 || size.height <= 0) {
      Dimension preferred = window.getPreferredSize();
      size =
        new Dimension(Math.max(FLOATING_WINDOW_MIN_WIDTH, preferred.width),
            Math.max(FLOATING_WINDOW_MIN_HEIGHT, preferred.height));
    }

    FloatingWindow fw = createFloatingWindow(p, size, window);
    fw.getTopLevelAncestor().setVisible(true);

    return fw;
  }

  void removeFloatingWindow(FloatingWindow fw) {
    floatingWindows.remove(fw);
    removeWindow(fw);
  }

  /**
   * Returns the view serializer object for the views inside this root window.
   *
   * @return the view serializer object for the views inside this root window
   */
  public ViewSerializer getViewSerializer() {
    return viewSerializer;
  }

  public DockingWindow getChildWindow(int index) {
    return index < 4 ? windowBars[index] :
      window != null ? (index == 4 ? window : (DockingWindow) floatingWindows.get(index - 5)) :
        (DockingWindow) floatingWindows.get(index - 4);
  }

  public int getChildWindowCount() {
    return 4 + (window == null ? 0 : 1) + floatingWindows.size();
  }

  public Icon getIcon() {
    return null;
  }

  /**
   * Writes the state of this root window and all child windows.
   *
   * @param out the stream on which to write the state
   * @throws IOException if there is a stream error
   */
  public void write(ObjectOutputStream out) throws IOException {
    write(out, true);
  }

  /**
   * Writes the state of this root window and all child windows.
   *
   * @param out             the stream on which to write the state
   * @param writeProperties true if the property values for all docking windows should be written to the stream
   * @throws IOException if there is a stream error
   */
  public void write(ObjectOutputStream out, boolean writeProperties) throws IOException {
    cleanUpModel();
    out.writeInt(SERIALIZE_VERSION);
    out.writeBoolean(writeProperties);
    WriteContext context = new WriteContext(writeProperties, getViewSerializer());

    final ArrayList v = new ArrayList();

    for (int i = 0; i < views.size(); i++) {
      View view = (View) ((WeakReference) views.get(i)).get();

      if (view != null)
        v.add(view);
    }

    writeViews(v, out, context);
    ViewWriter viewWriter = new ViewWriter() {
      public void writeWindowItem(WindowItem windowItem, ObjectOutputStream out, WriteContext context) throws
      IOException {
        if (windowItem.getRootItem() == getWindowItem()) {
          out.writeBoolean(true);
          writeWindowItemIndex(windowItem, out);
          out.writeInt(-1);
        }
        else {
          out.writeBoolean(false);
          windowItem.writeSettings(out, context);
        }
      }

      public void writeView(View view, ObjectOutputStream out, WriteContext context) throws IOException {
        for (int i = 0; i < v.size(); i++) {
          if (v.get(i) == view) {
            out.writeInt(i);
            return;
          }
        }

        out.writeInt(-1);
      }
    };
    getWindowItem().write(out, context, viewWriter);

    for (int i = 0; i < 4; i++)
      windowBars[i].write(out, context, viewWriter);

    out.writeInt(floatingWindows.size());

    for (int i = 0; i < floatingWindows.size(); i++)
      ((FloatingWindow) floatingWindows.get(i)).write(out, context, viewWriter);

    writeLocations(out);

    if (maximizedWindow != null)
      writeMaximized(maximizedWindow, out);

    out.writeInt(-1);
  }

  private void writeWindowItemIndex(WindowItem item, ObjectOutputStream out) throws IOException {
    if (item.getParent() == null)
      return;

    writeWindowItemIndex(item.getParent(), out);
    int index = item.getParent().getWindowIndex(item);
    out.writeInt(index);
  }

  private void writeMaximized(DockingWindow window, ObjectOutputStream out) throws IOException {
    DockingWindow parent = window.getWindowParent();

    if (parent != null) {
      writeMaximized(parent, out);
      out.writeInt(parent.getChildWindowIndex(window));
    }
  }

  private static void writeViews(ArrayList views, ObjectOutputStream out, WriteContext context) throws IOException {
    out.writeInt(views.size());

    for (int i = 0; i < views.size(); i++)
      ((View) views.get(i)).write(out, context);
  }

  /**
   * Reads a previously written window state. This will create child windows and read their state.
   *
   * @param in the stream from which to read the state
   * @throws IOException if there is a stream error
   */
  public void read(ObjectInputStream in) throws IOException {
    read(in, true);
  }

  private void oldInternalRead(ObjectInputStream in, ReadContext context) throws IOException {
    setWindow(in.readBoolean() ? WindowDecoder.decodeWindow(in, context) : null);

    for (int i = 0; i < 4; i++) {
      in.readInt(); // DockingWindow bar ID
      windowBars[i].oldRead(in, context);
    }

    super.oldRead(in, context);
    readLocations(in, this, context.getVersion());

    if (context.getVersion() > 1) {
      int viewCount = in.readInt();

      for (int i = 0; i < viewCount; i++) {
        View view = (View) WindowDecoder.decodeWindow(in, context);
        view.setRootWindow(this);
        view.readLocations(in, this, context.getVersion());
      }
    }
  }

  private void newInternalRead(ObjectInputStream in, ReadContext context) throws IOException {
    beginUpdateModel();

    try {
      int viewCount = in.readInt();
      final View[] views = new View[viewCount];

      for (int i = 0; i < viewCount; i++) {
        views[i] = View.read(in, context);

        if (views[i] != null)
          views[i].setRootWindow(this);
      }

      ViewReader viewReader = new ViewReader() {
        public ViewItem readViewItem(ObjectInputStream in, ReadContext context) throws IOException {
          View view = readView(in, context);
          return view == null ? new ViewItem() : (ViewItem) view.getWindowItem();
        }

        public WindowItem readWindowItem(ObjectInputStream in, ReadContext context) throws IOException {
          if (in.readBoolean()) {
            int index;
            WindowItem item = getWindowItem();

            while ((index = in.readInt()) != -1) {
              item = item.getWindow(index);
            }

            return item;
          }
          else
            return null;
        }

        public TabWindow createTabWindow(DockingWindow[] childWindows, TabWindowItem windowItem) {
          TabWindow tabWindow = new TabWindow(childWindows, windowItem);
          tabWindow.updateSelectedTab();
          return tabWindow;
        }

        public SplitWindow createSplitWindow(DockingWindow leftWindow,
                                             DockingWindow rightWindow,
                                             SplitWindowItem windowItem) {
          return new SplitWindow(windowItem.isHorizontal(),
              windowItem.getDividerLocation(),
              leftWindow,
              rightWindow,
              windowItem);
        }

        public View readView(ObjectInputStream in, ReadContext context) throws IOException {
          int id = in.readInt();
          return id == -1 ? null : (View) views[id];
        }
      };
      setWindow(getWindowItem().read(in, context, viewReader));

      for (int i = 0; i < 4; i++)
        windowBars[i].newRead(in, context, viewReader);

      if (context.getVersion() >= 4) {
        int count = in.readInt();

        for (int i = 0; i < count; i++) {
          FloatingWindow w = createFloatingWindow();
          w.read(in, context, viewReader);
        }

      }

      readLocations(in, this, context.getVersion());
    }
    finally {
      endUpdateModel();
    }
  }

  /**
   * Reads a previously written window state. This will create child windows and read their state.
   *
   * @param in             the stream from which to read the state
   * @param readProperties true if the property values for all child windows should be read. This parameter can be set
   *                       to true or false regardless of if the property values was included when the state was
   *                       written, though obviously no property values are read if there aren't any in the stream.
   * @throws IOException if there is a stream error
   */
  public void read(ObjectInputStream in, boolean readProperties) throws IOException {
    FocusManager.getInstance().startIgnoreFocusChanges();
    PropertyMapManager.getInstance().beginBatch();

    try {
      setWindow(null);

      while (floatingWindows.size() > 0) {
        ((FloatingWindow) floatingWindows.get(0)).close();
      }

      int serializeVersion = in.readInt();

      if (serializeVersion > SERIALIZE_VERSION)
        throw new IOException(
        "Can't read serialized data because it was written by a later version of InfoNode Docking Windows!");

      ReadContext context = new ReadContext(this, serializeVersion, in.readBoolean(), readProperties);

      if (context.getVersion() < 3)
        oldInternalRead(in, context);
      else
        newInternalRead(in, context);

      if (serializeVersion > 1)
        readMaximized(in);

      FocusManager.focusWindow(this);
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          FocusManager.getInstance().stopIgnoreFocusChanges();
        }
      });
    }
  }

  private void readMaximized(ObjectInputStream in) throws IOException {
    int index;
    DockingWindow w = this;

    while ((index = in.readInt()) != -1) {
      if (index >= w.getChildWindowCount()) {
        while (in.readInt() != -1) ;
        return;
      }

      w = w.getChildWindow(index);
    }

    if (w != this)
      setMaximizedWindow(w);
  }

  /**
   * Returns the maximized window in this root window, or null if there no maximized window.
   *
   * @return the maximized window in this root window, or null if there no maximized window
   * @since IDW 1.1.0
   */
  public DockingWindow getMaximizedWindow() {
    return maximizedWindow;
  }

  /**
   * Sets the maximized window in this root window.
   * This method takes the window component and displays it at the top in the root window. It does NOT modify the
   * window tree structure, ie the window parent remains the unchanged.
   *
   * @param window the maximized window in this root window, null means no maximized window
   * @since IDW 1.1.0
   */
  public void setMaximizedWindow(DockingWindow window) {
    /*if (window == maximizedWindow)
      return;*/

    if (window != null && (window.isMinimized() || window.isUndocked()))
      return;

    internalSetMaximizedWindow(window);
  }

  void addView(View view) {
    int freeIndex = views.size();

    for (int i = 0; i < views.size(); i++) {
      View v = (View) ((WeakReference) views.get(i)).get();

      if (v == view)
        return;

      if (v == null)
        freeIndex = i;
    }

    views.add(freeIndex, new WeakReference(view));
  }

  /**
   * Removes all internal references to a view. It's not possible to restore the view to the previous location
   * after this method has been called.
   *
   * If the view is located in the window tree where this is the root nothing happens.
   *
   * @param view all internal references to this view are removed
   * @since IDW 1.4.0
   */
  public void removeView(View view) {
    if (view.getRootWindow() == this)
      return;

    if (focusedView == view)
      focusedView = null;

    removeWeak(view, views);
    removeWeak(view, lastFocusedWindows);
    removeWeak(view, focusedWindows);

    getWindowItem().removeWindowRefs(view);
  }

  private void removeWeak(Object item, java.util.List l) {
    for (int i = 0; i < l.size(); i++) {
      Object o = ((WeakReference) l.get(i)).get();

      if (o == item) {
        l.remove(i);
        return;
      }
    }
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
        windowPanel.remove(oldMaximized);

      focusWindow = oldMaximized;
      fireWindowRestored(oldMaximized);
    }

    maximizedWindow = window;

    if (maximizedWindow != null) {
      if (maximizedWindow.getWindowParent() != null)
        maximizedWindow.getWindowParent().removeWindowComponent(maximizedWindow);

      if (maximizedWindow != this.window) {
        windowPanel.add(maximizedWindow);

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

  private void createWindowBars() {
    final Direction[] directions = Direction.getDirections();

    for (int i = 0; i < directions.length; i++) {
      windowBars[i] = new WindowBar(this, directions[i]);
      windowBars[i].setEnabled(false);
      addWindow(windowBars[i]);
      layeredPane.add(windowBars[i].getEdgePanel());

      mainPanel.add(windowBars[i],
          new Point(directions[i] == Direction.LEFT ?
                                                     0 :
                                                       directions[i] == Direction.RIGHT ? 2 : 1,
                                                                                        directions[i] == Direction.UP ?
                                                                                                                       0 :
                                                                                                                         directions[i] == Direction.DOWN ? 2 : 1));

      windowBars[i].addPropertyChangeListener("enabled", new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
          updateButtonVisibility();
        }
      });
    }
  }

  JComponent getLayeredPane() {
    return layeredPane;
  }

  JComponent getWindowPanel() {
    return windowPanel;
  }

  protected void showChildWindow(DockingWindow window) {
    if (maximizedWindow != null && window == this.window)
      setMaximizedWindow(null);

    super.showChildWindow(window);
  }

  protected void update() {
    RootWindowProperties properties = getRootWindowProperties();
    properties.getComponentProperties().applyTo(layeredPane);
    InternalPropertiesUtil.applyTo(properties.getShapedPanelProperties(), layeredPane);

    properties.getWindowAreaProperties().applyTo(windowPanel);
    InternalPropertiesUtil.applyTo(properties.getWindowAreaShapedPanelProperties(), windowPanel);

    properties.getDragLabelProperties().applyTo(dragTextLabel);

    if (!heavyweightSupport) {
      ShapedPanel dragRectangle = (ShapedPanel) this.dragRectangle;
      InternalPropertiesUtil.applyTo(properties.getDragRectangleShapedPanelProperties(), dragRectangle);

      if (dragRectangle.getComponentPainter() == null)
        dragRectangle.setComponentPainter(
            new RectangleComponentPainter(Color.BLACK, properties.getDragRectangleBorderWidth()));
    }
    else {
      HeavyWeightDragRectangle rectangle = (HeavyWeightDragRectangle) dragRectangle;
      ComponentPainter painter = properties.getDragRectangleShapedPanelProperties().getComponentPainter();
      Color color = painter != null ? painter.getColor(this) : null;
      rectangle.setColor(color != null ? color : Color.BLACK);
      rectangle.setBorderWidth(properties.getDragRectangleBorderWidth());
    }
  }

  void internalStartDrag(JComponent component) {
    currentDragRootPane = getRootPane();

    FloatingWindow fwStartedDrag = DockingUtil.getFloatingWindowFor((DockingWindow) component);

    for (int i = 0; i < floatingWindows.size(); i++) {
      FloatingWindow fw = (FloatingWindow) floatingWindows.get(i);
      fw.startDrag();
      if (dummyFrame != null && fw != fwStartedDrag)
        ((JDialog) (fw.getTopLevelAncestor())).toFront();
    }

    if (dummyFrame != null && fwStartedDrag != null)
      ((JDialog) (fwStartedDrag.getTopLevelAncestor())).toFront();
  }

  void stopDrag() {
    if (dragTextContainer.getParent() != null) {
      if (!heavyweightSupport)
        dragTextContainer.getParent().repaint(dragTextContainer.getX(),
            dragTextContainer.getY(),
            dragTextContainer.getWidth(),
            dragTextContainer.getHeight());
      dragTextContainer.getParent().remove(dragTextContainer);
      //dragTextContainer.setVisible(false);
    }

    if (dragTextWindow != null) {
      dragTextWindow.setVisible(false);
      dragTextWindow.dispose();
      dragTextWindow = null;
    }

    if (dragRectangle.getParent() != null) {
      if (!heavyweightSupport)
        dragRectangle.getParent().repaint(dragRectangle.getX(),
            dragRectangle.getY(),
            dragRectangle.getWidth(),
            dragRectangle.getHeight());
      dragRectangle.getParent().remove(dragRectangle);
    }

    CursorManager.resetGlobalCursor(getCurrentDragRootPane());
    currentDragRootPane = null;

    for (int i = 0; i < floatingWindows.size(); i++) {
      ((FloatingWindow) floatingWindows.get(i)).stopDrag();
    }
  }

  boolean floatingWindowsContainPoint(Point p) {
    for (int i = 0; i < floatingWindows.size(); i++) {
      FloatingWindow c = ((FloatingWindow) floatingWindows.get(i));

      if (c.isShowing() && c.windowContainsPoint(SwingUtilities.convertPoint(getRootPane(), p, c)))
        return true;
    }

    return false;
  }

  void setCurrentDragRootPane(JRootPane rootPane) {
    //if (rootPane != currentDragRootPane) {
    CursorManager.resetGlobalCursor(getCurrentDragRootPane());
    currentDragRootPane = rootPane;
    //}
  }

  JRootPane getCurrentDragRootPane() {
    return currentDragRootPane == null ? getRootPane() : currentDragRootPane;
  }

  Component getTopLevelComponent() {
    Component c = getTopLevelAncestor();

    if (c instanceof JFrame || c instanceof JDialog)
      return c;

    if (dummyFrame == null) {
      dummyFrame = new JFrame("");
      dummyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    return dummyFrame;
  }

  boolean isHeavyweightSupported() {
    return heavyweightSupport;
  }

  private static boolean reparent(Container parent, Component c) {
    if (c.getParent() != parent) {
      if (c.getParent() != null)
        c.getParent().remove(c);

      parent.add(c);
      return true;
    }

    return false;
  }

  void setDragCursor(Cursor cursor) {
    CursorManager.setGlobalCursor(getCurrentDragRootPane(), cursor);

    if (dragTextWindow != null)
      dragTextWindow.setCursor(cursor);
  }

  void setDragText(Point textPoint, String text) {
    if (textPoint != null) {
      JRootPane currentDragRootPane = getCurrentDragRootPane();

      if (reparent(currentDragRootPane.getLayeredPane(), dragTextContainer))
        currentDragRootPane.getLayeredPane().setLayer(dragTextContainer,
            JLayeredPane.DRAG_LAYER.intValue() +
            (heavyweightSupport ? -1 : 1));

      dragTextLabel.setText(text);
      dragTextContainer.setSize(dragTextContainer.getPreferredSize());
      Point p2 = SwingUtilities.convertPoint(currentDragRootPane, textPoint, dragTextContainer.getParent());
      int yLocationOffs = dragTextLabel.getInsets().bottom;
      dragTextContainer.setLocation((int) (p2.getX() - dragTextContainer.getWidth() / 2),
          (int) (p2.getY() - dragTextContainer.getHeight() + yLocationOffs));

      // Drag text window
      Point rp = SwingUtilities.convertPoint(currentDragRootPane, textPoint, getRootPane());

      if (!getRootPane().contains(rp) && !floatingWindowsContainPoint(rp)) {
        dragTextContainer.setVisible(false);

        if (dragTextWindow == null) {
          Component c = getTopLevelComponent();
          if (c instanceof Frame)
            dragTextWindow = new DragLabelWindow((Frame) c);
          else if (c instanceof Dialog)
            dragTextWindow = new DragLabelWindow((Dialog) c);

          if (dragTextWindow != null) {
            dragTextWindow.setFocusableWindowState(false);
            dragTextWindow.setFocusable(false);
            getRootWindowProperties().getDragLabelProperties().applyTo(dragTextWindow.getLabel());
            //dragTextWindow.setSize(dragText.getSize());
          }
        }

        if (dragTextWindow != null) {
          dragTextWindow.getLabel().setText(text);
          SwingUtilities.convertPointToScreen(textPoint, currentDragRootPane);
          dragTextWindow.setLocation(textPoint.x - dragTextContainer.getWidth() / 2,
              (textPoint.y - dragTextContainer.getHeight() + yLocationOffs));
          dragTextWindow.setVisible(true);
        }
      }
      else {
        dragTextContainer.setVisible(true);
        if (heavyweightSupport)
          dragTextContainer.repaint();

        if (dragTextWindow != null)
          dragTextWindow.setVisible(false);
      }
    }
    else if (dragTextContainer.getParent() != null) {
      dragTextContainer.setVisible(false);

      if (dragTextWindow != null)
        dragTextWindow.setVisible(false);
    }
  }

  void setDragRectangle(Rectangle rect) {
    if (rect != null) {
      if (reparent(getCurrentDragRootPane().getLayeredPane(), dragRectangle))
        getCurrentDragRootPane().getLayeredPane().setLayer(dragRectangle,
            JLayeredPane.DRAG_LAYER.intValue() +
            (heavyweightSupport ? -1 : 0));

      dragRectangle.setBounds(SwingUtilities.convertRectangle(this, rect, dragRectangle.getParent()));
      dragRectangle.setVisible(true);
    }
    else if (dragRectangle.getParent() != null)
      dragRectangle.setVisible(false);
  }

  protected void doReplace(DockingWindow oldWindow, DockingWindow newWindow) {
    if (oldWindow == window) {
      if (window != null) {
        windowPanel.remove(window);
        window.setVisible(true);
      }

      window = newWindow;

      if (window != null) {
        if (maximizedWindow != null)
          window.setVisible(false);

        windowPanel.add(window);
        windowPanel.revalidate();
        //revalidate();
      }
    }
  }

  protected void doRemoveWindow(DockingWindow window) {
    if (window == this.window) {
      windowPanel.remove(window);
      this.window.setVisible(true);
      this.window = null;
    }

    repaint();
  }

  public RootWindow getRootWindow() {
    return this;
  }

  protected boolean acceptsSplitWith(DockingWindow window) {
    return false;
  }

  protected DropAction doAcceptDrop(Point p, DockingWindow window) {
    if (maximizedWindow != null) {
      Point p2 = SwingUtilities.convertPoint(this, p, maximizedWindow);

      if (maximizedWindow.contains(p2) &&
          getChildDropFilter().acceptDrop(new ChildDropInfo(window, this, p, maximizedWindow))) {
        DropAction da = maximizedWindow.acceptDrop(p2, window);

        if (da != null)
          return da;
      }
    }

    return super.doAcceptDrop(p, window);
  }

  protected DropAction acceptInteriorDrop(Point p, DockingWindow window) {
    if (this.window != null)
      return null;

    if (getInteriorDropFilter().acceptDrop(new InteriorDropInfo(window, this, p)))
      return new DropAction() {
      public void execute(DockingWindow window, MouseEvent mouseEvent) {
        setWindow(window);
      }
    };

    return null;
  }

  protected PropertyMap getPropertyObject() {
    return getRootWindowProperties().getMap();
  }

  protected PropertyMap createPropertyObject() {
    return new RootWindowProperties().getMap();
  }

  boolean windowBarEnabled() {
    for (int i = 0; i < windowBars.length; i++)
      if (windowBars[i].isEnabled())
        return true;

    return false;
  }

  void removeWindowComponent(DockingWindow window) {
  }

  void restoreWindowComponent(DockingWindow window) {
  }

  protected void cleanUpModel() {
    if (!cleanUpModel) {
      cleanUpModel = true;
      SwingUtilities.invokeLater(modelCleanUpEvent);
    }
  }

  protected boolean isShowingInRootWindow() {
    return true;
  }

  public void updateUI() {
    super.updateUI();

    if (floatingWindows != null) {
      for (int i = 0; i < floatingWindows.size(); i++)
        SwingUtilities.updateComponentTreeUI(((JComponent) floatingWindows.get(i)).getTopLevelAncestor());

      for (int i = 0; i < views.size(); i++) {
        View v = (View) ((WeakReference) views.get(i)).get();
        if (v != null && v.getWindowParent() == null)
          SwingUtilities.updateComponentTreeUI(v);
      }
    }
  }

  protected void paintComponent(final Graphics g) {
    //((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    super.paintComponent(g);
  }
}
