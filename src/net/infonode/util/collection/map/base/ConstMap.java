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


// $Id: ConstMap.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.util.collection.map.base;

import net.infonode.util.collection.ConstCollection;

/**
 * An immutable map.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public interface ConstMap extends ConstCollection {
  /**
   * Returns the value associated with the key.
   *
   * @param key the key
   * @return the value associated with the key, null if no value is associated with the key
   */
  Object get(Object key);

  /**
   * Returns true if this map contains the key.
   *
   * @param key the key
   * @return true if this map contains the key
   */
  boolean containsKey(Object key);

  /**
   * Returns true if this map contains the value.
   *
   * @param value the value
   * @return true if this map contains the value
   */
  boolean containsValue(Object value);

  /**
   * Returns an iterator for this map.
   *
   * @return an iterator for this map
   */
  ConstMapIterator constIterator();
}
