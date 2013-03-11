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


// $Id: ConstVectorMap.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.util.collection.map;

import java.util.ArrayList;

import net.infonode.util.collection.map.base.ConstMap;
import net.infonode.util.collection.map.base.ConstMapIterator;

public class ConstVectorMap implements ConstMap {
  private class ConstIterator implements ConstMapIterator {
    private int index = 1;
    private ConstMapIterator iterator;

    ConstIterator() {
      if (maps.size() > 0) {
        iterator = getMap(0).constIterator();
        advance();
      }
      else
        iterator = EmptyIterator.INSTANCE;
    }

    public Object getKey() {
      return iterator.getKey();
    }

    public Object getValue() {
      return iterator.getValue();
    }

    public void next() {
      iterator.next();
      advance();
    }

    public boolean atEntry() {
      return iterator.atEntry();
    }

    private void advance() {
      while (true) {
        while (!iterator.atEntry()) {
          if (index == maps.size())
            return;

          iterator = getMap(index++).constIterator();
        }

        if (ConstVectorMap.this.getValue(iterator.getKey(), 0, index - 1) == null)
          return;

        iterator.next();
      }
    }
  }

  private ArrayList maps = new ArrayList(2);

  private Object getValue(Object key, int fromIndex, int toIndex) {
    for (int i = fromIndex; i < toIndex; i++) {
      Object value = getMap(i).get(key);

      if (value != null)
        return value;
    }

    return null;
  }

  public void addMap(ConstMap map) {
    addMap(maps.size(), map);
  }

  public void addMap(int index, final ConstMap map) {
    maps.add(index, map);
  }

  public int getMapCount() {
    return maps.size();
  }

  public ConstMap removeMap(int index) {
    return (ConstMap) maps.remove(index);
  }

  public Object get(Object key) {
    for (int i = 0; i < maps.size(); i++) {
      Object v = getMap(i).get(key);

      if (v != null)
        return v;
    }

    return null;
  }

  public boolean containsKey(Object key) {
    for (int i = 0; i < maps.size(); i++) {
      if (getMap(i).containsKey(key))
        return true;
    }

    return false;
  }

  public boolean containsValue(Object value) {
    for (int i = 0; i < maps.size(); i++) {
      if (getMap(i).containsValue(value))
        return true;
    }

    return false;
  }

  public boolean isEmpty() {
    for (int i = 0; i < maps.size(); i++) {
      if (!getMap(i).isEmpty())
        return false;
    }

    return true;
  }

  public ConstMap getMap(int index) {
    return (ConstMap) maps.get(index);
  }

  public int getMapIndex(ConstMap map) {
    return maps.indexOf(map);
  }

  public ConstMapIterator constIterator() {
    return new ConstIterator();
  }
}
