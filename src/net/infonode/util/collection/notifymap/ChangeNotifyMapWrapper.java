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


// $Id: ChangeNotifyMapWrapper.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.util.collection.notifymap;

import net.infonode.util.ValueChange;
import net.infonode.util.collection.map.MapAdapter;
import net.infonode.util.collection.map.base.ConstMapIterator;
import net.infonode.util.collection.map.base.Map;
import net.infonode.util.collection.map.base.MapIterator;

public class ChangeNotifyMapWrapper extends AbstractChangeNotifyMap {
  private class Iterator implements MapIterator {
    private MapIterator iterator;

    public Iterator(MapIterator iterator) {
      this.iterator = iterator;
    }

    public void remove() {
      iterator.remove();
      fireEntryRemoved(iterator.getKey(), iterator.getValue());
    }

    public Object getKey() {
      return iterator.getKey();
    }

    public Object getValue() {
      return iterator.getValue();
    }

    public void next() {
      iterator.next();
    }

    public boolean atEntry() {
      return iterator.atEntry();
    }
  }

  private Map map;

  public ChangeNotifyMapWrapper(Map map) {
    this.map = map;
  }

  public Map getMap() {
    return map;
  }

  public Object get(Object key) {
    return map.get(key);
  }

  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  public Object put(Object key, Object value) {
    Object oldValue = map.put(key, value);
    fireEntryChanged(key, oldValue, value);
    return oldValue;
  }

  public Object remove(Object key) {
    Object oldValue = map.remove(key);
    fireEntryRemoved(key, oldValue);
    return oldValue;
  }

  public void clear() {
    MapAdapter changeMap = new MapAdapter();

    for (ConstMapIterator iterator = map.constIterator(); iterator.atEntry(); iterator.next()) {
      changeMap.put(iterator.getKey(), new ValueChange(iterator.getValue(), null));
    }

    map.clear();
    fireEntriesChanged(changeMap);
  }

  public MapIterator iterator() {
    return new Iterator(map.iterator());
  }
}
