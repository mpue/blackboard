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


// $Id: PropertyMap.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.properties.propertymap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.infonode.properties.base.Property;
import net.infonode.properties.base.exception.InvalidPropertyException;
import net.infonode.properties.base.exception.InvalidPropertyTypeException;
import net.infonode.properties.util.PropertyChangeListener;
import net.infonode.util.ReadWritable;

/**
 * A property map contains values for some or all properties in a {@link PropertyMapGroup}. A property map
 * can have any number of super maps from which property values are inherited. Super maps that are searched for
 * values in the reverse order they were added to the property map. Property values are always set in the property
 * map specified.
 * <p>
 * Properties of type {@link PropertyMapProperty} in the {@link PropertyMapGroup} will automatically be assigned
 * new PropertyMap's as values. These PropertyMap's are called child maps. These property values cannot be
 * modified.
 * <p>
 * Listeners can be added to a PropertyMap. The listeners are notified when a property value is modified in the
 * PropertyMap or, if the property value is not overridden, one of it's super maps. A tree listener can also
 * be added that listens for value changes in the property map, it's super maps and it's child mapss.
 * <p>
 * Property maps are created using the factory methods in {@link PropertyMapFactory}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public interface PropertyMap extends ReadWritable {
  /**
   * Adds a listener that listens for value changes in this PropertyMap.
   * This listener will be notified of updates to values in this PropertyMap and super maps unless the property
   * value is overridden.
   *
   * @param listener the listener
   */
  void addListener(PropertyMapListener listener);

  /**
   * Removes a listener which was previously added with {@link #addListener(PropertyMapListener)}.
   *
   * @param listener the listener
   */
  void removeListener(PropertyMapListener listener);

  /**
   * Adds a tree listener that listens for value changes in this PropertyMap or any child maps.
   * This listener will be notified of updates to values in this PropertyMap, any child map recusively and super
   * maps unless the property value is overridden.
   *
   * @param listener the listener
   */
  void addTreeListener(PropertyMapTreeListener listener);

  /**
   * Removes a previously added tree listener.
   *
   * @param listener the listener
   */
  void removeTreeListener(PropertyMapTreeListener listener);

  /**
   * Adds a property listener that listens for value changes for a specific property.
   * This listener will be notified of value changes for the property in this PropertyMap and super maps unless
   * the property value is overridden.
   *
   * @param property the property to listen to changes on
   * @param listener the listener
   */
  void addPropertyChangeListener(Property property, PropertyChangeListener listener);

  /**
   * Removes a previously added property listener.
   *
   * @param property the property which the listener listens to changes on
   * @param listener the listener
   */
  void removePropertyChangeListener(Property property, PropertyChangeListener listener);

  /**
   * Adds a super map to this map.
   * If a property value is not found in this property map, the super maps will be searched recursively. The
   * super map last added will be searched first.
   *
   * @param superMap the super map
   */
  void addSuperMap(PropertyMap superMap);

  /**
   * Removes the most recently added super map.
   *
   * @return the super map removed
   */
  PropertyMap removeSuperMap();

  /**
   * Removes a super map that has previously been added using {@link #addSuperMap(PropertyMap)}.
   *
   * @param superMap the super map to remove
   * @return true if the super map was found and removed, otherwise false
   * @since IDW 1.3.0
   */
  boolean removeSuperMap(PropertyMap superMap);

  /**
   * Replaces a super map that has previously been added using {@link #addSuperMap(PropertyMap)}.
   *
   * @param oldSuperMap the super map to replace
   * @param newSuperMap the super map to replace it with
   * @return true if the super map was found and replaced, otherwise false
   * @since IDW 1.3.0
   */
  boolean replaceSuperMap(PropertyMap oldSuperMap, PropertyMap newSuperMap);

  /**
   * Returns the most recently added super map.
   *
   * @return the super map
   */
  PropertyMap getSuperMap();

  /**
   * Creates a relative reference from one property value to another property value.
   * <p>
   * When the value of the <tt>fromProperty</tt> is read, it will return the value of the <tt>toProperty</tt> in the
   * <tt>toMap</tt>.
   * <p>
   * Sub maps of this property map will inherit this reference relative to themselves, ie the reference in the sub
   * map is converted to a reference relative to the sub map if possible, otherwise the reference is the same as
   * for the super map. Here is an example:
   * <p>
   * <ul>
   * <li>Property map A contains value 5 for property X.</li>
   * <li>A relative reference is created in map A from property Y to property X. Getting the property value for Y in
   * A will now return 5.</li>
   * <li>A property map B is created and A is added as super map to B. Note that now B.Y will reference B.X and
   * not A.X! Getting B.X now returns 5 and B.Y also returns 5.</li>
   * <li>X is set to 7 in B. Getting B.Y will now return 7 as expected. Map A is unchanged and will still return
   * 5 as value for property Y.</li>
   * <li>A.Y is set to 1 which destroys the reference to A.X, and also the reference B.Y -> B.X. Getting B.Y will now
   * return 1 as it's inherited from A.Y.
   * </ul>
   * <p>
   * Changes to the referenced property value will be propagated to listeners of this property.
   *
   * @param fromProperty the property value that will hold the reference
   * @param toMap        the property map that holds the property value that is referenced
   * @param toProperty   the property which value is referenced
   * @return the old value that the fromProperty had in this property map
   * @throws InvalidPropertyTypeException
   */
  Object createRelativeRef(Property fromProperty, PropertyMap toMap, Property toProperty) throws InvalidPropertyTypeException;

  /**
   * Removes a property value.
   *
   * @param property the property
   * @return the value removed
   * @throws InvalidPropertyException if values for this property can't be stored in this property map
   */
  Object removeValue(Property property) throws InvalidPropertyException;

  /**
   * Returns true if this property map doesn't contain any property values.
   *
   * @param recursive true if child maps should be recursively checked
   * @return true if this property map doesn't contain any property values
   */
  boolean isEmpty(boolean recursive);

  /**
   * Removes all property values in this property map.
   *
   * @param recursive true if child maps should be cleared recursively
   */
  void clear(boolean recursive);

  /**
   * Returns true if all the values in this property map is equal to the values in the given map.
   * The property values are compared using {@link Object#equals}.
   *
   * @param propertyMap the map to compare values with
   * @param recursive   true if child maps should be recursively checked
   * @return true if all the values in this property map is equal to the values in the given map
   */
  boolean valuesEqualTo(PropertyMap propertyMap, boolean recursive);

  /**
   * Serializes the serializable values of this property map. Values not implementing the {@link java.io.Serializable}
   * interface will not be written to the stream. The properties are identified using their names.
   *
   * @param out       the stream on which to serialize this map
   * @param recursive true if child maps should be recursively serialized
   * @throws IOException if there is an error in the stream
   */
  void write(ObjectOutputStream out, boolean recursive) throws IOException;

  /**
   * <p>
   * Serializes the serializable values of this property map. Values not implementing the {@link java.io.Serializable}
   * interface will not be written to the stream. The properties are identified using their names.
   * </p>
   * <p>
   * This method recursively writes all child maps.
   * </p>
   *
   * @param out the stream
   * @throws IOException if there is a stream error
   */
  void write(ObjectOutputStream out) throws IOException;

  /**
   * Reads property values from a stream and sets them in this map.
   * Will overwrite existing values, but not remove values not found in the stream.
   * The properties are identified using their names.
   * If no property is found for a property name read from the stream the value is skipped and no error is reported.
   * If a value for a property in the stream is a reference to a another property value that cannot be resolved,
   * the property is not modified.
   *
   * @param in the stream from which to read property values
   * @throws IOException if there is an error in the stream
   */
  void read(ObjectInputStream in) throws IOException;

  /**
   * Creates a copy of this map. The method copies the values and optionally the references to super maps.
   *
   * @param copySuperMapRefs if true, copies the references to super maps
   * @param recursive        if true, copies all child maps as well
   * @return a copy of this map
   * @since IDW 1.3.0
   */
  PropertyMap copy(boolean copySuperMapRefs, boolean recursive);
}
