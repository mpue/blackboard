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


// $Id: RoundedCornerBorder.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.shaped.border;

import java.awt.Component;
import java.awt.Insets;

import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.FixedColorProvider;

/**
 * @author johan
 */
public class RoundedCornerBorder extends PolygonBorder {
  private static final long serialVersionUID = 1;

  private static int[][] corner1 = {
    {0, 0},
    {0, 1, 1, 0},
    {0, 2, 2, 0},
    {0, 4, 1, 3, 1, 2, 2, 1, 3, 1, 4, 0},
    {0, 5, 1, 4, 1, 3, 3, 1, 4, 1, 5, 0}};

  private static int[][] corner2 = {
    {-1, 0},
    {-2, 0, -1, 1},
    {-3, 0, -1, 2},
    {-5, 0, -4, 1, -3, 1, -2, 2, -2, 3, -1, 4},
    {-6, 0, -5, 1, -4, 1, -2, 3, -2, 4, -1, 5}};

  private static int[][] corner3 = {
    {-1, -1},
    {-1, -2, -2, -1},
    {-1, -3, -3, -1},
    {-1, -5, -2, -4, -2, -3, -3, -2, -4, -2, -5, -1},
    {-1, -6, -2, -5, -2, -4, -4, -2, -5, -2, -6, -1}};

  private static int[][] corner4 = {
    {0, -1},
    {1, -1, 0, -2},
    {2, -1, 0, -3},
    {4, -1, 3, -2, 2, -2, 1, -3, 1, -4, 0, -5},
    {5, -1, 4, -2, 3, -2, 1, -4, 1, -5, 0, -6}};

  private Insets insets;

  public RoundedCornerBorder(ColorProvider lineColor, int cType) {
    this(lineColor, cType, true, true, true, true);
  }

  public RoundedCornerBorder(ColorProvider lineColor,
                             int cType,
                             boolean drawTop,
                             boolean drawLeft,
                             boolean drawBottom,
                             boolean drawRight) {
    this(lineColor, FixedColorProvider.WHITE, cType, cType, cType, cType, drawTop, drawLeft, drawBottom, drawRight);
  }

  public RoundedCornerBorder(ColorProvider lineColor, ColorProvider highlightColor,
                             int cType1, int cType2, int cType3, int cType4) {
    this(lineColor, highlightColor, cType1, cType2, cType3, cType4, true, true, true, true);
  }

  public RoundedCornerBorder(ColorProvider lineColor, ColorProvider highlightColor,
                             int cType1, int cType2, int cType3, int cType4,
                             boolean drawTop, boolean drawLeft, boolean drawBottom, boolean drawRight) {
    super(lineColor,
          highlightColor,
          createCoordinates(cType1, cType2, cType3, cType4, drawTop, drawLeft, drawBottom, drawRight),
          createXScales(cType1, cType2, cType3, cType4),
          createYScales(cType1, cType2, cType3, cType4));

    insets = new Insets(drawTop ? 1 : 0,
                        drawLeft ? 1 : 0,
                        drawBottom ? 1 : 0,
                        drawRight ? 1 : 0);
  }

  protected Insets getShapedBorderInsets(Component c) {
    return insets;
  }

  private static int[] createCoordinates(int cType1,
                                         int cType2,
                                         int cType3,
                                         int cType4,
                                         boolean drawTop,
                                         boolean drawLeft,
                                         boolean drawBottom,
                                         boolean drawRight) {
    int c1[] = corner1[cType1];
    int c2[] = corner2[cType2];
    int c3[] = corner3[cType3];
    int c4[] = corner4[cType4];

    int coords[] = new int[c1.length + c2.length + c3.length + c4.length];
    int index = 0;

    for (int i = 0; i < c1.length; i++) {
      coords[index] = drawLeft ? c1[i] : c1[i] - 1;
      index++;
      i++;
      coords[index] = drawTop ? c1[i] : c1[i] - 1;
      index++;
    }

    for (int i = 0; i < c2.length; i++) {
      coords[index] = drawRight ? c2[i] : c2[i] + 1;
      index++;
      i++;
      coords[index] = drawTop ? c2[i] : c2[i] - 1;
      index++;
    }

    for (int i = 0; i < c3.length; i++) {
      coords[index] = drawRight ? c3[i] : c3[i] + 1;
      index++;
      i++;
      coords[index] = drawBottom ? c3[i] : c3[i] + 1;
      index++;
    }

    for (int i = 0; i < c4.length; i++) {
      coords[index] = drawLeft ? c4[i] : c4[i] - 1;
      index++;
      i++;
      coords[index] = drawBottom ? c4[i] : c4[i] + 1;
      index++;
    }

    return coords;
  }

  private static float[] createXScales(int cType1, int cType2, int cType3, int cType4) {
    int c[][] = new int[4][1];
    c[0] = corner1[cType1];
    c[1] = corner2[cType2];
    c[2] = corner3[cType3];
    c[3] = corner4[cType4];

    float xscales[] = new float[c[0].length + c[1].length + c[2].length + c[3].length];
    int index = 0;

    for (int k = 0; k < c.length; k++) {
      for (int i = 0; i < c[k].length; i++) {
        xscales[index] = c[k][i] < 0 ? 1 : 0;
        i++;
        index += 2;
      }

    }

    return xscales;
  }

  private static float[] createYScales(int cType1, int cType2, int cType3, int cType4) {
    int c[][] = new int[4][1];
    c[0] = corner1[cType1];
    c[1] = corner2[cType2];
    c[2] = corner3[cType3];
    c[3] = corner4[cType4];

    float yscales[] = new float[c[0].length + c[1].length + c[2].length + c[3].length];
    int index = 1;

    for (int k = 0; k < c.length; k++) {
      for (int i = 1; i < c[k].length; i++) {
        yscales[index] = c[k][i] < 0 ? 1 : 0;
        i++;
        index += 2;
      }
    }

    return yscales;
  }
}
