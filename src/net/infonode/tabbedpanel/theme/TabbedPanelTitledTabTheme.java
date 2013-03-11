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


// $Id: TabbedPanelTitledTabTheme.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.tabbedpanel.theme;

import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * Abstract class for a theme for a tabbed panel with titled tabs.
 *
 * @author johan
 * @version $Revision: 1.2 $
 * @since ITP 1.1.0
 */
public abstract class TabbedPanelTitledTabTheme {
  /**
   * Gets the name for this theme
   *
   * @return the name
   */
  abstract public String getName();

  /**
   * Gets the TabbedPanelProperties for this theme
   *
   * @return the TabbedPanelProperties
   */
  abstract public TabbedPanelProperties getTabbedPanelProperties();

  /**
   * Gets the TitledTabProperties for this theme
   *
   * @return the TitledTabProperties
   */
  abstract public TitledTabProperties getTitledTabProperties();
}
