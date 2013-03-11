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


// $Id: TabbedPanelHoverAction.java,v 1.2 2011-08-26 15:10:44 mpue Exp $
package net.infonode.tabbedpanel.hover;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;

/**
 * <p>
 * TabbedPanelHoverAction is an action that makes it easy to change properties for
 * a hovered {@link TabbedPanel}. The action is meant to be set as a {@link HoverListener}
 * for the entire tabbed panel, the tab area, the tab area components area and/or the
 * content area in their corresponding properties objects.
 * </p>
 *
 * <p>
 * This hover action contains a TabbedPanelProperties object that will be added as
 * super object to the hovered tabbed panel and then automatically removed when the
 * area is no longer hovered.
 * </p>
 *
 * @author johan
 * @version $Revision: 1.2 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @see net.infonode.tabbedpanel.TabAreaProperties
 * @see net.infonode.tabbedpanel.TabAreaComponentsProperties
 * @see net.infonode.tabbedpanel.TabbedPanelContentPanelProperties
 * @since ITP 1.3.0
 */
public class TabbedPanelHoverAction implements HoverListener {
  private TabbedPanelProperties props;

  /**
   * Creates a TabbedPanelHoverAction containing an empty TabbedPanelProperties
   * object.
   */
  public TabbedPanelHoverAction() {
    this(new TabbedPanelProperties());
  }

  /**
   * Creates a TabbedPanelHoverAction with the given TabbedPanelProperties
   * object.
   *
   * @param props reference to a TabbedPanelProperties object
   */
  public TabbedPanelHoverAction(TabbedPanelProperties props) {
    this.props = props;
  }

  /**
   * Gets the TabbedPanelProperties object for this action.
   *
   * @return reference to the TabbedPanelProperties
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return props;
  }

  public void mouseEntered(HoverEvent event) {
    ((TabbedPanel) event.getSource()).getProperties().addSuperObject(props);
  }

  public void mouseExited(HoverEvent event) {
    ((TabbedPanel) event.getSource()).getProperties().removeSuperObject(props);
  }
}
