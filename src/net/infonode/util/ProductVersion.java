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


//$Id: ProductVersion.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.util;

import java.io.Serializable;

/**
 * A class that represents a product version
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class ProductVersion implements Serializable {
  private static final long serialVersionUID = 1;

  private int major;
  private int minor;
  private int patch;

  /**
   * Constructs a product version object
   *
   * @param major Major version number
   * @param minor Minor version number
   * @param patch Patch version number
   */
  public ProductVersion(int major, int minor, int patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  /**
   * Gets the major version number, i.e.
   * the number X in version X.0.0
   *
   * @return Major version number
   */
  public int getMajor() {
    return major;
  }

  /**
   * Gets the minor version number, i.e.
   * the number X in version 0.X.0
   *
   * @return Minor version number
   */
  public int getMinor() {
    return minor;
  }

  /**
   * Gets the patch version number, i.e.
   * the number X in version 0.0.X
   *
   * @return Minor version number
   */
  public int getPatch() {
    return patch;
  }

  /**
   * Gets the version as string
   *
   * @return Version as string
   */
  public String toString() {
    return major + "." + minor + "." + patch;
  }
}
