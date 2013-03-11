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


// $Id: DefaultButtonFactories.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.docking;

import net.infonode.gui.button.ButtonFactory;
import net.infonode.gui.button.FlatButtonFactory;

/**
 * Contains the default window button factories used in window tabs and {@link TabWindow}'s.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.1.0
 */
public class DefaultButtonFactories {
  private static final FlatButtonFactory BUTTON_FACTORY = new FlatButtonFactory();

  private DefaultButtonFactories() {
  }

  /**
   * Returns the default close button factory.
   *
   * @return the default close button factory
   */
  public static ButtonFactory getCloseButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default minimize button factory.
   *
   * @return the default minimize button factory
   */
  public static ButtonFactory getMinimizeButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default maximize button factory.
   *
   * @return the default maximize button factory
   */
  public static ButtonFactory getMaximizeButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default restore button factory.
   *
   * @return the default restore button factory
   */
  public static ButtonFactory getRestoreButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default undock button factory.
   *
   * @return the default undock button factory
   * @since IDW 1.4.0
   */
  public static ButtonFactory getUndockButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default dock button factory.
   *
   * @return the default dock button factory
   * @since IDW 1.4.0
   */
  public static ButtonFactory getDockButtonFactory() {
    return BUTTON_FACTORY;
  }
}
