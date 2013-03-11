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


// $Id: TitledTab.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.tabbedpanel.titledtab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import net.infonode.gui.DimensionProvider;
import net.infonode.gui.InsetsUtil;
import net.infonode.gui.RotatableLabel;
import net.infonode.gui.TranslatingShape;
import net.infonode.gui.border.FocusBorder;
import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.gui.hover.hoverable.HoverManager;
import net.infonode.gui.hover.hoverable.Hoverable;
import net.infonode.gui.icon.IconProvider;
import net.infonode.gui.layout.StackableLayout;
import net.infonode.gui.panel.SimplePanel;
import net.infonode.gui.shaped.panel.ShapedPanel;
import net.infonode.properties.base.Property;
import net.infonode.properties.gui.InternalPropertiesUtil;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;
import net.infonode.properties.util.PropertyChangeListener;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabEvent;
import net.infonode.tabbedpanel.TabRemovedEvent;
import net.infonode.tabbedpanel.TabSelectTrigger;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.util.Alignment;
import net.infonode.util.Direction;
import net.infonode.util.ValueChange;

/**
 * <p>A TitledTab is a tab that has support for text, icon and a custom Swing component
 * (called title component). Titled tab supports several properties that makes it possible
 * to change the look (borders, colors, insets), layout (up, down, left, right).</p>
 *
 * <p>Titled tab has a line based layout, i.e. the text, icon and title component are
 * laid out in a line. The layout of the tab can be rotated, i.e. the text and the icon will
 * be rotated 90, 180 or 270 degrees. The title component will not be rotated but moved so
 * that the line layout will persist.</p>
 *
 * <p>A titled tab has 3 rendering states:
 * <ul>
 * <li>Normal - The tab is selectable but not yet selected
 * <li>Highlighted - The tab is either highlighted or selected
 * <li>Disabled - The tab is disabled and cannot be selected or highlighted
 * </ul>Most of the properties for the tab can be configured for each of the tab rendering
 * states.</p>
 *
 * <p><strong>Note:</strong> If only the normal state properties have been configured, the
 * highlighted and disabled state will automatically use the same properties as for the normal
 * state, see {@link TitledTabProperties} and {@link TitledTabStateProperties}.</p>
 *
 * <p>TitledTab implements the {@link net.infonode.gui.icon.IconProvider} interface and
 * overloads toString() so that both text and icon for the normal state is shown in the
 * tab drop down list in a tabbed panel.</p>
 *
 * <p>TitledTab supports mouse hovering. A {@link HoverListener} can be set in the
 * {@link TitledTabProperties}. The hover listener receives a {@link HoverEvent} when the mouse
 * enters or exits the tab. The hover event's source will be the affected titled tab.</p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TitledTabProperties
 * @see TitledTabStateProperties
 */
public class TitledTab extends Tab implements IconProvider {
  private static PanelUI UI = new PanelUI() {
  };

  private class StatePanel extends SimplePanel {
    private final ShapedPanel panel = new ShapedPanel();
    private final SimplePanel titleComponentPanel = new SimplePanel();
    private final RotatableLabel label = new RotatableLabel(null, null) {
      public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        String text = this.getText();
        Icon tmpIcon = this.getIcon();

        if (text == null || tmpIcon == null) {
          this.setText(" ");
          this.setIcon(icon);
          if (getDirection().isHorizontal())
            d = new Dimension(d.width, super.getPreferredSize().height);
          else
            d = new Dimension(super.getPreferredSize().width, d.height);

          this.setText(text);
          this.setIcon(tmpIcon);
        }

        return d;
      }
    };
    private JComponent titleComponent;
    private Direction currentLayoutDirection;
    private int currentLayoutGap = -1;
    private Alignment currentLayoutAlignment;
    private String toolTipText;
    private Icon icon;

    public StatePanel(Border focusBorder) {
      super(new BorderLayout());

      label.setBorder(focusBorder);
      label.setMinimumSize(new Dimension(0, 0));

      panel.add(label, BorderLayout.CENTER);
      add(panel, BorderLayout.CENTER);
    }

    public String getToolTipText() {
      return toolTipText;
    }

    public JComponent getTitleComponent() {
      return titleComponent;
    }

    public Shape getShape() {
      return panel.getShape();
    }

    public JLabel getLabel() {
      return label;
    }

    public void setTitleComponent(JComponent titleComponent, TitledTabStateProperties stateProps) {
      JComponent oldTitleComponent = this.titleComponent;
      this.titleComponent = null;
      if (oldTitleComponent != null && oldTitleComponent.getParent() == titleComponentPanel)
        titleComponentPanel.remove(oldTitleComponent);
      this.titleComponent = titleComponent;
      updateLayout(stateProps, true);
    }

    public void activateTitleComponent() {
      if (titleComponent != null) {
        if (titleComponent.getParent() != titleComponentPanel) {
          if (titleComponent.getParent() != null)
            titleComponent.getParent().remove(titleComponent);
          titleComponentPanel.add(titleComponent, BorderLayout.CENTER);
        }
      }
      else {
        titleComponentPanel.removeAll();
      }
    }

