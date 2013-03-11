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


// $Id: AntUtils.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.util;

/**
 * Utility functions for Ant build environment
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class AntUtils {
  public static ProductVersion createProductVersion(int major, int minor, int patch) {
    return new ProductVersion(major, minor, patch);
  }

  public static ProductVersion createProductVersion(String major, String minor, String patch) {
    return createProductVersion(0, 0, 0);
  }

  public static long getBuildTime(long time) {
    return time;
  }

  public static long getBuildTime(String time) {
    return getBuildTime(System.currentTimeMillis());
  }
}
