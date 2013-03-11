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


// $Id: TabAreaLineBorder.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.border.Border;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.ColorProviderList;
import net.infonode.gui.colorprovider.ColorProviderUtil;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabAreaComponentsProperties;
import net.infonode.tabbedpanel.TabAreaProperties;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.util.Direction;

/**
 * TabAreaLineBorder draws a 1 pixel wide border on all sides except the side towards
 * the content area of a tabbed panel.
 *
 * @author $Author: mpue $
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see Tab
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @see TabAreaProperties
 * @see TabAreaComponentsProperties
 * @since ITP 1.1.0
 */
public class TabAreaLineBorder implements Border, Serializable {
  private static final long serialVersionUID = 1;

  private ColorProvider color;
  private boolean drawTop;
  private boolean drawLeft;
  private boolean drawRight;
  private boolean flipLeftRight;

  /**
   * Constructs a TabAreaLineBorder with color based on the look and feel
   */
  public TabAreaLineBorder() {
    this(null);
  }

  /**
   * Constructs a TabAreaLineBorder with the give color
   *
   * @param color color for the border
   */
  public TabAreaLineBorder(Color color) {
    this(color, true, true, true, false);
  }

  /**
   * Constructor.
   *
   * @param drawTop       draw the top line
   * @param drawLeft      draw the left line
   * @param drawRight     draw the right line
   * @param flipLeftRight if true the left line is rotated so that it is always to the left or at the top and
   *                      vice versa for the right line, if false the left and right lines are rotated the same way as
   *                      the other lines
   */
  public TabAreaLineBorder(boolean drawTop, boolean drawLeft, boolean drawRight, boolean flipLeftRight) {
    this((Color) null, drawTop, drawLeft, drawRight, flipLeftRight);
  }

  /**
   * Constructor.
   *
   * @param color         the line color
   * @param drawTop       draw the top line
   * @param drawLeft      draw the left line
   * @param drawRight     draw the right line
   * @param flipLeftRight if true the left line is rotated so that it is always to the left or at the top and
   *                      vice versa for the right line, if false the left and right lines are rotated the same way as
   *                      the other lines
   */
  public TabAreaLineBorder(Color color, boolean drawTop, boolean drawLeft, boolean drawRight, boolean flipLeftRight) {
    this(ColorProviderUtil.getColorProvider(color, new ColorProviderList(
        UIManagerColorProvider.TABBED_PANE_DARK_SHADOW,
        UIManagerColorProvider.CONTROL_DARK_SHADOW,
        FixedColorProvider.BLACK)),
         drawTop, drawLeft, drawRight, flipLeftRight);
  }

  /**
   * Constructor.
   *
   * @param colorProvider the line color provider
   * @param drawTop       draw the top line
   * @param drawLeft      draw the left line
   * @param drawRight     draw the right line
   * @param flipLeftRight if true the left line is rotated so that it is always to the left or at the top and
   *                      vice versa for the right line, if false the left and right lines are rotated the same way as
   *                      the other lines
   */
  public TabAreaLineBorder(ColorProvider colorProvider, boolean drawTop, boolean drawLeft, boolean drawRight,
                           boolean flipLeftRight) {
    this.color = colorProvider;
    this.drawTop = drawTop;
    this.drawLeft = drawLeft;
    this.drawRight = drawRight;
    this.flipLeftRight = flipLeftRight;
  }

  public boolean isBorderOpaque() {
    return true;
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Insets insets = getBorderInsets(c);
    g.setColor(color.getColor(c));

    if (insets.top == 1)
      GraphicsUtil.drawOptimizedLine(g, x, y, x + width - 1, y);

    if (insets.bottom == 1)
      GraphicsUtil.drawOptimizedLine(g, x, y + height - 1, x + width - 1, y + height - 1);

    if (insets.left == 1)
      GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - 1);

    if (insets.right == 1)
      GraphicsUtil.drawOptimizedLine(g, x + width - 1, y, x + width - 1, y + height - 1);
  }

  private boolean drawTop(Direction orientation) {
    return orientation == Direction.UP ? drawTop :
           orientation == Direction.LEFT ? (flipLeftRight ? drawLeft : drawRight) :
           orientation == Direction.RIGHT ? drawLeft :
           false;
  }

  private boolean drawLeft(Direction orientation) {
    return orientation == Direction.UP ? drawLeft :
           orientation == Direction.LEFT ? drawTop :
           orientation == Direction.DOWN ? (flipLeftRight ? drawLeft : drawRight) :
           false;
  }

  private boolean drawRight(Direction orientation) {
    return orientation == Direction.UP ? drawRight :
           orientation == Direction.LEFT ? false :
           orientation == Direction.DOWN ? (flipLeftRight ? drawRight : drawLeft) :
           drawTop;
  }

  private boolean drawBottom(Direction orientation) {
    return orientation == Direction.UP ? false :
           orientation == Direction.LEFT ? (flipLeftRight ? drawRight : drawLeft) :
           orientation == Direction.RIGHT ? drawRight :
           drawTop;
  }

  public Insets getBorderInsets(Component c) {
    if (c instanceof JComponent && ((JComponent) c).getComponentCount() == 0)
      return new Insets(0, 0, 0, 0);

    Direction orientation = getTabAreaDirection(c);

    return orientation != null ? new Insets(drawTop(orientation) ? 1 : 0,
                                            drawLeft(orientation) ? 1 : 0,
                                            drawBottom(orientation) ? 1 : 0,
                                            drawRight(orientation) ? 1 : 0) :
           new Insets(0, 0, 0, 0);
  }

  private static Direction getTabAreaDirection(Component c) {
    TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
    return tp != null ? tp.getProperties().getTabAreaOrientation() : null;

  }

}
