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


// $Id: DraggableComponentBox.java,v 1.3 2011-09-07 19:56:10 mpue Exp $

package net.infonode.gui.draggable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.infonode.gui.ComponentUtil;
import net.infonode.gui.ScrollButtonBox;
import net.infonode.gui.ScrollButtonBoxListener;
import net.infonode.gui.ScrollableBox;
import net.infonode.gui.ScrollableBoxListener;
import net.infonode.gui.layout.DirectionLayout;
import net.infonode.gui.panel.SimplePanel;
import net.infonode.util.Direction;

public class DraggableComponentBox extends SimplePanel {
  private final boolean componentBoxEnabled = true;

  private final JComponent componentBox;
  private JComponent componentContainer;

  private JComponent outerParentArea = this;
  private Direction componentDirection = Direction.UP;
  private boolean scrollEnabled = false;
  private boolean ensureSelectedVisible;
  private boolean autoSelect = true;
  private boolean descendingSortOrder = true;

  private boolean doReverseSort = false;
  private boolean mustSort = false;

  private int scrollOffset;
  private final int iconSize;
  private DraggableComponent selectedComponent;
  private DraggableComponent topComponent;
  private ArrayList listeners;
  private final ArrayList draggableComponentList = new ArrayList(10);
  private final ArrayList layoutOrderList = new ArrayList(10);

  private ScrollButtonBox scrollButtonBox;

  private boolean useDefaultScrollButtons = true;

  private final DraggableComponentListener draggableComponentListener = new DraggableComponentListener() {
    public void changed(DraggableComponentEvent event) {
      if (event.getType() == DraggableComponentEvent.TYPE_MOVED) {
        sortComponentList(!descendingSortOrder);
      }
      fireChangedEvent(event);
    }

    public void selected(DraggableComponentEvent event) {
      doSelectComponent(event.getSource());
    }

    public void dragged(DraggableComponentEvent event) {
      fireDraggedEvent(event);
    }

    public void dropped(DraggableComponentEvent event) {
      ensureSelectedVisible();
      fireDroppedEvent(event);
    }

    public void dragAborted(DraggableComponentEvent event) {
      ensureSelectedVisible();
      fireNotDroppedEvent(event);
    }
  };

  public DraggableComponentBox(int iconSize) {
    this(iconSize, true);
  }

  public DraggableComponentBox(int iconSize, boolean useDefaultScrollButtons) {
    this.iconSize = iconSize;
    this.useDefaultScrollButtons = useDefaultScrollButtons;
    // Fix minimum size when flipping direction
    final DirectionLayout layout = new DirectionLayout(componentDirection == Direction.UP ? Direction.RIGHT : componentDirection == Direction.LEFT
                                                                                          ?
                                                                                           Direction.DOWN
                                                                                           :
                                                                                             componentDirection == Direction.DOWN ?
                                                                                                                                   Direction.RIGHT :
                                                                                                                                     Direction.DOWN) {
      public Dimension minimumLayoutSize(Container parent) {
        Dimension min = super.minimumLayoutSize(parent);
        Dimension pref = super.preferredLayoutSize(parent);
        return componentDirection.isHorizontal() ?
                                                  new Dimension(pref.width, min.height) : new Dimension(min.width, pref.height);
      }

      public void layoutContainer(Container parent) {
        if (DraggableComponentBox.this != null && componentBoxEnabled) {
          //long millis = System.currentTimeMillis();
          doSort();
          super.layoutContainer(parent);
          //System.out.println("Layout: " + (System.currentTimeMillis() - millis));
        }
      }

      public Dimension preferredLayoutSize(Container parent) {
        doSort();
        return super.preferredLayoutSize(parent);
      }
    };

    layout.setLayoutOrderList(layoutOrderList);

    componentBox = new SimplePanel(layout) {
      public boolean isOptimizedDrawingEnabled() {
        return DraggableComponentBox.this != null && getComponentSpacing() >= 0;
      }
    };

    componentBox.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        //fireChangedEvent();
      }

