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


// $Id: GradientDockingTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.theme;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.properties.WindowBarProperties;
import net.infonode.gui.colorprovider.ColorMultiplier;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.tabbedpanel.border.OpenContentBorder;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.tabbedpanel.theme.GradientTheme;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;

/**
 * A theme that draws gradient tab backgrounds.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.1.0
 */
public class GradientDockingTheme extends DockingWindowsTheme {
  private boolean opaqueTabArea;
  private boolean shadowEnabled;
  private boolean highlightedBold;
  private boolean focusHighlighterEnabled;
  private Color borderColor;
  private Color tabAreaBackgroundColor;
  private RootWindowProperties rootProperties;

  /**
   * Creates a default theme with opaque title bar, shadows and focus highlighter.
   */
  public GradientDockingTheme() {
    this(true, true, false, true);
  }

  /**
   * Constructor.
   *
   * @param opaqueTabArea           set to true if the tab area should be opaque
   * @param shadowEnabled           shadow on/off
   * @param highlightedBold         if true the highlighted tab text uses a bold font
   * @param focusHighlighterEnabled if true the currently focused tab is highlighted
   */
  public GradientDockingTheme(boolean opaqueTabArea, boolean shadowEnabled, boolean highlightedBold,
                              boolean focusHighlighterEnabled) {
    this(opaqueTabArea, shadowEnabled, highlightedBold, focusHighlighterEnabled, Color.BLACK);
  }

  /**
   * Constructor.
   *
   * @param opaqueTabArea           set to true if the tab area should be opaque
   * @param shadowEnabled           shadow on/off
   * @param highlightedBold         if true the highlighted tab text uses a bold font
   * @param focusHighlighterEnabled if true the currently focused tab is highlighted
   * @param borderColor             the border color
   */
  public GradientDockingTheme(boolean opaqueTabArea, boolean shadowEnabled, boolean highlightedBold,
                              boolean focusHighlighterEnabled, Color borderColor) {
    this(opaqueTabArea, shadowEnabled, highlightedBold, focusHighlighterEnabled, borderColor,
         GradientTheme.DEFAULT_TAB_AREA_BACKGROUND_COLOR);
  }

  /**
   * Constructor.
   *
   * @param opaqueTabArea           set to true if the tab area should be opaque
   * @param shadowEnabled           shadow on/off
   * @param highlightedBold         if true the highlighted tab text uses a bold font
   * @param focusHighlighterEnabled if true the currently focused tab is highlighted
   * @param borderColor             the border color
   * @param tabAreaBackgroundColor  the background color for the tab area and tabs in the normal state
   */
  public GradientDockingTheme(boolean opaqueTabArea, boolean shadowEnabled, boolean highlightedBold,
                              boolean focusHighlighterEnabled, Color borderColor, Color tabAreaBackgroundColor) {
    this.opaqueTabArea = opaqueTabArea;
    this.shadowEnabled = shadowEnabled;
    this.highlightedBold = highlightedBold;
    this.focusHighlighterEnabled = focusHighlighterEnabled;
    this.borderColor = borderColor;
    this.tabAreaBackgroundColor = tabAreaBackgroundColor;

    GradientTheme theme = new GradientTheme(opaqueTabArea, shadowEnabled, borderColor);

    rootProperties = new RootWindowProperties();
    createRootWindowProperties(theme);
    createWindowBarProperties(theme);
  }

