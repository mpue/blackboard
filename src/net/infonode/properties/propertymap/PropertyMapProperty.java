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


// $Id: PropertyMapProperty.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.properties.propertymap;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.types.PropertyGroupProperty;

/**
 * An immutable property which has {@link PropertyMap}'s as values.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class PropertyMapProperty extends PropertyGroupProperty {
  /**
   * Constructor.
   *
   * @param group         the property group
   * @param name          the property name
   * @param description   the property description
   * @param propertyGroup property maps for this property group can be values for this property
   */
  public PropertyMapProperty(PropertyGroup group, String name, String description, PropertyMapGroup propertyGroup) {
    super(group, name, PropertyMap.class, description, PropertyMapValueHandler.INSTANCE, propertyGroup);
  }

  /**
   * Returns the property group which property maps can be used as values for this property.
   *
   * @return the property group which property maps can be used as values for this property
   */
  public PropertyMapGroup getPropertyMapGroup() {
    return (PropertyMapGroup) getPropertyGroup();
  }

  public boolean isMutable() {
    return false;
  }

  public Object getValue(Object object) {
    return ((PropertyMapImpl) object).getChildMapImpl(this);
  }

  /**
   * Return the property valueContainer value for this property in the value container.
   *
   * @param valueContainer the value container
   * @return the property valueContainer value for this property in the value container
   */
  public PropertyMap get(Object valueContainer) {
    return (PropertyMap) getValue(valueContainer);
  }

}
