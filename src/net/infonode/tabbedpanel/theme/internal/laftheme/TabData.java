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


// $Id: TabData.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.tabbedpanel.theme.internal.laftheme;

import java.awt.Dimension;
import java.util.ArrayList;

import net.infonode.gui.DimensionUtil;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.util.Direction;

class TabData {
  private final ArrayList tabList = new ArrayList();

  private final ArrayList visibleTabRects = new ArrayList();

  private TabbedPanel tabbedPanel;

  private Direction areaOrientation;

  private int tabAreaHeight;

  private int tabAreaWidth;

  private int selectedTabPainterIndex;

  private Dimension tpInternalSize;

  private Tab preTab;
  private Tab postTab;

  public TabData() {
    reset();
  }

  public void reset() {
    tabList.clear();
    visibleTabRects.clear();
    tabbedPanel = null;
    areaOrientation = null;
    tabAreaHeight = 0;
    tabAreaWidth = 0;
    selectedTabPainterIndex = -1;
    tpInternalSize = null;
    preTab = null;
    postTab = null;
  }

  public ArrayList getTabList() {
    return tabList;
  }

  public ArrayList getVisibleTabRects() {
    return visibleTabRects;
  }

  public Direction getAreaOrientation() {
    return areaOrientation;
  }

  public TabbedPanel getTabbedPanel() {
    return tabbedPanel;
  }

  public void initialize(TabbedPanel tabbedPanel) {
    this.tabbedPanel = tabbedPanel;
    areaOrientation = tabbedPanel.getProperties().getTabAreaOrientation();
    tpInternalSize = DimensionUtil.getInnerDimension(tabbedPanel.getSize(), tabbedPanel.getInsets());
  }

  public Dimension getTabbedPanelSize() {
    return tpInternalSize;
  }

  public int getTabbedPanelWidth() {
    return tpInternalSize.width;
  }

  public int getTabbedPanelHeight() {
    return tpInternalSize.height;
  }

  public boolean isHorizontalLayout() {
    return !areaOrientation.isHorizontal();
  }

  public int getSelectedTabPainterIndex() {
    return selectedTabPainterIndex;
  }

  public void setSelectedTabPainterIndex(int selectedTabPainterIndex) {
    this.selectedTabPainterIndex = selectedTabPainterIndex;
  }

  public int getTabCount() {
    return tabList.size();
  }

  public int getTabAreaHeight() {
    return tabAreaHeight;
  }

  public void setTabAreaHeight(int tabAreaHeight) {
    this.tabAreaHeight = tabAreaHeight;
  }

  public int getTabAreaWidth() {
    return tabAreaWidth;
  }

  public void setTabAreaWidth(int tabAreaWidth) {
    this.tabAreaWidth = tabAreaWidth;
  }

  public Tab getPostTab() {
    return postTab;
  }

  public void setPostTab(Tab postTab) {
    this.postTab = postTab;
  }

  public Tab getPreTab() {
    return preTab;
  }

  public void setPreTab(Tab preTab) {
    this.preTab = preTab;
  }
}