  private void createRootWindowProperties(GradientTheme theme) {
    rootProperties.getTabWindowProperties().getTabbedPanelProperties().addSuperObject(theme.getTabbedPanelProperties());
    rootProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().addSuperObject(
        theme.getTitledTabProperties());

    rootProperties.getTabWindowProperties().getCloseButtonProperties().setVisible(false);

    if (!shadowEnabled)
      rootProperties.getWindowAreaProperties().setInsets(new Insets(6, 6, 6, 6));

    rootProperties.getWindowAreaShapedPanelProperties().setComponentPainter(new GradientComponentPainter(
        UIManagerColorProvider.DESKTOP_BACKGROUND,
        new ColorMultiplier(UIManagerColorProvider.DESKTOP_BACKGROUND, 0.9f),
        new ColorMultiplier(UIManagerColorProvider.DESKTOP_BACKGROUND, 0.9f),
        new ColorMultiplier(UIManagerColorProvider.DESKTOP_BACKGROUND, 0.8f)));

    rootProperties.getWindowAreaProperties().setBorder(new LineBorder(Color.BLACK));

    if (tabAreaBackgroundColor != null)
      rootProperties.getComponentProperties().setBackgroundColor(tabAreaBackgroundColor);

    if (!shadowEnabled)
      rootProperties.getSplitWindowProperties().setDividerSize(6);

    TitledTabProperties tabProperties = rootProperties.getTabWindowProperties().getTabProperties()
        .getTitledTabProperties();

    tabProperties.getNormalProperties().setIconVisible(false);
    tabProperties.getHighlightedProperties().setIconVisible(true);

    if (!highlightedBold)
      tabProperties.getHighlightedProperties().
          getComponentProperties().getMap().
          createRelativeRef(ComponentProperties.FONT,
                            tabProperties.getNormalProperties().getComponentProperties().getMap(),
                            ComponentProperties.FONT);

    if (focusHighlighterEnabled) {
      tabProperties.getHighlightedProperties().getComponentProperties()
          .setBorder(new CompoundBorder(opaqueTabArea ?
                                        (Border) new TabAreaLineBorder(false, false, true, true) :
                                        new TabAreaLineBorder(borderColor),
                                        theme.getTabAreaComponentsGradientBorder()));

      rootProperties.getTabWindowProperties().getTabProperties().getFocusedProperties().getComponentProperties()
          .setBorder(new CompoundBorder(opaqueTabArea ?
                                        (Border) new TabAreaLineBorder(false, false, true, true) :
                                        new TabAreaLineBorder(borderColor),
                                        theme.getHighlightedTabGradientBorder()));
    }

    rootProperties.getTabWindowProperties().getTabbedPanelProperties().getTabAreaComponentsProperties()
        .getComponentProperties().setInsets(opaqueTabArea ? new Insets(0, 3, 0, 3) : new Insets(1, 3, 1, 3));


    rootProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getCloseButtonProperties()
        .setVisible(false);

    rootProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getMinimizeButtonProperties()
        .setVisible(true);

    rootProperties.getTabWindowProperties().getTabProperties().getHighlightedButtonProperties()
        .getRestoreButtonProperties()
        .setVisible(true);
  }

  private void createWindowBarProperties(GradientTheme theme) {
    WindowBarProperties barProperties = rootProperties.getWindowBarProperties();

    barProperties.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties()
        .getComponentProperties()
        .setBorder(new OpenContentBorder(Color.BLACK, 1));

    barProperties.getTabWindowProperties().getTabProperties().getNormalButtonProperties().
        getCloseButtonProperties().setVisible(false);

    barProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().getNormalProperties()
        .setIconVisible(true)
        .getComponentProperties()
        .setBorder(new CompoundBorder(new TabAreaLineBorder(), theme.getTabAreaComponentsGradientBorder()));

    barProperties.getTabWindowProperties().getTabProperties().getFocusedProperties()
        .getComponentProperties()
        .setBorder(new CompoundBorder(new TabAreaLineBorder(Color.BLACK), theme.getHighlightedTabGradientBorder()));

    barProperties.getTabWindowProperties().getTabProperties().getTitledTabProperties().getHighlightedProperties()
        .getComponentProperties()
        .setBorder(new CompoundBorder(new TabAreaLineBorder(Color.BLACK), theme.getHighlightedTabGradientBorder()));

    barProperties.getTabWindowProperties().getTabbedPanelProperties().setTabSpacing(-1);

    barProperties.getTabWindowProperties().getTabbedPanelProperties().getTabAreaProperties().getComponentProperties()
        .setBorder(null)
        .setBackgroundColor(null);
  }

  public String getName() {
    String str = (opaqueTabArea ? "" : "Transparent Tab Area, ") +
                 (shadowEnabled ? "" : "No Shadow, ") +
                 (focusHighlighterEnabled ? "" : "No Focus Highlight, ") +
                 (highlightedBold ? "Highlighted Bold, " : "");
    return "Gradient Theme" + (str.length() > 0 ? " - " + str.substring(0, str.length() - 2) : "");
  }

  public RootWindowProperties getRootWindowProperties() {
    return rootProperties;
  }

}
