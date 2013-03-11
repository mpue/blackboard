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


// $Id: Alignment.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.util;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * An enum class for alignments, left, center, right, top, bottom.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public final class Alignment extends Enum {
  private static final long serialVersionUID = 4945539895437047593L;

  /**
   * Left alignment.
   */
  public static final Alignment LEFT = new Alignment(0, "Left");

  /**
   * Center alignment.
   */
  public static final Alignment CENTER = new Alignment(1, "Center");

  /**
   * Right alignment.
   */
  public static final Alignment RIGHT = new Alignment(2, "Right");

  /**
   * Top alignment.
   */
  public static final Alignment TOP = new Alignment(3, "Top");

  /**
   * Bottom alignment.
   */
  public static final Alignment BOTTOM = new Alignment(4, "Bottom");

  /**
   * Array containing all alignments..
   */
  public static final Alignment[] ALIGNMENTS = {LEFT, CENTER, RIGHT, TOP, BOTTOM};

  /**
   * Array containing all horizontal alignments..
   */
  public static final Alignment[] HORIZONTAL_ALIGNMENTS = {LEFT, CENTER, RIGHT};

  /**
   * Array containing all vertical alignments..
   */
  public static final Alignment[] VERTICAL_ALIGNMENTS = {TOP, CENTER, BOTTOM};

  private Alignment(int value, String name) {
    super(value, name);
  }

  /**
   * Gets the alignments.
   *
   * @return the alignments
   * @since 1.1.0
   */
  public static Alignment[] getAlignments() {
    return (Alignment[]) ALIGNMENTS.clone();
  }

  /**
   * Gets the horizontal alignments.
   *
   * @return the horizontal alignments
   * @since 1.1.0
   */
  public static Alignment[] getHorizontalAlignments() {
    return (Alignment[]) HORIZONTAL_ALIGNMENTS.clone();
  }

  /**
   * Gets the vertical alignments.
   *
   * @return the vertical alignments
   * @since 1.1.0
   */
  public static Alignment[] getVerticalAlignments() {
    return (Alignment[]) VERTICAL_ALIGNMENTS.clone();
  }

  /**
   * Decodes an alignment from a stream.
   *
   * @param in the stream containing the alignment
   * @return the alignment
   * @throws IOException if there is a stream error
   */
  public static Alignment decode(ObjectInputStream in) throws IOException {
    return (Alignment) decode(Alignment.class, in);
  }
}
