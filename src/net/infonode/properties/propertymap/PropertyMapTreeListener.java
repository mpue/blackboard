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


// $Id: PropertyMapTreeListener.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.properties.propertymap;

import java.util.Map;

/**
 * Listener interface for property value changes in a property map and it's child maps.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public interface PropertyMapTreeListener {
  /**
   * Invoked when one or more property value has been modified in one or more property maps.
   *
   * @param changes an unmodifiable {@link Map} containing {@link PropertyMap}'s as keys and
   *                unmodifiable {@link Map}'s as values. The value Map's contains
   *                {@link net.infonode.properties.base.Property}'s as keys and
   *                {@link net.infonode.util.ValueChange}'s as values.
   */
  void propertyValuesChanged(Map changes);
}
