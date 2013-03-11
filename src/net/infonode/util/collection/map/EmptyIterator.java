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


// $Id: EmptyIterator.java,v 1.2 2011-08-26 15:10:44 mpue Exp $
package net.infonode.util.collection.map;

import net.infonode.util.collection.map.base.MapIterator;

public class EmptyIterator implements MapIterator {
  public static final EmptyIterator INSTANCE = new EmptyIterator();

  private EmptyIterator() {
  }

  public void remove() {
  }

  public Object getKey() {
    return null;
  }

  public Object getValue() {
    return null;
  }

  public void next() {
  }

  public boolean atEntry() {
    return false;
  }
}
