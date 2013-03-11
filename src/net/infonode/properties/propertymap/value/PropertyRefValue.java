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


// $Id: PropertyRefValue.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.properties.propertymap.value;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.infonode.properties.base.Property;
import net.infonode.properties.base.exception.InvalidPropertyTypeException;
import net.infonode.properties.propertymap.PropertyMapImpl;
import net.infonode.properties.propertymap.ref.PropertyMapRef;
import net.infonode.properties.propertymap.ref.PropertyMapRefDecoder;
import net.infonode.util.Printer;
import net.infonode.util.ValueChange;
import net.infonode.util.collection.map.base.ConstMap;
import net.infonode.util.signal.Signal;
import net.infonode.util.signal.SignalListener;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class PropertyRefValue implements PropertyValue, SignalListener {
  private PropertyMapImpl map;
  private Property property;
  private PropertyMapRef propertyObjectRef;
  private Property propertyRef;
  private PropertyRefValue parentRef;

  public PropertyRefValue(PropertyMapImpl map, Property property, PropertyMapRef propertyObjectRef,
                          Property propertyRef, PropertyRefValue parentRef) {
    if (!property.getType().isAssignableFrom(propertyRef.getType()))
      throw new InvalidPropertyTypeException(property,
                                             propertyRef,
                                             "Can't create reference from Property '" + property + "' to property '" +
                                             propertyRef +
                                             "' because they are of incompatible types!");

    this.map = map;
    this.property = property;
    this.propertyObjectRef = propertyObjectRef;
    this.propertyRef = propertyRef;
    this.parentRef = parentRef;
  }

  public Property getProperty() {
    return property;
  }

  public PropertyMapImpl getMap() {
    return map;
  }

  public void updateListener(boolean enable) {
    if (enable)
      propertyObjectRef.getMap(map).getMap().getChangeSignal().add(this);
    else
      propertyObjectRef.getMap(map).getMap().getChangeSignal().remove(this);
  }

  public PropertyValue getParent() {
    return parentRef;
  }

  public Object get(PropertyMapImpl object) {
    PropertyMapImpl o = propertyObjectRef.getMap(object);
    PropertyValue v = (o == null ? propertyObjectRef.getMap(map) : o).getValue(propertyRef);
    return v == null ? null : v.get(o);
  }

  public Object getWithDefault(PropertyMapImpl object) {
    PropertyMapImpl o = propertyObjectRef.getMap(object);
    PropertyValue v = (o == null ? propertyObjectRef.getMap(map) : o).getValueWithDefault(propertyRef);
    return v == null ? null : v.getWithDefault(o);
  }

  public PropertyValue getSubValue(PropertyMapImpl object) {
    PropertyMapImpl newObject = propertyObjectRef.getMap(object);

    if (newObject == null)
      return null;

    if (!newObject.getPropertyGroup().hasProperty(propertyRef))
      return null;

    return new PropertyRefValue(object, property, propertyObjectRef, propertyRef, this);
  }

  public void unset() {
    propertyObjectRef.getMap(map).getMap().getChangeSignal().remove(this);
  }

  public void signalEmitted(Signal signal, Object object) {
    ConstMap changes = (ConstMap) object;
    ValueChange vc = (ValueChange) changes.get(propertyRef);

    if (vc != null)
      map.firePropertyValueChanged(property, new ValueChange(vc.getOldValue(), this));
  }

  public String toString() {
    return "ref -> " + propertyObjectRef + '.' + propertyRef;
  }

  public void dump(Printer printer) {
    printer.println(toString());
  }

  public void write(ObjectOutputStream out) throws IOException {
    out.writeInt(ValueDecoder.REF);
    propertyObjectRef.write(out);
    out.writeUTF(propertyRef.getName());
  }

  public boolean isSerializable() {
    return true;
  }

  public static PropertyValue decode(ObjectInputStream in, PropertyMapImpl propertyObject, Property property) throws IOException {
    PropertyMapRef ref = PropertyMapRefDecoder.decode(in);
    String propertyName = in.readUTF();

    if (property == null || ref == null)
      return null;

    Property refProperty = ref.getMap(propertyObject).getPropertyGroup().getProperty(propertyName);

    if (refProperty == null)
      return null;

    return new PropertyRefValue(propertyObject, property, ref, refProperty, null);
  }

  public static void skip(ObjectInputStream in) throws IOException {
    PropertyMapRefDecoder.decode(in);
    in.readUTF();
  }

  public PropertyValue copyTo(PropertyMapImpl propertyMap) {
    return new PropertyRefValue(propertyMap, property, propertyObjectRef, propertyRef, null);
  }

}
