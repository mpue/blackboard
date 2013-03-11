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


// $Id: DimensionUtil.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Dimension;
import java.awt.Insets;

import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class DimensionUtil {
  public static final Dimension ZERO = new Dimension(0, 0);

  public static Dimension min(Dimension d1, Dimension d2) {
    return new Dimension(Math.min((int) d1.getWidth(), (int) d2.getWidth()),
                         Math.min((int) d1.getHeight(), (int) d2.getHeight()));
  }

  public static Dimension max(Dimension d1, Dimension d2) {
    return new Dimension(Math.max((int) d1.getWidth(), (int) d2.getWidth()),
                         Math.max((int) d1.getHeight(), (int) d2.getHeight()));
  }

  public static Dimension getInnerDimension(Dimension d, Insets i) {
    return new Dimension((int) (d.getWidth() - i.left - i.right),
                         (int) (d.getHeight() - i.top - i.bottom));
  }

  public static Dimension add(Dimension dim, Insets insets) {
    return new Dimension(dim.width + insets.left + insets.right, dim.height + insets.top + insets.bottom);
  }

  public static Dimension add(Dimension d1, Dimension d2, boolean isHorizontalAdd) {
    return new Dimension(isHorizontalAdd ? (d1.width + d2.width) : Math.max(d1.width, d2.width),
                         isHorizontalAdd ? Math.max(d1.height, d2.height) : d1.height + d2.height);
  }

  public static Dimension rotate(Direction source, Dimension d, Direction destination) {
    if (source.isHorizontal() && destination.isHorizontal())
      return d;

    return new Dimension(d.height, d.width);
  }
}
