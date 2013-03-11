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


// $Id: ComponentPainter.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.componentpainter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import net.infonode.util.Direction;

/**
 * <p>
 * Paints an area of a component.
 * </p>
 * <p>
 * Note: New methods might be added to this interface in the future. To ensure future compatibility inherit from
 * {@link AbstractComponentPainter} instead of directly implementing this interface.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.2.0
 */
public interface ComponentPainter {
  /**
   * Paints an area of a component. The area should be painted the same way as for direction Direction.RIGHT without
   * any flipping.
   *
   * @param component the component to paint on
   * @param g         the graphics to paint on
   * @param x         the x-coordinate
   * @param y         the y-coordinate
   * @param width     the width
   * @param height    the height
   */
  void paint(Component component, Graphics g, int x, int y, int width, int height);

  /**
   * Paints an area in a specific direction and optinally flipped horizontally and/or vertically. The flips are performed
   * before the rotation is applied.
   *
   * @param component      the component to paint on
   * @param g              the graphics to paint on
   * @param x              the x-coordinate
   * @param y              the y-coordinate
   * @param width          the width
   * @param height         the height
   * @param direction      the direction, Direction.RIGHT is the normal direction
   * @param horizontalFlip flip the painted graphics horizontally
   * @param verticalFlip   flip the painted graphics vertically
   */
  void paint(Component component, Graphics g, int x, int y, int width, int height, Direction direction,
             boolean horizontalFlip, boolean verticalFlip);

  /**
   * Returns true if this painter paints the entire area with an opaque color.
   *
   * @param component the component to paint on
   * @return true if this painter paints the entire area with an opaque color
   */
  boolean isOpaque(Component component);

  /**
   * Returns an approximate average color of the pixels painted by this painter.
   *
   * @param component the component to paint on
   * @return an approximate average color of the pixels painted by this painter
   */
  Color getColor(Component component);

}
