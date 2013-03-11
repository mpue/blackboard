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


// $Id: PropertyMapImpl.java,v 1.4 2011-09-30 13:27:59 mpue Exp $
package net.infonode.properties.propertymap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.infonode.properties.base.Property;
import net.infonode.properties.base.exception.InvalidPropertyException;
import net.infonode.properties.propertymap.ref.CompositeMapRef;
import net.infonode.properties.propertymap.ref.ParentMapRef;
import net.infonode.properties.propertymap.ref.PropertyMapPropertyRef;
import net.infonode.properties.propertymap.ref.PropertyMapRef;
import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;
import net.infonode.properties.propertymap.value.PropertyRefValue;
import net.infonode.properties.propertymap.value.PropertyValue;
import net.infonode.properties.propertymap.value.ValueDecoder;
import net.infonode.properties.util.PropertyChangeListener;
import net.infonode.properties.util.PropertyPath;
import net.infonode.util.Printer;
import net.infonode.util.Utils;
import net.infonode.util.ValueChange;
import net.infonode.util.collection.map.ConstVectorMap;
import net.infonode.util.collection.map.MapAdapter;
import net.infonode.util.collection.map.SingleValueMap;
import net.infonode.util.collection.map.base.ConstMap;
import net.infonode.util.collection.map.base.ConstMapIterator;
import net.infonode.util.collection.map.base.MapIterator;
import net.infonode.util.collection.notifymap.AbstractConstChangeNotifyMap;
import net.infonode.util.collection.notifymap.ChangeNotifyMapWrapper;
import net.infonode.util.collection.notifymap.ConstChangeNotifyMap;
import net.infonode.util.collection.notifymap.ConstChangeNotifyVectorMap;
import net.infonode.util.signal.Signal;
import net.infonode.util.signal.SignalListener;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.4 $
 */
public class PropertyMapImpl implements PropertyMap {
  private static final int SERIALIZE_VERSION = 1;

  private class PropertyObjectMap extends AbstractConstChangeNotifyMap implements SignalListener {
    private boolean listenerActive;

    PropertyObjectMap() {
    }

    protected void listenerAdded() {
      if (!listenerActive) {
        listenerActive = true;
        addInheritedReferences();
        superMap.getChangeSignal().add(this);
      }
    }

    public void signalEmitted(Signal signal, Object object) {
      ConstMap changes = (ConstMap) object;
      MapAdapter m = new MapAdapter();

      for (ConstMapIterator iterator = changes.constIterator(); iterator.atEntry(); iterator.next()) {
        Property property = (Property) iterator.getKey();

        if (propertyGroup.hasProperty(property)) {
          PropertyValue currentValue = (PropertyValue) values.get(property);

          if (currentValue == null || currentValue.getParent() != null) {
            ValueChange vc = (ValueChange) iterator.getValue();
            PropertyValue superValue = (PropertyValue) vc.getNewValue();
            PropertyValue newValue = superValue == null ? null : superValue.getSubValue(PropertyMapImpl.this);
            internalSetValue(property, newValue);
            m.put(property, new ValueChange(currentValue != null ? currentValue : vc.getOldValue(),
                                            newValue != null ? newValue : vc.getNewValue()));
          }
        }
      }

      if (!m.isEmpty())
        fireEntriesChanged(m);
    }

    protected void lastListenerRemoved() {
      if (listenerActive) {
        listenerActive = false;
        superMap.getChangeSignal().remove(this);
        removeInheritedReferences();
      }
    }

    public boolean checkListeners(Set visited) {
      for (Iterator it = getChangeSignalInternal().iterator(); it.hasNext();) {
        Object l = it.next();

        if (l instanceof PropertyRefValue) {
          PropertyRefValue v = (PropertyRefValue) l;

          if (v.getMap().checkListeners(visited))
            return true;
        }
      }

      return false;
    }