    public void activate() {
      remove(panel);
      eventPanel.add(panel, BorderLayout.CENTER);
      add(eventPanel, BorderLayout.CENTER);
    }

    public void deactivate() {
      remove(eventPanel);
      eventPanel.remove(panel);
      add(panel, BorderLayout.CENTER);
    }

    public Dimension getPreferredSize() {
      activateTitleComponent();

      return getAdjustedSize(super.getPreferredSize());
    }

    public Dimension getMinimumSize() {
      activateTitleComponent();

      return getAdjustedSize(super.getMinimumSize());
    }

    public Dimension getMaximumSize() {
      activateTitleComponent();
      return super.getMaximumSize();
    }

    private Dimension getAdjustedSize(Dimension d) {
      DimensionProvider prov = properties.getMinimumSizeProvider();
      if (prov == null)
        return d;

      Dimension min = properties.getMinimumSizeProvider().getDimension(this);

      if (min == null)
        return d;

      return new Dimension(Math.max(min.width, d.width), Math.max(min.height, d.height));
    }

    public JComponent getFocusableComponent() {
      return label;
    }

    private void updateLayout(TitledTabStateProperties stateProperties, boolean titleComponentChanged) {
      if (titleComponent != null && stateProperties.getTitleComponentVisible()) {
        Direction d = stateProperties.getDirection();
        int gap;
        if (stateProperties.getIconVisible() || stateProperties.getTextVisible())
          gap = stateProperties.getTextTitleComponentGap();
        else
          gap = 0;
        Alignment alignment = stateProperties.getTitleComponentTextRelativeAlignment();
        if (titleComponentPanel.getComponentCount() == 0 ||
            (titleComponentPanel.getComponentCount() > 0 && titleComponentPanel.getComponent(0) != titleComponent) ||
            titleComponentChanged ||
            gap != currentLayoutGap ||
            alignment != currentLayoutAlignment ||
            d != currentLayoutDirection) {
          titleComponentChanged = false;
          currentLayoutDirection = d;
          currentLayoutGap = gap;
          currentLayoutAlignment = alignment;

          panel.remove(titleComponentPanel);
          if (d == Direction.UP) {
            panel.add(titleComponentPanel, alignment == Alignment.LEFT ? BorderLayout.SOUTH : BorderLayout.NORTH);
            titleComponentPanel.setBorder(
                new EmptyBorder(alignment == Alignment.LEFT ? gap : 0, 0, alignment == Alignment.LEFT ? 0 : gap, 0));
          }
          else if (d == Direction.LEFT) {
            panel.add(titleComponentPanel, alignment == Alignment.LEFT ? BorderLayout.EAST : BorderLayout.WEST);
            titleComponentPanel.setBorder(
                new EmptyBorder(0, alignment == Alignment.LEFT ? gap : 0, 0, alignment == Alignment.LEFT ? 0 : gap));
          }
          else if (d == Direction.DOWN) {
            panel.add(titleComponentPanel, alignment == Alignment.LEFT ? BorderLayout.NORTH : BorderLayout.SOUTH);
            titleComponentPanel.setBorder(
                new EmptyBorder(alignment == Alignment.LEFT ? 0 : gap, 0, alignment == Alignment.LEFT ? gap : 0, 0));
          }
          else {
            panel.add(titleComponentPanel, alignment == Alignment.LEFT ? BorderLayout.WEST : BorderLayout.EAST);
            titleComponentPanel.setBorder(
                new EmptyBorder(0, alignment == Alignment.LEFT ? 0 : gap, 0, alignment == Alignment.LEFT ? gap : 0));
          }

          panel.revalidate();
        }
      }
      else {
        panel.remove(titleComponentPanel);
        titleComponentPanel.removeAll();

        panel.revalidate();
      }
    }

    public void updateShapedPanel(TitledTabStateProperties stateProperties) {
      Direction tabAreaOrientation = getTabAreaOrientation();
      ShapedPanelProperties shapedPanelProperties = stateProperties.getShapedPanelProperties();
      InternalPropertiesUtil.applyTo(shapedPanelProperties, panel, tabAreaOrientation.getNextCW());
      panel
      .setHorizontalFlip(tabAreaOrientation == Direction.DOWN || tabAreaOrientation == Direction.LEFT ? !shapedPanelProperties
                                                                                                      .getHorizontalFlip()
                                                                                                      : shapedPanelProperties.getHorizontalFlip());
    }

    public void setBorders(Border outerBorder, Border innerBorder) {
      setBorder(outerBorder);
      panel.setBorder(innerBorder);
    }

