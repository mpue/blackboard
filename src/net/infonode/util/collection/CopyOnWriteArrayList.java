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


// $Id: CopyOnWriteArrayList.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.util.collection;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public final class CopyOnWriteArrayList {
  private static class IteratorImpl implements Iterator {
    private Object[] e;
    private int size;
    private int index;

    IteratorImpl(Object[] e, int size, int index) {
      this.e = e;
      this.size = size;
      this.index = index;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
      return index < size;
    }

    public Object next() {
      return e[index++];
    }
  }

  private Object[] elements;
  private int size;

  public CopyOnWriteArrayList(int initialCapacity) {
    elements = new Object[initialCapacity];
  }

  public void removeAll(Collection toRemove) {
    Object[] ne = new Object[size - toRemove.size()];
    int j = 0;

    for (int i = 0; i < size; i++) {
      if (!toRemove.contains(elements[i])) {
        ne[j++] = elements[i];
      }
    }

    size = j;
    elements = ne;
  }

  public void add(Object element) {
    if (size >= elements.length) {
      Object[] newElements = new Object[getPreferredSize(size)];
      System.arraycopy(elements, 0, newElements, 0, size);
      elements = newElements;
    }

    elements[size++] = element;
  }

  public boolean remove(Object element) {
    int index = indexOf(element);

    if (index == -1)
      return false;

    remove(index);
    return true;
  }

  public void remove(int index) {
    size--;
    Object[] newElements = new Object[getPreferredSize(size)];
    System.arraycopy(elements, 0, newElements, 0, index);
    System.arraycopy(elements, index + 1, newElements, index, size - index);
    elements = newElements;
  }

  public int indexOf(Object element) {
    for (int i = 0; i < size; i++)
      if (elements[i] == element)
        return i;

    return -1;
  }

  public void each(Closure closure) {
    Object[] l = elements;
    int s = size;

    for (int i = 0; i < s; i++)
      closure.apply(l[i]);
  }

  public Iterator iterator() {
    return new IteratorImpl(elements, size, 0);
  }

  private static int getPreferredSize(int size) {
    return size * 3 / 2 + 1;
  }

  public int size() {
    return size;
  }

  public Object get(int index) {
    return elements[index];
  }

  public Object[] getElements() {
    return elements;
  }

/*  private static class Iterator implements java.util.Iterator {
    private Object[] elements;
    private int size;
    private int index;

    Iterator(Object[] elements, int size) {
      this.elements = elements;
      this.size = size;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
      return index < size;
    }

    public Object next() {
      return elements[index++];
    }
  }*/

}
