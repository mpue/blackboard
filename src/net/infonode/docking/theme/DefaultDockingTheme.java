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


// $Id: DefaultDockingTheme.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.docking.theme;

import net.infonode.docking.properties.RootWindowProperties;

/**
 * A helper class that contains an empty theme. This class is only available to make it easier to apply themes to a
 * {@link net.infonode.docking.RootWindow}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.1.0
 */
public class DefaultDockingTheme extends DockingWindowsTheme {
  private RootWindowProperties rootWindowProperties = new RootWindowProperties();

  /**
   * Constructor.
   */
  public DefaultDockingTheme() {
  }

  public String getName() {
    return "Default Theme";
  }

  public RootWindowProperties getRootWindowProperties() {
    return rootWindowProperties;
  }
}
