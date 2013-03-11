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


// $Id: Tab.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel;

import java.awt.BorderLayout;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.infonode.gui.draggable.DraggableComponent;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.util.Direction;

/**
 * <p>A Tab is a component that represents a tab in a {@link TabbedPanel}.</p>
 *
 * <p>A tab can hold a content component. The content component will then be shown in
 * the content area of the TabbedPanel that the tab is a member of when the tab is
 * selected. If the tab doesn't have a content component, then the TabbedPanel will not
 * show any content in the content area, i.e. it will be empty.</p>
 *
 * <p>The tab is basically a JPanel with a BorderLayout. The layout manager can be
 * changed using setLayout. Components and borders can be added and removed from the
 * tab. The tab can also be subclassed to create other types of tabs, see
 * {@link TitledTab}. <strong>In most cases {@link TitledTab} is the preferred tab type
 * to use because TitledTab adds support for a text, icon, looks etc.</strong></p>
 *
 * <p>The tab component will be shown in the tab area of a TabbedPanel
 * after the tab has become a member of that TabbedPanel by either adding or inserting
 * it. A tab can only be a member of one TabbedPanel at the same time.</p>
 *
 * <p>A tab can have different states when it is a member of a TabbedPanel:
 * <ul>
 * <li>Normal: This means that the tab is shown (and not selected) in the TabbedPanel. The
 * content component is not shown until the user selects the tab.
 * <li>Highlighted: This means that for some reason the tab should be highlighted in
 * the TabbedPanel. Highlighted could mean that the user pressed the tab with the mouse
 * and has not yet released the mouse, i.e. it has not been selected yet.
 * <li>Selected: This means that the tab is selected in the TabbedPanel. The TabbedPanel
 * will then show the Tab's content component (if any). A selected tab will also be
 * be highlighted before it is selected.
 * <li>Enabled: This means that the tab is enabled and can be selected, highlighted
 * dragged, moved etc.
 * <li>Disabled: This means that the tab cannot be selected, highlighted
 * dragged, moved etc.
 * </ul></p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TabListener
 * @see TabbedPanel
 * @see TitledTab
 */
public class Tab extends JPanel {
  private TabbedPanel tabbedPanel;
  private JComponent contentComponent;
  private JComponent focusableComponent;
  private ArrayList listeners;
  private DraggableComponent draggableComponent;

