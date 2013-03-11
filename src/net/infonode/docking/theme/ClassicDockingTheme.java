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


// $Id: ClassicDockingTheme.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.theme;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.gui.border.EdgeBorder;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.tabbedpanel.TabSelectTrigger;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.tabbedpanel.theme.ClassicTheme;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.util.Direction;

/**
 * A theme with a "classic" look with round edges for the tabs.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.2.0
 */
public class ClassicDockingTheme extends DockingWindowsTheme {
  private RootWindowProperties rootWindowProperties = new RootWindowProperties();

  /**
   * Creates a ClassicDockingTheme.
   */
  public ClassicDockingTheme() {
    ClassicTheme theme = new ClassicTheme();

    TabbedPanelProperties tabbedPanelProperties = theme.getTabbedPanelProperties();
    TitledTabProperties titledTabProperties = theme.getTitledTabProperties();

    Border insetsBorder = new Border() {
      public boolean isBorderOpaque() {
        return false;
      }

      public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      }

      public Insets getBorderInsets(Component c) {
        TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
        if (tp != null) {
          Direction d = tp.getProperties().getTabAreaOrientation();
          return new Insets(d == Direction.UP ? 2 : 0,
                            d == Direction.LEFT ? 2 : 0,
                            d == Direction.DOWN ? 2 : 0,
                            d == Direction.RIGHT ? 2 : 0);
        }
        return new Insets(0, 0, 0, 0);
      }
    };

    // Tab window
    rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().setTabSelectTrigger(
        TabSelectTrigger.MOUSE_PRESS)
        .addSuperObject(tabbedPanelProperties)
        .getTabAreaComponentsProperties().getComponentProperties().setInsets(new Insets(0, 0, 0, 0));
    rootWindowProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().addSuperObject(
        titledTabProperties);

    // Window bar
    rootWindowProperties.getWindowBarProperties().getTabWindowProperties().getTabbedPanelProperties().addSuperObject(
        tabbedPanelProperties);
    rootWindowProperties.getWindowBarProperties().getTabWindowProperties().getTabProperties().getTitledTabProperties()
        .addSuperObject(titledTabProperties);

    rootWindowProperties.getWindowBarProperties().getTabWindowProperties().getTabbedPanelProperties()
        .getTabAreaComponentsProperties()
        .getComponentProperties()
        .setBorder(new CompoundBorder(insetsBorder, theme.createTabBorder(true, false, true)));

    rootWindowProperties.getWindowBarProperties()
        .getComponentProperties()
        .setInsets(new Insets(0, 0, 2, 0));

    rootWindowProperties.getWindowBarProperties()
        .getTabWindowProperties()
        .getTabProperties()
        .getTitledTabProperties()
        .getNormalProperties()
        .getComponentProperties()
        .setBorder(theme.createInsetsTabBorder(true, false, true));

    // Tweak root window
    rootWindowProperties.getWindowAreaProperties().setBackgroundColor(null).setInsets(new Insets(0, 0, 0, 0))
        .setBorder(new EdgeBorder(UIManagerColorProvider.TABBED_PANE_DARK_SHADOW,
                                  UIManagerColorProvider.TABBED_PANE_HIGHLIGHT,
                                  true,
                                  true,
                                  true,
                                  true));
    rootWindowProperties.setDragRectangleBorderWidth(3);
    rootWindowProperties.getViewProperties().getViewTitleBarProperties().getNormalProperties()
        .getShapedPanelProperties()
        .setDirection(Direction.DOWN);
  }

  /**
   * Gets the theme name
   *
   * @return name
   */
  public String getName() {
    return "Classic Theme";
  }

  /**
   * Gets the theme RootWindowProperties
   *
   * @return the RootWindowProperties
   */
  public RootWindowProperties getRootWindowProperties() {
    return rootWindowProperties;
  }
}
