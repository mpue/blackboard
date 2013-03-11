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


// $Id: ViewTitleBar.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.internal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import net.infonode.docking.View;
import net.infonode.docking.internalutil.ButtonInfo;
import net.infonode.docking.internalutil.CloseButtonInfo;
import net.infonode.docking.internalutil.DockButtonInfo;
import net.infonode.docking.internalutil.InternalDockingUtil;
import net.infonode.docking.internalutil.MaximizeButtonInfo;
import net.infonode.docking.internalutil.MinimizeButtonInfo;
import net.infonode.docking.internalutil.RestoreButtonInfo;
import net.infonode.docking.internalutil.UndockButtonInfo;
import net.infonode.docking.properties.ViewTitleBarProperties;
import net.infonode.docking.properties.ViewTitleBarStateProperties;
import net.infonode.gui.ContentTitleBar;
import net.infonode.gui.DimensionProvider;
import net.infonode.gui.hover.hoverable.HoverManager;
import net.infonode.properties.gui.InternalPropertiesUtil;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapListener;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;

/**
 * @author johan
 */
public class ViewTitleBar extends ContentTitleBar {
  private static final ButtonInfo[] buttonInfos = {
      new UndockButtonInfo(ViewTitleBarStateProperties.UNDOCK_BUTTON_PROPERTIES),
      new DockButtonInfo(ViewTitleBarStateProperties.DOCK_BUTTON_PROPERTIES),
      new MinimizeButtonInfo(ViewTitleBarStateProperties.MINIMIZE_BUTTON_PROPERTIES),
      new MaximizeButtonInfo(ViewTitleBarStateProperties.MAXIMIZE_BUTTON_PROPERTIES),
      new RestoreButtonInfo(ViewTitleBarStateProperties.RESTORE_BUTTON_PROPERTIES),
      new CloseButtonInfo(ViewTitleBarStateProperties.CLOSE_BUTTON_PROPERTIES)};

  private DimensionProvider minimumSizeProvider;
  private net.infonode.docking.View view;
  private AbstractButton[] buttons = new AbstractButton[buttonInfos.length];
  private List customBarComponents;

  private PropertyMapTreeListener propertiesListener = new PropertyMapTreeListener() {
    public void propertyValuesChanged(Map changes) {
      updateTitleBar(changes);
    }
  };

  private PropertyMapListener buttonsListener = new PropertyMapListener() {
    public void propertyValuesChanged(PropertyMap propertyMap, Map changes) {
      updateViewButtons(null);
    }
  };

  public ViewTitleBar(View view) {
    super(view);
    this.view = view;

    PropertyMapWeakListenerManager.addWeakTreeListener(view.getViewProperties().getViewTitleBarProperties().getMap(),
                                                       propertiesListener);
    PropertyMapWeakListenerManager.addWeakListener(view.getWindowProperties().getMap(), buttonsListener);

    updateTitleBar(null);
    updateTitle();
    updateViewButtons(null);

    HoverManager.getInstance().addHoverable(this);
  }

  private void updateTitle() {
    ViewTitleBarStateProperties titleProps = view.getViewProperties().getViewTitleBarProperties().getNormalProperties();
    getLabel().setText(titleProps.getTitleVisible() ? titleProps.getTitle() : null);
    getLabel().setIcon(titleProps.getIconVisible() ? titleProps.getIcon() : null);
  }

