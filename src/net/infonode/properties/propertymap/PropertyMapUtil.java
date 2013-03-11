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


// $Id: PropertyMapUtil.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.properties.propertymap;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Property map utility methods.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.3.0
 */
public final class PropertyMapUtil {
  private PropertyMapUtil() {
  }

  /**
   * Skips a property map in the stream.
   *
   * @param in the stream containing the property map
   * @throws IOException if there is an error in the stream
   */
  public static void skipMap(ObjectInputStream in) throws IOException {
    PropertyMapImpl.skip(in);
  }

}
