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


// $Id: DockingWindowDropFilterProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $

package net.infonode.docking.properties;

import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropFilterProperty;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapValueHandler;

/**
 * Properties and property values for {@link net.infonode.docking.drop.DropFilter}s
 * for all types of {@link net.infonode.docking.DockingWindow}s.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.4.0
 */
public class DockingWindowDropFilterProperties extends PropertyMapContainer {

  /**
   * Property group containing all docking window drop filter properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Docking Window Drop Filter Properties", "");

  /**
   * The {@link net.infonode.docking.drop.DropFilter} that is called when a
   * split drop is in progress.
   */
  public static final DropFilterProperty SPLIT_DROP_FILTER = new DropFilterProperty(PROPERTIES,
                                                                                    "Split Drop Filter",
                                                                                    "The drop filter that is called when a split drop is in progress.",
                                                                                    PropertyMapValueHandler.INSTANCE);

  /**
   * The {@link net.infonode.docking.drop.DropFilter} that is called when a
   * child window will be asked for accept drop.
   */
  public static final DropFilterProperty CHILD_DROP_FILTER = new DropFilterProperty(PROPERTIES,
                                                                                    "Child Drop Filter",
                                                                                    "The drop filter that is called when a child window will be asked for accept drop.",
                                                                                    PropertyMapValueHandler.INSTANCE);

  /**
   * The {@link net.infonode.docking.drop.DropFilter} that is called when an
   * interior drop is in progress.
   */
  public static final DropFilterProperty INTERIOR_DROP_FILTER = new DropFilterProperty(PROPERTIES,
                                                                                       "Interior Drop Filter",
                                                                                       "The drop filter that is called when an interior drop is in progress.",
                                                                                       PropertyMapValueHandler.INSTANCE);

  /**
   * The {@link net.infonode.docking.drop.DropFilter} that is called when an
   * insert tab drop is in progress.
   */
  public static final DropFilterProperty INSERT_TAB_DROP_FILTER = new DropFilterProperty(PROPERTIES,
                                                                                         "Insert Tab Drop Filter",
                                                                                         "The drop filter that is called when an insert tab drop is in progress.",
                                                                                         PropertyMapValueHandler.INSTANCE);

  /**
   * Creates an empty property object.
   */
  public DockingWindowDropFilterProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public DockingWindowDropFilterProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object that inherit values from another property object.
   *
   * @param inheritFrom the object from which to inherit property values
   */
  public DockingWindowDropFilterProperties(DockingWindowDropFilterProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public DockingWindowDropFilterProperties addSuperObject(DockingWindowDropFilterProperties properties) {
    getMap().addSuperMap(properties.getMap());

    return this;
  }

  /**
   * Removes a super object.
   *
   * @param superObject the super object to remove
   * @return this
   */
  public DockingWindowDropFilterProperties removeSuperObject(DockingWindowDropFilterProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Sets the split drop filter to be used when a split drop is in progress.
   *
   * @param filter the split drop filter
   * @return this
   */
  public DockingWindowDropFilterProperties setSplitDropFilter(DropFilter filter) {
    SPLIT_DROP_FILTER.set(getMap(), filter);
    return this;
  }

  /**
   * Returns the split drop filter that is used when a split drop is in
   * progress.
   *
   * @return the split drop filter
   */
  public DropFilter getSplitDropFilter() {
    return SPLIT_DROP_FILTER.get(getMap());
  }

  /**
   * Sets the child drop filter to be used when a child window will be asked
   * for accept drop.
   *
   * @param filter the child drop filter
   * @return this
   */
  public DockingWindowDropFilterProperties setChildDropFilter(DropFilter filter) {
    CHILD_DROP_FILTER.set(getMap(), filter);
    return this;
  }

  /**
   * Returns the child drop filter that is used when a child window will be asked
   * for accept drop.
   *
   * @return the child drop filter
   */
  public DropFilter getChildDropFilter() {
    return CHILD_DROP_FILTER.get(getMap());
  }

  /**
   * Sets the interior drop filter to be used when an interior drop is in progress.
   *
   * @param filter the interior drop filter
   * @return this
   */
  public DockingWindowDropFilterProperties setInteriorDropFilter(DropFilter filter) {
    INTERIOR_DROP_FILTER.set(getMap(), filter);
    return this;
  }

  /**
   * Returns the interior drop filter that is used when an interior drop is in progress.
   *
   * @return the interior drop filter
   */
  public DropFilter getInteriorDropFilter() {
    return INTERIOR_DROP_FILTER.get(getMap());
  }


  /**
   * Sets the insert tab drop filter to be used when an insert tab drop is in progress.
   *
   * @param filter the insert tab drop filter
   * @return this
   */
  public DockingWindowDropFilterProperties setInsertTabDropFilter(DropFilter filter) {
    INSERT_TAB_DROP_FILTER.set(getMap(), filter);
    return this;
  }

  /**
   * Returns the insert tab drop filter that is used when an insert tab drop is in progress.
   *
   * @return the child drop filter
   */
  public DropFilter getInsertTabDropFilter() {
    return INSERT_TAB_DROP_FILTER.get(getMap());
  }
}
