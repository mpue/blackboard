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


// $Id: PropertiesUtil.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.docking.util;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.tabbedpanel.TabAreaVisiblePolicy;
import net.infonode.tabbedpanel.TabLayoutPolicy;
import net.infonode.util.Direction;

/**
 * Utility functions for manipulating properties.
 *
 * @author johan
 * @since IDW 1.4.0
 */
public class PropertiesUtil {
  private PropertiesUtil() {
  }

  /**
   * <p>
   * Creates and returns a new {@link RootWindowProperties} object that is meant to be
   * added as super object on another {@link RootWindowProperties} object, for example
   * a theme's {@link RootWindowProperties}.
   * </p>
   *
   * <p>
   * The created properties object will have title bar style properties set, see
   * {@link #setTitleBarStyle(RootWindowProperties)}.
   * </p>
   *
   * @return created properties object
   */
  public static RootWindowProperties createTitleBarStyleRootWindowProperties() {
    RootWindowProperties titleBarStyleProperties = new RootWindowProperties();
    setupTitleBarStyleProperties(titleBarStyleProperties);
    return titleBarStyleProperties;
  }

  /**
   * <p>
   * Sets title bar style in the given root window properties object.
   * </p>
   *
   * <p>
   * This function sets properties in the give {@link RootWindowProperties} object:
   * </p>
   * <ul>
   * <li>View title bars are made visible at the top of a view.</li>
   * <li>Tab area is oriented below the content area.</li>
   * <li>No tab buttons are visible (except custom buttons).</li>
   * <li>No tab window buttons except scroll buttons, drop down list button
   * and custom buttons.</li>
   * <li>Hide the entire tab area when a tab window contains a view as the
   * only child.</li>
   * </ul>
   *
   * <p>
   * <strong>Note:</strong> It will modify properties values in the object
   * without checking if it was already set i.e. overwriting the previous
   * value.
   * </p>
   *
   * @param rootProps {@link RootWindowProperties} object to modify
   */
  public static void setTitleBarStyle(RootWindowProperties rootProps) {
    setupTitleBarStyleProperties(rootProps);
  }

  private static void setupTitleBarStyleProperties(RootWindowProperties titleBarStyleProperties) {
    titleBarStyleProperties.getViewProperties().getViewTitleBarProperties().setVisible(true);
    titleBarStyleProperties.getTabWindowProperties().getTabbedPanelProperties().setTabAreaOrientation(Direction.DOWN)
        .setTabLayoutPolicy(TabLayoutPolicy.SCROLLING);
    titleBarStyleProperties.getTabWindowProperties().getTabbedPanelProperties().getTabAreaProperties()
        .setTabAreaVisiblePolicy(TabAreaVisiblePolicy.MORE_THAN_ONE_TAB);

    titleBarStyleProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getMinimizeButtonProperties()
        .setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getRestoreButtonProperties()
        .setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getCloseButtonProperties()
        .setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getUndockButtonProperties()
        .setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getDockButtonProperties()
        .setVisible(false);

    titleBarStyleProperties.getTabWindowProperties().getCloseButtonProperties().setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getMaximizeButtonProperties().setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getMinimizeButtonProperties().setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getUndockButtonProperties().setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getDockButtonProperties().setVisible(false);
    titleBarStyleProperties.getTabWindowProperties().getRestoreButtonProperties().setVisible(false);

    titleBarStyleProperties.getWindowBarProperties().getTabWindowProperties().getTabProperties()
        .getHighlightedButtonProperties()
        .getMinimizeButtonProperties()
        .setVisible(true);
    titleBarStyleProperties.getWindowBarProperties().getTabWindowProperties().getTabProperties()
        .getHighlightedButtonProperties()
        .getRestoreButtonProperties()
        .setVisible(true);
    titleBarStyleProperties.getWindowBarProperties().getTabWindowProperties().getTabProperties()
        .getHighlightedButtonProperties()
        .getCloseButtonProperties()
        .setVisible(true);
    titleBarStyleProperties.getWindowBarProperties().getTabWindowProperties().getTabProperties()
        .getHighlightedButtonProperties()
        .getUndockButtonProperties()
        .setVisible(true);
    titleBarStyleProperties.getWindowBarProperties().getTabWindowProperties().getTabProperties()
        .getHighlightedButtonProperties()
        .getDockButtonProperties()
        .setVisible(true);

    titleBarStyleProperties.getWindowBarProperties().getTabWindowProperties().getTabbedPanelProperties()
        .setTabLayoutPolicy(TabLayoutPolicy.SCROLLING);
  }
}
