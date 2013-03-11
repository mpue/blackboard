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


// $Id: CompositeMapRef.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.properties.propertymap.ref;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.infonode.properties.propertymap.PropertyMapImpl;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class CompositeMapRef implements PropertyMapRef {
  private PropertyMapRef ref1;
  private PropertyMapRef ref2;

  public CompositeMapRef(PropertyMapRef ref1, PropertyMapRef ref2) {
    this.ref1 = ref1;
    this.ref2 = ref2;
  }

  public PropertyMapImpl getMap(PropertyMapImpl object) {
    return ref2.getMap(ref1.getMap(object));
  }

  public String toString() {
    return ref1 + "." + ref2;
  }

  public void write(ObjectOutputStream out) throws IOException {
    out.writeInt(PropertyMapRefDecoder.COMPOSITE);
    ref1.write(out);
    ref2.write(out);
  }

  public static CompositeMapRef decode(ObjectInputStream in) throws IOException {
    return new CompositeMapRef(PropertyMapRefDecoder.decode(in), PropertyMapRefDecoder.decode(in));
  }
}
