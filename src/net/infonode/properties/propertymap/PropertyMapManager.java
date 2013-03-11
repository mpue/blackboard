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


// $Id: PropertyMapManager.java,v 1.5 2011-09-07 19:56:10 mpue Exp $
package net.infonode.properties.propertymap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.infonode.properties.propertymap.value.PropertyValue;
import net.infonode.util.Utils;
import net.infonode.util.ValueChange;
import net.infonode.util.collection.map.base.ConstMap;
import net.infonode.util.collection.map.base.ConstMapIterator;

/**
 * Utility class for performing multiple modifications to {@link PropertyMap}'s and merging change notifications to
 * optimize performance.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.5 $
 */
public class PropertyMapManager {
  private static final PropertyMapManager INSTANCE = new PropertyMapManager();

  private HashMap changes;
  private int batchCounter;

  /**
   * Returns the only instance of this class.
   *
   * @return the only instance of this class
   */
  public static PropertyMapManager getInstance() {
    return INSTANCE;
  }

  void addMapChanges(PropertyMapImpl propertyMap, ConstMap mapChanges) {
	  
	if (propertyMap == null || changes == null)
		return;
	  
    HashMap map = (HashMap) changes.get(propertyMap);

    if (map == null) {
      map = new HashMap();
      changes.put(propertyMap, map);
    }

    for (ConstMapIterator iterator = mapChanges.constIterator(); iterator.atEntry(); iterator.next()) {
      ValueChange vc = (ValueChange) iterator.getValue();
      Object key = iterator.getKey();
      Object newValue = vc.getNewValue() == null ?
                        null : ((PropertyValue) vc.getNewValue()).getWithDefault(propertyMap);
      Object value = map.get(key);
      Object oldValue = value == null ?
                        vc.getOldValue() == null ?
                        null : ((PropertyValue) vc.getOldValue()).getWithDefault(propertyMap) :
                        ((ValueChange) value).getOldValue();

      if (!Utils.equals(oldValue, newValue))
        map.put(iterator.getKey(), new ValueChange(oldValue, newValue));
      else if (value != null)
        map.remove(key);
    }
  }

  /**
   * Executes a method inside a {@link #beginBatch()} - {@link #endBatch()} pair. See {@link #beginBatch()} for
   * more information. It's safe to call other batch methods from inside {@link Runnable#run}.
   *
   * @param runnable the runnable to invoke
   */
  public static void runBatch(Runnable runnable) {
    getInstance().beginBatch();

    try {
      runnable.run();
    }
    finally {
      getInstance().endBatch();
    }
  }

  /**
   * Begins a batch operation. This stores and merges all change notifications occuring in all property maps until
   * {@link #endBatch} is called. Each call to this method MUST be followed by a call to {@link #endBatch}.
   * This method can be called an unlimited number of times without calling {@link #endBatch} in between, but each
   * call must have a corresponding call to {@link #endBatch}. Only when exiting from the
   * outermost {@link #endBatch()} the changes be propagated to the listeners.
   */
  public void beginBatch() {
    if (batchCounter++ == 0)
      changes = new HashMap();
  }

  private void addTreeChanges(PropertyMapImpl map, PropertyMapImpl modifiedMap, HashMap changes, HashMap treeChanges) {
    HashMap changeMap = (HashMap) treeChanges.get(map);

    if (changeMap == null) {
      changeMap = new HashMap();
      treeChanges.put(map, changeMap);
    }

    changeMap.put(modifiedMap, changes);

    if (map.getParent() != null)
      addTreeChanges(map.getParent(), modifiedMap, changes, treeChanges);
  }

  /**
   * Ends a batch operation. See {@link #beginBatch()} for more information.
   */
  public void endBatch() {
    if (--batchCounter == 0) {
      HashMap treeChanges = new HashMap();
      HashMap localChanges = changes;
      changes = null;

      for (Iterator iterator = localChanges.entrySet().iterator(); iterator.hasNext();) {
        Map.Entry entry = (Map.Entry) iterator.next();
        PropertyMapImpl object = (PropertyMapImpl) entry.getKey();
        HashMap objectChanges = (HashMap) entry.getValue();

        if (!objectChanges.isEmpty()) {
          object.firePropertyValuesChanged(Collections.unmodifiableMap(objectChanges));
          addTreeChanges(object, object, objectChanges, treeChanges);
        }
      }

      for (Iterator iterator = treeChanges.entrySet().iterator(); iterator.hasNext();) {
        Map.Entry entry = (Map.Entry) iterator.next();
        PropertyMapImpl object = (PropertyMapImpl) entry.getKey();
        HashMap objectChanges = (HashMap) entry.getValue();

        if (!objectChanges.isEmpty())
          object.firePropertyTreeValuesChanged(Collections.unmodifiableMap(objectChanges));
      }
    }
  }
}
