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


// $Id: TabbedPanel.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import net.infonode.gui.ComponentPaintChecker;
import net.infonode.gui.ComponentUtil;
import net.infonode.gui.DimensionUtil;
import net.infonode.gui.EventUtil;
import net.infonode.gui.ScrollButtonBox;
import net.infonode.gui.draggable.DraggableComponent;
import net.infonode.gui.draggable.DraggableComponentBox;
import net.infonode.gui.draggable.DraggableComponentBoxEvent;
import net.infonode.gui.draggable.DraggableComponentBoxListener;
import net.infonode.gui.draggable.DraggableComponentEvent;
import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.gui.hover.panel.HoverableShapedPanel;
import net.infonode.gui.layout.DirectionLayout;
import net.infonode.gui.panel.BaseContainerUtil;
import net.infonode.gui.shaped.panel.ShapedPanel;
import net.infonode.properties.gui.InternalPropertiesUtil;
import net.infonode.properties.gui.util.ButtonProperties;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;
import net.infonode.tabbedpanel.internal.ShadowPainter;
import net.infonode.tabbedpanel.internal.TabDropDownList;
import net.infonode.tabbedpanel.internal.TabbedHoverUtil;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.util.Direction;
import net.infonode.util.ValueChange;

/**
 * <p>
 * A TabbedPanel is a component that handles a group of components in a notebook
 * like manor. Each component is represented by a {@link Tab}. A tab is a
 * component itself that defines how the tab will be rendered. The tab also
 * holds a reference to the content component associated with the tab. The
 * tabbed panel is divided into two areas, the tab area where the tabs are
 * displayed and the content area where the tab's content component is
 * displayed.
 * </p>
 *
 * <p>
 * The demo program for InfoNode Tabbed Panel on
 * <a href="http://www.infonode.net/index.html?itpdemo" target="_blank">
 * www.infonode.net</a> demonstrates and explains most of the tabbed panel's
 * features.
 * </p>
 *
 * <p>
 * The tabbed panel is configured using a {@link TabbedPanelProperties} object.
 * A tabbed panel will always have a properties object with default values based
 * on the current Look and Feel
 * </p>
 *
 * <p>
 * Tabs can be added, inserted, removed, selected, highlighted, dragged and
 * moved.
 * </p>
 *
 * <p>
 * The tabbed panel support tab placement in a horizontal line above or under
 * the content area or a vertical row to the left or to the right of the content
 * area. The tab line can be laid out as either scrolling or compression. If the
 * tabs are too many to fit in the tab area and scrolling is enabled, then the
 * mouse wheel is activated and scrollbuttons are shown so that the tabs can be
 * scrolled. Compression means that the tabs will be downsized to fit into the
 * visible tab area.
 * </p>
 *
 * <p>
 * It is possible to display a button in the tab area next to the tabs that shows
 * a drop down list (called tab drop down list) with all the tabs where it is
 * possible to select a tab. This is for example useful when the tabbed panel
 * contains a large amount of tabs or if some tabs are scrolled out. The drop down
 * list can show a text and an icon for a tab. The text is retrieved by calling
 * toString() on the tab and the icon is only retrieved if the tab implements the
 * {@link net.infonode.gui.icon.IconProvider} interface.
 * </p>
 *
 * <p>
 * It is possible to set an array of components (called tab area components) to
 * be shown next to the tabs in the tab area, the same place where the drop down
 * list and the scrollbuttons are shown. This for example useful for adding
 * buttons to the tabbed panel.
 * </p>
 *
 * <p>
 * It is possible to add a {@link TabListener} and receive events when a tab is
 * added, removed, selected, deselected, highlighted, dehighlighted, moved,
 * dragged, dropped or drag is aborted. The listener will receive events for all
 * the tabs in the tabbed panel. A tabbed panel will trigger selected,
 * deselected, highlighted and dehighlighted even if for example the selected
 * tab is null (no selected tab), i.e. null will be treated as if it was a tab.
 * </p>
 *
 * <p>
 * A tabbed panel supports several mouse hover alternatives. It is possible to
 * specify {@link HoverListener}s for the entire tabbed panel, the tab area, the
 * tab area components area and the content area. The listeners are set in the
 * TabbedPanelProperties, TabAreaProperties, TabAreaComponentsProperties and the
 * TabbedPanelContentPanelProperties. A hover listener is called when the mouse
 * enter or exits the area. The hover listener is called with a {@link HoverEvent}
 * and the source for the event is always the hovered tabbed panel.
 * </p>
 *
 * <p>
 * A tabbed panel calls the hover listeners in the following order:
 * <ol>
 * <li>The hover listener for the tabbed panel itself.
 * <li>The hover listener for the tab area or the content area depending on where the
 * mouse pointer is located.
 * <li>The hover listener for the tab area components area if the mouse pointer is over
 * that area.
 * </ol>
 * When the tabbed panel is no longer hovered, the hover listenrs are called in the
 * reverse order.
 * </p>
 *
 * <p>
 * It is possible to specify different hover policies ({@link TabbedPanelHoverPolicy})
 * in the TabbedPanelProperties that affects all hover areas of the tabbed panel.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see Tab
 * @see TitledTab
 * @see TabbedPanelProperties
 * @see TabListener
 * @see TabbedPanelHoverPolicy
 * @see HoverListener
 */
public class TabbedPanel extends JPanel {
  // Shadow property values
  private int shadowSize = 4;
  private ComponentPaintChecker shadowRepaintChecker;

  private TabDropDownList dropDownList;

  private JComponent contentPanel;

  private JComponent[] tabAreaComponents;

  private Direction tabAreaOrientation;
  private TabDropDownListVisiblePolicy listVisiblePolicy = TabDropDownListVisiblePolicy.NEVER;
  private TabLayoutPolicy listTabLayoutPolicy = TabLayoutPolicy.SCROLLING;
  private DraggableComponentBox draggableComponentBox = new DraggableComponentBox(TabbedUIDefaults.getButtonIconSize(),
                                                                                  false);
  private ArrayList listeners;
  private TabbedPanelProperties properties = new TabbedPanelProperties(TabbedPanelProperties.getDefaultProperties());
  private Tab highlightedTab;

  private boolean settingHighlighted;
  private boolean mouseEntered = false;
  private boolean removingSelected = false;

  private TabAreaVisiblePolicy areaVisiblePolicy = TabAreaVisiblePolicy.ALWAYS;

  private class HoverablePanel extends HoverableShapedPanel {
    public HoverablePanel(LayoutManager l, HoverListener listener) {
      super(l, listener, TabbedPanel.this);
    }

    protected void processMouseEvent(MouseEvent event) {
      super.processMouseEvent(event);
      doProcessMouseEvent(event);
    }

    protected void processMouseMotionEvent(MouseEvent event) {
      super.processMouseMotionEvent(event);
      doProcessMouseMotionEvent(event);
    }

    public boolean acceptHover(ArrayList enterableHoverables) {
      return getHoverListener() == null ? false : TabbedHoverUtil.acceptTabbedPanelHover(properties.getHoverPolicy(),
                                                                                         enterableHoverables,
                                                                                         TabbedPanel.this,
                                                                                         this);
    }
  }

  private ShadowPanel componentsPanel = new ShadowPanel();

  private ScrollButtonBox scrollButtonBox;

