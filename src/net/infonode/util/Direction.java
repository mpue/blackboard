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


// $Id: Direction.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.util;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * An enum class for directions, up, down, left, right.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
final public class Direction extends Enum {
  private static final long serialVersionUID = 1;

  /**
   * Up direction.
   */
  public static final Direction UP = new Direction(0, "Up", false);

  /**
   * Right direction.
   */
  public static final Direction RIGHT = new Direction(1, "Right", true);

  /**
   * Down direction.
   */
  public static final Direction DOWN = new Direction(2, "Down", false);

  /**
   * Left direction.
   */
  public static final Direction LEFT = new Direction(3, "Left", true);

  /**
   * Array containing all directions.
   */
  public static final Direction[] DIRECTIONS = {UP, RIGHT, DOWN, LEFT};

  static {
    UP.rotateCW = RIGHT;
    RIGHT.rotateCW = DOWN;
    DOWN.rotateCW = LEFT;
    LEFT.rotateCW = UP;
  }

  private transient Direction rotateCW;
  private transient boolean isHorizontal;

  private Direction(int value, String name, boolean isHorizontal) {
    super(value, name);
    this.isHorizontal = isHorizontal;
  }

  /**
   * Returns the direction that is one quarter of a revolution clock wise.
   *
   * @return the direction that is one quarter of a revolution clock wise
   */
  public Direction getNextCW() {
    return rotateCW;
  }

  /**
   * Returns the direction that is one quarter of a revolution counter clock wise.
   *
   * @return the direction that is one quarter of a revolution counter clock wise
   */
  public Direction getNextCCW() {
    return rotateCW.rotateCW.rotateCW;
  }

  /**
   * Returns true if the direction is horizontal.
   *
   * @return true if the direction is horizontal
   */
  public boolean isHorizontal() {
    return isHorizontal;
  }

  /**
   * Returns the opposite direction.
   *
   * @return the opposite direction
   */
  public Direction getOpposite() {
    return getNextCW().getNextCW();
  }

  /**
   * Gets all directions.
   *
   * @return all directions
   * @since 1.1.0
   */
  public static Direction[] getDirections() {
    return (Direction[]) DIRECTIONS.clone();
  }

  /**
   * Decodes a direction from a stream.
   *
   * @param in the stream containing the direction
   * @return the direction
   * @throws IOException if there is a stream error
   */
  public static Direction decode(ObjectInputStream in) throws IOException {
    return (Direction) decode(Direction.class, in);
  }

}