  private void updateTitleBar(Map changes) {
    updateTitle();

    JLabel label = getLabel();
    ViewTitleBarProperties titleBarProperties = view.getViewProperties().getViewTitleBarProperties();
    ShapedPanelProperties shapedProperties = titleBarProperties.getNormalProperties().getShapedPanelProperties();
    ComponentProperties componentProperties = titleBarProperties.getNormalProperties().getComponentProperties();

    updateViewButtons(changes);

    if (changes == null) {
      minimumSizeProvider = titleBarProperties.getMinimumSizeProvider();

      componentProperties.applyTo(this);

      for (int i = 0; i < buttons.length; i++)
        if (buttons[i] != null) {
          buttons[i].setForeground(componentProperties.getForegroundColor());
          buttons[i].setBackground(
              shapedProperties.getComponentPainter() != null ? null : componentProperties.getBackgroundColor());
        }

      {
        label.setForeground(componentProperties.getForegroundColor());
        label.setFont(componentProperties.getFont());
        label.setIconTextGap(titleBarProperties.getNormalProperties().getIconTextGap());
      }

      {
        InternalPropertiesUtil.applyTo(shapedProperties, this);
      }

      setLayoutDirection(titleBarProperties.getDirection());
      setHoverListener(titleBarProperties.getHoverListener());
      setLabelAlignment(titleBarProperties.getNormalProperties().getIconTextHorizontalAlignment());
    }
    else {
      // View title bar
      Map map = (Map) changes.get(titleBarProperties.getMap());
      if (map != null) {
        if (map.containsKey(ViewTitleBarProperties.MINIMUM_SIZE_PROVIDER)) {
          minimumSizeProvider = titleBarProperties.getMinimumSizeProvider();
          revalidate();
        }

        if (map.containsKey(ViewTitleBarProperties.DIRECTION))
          setLayoutDirection(titleBarProperties.getDirection());

        if (map.containsKey(ViewTitleBarProperties.HOVER_LISTENER))
          setHoverListener(titleBarProperties.getHoverListener());

      }

      // State properties
      map = (Map) changes.get(titleBarProperties.getNormalProperties().getMap());
      if (map != null) {
        if (map.containsKey(ViewTitleBarStateProperties.ICON_TEXT_GAP))
          label.setIconTextGap(titleBarProperties.getNormalProperties().getIconTextGap());

        if (map.containsKey(ViewTitleBarStateProperties.ICON_TEXT_HORIZONTAL_ALIGNMENT))
          setLabelAlignment(titleBarProperties.getNormalProperties().getIconTextHorizontalAlignment());
      }

      // Component properties
      map = (Map) changes.get(titleBarProperties.getNormalProperties().getComponentProperties().getMap());
      if (map != null) {
        componentProperties.applyTo(this);

        label.setForeground(componentProperties.getForegroundColor());
        label.setFont(componentProperties.getFont());

        for (int i = 0; i < buttons.length; i++)
          if (buttons[i] != null) {
            buttons[i].setForeground(componentProperties.getForegroundColor());
            buttons[i].setBackground(
                shapedProperties.getComponentPainter() != null ? null : componentProperties.getBackgroundColor());
          }
      }

      // Shaped panel properties
      map = (Map) changes.get(titleBarProperties.getNormalProperties().getShapedPanelProperties().getMap());
      if (map != null) {
        InternalPropertiesUtil.applyTo(shapedProperties, this);
      }
    }
  }

  public void updateViewButtons(Map changes) {
    InternalDockingUtil.updateButtons(buttonInfos,
                                      buttons,
                                      null,
                                      view,
                                      view.getViewProperties().getViewTitleBarProperties().getNormalProperties()
                                          .getMap(),
                                      changes);

    if (shouldUpdateButtons())
      updateCustomBarComponents(customBarComponents);
  }

  private boolean shouldUpdateButtons() {
    JComponent[] titleComponents = getRightTitleComponents();

    if (titleComponents == null || titleComponents.length < buttons.length)
      return true;

    for (int i = 0; i < buttons.length; i++)
      if (titleComponents[titleComponents.length - i - 1] != buttons[i])
        return true;

    return false;
  }

  public void updateCustomBarComponents(List list) {
    this.customBarComponents = list;

    int size = list == null ? 0 : list.size();
    JComponent[] components = new JComponent[size + buttons.length];
    Insets[] insets = new Insets[components.length];

    if (list != null) {
      for (int i = 0; i < list.size(); i++) {
        components[i] = (JComponent) list.get(i);
        insets[i] = i == 0 ?
                    new Insets(0, 0, 0, 0) :
                    new Insets(0,
                               view.getViewProperties().getViewTitleBarProperties().getNormalProperties()
                                   .getButtonSpacing(),
                               0,
                               0);
      }
    }

    for (int i = 0; i < buttons.length; i++) {
      int k = size + i;
      components[k] = buttons[i];
      insets[k] = k == 0 ?
                  new Insets(0, 0, 0, 0) :
                  new Insets(0,
                             view.getViewProperties().getViewTitleBarProperties().getNormalProperties()
                                 .getButtonSpacing(),
                             0,
                             0);
    }

    setRightTitleComponents(components, insets);
  }

  public void dispose() {
    HoverManager.getInstance().removeHoverable(this);
  }

  private int pressedCount = 0;
  private boolean dragOutside = false;

  protected void processMouseEvent(MouseEvent e) {
    if (e.getID() == MouseEvent.MOUSE_PRESSED)
      pressedCount++;

    if (e.getID() == MouseEvent.MOUSE_RELEASED) {
      pressedCount--;

      if (pressedCount <= 0)
        dragOutside = false;
    }

    super.processMouseEvent(e);
  }

  protected void processMouseMotionEvent(MouseEvent e) {
    if (e.getID() == MouseEvent.MOUSE_DRAGGED && !dragOutside) {
      dragOutside = !contains(e.getPoint());
      if (!dragOutside)
        return;
    }

    super.processMouseMotionEvent(e);
  }

  public Dimension getMinimumSize() {
    if (minimumSizeProvider == null)
      return super.getMinimumSize();

    Dimension d = minimumSizeProvider.getDimension(this);
    return d == null ? super.getMinimumSize() : d;
  }

  public Dimension getPreferredSize() {
    Dimension d = minimumSizeProvider == null ? null : minimumSizeProvider.getDimension(this);

    Dimension pSize = super.getPreferredSize();
    if (d == null)
      return pSize;

    return new Dimension(Math.max(d.width, pSize.width), Math.max(d.height, pSize.height));
  }
}
