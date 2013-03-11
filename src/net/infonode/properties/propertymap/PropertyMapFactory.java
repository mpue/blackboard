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


// $Id: PropertyMapFactory.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.properties.propertymap;

/**
 * Contains factory methods for {@link PropertyMap}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class PropertyMapFactory {
  private PropertyMapFactory() {
  }

  /**
   * Creates a property map that can contain values for properties in the given property group.
   *
   * @param propertyGroup the property group
   * @return a new property map
   */
  public static PropertyMap create(PropertyMapGroup propertyGroup) {
    return new PropertyMapImpl(propertyGroup);
  }

  /**
   * Creates a property map with the same property group as <tt>inheritFrom</tt>.
   * <tt>inheritFrom</tt> is added as a super map to the created property map.
   *
   * @param inheritFrom the super map
   * @return a new property map
   */
  public static PropertyMap create(PropertyMap inheritFrom) {
    return new PropertyMapImpl((PropertyMapImpl) inheritFrom);
  }
}
