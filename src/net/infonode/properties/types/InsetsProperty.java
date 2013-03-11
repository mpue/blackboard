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


// $Id: InsetsProperty.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.properties.types;

import java.awt.Insets;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;

/**
 * A property of type {@link Insets}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class InsetsProperty extends ValueHandlerProperty {
  public InsetsProperty(PropertyGroup group, String name, String description, PropertyValueHandler valueStorage) {
    super(group, name, Insets.class, description, valueStorage);
  }

  /**
   * Returns the insets value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the insets value of this property
   */
  public Insets get(Object valueContainer) {
    return (Insets) getValue(valueContainer);
  }

  /**
   * Sets the insets value of this property in a value container.
   *
   * @param valueContainer the value container
   * @param insets         the insets value
   */
  public void set(Object valueContainer, Insets insets) {
    setValue(valueContainer, insets);
  }

}
