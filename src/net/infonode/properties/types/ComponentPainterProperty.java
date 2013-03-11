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


// $Id: ComponentPainterProperty.java,v 1.2 2011-08-26 15:10:43 mpue Exp $
package net.infonode.properties.types;

import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;

/**
 * A property of type {@link ComponentPainter}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class ComponentPainterProperty extends ValueHandlerProperty {
  public ComponentPainterProperty(PropertyGroup group,
                                  String name,
                                  String description,
                                  PropertyValueHandler valueStorage) {
    super(group, name, ComponentPainter.class, description, valueStorage);
  }

  /**
   * Returns the {@link ComponentPainter} value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the {@link ComponentPainter} value of this property
   */
  public ComponentPainter get(Object valueContainer) {
    return (ComponentPainter) getValue(valueContainer);
  }

  /**
   * Sets the {@link ComponentPainter} value of this property in a value container.
   *
   * @param valueContainer   the value container
   * @param componentPainter the {@link ComponentPainter} value
   */
  public void set(Object valueContainer, ComponentPainter componentPainter) {
    setValue(valueContainer, componentPainter);
  }

}
