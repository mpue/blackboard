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


// $Id: ArrowIcon.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui.icon.button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import net.infonode.util.Direction;

public class ArrowIcon extends AbstractButtonIcon {
  private Direction direction;

  public ArrowIcon(Direction direction) {
    this.direction = direction;
  }

  public ArrowIcon(Color color, Direction direction) {
    super(color);
    this.direction = direction;
  }

  public ArrowIcon(Color color, int size, Direction direction) {
    super(color, size);
    this.direction = direction;
  }

  public ArrowIcon(int size, Direction direction) {
    this(size, direction, true);
  }

  public ArrowIcon(int size, Direction direction, boolean enabled) {
    super(size, enabled);
    this.direction = direction;
  }

  public Direction getDirection() {
    return direction;
  }

  protected void paintIcon(Component c, Graphics g, int x1, int y1, int x2, int y2) {
    int size = (x2 - x1 + 1) + ((x2 - x1 + 1) % 2) - 1;
    int offset = (direction.isHorizontal() ? x1 : y1) +
                 (direction == Direction.RIGHT || direction == Direction.DOWN ?
                  (size + 1) / 4 : (size - (size + 1) / 2) / 2);
    int o2 = direction.isHorizontal() ? y1 : x1;
    int[] c1 = direction == Direction.DOWN || direction == Direction.RIGHT ?
               new int[]{offset, offset, offset + size / 2 + 1} :
               new int[]{offset + size / 2 + 1, offset + size / 2 + 1, offset - (direction == Direction.UP ? 1 : 0)};
    int[] c2 = {o2 + (direction == Direction.DOWN ? 0 : -1),
                o2 + size + (direction == Direction.UP ? 1 : 0),
                o2 + size / 2};
    g.fillPolygon(direction.isHorizontal() ? c1 : c2, direction.isHorizontal() ? c2 : c1, 3);
  }
}
