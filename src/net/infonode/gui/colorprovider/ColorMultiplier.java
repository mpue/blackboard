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


// $Id: ColorMultiplier.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.gui.colorprovider;

import java.awt.Color;
import java.awt.Component;

import net.infonode.util.ColorUtil;

/**
 * Multiplies the RGB components of a color with the given factor.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class ColorMultiplier extends AbstractColorProvider {
  private static final long serialVersionUID = 1;

  private final ColorProvider colorProvider;
  private final double factor;

  /**
   * Constructor.
   *
   * @param colorProvider provides the color which RGB components will be multiplied
   * @param factor        the multiply factor
   */
  public ColorMultiplier(ColorProvider colorProvider, double factor) {
    this.colorProvider = colorProvider;
    this.factor = factor;
  }

  public Color getColor(Component component) {
    return ColorUtil.mult(colorProvider.getColor(component), factor);
  }

  public Color getColor() {
    return ColorUtil.mult(colorProvider.getColor(), factor);
  }
}
