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


// $Id: BackgroundPainterColorProvider.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.gui.colorprovider;

import java.awt.Color;
import java.awt.Component;
import java.io.ObjectStreamException;

import javax.swing.UIManager;

import net.infonode.gui.ComponentUtil;

/**
 * Finds the most suitable background color of a component.
 * If the component has a {@link net.infonode.gui.componentpainter.ComponentPainter}
 * that paint its background, like for example a shaped panel, the
 * color is taken from this painter, otherwise the component background color is used.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class BackgroundPainterColorProvider extends AbstractColorProvider {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class.
   */
  public static final BackgroundPainterColorProvider INSTANCE = new BackgroundPainterColorProvider();

  private BackgroundPainterColorProvider() {
  }

  public Color getColor(Component component) {
    Color color = ComponentUtil.getBackgroundColor(component);
    return color == null ? UIManager.getColor("control") : color;
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
