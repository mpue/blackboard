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


// $Id: TabFactory.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel;

import javax.swing.Icon;
import javax.swing.JComponent;

import net.infonode.tabbedpanel.titledtab.TitledTab;

/**
 * Factory methods for creating different tabs
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see Tab
 * @see TitledTab
 */
public class TabFactory {
  private TabFactory() {
  }

  /**
   * Creates a TitledTab with a text and an icon
   *
   * @param text the text
   * @param icon the icon or null for no icon
   * @return the created TitledTab
   */
  public static TitledTab createTitledTab(String text, Icon icon) {
    return new TitledTab(text, icon, null, null);
  }

  /**
   * Creates a TitledTab with a text, an icon and a content component
   *
   * @param text             the text
   * @param icon             the icon or null for no icon
   * @param contentComponent the content component for the tab
   * @return the created TitledTab
   */
  public static TitledTab createTitledTab(String text, Icon icon, JComponent contentComponent) {
    return new TitledTab(text, icon, contentComponent, null);
  }

  /**
   * Creates a TitledTab with a text, an icon, a title component and a
   * content component
   *
   * @param text             the text
   * @param icon             the icon or null for no icon
   * @param contentComponent the content component for the tab
   * @param titleComponent   the title component for the tab
   * @return the created TitledTab
   */
  public static TitledTab createTitledTab(String text, Icon icon, JComponent contentComponent,
                                          JComponent titleComponent) {
    return new TitledTab(text, icon, contentComponent, titleComponent);
  }

  /**
   * Creates a TitledTab with a text, a different icon for each of the
   * states, a title component and a content component
   *
   * @param text             the text
   * @param icon             the icon for the normal state or null for no icon
   * @param highlightedIcon  the icon for the highlighted state or null fo no icon
   * @param disabledIcon     the icon for the disabled state or null for no icon
   * @param contentComponent the content component for the tab
   * @param titleComponent   the title component for the tab
   * @return the created TitledTab
   */
  public static TitledTab createTitledTab(String text, Icon icon, Icon highlightedIcon, Icon disabledIcon,
                                          JComponent contentComponent, JComponent titleComponent) {
    TitledTab tab = new TitledTab(text, icon, contentComponent, titleComponent);
    tab.getProperties().getHighlightedProperties().setIcon(highlightedIcon);
    tab.getProperties().getDisabledProperties().setIcon(disabledIcon);
    return tab;
  }
}