  private KeyListener focusableKeyListener = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      if (tabbedPanel != null) {
        Direction tabOrientation = tabbedPanel.getProperties().getTabAreaOrientation();
        int incKey = tabOrientation.isHorizontal() ? KeyEvent.VK_DOWN : KeyEvent.VK_RIGHT;
        int decKey = tabOrientation.isHorizontal() ? KeyEvent.VK_UP : KeyEvent.VK_LEFT;
        int index = tabbedPanel.getTabIndex(Tab.this);
        while (true) {
          index = (index + tabbedPanel.getTabCount() + (e.getKeyCode() == incKey ?
                                                        1 : e.getKeyCode() == decKey ? -1 : 0)) %
                  tabbedPanel.getTabCount();
          Tab tab = tabbedPanel.getTabAt(index);
          if (tab == Tab.this)
            return;

          if (tab.getFocusableComponent() != null) {
            /*tab.getFocusableComponent().setFocusable(true);
            tab.getFocusableComponent().requestFocusInWindow();*/
            tab.setSelected(true);
            break;
          }
        }
      }
    }
  };

  private TabListener tabbedPanelListener = new TabListener() {
    public void tabAdded(TabEvent event) {
      if (event.getTab() == Tab.this)
        fireAddedEvent();
    }

    public void tabRemoved(TabRemovedEvent event) {
      if (event.getTab() == Tab.this) {
        event.getTabbedPanel().removeTabListener(this);
        fireRemovedEvent(event);
      }
    }

    public void tabMoved(TabEvent event) {
      if (event.getTab() == Tab.this)
        fireMovedEvent();
    }

    public void tabDragged(TabDragEvent event) {
      if (event.getTab() == Tab.this)
        fireDraggedEvent(event);
    }

    public void tabDropped(TabDragEvent event) {
      if (event.getTab() == Tab.this)
        fireDroppedEvent(event);
    }

    public void tabDragAborted(TabEvent event) {
      if (event.getTab() == Tab.this)
        fireNotDroppedEvent();
    }

    public void tabSelected(TabStateChangedEvent event) {
      if (event.getTab() == Tab.this) {
        Tab tab = event.getPreviousTab();
        boolean hasFocus = tab != null && tab.getFocusableComponent() != null && tab.getFocusableComponent().hasFocus();

        if (tab != null && tab.getFocusableComponent() != null)
          tab.getFocusableComponent().setFocusable(false);

        if (focusableComponent != null) {
          focusableComponent.setFocusable(true);

          if (hasFocus)
            focusableComponent.requestFocusInWindow();
        }

        fireSelectedEvent(event);
      }
    }

    public void tabDeselected(TabStateChangedEvent event) {
      if (event.getTab() == Tab.this) {
        fireDeselectedEvent(event);
      }
    }

    public void tabHighlighted(TabStateChangedEvent event) {
      if (event.getTab() == Tab.this)
        fireHighlightedEvent(event);
    }

    public void tabDehighlighted(TabStateChangedEvent event) {
      if (event.getPreviousTab() == Tab.this)
        fireDehighlightedEvent(event);
    }
  };

  /**
   * Constructs a tab without a content component and this tab as event
   * component
   *
   * @see #setEventComponent
   */
  public Tab() {
    this(null);
  }

  /**
   * Constructs a tab with a content component and this tab as event
   * component
   *
   * @param contentComponent content component for this tab or null for
   *                         no content component.
   * @see #setEventComponent
   */
  public Tab(JComponent contentComponent) {
    super(new BorderLayout());
    setOpaque(false);
    this.contentComponent = contentComponent;
    draggableComponent = new DraggableComponent(this);
  }

  /**
   * Adds a TabListener
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
   * Gets the content component
   *
   * @return the content component for this tab or null if this Tab
   *         doesn't have a content component
   */
  public JComponent getContentComponent() {
    return contentComponent;
  }

  /**
   * Gets the TabbedPanel that this tab is a member of
   *
   * @return the TabbedPanel or null if this tab is not a member of
   *         any TabbedPanel
   */
  public TabbedPanel getTabbedPanel() {
    return tabbedPanel;
  }

  /**
   * <p>Enable or disable this tab.</p>
   *
   * <p>If the tab is disabled, then the tab will not signal any events
   * until it is enabled again.</p>
   *
   * @param enabled true for enabled, otherwise false
   */
  public void setEnabled(boolean enabled) {
    getDraggableComponent().setEnabled(enabled);
    super.setEnabled(enabled);
  }

  /**
   * <p>Selects this tab. A tab can only have the selected state if it is a
   * member of a TabbedPanel.</p>
   *
   * <p>Setting selected to true means that this tab will be the selected
   * tab in the TabbedPanel it is a member of. If this tab is the selected
   * tab in the TabbedPanel then setting selected to false means there will
   * be no selected tab in the TabbedPanel until another tab is selected.</p>
   *
   * @param selected True for selected, otherwise false
   */
  public void setSelected(boolean selected) {
    if (selected)
      draggableComponent.select();
    else if (tabbedPanel != null && tabbedPanel.getSelectedTab() == this)
      tabbedPanel.setSelectedTab(null);
  }

  /**
   * Returns if this tab is selected in the TabbedPanel that it is a member of.
   *
   * @return true if selected, false if not selected or this tab is not member
   *         of a TabbedPanel
   */
  public boolean isSelected() {
    return tabbedPanel != null ? tabbedPanel.getSelectedTab() == this : false;
  }

  /**
   * Highlights this tab. This tab will be the highlighted tab in the TabbedPanel
   * that it is member of.
   *
   * @param highlighted true for highlight, otherwise false
   */
  public void setHighlighted(boolean highlighted) {
    if (tabbedPanel != null) {
      if (highlighted)
        tabbedPanel.setHighlightedTab(this);
      else if (tabbedPanel.getHighlightedTab() == this)
        tabbedPanel.setHighlightedTab(null);
    }
  }

  /**
   * Returns if this tab is highlighted in the TabbedPanel that it is a member of.
   *
   * @return true if highlighted, false if not highlighted or this tab is not member
   *         of a TabbedPanel
   */
  public boolean isHighlighted() {
    return tabbedPanel != null ? tabbedPanel.getHighlightedTab() == this : false;
  }

  /**
   * <p>Sets the event component. An event component is a component in the tab that
   * is used for internal listening to mouse events on the tab.</p>
   *
   * <p><strong>Note:</strong> The event component must be part of this Tab</p>
   *
   * @param eventComponent a component in this tab that should be used for mouse
   *                       event listening
   */
  public void setEventComponent(JComponent eventComponent) {
    setEventComponents(new JComponent[]{eventComponent});
  }

  /**
   * <p>Sets a list of event components. An event component is a component in the
   * tab that is used for internal listening to mouse events on the tab. This
   * method makes it possible to use several components in the tab as event
   * components.</p>
   *
   * <p><strong>Note:</strong> The event components must be part of this Tab</p>
   *
   * @param eventComponents a list of components in this tab that should be used for
   *                        mouse event listening
   */
  public void setEventComponents(JComponent[] eventComponents) {
    draggableComponent.setEventComponents(eventComponents);
  }

  /**
   * Gets the event components for this Tab
   *
   * @return a list of all event components for this tab
   */
  public JComponent[] getEventComponents() {
    return draggableComponent.getEventComponents();
  }

  /**
   * Gets the index of this tab in the TabbedPanel.
   *
   * @return the tab index, -1 if this tab is not a member of a TabbedPanel.
   */
  public int getIndex() {
    return tabbedPanel == null ? -1 : tabbedPanel.getTabIndex(this);
  }

  /**
   * Gets the component in this tab that is focusable
   *
   * @return focusable component or null if this tab doesn't have any focusable
   *         component
   */
  public JComponent getFocusableComponent() {
    return focusableComponent;
  }

  /**
   * <p>Sets the component in this tab that represents the focusable part of the
   * tab.</p>
   *
   * <p><strong>Note:</strong> The focusable component must be part of this Tab</p>
   *
   * @param focusableComponent a component in this tab or null if no component
   *                           should be focusable
   */
  public void setFocusableComponent(JComponent focusableComponent) {
    if (this.focusableComponent != focusableComponent) {
      boolean hasFocus = false;

      if (this.focusableComponent != null) {
        this.focusableComponent.removeKeyListener(focusableKeyListener);
        hasFocus = this.focusableComponent.hasFocus();
      }

      this.focusableComponent = focusableComponent;
      if (this.focusableComponent != null) {
        this.focusableComponent.setFocusable(isSelected());
        this.focusableComponent.addKeyListener(focusableKeyListener);
        if (hasFocus)
          this.focusableComponent.requestFocusInWindow();
      }
    }
  }

  /**
   * <p>Gets the tab {@link Shape}.</p>
   *
   * <p>
   * This returns the shape of the tab. This can be be used by for
   * example content borders in the tabbed panel so they can skip a gap where the
   * tab intersects the tabbed panel content area.
   * </p>
   *
   * @return the tab {@link Shape}, null if the tab has the normal component rectangle shape
   * @since ITP 1.2.0
   */
  public Shape getShape() {
    return null;
  }

  /**
   * Called by the tabbed panel when the tab becomes a member or is no longer a member of the
   * tabbed panel
   *
   * @param tabbedPanel tabbed panel that this tab is a member of or null if this tab is no
   *                    longer a member o a tabbed panel
   */
  protected void setTabbedPanel(TabbedPanel tabbedPanel) {
    this.tabbedPanel = tabbedPanel;
    if (this.tabbedPanel != null)
      this.tabbedPanel.addTabListener(tabbedPanelListener);
  }

  DraggableComponent getDraggableComponent() {
    return draggableComponent;
  }

  private void fireHighlightedEvent(TabStateChangedEvent event) {
    if (listeners != null) {
      TabStateChangedEvent e = new TabStateChangedEvent(this,
                                                        event.getTabbedPanel(),
                                                        this,
                                                        event.getPreviousTab(),
                                                        event.getCurrentTab());
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabHighlighted(e);
    }
  }

  private void fireDehighlightedEvent(TabStateChangedEvent event) {
    if (listeners != null) {
      TabStateChangedEvent e = new TabStateChangedEvent(this,
                                                        event.getTabbedPanel(),
                                                        this,
                                                        event.getPreviousTab(),
                                                        event.getCurrentTab());
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDehighlighted(e);
    }
  }

  private void fireSelectedEvent(TabStateChangedEvent event) {
    if (listeners != null) {
      TabStateChangedEvent e = new TabStateChangedEvent(this,
                                                        event.getTabbedPanel(),
                                                        this,
                                                        event.getPreviousTab(),
                                                        event.getCurrentTab());
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabSelected(e);
    }
  }

  private void fireDeselectedEvent(TabStateChangedEvent event) {
    if (listeners != null) {
      TabStateChangedEvent e = new TabStateChangedEvent(this,
                                                        event.getTabbedPanel(),
                                                        this,
                                                        event.getPreviousTab(),
                                                        event.getCurrentTab());
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDeselected(e);
    }
  }

  private void fireDraggedEvent(TabDragEvent event) {
    if (listeners != null) {
      TabDragEvent e = new TabDragEvent(this, event.getMouseEvent());
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDragged(e);
    }
  }

  private void fireDroppedEvent(TabDragEvent event) {
    if (listeners != null) {
      TabDragEvent e = new TabDragEvent(this, this, event.getPoint());
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDropped(e);
    }
  }

  private void fireNotDroppedEvent() {
    if (listeners != null) {
      TabEvent e = new TabEvent(this, this);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabDragAborted(e);
    }
  }

  private void fireMovedEvent() {
    if (listeners != null) {
      TabEvent e = new TabEvent(this, this);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabMoved(e);
    }
  }

  private void fireAddedEvent() {
    if (listeners != null) {
      TabEvent e = new TabEvent(this, this);
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabAdded(e);
    }
  }

  private void fireRemovedEvent(TabRemovedEvent event) {
    if (listeners != null) {
      TabRemovedEvent e = new TabRemovedEvent(this, this, event.getTabbedPanel());
      Object[] l = listeners.toArray();
      for (int i = 0; i < l.length; i++)
        ((TabListener) l[i]).tabRemoved(e);
    }
  }

  public void addNotify() {
    if (!draggableComponent.isIgnoreAddNotify())
      super.addNotify();
  }

  public void removeNotify() {
    if (!draggableComponent.isIgnoreAddNotify())
      super.removeNotify();
  }
}
