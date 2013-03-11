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


// $Id: MixedViewHandler.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;

/**
 * The mixed view map simplifies mixing static and dynamic views inside the same root window.
 * The static views are handled by an {@link AbstractViewMap} and the dynamic views are handled
 * by an custom {@link ViewSerializer}.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
public class MixedViewHandler implements ViewFactoryManager, ViewSerializer {
  private AbstractViewMap viewMap;
  private ViewSerializer viewSerializer;

  /**
   * Constructor.
   *
   * @param viewMap        this map is first searched when serializing a view
   * @param viewSerializer is used if the view was not found in the <tt>viewMap</tt>
   */
  public MixedViewHandler(AbstractViewMap viewMap, ViewSerializer viewSerializer) {
    this.viewMap = viewMap;
    this.viewSerializer = viewSerializer;
  }

  public ViewFactory[] getViewFactories() {
    return new ViewFactory[0];
  }

  public void writeView(View view, ObjectOutputStream out) throws IOException {
    if (viewMap.contains(view)) {
      out.writeBoolean(true);
      viewMap.writeView(view, out);
    }
    else {
      out.writeBoolean(false);
      viewSerializer.writeView(view, out);
    }
  }

  public View readView(ObjectInputStream in) throws IOException {
    return in.readBoolean() ? viewMap.readView(in) : viewSerializer.readView(in);
  }

}