  private GridBagConstraints constraints = new GridBagConstraints();
  private GridBagLayout tabAreaLayoutManager = new GridBagLayout() {
    public void layoutContainer(Container parent) {
      setTabAreaComponentsButtonsVisible();
      super.layoutContainer(parent);

      // Overlap if tab area is too narrow to fit both tabAreaComponentsPanel and draggableComponentBox
      if (tabAreaComponentsPanel.isVisible()) {
        if (tabAreaOrientation.isHorizontal()) {
          if (tabAreaContainer.getHeight() < tabAreaComponentsPanel.getPreferredSize().getHeight()) {
            draggableComponentBox.setSize(draggableComponentBox.getWidth(), 0);
            tabAreaComponentsPanel.setSize(tabAreaComponentsPanel.getWidth(), tabAreaContainer.getHeight());
          }
        }
        else {
          if (tabAreaContainer.getWidth() < tabAreaComponentsPanel.getPreferredSize().getWidth()) {
            draggableComponentBox.setSize(0, draggableComponentBox.getHeight());
            tabAreaComponentsPanel.setSize(tabAreaContainer.getWidth(), tabAreaComponentsPanel.getHeight());
          }
        }
      }
      
      /*if (contentPanel != null) {
        int newSize = (tabAreaOrientation == Direction.UP || tabAreaOrientation == Direction.DOWN) ?
                      draggableComponentBox.getWidth() : draggableComponentBox.getHeight();
        int newOuterSize = (tabAreaOrientation == Direction.UP || tabAreaOrientation == Direction.DOWN) ?
                           tabAreaContainer.getWidth() : tabAreaContainer.getHeight();
        if (newOuterSize == outerSize && newSize != size) {
          size = newSize;
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	if (contentPanel.isShowing()) {
            		contentPanel.repaint();
            	}
            }
          });
        }
        outerSize = newOuterSize;
      }*/

      updateShadow();
    }
  };

  private HoverableShapedPanel tabAreaContainer = new HoverablePanel(tabAreaLayoutManager,
                                                                     properties.getTabAreaProperties()
                                                                     .getHoverListener()) {
    public Dimension getPreferredSize() {
      Dimension d = super.getPreferredSize();

      if (getTabCount() == 0) {
        Insets insets = getInsets();
        Dimension d2 = tabAreaComponentsPanel.getPreferredSize();
        d = new Dimension(insets.left + insets.right + d2.width, insets.top + insets.bottom + d2.height);
      }

      return d;
    }
  };

  private HoverableShapedPanel tabAreaComponentsPanel = new HoverablePanel(new DirectionLayout(),
                                                                           properties.getTabAreaComponentsProperties()
                                                                           .getHoverListener()) {
    public Dimension getMaximumSize() {
      return getPreferredSize();
    }

    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    public Dimension getPreferredSize() {
      Dimension d = super.getPreferredSize();
      Insets insets = getInsets();

      if (ComponentUtil.hasVisibleChildren(this)) {
        if (tabAreaOrientation.isHorizontal()) {
          int maxWidth = ComponentUtil.getPreferredMaxWidth(getComponents()) + insets.left + insets.right;
          return new Dimension(maxWidth, d.height);
        }
        else {
          int maxHeight = ComponentUtil.getPreferredMaxHeight(getComponents()) + insets.top + insets.bottom;
          return new Dimension(d.width, maxHeight);
        }
      }

      return new Dimension(0, 0);
    }
  };

  private DraggableComponentBoxListener draggableComponentBoxListener = new DraggableComponentBoxListener() {
    private boolean selectedMoved;

    public void componentSelected(DraggableComponentBoxEvent event) {
      if (event.getDraggableComponent() == event.getOldDraggableComponent()) {
        if (!selectedMoved && properties.getTabDeselectable())
          draggableComponentBox.selectDraggableComponent(null);
      }
      else {
        Tab tab = findTab(event.getDraggableComponent());
        setHighlightedTab(tab);
        Tab oldTab = findTab(event.getOldDraggableComponent());
        fireSelectedEvent(tab, oldTab);
        if (removingSelected) {
          removingSelected = false;
          if (oldTab != null)
            oldTab.setTabbedPanel(null);
        }
      }

      tabAreaContainer.repaint();
    }

    public void componentRemoved(DraggableComponentBoxEvent event) {
      Tab tab = findTab(event.getDraggableComponent());
      if (highlightedTab == tab)
        highlightedTab = null;

      setTabAreaComponentsButtonsVisible();
      updateTabAreaVisibility();
      //revalidate();
      tabAreaContainer.repaint();
      fireRemovedEvent(tab);
    }

    public void componentAdded(DraggableComponentBoxEvent event) {
      updateTabAreaVisibility();
      //revalidate();
      tabAreaContainer.repaint();
      fireAddedEvent(findTab(event.getDraggableComponent()));
    }

    public void componentDragged(DraggableComponentBoxEvent event) {
      fireDraggedEvent(findTab(event.getDraggableComponent()),
                       event.getDraggableComponentEvent().getMouseEvent());
    }

    public void componentDropped(DraggableComponentBoxEvent event) {
      if (!draggableComponentBox.contains(event.getDraggableComponentBoxPoint()))
        setHighlightedTab(findTab(draggableComponentBox.getSelectedDraggableComponent()));
      fireDroppedEvent(findTab(event.getDraggableComponent()),
                       event.getDraggableComponentEvent().getMouseEvent());
    }

    public void componentDragAborted(DraggableComponentBoxEvent event) {
      fireNotDroppedEvent(findTab(event.getDraggableComponent()));
    }

    public void changed(DraggableComponentBoxEvent event) {
      if (event.getDraggableComponentEvent() != null) {
        int type = event.getDraggableComponentEvent().getType();

        if (type == DraggableComponentEvent.TYPE_PRESSED && properties.getHighlightPressedTab()) {
          if (highlightedTab != null)
            setHighlightedTab(findTab(event.getDraggableComponent()));
        }
        else if (type == DraggableComponentEvent.TYPE_RELEASED) {
          selectedMoved = false;
          setHighlightedTab(getSelectedTab());
        }
        else if (type == DraggableComponentEvent.TYPE_DISABLED && highlightedTab != null &&
                 highlightedTab.getDraggableComponent() == event.getDraggableComponent())
          setHighlightedTab(null);
        else if (type == DraggableComponentEvent.TYPE_ENABLED &&
                 draggableComponentBox.getSelectedDraggableComponent() == event.getDraggableComponent())
          setHighlightedTab(findTab(event.getDraggableComponent()));
        else if (type == DraggableComponentEvent.TYPE_MOVED) {
          tabAreaContainer.repaint();
          selectedMoved = event.getDraggableComponent() == draggableComponentBox.getSelectedDraggableComponent();
          fireTabMoved(findTab(event.getDraggableComponent()));
        }
      }
      else {
        // Scrolling
        tabAreaContainer.repaint();
      }

      updateShadow();
    }
  };

  private PropertyMapTreeListener propertyChangedListener = new PropertyMapTreeListener() {
    public void propertyValuesChanged(Map changes) {
      updateProperties(changes);
      updatePropertiesForTabArea(changes);
      updatePropertiesForTabAreaComponentsArea(changes);
      updatePropertiesForTabAreaComponentsButtons(changes);

      updateScrollButtons();
      checkIfOnlyOneTab(true);
    }
  };