    public boolean updateState(Map changes, TitledTabStateProperties stateProperties) {
      boolean updateBorders = false;

      if (changes == null) {
        label.setText(stateProperties.getTextVisible() ? stateProperties.getText() : null);

        icon = stateProperties.getIcon();
        label.setIcon(stateProperties.getIconVisible() ? stateProperties.getIcon() : null);

        label.setIconTextGap(stateProperties.getIconTextGap());

        label.setDirection(stateProperties.getDirection());

        Alignment alignment = stateProperties.getIconTextRelativeAlignment();
        label.setHorizontalTextPosition(alignment == Alignment.LEFT ? JLabel.RIGHT :
          JLabel.LEFT);

        alignment = stateProperties.getHorizontalAlignment();
        label.setHorizontalAlignment(alignment == Alignment.LEFT ? JLabel.LEFT :
          alignment == Alignment.CENTER ? JLabel.CENTER :
            JLabel.RIGHT);

        alignment = stateProperties.getVerticalAlignment();
        label.setVerticalAlignment(alignment == Alignment.TOP ? JLabel.TOP :
          alignment == Alignment.CENTER ? JLabel.CENTER :
            JLabel.BOTTOM);

        toolTipText = stateProperties.getToolTipEnabled() ? stateProperties.getToolTipText() : null;
        if (toolTipText != null && toolTipText.length() == 0)
          toolTipText = null;
        if (currentStatePanel == this)
          eventPanel.setToolTipText(toolTipText);

        updateLayout(stateProperties, true);

        ComponentProperties componentProperties = stateProperties.getComponentProperties();
        label.setFont(componentProperties.getFont());

        Color c = componentProperties.getForegroundColor();
        label.setForeground(c);
        setForeground(c);

        updateShapedPanel(stateProperties);

        updateBorders = true;
      }
      else {
        Map m = (Map) changes.get(stateProperties.getMap());
        if (m != null) {
          Set keySet = m.keySet();

          if (keySet.contains(TitledTabStateProperties.TEXT) || keySet.contains(TitledTabStateProperties.TEXT_VISIBLE)) {
            label.setText(stateProperties.getTextVisible() ? stateProperties.getText() : null);
          }

          if (keySet.contains(TitledTabStateProperties.ICON) || keySet.contains(TitledTabStateProperties.ICON_VISIBLE)) {
            icon = stateProperties.getIcon();
            label.setIcon(stateProperties.getIconVisible() ? stateProperties.getIcon() : null);
          }

          if (keySet.contains(TitledTabStateProperties.ICON_TEXT_GAP)) {
            label.setIconTextGap(
                ((Integer) ((ValueChange) m.get(TitledTabStateProperties.ICON_TEXT_GAP)).getNewValue()).intValue());
          }

          if (keySet.contains(TitledTabStateProperties.ICON_TEXT_RELATIVE_ALIGNMENT)) {
            Alignment alignment = (Alignment) ((ValueChange) m.get(
                TitledTabStateProperties.ICON_TEXT_RELATIVE_ALIGNMENT)).getNewValue();
            label.setHorizontalTextPosition(alignment == Alignment.LEFT ? JLabel.RIGHT : JLabel.LEFT);
          }

          if (keySet.contains(TitledTabStateProperties.HORIZONTAL_ALIGNMENT)) {
            Alignment alignment = (Alignment) ((ValueChange) m.get(TitledTabStateProperties.HORIZONTAL_ALIGNMENT)).getNewValue();
            label.setHorizontalAlignment(
                alignment == Alignment.LEFT ?
                                             JLabel.LEFT : alignment == Alignment.CENTER ? JLabel.CENTER : JLabel.RIGHT);
          }

          if (keySet.contains(TitledTabStateProperties.VERTICAL_ALIGNMENT)) {
            Alignment alignment = (Alignment) ((ValueChange) m.get(TitledTabStateProperties.VERTICAL_ALIGNMENT)).getNewValue();
            label.setVerticalAlignment(
                alignment == Alignment.TOP ?
                                            JLabel.TOP : alignment == Alignment.CENTER ? JLabel.CENTER : JLabel.BOTTOM);
          }

          if (keySet.contains(TitledTabStateProperties.TOOL_TIP_TEXT) || keySet.contains(
              TitledTabStateProperties.TOOL_TIP_ENABLED)) {
            toolTipText = stateProperties.getToolTipEnabled() ? stateProperties.getToolTipText() : null;
            if (toolTipText != null && toolTipText.length() == 0)
              toolTipText = null;

            if (currentStatePanel == this)
              eventPanel.setToolTipText(toolTipText);
          }

          if (keySet.contains(TitledTabStateProperties.DIRECTION) || keySet.contains(
              TitledTabStateProperties.TITLE_COMPONENT_TEXT_RELATIVE_ALIGNMENT)
              || keySet.contains(TitledTabStateProperties.TITLE_COMPONENT_VISIBLE) || keySet.contains(
                  TitledTabStateProperties.TEXT_TITLE_COMPONENT_GAP)
                  || keySet.contains(TitledTabStateProperties.ICON_VISIBLE) || keySet.contains(
                      TitledTabStateProperties.TEXT_VISIBLE)) {
            label.setDirection(stateProperties.getDirection());

            updateLayout(stateProperties, keySet.contains(TitledTabStateProperties.TITLE_COMPONENT_VISIBLE));
          }

          if (keySet.contains(TitledTabStateProperties.DIRECTION)) {
            updateBorders = true;
          }
        }

        m = (Map) changes.get(stateProperties.getComponentProperties().getMap());
        if (m != null) {
          Set keySet = m.keySet();

          if (keySet.contains(ComponentProperties.FONT)) {
            label.setFont((Font) ((ValueChange) m.get(ComponentProperties.FONT)).getNewValue());
          }

          if (keySet.contains(ComponentProperties.FOREGROUND_COLOR)) {
            Color c = (Color) ((ValueChange) m.get(ComponentProperties.FOREGROUND_COLOR)).getNewValue();
            label.setForeground(c);
            setForeground(c);
          }

          if (keySet.contains(ComponentProperties.BACKGROUND_COLOR)) {
            Color c = (Color) ((ValueChange) m.get(ComponentProperties.BACKGROUND_COLOR)).getNewValue();
            panel.setBackground(c);
          }

          if (keySet.contains(ComponentProperties.INSETS) || keySet.contains(ComponentProperties.BORDER)) {
            updateBorders = true;
          }
        }

        m = (Map) changes.get(stateProperties.getShapedPanelProperties().getMap());
        if (m != null) {
          updateShapedPanel(stateProperties);
        }
      }

      return updateBorders;
    }
  }

  private final TitledTabProperties properties = TitledTabProperties.getDefaultProperties();

  private HoverListener hoverListener = properties.getHoverListener();

  private class HoverablePanel extends SimplePanel implements Hoverable {
    public HoverablePanel(LayoutManager l) {
      super(l);
    }

    public void hoverEnter() {
      if (hoverListener != null && getTabbedPanel() != null)
        hoverListener.mouseEntered(new HoverEvent(TitledTab.this));
    }

    public void hoverExit() {
      if (hoverListener != null)
        hoverListener.mouseExited(new HoverEvent(TitledTab.this));
    }

    public boolean acceptHover(ArrayList enterableHoverables) {
      return true;
    }
  }

  private final HoverablePanel eventPanel = new HoverablePanel(new BorderLayout()) {

    public boolean contains(int x, int y) {
      return getComponentCount() > 0 && getComponent(0).contains(x, y);
    }

    public boolean inside(int x, int y) {
      return getComponentCount() > 0 && getComponent(0).inside(x, y);
    }

  };

  public boolean contains(int x, int y) {
    Point p = SwingUtilities.convertPoint(this, new Point(x, y), eventPanel);
    return eventPanel.contains(p.x, p.y);
  }

  public boolean inside(int x, int y) {
    Point p = SwingUtilities.convertPoint(this, new Point(x, y), eventPanel);
    return eventPanel.inside(p.x, p.y);
  }

  private final StatePanel normalStatePanel;
  private final StatePanel highlightedStatePanel;
  private final StatePanel disabledStatePanel;

  private ArrayList mouseListeners;
  private ArrayList mouseMotionListeners;
  private final StackableLayout layout;
  private StatePanel currentStatePanel;
  private final FocusBorder focusBorder;

  private Direction lastTabAreaOrientation = Direction.UP;

  private final PropertyMapTreeListener propertiesListener = new PropertyMapTreeListener() {
    public void propertyValuesChanged(Map changes) {
      doUpdateTab(changes);
    }
  };

  private final PropertyChangeListener tabbedPanelPropertiesListener = new PropertyChangeListener() {
    public void propertyChanged(Property property, Object valueContainer, Object oldValue, Object newValue) {
      updateTabAreaOrientation((Direction) newValue);
    }
  };

  /*  private FocusListener focusListener = new FocusListener() {
    public void focusGained(FocusEvent e) {
      if (properties.getFocusable())
        repaint();
    }

    public void focusLost(FocusEvent e) {
      if (properties.getFocusable())
        repaint();
    }
  };*/

  /**
   * Constructs a TitledTab with a text, icon, content component and title component.
   *
   * @param text             text or null for no text. The text will be applied to the
   *                         normal state properties
   * @param icon             icon or null for no icon. The icon will be applied to the
   *                         normal state properties
   * @param contentComponent content component or null for no content component
   * @param titleComponent   title component or null for no title component. The title
   *                         component will be applied to all the states
   * @see net.infonode.tabbedpanel.TabFactory
   */
  public TitledTab(String text, Icon icon, JComponent contentComponent, JComponent titleComponent) {
    super(contentComponent);
    super.setOpaque(false);

    addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        repaint();
      }

      public void focusLost(FocusEvent e) {
        repaint();
      }
    });

    focusBorder = new FocusBorder(this);
    normalStatePanel = new StatePanel(focusBorder);
    highlightedStatePanel = new StatePanel(focusBorder);
    disabledStatePanel = new StatePanel(focusBorder);


    layout = new StackableLayout(this) {
      public void layoutContainer(Container parent) {
        super.layoutContainer(parent);
        StatePanel visibleStatePanel = (StatePanel) getVisibleComponent();
        visibleStatePanel.activateTitleComponent();
      }
    };

    setLayout(layout);

    add(normalStatePanel);
    add(highlightedStatePanel);
    add(disabledStatePanel);

    setText(text);
    setIcon(icon);
    setTitleComponent(titleComponent);

    eventPanel.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        updateFocus(TabSelectTrigger.MOUSE_PRESS);
      }

      public void mouseReleased(MouseEvent e) {
        updateFocus(TabSelectTrigger.MOUSE_RELEASE);
      }

      private void updateFocus(TabSelectTrigger trigger) {
        if (isEnabled() && properties.getFocusable() && getTabbedPanel() != null && getTabbedPanel().getProperties()
            .getTabSelectTrigger() == trigger) {
          Component focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

          if (focusedComponent instanceof TitledTab
              && ((TitledTab) focusedComponent).getTabbedPanel() == getTabbedPanel())
            requestFocusInWindow();
          else if (isSelected() || TabbedUtils.getParentTabbedPanel(focusedComponent) != getTabbedPanel())
            requestFocusInWindow();
        }
      }
    });

    setEventComponent(eventPanel);

    MouseListener mouseListener = new MouseListener() {
      public void mouseClicked(MouseEvent e) {
        if (mouseListeners != null) {
          MouseEvent event = convertMouseEvent(e);
          Object[] l = mouseListeners.toArray();
          for (int i = 0; i < l.length; i++)
            ((MouseListener) l[i]).mouseClicked(event);
        }
      }

      public void mousePressed(MouseEvent e) {
        if (mouseListeners != null) {
          MouseEvent event = convertMouseEvent(e);
          Object[] l = mouseListeners.toArray();
          for (int i = 0; i < l.length; i++)
            ((MouseListener) l[i]).mousePressed(event);
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (mouseListeners != null) {
          MouseEvent event = convertMouseEvent(e);
          Object[] l = mouseListeners.toArray();
          for (int i = 0; i < l.length; i++)
            ((MouseListener) l[i]).mouseReleased(event);
        }
      }

      public void mouseEntered(MouseEvent e) {
        if (mouseListeners != null) {
          MouseEvent event = convertMouseEvent(e);
          Object[] l = mouseListeners.toArray();
          for (int i = 0; i < l.length; i++)
            ((MouseListener) l[i]).mouseEntered(event);
        }
      }

      public void mouseExited(MouseEvent e) {
        if (mouseListeners != null) {
          MouseEvent event = convertMouseEvent(e);
          Object[] l = mouseListeners.toArray();
          for (int i = 0; i < l.length; i++)
            ((MouseListener) l[i]).mouseExited(event);
        }
      }
    };

    MouseMotionListener mouseMotionListener = new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        if (mouseMotionListeners != null) {
          MouseEvent event = convertMouseEvent(e);
          Object[] l = mouseMotionListeners.toArray();
          for (int i = 0; i < l.length; i++)
            ((MouseMotionListener) l[i]).mouseDragged(event);
        }
      }

      public void mouseMoved(MouseEvent e) {
        if (mouseMotionListeners != null) {
          MouseEvent event = convertMouseEvent(e);
          Object[] l = mouseMotionListeners.toArray();
          for (int i = 0; i < l.length; i++)
            ((MouseMotionListener) l[i]).mouseMoved(event);
        }
      }
    };

    eventPanel.addMouseListener(mouseListener);
    eventPanel.addMouseMotionListener(mouseMotionListener);

    PropertyMapWeakListenerManager.addWeakTreeListener(properties.getMap(), propertiesListener);

    addTabListener(new TabAdapter() {
      public void tabAdded(TabEvent event) {
        PropertyMapWeakListenerManager.addWeakPropertyChangeListener(getTabbedPanel().getProperties().getMap(),
            TabbedPanelProperties.TAB_AREA_ORIENTATION,
            tabbedPanelPropertiesListener);
        updateTabAreaOrientation(getTabbedPanel().getProperties().getTabAreaOrientation());
      }

      public void tabRemoved(TabRemovedEvent event) {
        PropertyMapWeakListenerManager.removeWeakPropertyChangeListener(
            event.getTabbedPanel().getProperties().getMap(),
            TabbedPanelProperties.TAB_AREA_ORIENTATION,
            tabbedPanelPropertiesListener);
      }
    });

    doUpdateTab(null);
    updateCurrentStatePanel();
  }

  /**
   * Gets the title component for the normal state
   *
   * @return title component or null if no title component
   */
  public JComponent getNormalStateTitleComponent() {
    return normalStatePanel.getTitleComponent();
  }

  /**
   * Gets the title component for the highlighted state
   *
   * @return title component or null if no title component
   */
  public JComponent getHighlightedStateTitleComponent() {
    return highlightedStatePanel.getTitleComponent();
  }

  /**
   * Gets the title component for the disabled state
   *
   * @return title component or null if no title component
   */
  public JComponent getDisabledStateTitleComponent() {
    return disabledStatePanel.getTitleComponent();
  }

  /**
   * <p>Sets the title component.</p>
   *
   * <p>This method is a convenience method for setting the same title component for
   * all states.</p>
   *
   * @param titleComponent the title component or null for no title component
   */
  public void setTitleComponent(JComponent titleComponent) {
    normalStatePanel.setTitleComponent(titleComponent, properties.getNormalProperties());
    highlightedStatePanel.setTitleComponent(titleComponent, properties.getHighlightedProperties());
    disabledStatePanel.setTitleComponent(titleComponent, properties.getDisabledProperties());
  }

  /**
   * Sets the normal state title component
   *
   * @param titleComponent the title component or null for no title component
   */
  public void setNormalStateTitleComponent(JComponent titleComponent) {
    normalStatePanel.setTitleComponent(titleComponent, properties.getNormalProperties());
  }

  /**
   * Sets the highlighted state title component
   *
   * @param titleComponent the title component or null for no title component
   */
  public void setHighlightedStateTitleComponent(JComponent titleComponent) {
    highlightedStatePanel.setTitleComponent(titleComponent, properties.getHighlightedProperties());
  }

  /**
   * Sets the disabled state title component
   *
   * @param titleComponent the title component or null for no title component
   */
  public void setDisabledStateTitleComponent(JComponent titleComponent) {
    disabledStatePanel.setTitleComponent(titleComponent, properties.getDisabledProperties());
  }

  /**
   * <p>Sets if this TitledTab should be highlighted or not.</p>
   *
   * <p><strong>Note:</strong> This will only have effect if this TitledTab
   * is enabled and a member of a tabbed panel.</p>
   *
   * @param highlighted true for highlight, otherwise false
   */
  public void setHighlighted(boolean highlighted) {
    super.setHighlighted(highlighted);
    updateCurrentStatePanel();
  }

  /**
   * <p>
   * Sets if this TitledTab should be enabled or disabled
   * </p>
   * 
   * <p>
   * <strong>Note:</strong> since ITP 1.5.0 this method will change the enabled property
   * in the {@link TitledTabProperties} for this tab. Enabled/disabled can be controlled by
   * modifying the property or this method.
   * </p>
   *
   * @param enabled true for enabled, otherwise false
   */
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    updateCurrentStatePanel();
  }

  /**
   * Gets the text for the normal state
   *
   * @return the text or null if no text
   */
  public String getText() {
    return properties.getNormalProperties().getText();
  }

  /**
   * Sets the text for the normal state
   *
   * @param text the text or null for no text
   */
  public void setText(String text) {
    properties.getNormalProperties().setText(text);
  }

  /**
   * Gets the icon for the normal state
   *
   * @return the icon or null if none
   */
  public Icon getIcon() {
    return properties.getNormalProperties().getIcon();
  }

  /**
   * Sets the icon for the normal state
   *
   * @param icon the icon or null for no icon
   */
  public void setIcon(Icon icon) {
    properties.getNormalProperties().setIcon(icon);
  }

  /**
   * Gets the TitledTabProperties
   *
   * @return the TitledTabProperties for this TitledTab
   */
  public TitledTabProperties getProperties() {
    return properties;
  }

  /**
   * Gets the text for the normal state.
   *
   * Same as getText().
   *
   * @return the text or null if no text
   * @see #getText
   * @since ITP 1.1.0
   */
  public String toString() {
    return getText();
  }

  /**
   * Adds a MouseListener to receive mouse events from this TitledTab.
   *
   * @param l the MouseListener
   */
  public synchronized void addMouseListener(MouseListener l) {
    if (mouseListeners == null)
      mouseListeners = new ArrayList(2);
    mouseListeners.add(l);
  }

  /**
   * Removes a MouseListener
   *
   * @param l the MouseListener to remove
   */
  public synchronized void removeMouseListener(MouseListener l) {
    if (mouseListeners != null) {
      mouseListeners.remove(l);

      if (mouseListeners.size() == 0)
        mouseListeners = null;
    }
  }

  /**
   * Gets the mouse listeners
   *
   * @return the mouse listeners
   */
  public synchronized MouseListener[] getMouseListeners() {
    MouseListener[] listeners = new MouseListener[0];

    if (mouseListeners != null) {
      Object[] l = mouseListeners.toArray();
      listeners = new MouseListener[l.length];
      for (int i = 0; i < l.length; i++)
        listeners[i] = (MouseListener) l[i];
    }

    return listeners;
  }

  /**
   * Adds a MouseMotionListener to receive mouse events from this TitledTab.
   *
   * @param l the MouseMotionListener
   */
  public synchronized void addMouseMotionListener(MouseMotionListener l) {
    if (mouseMotionListeners == null)
      mouseMotionListeners = new ArrayList(2);

    mouseMotionListeners.add(l);
  }

  /**
   * Removes a MouseMotionListener
   *
   * @param l the MouseMotionListener to remove
   */
  public synchronized void removeMouseMotionListener(MouseMotionListener l) {
    if (mouseMotionListeners != null) {
      mouseMotionListeners.remove(l);

      if (mouseMotionListeners.size() == 0)
        mouseMotionListeners = null;
    }
  }

  /**
   * Gets the mouse motion listeners
   *
   * @return the mouse motion listeners
   */
  public synchronized MouseMotionListener[] getMouseMotionListeners() {
    MouseMotionListener[] listeners = new MouseMotionListener[0];

    if (mouseMotionListeners != null) {
      Object[] l = mouseMotionListeners.toArray();
      listeners = new MouseMotionListener[l.length];
      for (int i = 0; i < l.length; i++)
        listeners[i] = (MouseMotionListener) l[i];
    }

    return listeners;
  }

  /**
   * Gets the Shape for the current active rendering state.
   *
   * @return the Shape for the active rendering state, null if no special shape
   * @since ITP 1.2.0
   */
  public Shape getShape() {
    Shape shape = currentStatePanel.getShape();

    if (shape == null)
      return null;

    Point p = SwingUtilities.convertPoint(currentStatePanel, 0, 0, this);
    return new TranslatingShape(shape, p.x, p.y);
  }

  protected void setTabbedPanel(TabbedPanel tabbedPanel) {
    if (tabbedPanel == null)
      HoverManager.getInstance().removeHoverable(eventPanel);

    super.setTabbedPanel(tabbedPanel);

    if (tabbedPanel != null)
      HoverManager.getInstance().addHoverable(eventPanel);
  }

  private Insets getBorderInsets(Border border) {
    return border == null ? InsetsUtil.EMPTY_INSETS : border.getBorderInsets(this);
  }

  private void updateBorders() {
    Direction tabAreaOrientation = getTabAreaOrientation();
    int raised = properties.getHighlightedRaised();
    Insets notRaised = InsetsUtil.setInset(InsetsUtil.EMPTY_INSETS, tabAreaOrientation, raised);
    Border normalBorder = new EmptyBorder(notRaised);

    Insets maxInsets = properties.getBorderSizePolicy() == TitledTabBorderSizePolicy.INDIVIDUAL_SIZE ?
                                                                                                      null :
                                                                                                        InsetsUtil.max(
                                                                                                            getBorderInsets(properties.getNormalProperties().getComponentProperties().getBorder()),
                                                                                                            InsetsUtil.max(getBorderInsets(
                                                                                                                properties.getHighlightedProperties().getComponentProperties().getBorder()),
                                                                                                                getBorderInsets(
                                                                                                                    properties.getDisabledProperties().getComponentProperties().getBorder())));

    Insets normalInsets = InsetsUtil.rotate(properties.getNormalProperties().getDirection(),
        properties.getNormalProperties().getComponentProperties().getInsets());

    Insets disabledInsets = InsetsUtil.rotate(properties.getDisabledProperties().getDirection(),
        properties.getDisabledProperties().getComponentProperties().getInsets());

    int edgeInset = Math.min(InsetsUtil.getInset(normalInsets,
        tabAreaOrientation.getOpposite()),
        InsetsUtil.getInset(disabledInsets,
            tabAreaOrientation.getOpposite()));

    int normalLowered = Math.min(edgeInset, raised);

    Border innerNormalBorder = getInnerBorder(properties.getNormalProperties(),
        tabAreaOrientation,
        -normalLowered,
        maxInsets);
    Border innerHighlightBorder = getInnerBorder(properties.getHighlightedProperties(),
        tabAreaOrientation,
        raised - normalLowered,
        maxInsets);
    Border innerDisabledBorder = getInnerBorder(properties.getDisabledProperties(),
        tabAreaOrientation,
        -normalLowered,
        maxInsets);

    normalStatePanel.setBorders(normalBorder, innerNormalBorder);
    highlightedStatePanel.setBorders(null, innerHighlightBorder);
    disabledStatePanel.setBorders(normalBorder, innerDisabledBorder);
  }

  private void doUpdateTab(Map changes) {
    boolean updateBorders = false;

    if (changes == null) {
      // Init all
      updateBorders = true;

      setFocusableComponent(properties.getFocusable() ? this : null);
      focusBorder.setEnabled(properties.getFocusMarkerEnabled());

      updateHoverListener(properties.getHoverListener());
      layout.setUseSelectedComponentSize(properties.getSizePolicy() == TitledTabSizePolicy.INDIVIDUAL_SIZE);
    }
    else {
      Map m = (Map) changes.get(properties.getMap());
      if (m != null) {
        Set keySet = m.keySet();

        if (keySet.contains(TitledTabProperties.FOCUSABLE)) {
          setFocusableComponent(properties.getFocusable() ? this : null);
        }

        if (keySet.contains(TitledTabProperties.FOCUS_MARKER_ENABLED)) {
          focusBorder.setEnabled(properties.getFocusMarkerEnabled());
          currentStatePanel.getLabel().repaint();
        }

        if (keySet.contains(TitledTabProperties.HOVER_LISTENER)) {
          updateHoverListener((HoverListener) ((ValueChange) m.get(TitledTabProperties.HOVER_LISTENER)).getNewValue());
        }

        if (keySet.contains(TitledTabProperties.SIZE_POLICY)) {
          layout.setUseSelectedComponentSize(
              ((TitledTabSizePolicy) ((ValueChange) m.get(TitledTabProperties.SIZE_POLICY)).getNewValue()) == TitledTabSizePolicy.INDIVIDUAL_SIZE);
        }

        if (keySet.contains(TitledTabProperties.HIGHLIGHTED_RAISED_AMOUNT) || keySet.contains(
            TitledTabProperties.BORDER_SIZE_POLICY)) {
          updateBorders = true;
        }

        if (keySet.contains(TitledTabProperties.ENABLED)) {
          doSetEnabled(properties.getEnabled());
        }
      }
    }

    updateBorders = normalStatePanel.updateState(changes, properties.getNormalProperties()) || updateBorders;
    updateBorders = highlightedStatePanel.updateState(changes, properties.getHighlightedProperties()) || updateBorders;
    updateBorders = disabledStatePanel.updateState(changes, properties.getDisabledProperties()) || updateBorders;

    if (updateBorders)
      updateBorders();
  }

  private void updateHoverListener(HoverListener newHoverListener) {
    HoverListener oldHoverListener = hoverListener;
    hoverListener = newHoverListener;
    if (HoverManager.getInstance().isHovered(eventPanel)) {
      if (oldHoverListener != null)
        oldHoverListener.mouseExited(new HoverEvent(TitledTab.this));
      if (hoverListener != null)
        hoverListener.mouseEntered(new HoverEvent(TitledTab.this));
    }
  }

  private Border getInnerBorder(TitledTabStateProperties properties,
                                Direction tabOrientation,
                                int raised,
                                Insets maxInsets) {
    Direction tabDir = properties.getDirection();
    Insets insets = InsetsUtil.rotate(tabDir, properties.getComponentProperties().getInsets());

    if (maxInsets != null)
      insets = InsetsUtil.add(insets,
          InsetsUtil.sub(maxInsets,
              getBorderInsets(properties.getComponentProperties().getBorder())));

    Border border = properties.getComponentProperties().getBorder();
    Border innerBorder = new EmptyBorder(InsetsUtil.add(insets,
        InsetsUtil.setInset(InsetsUtil.EMPTY_INSETS,
            tabOrientation.getOpposite(),
            raised)));
    return border == null ? innerBorder : new CompoundBorder(border, innerBorder);
  }

  private Direction getTabAreaOrientation() {
    return getTabbedPanel() == null ?
                                     lastTabAreaOrientation : getTabbedPanel().getProperties().getTabAreaOrientation();
  }

  private void updateTabAreaOrientation(Direction newDirection) {
    if (lastTabAreaOrientation != newDirection) {
      lastTabAreaOrientation = newDirection;
      updateBorders();

      normalStatePanel.updateShapedPanel(properties.getNormalProperties());
      highlightedStatePanel.updateShapedPanel(properties.getHighlightedProperties());
      disabledStatePanel.updateShapedPanel(properties.getDisabledProperties());
    }
  }

  private void updateCurrentStatePanel() {
    StatePanel newStatePanel = normalStatePanel;
    if (!isEnabled())
      newStatePanel = disabledStatePanel;
    else if (isHighlighted())
      newStatePanel = highlightedStatePanel;

    eventPanel.setToolTipText(newStatePanel.getToolTipText());

    if (currentStatePanel != newStatePanel) {
      if (currentStatePanel != null)
        currentStatePanel.deactivate();
      currentStatePanel = newStatePanel;
      currentStatePanel.activate();
    }
    layout.showComponent(currentStatePanel);
  }

  private MouseEvent convertMouseEvent(MouseEvent e) {
    Point p = SwingUtilities.convertPoint((JComponent) e.getSource(), e.getPoint(), TitledTab.this);
    return new MouseEvent(TitledTab.this, e.getID(), e.getWhen(), e.getModifiers(),
        (int) p.getX(), (int) p.getY(), e.getClickCount(),
        !e.isConsumed() && e.isPopupTrigger(), e.getButton());
  }

  private void doSetEnabled(boolean enabled) {
    super.setEnabled(enabled);
    updateCurrentStatePanel();
  }

  public void setUI(PanelUI ui) {
    if (getUI() != UI)
      super.setUI(UI);
  }

  public void updateUI() {
    setUI(UI);
  }

  public void setOpaque(boolean opaque) {
    // Ignore
  }

}