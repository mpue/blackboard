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


// $Id: TabContentPanel.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel;

import java.awt.Component;

import javax.swing.JPanel;

import net.infonode.gui.layout.StackableLayout;

/**
 * A TabContentPanel is a container for tabs' content components. It listens to
 * a tabbed panel and manages the tabs' content components by showing and hiding
 * the components based upon the selection of tabs in the tabbed panel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TabbedPanel
 * @see Tab
 */
public class TabContentPanel extends JPanel {
  private TabbedPanel tabbedPanel;
  private StackableLayout layout = new StackableLayout(this);
  private TabListener listener = new TabAdapter() {
    public void tabSelected(TabStateChangedEvent event) {
      layout.showComponent(event.getTab() == null ? null : event.getTab().getContentComponent());
    }

    public void tabRemoved(TabRemovedEvent event) {
      if (event.getTab().getContentComponent() != null)
        remove(event.getTab().getContentComponent());
    }

    public void tabAdded(TabEvent event) {
      if (event.getTab().getContentComponent() != null)
        add(event.getTab().getContentComponent());
    }
  };

  /**
   * <p>
   * Constructs a TabContentPanel
   * </p>
   *
   * <p><strong>Note:</strong> setTabbedPanel(...) must be called before the tabs'
   * content components can be shown on the screen.
   * </p>
   *
   * @since ITP 1.4.0
   */
  public TabContentPanel() {
    setLayout(layout);
    setOpaque(false);
    layout.setAutoShowFirstComponent(false);
  }

  /**
   * Constructs a TabContentPanel
   *
   * @param tabbedPanel the TabbedPanel for whom this component is the tabs' content
   *                    component container
   */
  public TabContentPanel(TabbedPanel tabbedPanel) {
    this();
    setTabbedPanel(tabbedPanel);
  }

  /**
   * Gets the TabbedPanel for whom this component is the tabs' content component
   * container
   *
   * @return the TabbedPanel
   */
  public TabbedPanel getTabbedPanel() {
    return tabbedPanel;
  }

  /**
   * Sets the TabbedPanel
   *
   * @param tabbedPanel the TabbedPanel for whom this component is the tabs' content
   *                    component container
   * @since ITP 1.4.0
   */
  public void setTabbedPanel(TabbedPanel tabbedPanel) {
    if (this.tabbedPanel != tabbedPanel) {
      if (this.tabbedPanel != null) {
        this.tabbedPanel.removeTabListener(listener);
        removeAll();
      }

      this.tabbedPanel = tabbedPanel;

      if (this.tabbedPanel != null) {
        tabbedPanel.addTabListener(listener);
        for (int i = 0; i < tabbedPanel.getTabCount(); i++) {
          Component c = tabbedPanel.getTabAt(i).getContentComponent();
          if (c != null)
            add(tabbedPanel.getTabAt(i).getContentComponent());
        }
      }
    }
  }
}