  private void updatePropertiesForTabAreaComponentsButtons(Map changes) {
    TabbedPanelButtonProperties buttonProps = properties.getButtonProperties();
    Map m = (Map) changes.get(buttonProps.getTabDropDownListButtonProperties().getMap());
    {
      if (m != null) {
        if (dropDownList != null) {
          AbstractButton b = dropDownList.getButton();
          if (m.keySet().contains(ButtonProperties.FACTORY)) {
            b =
            properties.getButtonProperties().getTabDropDownListButtonProperties().getFactory().createButton(
                TabbedPanel.this);
            dropDownList.setButton(b);
          }

          properties.getButtonProperties().getTabDropDownListButtonProperties().applyTo(b);
        }
      }
    }

    if (scrollButtonBox != null) {
      AbstractButton buttons[] = new AbstractButton[]{scrollButtonBox.getUpButton(),
                                                      scrollButtonBox.getDownButton(),
                                                      scrollButtonBox.getLeftButton(),
                                                      scrollButtonBox.getRightButton()
      };

      ButtonProperties props[] = new ButtonProperties[]{buttonProps.getScrollUpButtonProperties(),
                                                        buttonProps.getScrollDownButtonProperties(),
                                                        buttonProps.getScrollLeftButtonProperties(),
                                                        buttonProps.getScrollRightButtonProperties()
      };

      for (int i = 0; i < buttons.length; i++) {
        m = (Map) changes.get(props[i].getMap());
        if (m != null) {
          if (m.keySet().contains(ButtonProperties.FACTORY))
            buttons[i] = props[i].getFactory().createButton(TabbedPanel.this);

          props[i].applyTo(buttons[i]);
        }
      }

      scrollButtonBox.setButtons(buttons[0], buttons[1], buttons[2], buttons[3]);
    }
  }

  private void updateAllDefaultValues() {
    // General
    updateAllTabsProperties();
    draggableComponentBox.setScrollEnabled(properties.getTabLayoutPolicy() == TabLayoutPolicy.SCROLLING);
    updateTabDropDownList();
    draggableComponentBox.setScrollOffset(properties.getTabScrollingOffset());
    draggableComponentBox.setEnsureSelectedVisible(properties.getEnsureSelectedTabVisible());

    tabAreaOrientation = properties.getTabAreaOrientation();
    updatePropertiesForTabAreaLayoutConstraints();
    componentsPanel.add(tabAreaContainer, ComponentUtil.getBorderLayoutOrientation(tabAreaOrientation));
    componentsPanel.revalidate();

    draggableComponentBox.setComponentSpacing(properties.getTabSpacing());
    draggableComponentBox.setDepthSortOrder(properties.getTabDepthOrderPolicy() == TabDepthOrderPolicy.DESCENDING);
    draggableComponentBox.setAutoSelect(properties.getAutoSelectTab());

    shadowSize = properties.getShadowSize();
    componentsPanel.setBorder(
        contentPanel != null && properties.getShadowEnabled() ? new EmptyBorder(0, 0, shadowSize, shadowSize) : null);

    componentsPanel.setHoverListener(properties.getHoverListener());

    // Tab area
    tabAreaContainer.setHoverListener(properties.getTabAreaProperties().getHoverListener());
    ShapedPanelProperties shapedProps = properties.getTabAreaProperties().getShapedPanelProperties();
    properties.getTabAreaProperties().getComponentProperties().applyTo(tabAreaContainer);
    updateIntelligentInsets(tabAreaContainer, properties.getTabAreaProperties().getComponentProperties());
    updateShapedPanelProperties(tabAreaContainer, properties.getTabAreaProperties().getShapedPanelProperties());


    // Tab area components area
    tabAreaComponentsPanel.setHoverListener(properties.getTabAreaComponentsProperties().getHoverListener());
    updatePropertiesForTabAreaLayoutConstraints();
    shapedProps = properties.getTabAreaComponentsProperties().getShapedPanelProperties();
    properties.getTabAreaComponentsProperties().getComponentProperties().applyTo(tabAreaComponentsPanel);
    updateIntelligentInsets(tabAreaComponentsPanel,
                            properties.getTabAreaComponentsProperties().getComponentProperties());
    updateShapedPanelProperties(tabAreaComponentsPanel,
                                shapedProps);
    tabAreaComponentsPanel.setHorizontalFlip(tabAreaOrientation == Direction.DOWN || tabAreaOrientation == Direction.LEFT ?
                                             !shapedProps.getHorizontalFlip() : shapedProps.getHorizontalFlip());
    tabAreaComponentsPanel.setVerticalFlip(shapedProps.getVerticalFlip());

    updatePanelOpaque();
  }


  private void updateProperties(Map changes) {
    Map m = getMap(changes, properties.getMap());
    if (m != null) {
      Set keySet = m.keySet();

      // Properties contained by tabs
      if (keySet.contains(TabbedPanelProperties.TAB_REORDER_ENABLED) || m.keySet().contains(
          TabbedPanelProperties.ABORT_DRAG_KEY)
          || m.keySet().contains(TabbedPanelProperties.TAB_SELECT_TRIGGER))
        updateAllTabsProperties();

      // Other
      if (keySet.contains(TabbedPanelProperties.TAB_LAYOUT_POLICY) && getTabCount() > 1)
        draggableComponentBox.setScrollEnabled(
            ((TabLayoutPolicy) ((ValueChange) m.get(TabbedPanelProperties.TAB_LAYOUT_POLICY)).getNewValue()) == TabLayoutPolicy.SCROLLING);

      if (keySet.contains(TabbedPanelProperties.TAB_DROP_DOWN_LIST_VISIBLE_POLICY))
        updateTabDropDownList();

      if (keySet.contains(TabbedPanelProperties.TAB_SCROLLING_OFFSET))
        draggableComponentBox.setScrollOffset(
            ((Integer) ((ValueChange) m.get(TabbedPanelProperties.TAB_SCROLLING_OFFSET)).getNewValue()).intValue());

      if (keySet.contains(TabbedPanelProperties.ENSURE_SELECTED_VISIBLE))
        draggableComponentBox.setEnsureSelectedVisible(
            ((Boolean) ((ValueChange) m.get(TabbedPanelProperties.ENSURE_SELECTED_VISIBLE)).getNewValue()).booleanValue());

      if (keySet.contains(TabbedPanelProperties.TAB_AREA_ORIENTATION)) {
        tabAreaOrientation = (Direction) ((ValueChange) m.get(TabbedPanelProperties.TAB_AREA_ORIENTATION)).getNewValue();
        updatePropertiesForTabAreaLayoutConstraints();
        componentsPanel.remove(tabAreaContainer);
        componentsPanel.add(tabAreaContainer, ComponentUtil.getBorderLayoutOrientation(tabAreaOrientation));

        componentsPanel.revalidate();

        properties.getTabAreaComponentsProperties().getComponentProperties().applyTo(tabAreaComponentsPanel);
        updateIntelligentInsets(tabAreaContainer, properties.getTabAreaProperties().getComponentProperties());
        tabAreaComponentsPanel.setDirection(tabAreaOrientation.getNextCW());
        updateIntelligentInsets(tabAreaComponentsPanel,
                                properties.getTabAreaComponentsProperties().getComponentProperties());
      }

      if (keySet.contains(TabbedPanelProperties.TAB_SPACING))
        draggableComponentBox.setComponentSpacing(
            ((Integer) ((ValueChange) m.get(TabbedPanelProperties.TAB_SPACING)).getNewValue()).intValue());

      if (keySet.contains(TabbedPanelProperties.TAB_DEPTH_ORDER))
        draggableComponentBox.setDepthSortOrder(
            ((TabDepthOrderPolicy) ((ValueChange) m.get(TabbedPanelProperties.TAB_DEPTH_ORDER)).getNewValue()) == TabDepthOrderPolicy.DESCENDING);

      if (keySet.contains(TabbedPanelProperties.AUTO_SELECT_TAB))
        draggableComponentBox.setAutoSelect(
            ((Boolean) ((ValueChange) m.get(TabbedPanelProperties.AUTO_SELECT_TAB)).getNewValue()).booleanValue());

      /*
       * if
       * (keySet.contains(TabbedPanelProperties.HIGHLIGHT_PRESSED_TAB)) { //
       * Elsewhere }
       *
       * if (keySet.contains(TabbedPanelProperties.TAB_DESELECTABLE)) { //
       * Elsewhere }
       */

      if (keySet.contains(TabbedPanelProperties.SHADOW_ENABLED) || m.keySet().contains(
          TabbedPanelProperties.SHADOW_STRENGTH) || m.keySet().contains(TabbedPanelProperties.SHADOW_COLOR) || m.keySet()
          .contains(TabbedPanelProperties.SHADOW_BLEND_AREA_SIZE) || m.keySet().contains(
              TabbedPanelProperties.SHADOW_SIZE) || m.keySet().contains(TabbedPanelProperties.PAINT_TAB_AREA_SHADOW)) {
        shadowSize = properties.getShadowSize();
        componentsPanel.setBorder(
            contentPanel != null && properties.getShadowEnabled() ?
            new EmptyBorder(0, 0, shadowSize, shadowSize) : null);
      }

      if (keySet.contains(TabbedPanelProperties.HOVER_LISTENER)) {
        componentsPanel.setHoverListener(
            (HoverListener) ((ValueChange) m.get(TabbedPanelProperties.HOVER_LISTENER)).getNewValue());
      }
    }

    updatePanelOpaque();
  }

