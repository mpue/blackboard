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


//$Id: SlimFlatDockingTheme.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.theme;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.border.Border;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.properties.ViewTitleBarStateProperties;
import net.infonode.docking.properties.WindowBarProperties;
import net.infonode.docking.properties.WindowTabProperties;
import net.infonode.gui.icon.button.CloseIcon;
import net.infonode.gui.icon.button.DockIcon;
import net.infonode.gui.icon.button.MaximizeIcon;
import net.infonode.gui.icon.button.MinimizeIcon;
import net.infonode.gui.icon.button.RestoreIcon;
import net.infonode.gui.icon.button.UndockIcon;
import net.infonode.tabbedpanel.TabLayoutPolicy;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.tabbedpanel.theme.SmallFlatTheme;

/**
 * A theme very slim theme that doesn't waste any screen space.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public final class SlimFlatDockingTheme extends DockingWindowsTheme {
  private RootWindowProperties rootWindowProperties;

  public SlimFlatDockingTheme() {
    rootWindowProperties = createRootWindowProperties();
  }

  public String getName() {
    return "Slim Flat Theme";
  }

  public RootWindowProperties getRootWindowProperties() {
    return rootWindowProperties;
  }

  /**
   * Create a root window properties object with the property values for this theme.
   *
   * @return the root window properties object
   */
  public static final RootWindowProperties createRootWindowProperties() {
    SmallFlatTheme smallFlatTheme = new SmallFlatTheme();

    RootWindowProperties rootWindowProperties = new RootWindowProperties();
    rootWindowProperties.getWindowAreaProperties()
        .setInsets(new Insets(0, 0, 0, 0))
        .setBorder(null);

    rootWindowProperties.getSplitWindowProperties().setDividerSize(3);

    TabbedPanelProperties tpProperties = rootWindowProperties.getTabWindowProperties().getTabbedPanelProperties();
    tpProperties.addSuperObject(smallFlatTheme.getTabbedPanelProperties());
    tpProperties.setShadowEnabled(false).setTabLayoutPolicy(TabLayoutPolicy.COMPRESSION);
    tpProperties.getTabAreaComponentsProperties().getComponentProperties().setInsets(new Insets(0, 1, 0, 1));

    WindowTabProperties tabProperties = rootWindowProperties.getTabWindowProperties().getTabProperties();
    tabProperties.getTitledTabProperties().addSuperObject(smallFlatTheme.getTitledTabProperties());

    Font font = tabProperties.getTitledTabProperties().getHighlightedProperties().getComponentProperties().getFont();
    if (font != null)
      font = font.deriveFont(
          tabProperties.getTitledTabProperties().getNormalProperties().getComponentProperties().getFont().getSize2D());
    tabProperties.getTitledTabProperties().getHighlightedProperties().getComponentProperties().setFont(font);

    Icon closeIcon = new CloseIcon(8);
    Icon restoreIcon = new RestoreIcon(8);
    Icon minimizeIcon = new MinimizeIcon(8);
    Icon maximizeIcon = new MaximizeIcon(8);
    Icon dockIcon = new DockIcon(8);
    Icon undockIcon = new UndockIcon(8);

    tabProperties.getNormalButtonProperties().getCloseButtonProperties().setIcon(closeIcon);
    tabProperties.getNormalButtonProperties().getRestoreButtonProperties().setIcon(restoreIcon);
    tabProperties.getNormalButtonProperties().getMinimizeButtonProperties().setIcon(minimizeIcon);
    tabProperties.getNormalButtonProperties().getDockButtonProperties().setIcon(dockIcon);
    tabProperties.getNormalButtonProperties().getUndockButtonProperties().setIcon(undockIcon);

    rootWindowProperties.getTabWindowProperties().getCloseButtonProperties().setIcon(closeIcon);
    rootWindowProperties.getTabWindowProperties().getRestoreButtonProperties().setIcon(restoreIcon);
    rootWindowProperties.getTabWindowProperties().getMinimizeButtonProperties().setIcon(minimizeIcon);
    rootWindowProperties.getTabWindowProperties().getMaximizeButtonProperties().setIcon(maximizeIcon);
    rootWindowProperties.getTabWindowProperties().getDockButtonProperties().setIcon(dockIcon);
    rootWindowProperties.getTabWindowProperties().getUndockButtonProperties().setIcon(undockIcon);

    ViewTitleBarStateProperties stateProps = rootWindowProperties.getViewProperties().getViewTitleBarProperties()
        .getNormalProperties();
    stateProps.getCloseButtonProperties().setIcon(closeIcon);
    stateProps.getRestoreButtonProperties().setIcon(restoreIcon);
    stateProps.getMaximizeButtonProperties().setIcon(maximizeIcon);
    stateProps.getMinimizeButtonProperties().setIcon(minimizeIcon);
    stateProps.getDockButtonProperties().setIcon(dockIcon);
    stateProps.getUndockButtonProperties().setIcon(undockIcon);

    rootWindowProperties.getViewProperties().getViewTitleBarProperties().getNormalProperties().getComponentProperties()
        .setFont(tabProperties.getTitledTabProperties().getNormalProperties().getComponentProperties().getFont());

    setWindowBarProperties(rootWindowProperties.getWindowBarProperties());

    return rootWindowProperties;
  }

  private static void setWindowBarProperties(WindowBarProperties windowBarProperties) {
    windowBarProperties.setMinimumWidth(3);

    Border border = new TabAreaLineBorder(false, true, true, false);

    windowBarProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().getNormalProperties()
        .getComponentProperties().setInsets(new Insets(0, 4, 0, 4))
        .setBorder(border);

    windowBarProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().getHighlightedProperties()
        .getComponentProperties().setBorder(border);
  }

  /**
   * Create a window bar properties object with the property values for this theme.
   *
   * @return the root window properties object
   * @deprecated the window bar properties are now included in the root window properties
   */
  public static final WindowBarProperties createWindowBarProperties() {
    return new WindowBarProperties();
  }
}
