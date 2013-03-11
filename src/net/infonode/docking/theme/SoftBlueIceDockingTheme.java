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


// $Id: SoftBlueIceDockingTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.theme;

import java.awt.Color;
import java.awt.Insets;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.gui.colorprovider.ColorBlender;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.componentpainter.SolidColorComponentPainter;
import net.infonode.gui.shaped.border.FixedInsetsShapedBorder;
import net.infonode.gui.shaped.border.ShapedBorder;
import net.infonode.tabbedpanel.TabAreaProperties;
import net.infonode.tabbedpanel.theme.SoftBlueIceTheme;

/**
 * A light blue theme with gradients and rounded corners.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class SoftBlueIceDockingTheme extends DockingWindowsTheme {
  private RootWindowProperties rootWindowProperties = new RootWindowProperties();
  private boolean slim;

  /**
   * Create a theme with default settings.
   */
  public SoftBlueIceDockingTheme() {
    this(false);
  }

  /**
   * Constructor.
   *
   * @param slim if true there is less spacing in the tab area
   */
  public SoftBlueIceDockingTheme(boolean slim) {
    this(SoftBlueIceTheme.DEFAULT_DARK_COLOR, SoftBlueIceTheme.DEFAULT_LIGHT_COLOR, 4, slim);
  }

  /**
   * Constructor.
   *
   * @param darkColor  the dark color used in the gradients
   * @param lightColor the light color used in the gradients
   * @param cornerType how much rounding to apply to corners
   * @param slim       if true there is less spacing in the tab area
   */
  public SoftBlueIceDockingTheme(ColorProvider darkColor, ColorProvider lightColor, int cornerType, boolean slim) {
    SoftBlueIceTheme theme = new SoftBlueIceTheme(darkColor, lightColor, cornerType);
    this.slim = slim;

    if (slim) {
      theme.getTabbedPanelProperties().getTabAreaProperties().getComponentProperties()
          .setBorder(new FixedInsetsShapedBorder(
              new Insets(0, 0, 0, 0),
              (ShapedBorder) theme.getTabbedPanelProperties().getTabAreaProperties().getComponentProperties()
                  .getBorder()));

      theme.getTabbedPanelProperties().getTabAreaProperties().getComponentProperties().setInsets(
          new Insets(0, 0, 0, 0));
      theme.getTabbedPanelProperties().setTabSpacing(-1);
      theme.getTabbedPanelProperties().getTabAreaComponentsProperties().getComponentProperties().setInsets(
          new Insets(2, 2, 2, 2));
    }

    rootWindowProperties.getWindowAreaProperties()
        .setBorder(null)
        .setInsets(new Insets(2, 2, 2, 2));
    rootWindowProperties.getWindowAreaShapedPanelProperties().setComponentPainter(
        new SolidColorComponentPainter(new ColorBlender(darkColor, lightColor, 0.5f)));

    rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().addSuperObject(
        theme.getTabbedPanelProperties());
    rootWindowProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().addSuperObject(
        theme.getTitledTabProperties());

    rootWindowProperties.getShapedPanelProperties().
        setComponentPainter(theme.getTabbedPanelProperties().getTabAreaProperties().getShapedPanelProperties().
            getComponentPainter());

    rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties()
        .getShapedPanelProperties()
        .setClipChildren(true);
    rootWindowProperties.getViewProperties().getViewTitleBarProperties().getNormalProperties().getComponentProperties()
        .setForegroundColor(Color.BLACK)
        .setInsets(new Insets(0, 2, 0, 2));
    rootWindowProperties.getViewProperties().getViewTitleBarProperties().getNormalProperties()
        .getShapedPanelProperties()
        .setComponentPainter(null)
        .setOpaque(false);
    rootWindowProperties.getViewProperties().getViewTitleBarProperties().getFocusedProperties()
        .getShapedPanelProperties()
        .setComponentPainter(null);//.setOpaque(false);
    rootWindowProperties.getViewProperties().getViewTitleBarProperties().getFocusedProperties().getComponentProperties()
        .setForegroundColor(Color.BLACK);

    TabAreaProperties p = rootWindowProperties.getWindowBarProperties().getTabWindowProperties().
        getTabbedPanelProperties().getTabAreaProperties();

    p.getShapedPanelProperties().setComponentPainter(null);
    p.getComponentProperties().setBorder(null);

    rootWindowProperties.getWindowBarProperties().getTabWindowProperties().
        getTabbedPanelProperties().getTabAreaComponentsProperties().getComponentProperties().setBorder(null);

    rootWindowProperties.getWindowBarProperties().getTabWindowProperties().
        getTabbedPanelProperties().getContentPanelProperties().getShapedPanelProperties().setOpaque(false);
  }

  public String getName() {
    return "Soft Blue Ice Theme" + (slim ? " - Slim" : "");
  }

  public RootWindowProperties getRootWindowProperties() {
    return rootWindowProperties;
  }
}