    public void updateListeners() {
      for (Iterator it = getChangeSignalInternal().iterator(); it.hasNext();) {
        if (!(it.next() instanceof PropertyRefValue)) {
          return;
        }
      }

      for (Iterator it = getChangeSignalInternal().iterator(); it.hasNext();) {
        Object l = it.next();

        if (l instanceof PropertyRefValue) {
          PropertyRefValue v = (PropertyRefValue) l;

          if (v.getMap().checkListeners(new HashSet())) {
            return;
          }
        }
      }

      lastListenerRemoved();
    }

    private void addInheritedReferences() {
      for (ConstMapIterator iterator = values.constIterator(); iterator.atEntry(); iterator.next()) {
        Property property = (Property) iterator.getKey();
        PropertyValue currentValue = (PropertyValue) values.get(property);
        currentValue.updateListener(true);
      }

      for (ConstMapIterator iterator = superMap.constIterator(); iterator.atEntry(); iterator.next()) {
        Property property = (Property) iterator.getKey();

        if (propertyGroup.hasProperty(property)) {
          PropertyValue currentValue = (PropertyValue) values.get(property);

          if (currentValue == null || currentValue.getParent() != null) {
            PropertyValue superValue = (PropertyValue) iterator.getValue();
            PropertyValue newValue = superValue == null ? null : superValue.getSubValue(PropertyMapImpl.this);
            internalSetValue(property, newValue);
          }
        }
      }
    }

    private void removeInheritedReferences() {
      ArrayList toBeRemoved = new ArrayList();

      for (ConstMapIterator iterator = values.constIterator(); iterator.atEntry(); iterator.next()) {
        Property property = (Property) iterator.getKey();
        PropertyValue currentValue = (PropertyValue) values.get(property);

        if (currentValue.getParent() != null) {
          currentValue.unset();
          toBeRemoved.add(property);
        }
        else {
          currentValue.updateListener(false);
        }
      }

      for (int i = 0; i < toBeRemoved.size(); i++) {
        values.remove(toBeRemoved.get(i));
      }
    }

    public Object get(Object key) {
      return vectorMap.get(key);
    }

    public boolean containsKey(Object key) {
      return vectorMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
      return vectorMap.containsValue(value);
    }

    public boolean isEmpty() {
      return vectorMap.isEmpty();
    }

    public ConstMapIterator constIterator() {
      return vectorMap.constIterator();
    }

    protected void fireEntriesChanged(ConstMap changes) {
      super.fireEntriesChanged(changes);
    }
  }

  private PropertyMapGroup propertyGroup;
  private PropertyMapImpl parent;
  private PropertyMapProperty property;

  private ChangeNotifyMapWrapper values = new ChangeNotifyMapWrapper(new MapAdapter());
  private ConstChangeNotifyVectorMap superMap = new ConstChangeNotifyVectorMap();
  private ConstVectorMap vectorMap = new ConstVectorMap();
  private PropertyObjectMap map = new PropertyObjectMap();

  private ArrayList superMaps = new ArrayList(1);
  private MapAdapter childMaps = new MapAdapter();

  private HashMap propertyChangeListeners;
  private ArrayList listeners;
  private ArrayList treeListeners;

  private SignalListener mapListener;

  public PropertyMapImpl(PropertyMapGroup propertyGroup) {
    this(propertyGroup, null);
  }

  public PropertyMapImpl(PropertyMapImpl inheritFrom) {
    this(inheritFrom.getPropertyGroup(), inheritFrom);
  }

  public PropertyMapImpl(PropertyMapGroup propertyGroup, PropertyMapImpl superObject) {
    this(propertyGroup, null, null);

    if (superObject != null)
      addSuperMap(superObject);
  }

  public PropertyMapImpl(PropertyMapImpl parent, PropertyMapProperty property) {
    this(property.getPropertyMapGroup(), parent, property);
  }

