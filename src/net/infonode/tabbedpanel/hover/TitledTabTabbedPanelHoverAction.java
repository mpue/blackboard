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


// $Id: TitledTabTabbedPanelHoverAction.java,v 1.2 2011-08-26 15:10:44 mpue Exp $

package net.infonode.tabbedpanel.hover;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabStateChangedEvent;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * <p>
 * TitledTabTabbedPanelHoverAction is an action that makes it easy to change
 * properties for a hovered {@link TitledTab} and the {@link TabbedPanel} it is a
 * member of. The action is meant to be set as a {@link HoverListener} for a
 * TitledTab in the
 * {@link net.infonode.tabbedpanel.titledtab.TitledTabProperties}.
 * </p>
 *
 * <p>
 * The action can be configured to add the TabbedPanelProperties only when the
 * highlighted TitledTab is hovered or when any of the TitledTabs are hovered.
 * </p>
 *
 * <p>
 * This hover action contains a TitledTabProperties object that will be added as
 * super object to the hovered titled tab and then automatically removed when
 * the titled tab is no longer hovered. It also contains a TabbedPanelProperties
 * object that will be added as super object to the tabbed panel that the
 * hovered titled tab is a member of. The TabbedPanelProperties are
 * automatically removed from the tabbed panel if the hovered titled tab is
 * removed.
 * </p>
 *
 * <p>
 * <strong>Note: </strong> This action is not meant to be set as hover listener
 * for a Tabbed Panel (or any of its areas). For TabbedPanel, use
 * {@link net.infonode.tabbedpanel.hover.TabbedPanelTitledTabHoverAction} instead.
 * </p>
 *
 * @author johan
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see TitledTab
 * @see net.infonode.tabbedpanel.titledtab.TitledTabProperties
 * @see TabbedPanelProperties
 * @see net.infonode.tabbedpanel.hover.TabbedPanelTitledTabHoverAction
 * @since ITP 1.3.0
 */
public class TitledTabTabbedPanelHoverAction implements HoverListener {
  private TabbedPanelProperties tabbedPanelProperties;
  private TitledTabProperties titledTabProperties;

  private boolean applied = false;
  private boolean onlyHighlighted;

  private TabAdapter tabListener = new TabAdapter() {
    public void tabHighlighted(TabStateChangedEvent event) {
      if (event.getCurrentTab() != null && onlyHighlighted)
        applyTabbedPanel(event.getCurrentTab().getTabbedPanel());
    }

    public void tabDehighlighted(TabStateChangedEvent event) {
      if (event.getPreviousTab() != null && onlyHighlighted)
        removeTabbedPanel(event.getPreviousTab().getTabbedPanel());
    }
  };

  /**
   * Creates a TitledTabTabbedPanelHoverAction containing an empty
   * TitledTabProperties object and an empty TabbedPanelProperties object. The
   * TabbedPanelProperties are only applied when the highlighted tab is
   * hovered.
   */
  public TitledTabTabbedPanelHoverAction() {
    this(new TitledTabProperties(), new TabbedPanelProperties());
  }

  /**
   * Creates a TitledTabTabbedPanelHoverAction containing an empty
   * TitledTabProperties object and an empty TabbedPanelProperties object.
   *
   * @param allTabs true if the TabbedPanelProperties should be applied to the
   *                tabbed panel when a tab is hovered, false if it should only be
   *                applied when the the highlighted tab is hovered
   */
  public TitledTabTabbedPanelHoverAction(boolean allTabs) {
    this(new TitledTabProperties(), new TabbedPanelProperties(), allTabs);
  }

  /**
   * Creates a TitledTabTabbedPanelHoverAction containing with the given
   * TitledTabProperties object and the given TabbedPanelProperties object.
   * The TabbedPanelProperties are only applied when the highlighted tab is
   * hovered.
   *
   * @param titledTabProperties   reference to a TitledTabProperties object
   * @param tabbedPanelProperties reference to a TabbedPanelProperties object
   */
  public TitledTabTabbedPanelHoverAction(TitledTabProperties titledTabProperties,
                                         TabbedPanelProperties tabbedPanelProperties) {
    this(titledTabProperties, tabbedPanelProperties, false);
  }

  /**
   * Creates a TitledTabTabbedPanelHoverAction containing with the given
   * TitledTabProperties object and the given TabbedPanelProperties object.
   *
   * @param titledTabProperties   reference to a TitledTabProperties object
   * @param tabbedPanelProperties reference to a TabbedPanelProperties object
   * @param allTabs               true if the TabbedPanelProperties should be applied to the
   *                              tabbed panel when a tab is hovered, false if it should only be
   *                              applied when the the highlighted tab is hovered
   */
  public TitledTabTabbedPanelHoverAction(TitledTabProperties titledTabProperties,
                                         TabbedPanelProperties tabbedPanelProperties,
                                         boolean allTabs) {
    this.titledTabProperties = titledTabProperties;
    this.tabbedPanelProperties = tabbedPanelProperties;
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
    TitledTab tab = (TitledTab) event.getSource();
    if (onlyHighlighted) {
      tab.addTabListener(tabListener);
      if (tab.isHighlighted())
        applyTabbedPanel(tab.getTabbedPanel());
    }
    else {
      applyTabbedPanel(tab.getTabbedPanel());
    }

    tab.getProperties().addSuperObject(titledTabProperties);
  }

  public void mouseExited(HoverEvent event) {
    TitledTab tab = (TitledTab) event.getSource();
    removeTabbedPanel(tab.getTabbedPanel());
    if (onlyHighlighted)
      tab.removeTabListener(tabListener);
    tab.getProperties().removeSuperObject(titledTabProperties);
  }

  private void applyTabbedPanel(TabbedPanel tabbedPanel) {
    if (!applied) {
      tabbedPanel.getProperties().addSuperObject(tabbedPanelProperties);
      applied = true;
    }
  }

  private void removeTabbedPanel(TabbedPanel tabbedPanel) {
    if (applied) {
      tabbedPanel.getProperties().removeSuperObject(tabbedPanelProperties);
      applied = false;
    }
  }
}