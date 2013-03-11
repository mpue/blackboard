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


// $Id: TabDropDownList.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.tabbedpanel.internal;

import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.infonode.gui.PopupList;
import net.infonode.gui.PopupListListener;
import net.infonode.gui.TextIconListCellRenderer;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabDropDownListVisiblePolicy;
import net.infonode.tabbedpanel.TabEvent;
import net.infonode.tabbedpanel.TabListener;
import net.infonode.tabbedpanel.TabRemovedEvent;
import net.infonode.tabbedpanel.TabbedPanel;

/**
 * @author Bjorn Lind
 * @version $Revision: 1.3 $ $Date: 2011-09-07 19:56:09 $
 * @since ITP 1.1.0
 */
public class TabDropDownList extends PopupList {
  private TabbedPanel tabbedPanel;
  private TextIconListCellRenderer cellRenderer;

  private TabListener tabListener = new TabAdapter() {
    public void tabAdded(TabEvent event) {
      if (event.getTab().getTabbedPanel().getTabCount() == 2)
        setVisible(true);
    }

    public void tabRemoved(TabRemovedEvent event) {
      if (event.getTabbedPanel().getTabCount() == 1)
        setVisible(false);
    }
  };

  public TabDropDownList(final TabbedPanel tabbedPanel, AbstractButton button) {
    super(button);
    this.tabbedPanel = tabbedPanel;

    addPopupListListener(new PopupListListener() {
      public void willBecomeVisible(PopupList l) {
        int numTabs = tabbedPanel.getTabCount();
        Tab[] tabs = new Tab[numTabs];
        for (int i = 0; i < numTabs; i++) {
          tabs[i] = tabbedPanel.getTabAt(i);
        }
        cellRenderer.calculateMaximumIconWidth(tabs);
        getList().setListData(tabs);
        getList().setSelectedValue(tabbedPanel.getSelectedTab(), true);
      }
    });

    addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting())
          tabbedPanel.setSelectedTab((Tab) getList().getSelectedValue());
      }
    });

    if (tabbedPanel.getProperties().getTabDropDownListVisiblePolicy() ==
        TabDropDownListVisiblePolicy.MORE_THAN_ONE_TAB) {
      tabbedPanel.addTabListener(tabListener);
      setVisible(tabbedPanel.getTabCount() > 1);
    }

    cellRenderer = new TextIconListCellRenderer(getList().getCellRenderer());
    getList().setCellRenderer(cellRenderer);
    setOpaque(false);
  }

  public void dispose() {
    tabbedPanel.removeTabListener(tabListener);
  }

  public void updateUI() {
    super.updateUI();

    if (cellRenderer != null) {
      ListCellRenderer renderer = (ListCellRenderer) UIManager.get("List.cellRenderer");
      if (renderer == null)
        renderer = new DefaultListCellRenderer();
      cellRenderer.setRenderer(renderer);
    }
  }
}