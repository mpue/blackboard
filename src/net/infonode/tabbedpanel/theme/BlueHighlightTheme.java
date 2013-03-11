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


// $Id: BlueHighlightTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel.theme;

import java.awt.Color;

import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import net.infonode.gui.border.HighlightBorder;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * A theme with dark borders and blue (or custom color) background for the
 * highlighted state.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class BlueHighlightTheme extends TabbedPanelTitledTabTheme {
  private TitledTabProperties tabProperties = new TitledTabProperties();
  private TabbedPanelProperties tabbedPanelProperties = new TabbedPanelProperties();

  /**
   * Constructs a BlueHighlightTheme
   */
  public BlueHighlightTheme() {
    this(new Color(90, 120, 200));
  }

  /**
   * Constructs a BlueHighlightTheme
   *
   * @param backgroundColor the background color for the highlighted tab
   */
  public BlueHighlightTheme(Color backgroundColor) {
    tabProperties.getHighlightedProperties().getComponentProperties()
        .setForegroundColor(Color.WHITE)
        .setBackgroundColor(backgroundColor)
        .setBorder(new TabAreaLineBorder(Color.BLACK));

    tabbedPanelProperties.getContentPanelProperties().getComponentProperties().setBorder(new CompoundBorder(
        new LineBorder(Color.BLACK),
        new HighlightBorder()));
    tabbedPanelProperties.getTabAreaComponentsProperties().getComponentProperties().setBorder(
        new TabAreaLineBorder(Color.BLACK));
  }

  /**
   * Gets the name for this theme
   *
   * @return the name
   * @since ITP 1.1.0
   */
  public String getName() {
    return "Blue Highlight Theme";
  }

  /**
   * Gets the TitledTabProperties for this theme
   *
   * @return the TitledTabProperties
   */
  public TitledTabProperties getTitledTabProperties() {
    return tabProperties;
  }

  /**
   * Gets the TabbedPanelProperties for this theme
   *
   * @return the TabbedPanelProperties
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return tabbedPanelProperties;
  }
}