  private void updatePropertiesForTabArea(Map changes) {
    Map m = getMap(changes, properties.getTabAreaProperties().getMap());
    if (m != null) {
      if (m.keySet().contains(TabAreaProperties.HOVER_LISTENER)) {
        tabAreaContainer.setHoverListener(
            (HoverListener) ((ValueChange) m.get(TabAreaProperties.HOVER_LISTENER)).getNewValue());
      }

      areaVisiblePolicy = getProperties().getTabAreaProperties().getTabAreaVisiblePolicy();
      updateTabAreaVisibility();
    }

    m = getMap(changes, properties.getTabAreaProperties().getComponentProperties().getMap());
    Map m2 = getMap(changes, properties.getTabAreaProperties().getShapedPanelProperties().getMap());
    if (m != null || m2 != null) {
      properties.getTabAreaProperties().getComponentProperties().applyTo(tabAreaContainer);
      updateIntelligentInsets(tabAreaContainer, properties.getTabAreaProperties().getComponentProperties());

      updateShapedPanelProperties(tabAreaContainer, properties.getTabAreaProperties().getShapedPanelProperties());

      repaint();
    }
  }

  private void updateIntelligentInsets(JComponent c, ComponentProperties props) {
    Direction d = properties.getTabAreaOrientation();
    Insets insets = props.getInsets();
    if (insets != null) {
      if (d == Direction.RIGHT)
        insets = new Insets(insets.left, insets.bottom, insets.right, insets.top);
      else if (d == Direction.DOWN)
        insets = new Insets(insets.bottom, insets.left, insets.top, insets.right);
      else if (d == Direction.LEFT)
        insets = new Insets(insets.left, insets.top, insets.right, insets.bottom);

      Border b = props.getBorder();
      c.setBorder(b != null ? (Border) new CompoundBorder(b, new EmptyBorder(insets)) : (Border) new EmptyBorder(
          insets));
    }
  }

  private void updatePropertiesForTabAreaComponentsArea(Map changes) {
    Map m = getMap(changes, properties.getTabAreaComponentsProperties().getMap());

    if (m != null) {
      if (m.keySet().contains(TabAreaComponentsProperties.HOVER_LISTENER)) {
        tabAreaComponentsPanel.setHoverListener(
            (HoverListener) ((ValueChange) m.get(TabAreaComponentsProperties.HOVER_LISTENER)).getNewValue());
      }

      if (m.keySet().contains(TabAreaComponentsProperties.STRETCH_ENABLED)) {
        updatePropertiesForTabAreaLayoutConstraints();
      }
    }

    m = getMap(changes, properties.getTabAreaComponentsProperties().getComponentProperties().getMap());
    Map m2 = getMap(changes, properties.getTabAreaComponentsProperties().getShapedPanelProperties().getMap());
    if (m != null || m2 != null) {
      ShapedPanelProperties shapedProps = properties.getTabAreaComponentsProperties().getShapedPanelProperties();
      properties.getTabAreaComponentsProperties().getComponentProperties().applyTo(tabAreaComponentsPanel);
      updateIntelligentInsets(tabAreaComponentsPanel,
                              properties.getTabAreaComponentsProperties().getComponentProperties());
      updateShapedPanelProperties(tabAreaComponentsPanel, shapedProps);
      tabAreaComponentsPanel.setHorizontalFlip(
          tabAreaOrientation == Direction.DOWN || tabAreaOrientation == Direction.LEFT ?
          !shapedProps.getHorizontalFlip() : shapedProps.getHorizontalFlip());
      tabAreaComponentsPanel.setVerticalFlip(shapedProps.getVerticalFlip());
    }
  }

  private void updatePropertiesForTabAreaLayoutConstraints() {
    boolean stretch = properties.getTabAreaComponentsProperties().getStretchEnabled();
    if (tabAreaOrientation == Direction.UP) {
      setTabAreaLayoutConstraints(draggableComponentBox,
                                  0,
                                  0,
                                  GridBagConstraints.HORIZONTAL,
                                  1,
                                  1,
                                  GridBagConstraints.SOUTH);
      setTabAreaLayoutConstraints(tabAreaComponentsPanel,
                                  1,
                                  0,
                                  stretch ? GridBagConstraints.VERTICAL : GridBagConstraints.NONE,
                                  0,
                                  1,
                                  GridBagConstraints.SOUTH);
      updateTabAreaComponentsPanel(Direction.RIGHT, 0, 1);
    }
    else if (tabAreaOrientation == Direction.DOWN) {
      setTabAreaLayoutConstraints(draggableComponentBox,
                                  0,
                                  0,
                                  GridBagConstraints.HORIZONTAL,
                                  1,
                                  1,
                                  GridBagConstraints.NORTH);
      setTabAreaLayoutConstraints(tabAreaComponentsPanel,
                                  1,
                                  0,
                                  stretch ? GridBagConstraints.VERTICAL : GridBagConstraints.NONE,
                                  0,
                                  0,
                                  GridBagConstraints.NORTH);
      updateTabAreaComponentsPanel(Direction.RIGHT, 0, 0);
    }
    else if (tabAreaOrientation == Direction.LEFT) {
      setTabAreaLayoutConstraints(draggableComponentBox,
                                  0,
                                  0,
                                  GridBagConstraints.VERTICAL,
                                  1,
                                  1,
                                  GridBagConstraints.EAST);
      setTabAreaLayoutConstraints(tabAreaComponentsPanel,
                                  0,
                                  1,
                                  stretch ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE,
                                  0,
                                  0,
                                  GridBagConstraints.EAST);
      updateTabAreaComponentsPanel(Direction.DOWN, 0, 0);
    }
    else {
      setTabAreaLayoutConstraints(draggableComponentBox,
                                  0,
                                  0,
                                  GridBagConstraints.VERTICAL,
                                  1,
                                  1,
                                  GridBagConstraints.WEST);
      setTabAreaLayoutConstraints(tabAreaComponentsPanel,
                                  0,
                                  1,
                                  stretch ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE,
                                  0,
                                  0,
                                  GridBagConstraints.WEST);
      updateTabAreaComponentsPanel(Direction.DOWN, 0, 1);
    }

    draggableComponentBox.setComponentDirection(tabAreaOrientation);
  }

