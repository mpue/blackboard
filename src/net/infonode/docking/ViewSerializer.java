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


// $Id: ViewSerializer.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.docking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Reads and writes the state of a view.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public interface ViewSerializer {
  /**
   * Writes a view to a stream.
   * Note that the view property values are written automatically, so this method should not write them.
   *
   * @param view the view to write
   * @param out  the stream on which to write the view
   * @throws IOException if there is a stream error
   */
  void writeView(View view, ObjectOutputStream out) throws IOException;

  /**
   * Reads and returns a view.
   * Must read all the data written in the {@link #writeView} method.
   * Note that the view property values are read automatically, so this method should not read them.
   * This method should return null if the serialized view can't be resolved.
   *
   * @param in the stream from which to read the view state
   * @return the view, null if the view can't be resolved
   * @throws IOException if there is a stream error
   */
  View readView(ObjectInputStream in) throws IOException;

}
