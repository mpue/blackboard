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


// $Id: GradientTabAreaBorder.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;

import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.ColorProviderUtil;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedUtils;

/**
 * Paints a gradient background for a tab area component. The background blends from one color on the component edge
 * opposite to the tabbed panel content panel to the another color on the component edge closest to the tabbed panel
 * content panel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since ITP 1.1.0
 */
public class GradientTabAreaBorder implements Border, Serializable {
  private static final long serialVersionUID = 1;

  private GradientComponentPainter painter;

  /**
   * Creates a border where the color of the component edge closest to the tabben panel content panel will be the
   * default control color.
   *
   * @param topColor the color of the component edge opposite to the tabbed panel content panel
   */
  public GradientTabAreaBorder(Color topColor) {
    this(topColor, null);
  }

  /**
   * Constructor.
   *
   * @param topColor    the color of the component edge opposite to the tabbed panel content panel
   * @param bottomColor the color of the component edge closest to the tabbed panel content panel
   */
  public GradientTabAreaBorder(Color topColor, Color bottomColor) {
    this(ColorProviderUtil.getColorProvider(topColor, UIManagerColorProvider.CONTROL_COLOR),
         ColorProviderUtil.getColorProvider(bottomColor, UIManagerColorProvider.CONTROL_COLOR));
  }

  /**
   * Constructor.
   *
   * @param topColorProvider    provides the color of the component edge opposite to the tabbed panel content panel
   * @param bottomColorProvider provides the color of the component edge closest to the tabbed panel content panel
   */
  public GradientTabAreaBorder(ColorProvider topColorProvider, ColorProvider bottomColorProvider) {
    painter = new GradientComponentPainter(topColorProvider,
                                           topColorProvider,
                                           bottomColorProvider,
                                           bottomColorProvider);
  }

  public boolean isBorderOpaque() {
    return true;
  }

  public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
    TabbedPanel tp = TabbedUtils.getParentTabbedPanel(component);

    if (tp == null)
      return;

    painter.paint(component,
                  g,
                  x,
                  y,
                  width,
                  height,
                  tp.getProperties().getTabAreaOrientation().getNextCW(),
                  false,
                  false);
  }

  public Insets getBorderInsets(Component c) {
    return new Insets(0, 0, 0, 0);
  }
}
