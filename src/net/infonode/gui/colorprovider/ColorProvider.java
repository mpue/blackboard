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


// $Id: ColorProvider.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.gui.colorprovider;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;

/**
 * An object that provides colors. Instances of this class is typically used by {@link javax.swing.border.Border}
 * implementations to obtain border colors which might change depending on the current Look and Feel or which component
 * the border is painted on.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public interface ColorProvider extends Serializable {
  /**
   * Returns the default color when no component is available.
   *
   * @return the default color when no component is available
   */
  Color getColor();

  /**
   * Returns the color obtained from the given component.
   *
   * @param component the component
   * @return the color obtained from the given component
   */
  Color getColor(Component component);
}