      public void componentMoved(ComponentEvent e) {
        fireChangedEvent();
      }
    });

    initialize();
  }

  public void addListener(DraggableComponentBoxListener listener) {
    if (listeners == null)
      listeners = new ArrayList(2);

    listeners.add(listener);
  }

  public void removeListener(DraggableComponentBoxListener listener) {
    if (listeners != null) {
      listeners.remove(listener);

      if (listeners.size() == 0)
        listeners = null;
    }
  }

  public void addDraggableComponent(DraggableComponent component) {
    insertDraggableComponent(component, -1);
  }

  public void insertDraggableComponent(DraggableComponent component, int index) {
    component.setLayoutOrderList(layoutOrderList);

    component.addListener(draggableComponentListener);
    if (index < 0) {
      layoutOrderList.add(component.getComponent());
      componentBox.add(component.getComponent());
    }
    else {
      layoutOrderList.add(index, component.getComponent());
      componentBox.add(component.getComponent(), index);
    }

    sortComponentList(!descendingSortOrder);

    draggableComponentList.add(component);
    component.setOuterParentArea(outerParentArea);
    componentBox.revalidate();

    fireAddedEvent(component);
    if (autoSelect && layoutOrderList.size() == 1 && selectedComponent == null && component.isEnabled())
      doSelectComponent(component);

    updateScrollButtons();
  }

  private void updateScrollButtons() {
    if (scrollButtonBox != null) {
      ScrollableBox scrollableBox = (ScrollableBox) componentContainer;
      scrollButtonBox.setButton1Enabled(!scrollableBox.isLeftEnd());
      scrollButtonBox.setButton2Enabled(!scrollableBox.isRightEnd());
    }
  }

  public void insertDraggableComponent(DraggableComponent component, Point p) {
    int componentIndex = getComponentIndexAtPoint(p);
    if (componentIndex != -1 && layoutOrderList.size() > 0)
      insertDraggableComponent(component, componentIndex);
    else
      insertDraggableComponent(component, -1);
  }

  public void selectDraggableComponent(DraggableComponent component) {
    if (component == null) {
      if (selectedComponent != null) {
        DraggableComponent oldSelected = selectedComponent;
        selectedComponent = null;
        fireSelectedEvent(selectedComponent, oldSelected);
        //componentBox.repaint();
      }
    }
    else
      component.select();
  }

  public void removeDraggableComponent(DraggableComponent component) {
    if (component != null && draggableComponentList.contains(component)) {
      //component.abortDrag();
      int index = layoutOrderList.indexOf(component.getComponent());
      component.removeListener(draggableComponentListener);
      if (component == topComponent)
        topComponent = null;
      if (layoutOrderList.size() > 1 && selectedComponent != null) {
        if (selectedComponent == component) {
          if (autoSelect) {
            int selectIndex = findSelectableComponentIndex(index);
            if (selectIndex > -1)
              selectDraggableComponent(findDraggableComponent((Component) layoutOrderList.get(selectIndex)));
            else
              selectedComponent = null;
          }
          else {
            selectDraggableComponent(null);
          }
        }
      }
      else {
        if (selectedComponent != null) {
          DraggableComponent oldSelected = selectedComponent;
          selectedComponent = null;
          fireSelectedEvent(selectedComponent, oldSelected);
        }
      }
      draggableComponentList.remove(component);
      layoutOrderList.remove(component.getComponent());
      componentBox.remove(component.getComponent());
      componentBox.revalidate();
      //componentBox.validate();
      component.setLayoutOrderList(null);

      sortComponentList(!descendingSortOrder);

      updateScrollButtons();

      fireRemovedEvent(component);
    }
  }

  public boolean containsDraggableComponent(DraggableComponent component) {
    return draggableComponentList.contains(component);
  }

  public DraggableComponent getSelectedDraggableComponent() {
    return selectedComponent;
  }

  public int getDraggableComponentCount() {
    return layoutOrderList.size();
  }

  public DraggableComponent getDraggableComponentAt(int index) {
    return index < layoutOrderList.size() ? findDraggableComponent((Component) layoutOrderList.get(index)) : null;
  }

  public int getDraggableComponentIndex(DraggableComponent component) {
    return layoutOrderList.indexOf(component.getComponent());
  }

  public Object[] getDraggableComponents() {
    return draggableComponentList.toArray();
  }

  public Component[] getBoxComponents() {
    return componentBox.getComponents();
  }

  public boolean getDepthSortOrder() {
    return descendingSortOrder;
  }

  public void setDepthSortOrder(boolean descending) {
    if (descending != this.descendingSortOrder) {
      this.descendingSortOrder = descending;
      sortComponentList(!descending);
      doSort();
    }
  }

  public boolean isScrollEnabled() {
    return scrollEnabled;
  }

  public void setScrollEnabled(boolean scrollEnabled) {
    if (scrollEnabled != this.scrollEnabled) {
      this.scrollEnabled = scrollEnabled;
      initialize();
    }
  }

  public int getScrollOffset() {
    return scrollOffset;
  }

  public void setScrollOffset(int scrollOffset) {
    if (scrollOffset != this.scrollOffset) {
      this.scrollOffset = scrollOffset;
      if (scrollEnabled)
        ((ScrollableBox) componentContainer).setScrollOffset(scrollOffset);
    }
  }

  public int getComponentSpacing() {
    return getDirectionLayout().getComponentSpacing();
  }

  public void setComponentSpacing(int componentSpacing) {
    if (componentSpacing != getDirectionLayout().getComponentSpacing()) {
      if (getComponentSpacing() < 0 && componentSpacing >= 0) {
        DraggableComponent tmp = topComponent;
        sortComponentList(false);
        topComponent = tmp;
      }
      getDirectionLayout().setComponentSpacing(componentSpacing);
      sortComponentList(!descendingSortOrder);
      componentBox.revalidate();
    }
  }

  public boolean isEnsureSelectedVisible() {
    return ensureSelectedVisible;
  }

  public void setEnsureSelectedVisible(boolean ensureSelectedVisible) {
    this.ensureSelectedVisible = ensureSelectedVisible;
  }

  public boolean isAutoSelect() {
    return autoSelect;
  }

  public void setAutoSelect(boolean autoSelect) {
    this.autoSelect = autoSelect;
  }

  public Direction getComponentDirection() {
    return componentDirection;
  }

  public void setComponentDirection(Direction componentDirection) {
    if (componentDirection != this.componentDirection) {
      this.componentDirection = componentDirection;
      getDirectionLayout().setDirection(componentDirection == Direction.UP ? Direction.RIGHT : componentDirection == Direction.LEFT ? Direction.DOWN : componentDirection == Direction.DOWN
                                                                                                                                    ?
                                                                                                                                     Direction.RIGHT
                                                                                                                                     :
                                                                                                                                       Direction.DOWN);
      if (scrollEnabled) {
        scrollButtonBox.setVertical(componentDirection.isHorizontal());
        ((ScrollableBox) componentContainer).setVertical(componentDirection.isHorizontal());
      }
    }
  }

  public void setTopComponent(DraggableComponent topComponent) {
    if (topComponent != this.topComponent) {
      this.topComponent = topComponent;

      sortComponentList(!descendingSortOrder);
    }
  }

  public ScrollButtonBox getScrollButtonBox() {
    return scrollButtonBox;
  }

  public JComponent getOuterParentArea() {
    return outerParentArea;
  }

  public void setOuterParentArea(JComponent outerParentArea) {
    this.outerParentArea = outerParentArea;
  }

  public void dragDraggableComponent(DraggableComponent component, Point p) {
    if (draggableComponentList.contains(component)) {
      component.drag(SwingUtilities.convertPoint(this, p, component.getComponent()));
    }

    //component.drag(SwingUtilities.convertPoint(this, p, component.getComponent()));
  }

  public Dimension getMaximumSize() {
    if (scrollEnabled)
      return getPreferredSize();

    if (componentDirection == Direction.LEFT || componentDirection == Direction.RIGHT)
      return new Dimension((int) getPreferredSize().getWidth(), (int) super.getMaximumSize().getHeight());

    return new Dimension((int) super.getMaximumSize().getWidth(), (int) getPreferredSize().getHeight());

  }

  public Dimension getInnerSize() {
    boolean mustSort = this.mustSort;
    this.mustSort = false;
    Dimension d = scrollEnabled ? componentBox.getPreferredSize() : componentBox.getSize();
    this.mustSort = mustSort;
    return d;
  }

  public void scrollToVisible(final DraggableComponent c) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (scrollEnabled) {
          ((ScrollableBox) componentContainer).ensureVisible(layoutOrderList.indexOf(c.getComponent()));
        }
      }
    });
  }

  // Prevents focus problems when adding/removing focused component while sorting when spacing < 0
  private void setIgnoreAddRemoveNotify(boolean ignore) {
    for (int i = 0; i < draggableComponentList.size(); i++)
      ((DraggableComponent) draggableComponentList.get(i)).setIgnoreAddNotify(ignore);
  }

  private void doSort() {
    if (mustSort && getComponentSpacing() < 0 && componentBox.getComponentCount() > 0) {
      setIgnoreAddRemoveNotify(true);

      mustSort = false;
      Component c;
      Component tc = topComponent != null ? topComponent.getComponent() : null;

      //componentBoxEnabled = false;

      //long millis = System.currentTimeMillis();

      int index = 0;
      if (tc != null) {
        if (componentBox.getComponent(0) != tc) {
          componentBox.remove(tc);
          componentBox.add(tc, index);
        }
        index++;
      }

      int switc = 0;
      int size = layoutOrderList.size();
      for (int i = 0; i < size; i++) {
        c = (Component) layoutOrderList.get(doReverseSort ? size - i - 1 : i);
        if (c != tc) {
          if (componentBox.getComponent(index) != c) {
            switc++;
            componentBox.remove(c);
            componentBox.add(c, index);
          }
          index++;
        }
      }

      setIgnoreAddRemoveNotify(false);

      /*System.out.print("  Box:   ");
      for (int i = 0; i < componentBox.getComponentCount(); i++)
        System.out.print(componentBox.getComponent(i) + "  ");
      System.out.println();
      System.out.print("  Order: ");
      for (int i = 0; i < layoutOrderList.size(); i++)
        System.out.print(layoutOrderList.get(i) + "  ");*/
      //System.out.println();
      /*			long millis = System.currentTimeMillis();
			componentBox.removeAll();

			if (tc != null)
				componentBox.add(tc);

			int size = layoutOrderList.size();
			for (int i = 0; i < size; i++) {
				c = (Component) layoutOrderList.get(doReverseSort ? size - i - 1 : i);
				if (c != tc)
					componentBox.add(c);
			}*/

      //componentBoxEnabled = true;

      //System.out.println("Sorting " + scount++ + "  time: " + (System.currentTimeMillis() - millis) + "  Sorted: " + switc);
    }
  }

  private void sortComponentList(boolean reverseSort) {
    this.doReverseSort = reverseSort;
    mustSort = true;
  }

  private int getComponentIndexAtPoint(Point p) {
    JComponent c = null;
    Point p2 = SwingUtilities.convertPoint(this, p, componentBox);
    Point p3 = SwingUtilities.convertPoint(componentBox, p, outerParentArea);
    if (outerParentArea.contains(p3))
      c = (JComponent) ComponentUtil.getChildAtLine(componentBox,
          p2,
          getDirectionLayout().getDirection().isHorizontal());

    return layoutOrderList.indexOf(c);
  }

  private void doSelectComponent(DraggableComponent component) {
    if (selectedComponent != null) {
      DraggableComponent oldSelected = selectedComponent;
      selectedComponent = component;
      ensureSelectedVisible();
      fireSelectedEvent(selectedComponent, oldSelected);
    }
    else {
      selectedComponent = component;
      ensureSelectedVisible();
      fireSelectedEvent(selectedComponent, null);
    }
  }

  private int findSelectableComponentIndex(int index) {
    int selectIndex = -1;
    int k;
    for (int i = 0; i < layoutOrderList.size(); i++) {
      if ((findDraggableComponent((Component) layoutOrderList.get(i))).isEnabled() && i != index) {
        k = selectIndex;
        selectIndex = i;
        if (k < index && selectIndex > index)
          return selectIndex;
        else if (k > index && selectIndex > index)
          return k;
      }
    }

    return selectIndex;
  }

  private DraggableComponent findDraggableComponent(Component c) {
    for (int i = 0; i < draggableComponentList.size(); i++)
      if (((DraggableComponent) draggableComponentList.get(i)).getComponent() == c)
        return (DraggableComponent) draggableComponentList.get(i);

    return null;
  }

  private DirectionLayout getDirectionLayout() {
    return (DirectionLayout) componentBox.getLayout();
  }

  private void initialize() {
    if (componentContainer != null)
      remove(componentContainer);

    DirectionLayout layout = getDirectionLayout();
    layout.setCompressing(!scrollEnabled);

    if (scrollEnabled) {
      if (useDefaultScrollButtons)
        scrollButtonBox = new ScrollButtonBox(componentDirection.isHorizontal(), iconSize);
      else
        scrollButtonBox = new ScrollButtonBox(componentDirection.isHorizontal(), null, null, null, null);

      final ScrollableBox scrollableBox = new ScrollableBox(componentBox,
          componentDirection.isHorizontal(),
          scrollOffset);
      scrollableBox.setLayoutOrderList(layoutOrderList);
      scrollButtonBox.addListener(new ScrollButtonBoxListener() {
        public void scrollButton1() {
          scrollableBox.scrollLeft(1);
        }

        public void scrollButton2() {
          scrollableBox.scrollRight(1);
        }
      });

      scrollableBox.addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
          scrollButtonBox.setButton1Enabled(!scrollableBox.isLeftEnd());
          scrollButtonBox.setButton2Enabled(!scrollableBox.isRightEnd());
        }
      });

      scrollButtonBox.setButton1Enabled(!scrollableBox.isLeftEnd());
      scrollButtonBox.setButton2Enabled(!scrollableBox.isRightEnd());

      scrollableBox.addScrollableBoxListener(new ScrollableBoxListener() {
        public void scrolledLeft(ScrollableBox box) {
          scrollButtonBox.setButton1Enabled(!box.isLeftEnd());
          scrollButtonBox.setButton2Enabled(true);
        }

        public void scrolledRight(ScrollableBox box) {
          scrollButtonBox.setButton1Enabled(true);
          scrollButtonBox.setButton2Enabled(!box.isRightEnd());
        }

        public void changed(ScrollableBox box) {
          fireChangedEvent();
        }
      });
      componentContainer = scrollableBox;
    }
    else {
      scrollButtonBox = null;
      componentContainer = componentBox;
    }

    componentContainer.setAlignmentY(0);
    add(componentContainer, BorderLayout.CENTER);

    revalidate();
  }

  private void ensureSelectedVisible() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (scrollEnabled && ensureSelectedVisible && selectedComponent != null) {
          //componentContainer.validate();
          ((ScrollableBox) componentContainer).ensureVisible(layoutOrderList.indexOf(selectedComponent.getComponent()));
        }
      }
    });
  }

  private void fireDraggedEvent(DraggableComponentEvent e) {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this,
          e.getSource(),
          e,
          SwingUtilities.convertPoint(
              e.getSource().getComponent(),
              e.getMouseEvent().getPoint(),
              this));
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).componentDragged(event);
    }
  }

  private void fireDroppedEvent(DraggableComponentEvent e) {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this,
          e.getSource(),
          e,
          SwingUtilities.convertPoint(
              e.getSource().getComponent(),
              e.getMouseEvent().getPoint(),
              this));
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).componentDropped(event);
    }
  }

  private void fireNotDroppedEvent(DraggableComponentEvent e) {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, e.getSource(), e);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).componentDragAborted(event);
    }
  }

  private void fireSelectedEvent(DraggableComponent component, DraggableComponent oldDraggableComponent) {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, component, oldDraggableComponent);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).componentSelected(event);
    }
  }

  private void fireAddedEvent(DraggableComponent component) {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, component);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).componentAdded(event);
    }
  }

  private void fireRemovedEvent(DraggableComponent component) {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, component);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).componentRemoved(event);
    }
  }

  private void fireChangedEvent(DraggableComponentEvent e) {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, e.getSource(), e);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).changed(event);
    }
  }

  private void fireChangedEvent() {
    if (listeners != null) {
      DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((DraggableComponentBoxListener) l[i]).changed(event);
    }
  }
}