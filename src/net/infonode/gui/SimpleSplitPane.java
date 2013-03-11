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


// $Id: SimpleSplitPane.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JComponent;

import net.infonode.gui.panel.BaseContainer;
import net.infonode.gui.panel.SimplePanel;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class SimpleSplitPane extends BaseContainer {
  private LayoutManager splitLayout = new LayoutManager() {
    public void addLayoutComponent(String name, Component comp) {
    }

    public void layoutContainer(Container parent) {
      if (leftComponent == null || !leftComponent.isVisible()) {
        maximize(rightComponent);
      }
      else if (rightComponent == null || !rightComponent.isVisible()) {
        maximize(leftComponent);
      }
      else {
        float dividerLocation = fixDividerLocation(getDividerLocation());
        int totalSize = getViewSize();
        int leftSize = (int) (totalSize * dividerLocation);
        int otherSize = getOtherSize();
        int offsetX = getInsets().left;
        int offsetY = getInsets().top;

        Dimension d = createSize(leftSize, otherSize);
        leftComponent.setBounds(offsetX, offsetY, (int) d.getWidth(), (int) d.getHeight());

        Point p = createPoint(leftSize, 0);
        d = createSize(dividerSize, otherSize);
        dividerPanel.setBounds(p.x + offsetX, p.y + offsetY, (int) d.getWidth(), (int) d.getHeight());

        p = createPoint(leftSize + dividerSize, 0);
        d = createSize(totalSize - leftSize, otherSize);
        rightComponent.setBounds(p.x + offsetX, p.y + offsetY, (int) d.getWidth(), (int) d.getHeight());
      }
    }

    private void maximize(Component component) {
      if (component != null && component.isVisible()) {
        Insets i = getInsets();
        Dimension d = getSize();
        component.setBounds(i.left, i.top, d.width - i.left - i.right, d.height - i.top - i.bottom);
      }

      dividerPanel.setBounds(0, 0, 0, 0);
    }

    public Dimension minimumLayoutSize(Container parent) {
      Dimension d = createSize((leftComponent == null ? 0 : getDimensionSize(leftComponent.getMinimumSize())) + dividerSize
                               + (rightComponent == null ? 0 : getDimensionSize(rightComponent.getMinimumSize())), Math.max(
                                   leftComponent == null ? 0 : getOtherSize(leftComponent.getMinimumSize()), rightComponent == null
                                                                                                             ?
                                                                                                             0
                                                                                                             :
                                                                                                             getOtherSize(
                                                                                                                 rightComponent.getMinimumSize())));
      return new Dimension(d.width + getInsets().left + getInsets().right,
                           d.height + getInsets().top + getInsets().bottom);
    }

    public Dimension preferredLayoutSize(Container parent) {
      boolean lv = leftComponent != null && leftComponent.isVisible();
      boolean rv = rightComponent != null && rightComponent.isVisible();
      Dimension d = createSize(
          (lv ? getDimensionSize(leftComponent.getPreferredSize()) : 0) + (lv && rv ? dividerSize : 0) + (rv ?
                                                                                                          getDimensionSize(
                                                                                                              rightComponent.getPreferredSize()) :
                                                                                                          0),
          Math.max(lv ? getOtherSize(leftComponent.getPreferredSize()) : 0,
                   rv ? getOtherSize(rightComponent.getPreferredSize()) : 0));
      return new Dimension(d.width + getInsets().left + getInsets().right,
                           d.height + getInsets().top + getInsets().bottom);
    }

    public void removeLayoutComponent(Component comp) {
    }
  };

  private Component leftComponent;
  private Component rightComponent;
  private SimplePanel dividerPanel = new SimplePanel();
  private Component dragIndicator;
  private boolean dividerDraggable = true;
  private boolean continuousLayout = true;
  private float dragLocation;
  private boolean horizontal;
  private float dividerLocation = 0.5F;
  private int dividerSize = 6;
  private ArrayList listeners = new ArrayList(0);
  private Color dragIndicatorColor = Color.DARK_GRAY;

  public SimpleSplitPane(boolean horizontal) {
    this(horizontal, false);
  }

  public SimpleSplitPane(boolean horizontal, boolean heavyWeightDragIndicator) {
    setLayout(splitLayout);
    add(dividerPanel);
    setHorizontal(horizontal);

    setHeavyWeightDragIndicator(heavyWeightDragIndicator);

    dividerPanel.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {// &&
          // MouseEventCoalesceManager.getInstance().isPressedAllowed(e))
          // {
          CursorManager.setGlobalCursor(getRootPane(), dividerPanel.getCursor());
          if (dividerDraggable && !continuousLayout) {
            float location = (float) (getPos(dividerPanel.getLocation()) - getOffset() + getPos(e.getPoint())) / getViewSize();
            setDragIndicator(location);
          }
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {// &&
          // MouseEventCoalesceManager.getInstance().isReleasedAllowed(e))
          // {
          CursorManager.resetGlobalCursor(getRootPane());

          if (dividerDraggable && !continuousLayout) {
            dragIndicator.setVisible(false);
            setDividerLocation(dragLocation);
          }
        }
      }
    });

    dividerPanel.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if (dividerDraggable && /* MouseEventCoalesceManager.getInstance().isDraggedAllowed(e) && */(e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
          float location = (float) (getPos(dividerPanel.getLocation()) - getOffset() + getPos(e.getPoint())) / getViewSize();

          if (continuousLayout)
            setDividerLocation(location);
          else
            setDragIndicator(location);
        }
      }
    });
  }

  public SimpleSplitPane(boolean horizontal, Component leftComponent, Component rightComponent) {
    this(horizontal);
    setLeftComponent(leftComponent);
    setRightComponent(rightComponent);
  }

  public void addListener(SimpleSplitPaneListener listener) {
    ArrayList newListeners = new ArrayList(listeners.size() + 1);
    newListeners.addAll(listeners);
    listeners = newListeners;
    listeners.add(listener);
  }

  public JComponent getDividerPanel() {
    return dividerPanel;
  }

  public boolean isDividerDraggable() {
    return dividerDraggable;
  }

  public void setDividerDraggable(boolean dividerDraggable) {
    this.dividerDraggable = dividerDraggable;
    updateDividerCursor();
  }

  public void setHeavyWeightDragIndicator(boolean heavyWeight) {
    initDragIndicatior(heavyWeight);
  }

  public Color getDragIndicatorColor() {
    return dragIndicatorColor;
  }

  public void setDragIndicatorColor(Color dragIndicatorColor) {
    this.dragIndicatorColor = dragIndicatorColor;
    dragIndicator.setBackground(dragIndicatorColor);
  }

  private void setDragIndicator(float location) {
    dragLocation = fixDividerLocation(location);
    dragIndicator.setVisible(true);
    Point p = createPoint((int) (getViewSize() * dragLocation), 0);
    Dimension d = createSize(dividerSize, getOtherSize());
    dragIndicator.setBounds((int) (p.getX() + getInsets().left),
                            (int) (p.getY() + getInsets().top),
                            (int) d.getWidth(),
                            (int) d.getHeight());
  }


  private void initDragIndicatior(boolean heavyWeight) {
    if (dragIndicator != null)
      remove(dragIndicator);

    if (heavyWeight) {
      dragIndicator = new Canvas();
    }
    else {
      dragIndicator = new BaseContainer();
    }

    add(dragIndicator, 0);
    dragIndicator.setBackground(dragIndicatorColor);
    dragIndicator.setVisible(false);
  }

  private float fixDividerLocation(float location) {
    int totalSize = getViewSize();

    if (totalSize <= 0)
      return 0.5F;

    int leftSize = Math.max((int) (totalSize * location),
                            leftComponent == null || !leftComponent.isVisible() ?
                            0 : getDimensionSize(leftComponent.getMinimumSize()));
    leftSize = Math.min(leftSize,
                        totalSize - (rightComponent == null || !rightComponent.isVisible() ?
                                     0 : getDimensionSize(rightComponent.getMinimumSize())));
    return (float) leftSize / totalSize;
  }

  public void setContinuousLayout(boolean value) {
    continuousLayout = value;
  }

  public boolean isContinuousLayout() {
    return continuousLayout;
  }

  public int getDividerSize() {
    return dividerSize;
  }

  public void setDividerSize(int dividerSize) {
    this.dividerSize = dividerSize;
    revalidate();
  }

  private int getOffset() {
    return horizontal ? getInsets().left : getInsets().top;
  }

  private int getOtherSize() {
    return horizontal ?
           getHeight() - getInsets().top - getInsets().bottom : getWidth() - getInsets().left - getInsets().right;
  }

  private int getViewSize() {
    return getDimensionSize(getSize()) - dividerSize - (horizontal ?
                                                        getInsets().left + getInsets().right :
                                                        getInsets().top + getInsets().bottom);
  }

  private int getDimensionSize(Dimension d) {
    return (int) (horizontal ? d.getWidth() : d.getHeight());
  }

  private int getOtherSize(Dimension d) {
    return (int) (horizontal ? d.getHeight() : d.getWidth());
  }

  private int getPos(Point p) {
    return (int) (horizontal ? p.getX() : p.getY());
  }

  private Dimension createSize(int size, int otherSize) {
    return horizontal ? new Dimension(size, otherSize) : new Dimension(otherSize, size);
  }

  private Point createPoint(int pos, int otherPos) {
    return horizontal ? new Point(pos, otherPos) : new Point(otherPos, pos);
  }

  public boolean isHorizontal() {
    return horizontal;
  }

  public void setHorizontal(boolean horizontal) {
    this.horizontal = horizontal;
    updateDividerCursor();
    revalidate();
  }

  public float getDividerLocation() {
    return dividerLocation;
  }

  public void setDividerLocation(float dividerLocation) {
    this.dividerLocation = dividerLocation;
    revalidate();

    for (int i = 0; i < listeners.size(); i++)
      ((SimpleSplitPaneListener) listeners.get(i)).dividerLocationChanged(this);
  }

  public Component getLeftComponent() {
    return leftComponent;
  }

  public void setLeftComponent(Component leftComponent) {
    if (this.leftComponent != null)
      remove(this.leftComponent);

    this.leftComponent = leftComponent;

    if (leftComponent != null)
      add(leftComponent);

    revalidate();
  }

  public Component getRightComponent() {
    return rightComponent;
  }

  public void setRightComponent(Component rightComponent) {
    if (this.rightComponent != null)
      remove(this.rightComponent);

    this.rightComponent = rightComponent;

    if (rightComponent != null)
      add(rightComponent);

    revalidate();
  }

  private void updateDividerCursor() {
    dividerPanel.setCursor(
        dividerDraggable ?
        new Cursor(horizontal ? Cursor.W_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR) : Cursor.getDefaultCursor());
  }

  public void setComponents(Component leftComponent, Component rightComponent) {
    setLeftComponent(leftComponent);
    setRightComponent(rightComponent);
  }
}
