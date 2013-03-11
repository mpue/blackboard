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


// $Id: TabWindowHoverAction.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import net.infonode.docking.properties.ViewTitleBarProperties;
import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabListener;
import net.infonode.tabbedpanel.TabStateChangedEvent;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.hover.TabbedPanelTitledTabHoverAction;
import net.infonode.tabbedpanel.hover.TitledTabTabbedPanelHoverAction;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * <p>
 * TabWindowHoverAction is a hover action that makes it easy to change properties for a tab window
 * and the title bar in the view.
 * </p>
 *
 * <p>
 * This action contains a titled tab proeprties object, a tabbed panel properties object and a view
 * title bar properties object. Those objects are automatically added/removed as superobject to the
 * currently hovered tab window if this action is set as a hover listener in the titled tab properties
 * and the content panel properties for the tabbed panel.
 * </p>
 *
 * <p>
 * Example:
 * <pre>
 * rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties().setHoverListener(tabWindowHoverAction);<br>
 * rootWindowProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().setHoverListener(tabWindowHoverAction);
 * </pre>
 * </p>
 *
 * @author johan
 * @since IDW 1.4.0
 */
public class TabWindowHoverAction implements HoverListener {
  private TabbedPanelProperties tabbedPanelProperties;
  private TitledTabProperties titledTabProperties;
  private ViewTitleBarProperties viewTitleBarProperties;

  private TabbedPanelTitledTabHoverAction tpTabAction;
  private TitledTabTabbedPanelHoverAction tabTpAction;

  private boolean titleBarPropsAdded = false;

  private TabListener tabListener = new TabAdapter() {
    public void tabSelected(TabStateChangedEvent event) {
      if (event.getTab() != null) {
        DockingWindow w = ((WindowTab) event.getTab()).getWindow();
        if (!titleBarPropsAdded && w instanceof View)
          addViewTitleBarProperties((View) w);
      }
    }

    public void tabDeselected(TabStateChangedEvent event) {
      if (event.getTab() != null) {
        DockingWindow w = ((WindowTab) event.getTab()).getWindow();
        if (titleBarPropsAdded && w instanceof View)
          removeViewTitleBarProperties((View) w);
      }
    }
  };

  /**
   * Creates an empty tab window hover action object.
   */
  public TabWindowHoverAction() {
    this(new TabbedPanelProperties(), new TitledTabProperties(), new ViewTitleBarProperties());
  }

  public TabWindowHoverAction(TabbedPanelProperties tabbedPanelProperties,
                              TitledTabProperties titledTabProperties,
                              ViewTitleBarProperties viewTitleBarProperties) {
    this.tabbedPanelProperties = tabbedPanelProperties;
    this.titledTabProperties = titledTabProperties;
    this.viewTitleBarProperties = viewTitleBarProperties;

    tpTabAction = new TabbedPanelTitledTabHoverAction(tabbedPanelProperties, titledTabProperties);
    tabTpAction = new TitledTabTabbedPanelHoverAction(titledTabProperties, tabbedPanelProperties);
  }

  /**
   * Returns this action's tabbed panel properties
   *
   * @return tabbed panel properties
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return tabbedPanelProperties;
  }

  /**
   * Returns this action's titled tab properties
   *
   * @return titled tab properties
   */
  public TitledTabProperties getTitledTabProperties() {
    return titledTabProperties;
  }

  /**
   * Returns this action's view title bar properties
   *
   * @return view title bar properties
   */
  public ViewTitleBarProperties getViewTitleBarProperties() {
    return viewTitleBarProperties;
  }

  public void mouseEntered(HoverEvent event) {
    if (event.getSource() instanceof TabbedPanel) {
      TabbedPanel tp = (TabbedPanel) event.getSource();
      tp.addTabListener(tabListener);
      if (tp.getSelectedTab() != null) {
        DockingWindow w = ((WindowTab) tp.getSelectedTab()).getWindow();
        if (w instanceof View)
          addViewTitleBarProperties((View) w);
      }

      tpTabAction.mouseEntered(event);
    }
    else if (event.getSource() instanceof TitledTab) {
      WindowTab tab = (WindowTab) event.getSource();
      tab.addTabListener(tabListener);
      if (tab.isSelected() && tab.getWindow() instanceof View)
        addViewTitleBarProperties((View) tab.getWindow());

      tabTpAction.mouseEntered(event);
    }
  }

  public void mouseExited(HoverEvent event) {
    if (event.getSource() instanceof TabbedPanel) {
      TabbedPanel tp = (TabbedPanel) event.getSource();
      tp.removeTabListener(tabListener);
      if (titleBarPropsAdded && tp.getSelectedTab() != null) {
        DockingWindow w = ((WindowTab) tp.getSelectedTab()).getWindow();
        if (w instanceof View)
          removeViewTitleBarProperties((View) w);
      }

      tpTabAction.mouseExited(event);
    }
    else if (event.getSource() instanceof TitledTab) {
      WindowTab tab = (WindowTab) event.getSource();
      tab.removeTabListener(tabListener);
      if (titleBarPropsAdded && tab.getWindow() instanceof View)
        removeViewTitleBarProperties((View) tab.getWindow());

      tabTpAction.mouseExited(event);
    }
  }

  private void addViewTitleBarProperties(View view) {
    view.getViewProperties().getViewTitleBarProperties().addSuperObject(viewTitleBarProperties);
    titleBarPropsAdded = true;
  }

  private void removeViewTitleBarProperties(View view) {
    view.getViewProperties().getViewTitleBarProperties().removeSuperObject(viewTitleBarProperties);
    titleBarPropsAdded = false;
  }
}