  private Map getMap(Map changes, PropertyMap map) {
    return changes != null ? (Map) changes.get(map) : null;
  }

  private void updateTabAreaVisibility() {
    if (areaVisiblePolicy == TabAreaVisiblePolicy.ALWAYS)
      tabAreaContainer.setVisible(true);
    else if (areaVisiblePolicy == TabAreaVisiblePolicy.NEVER)
      tabAreaContainer.setVisible(false);
    else if (areaVisiblePolicy == TabAreaVisiblePolicy.MORE_THAN_ONE_TAB)
      tabAreaContainer.setVisible(getTabCount() > 1);
    else if (areaVisiblePolicy == TabAreaVisiblePolicy.TABS_EXIST)
      tabAreaContainer.setVisible(getTabCount() > 0);

    if (!tabAreaContainer.isVisible())
      tabAreaContainer.setSize(0, 0);
  }

  /**
   * Constructs a TabbedPanel with a TabbedPanelContentPanel as content area
   * component and with default TabbedPanelProperties
   *
   * @see TabbedPanelProperties
   * @see TabbedPanelContentPanel
   */
  public TabbedPanel() {
    initialize(new TabbedPanelContentPanel(this, new TabContentPanel(this)));
  }

  /**
   * <p>
   * Constructs a TabbedPanel with a custom component as content area
   * component or without any content area component and with default
   * TabbedPanelProperties. The properties for the content area will not be used.
   * </p>
   *
   * <p>
   * If no content area component is used, then the tabbed panel will act as a
   * bar and the tabs will be laid out in a line.
   * </p>
   *
   * <p>
   * <strong>Note: </strong> A custom content area component is by itself
   * responsible for showing a tab's content component when a tab is selected,
   * for eaxmple by listening to events from the tabbed panel. The component
   * will be laid out just as the default content area component so that
   * shadows etc. can be used.
   * </p>
   *
   * @param contentAreaComponent component to be used as content area component or null for no
   *                             content area component
   * @see TabbedPanelProperties
   */
  public TabbedPanel(JComponent contentAreaComponent) {
    this(contentAreaComponent, false);
  }

  /**
   * <p>
   * Constructs a TabbedPanel with a custom component as content area
   * component or without any content area component and with default
   * TabbedPanelProperties. It's possible to choose if the properties for the
   * content area should be used or not.
   * </p>
   *
   * <p>
   * If no content area component is used, then the tabbed panel will act as a
   * bar and the tabs will be laid out in a line.
   * </p>
   *
   * <p>
   * <strong>Note: </strong> A custom content area component is by itself
   * responsible for showing a tab's content component when a tab is selected,
   * for eaxmple by listening to events from the tabbed panel. The component
   * will be laid out just as the default content area component so that
   * shadows etc. can be used.
   * </p>
   *
   * @param contentAreaComponent component to be used as content area component or null for no
   *                             content area component
   * @param useProperties        true if the properties for the content area should be used,
   *                             otherwise false
   * @see TabbedPanelProperties
   * @since ITP 1.4.0
   */
  public TabbedPanel(JComponent contentAreaComponent, boolean useProperties) {
    if (useProperties)
      initialize(new TabbedPanelContentPanel(this, contentAreaComponent));
    else
      initialize(contentAreaComponent);
  }

  /**
   * Check if the tab area contains the given point
   *
   * @param p the point to check. Must be relative to this tabbed panel.
   * @return true if tab area contains point, otherwise false
   * @see #contentAreaContainsPoint
   */
  public boolean tabAreaContainsPoint(Point p) {
    if (!tabAreaContainer.isVisible())
      return false;

    return tabAreaContainer.contains(SwingUtilities.convertPoint(this, p, tabAreaContainer));
  }

  /**
   * Check if the content area contains the given point
   *
   * @param p the point to check. Must be relative to this tabbed panel.
   * @return true if content area contains point, otherwise false
   * @see #tabAreaContainsPoint
   */
  public boolean contentAreaContainsPoint(Point p) {
    return contentPanel != null ? contentPanel.contains(SwingUtilities.convertPoint(this, p, contentPanel)) : false;
  }

  /**
   * Checks if the tab area is currently visible
   *
   * @return true if visible, otherwise false
   * @since ITP 1.4.0
   */
  public boolean isTabAreaVisible() {
    return tabAreaContainer.isVisible();
  }

  /**
   * <p>
   * Add a tab. The tab will be added after the last tab.
   * </p>
   *
   * <p>
   * If the tab to be added is the only tab in this tabbed panel and the
   * property "Auto Select Tab" is enabled then the tab will become selected
   * in this tabbed panel after the tab has been added.
   * </p>
   *
   * @param tab tab to be added
   * @see #insertTab(Tab, int)
   * @see TabbedPanelProperties
   */
  public void addTab(Tab tab) {
    doInsertTab(tab, null, -1);
  }

  /**
   * <p>
   * Insert a tab at the specified tab index (position).
   * </p>
   *
   * <p>
   * If the tab to be inserted is the only tab in this tabbed panel and the
   * property "Auto Select Tab" is enabled then the tab will become selected
   * in this tabbed panel after the tab has been inserted.
   * </p>
   *
   * @param tab   tab to be inserted
   * @param index the index to insert tab at
   * @see #addTab
   * @see TabbedPanelProperties
   */
  public void insertTab(Tab tab, int index) {
    doInsertTab(tab, null, index);
  }

  /**
   * <p>
   * Insert a tab at the specified point.
   * </p>
   *
   * <p>
   * If the point is outside the tab area then the tab will be inserted after
   * the last tab.
   * </p>
   *
   * <p>
   * If the tab to be inserted is the only tab in this tabbed panel and the
   * property "Auto Select Tab" is enabled then the tab will become selected
   * in this tabbed panel after the tab has been inserted.
   * </p>
   *
   * @param tab tab to be inserted
   * @param p   the point to insert tab at. Must be relative to this tabbed
   *            panel.
   * @see #addTab
   * @see TabbedPanelProperties
   */
  public void insertTab(Tab tab, Point p) {
    doInsertTab(tab, p, -1);
  }

  /**
   * Removes a tab
   *
   * @param tab tab to be removed from this TabbedPanel
   */
  public void removeTab(Tab tab) {
    if (tab != null && tab.getTabbedPanel() == this) {
      if (getSelectedTab() != tab) {
        tab.setTabbedPanel(null);
      }
      else {
        removingSelected = true;
      }
      draggableComponentBox.removeDraggableComponent(tab.getDraggableComponent());
    }
    checkIfOnlyOneTab(false);
  }

  /**
   * Move tab to point p. If p is outside the tab area then the tab is not
   * moved.
   *
   * @param tab tab to move. Tab must be a member (added/inserted) of this
   *            tabbed panel.
   * @param p   point to move tab to. Must be relative to this tabbed panel.
   */
  public void moveTab(Tab tab, Point p) {
    draggableComponentBox.dragDraggableComponent(tab.getDraggableComponent(),
                                                 SwingUtilities.convertPoint(this, p, draggableComponentBox));
  }

  /**
   * Selects a tab, i.e. displays the tab's content component in this tabbed
   * panel's content area
   *
   * @param tab tab to select. Tab must be a member (added/inserted) of this
   *            tabbed panel.
   */
  public void setSelectedTab(Tab tab) {
    if (getSelectedTab() == tab)
      return;

    if (tab != null) {
      if (tab.isEnabled() && getTabIndex(tab) > -1) {
        if (tab.getDraggableComponent() == draggableComponentBox.getSelectedDraggableComponent()) {
          setHighlightedTab(tab);
        }
        else {
          tab.setSelected(true);
        }
      }
    }
    else {
      draggableComponentBox.selectDraggableComponent(null);
    }
  }

