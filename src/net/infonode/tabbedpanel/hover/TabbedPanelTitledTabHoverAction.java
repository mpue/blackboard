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


// $Id: TabbedPanelTitledTabHoverAction.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.tabbedpanel.hover;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabEvent;
import net.infonode.tabbedpanel.TabRemovedEvent;
import net.infonode.tabbedpanel.TabStateChangedEvent;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * <p>
 * TabbedPanelTitledTabHoverAction is an action that makes it easy to change
 * properties for a hovered {@link TabbedPanel} containing {@link TitledTab}s.
 * The action is meant to be set as a {@link HoverListener} for the entire
 * tabbed panel, the tab area, the tab area components area and/or the content
 * area in their corresponding properties objects.
 * </p>
 *
 * <p>
 * The action can be configured to add the TitledTabProperties to all tabs or
 * only the highlighted tab.
 * </p>
 *
 * <p>
 * This hover action contains a TabbedPanelProperties object that will be added
 * as super object to the hovered tabbed panel and then automatically removed
 * when the area is no longer hovered. It also contains a TitledTabProperties
 * object that will be added as super object to all titled tabs in the hovered
 * tabbed panel and then removed when the tabbed panel is no longer hovered.
 * </p>
 *
 * <p>
 * If a titled tab is added to the tabbed panel while the tabbed panel is
 * hovered, the action will automatically add the TitledTabProperties to the
 * titled tab. If a titled tab is removed while the tabbed panel is hovered, the
 * properties will automatically be removed.
 * </p>
 *
 * <p>
 * <strong>Note: </strong> This action is not meant to be set as hover listener
 * in the TitledTabProperties for a titled tab. For TitledTab, use
 * {@link net.infonode.tabbedpanel.hover.TitledTabTabbedPanelHoverAction} instead.
 * </p>
 *
 * @author johan
 * @version $Revision: 1.3 $
 * @see TabbedPanel
 * @see TitledTab
 * @see TabbedPanelProperties
 * @see net.infonode.tabbedpanel.TabAreaProperties
 * @see net.infonode.tabbedpanel.TabAreaComponentsProperties
 * @see net.infonode.tabbedpanel.TabbedPanelContentPanelProperties
 * @see net.infonode.tabbedpanel.hover.TitledTabTabbedPanelHoverAction
 * @see TitledTabProperties
 * @since ITP 1.3.0
 */
public class TabbedPanelTitledTabHoverAction implements HoverListener {
  private TabbedPanelProperties tabbedPanelProperties;
  private TitledTabProperties titledTabProperties;
  private boolean onlyHighlighted;

  private TabAdapter tabListener = new TabAdapter() {
    public void tabAdded(TabEvent event) {
      if (event.getTab() instanceof TitledTab) {
        if (!onlyHighlighted || (onlyHighlighted && event.getTab().isHighlighted())) {
          ((TitledTab) event.getTab()).getProperties().addSuperObject(titledTabProperties);
        }
      }
    }

    public void tabRemoved(TabRemovedEvent event) {
      if (event.getTab() instanceof TitledTab)
        ((TitledTab) event.getTab()).getProperties().removeSuperObject(titledTabProperties);
    }

    public void tabHighlighted(TabStateChangedEvent event) {
      if (event.getCurrentTab() != null && event.getCurrentTab() instanceof TitledTab)
        applyTitledTabProperties(event.getTabbedPanel(), ((TitledTab) event.getCurrentTab()));
    }

    public void tabDehighlighted(TabStateChangedEvent event) {
      if (event.getPreviousTab() != null && event.getPreviousTab() instanceof TitledTab)
        removeTitledTabProperties(event.getTabbedPanel(), ((TitledTab) event.getPreviousTab()));
    }
  };

  /**
   * Creates a TabbedPanelTitledTabHoverAction containing an empty
   * TabbedPanelProperties object and an empty TitledTabProperties object. The
   * TitledTabProperties are only applied to the highlighted tab.
   */
  public TabbedPanelTitledTabHoverAction() {
    this(false);
  }

