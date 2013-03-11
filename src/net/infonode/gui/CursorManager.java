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


// $Id: CursorManager.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Cursor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.WeakHashMap;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class CursorManager {
  private static class RootCursorInfo {
    private Cursor savedCursor;
    private Cursor cursor;
    private JComponent panel;

    private boolean cursorSet = false;

    RootCursorInfo(JComponent panel) {
      this.panel = panel;
    }

    public JComponent getComponent() {
      return panel;
    }

    public void pushCursor(Cursor cursor) {
      if (savedCursor == null)
        savedCursor = cursor;

      cursorSet = true;
    }

    public Cursor popCursor() {
      Cursor c = savedCursor;
      savedCursor = null;

      cursorSet = false;
      return c;
    }

    public boolean isCursorSet() {
      return cursorSet;
    }

    public Cursor getCursor() {
      return cursor;
    }

    public void setCursor(Cursor cursor) {
      this.cursor = cursor;
    }

  }

  private static boolean enabled = true;
  private static WeakHashMap windowPanels = new WeakHashMap();

  private CursorManager() {
  }

  public static void setGlobalCursor(final JRootPane root, Cursor cursor) {
    if (root == null)
      return;

    RootCursorInfo rci = (RootCursorInfo) windowPanels.get(root);

    if (rci == null) {
      rci = new RootCursorInfo(new JComponent() {
      });
      windowPanels.put(root, rci);
      root.getLayeredPane().add(rci.getComponent());
      root.getLayeredPane().setLayer(rci.getComponent(), JLayeredPane.DRAG_LAYER.intValue() + 10);
      rci.getComponent().setBounds(0, 0, root.getWidth(), root.getHeight());
      root.getLayeredPane().addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          ((RootCursorInfo) windowPanels.get(root)).getComponent().setSize(root.getSize());
        }
      });
    }

    if (!rci.isCursorSet()) {
      rci.setCursor(cursor);
      rci.pushCursor(root.isCursorSet() ? root.getCursor() : null);
    }

    if (enabled) {
      root.setCursor(cursor);
      rci.getComponent().setVisible(true);
    }
  }

  public static Cursor getCurrentGlobalCursor(JRootPane root) {
    if (root == null)
      return Cursor.getDefaultCursor();

    RootCursorInfo rci = (RootCursorInfo) windowPanels.get(root);
    return rci == null || !rci.isCursorSet() ? Cursor.getDefaultCursor() : rci.getCursor();
  }

  public static void resetGlobalCursor(JRootPane root) {
    if (root == null)
      return;

    RootCursorInfo rci = (RootCursorInfo) windowPanels.get(root);

    if (rci != null && rci.isCursorSet()) {
      root.setCursor(rci.popCursor());
      rci.getComponent().setVisible(false);
    }
  }

  public static void setEnabled(boolean enabled) {
    CursorManager.enabled = enabled;
  }

  public static boolean isEnabled() {
    return enabled;
  }

  public static JComponent getCursorLayerComponent(JRootPane root) {
    if (root == null)
      return null;

    RootCursorInfo rci = (RootCursorInfo) windowPanels.get(root);
    return rci == null ? null : rci.getComponent();
  }

  public static boolean isGlobalCursorSet(JRootPane root) {
    if (root == null)
      return false;

    RootCursorInfo rci = (RootCursorInfo) windowPanels.get(root);
    return rci != null && rci.isCursorSet();
  }
}
