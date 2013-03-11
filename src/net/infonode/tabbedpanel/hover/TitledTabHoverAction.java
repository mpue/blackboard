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


// $Id: TitledTabHoverAction.java,v 1.2 2011-08-26 15:10:44 mpue Exp $

package net.infonode.tabbedpanel.hover;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * <p>
 * TitledTabHoverAction is an action that makes it easy to change properties for
 * a hovered {@link TitledTab}. The action is meant to be set as a {@link HoverListener}
 * in the {@link TitledTabProperties}.
 * </p>
 *
 * <p>
 * This hover action contains a TitledTabProperties object that will be added as
 * super object to the hovered titled tab and then automatically removed when the
 * titled tab is no longer hovered.
 * </p>
 *
 * @author johan
 * @version $Revision: 1.2 $
 * @see TitledTab
 * @see TitledTabProperties
 * @since ITP 1.3.0
 */
public class TitledTabHoverAction implements HoverListener {
  private TitledTabProperties props;

  /**
   * Creates a TitledTabHoverAction containing an empty TitledTabProperties
   * object.
   */
  public TitledTabHoverAction() {
    this(new TitledTabProperties());
  }

  /**
   * Creates a TitledTabHoverAction with the given TitledTabProperties
   * object.
   *
   * @param props reference to a TitledTabProperties object
   */
  public TitledTabHoverAction(TitledTabProperties props) {
    this.props = props;
  }

  /**
   * Gets the TitledTabProperties object for this action.
   *
   * @return reference to the TitledTabProperties
   */
  public TitledTabProperties getTitledTabProperties() {
    return props;
  }

  public void mouseEntered(HoverEvent event) {
    ((TitledTab) event.getSource()).getProperties().addSuperObject(props);
  }

  public void mouseExited(HoverEvent event) {
    ((TitledTab) event.getSource()).getProperties().removeSuperObject(props);
  }
}