  /**
   * Gets the selected tab, i.e. the tab who's content component is currently
   * displayed in this tabbed panel's content area
   *
   * @return the selected tab or null if no tab is selected in this tabbed
   *         panel
   */
  public Tab getSelectedTab() {
    return findTab(draggableComponentBox.getSelectedDraggableComponent());
  }

  /**
   * Sets which tab that should be highlighted, i.e. signal highlighted state
   * to the tab
   *
   * @param highlightedTab tab that should be highlighted or null if no tab should be
   *                       highlighted. The tab must be a member (added/inserted) of this
   *                       tabbed panel.
   */
  public void setHighlightedTab(Tab highlightedTab) {
    if (!settingHighlighted) {
      settingHighlighted = true;
      Tab oldTab = this.highlightedTab;
      Tab newTab = null;
      if (oldTab != highlightedTab)
        draggableComponentBox.setTopComponent(highlightedTab != null ? highlightedTab.getDraggableComponent() : null);
      if (highlightedTab != null) {
        if (getTabIndex(highlightedTab) > -1) {
          this.highlightedTab = highlightedTab;
          if (oldTab != null && oldTab != highlightedTab) {
            oldTab.setHighlighted(false);
          }

          if (oldTab != highlightedTab)
            if (highlightedTab.isEnabled()) {
              highlightedTab.setHighlighted(true);
            }
            else {
              highlightedTab.setHighlighted(false);
              this.highlightedTab = null;
            }

          if (highlightedTab.isEnabled() && highlightedTab != oldTab)
            newTab = highlightedTab;

          if (oldTab != highlightedTab)
            fireHighlightedEvent(newTab, oldTab);
        }
      }
      else if (oldTab != null) {
        this.highlightedTab = null;
        oldTab.setHighlighted(false);
        fireHighlightedEvent(null, oldTab);
      }

      updateShadow();
      settingHighlighted = false;
    }
  }

  /**
   * Gets the highlighted tab
   *
   * @return the highlighted tab or null if no tab is highlighted in this
   *         tabbed panel
   */
  public Tab getHighlightedTab() {
    return highlightedTab;
  }

  /**
   * Gets the number of tabs
   *
   * @return number of tabs
   */
  public int getTabCount() {
    return draggableComponentBox.getDraggableComponentCount();
  }

  /**
   * Gets the tab at index
   *
   * @param index index of tab
   * @return tab at index
   * @throws ArrayIndexOutOfBoundsException if there is no tab at index
   */
  public Tab getTabAt(int index) {
    DraggableComponent component = draggableComponentBox.getDraggableComponentAt(index);
    return component == null ? null : (Tab) component.getComponent();
  }

  /**
   * Gets the index for tab
   *
   * @param tab tab
   * @return index or -1 if tab is not a member of this TabbedPanel
   */
  public int getTabIndex(Tab tab) {
    return tab == null ? -1 : draggableComponentBox.getDraggableComponentIndex(tab.getDraggableComponent());
  }

  /**
   * <p>
   * Scrolls the given tab into the visible area of the tab area.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> This only has effect if the active tab layout
   * policy is scrolling.
   * </p>
   *
   * @param tab tab to scroll into visible tab area
   * @since ITP 1.4.0
   */
  public void scrollTabToVisibleArea(Tab tab) {
    if (tab.getTabbedPanel() == this)
      draggableComponentBox.scrollToVisible(tab.getDraggableComponent());
  }

  /**
   * Sets an array of components that will be shown in the tab area next to
   * the tabs, i.e. to the right or below the tabs depending on the tab area
   * orientation.
   *
   * The components will be laid out in a line and the direction
   * will change depending on the tab area orientation. Tab drop down list and
   * scroll buttons are also tab area components but those are handled
   * automatically by the tabbed panel and are not affected by calling this
   * method.
   *
   * @param tabAreaComponents array of components, null for no components
   * @since ITP 1.1.0
   */
  public void setTabAreaComponents(JComponent[] tabAreaComponents) {
    if (this.tabAreaComponents != null) {
      for (int i = 0; i < this.tabAreaComponents.length; i++)
        tabAreaComponentsPanel.remove(this.tabAreaComponents[i]);
    }

    this.tabAreaComponents = tabAreaComponents == null ? null : (JComponent[]) tabAreaComponents.clone();

    if (tabAreaComponents != null)
      for (int i = 0; i < tabAreaComponents.length; i++)
        tabAreaComponentsPanel.add(tabAreaComponents[i]);

    setTabAreaComponentsButtonsVisible();
    tabAreaComponentsPanel.revalidate();
  }

  /**
   * Gets if any tab area components i.e. scroll buttons etc are visible at the moment
   *
   * @return true if visible, otherwise false
   * @since ITP 1.2.0
   */
  public boolean isTabAreaComponentsVisible() {
    return tabAreaComponentsPanel.isVisible();
  }

  /**
   * Gets the tab area components.
   *
   * Tab drop down list and scroll buttons are also tab area components but
   * those are handled automatically by the tabbed panel and no references
   * to them will be returned. This method only returns the components that
   * have been set with the setTabAreaComponents method.
   *
   * @return an array of tab area components or null if none
   * @see #setTabAreaComponents
   * @since ITP 1.1.0
   */
  public JComponent[] getTabAreaComponents() {
    return tabAreaComponents == null ? null : (JComponent[]) tabAreaComponents.clone();
  }

  /**
   * Adds a TablListener that will receive events for all the tabs in this
   * TabbedPanel
   *
   * @param listener the TabListener to add
   */
  public void addTabListener(TabListener listener) {
    if (listeners == null)
      listeners = new ArrayList(2);

    listeners.add(listener);
  }

  /**
   * Removes a TabListener
   *
   * @param listener the TabListener to remove
   */
  public void removeTabListener(TabListener listener) {
    if (listeners != null) {
      listeners.remove(listener);

      if (listeners.size() == 0)
        listeners = null;
    }
  }

  /**
   * Gets the TabbedPanelProperties
   *
   * @return the TabbedPanelProperties for this tabbed panel
   */
  public TabbedPanelProperties getProperties() {
    return properties;
  }

  /**
   * Checks if this tabbed panel has a content area
   *
   * @return true if content area exist, otherwise false
   * @since ITP 1.3.0
   */
  public boolean hasContentArea() {
    return contentPanel != null;
  }

  DraggableComponentBox getDraggableComponentBox() {
    return draggableComponentBox;
  }

  private void initialize(JComponent contentPanel) {
    setLayout(new BorderLayout());

    shadowRepaintChecker = new ComponentPaintChecker(this);

    setOpaque(false);

    draggableComponentBox.setOuterParentArea(tabAreaContainer);
    tabAreaContainer.add(tabAreaComponentsPanel);
    tabAreaContainer.add(draggableComponentBox);

    this.contentPanel = contentPanel;
    draggableComponentBox.addListener(draggableComponentBoxListener);

    if (contentPanel != null) {
      componentsPanel.add(contentPanel, BorderLayout.CENTER);
    }

    add(componentsPanel, BorderLayout.CENTER);

    updateAllDefaultValues();

    PropertyMapWeakListenerManager.addWeakTreeListener(properties.getMap(), propertyChangedListener);
    //updateProperties(null);
  }

