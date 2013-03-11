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


// $Id: MapAdapter.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.util.collection.map;

import java.util.HashMap;

import net.infonode.util.collection.map.base.ConstMapIterator;
import net.infonode.util.collection.map.base.Map;
import net.infonode.util.collection.map.base.MapIterator;

public class MapAdapter implements Map {
  private static class Iterator implements MapIterator {
    private java.util.Iterator iterator;
    private java.util.Map.Entry nextEntry;

    Iterator(java.util.Iterator iterator) {
      this.iterator = iterator;
      next();
    }

    public void remove() {
      iterator.remove();
    }

    public boolean atEntry() {
      return nextEntry != null;
    }

    public Object getKey() {
      return nextEntry.getKey();
    }

    public Object getValue() {
      return nextEntry.getValue();
    }

    public void next() {
      nextEntry = iterator.hasNext() ? (java.util.Map.Entry) iterator.next() : null;
    }
  }

  private HashMap map;

  public MapAdapter() {
  }

  public MapAdapter(HashMap map) {
    this.map = map;
  }

  public Object put(Object key, Object value) {
    if (map == null)
      map = new HashMap(4);

    return map.put(key, value);
  }

  public Object remove(Object key) {
    return map == null ? null : map.remove(key);
  }

  public void clear() {
    if (map != null)
      map.clear();
  }

  public MapIterator iterator() {
    return map == null ? (MapIterator) EmptyIterator.INSTANCE : (MapIterator) new Iterator(map.entrySet().iterator());
  }

  public Object get(Object key) {
    return map == null ? null : map.get(key);
  }

  public boolean containsKey(Object key) {
    return map != null && map.containsKey(key);
  }

  public boolean containsValue(Object value) {
    return map != null && map.containsValue(value);
  }

  public boolean isEmpty() {
    return map == null || map.isEmpty();
  }

  public ConstMapIterator constIterator() {
    return iterator();
  }

  public int size() {
    return map == null ? 0 : map.size();
  }
}
