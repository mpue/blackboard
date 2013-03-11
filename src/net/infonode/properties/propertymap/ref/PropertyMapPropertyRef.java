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


// $Id: PropertyMapPropertyRef.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.properties.propertymap.ref;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.infonode.properties.propertymap.PropertyMapImpl;
import net.infonode.properties.propertymap.PropertyMapProperty;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class PropertyMapPropertyRef implements PropertyMapRef {
  private String propertyName;

  private PropertyMapPropertyRef(String propertyName) {
    this.propertyName = propertyName;
  }

  public PropertyMapPropertyRef(PropertyMapProperty property) {
    propertyName = property.getName();
  }

  public PropertyMapImpl getMap(PropertyMapImpl object) {
    return object == null ?
           null : object.getChildMapImpl((PropertyMapProperty) object.getPropertyGroup().getProperty(propertyName));
  }

  public String toString() {
    return "(property '" + propertyName + "')";
  }

  public void write(ObjectOutputStream out) throws IOException {
    out.writeInt(PropertyMapRefDecoder.PROPERTY_OBJECT_PROPERTY);
    out.writeUTF(propertyName);
  }

  public static PropertyMapRef decode(ObjectInputStream in) throws IOException {
    return new PropertyMapPropertyRef(in.readUTF());
  }
}