  /*private void updateProperties(Map changes) {
    //componentsPanel.remove(draggableComponentBox);
    tabAreaOrientation = properties.getTabAreaOrientation();
    updateTabArea();
    updateAllTabsProperties();

    componentsPanel.add(tabAreaContainer, ComponentUtil.getBorderLayoutOrientation(tabAreaOrientation));

    // Shadow
    shadowSize = properties.getShadowSize();
    componentsPanel.setBorder(
        contentPanel != null && properties.getShadowEnabled() ? new EmptyBorder(0, 0, shadowSize, shadowSize) : null);

    checkOnlyOneTab(true);

    updateScrollButtons();
    updateTabDropDownList();

    //repaint();
    //revalidate();
  }*/

  private void updateTabDropDownList() {
    TabDropDownListVisiblePolicy newListVisiblePolicy = properties.getTabDropDownListVisiblePolicy();
    TabLayoutPolicy newListTabLayoutPolicy = properties.getTabLayoutPolicy();

    if (newListVisiblePolicy != listVisiblePolicy || newListTabLayoutPolicy != listTabLayoutPolicy) {
      if (dropDownList != null) {
        tabAreaComponentsPanel.remove(dropDownList);
        dropDownList.dispose();
        dropDownList = null;
      }

      if (newListVisiblePolicy == TabDropDownListVisiblePolicy.MORE_THAN_ONE_TAB ||
          (newListVisiblePolicy == TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE &&
           newListTabLayoutPolicy == TabLayoutPolicy.SCROLLING)) {
        dropDownList = new TabDropDownList(this, properties.getButtonProperties().getTabDropDownListButtonProperties()
                                                 .applyTo(properties.getButtonProperties()
                                                          .getTabDropDownListButtonProperties()
                                                          .getFactory()
                                                          .createButton(this)));
        tabAreaComponentsPanel.add(dropDownList, scrollButtonBox == null ? 0 : 1);

        if (newListVisiblePolicy == TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE)
          dropDownList.setVisible(false);
      }
    }

    listVisiblePolicy = newListVisiblePolicy;
    listTabLayoutPolicy = newListTabLayoutPolicy;

    if (dropDownList != null && !draggableComponentBox.isScrollEnabled() &&
        listVisiblePolicy == TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE)
      dropDownList.setVisible(false);

    tabAreaComponentsPanel.revalidate();
  }

  private void updateAllTabsProperties() {
    Component[] components = draggableComponentBox.getBoxComponents();
    for (int i = 0; i < components.length; i++)
      updateTabProperties((Tab) components[i]);
  }

  private void updateTabProperties(Tab tab) {
    tab.getDraggableComponent().setAbortDragKeyCode(properties.getAbortDragKey());
    tab.getDraggableComponent().setReorderEnabled(properties.getTabReorderEnabled());
    tab.getDraggableComponent().setSelectOnMousePress(properties.getTabSelectTrigger() == TabSelectTrigger.MOUSE_PRESS);
  }

  private void updateTabAreaComponentsPanel(Direction direction, int alignmentX, int alignmentY) {
    ((DirectionLayout) tabAreaComponentsPanel.getLayout()).setDirection(direction);
  }

  private void updateShapedPanelProperties(ShapedPanel panel,
                                           ShapedPanelProperties shapedPanelProperties) {

    InternalPropertiesUtil.applyTo(shapedPanelProperties, panel, properties.getTabAreaOrientation().getNextCW());
  }

  private void setTabAreaLayoutConstraints(JComponent c,
                                           int gridx,
                                           int gridy,
                                           int fill,
                                           double weightx,
                                           double weighty,
                                           int anchor) {
    constraints.gridx = gridx;
    constraints.gridy = gridy;
    constraints.fill = fill;
    constraints.weightx = weightx;
    constraints.weighty = weighty;
    constraints.anchor = anchor;

    tabAreaLayoutManager.setConstraints(c, constraints);
  }

  private void doInsertTab(Tab tab, Point p, int index) {
    if (tab != null && !draggableComponentBox.containsDraggableComponent(tab.getDraggableComponent())) {
      tab.setTabbedPanel(this);
      if (p != null)
        draggableComponentBox.insertDraggableComponent(tab.getDraggableComponent(),
                                                       SwingUtilities.convertPoint(this, p, draggableComponentBox));
      else
        draggableComponentBox.insertDraggableComponent(tab.getDraggableComponent(), index);
      updateTabProperties(tab);
      checkIfOnlyOneTab(true);
    }
  }

  private Tab findTab(DraggableComponent draggableComponent) {
    return draggableComponent == null ? null : (Tab) draggableComponent.getComponent();
  }

  private void checkIfOnlyOneTab(boolean inc) {
    if (getTabCount() == 1) {
      draggableComponentBox.setScrollEnabled(false);
      updateScrollButtons();
    }
    else if (inc && getTabCount() == 2) {
      draggableComponentBox.setScrollEnabled(properties.getTabLayoutPolicy() == TabLayoutPolicy.SCROLLING);
      updateScrollButtons();
      updateTabDropDownList();
    }
  }

  private void setTabAreaComponentsButtonsVisible() {
    if (scrollButtonBox != null) {
      boolean visible = false;
      if (!tabAreaOrientation.isHorizontal())
        visible = draggableComponentBox.getInnerSize().getWidth() > calcScrollWidth();
      else
        visible = draggableComponentBox.getInnerSize().getHeight() > calcScrollHeight();
      scrollButtonBox.setVisible(visible);

      if (dropDownList != null && listVisiblePolicy == TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE)
        dropDownList.setVisible(visible);

      if (!visible) {
        scrollButtonBox.setButton1Enabled(false);
        scrollButtonBox.setButton2Enabled(true);
      }
    }
    else {
      if (dropDownList != null && listVisiblePolicy == TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE)
        dropDownList.setVisible(false);
    }

    tabAreaComponentsPanel.setVisible(ComponentUtil.hasVisibleChildren(tabAreaComponentsPanel));
  }

  private int calcScrollWidth() {
    Insets componentsPanelInsets = tabAreaComponentsPanel.getInsets();
    boolean includeDropDownWidth = dropDownList != null && listVisiblePolicy == TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE;
    boolean componentsVisible = includeDropDownWidth
                                ? ComponentUtil.isOnlyVisibleComponents(new Component[]{scrollButtonBox, dropDownList})
                                : ComponentUtil.isOnlyVisibleComponent(scrollButtonBox);
    int insetsWidth = tabAreaComponentsPanel.isVisible() && componentsVisible ?
                      componentsPanelInsets.left + componentsPanelInsets.right : 0;
    int componentsPanelWidth = tabAreaComponentsPanel.isVisible() ?
                               ((int) tabAreaComponentsPanel.getPreferredSize().getWidth() - insetsWidth - (scrollButtonBox.isVisible()
                                                                                                            ?
                                                                                                            scrollButtonBox.getWidth() +
                                                                                                            (includeDropDownWidth ?
                                                                                                             dropDownList.getWidth() :
                                                                                                             0)
                                                                                                            :
                                                                                                            0)) :
                               0;
    Insets areaInsets = tabAreaContainer.getInsets();
    return tabAreaContainer.getWidth() - componentsPanelWidth - areaInsets.left - areaInsets.right;
  }

  private int calcScrollHeight() {
    Insets componentsPanelInsets = tabAreaComponentsPanel.getInsets();
    boolean includeDropDownHeight = listVisiblePolicy == TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE;
    boolean componentsVisible = includeDropDownHeight
                                ? ComponentUtil.isOnlyVisibleComponents(new Component[]{scrollButtonBox, dropDownList})
                                : ComponentUtil.isOnlyVisibleComponent(scrollButtonBox);
    int insetsHeight = tabAreaComponentsPanel.isVisible() && componentsVisible ?
                       componentsPanelInsets.top + componentsPanelInsets.bottom : 0;
    int componentsPanelHeight = tabAreaComponentsPanel.isVisible() ?
                                ((int) tabAreaComponentsPanel.getPreferredSize().getHeight() - insetsHeight - (scrollButtonBox.isVisible()
                                                                                                               ?
                                                                                                               scrollButtonBox.getHeight() +
                                                                                                               (includeDropDownHeight ?
                                                                                                                dropDownList.getHeight() :
                                                                                                                0)
                                                                                                               :
                                                                                                               0)) :
                                0;
    Insets areaInsets = tabAreaContainer.getInsets();
    return tabAreaContainer.getHeight() - componentsPanelHeight - areaInsets.top - areaInsets.bottom;
  }

