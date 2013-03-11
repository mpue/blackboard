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


// $Id: AbstractViewMap.java,v 1.4 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Icon;

import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;

/**
 * Base class for view maps.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.4 $
 * @since IDW 1.1.0
 */
abstract public class AbstractViewMap implements ViewFactoryManager, ViewSerializer {
  private HashMap viewMap = new HashMap();
  private ArrayList views = new ArrayList(20);

  abstract protected void writeViewId(Object id, ObjectOutputStream out) throws IOException;

  abstract protected Object readViewId(ObjectInputStream in) throws IOException;

  /**
   * Returns the number of views in this map.
   *
   * @return the number of views in this map
   */
  public int getViewCount() {
    return viewMap.size();
  }

  /**
   * Returns the view at a specific index.
   * The view index is the same as the number of views in the map when the view was added to the map.
   *
   * @param index the view index
   * @return the view at the index
   */
  public View getViewAtIndex(int index) {
    return (View) views.get(index);
  }

  public ViewFactory[] getViewFactories() {
    ArrayList f = new ArrayList();

    for (int i = 0; i < views.size(); i++) {
      final View view = (View) views.get(i);

      if (view.getRootWindow() == null)
        f.add(new ViewFactory() {
          public Icon getIcon() {
            return view.getIcon();
          }

          public String getTitle() {
            return view.getTitle();
          }

          public View createView() {
            return view;
          }
        });
    }

    return (ViewFactory[]) f.toArray(new ViewFactory[f.size()]);
  }

  /**
   * Returns true if this view map contains the view.
   *
   * @param view the view
   * @return true if this view map contains the view
   * @since IDW 1.3.0
   */
  public boolean contains(View view) {
    return views.contains(view);
  }

  public void writeView(View view, ObjectOutputStream out) throws IOException {
    for (Iterator it = viewMap.entrySet().iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      
      if (entry.getValue() == view) {
        writeViewId(entry.getKey(), out);
        // return;
      }
    }

    // throw new IOException("Serialization of unknown view!");
  }

  public View readView(ObjectInputStream in) throws IOException {
    return (View) viewMap.get(readViewId(in));
  }

  protected void addView(Object id, View view) {
    Object oldView = viewMap.put(id, view);

    if (oldView != null)
      views.remove(oldView);

    views.add(view);
  }

  protected void removeView(Object id) {
	
    Object view = viewMap.remove(id);

    if (view != null)
      views.remove(view);
  }

  protected View getView(Object id) {
    return (View) viewMap.get(id);
  }

}
