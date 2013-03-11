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


// $Id: SlopedTabLineBorder.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.tabbedpanel.internal;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Polygon;

import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.gui.shaped.border.AbstractPolygonBorder;

/**
 * @author johan
 * @since 1.2.0
 */
public class SlopedTabLineBorder extends AbstractPolygonBorder {
  private static final long serialVersionUID = 1;

  private static final int[][][] corners = {
    {
      {0, 0},
      {-1, 0, 0, -1},
      {-2, 0, -1, -1, 0, -1},
      {-4, 0, -3, -1, -2, -1, -1, -2, -1, -3, 0, -4},
    },
    {
      {0, 0},
      {0, 0},
      {0, 1, 1, 1, 2, 0},
      {0, 4, 1, 3, 1, 2, 2, 1, 3, 1, 4, 0},
    },
    {
      {0, 0},
      {0, 0},
      {-2, 0, -1, 1, 0, 1},
      {-4, 0, -3, 1, -2, 1, -1, 2, -1, 3, 0, 4}
    },
    {
      {0, 0},
      {0, -1, 1, 0},
      {0, -1, 1, -1, 2, 0},
      {0, -4, 1, -3, 1, -2, 2, -1, 3, -1, 4, 0},
    },
    {
      {0, 0},
      {0, 0},
      {0, 2, 1, 1, 1, 0},
      {0, 4, 1, 3, 1, 2, 2, 1, 3, 1, 4, 0},
    },
    {
      {0, 0},
      {0, 0},
      {-1, 0, -1, 1, 0, 2},
      {-4, 0, -3, 1, -2, 1, -1, 2, -1, 3, 0, 4},
    },
  };

  private boolean drawBottomLine;
  private float leftSlope;
  private float rightSlope;
  private boolean bottomLeftRounded;
  private boolean topLeftRounded;
  private boolean topRightRounded;
  private boolean bottomRightRounded;
  private int leftHeight;
  private int rightHeight;

  private static int[] xCoords = new int[100];
  private static int[] yCoords = new int[100];
  private static int x;
  private static int y;
  private static int index;

  public SlopedTabLineBorder() {
    this(0, 1);
  }

  public SlopedTabLineBorder(float leftSlope, float rightSlope) {
    this(leftSlope, rightSlope, 22, 22);
  }

  public SlopedTabLineBorder(float leftSlope, float rightSlope, int leftHeight, int rightHeight) {
    this(leftSlope, rightSlope, leftHeight, rightHeight, false, false, false, false);
  }

  public SlopedTabLineBorder(float leftSlope, float rightSlope,
                             boolean bottomLeftRounded, boolean topLeftRounded, boolean topRightRounded,
                             boolean bottomRightRounded) {
    this(leftSlope,
         rightSlope,
         22,
         22,
         bottomLeftRounded,
         topLeftRounded,
         topRightRounded,
         bottomRightRounded);
  }

  public SlopedTabLineBorder(float leftSlope, float rightSlope, int leftHeight, int rightHeight,
                             boolean bottomLeftRounded, boolean topLeftRounded, boolean topRightRounded,
                             boolean bottomRightRounded) {
    this(UIManagerColorProvider.TABBED_PANE_DARK_SHADOW,
         UIManagerColorProvider.TABBED_PANE_HIGHLIGHT,
         false,
         leftSlope,
         rightSlope,
         leftHeight,
         rightHeight,
         bottomLeftRounded,
         topLeftRounded,
         topRightRounded,
         bottomRightRounded);
  }

  public SlopedTabLineBorder(ColorProvider lineColor, ColorProvider highlightColor, boolean drawBottomLine,
                             float leftSlope, float rightSlope, int leftHeight, int rightHeight,
                             boolean bottomLeftRounded, boolean topLeftRounded, boolean topRightRounded,
                             boolean bottomRightRounded) {
    super(lineColor, highlightColor);
    this.drawBottomLine = drawBottomLine;
    this.leftHeight = leftHeight;
    this.rightHeight = rightHeight;
    this.leftSlope = leftSlope;
    this.rightSlope = rightSlope;
    this.bottomLeftRounded = bottomLeftRounded;
    this.topLeftRounded = topLeftRounded;
    this.topRightRounded = topRightRounded;
    this.bottomRightRounded = bottomRightRounded;
  }

  protected boolean lineIsDrawn(int index, Polygon polygon) {
    return drawBottomLine || index < polygon.npoints - 1;
  }

  protected Insets getShapedBorderInsets(Component c) {
    return new Insets(1,
                      (isBottomLeftRounded(c) ? 4 : 1) +
                      (topLeftRounded ? (leftSlope <= 0.5f ? 4 : 1) : 0) +
                      (int) (leftSlope * leftHeight),
                      drawBottomLine ? 1 : 0,
                      (bottomRightRounded ? 4 : 1) +
                      (topRightRounded ? (rightSlope <= 0.5f ? 4 : 1) : 0) +
                      (int) (rightSlope * rightHeight));
  }

  protected boolean isBottomLeftRounded(Component c) {
    return bottomLeftRounded;
  }

  private static int[] getCorner(int type, float slope, boolean rounded) {
    return corners[type][!rounded ? 0 :
                         type < 4 ?
                         (slope >= 2f ? 1 : slope >= 1f ? 2 : 3) :
                         (slope <= 0.5f ? 1 : slope <= 1f ? 2 : 3)];
  }

  private static void addPoint(int px, int py) {
    xCoords[index] = px;
    yCoords[index++] = py;
    x = px;
    y = py;
  }

  private static void addCorner(int px, int py, int[] c) {
    for (int i = 0; i < c.length; i++) {
      addPoint(px + c[i++], py + c[i]);
    }
  }

  private static int getStartY(int[] corner) {
    return corner[1];
  }

  private static int getEndY(int[] corner) {
    return corner[corner.length - 1];
  }

  protected Polygon createPolygon(Component c, int width, int height) {
    boolean bottomLeftRounded = isBottomLeftRounded(c);
    int leftX = (int) (leftHeight * leftSlope + (bottomLeftRounded ? 4 : 0));
    int rightX = width - 1 - (int) (rightHeight * rightSlope + (bottomRightRounded ? 4 : 0));
    int bottomY = height - (drawBottomLine ? 1 : 0);

    int[] topLeft = getCorner(1, leftSlope, topLeftRounded);
    int[] topRight = getCorner(2, rightSlope, topRightRounded);
    int[] bottomLeft = getCorner(0, leftSlope, bottomLeftRounded);
    int[] bottomRight = getCorner(3, rightSlope, bottomRightRounded);

    index = 0;
    y = bottomY;
    int dy = height - getStartY(topLeft) + getEndY(bottomLeft);

    if (dy <= leftHeight) {
      x = leftX - (int) (dy * leftSlope);
      addCorner(x, y, bottomLeft);
    }
    else {
      x = leftX - (int) (leftHeight * leftSlope);
      addCorner(x, y, getCorner(0, 0, bottomLeftRounded));
      addPoint(x, getStartY(topLeft) + leftHeight);
    }

    addCorner(leftX, 0, topLeft);
    addCorner(rightX, 0, topRight);
    dy = height - getEndY(topRight) + getStartY(bottomRight);

    if (dy <= rightHeight) {
      addCorner((int) (rightX + dy * rightSlope), bottomY, bottomRight);
    }
    else {
      addPoint((int) (rightX + rightSlope * rightHeight), getEndY(topRight) + rightHeight);
      addCorner(x, bottomY, getCorner(3, 0, bottomRightRounded));
    }

    return new Polygon(xCoords, yCoords, index);
  }

}