  private void updateScrollButtons() {
    ScrollButtonBox oldScrollButtonBox = scrollButtonBox;
    scrollButtonBox = draggableComponentBox.getScrollButtonBox();
    if (oldScrollButtonBox != scrollButtonBox) {
      if (oldScrollButtonBox != null) {
        tabAreaComponentsPanel.remove(oldScrollButtonBox);
      }

      if (scrollButtonBox != null) {
        scrollButtonBox.setButtons(properties.getButtonProperties().getScrollUpButtonProperties().applyTo(
            properties.getButtonProperties().getScrollUpButtonProperties().getFactory().createButton(this)),
                                   properties.getButtonProperties().getScrollDownButtonProperties().applyTo(
                                       properties.getButtonProperties().getScrollDownButtonProperties().getFactory()
                                       .createButton(this)),
                                   properties.getButtonProperties().getScrollLeftButtonProperties().applyTo(
                                       properties.getButtonProperties().getScrollLeftButtonProperties().getFactory()
                                       .createButton(this)),
                                   properties.getButtonProperties().getScrollRightButtonProperties().applyTo(
                                       properties.getButtonProperties().getScrollRightButtonProperties().getFactory()
                                       .createButton(this)));
        scrollButtonBox.setVisible(false);
        tabAreaComponentsPanel.add(scrollButtonBox, 0);
      }

      tabAreaComponentsPanel.revalidate();
    }
  }

  private void fireTabMoved(Tab tab) {
    if (listeners != null) {
      TabEvent event = new TabEvent(this, tab);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabMoved(event);
    }
  }

  private void fireDraggedEvent(Tab tab, MouseEvent mouseEvent) {
    if (listeners != null) {
      TabDragEvent event = new TabDragEvent(this, EventUtil.convert(mouseEvent, tab));
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDragged(event);
    }
  }

  private void fireDroppedEvent(Tab tab, MouseEvent mouseEvent) {
    if (listeners != null) {
      TabDragEvent event = new TabDragEvent(this, EventUtil.convert(mouseEvent, tab));
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDropped(event);
    }
  }

  private void fireNotDroppedEvent(Tab tab) {
    if (listeners != null) {
      TabEvent event = new TabEvent(this, tab);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDragAborted(event);
    }
  }

  private void fireSelectedEvent(Tab tab, Tab oldTab) {
    if (listeners != null) {
      {
        TabStateChangedEvent event = new TabStateChangedEvent(this, this, oldTab, oldTab, tab);
        Object[] l = listeners.toArray();
        for (int i = 0; i < l.length; i++)
          ((TabListener) l[i]).tabDeselected(event);
      }
      {
        TabStateChangedEvent event = new TabStateChangedEvent(this, this, tab, oldTab, tab);
        Object[] l = listeners.toArray();
        for (int i = 0; i < l.length; i++)
          ((TabListener) l[i]).tabSelected(event);
      }
    }
  }

  private void fireHighlightedEvent(Tab tab, Tab oldTab) {
    if (listeners != null) {
      {
        TabStateChangedEvent event = new TabStateChangedEvent(this, this, oldTab, oldTab, tab);
        Object[] l = listeners.toArray();
        for (int i = 0; i < l.length; i++)
          ((TabListener) l[i]).tabDehighlighted(event);
      }
      {
        TabStateChangedEvent event = new TabStateChangedEvent(this, this, tab, oldTab, tab);
        Object[] l = listeners.toArray();
        for (int i = 0; i < l.length; i++)
          ((TabListener) l[i]).tabHighlighted(event);
      }
    }
  }

  private void fireAddedEvent(Tab tab) {
    if (listeners != null) {
      TabEvent event = new TabEvent(this, tab);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabAdded(event);
    }
  }

  private void fireRemovedEvent(Tab tab) {
    if (listeners != null) {
      TabRemovedEvent event = new TabRemovedEvent(this, tab, this);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabRemoved(event);
    }
  }

  protected void processMouseEvent(MouseEvent event) {
    if (event.getID() == MouseEvent.MOUSE_ENTERED) {
      if (!mouseEntered) {
        mouseEntered = true;
        super.processMouseEvent(event);
      }
    }
    else if (event.getID() == MouseEvent.MOUSE_EXITED) {
      if (!contains(event.getPoint())) {
        mouseEntered = false;
        super.processMouseEvent(event);
      }
    }
    else
      super.processMouseEvent(event);
  }

  void doProcessMouseEvent(MouseEvent event) {
    processMouseEvent(SwingUtilities.convertMouseEvent((Component) event.getSource(), event, this));
  }

  void doProcessMouseMotionEvent(MouseEvent event) {
    processMouseMotionEvent(SwingUtilities.convertMouseEvent((Component) event.getSource(), event, this));
  }

  private void updateShadow() {
    if (shadowRepaintChecker.isPaintingOk() && contentPanel != null && properties.getShadowEnabled()) {
      Point p = SwingUtilities.convertPoint(tabAreaContainer, new Point(0, 0), this);
      repaint(p.x, p.y, tabAreaContainer.getWidth() + shadowSize, tabAreaContainer.getHeight() + shadowSize);
    }
  }

  private void updatePanelOpaque() {
    if (!properties.getShadowEnabled()
        && properties.getTabAreaProperties().getShapedPanelProperties()
        .getOpaque()
        && (contentPanel == null ? true : properties
        .getContentPanelProperties().getShapedPanelProperties().getOpaque())) {
      BaseContainerUtil.setForcedOpaque(componentsPanel, true);
      setOpaque(true);
    }
    else {
      BaseContainerUtil.setForcedOpaque(componentsPanel, false);
      setOpaque(false);
    }
  }

  private class ShadowPanel extends HoverablePanel {
    ShadowPanel() {
      super(new BorderLayout(), properties.getHoverListener());
      setCursor(null);
    }

    public boolean contains(int x, int y) {
      return properties.getShadowEnabled() ? doContains(x, y) : super.contains(x, y);
    }

    public boolean inside(int x, int y) {
      return properties.getShadowEnabled() ? doContains(x, y) : super.inside(x, y);
    }

    private boolean doContains(int x, int y) {
      Dimension d = DimensionUtil.getInnerDimension(getSize(), getInsets());
      return x >= 0 && y >= 0 && x < d.getWidth() && y < d.getHeight();
    }

    public void paint(Graphics g) {
      super.paint(g);

      if (contentPanel == null || !properties.getShadowEnabled())
        return;

      new ShadowPainter(this,
                        componentsPanel,
                        highlightedTab,
                        contentPanel,
                        tabAreaComponentsPanel,
                        tabAreaContainer,
                        draggableComponentBox,
                        properties.getTabAreaOrientation(),
                        properties.getPaintTabAreaShadow(),
                        shadowSize,
                        properties.getShadowBlendAreaSize(),
                        properties.getShadowColor(),
                        properties.getShadowStrength(),
                        getTabIndex(getHighlightedTab()) == getTabCount() - 1).paint(g);
    }
  }
}