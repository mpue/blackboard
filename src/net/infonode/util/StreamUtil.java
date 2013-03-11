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


// $Id: StreamUtil.java,v 1.3 2011-09-07 19:56:10 mpue Exp $

package net.infonode.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class StreamUtil {
  public static final void readAll(InputStream in, byte[] data, int offset, int length) throws IOException {
    while (length > 0) {
      int len = in.read(data, offset, length);

      if (len == -1)
        throw new IOException("End of stream reached!");

      offset += len;
      length -= len;
    }
  }

  public static final byte[] readAll(InputStream is) throws IOException {
    byte[] data = new byte[is.available()];
    int pos = 0;

    while (pos < data.length)
      pos += is.read(data, pos, data.length - pos);

    is.close();
    return data;
  }

  public static final byte[] writeObject(Object object) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream o2 = new ObjectOutputStream(out);
    o2.writeObject(object);
    o2.close();
    return ArrayUtil.part(out.toByteArray(), 0, out.size());
  }

  public static final Object readObject(byte[] data) throws IOException, ClassNotFoundException {
    return new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
  }

  public static byte[] write(Writable writable) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream o2 = new ObjectOutputStream(out);
    writable.write(o2);
    o2.close();
    return out.toByteArray();
  }

  public static void read(byte[] data, Readable readable) throws IOException {
    readable.read(new ObjectInputStream(new ByteArrayInputStream(data)));
  }

  public static void readAll(InputStream in, byte[] data) throws IOException {
    readAll(in, data, 0, data.length);
  }

  public static void write(InputStream in, OutputStream out, int length) throws IOException {
    byte[] data = new byte[10000];

    while (length > 0) {
      int read = in.read(data, 0, Math.min(data.length, length));
      out.write(data, 0, read);
      length -= read;
    }
  }
}
