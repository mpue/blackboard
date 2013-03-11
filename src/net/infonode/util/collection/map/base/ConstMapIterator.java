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


// $Id: ConstMapIterator.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.util.collection.map.base;

/**
 * An iterator for a map.
 * The iterator points to a map entry when it's created so {@link #next} shouldn't be called at the start of the
 * iteration.
 * <p>
 * Here's an example on how to iterate over a map:
 * <code>
 * for (ConstIterator iterator = map.constIterator(); iterator.atEntry(); iterator.next()) {
 * Object key = iterator.getKey();
 * Object value = iterator.getValue();
 * ...
 * }
 * </code>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public interface ConstMapIterator {
  /**
   * Returns the key at the current map entry.
   *
   * @return the key at the current map entry
   */
  Object getKey();

  /**
   * Returns the value at the current map entry.
   *
   * @return the value at the current map entry
   */
  Object getValue();

  /**
   * Advance the iterator to the next entry.
   */
  void next();

  /**
   * Returns true if the iterator points to an entry in the map.
   *
   * @return true if the iterator points to an entry in the map
   */
  boolean atEntry();
}
