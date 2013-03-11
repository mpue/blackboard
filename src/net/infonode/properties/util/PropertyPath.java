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


// $Id: PropertyPath.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.properties.util;

import net.infonode.properties.base.Property;

/**
 * A path to a property.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class PropertyPath {
  private Property property;
  private PropertyPath tail;

  /**
   * Creates a path containing a single property.
   *
   * @param property the property
   */
  public PropertyPath(Property property) {
    this(property, null);
  }

  /**
   * Creates a path by prepending a path with a property.
   *
   * @param property the property to prepend
   * @param tail     the path to prepend to
   */
  public PropertyPath(Property property, PropertyPath tail) {
    this.property = property;
    this.tail = tail;
  }

  /**
   * Returns the first property in this path.
   *
   * @return the first property in the path
   */
  public Property getProperty() {
    return property;
  }

  /**
   * Returns the path after the first property.
   *
   * @return the path after the first property
   */
  public PropertyPath getTail() {
    return tail;
  }

  /**
   * Creates a new path that is a copy of this path.
   * The properties are not copied.
   *
   * @return a copy of this path
   */
  public PropertyPath copy() {
    return new PropertyPath(property, getTail() == null ? null : getTail().copy());
  }

}
