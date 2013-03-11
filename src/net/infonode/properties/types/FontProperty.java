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


// $Id: FontProperty.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.properties.types;

import java.awt.Font;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.util.PropertyValueHandler;
import net.infonode.properties.util.ValueHandlerProperty;

/**
 * A property of type {@link Font}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class FontProperty extends ValueHandlerProperty {
  /**
   * Constructor.
   *
   * @param group        the property group
   * @param name         the property name
   * @param description  the property description
   * @param valueHandler handles values for this property
   */
  public FontProperty(PropertyGroup group, String name, String description, PropertyValueHandler valueHandler) {
    super(group, name, Font.class, description, valueHandler);
  }

  /**
   * Returns the font value of this property in a value container.
   *
   * @param valueContainer the value container
   * @return the font value of this property
   */
  public Font get(Object valueContainer) {
    return (Font) getValue(valueContainer);
  }

  /**
   * Sets the font value of this property in a value container.
   *
   * @param valueContainer the value container
   * @param font           the font value
   */
  public void set(Object valueContainer, Font font) {
    setValue(valueContainer, font);
  }

}
