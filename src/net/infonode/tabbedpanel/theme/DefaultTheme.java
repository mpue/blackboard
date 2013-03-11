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


// $Id: DefaultTheme.java,v 1.2 2011-08-26 15:10:42 mpue Exp $

package net.infonode.tabbedpanel.theme;

import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * A default theme that only contains empty tabbed panel properties and titled
 * tab properties object i.e. they don't contain any properties that are set
 * with any values. This theme is only a convenience theme so that a default
 * theme can be added and removed just like any other theme.
 *
 * @author johan
 * @version $Revision: 1.2 $
 * @since ITP 1.1.0
 */
public class DefaultTheme extends TabbedPanelTitledTabTheme {
  private TitledTabProperties tabProperties = new TitledTabProperties();
  private TabbedPanelProperties tabbedPanelProperties = new TabbedPanelProperties();

  /**
   * Constructs a default theme
   */
  public DefaultTheme() {
  }

  /**
   * Gets the name for this theme
   *
   * @return the name
   */
  public String getName() {
    return "Default Theme";
  }

  /**
   * Gets the TabbedPanelProperties for this theme
   *
   * @return the TabbedPanelProperties
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return tabbedPanelProperties;
  }

  /**
   * Gets the TitledTabProperties for this theme
   *
   * @return the TitledTabProperties
   */
  public TitledTabProperties getTitledTabProperties() {
    return tabProperties;
  }
}