  /**
   * Creates a TabbedPanelTitledTabHoverAction containing an empty
   * TabbedPanelProperties object and an empty TitledTabProperties object.
   *
   * @param allTabs true if TitledTabProperties should be applied to all tabs,
   *                false if only to the highlighted tab
   */
  public TabbedPanelTitledTabHoverAction(boolean allTabs) {
    this(new TabbedPanelProperties(), new TitledTabProperties(), allTabs);
  }

  /**
   * Creates a TabbedPanelTitledTabHoverAction with the given
   * TabbedPanelProperties object and the given TitledTabProperties object.
   * The TitledTabProperties are only applied to the highlighted tab.
   *
   * @param tabbedPanelProperties reference to a TabbedPanelProperties object
   * @param titledTabProperties   reference to a TitledTabProperties object
   */
  public TabbedPanelTitledTabHoverAction(TabbedPanelProperties tabbedPanelProperties,
                                         TitledTabProperties titledTabProperties) {
    this(tabbedPanelProperties, titledTabProperties, false);
  }

  /**
   * Creates a TabbedPanelTitledTabHoverAction with the given
   * TabbedPanelProperties object and the given TitledTabProperties object.
   *
   * @param tabbedPanelProperties reference to a TabbedPanelProperties object
   * @param titledTabProperties   reference to a TitledTabProperties object
   * @param allTabs               true if TitledTabProperties should be applied to all tabs,
   *                              false if only to the highlighted tab
   */
  public TabbedPanelTitledTabHoverAction(TabbedPanelProperties tabbedPanelProperties,
                                         TitledTabProperties titledTabProperties,
                                         boolean allTabs) {
    this.tabbedPanelProperties = tabbedPanelProperties;
    this.titledTabProperties = titledTabProperties;
    this.onlyHighlighted = !allTabs;
  }

  /**
   * Gets the TitledTabProperties object for this action.
   *
   * @return reference to the TitledTabProperties
   */
  public TitledTabProperties getTitledTabProperties() {
    return titledTabProperties;
  }

  /**
   * Gets the TabbedPanelProperties object for this action.
   *
   * @return reference to the TabbedPanelProperties
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return tabbedPanelProperties;
  }

  public void mouseEntered(HoverEvent event) {
    TabbedPanel tp = (TabbedPanel) event.getSource();
    tp.getProperties().addSuperObject(tabbedPanelProperties);
    tp.addTabListener(tabListener);
    if (tp.getHighlightedTab() != null && tp.getHighlightedTab() instanceof TitledTab)
      applyTitledTabProperties(tp, ((TitledTab) tp.getHighlightedTab()));
    else
      applyTitledTabProperties(tp, null);
  }

  public void mouseExited(HoverEvent event) {
    TabbedPanel tp = (TabbedPanel) event.getSource();
    tp.getProperties().removeSuperObject(tabbedPanelProperties);
    tp.removeTabListener(tabListener);
    if (tp.getHighlightedTab() != null && tp.getHighlightedTab() instanceof TitledTab)
      removeTitledTabProperties(tp, ((TitledTab) tp.getHighlightedTab()));
    else
      removeTitledTabProperties(tp, null);
  }

  private void applyTitledTabProperties(TabbedPanel tabbedPanel, TitledTab titledTab) {
    if (onlyHighlighted) {
      if (titledTab != null)
        titledTab.getProperties().addSuperObject(titledTabProperties);
    }
    else {
      for (int i = 0; i < tabbedPanel.getTabCount(); i++) {
        Tab tab = tabbedPanel.getTabAt(i);
        if (tab instanceof TitledTab)
          ((TitledTab) tab).getProperties().addSuperObject(titledTabProperties);
      }
    }
  }

  private void removeTitledTabProperties(TabbedPanel tabbedPanel, TitledTab titledTab) {
    if (onlyHighlighted) {
      if (titledTab != null)
        titledTab.getProperties().removeSuperObject(titledTabProperties);
    }
    else {
      for (int i = 0; i < tabbedPanel.getTabCount(); i++) {
        Tab tab = tabbedPanel.getTabAt(i);
        if (tab instanceof TitledTab)
          ((TitledTab) tab).getProperties().removeSuperObject(titledTabProperties);
      }
    }
  }
}