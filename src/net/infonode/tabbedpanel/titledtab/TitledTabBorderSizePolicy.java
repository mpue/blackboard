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


// $Id: TitledTabBorderSizePolicy.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.tabbedpanel.titledtab;

import net.infonode.util.Enum;

/**
 * TitledTabBorderSizePolicy defines how the insets for the titled tab
 * should be calculated based on the borders for the different tab states.
 * If the states have borders with different insets titled tab can use the same
 * insets for each state based on the maximum insets (top, left, bottom,
 * right) for each state. The compensated insets will be added on the inside
 * of the borders.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @see TitledTab
 * @see TitledTabProperties
 */
public final class TitledTabBorderSizePolicy extends Enum {
  private static final long serialVersionUID = 5570620861429391549L;

  /**
   * Equal size policy. This means that if the different tab states have
   * borders with different insets titled tab will use the same insets for
   * each state based on the maximum insets (top, left, bottom, right)
   * for each state. The compensated insets will be added on the inside of
   * the borders.
   */
  public static final TitledTabBorderSizePolicy EQUAL_SIZE = new TitledTabBorderSizePolicy(0, "Equal Size");

  /**
   * Individual size policy. This means that titled tab will use the borders
   * for each state as they are and not modify any insets. If the borders for
   * the different states have different insets, then the titled tab's insets
   * will be different depending on the state the tab is currently in.
   */
  public static final TitledTabBorderSizePolicy INDIVIDUAL_SIZE = new TitledTabBorderSizePolicy(1, "Individual Size");

  /**
   * An array with all size policies
   */
  public static final TitledTabBorderSizePolicy[] SIZE_POLICIES = new TitledTabBorderSizePolicy[]{EQUAL_SIZE,
                                                                                                  INDIVIDUAL_SIZE};

  private TitledTabBorderSizePolicy(int value, String name) {
    super(value, name);
  }

  /**
   * Gets the titled tab border size policies.
   *
   * @return the titled tab border size policies
   * @since ITP 1.1.0
   */
  public static TitledTabBorderSizePolicy[] getSizePolicies() {
    return (TitledTabBorderSizePolicy[]) SIZE_POLICIES.clone();
  }
}
