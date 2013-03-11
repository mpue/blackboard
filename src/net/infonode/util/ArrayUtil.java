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


// $Id: ArrayUtil.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.util;

import java.util.ArrayList;

public class ArrayUtil {
  private ArrayUtil() {
  }

  static final public Object[] add(Object[] objects, Object object, Object[] newObjects) {
    System.arraycopy(objects, 0, newObjects, 0, objects.length);
    newObjects[objects.length] = object;
    return newObjects;
  }

  static final public byte[] part(byte[] array, int offset, int length) {
    byte[] b = new byte[length];
    System.arraycopy(array, offset, b, 0, length);
    return b;
  }

  static final public int countNotNull(Object[] objects) {
    int count = 0;

    for (int i = 0; i < objects.length; i++)
      if (objects[i] != null)
        count++;

    return count;
  }

  static final public int findSmallest(double[] items) {
    int index = 0;

    for (int i = 1; i < items.length; i++) {
      if (items[i] < items[index]) {
        index = i;
      }
    }

    return index;
  }

  static final public int findSmallest(int[] items) {
    int index = 0;

    for (int i = 1; i < items.length; i++) {
      if (items[i] < items[index]) {
        index = i;
      }
    }

    return index;
  }

  static final public int findLargest(float[] items) {
    int index = 0;

    for (int i = 1; i < items.length; i++) {
      if (items[i] > items[index]) {
        index = i;
      }
    }

    return index;
  }

  public static float[] toFloatArray(int[] values) {
    float[] floatValues = new float[values.length];

    for (int i = 0; i < values.length; i++)
      floatValues[i] = values[i];

    return floatValues;
  }

  static final public int indexOf(int[] array, int value) {
    for (int i = 0; i < array.length; i++)
      if (array[i] == value)
        return i;

    return -1;
  }

  static final public int indexOf(byte[] array, byte value) {
    for (int i = 0; i < array.length; i++)
      if (array[i] == value)
        return i;

    return -1;
  }

  static final public String[] append(String[] a1, String[] a2) {
    String[] n = new String[a1.length + a2.length];
    System.arraycopy(a1, 0, n, 0, a1.length);
    System.arraycopy(a2, 0, n, a1.length, a2.length);
    return n;
  }

  static final public Object[] append(Object[] a1, Object[] a2, Object[] out) {
    System.arraycopy(a1, 0, out, 0, a1.length);
    System.arraycopy(a2, 0, out, a1.length, a2.length);
    return out;
  }

  public static boolean equal(int[] a, int aOffset, int[] b, int bOffset, int length) {
    for (int i = 0; i < length; i++)
      if (a[aOffset + i] != b[bOffset + i])
        return false;

    return true;
  }

  public static boolean equal(byte[] a, int aOffset, byte[] b, int bOffset, int length) {
    for (int i = 0; i < length; i++)
      if (a[aOffset + i] != b[bOffset + i])
        return false;

    return true;
  }

  public static boolean contains(short[] a, short v) {
    for (int i = 0; i < a.length; i++)
      if (a[i] == v)
        return true;

    return false;
  }

  public static int[] range(int start, int length, int step) {
    int[] a = new int[length];

    for (int i = 0; i < length; i++)
      a[i] = start + step * i;

    return a;
  }

  public static boolean containsEqual(Object[] values, Object value) {
    return indexOfEqual(values, value) != -1;
  }

  public static boolean contains(Object[] values, Object value) {
    return indexOf(values, value) != -1;
  }

  public static int indexOf(Object[] values, Object value) {
    for (int i = 0; i < values.length; i++)
      if (values[i] == value)
        return i;

    return -1;
  }

  public static int indexOf(Object[] values, Object value, int startIndex, int length) {
    for (int i = startIndex; i < length; i++)
      if (values[i] == value)
        return i;

    return -1;
  }

  public static int indexOfEqual(Object[] values, Object value) {
    for (int i = 0; i < values.length; i++)
      if (values[i].equals(value))
        return i;

    return -1;
  }

  public static Object[] remove(Object[] values, Object value, Object[] newValues) {
    int index = indexOf(values, value);

    if (index == -1)
      index = values.length;

    System.arraycopy(values, 0, newValues, 0, index);
    System.arraycopy(values, index + 1, newValues, index, newValues.length - index);
    return newValues;
  }

  public static String toString(int[] a) {
    StringBuffer b = new StringBuffer(a.length * 4);

    for (int i = 0; i < a.length; i++) {
      if (i != 0)
        b.append(", ");

      b.append(a[i]);
    }

    return b.toString();
  }

  public static int[] part(int[] values, int start, int length) {
    int[] a = new int[length];
    System.arraycopy(values, start, a, 0, length);
    return a;
  }

  public static int sum(int[] values) {
    int sum = 0;

    for (int i = 0; i < values.length; i++)
      sum += values[i];

    return sum;
  }

  public static int count(int[] values, int value) {
    int count = 0;

    for (int i = 0; i < values.length; i++)
      if (values[i] == value)
        count++;

    return count;
  }

  public static int count(boolean[] values, boolean value) {
    int count = 0;

    for (int i = 0; i < values.length; i++)
      if (values[i] == value)
        count++;

    return count;
  }

  public static int findLargest(int[] items) {
    int index = 0;

    for (int i = 1; i < items.length; i++) {
      if (items[i] > items[index]) {
        index = i;
      }
    }

    return index;
  }

  public static int[] toIntArray(ArrayList items) {
    int[] result = new int[items.size()];

    for (int i = 0; i < items.size(); i++)
      result[i] = ((Number) items.get(i)).intValue();

    return result;
  }

  public static boolean[] toBooleanArray(ArrayList items) {
    boolean[] result = new boolean[items.size()];

    for (int i = 0; i < items.size(); i++)
      result[i] = ((Boolean) items.get(i)).booleanValue();

    return result;
  }
}
