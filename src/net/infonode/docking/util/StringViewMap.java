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


// $Id: StringViewMap.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.infonode.docking.View;

/**
 * A map of views that handles view serialization by assigning a string id to each view.
 * The id is unique for each view in the map. To guarantee serialization compatibility a view id must remain constant.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.1.0
 */
public class StringViewMap extends AbstractViewMap {
  /**
   * Constructor.
   */
  public StringViewMap() {
  }

  /**
   * Utility constructor that creates a map with a number of views.
   * A view gets it's title as id.
   *
   * @param views the views to add to the map
   */
  public StringViewMap(View[] views) {
    for (int i = 0; i < views.length; i++)
      addView(views[i]);
  }

  /**
   * Adds a view to the map.
   * The view title is used as id.
   *
   * @param view the view
   */
  public void addView(View view) {
    addView(view.getTitle(), view);
  }

  /**
   * Adds a view to the map.
   *
   * @param id   the view id
   * @param view the view
   */
  public void addView(String id, View view) {
    addView((Object) id, view);
  }

  /**
   * Removes a view with a specific id from the map.
   *
   * @param id the view id
   */
  public void removeView(String id) {
    removeView((Object) id);
  }

  /**
   * Returns the view with a specific id.
   *
   * @param id the view id
   * @return the view with the id
   */
  public View getView(String id) {
    return getView((Object) id);
  }

  protected void writeViewId(Object id, ObjectOutputStream out) throws IOException {
    out.writeUTF((String) id);
  }

  protected Object readViewId(ObjectInputStream in) throws IOException {
    return in.readUTF();
  }
}
