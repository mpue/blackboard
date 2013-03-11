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


// $Id: ResizablePanel.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.panel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import net.infonode.gui.CursorManager;
import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class ResizablePanel extends BaseContainer {
  private Direction direction;
  private int resizeWidth = 4;
  private boolean cursorChanged;
  private int offset = -1;
  private boolean mouseInside;
  private boolean heavyWeight = true;
  private boolean continuousLayout = false;
  private Component dragIndicator;
  private JComponent layeredPane;
  private JComponent innerArea;
  private Dimension lastSize;
  private int dragIndicatorThickness = 4;
  private Component comp;

  public ResizablePanel(Direction _direction) {
    this(false, _direction, null);
  }

  public ResizablePanel(boolean useHeavyWeightDragIndicator, Direction _direction, Component mouseListenComponent) {
    super(new BorderLayout());
    this.heavyWeight = useHeavyWeightDragIndicator;
    this.direction = _direction;

    if (heavyWeight) {
      dragIndicator = new Canvas();
    }
    else {
      dragIndicator = new BaseContainer();
    }
    setDragIndicatorColor(null);
    if (mouseListenComponent == null)
      mouseListenComponent = this;

    mouseListenComponent.addMouseListener(new MouseAdapter() {
      public void mouseExited(MouseEvent e) {
        if (offset == -1)
          resetCursor();

        mouseInside = false;
      }

      public void mouseEntered(MouseEvent e) {
        mouseInside = true;
      }

      public void mousePressed(MouseEvent e) {
        //if (MouseEventCoalesceManager.getInstance().isPressedAllowed(e)) {
        if (!continuousLayout && layeredPane != null) {
          if (layeredPane instanceof JLayeredPane)
            layeredPane.add(dragIndicator, JLayeredPane.DRAG_LAYER);
          else
            layeredPane.add(dragIndicator, 0);

          layeredPane.repaint();
          updateDragIndicator(e);
        }
        if (cursorChanged) {
          offset = direction == Direction.LEFT ? e.getPoint().x : direction == Direction.RIGHT ? getWidth() - e.getPoint()
              .x : direction == Direction.UP ? e.getPoint().y : getHeight()
                                                                - e.getPoint().y;
        }
        //}
      }

      public void mouseReleased(MouseEvent e) {
        //if (MouseEventCoalesceManager.getInstance().isReleasedAllowed(e)) {
        if (!continuousLayout && layeredPane != null) {
          layeredPane.remove(dragIndicator);
          layeredPane.repaint();
        }
        offset = -1;
        checkCursor(e.getPoint());

        if (!continuousLayout && lastSize != null) {
          setPreferredSize(lastSize);
          revalidate();
        }

        lastSize = null;
        //}
      }
    });

    mouseListenComponent.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        checkCursor(e.getPoint());
      }

      public void mouseDragged(MouseEvent e) {
        if (offset != -1) {// && MouseEventCoalesceManager.getInstance().isDraggedAllowed(e)) {
          int size = direction.isHorizontal() ?
                     (direction == Direction.LEFT ? getWidth() - e.getPoint().x + offset : e.getPoint().x + offset) :
                     (direction == Direction.UP ? getHeight() - e.getPoint().y + offset : e.getPoint().y + offset);
          lastSize = getBoundedSize(size);

          if (continuousLayout) {
            setPreferredSize(lastSize);
            revalidate();
          }
          else {
            updateDragIndicator(e);
          }
        }
      }
    });
  }

  public void setComponent(Component c) {
    if (comp != null)
      remove(comp);

    if (c != null) {
      add(c, BorderLayout.CENTER);
      //c.repaint();
      revalidate();
    }

    comp = c;
  }

  public void setDragIndicatorColor(Color color) {
    dragIndicator.setBackground(color == null ? Color.DARK_GRAY : color);
  }

  public void setLayeredPane(JComponent layeredPane) {
    this.layeredPane = layeredPane;
    if (innerArea == null)
      innerArea = layeredPane;
  }

  public void setInnerArea(JComponent innerArea) {
    if (innerArea == null)
      innerArea = layeredPane;
    else
      this.innerArea = innerArea;
  }

  public boolean isContinuousLayout() {
    return continuousLayout;
  }

  public void setContinuousLayout(boolean continuousLayout) {
    this.continuousLayout = continuousLayout;
  }

  public Dimension getPreferredSize() {
    Dimension d = super.getPreferredSize();
    return getBoundedSize(direction.isHorizontal() ? d.width : d.height);
  }

  private void updateDragIndicator(MouseEvent e) {
    if (layeredPane != null) {
      Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), layeredPane);
      Point p2 = SwingUtilities.convertPoint(this.getParent(), getLocation(), layeredPane);
      Dimension size = innerArea.getSize();
      Dimension minimumSize = getMinimumSize();
      Point offset = SwingUtilities.convertPoint(innerArea, 0, 0, layeredPane);

      if (direction.isHorizontal()) {
        int x = 0;
        if (direction == Direction.LEFT)
          x = Math.min(Math.max(offset.x, p.x), offset.x + size.width - minimumSize.width);
        else
          x = Math.min(Math.max(offset.x + minimumSize.width, p.x), offset.x + size.width) - dragIndicatorThickness;

        dragIndicator.setBounds(x, p2.y, dragIndicatorThickness, getHeight());
      }
      else {
        int y = 0;
        if (direction == Direction.UP)
          y = Math.min(Math.max(offset.y, p.y), offset.y + size.height - minimumSize.height);
        else
          y = Math.min(Math.max(offset.y + minimumSize.height, p.y), offset.y + size.height) - dragIndicatorThickness;

        dragIndicator.setBounds(p2.x, y, getWidth(), dragIndicatorThickness);
      }
    }
  }

  private Dimension getBoundedSize(int size) {
    if (direction.isHorizontal()) {
      return new Dimension(Math.max(getMinimumSize().width, Math.min(size, getMaximumSize().width)), 0);
    }
    else {
      return new Dimension(0, Math.max(getMinimumSize().height, Math.min(size, getMaximumSize().height)));
    }
  }

  public void setResizeWidth(int width) {
    this.resizeWidth = width;
  }

  public int getResizeWidth() {
    return resizeWidth;
  }

  private void checkCursor(Point point) {
    if (offset != -1)
      return;

    int dist = direction == Direction.UP ? point.y :
               direction == Direction.DOWN ? getHeight() - point.y :
               direction == Direction.LEFT ? point.x :
               getWidth() - point.x;

    if (dist >= 0 && dist < resizeWidth && mouseInside) {
      if (!cursorChanged) {
        cursorChanged = true;
        CursorManager.setGlobalCursor(getRootPane(),
                                      new Cursor(direction == Direction.LEFT ? Cursor.W_RESIZE_CURSOR :
                                                 direction == Direction.RIGHT ? Cursor.E_RESIZE_CURSOR :
                                                 direction == Direction.UP ? Cursor.N_RESIZE_CURSOR :
                                                 Cursor.S_RESIZE_CURSOR));
      }
    }
    else
      resetCursor();
  }

  private void resetCursor() {
    CursorManager.resetGlobalCursor(getRootPane());
    cursorChanged = false;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setVisible(boolean aFlag) {
    super.setVisible(aFlag);
  }
}
