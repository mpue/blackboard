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


// $Id: SmallFlatTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel.theme;

import java.awt.Font;
import java.awt.Insets;

import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.border.OpenContentBorder;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * A theme with small fonts and flat look
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class SmallFlatTheme extends TabbedPanelTitledTabTheme {
  private TitledTabProperties tabProperties = new TitledTabProperties();
  private TabbedPanelProperties tabbedPanelProperties = new TabbedPanelProperties();

  /**
   * Constructs a SmallFlatTheme
   */
  public SmallFlatTheme() {
    TitledTabProperties tabDefaultProp = TitledTabProperties.getDefaultProperties();

    TabAreaLineBorder border = new TabAreaLineBorder();
    Font font = tabDefaultProp.getNormalProperties().getComponentProperties().getFont();
    if (font != null)
      font = font.deriveFont((float) 9);
    tabProperties.getNormalProperties().getComponentProperties()
        .setBorder(border)
        .setFont(font)
        .setInsets(new Insets(0, 4, 0, 4));
    tabProperties.getHighlightedProperties().getComponentProperties()
        .setBorder(border);
    tabProperties.setHighlightedRaised(0);

    tabbedPanelProperties
        .getContentPanelProperties().getComponentProperties().setBorder(new OpenContentBorder());

    tabbedPanelProperties.getTabAreaComponentsProperties().getComponentProperties().setBorder(new TabAreaLineBorder());
  }

  /**
   * Gets the name for this theme
   *
   * @return the name
   * @since ITP 1.1.0
   */
  public String getName() {
    return "Small Flat Theme";
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
