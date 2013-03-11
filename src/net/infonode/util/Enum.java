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


// $Id: Enum.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Base class for enum classes.
 * Each enum value contains a name and an integer identifier.
 *
 * @author Jesper Nordenberg
 * @version $Revision: 1.3 $ $Date: 2011-09-07 19:56:10 $
 */
public class Enum implements Serializable, Writable {
  private static final long serialVersionUID = 1;
  private static final HashMap VALUE_MAP = new HashMap();

  private int value;
  private transient String name;

  protected Enum(int value, String name) {
    this.value = value;
    this.name = name;

    HashMap values = (HashMap) VALUE_MAP.get(getClass());

    if (values == null) {
      values = new HashMap();
      VALUE_MAP.put(getClass(), values);
    }

    values.put(new Integer(value), this);
  }

  /**
   * Returns the integer identifier for this enum value.
   *
   * @return the integer identifier for this enum value
   */
  public int getValue() {
    return value;
  }

  /**
   * Return the name of this enum value.
   *
   * @return the name of this enum value
   */
  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public void write(ObjectOutputStream out) throws IOException {
    writeObject(out);
  }

  protected static Object getObject(Class cl, int value) throws IOException {
    HashMap map = (HashMap) VALUE_MAP.get(cl);

    if (map == null)
      throw new IOException("Invalid enum class '" + cl + "'!");

    Object object = map.get(new Integer(value));

    if (object == null)
      throw new IOException("Invalid enum value '" + value + "'!");

    return object;
  }

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeShort(value);
  }

  private void readObject(ObjectInputStream in) throws IOException {
    value = in.readShort();
  }

  protected static Object decode(Class cl, ObjectInputStream in) throws IOException {
    return getObject(cl, in.readShort());
  }

  protected Object readResolve() throws ObjectStreamException {
    try {
      return getObject(getClass(), getValue());
    }
    catch (IOException e) {
      return this;
    }
  }
}
