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


// $Id: WindowSplitLocation.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.location;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.util.Direction;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class WindowSplitLocation extends AbstractWindowLocation {
  private Direction direction;
  private float dividerLocation;

  public WindowSplitLocation(DockingWindow splitWith,
                             WindowLocation parentLocation,
                             Direction direction,
                             float dividerLocation) {
    super(splitWith, parentLocation);
    this.direction = direction;
    this.dividerLocation = dividerLocation;
  }

  private WindowSplitLocation(Direction direction, float dividerLocation) {
    this.direction = direction;
    this.dividerLocation = dividerLocation;
  }

  public boolean set(DockingWindow parent, DockingWindow child) {
    parent.split(child, direction, dividerLocation);
    return true;
  }

  public void write(ObjectOutputStream out) throws IOException {
    out.writeInt(LocationDecoder.SPLIT);
    direction.write(out);
    out.writeFloat(dividerLocation);
    super.write(out);
  }

  public static WindowSplitLocation decode(ObjectInputStream in, RootWindow rootWindow) throws IOException {
    WindowSplitLocation location = new WindowSplitLocation(Direction.decode(in), in.readFloat());
    location.read(in, rootWindow);
    return location;
  }
}
