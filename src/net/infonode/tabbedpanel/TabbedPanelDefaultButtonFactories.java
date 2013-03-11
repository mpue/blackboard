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


// $Id: TabbedPanelDefaultButtonFactories.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.tabbedpanel;

import net.infonode.gui.button.ButtonFactory;
import net.infonode.gui.button.FlatButtonFactory;

/**
 * Contains the default tabbed panel button factories.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since ITP 1.3.0
 */
public class TabbedPanelDefaultButtonFactories {
  private static final FlatButtonFactory BUTTON_FACTORY = new FlatButtonFactory();

  /**
   * Returns the default scroll up button factory.
   *
   * @return the default scroll up button factory
   */
  public static ButtonFactory getScrollUpButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default scroll down button factory.
   *
   * @return the default scroll down button factory
   */
  public static ButtonFactory getScrollDownButtonFactory() {
    return BUTTON_FACTORY;
  }


  /**
   * Returns the default scroll left button factory.
   *
   * @return the default scroll left button factory
   */
  public static ButtonFactory getScrollLeftButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default scroll right button factory.
   *
   * @return the default scroll right button factory
   */
  public static ButtonFactory getScrollRightButtonFactory() {
    return BUTTON_FACTORY;
  }

  /**
   * Returns the default tab drop down list button factory.
   *
   * @return the default tab drop down list button factory
   */
  public static ButtonFactory getTabDropDownListButtonFactory() {
    return BUTTON_FACTORY;
  }
}