  public PropertyMapImpl(PropertyMapGroup propertyGroup, PropertyMapImpl parent, PropertyMapProperty property) {
    this.parent = parent;
    this.property = property;
    this.propertyGroup = propertyGroup;

    Property[] properties = this.propertyGroup.getProperties();

    for (int i = 0; i < properties.length; i++) {
      if (properties[i] instanceof PropertyMapProperty) {
        PropertyMapProperty p = (PropertyMapProperty) properties[i];
        PropertyMapImpl propertyObject = new PropertyMapImpl(this, p);
        childMaps.put(p, propertyObject);
      }
    }

    vectorMap.addMap(values);
    vectorMap.addMap(superMap);
  }

  private boolean hasTreeListener() {
    return (treeListeners != null && treeListeners.size() > 0) || (parent != null && parent.hasTreeListener());
  }

  private boolean hasListener() {
    return hasTreeListener() ||
           (listeners != null && listeners.size() > 0) ||
           (propertyChangeListeners != null && propertyChangeListeners.size() > 0);
  }

  private void updateListenerRecursive() {
    updateListener();

    for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next())
      ((PropertyMapImpl) iterator.getValue()).updateListenerRecursive();
  }

  private void updateListener() {
    if (hasListener()) {
      if (mapListener == null) {
        mapListener = new SignalListener() {
          public void signalEmitted(Signal signal, Object object) {
            PropertyMapManager.getInstance().addMapChanges(PropertyMapImpl.this, (ConstMap) object);
          }
        };

        map.getChangeSignal().add(mapListener);
      }
    }
    else {
      if (mapListener != null) {
        map.getChangeSignal().remove(mapListener);
        mapListener = null;
        map.updateListeners();
      }
    }
  }

  private boolean checkListeners(Set visited) {
    if (visited.contains(this))
      return false;

    visited.add(this);
    return hasListener() || map.checkListeners(visited);
  }

  public ConstChangeNotifyMap getMap() {
    return map;
  }

  public PropertyMap getSuperMap() {
    return superMaps.size() == 0 ? null : (PropertyMap) superMaps.get(0);
  }

  public Object removeValue(Property property) throws InvalidPropertyException {
    checkProperty(property);
    PropertyValue value = (PropertyValue) values.get(property);

    // Can't removeValue not set values or inherited reference values
    if (value == null || value.getParent() != null)
      return null;

    values.remove(property);

    PropertyMapManager.getInstance().beginBatch();

    try {
      firePropertyValueChanged(property, new ValueChange(value, getValue(property)));
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }

    return value.get(this);
  }

  private PropertyMapRef getPathFrom(PropertyMapImpl parentObject) {
    if (parent == null)
      return null;

    if (parent == parentObject)
      return new PropertyMapPropertyRef(property);

    PropertyMapRef parentRef = parent.getPathFrom(parentObject);
    return parentRef == null ? null : new CompositeMapRef(parentRef, new PropertyMapPropertyRef(property));
  }

  private PropertyMapRef getRelativePathTo(PropertyMapImpl propertyObject) {
    PropertyMapRef ref = propertyObject == this ? ThisPropertyMapRef.INSTANCE : propertyObject.getPathFrom(this);
    return ref == null ?
           parent == null ?
           null : new CompositeMapRef(ParentMapRef.INSTANCE, parent.getRelativePathTo(propertyObject)) :
           ref;
  }

  public Object createRelativeRef(Property fromProperty, PropertyMap toObject, Property toProperty) {
    PropertyValue value = setValue(fromProperty,
                                   new PropertyRefValue(this,
                                                        fromProperty,
                                                        getRelativePathTo((PropertyMapImpl) toObject),
                                                        toProperty,
                                                        null));
    return value == null ? null : value.getWithDefault(this);
  }

  public int getSuperMapCount() {
    return superMaps.size();
  }

  public void addSuperMap(PropertyMap superMap) {
    PropertyMapImpl superMapImpl = (PropertyMapImpl) superMap;
    PropertyMapManager.getInstance().beginBatch();

    try {
      addSuperMap(0, superMapImpl);
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }
  }

  public PropertyMap removeSuperMap() {
    if (superMaps.size() > (parent == null ? 0 : parent.superMaps.size())) {
      PropertyMapImpl object = (PropertyMapImpl) superMaps.get(0);
      removeSuperMap(0);
      return object;
    }
    else
      return null;
  }

  public boolean removeSuperMap(PropertyMap superMap) {
    if (superMaps.size() > (parent == null ? 0 : parent.superMaps.size())) {
      int index = superMaps.indexOf(superMap);

      if (index == -1)
        return false;
      else {
        removeSuperMap(index);
        return true;
      }
    }
    else
      return false;
  }

  public boolean replaceSuperMap(PropertyMap oldSuperMap, PropertyMap newSuperMap) {
    if (oldSuperMap != newSuperMap && superMaps.size() > (parent == null ? 0 : parent.superMaps.size())) {
      int index = superMaps.indexOf(oldSuperMap);

      if (index == -1)
        return false;
      else {
        PropertyMapManager.getInstance().beginBatch();

        try {
          removeSuperMap(index);
          addSuperMap(index, (PropertyMapImpl) newSuperMap);
        }
        finally {
          PropertyMapManager.getInstance().endBatch();
        }

        return true;
      }
    }
    else
      return false;
  }

  private void removeParentSuperMap(int parentIndex) {
    removeSuperMap(superMaps.size() - parent.superMaps.size() - 1 + parentIndex);
  }

  private void removeSuperMap(int index) {
    PropertyMapManager.getInstance().beginBatch();

    try {
      superMap.removeMap(index);
      superMaps.remove(index);

      for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
        ((PropertyMapImpl) iterator.getValue()).removeParentSuperMap(index);
      }
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }
  }

  private void addSuperMap(PropertyMapImpl propertyObjectImpl) {
    addSuperMap(0, propertyObjectImpl);
  }

  private void addParentSuperMap(PropertyMapImpl propertyObjectImpl, int parentIndex) {
    addSuperMap(superMaps.size() - parent.superMaps.size() + 1 + parentIndex, propertyObjectImpl);
  }

  private void addSuperMap(int index, PropertyMapImpl propertyObjectImpl) {
    PropertyMapManager.getInstance().beginBatch();

    try {
      superMap.addMap(index, propertyObjectImpl.map);
      superMaps.add(index, propertyObjectImpl);

      for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
        ((PropertyMapImpl) iterator.getValue()).addParentSuperMap(
            propertyObjectImpl.getChildMapImpl((PropertyMapProperty) iterator.getKey()), index);
      }
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }
  }

  public void addTreeListener(PropertyMapTreeListener listener) {
    if (treeListeners == null)
      treeListeners = new ArrayList(2);

    treeListeners.add(listener);
    updateListenerRecursive();
  }

  public void removeTreeListener(PropertyMapTreeListener listener) {
    if (treeListeners != null) {
      treeListeners.remove(listener);

      if (treeListeners.size() == 0)
        treeListeners = null;

      updateListenerRecursive();
    }
  }

  public void addListener(PropertyMapListener listener) {
    if (listeners == null)
      listeners = new ArrayList(2);

    listeners.add(listener);
    updateListener();
  }

  public void removeListener(PropertyMapListener listener) {
    if (listeners != null) {
      listeners.remove(listener);

      if (listeners.size() == 0)
        listeners = null;
    }

    updateListener();
  }

  public PropertyMapGroup getPropertyGroup() {
    return propertyGroup;
  }

  public void addPropertyChangeListener(Property property, PropertyChangeListener listener) {
    if (propertyChangeListeners == null)
      propertyChangeListeners = new HashMap(4);

    ArrayList list = (ArrayList) propertyChangeListeners.get(property);

    if (list == null) {
      list = new ArrayList(2);
      propertyChangeListeners.put(property, list);
    }

    list.add(listener);
    updateListener();
  }

  public void removePropertyChangeListener(Property property, PropertyChangeListener listener) {
    if (propertyChangeListeners != null) {
      ArrayList list = (ArrayList) propertyChangeListeners.get(property);

      if (list == null)
        return;

      list.remove(listener);

      if (list.isEmpty()) {
        propertyChangeListeners.remove(property);

        if (propertyChangeListeners.isEmpty())
          propertyChangeListeners = null;
      }

      updateListener();
    }
  }

  public PropertyMapImpl getParent() {
    return parent;
  }

  public PropertyMapProperty getProperty() {
    return property;
  }

  private void checkProperty(Property property) {
    if (!propertyGroup.hasProperty(property))
      throw new InvalidPropertyException(property,
                                         "Property '" + property + "' not found in object '" + propertyGroup + "'!");
  }

  public PropertyMap getChildMap(PropertyMapProperty property) {
    return getChildMapImpl(property);
  }

  public PropertyMapImpl getChildMapImpl(PropertyMapProperty property) {
    checkProperty(property);
    return (PropertyMapImpl) childMaps.get(property);
  }

  private PropertyValue getParentDefaultValue(PropertyPath path) {
    PropertyValue value = parent == null ? null : parent.getParentDefaultValue(new PropertyPath(property, path));
    return value == null ? ((PropertyMapImpl) propertyGroup.getDefaultMap()).getValue(path) : value;
  }

  public PropertyValue getValueWithDefault(Property property) {
    PropertyValue value = getValue(property);
    return value == null ? getParentDefaultValue(new PropertyPath(property)) : value;
  }

  private PropertyValue getValue(PropertyPath propertyPath) {
    return propertyPath.getTail() == null ?
           getValue(propertyPath.getProperty()) :
           getChildMapImpl((PropertyMapProperty) propertyPath.getProperty()).getValue(propertyPath.getTail());
  }

  public PropertyValue getValue(Property property) {
    checkProperty(property);
    return (PropertyValue) map.get(property);
  }

  private PropertyValue internalSetValue(Property property, PropertyValue value) {
    PropertyValue oldValue = (PropertyValue) (value == null ? values.remove(property) : values.put(property, value));

    if (value != null)
      value.updateListener(hasListener());

    if (oldValue != null)
      oldValue.unset();

    return oldValue;
  }

  public PropertyValue setValue(Property property, PropertyValue value) {
    checkProperty(property);
    PropertyValue oldValue = getValue(property);
    internalSetValue(property, value);

    if (!Utils.equals(value, oldValue)) {
      PropertyMapManager.getInstance().beginBatch();

      try {
        firePropertyValueChanged(property, new ValueChange(oldValue, value));
      }
      finally {
        PropertyMapManager.getInstance().endBatch();
      }
    }

    return oldValue;
  }

  public boolean valueIsSet(Property property) {
    PropertyValue value = (PropertyValue) values.get(property);
    return value != null && value.getParent() == null;
  }

  public void firePropertyValueChanged(Property property, ValueChange change) {
    map.fireEntriesChanged(new SingleValueMap(property, change));
  }

  protected void firePropertyTreeValuesChanged(Map changes) {
    if (treeListeners != null) {
      PropertyMapTreeListener[] l = (PropertyMapTreeListener[]) treeListeners.toArray(
          new PropertyMapTreeListener[treeListeners.size()]);

      for (int i = 0; i < l.length; i++)
        l[i].propertyValuesChanged(changes);
    }
  }

  void firePropertyValuesChanged(Map changes) {
    if (listeners != null) {
      PropertyMapListener[] l = (PropertyMapListener[]) listeners.toArray(new PropertyMapListener[listeners.size()]);

      for (int i = 0; i < l.length; i++)
        l[i].propertyValuesChanged(this, changes);
    }

    if (propertyChangeListeners != null) {
      for (Iterator iterator = changes.entrySet().iterator(); iterator.hasNext();) {
        Map.Entry entry = (Map.Entry) iterator.next();
        ArrayList list = (ArrayList) propertyChangeListeners.get(entry.getKey());

        if (list != null) {
          ValueChange vc = (ValueChange) entry.getValue();
          PropertyChangeListener[] l = (PropertyChangeListener[]) list.toArray(new PropertyChangeListener[list.size()]);

          for (int i = 0; i < l.length; i++)
            l[i].propertyChanged((Property) entry.getKey(), this, vc.getOldValue(), vc.getNewValue());
        }
      }
    }
  }

  public void dump() {
    dump(new Printer(), new HashSet(4));
  }

  public void dump(Printer printer, Set printed) {
    printed.add(this);

    for (ConstMapIterator iterator = values.constIterator(); iterator.atEntry(); iterator.next()) {
      printer.println(iterator.getKey() + " = " + iterator.getValue());
    }

    if (!values.isEmpty())
      printer.println();

    for (int i = 0; i < superMaps.size(); i++) {
/*      if (printed.contains(superMaps.get(i)))
        continue;
*/
      printer.println("Super Object " + (i + 1) + ':');
      printer.beginSection();
      ((PropertyMapImpl) superMaps.get(i)).dump(printer, printed);
      printer.endSection();
      printer.println();
    }

    for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
      printer.println(iterator.getKey() + ":");
      printer.beginSection();
      ((PropertyMapImpl) iterator.getValue()).dump(printer, printed);
      printer.endSection();
      printer.println();
    }
  }

  public void dumpSuperMaps(Printer printer) {
    printer.println(System.identityHashCode(this) + ":" + this);

    for (int i = 0; i < superMaps.size(); i++) {
//      if (superMap.getMap(i) != ((PropertyMapImpl) superMaps.get(i)).map)
//        System.out.println("Error!");

      printer.beginSection();
      ((PropertyMapImpl) superMaps.get(i)).dumpSuperMaps(printer);
      printer.endSection();
    }

  }

  public void clear(boolean recursive) {
    PropertyMapManager.getInstance().beginBatch();

    try {
      doClear(recursive);
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }
  }

  private void doClear(boolean recursive) {
    ArrayList items = new ArrayList(10);

    for (MapIterator iterator = values.iterator(); iterator.atEntry(); iterator.next()) {
      PropertyValue value = (PropertyValue) iterator.getValue();

      if (value.getParent() == null)
        items.add(iterator.getKey());
    }

    for (int i = 0; i < items.size(); i++)
      removeValue((Property) items.get(i));

    if (recursive) {
      for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
        ((PropertyMapImpl) iterator.getValue()).doClear(recursive);
      }
    }
  }

  public boolean isEmpty(boolean recursive) {
    for (ConstMapIterator iterator = values.constIterator(); iterator.atEntry(); iterator.next()) {
      PropertyValue value = (PropertyValue) iterator.getValue();

      if (value.getParent() == null)
        return false;
    }

    if (recursive) {
      for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
        if (!((PropertyMapImpl) iterator.getValue()).isEmpty(recursive))
          return false;
      }
    }

    return true;
  }

  private void doRead(ObjectInputStream in) throws IOException {
    while (in.readBoolean()) {
      String propertyName = in.readUTF();
      Property property = getPropertyGroup().getProperty(propertyName);
      PropertyValue value = ValueDecoder.decode(in, this, property);

      if (property != null && value != null)
        setValue(property, value);
    }

    while (in.readBoolean()) {
      PropertyMapProperty property = (PropertyMapProperty) getPropertyGroup().getProperty(in.readUTF());
      getChildMapImpl(property).doRead(in);
    }
  }

  public void write(ObjectOutputStream out, boolean recursive) throws IOException {
    out.writeInt(SERIALIZE_VERSION);
    doWrite(out, recursive);
  }

  public void write(ObjectOutputStream out) throws IOException {
    write(out, true);
  }

  private void doWrite(ObjectOutputStream out, boolean recursive) throws IOException {
    for (ConstMapIterator iterator = values.constIterator(); iterator.atEntry(); iterator.next()) {
      PropertyValue value = (PropertyValue) iterator.getValue();
      if (value.getParent() == null && value.isSerializable()) {
        out.writeBoolean(true);
        out.writeUTF(((Property) iterator.getKey()).getName());
        value.write(out);
      }
    }

    out.writeBoolean(false);

    if (recursive) {
      for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
        if (!((PropertyMapImpl) iterator.getValue()).isEmpty(true)) {
          out.writeBoolean(true);
          out.writeUTF(((Property) iterator.getKey()).getName());
          ((PropertyMapImpl) iterator.getValue()).doWrite(out, recursive);
        }
      }
    }

    out.writeBoolean(false);
  }

  public void read(ObjectInputStream in) throws IOException {
    PropertyMapManager.getInstance().beginBatch();

    try {
      int version = in.readInt();

      if (version > SERIALIZE_VERSION)
        throw new IOException("Can't read object because serialized version is newer than current version!");

      doRead(in);
    }
    finally {
      PropertyMapManager.getInstance().endBatch();
    }
  }

  public static void skip(ObjectInputStream in) throws IOException {
    int version = in.readInt();

    if (version > SERIALIZE_VERSION)
      throw new IOException("Can't read object because serialized version is newer than current version!");

    doSkip(in);
  }

  private static void doSkip(ObjectInputStream in) throws IOException {
    while (in.readBoolean()) {
      in.readUTF();
      ValueDecoder.skip(in);
    }

    while (in.readBoolean()) {
      in.readUTF();
      doSkip(in);
    }
  }

  private boolean doValuesEqual(PropertyMapImpl propertyObject, boolean recursive) {
    for (ConstMapIterator iterator = map.constIterator(); iterator.atEntry(); iterator.next()) {
      Property property = (Property) iterator.getKey();

      if (!Utils.equals(((PropertyValue) iterator.getValue()).get(this), propertyObject.getValue(property).get(this)))
        return false;
    }

    if (recursive) {
      for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
        PropertyMapProperty property = (PropertyMapProperty) iterator.getKey();

        if (!((PropertyMapImpl) iterator.getValue()).doValuesEqual(propertyObject.getChildMapImpl(property),
                                                                   recursive))
          return false;
      }
    }

    return true;
  }

  public boolean valuesEqualTo(PropertyMap propertyObject, boolean recursive) {
    return doValuesEqual((PropertyMapImpl) propertyObject, recursive);
  }

  public PropertyMap copy(boolean copySuperMaps, boolean recursive) {
    PropertyMapImpl map = new PropertyMapImpl(propertyGroup);
    doCopy(map, copySuperMaps, recursive, true);
    return map;
  }

  private void doCopy(PropertyMapImpl map, boolean copySuperMaps, boolean recursive, boolean topMap) {
    for (ConstMapIterator iterator = values.constIterator(); iterator.atEntry(); iterator.next()) {
      PropertyValue value = (PropertyValue) iterator.getValue();

      if (value.getParent() == null) {
        map.values.put(iterator.getKey(), value.copyTo(map));
      }
    }

    if (copySuperMaps) {
      for (int i = 0; i < (topMap ? superMaps.size() : superMaps.size() - parent.superMaps.size()); i++)
        map.addSuperMap((PropertyMapImpl) superMaps.get(i));
    }

    if (recursive) {
      for (ConstMapIterator iterator = childMaps.constIterator(); iterator.atEntry(); iterator.next()) {
        ((PropertyMapImpl) iterator.getValue()).doCopy(
            (PropertyMapImpl) map.getChildMap((PropertyMapProperty) iterator.getKey()),
            copySuperMaps,
            recursive,
            false);
      }
    }
  }
}
