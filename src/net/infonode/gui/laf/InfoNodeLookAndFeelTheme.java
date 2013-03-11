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


// $Id: InfoNodeLookAndFeelTheme.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.laf;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.InsetsUIResource;

import net.infonode.gui.Colors;
import net.infonode.gui.border.EdgeBorder;
import net.infonode.gui.border.EtchedLineBorder;
import net.infonode.gui.border.HighlightBorder;
import net.infonode.gui.border.PopupMenuBorder;
import net.infonode.gui.icon.EmptyIcon;
import net.infonode.gui.icon.button.BorderIcon;
import net.infonode.gui.icon.button.CloseIcon;
import net.infonode.gui.icon.button.MaximizeIcon;
import net.infonode.gui.icon.button.MinimizeIcon;
import net.infonode.gui.icon.button.RestoreIcon;
import net.infonode.gui.icon.button.WindowIcon;
import net.infonode.gui.laf.value.BorderValue;
import net.infonode.gui.laf.value.ColorValue;
import net.infonode.gui.laf.value.FontValue;
import net.infonode.util.ArrayUtil;
import net.infonode.util.ColorUtil;

/**
 * A theme for InfoNode look and feel. The theme infers some default colors from others, so modifying a color might
 * affect other, unmodified colors.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class InfoNodeLookAndFeelTheme {
  private static final float PRIMARY_HUE = Colors.ROYAL_BLUE_HUE;
  private static final float PRIMARY_SATURATION = 0.6f;
  private static final float PRIMARY_BRIGHTNESS = 0.67f;

  public static final Color DEFAULT_CONTROL_COLOR = Color.getHSBColor(Colors.SAND_HUE, 0.058f, 0.89f);
  public static final Color DEFAULT_PRIMARY_CONTROL_COLOR = Color.getHSBColor(PRIMARY_HUE, PRIMARY_SATURATION, 1f);
  public static final Color DEFAULT_BACKGROUND_COLOR = new Color(250, 250, 247);
  public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
  public static final Color DEFAULT_SELECTED_BACKGROUND_COLOR = Color.getHSBColor(PRIMARY_HUE,
                                                                                  PRIMARY_SATURATION + 0.1f,
                                                                                  PRIMARY_BRIGHTNESS);
  public static final Color DEFAULT_SELECTED_TEXT_COLOR = Color.WHITE;
  public static final Color DEFAULT_TOOLTIP_BACKGROUND_COLOR = new Color(255, 255, 180);
  public static final Color DEFAULT_TOOLTIP_FOREGROUND_COLOR = Color.BLACK;
  public static final Color DEFAULT_DESKTOP_COLOR = Color.getHSBColor(PRIMARY_HUE - 0.02f,
                                                                      PRIMARY_SATURATION,
                                                                      PRIMARY_BRIGHTNESS);

  public static final int DEFAULT_FONT_SIZE = 11;

  private static final String[] FONT_NAMES = {/*"Tahoma", */"Dialog"};

  private FontUIResource font = new FontUIResource("Dialog", 0, 11);
  private FontUIResource boldFont;

  private ColorValue controlColor = new ColorValue();
  private ColorValue primaryControlColor = new ColorValue();
  private ColorValue backgroundColor = new ColorValue();
  private ColorValue textColor = new ColorValue();
  private ColorValue selectedTextBackgroundColor = new ColorValue();

  private ColorValue focusColor = new ColorValue();
  private ColorValue selectedTextColor = new ColorValue();
  private ColorValue tooltipBackgroundColor = new ColorValue(DEFAULT_TOOLTIP_BACKGROUND_COLOR);
  private ColorValue tooltipForegroundColor = new ColorValue(DEFAULT_TOOLTIP_FOREGROUND_COLOR);
  private ColorValue desktopColor = new ColorValue(DEFAULT_DESKTOP_COLOR);

  private ColorValue treeIconBackgroundColor = new ColorValue();

  private ColorValue selectedMenuBackgroundColor = new ColorValue();
  private ColorValue selectedMenuForegroundColor = new ColorValue();

  private ColorValue inactiveTextColor = new ColorValue();

  private ColorUIResource controlHighlightColor;
  private ColorUIResource controlLightShadowColor;
  private ColorUIResource controlShadowColor;
  private ColorUIResource controlDarkShadowColor;

  private ColorUIResource primaryControlHighlightColor;
  private ColorUIResource primaryControlShadowColor;
  private ColorUIResource primaryControlDarkShadowColor;

  private ColorValue scrollBarBackgroundColor = new ColorValue();
  private ColorUIResource scrollBarBackgroundShadowColor;

  private ColorValue activeInternalFrameTitleBackgroundColor = new ColorValue();
  private ColorValue activeInternalFrameTitleGradientColor = new ColorValue();
  private ColorValue activeInternalFrameTitleForegroundColor = new ColorValue();
  private ColorValue inactiveInternalFrameTitleBackgroundColor = new ColorValue();
  private ColorValue inactiveInternalFrameTitleGradientColor = new ColorValue();
  private ColorValue inactiveInternalFrameTitleForegroundColor = new ColorValue();
  private IconUIResource internalFrameIcon = new IconUIResource(new BorderIcon(new WindowIcon(Color.BLACK, 12), 2));
  private IconUIResource internalFrameIconifyIcon = new IconUIResource(new MinimizeIcon());
  private IconUIResource internalFrameMinimizeIcon = new IconUIResource(new RestoreIcon());
  private IconUIResource internalFrameMaximizeIcon = new IconUIResource(new MaximizeIcon());
  private IconUIResource internalFrameCloseIcon = new IconUIResource(new CloseIcon());
  private BorderUIResource internalFrameBorder = new BorderUIResource(new LineBorder(Color.BLACK, 2));

  private FontValue internalFrameTitleFont = new FontValue();
  private FontValue optionPaneButtonFont = new FontValue();

  private IconUIResource treeOpenIcon = new IconUIResource(EmptyIcon.INSTANCE);
  private IconUIResource treeClosedIcon = new IconUIResource(EmptyIcon.INSTANCE);
  private IconUIResource treeLeafIcon = new IconUIResource(EmptyIcon.INSTANCE);

  private BorderValue menuBarBorder = new BorderValue();
  private BorderValue popupMenuBorder = new BorderValue();

  private BorderValue tableHeaderCellBorder = new BorderValue();

  private BorderValue textFieldBorder = new BorderValue();

  private BorderValue listItemBorder = new BorderValue(new EmptyBorder(1, 4, 1, 4));
  private BorderValue listFocusedItemBorder = new BorderValue();

  private int splitPaneDividerSize = 7;
  private int scrollBarWidth = 17;
  private InsetsUIResource buttonMargin = new InsetsUIResource(1, 6, 1, 6);

  private double shadingFactor = 1.6;

  private String name;

  /**
   * Creates a default InfoNode look and feel theme.
   */
  public InfoNodeLookAndFeelTheme() {
    this("Default Theme",
         DEFAULT_CONTROL_COLOR,
         DEFAULT_PRIMARY_CONTROL_COLOR,
         DEFAULT_BACKGROUND_COLOR,
         DEFAULT_TEXT_COLOR,
         DEFAULT_SELECTED_BACKGROUND_COLOR,
         DEFAULT_SELECTED_TEXT_COLOR);
  }

  /**
   * Creates a theme with custom colors.
   *
   * @param name                the name of this theme
   * @param controlColor        the background color for buttons, labels etc.
   * @param primaryControlColor the color of scrollbar "knobs", text and menu selection background
   * @param backgroundColor     the background color for viewports, tree's, tables etc.
   * @param textColor           the text color
   */
  public InfoNodeLookAndFeelTheme(String name,
                                  Color controlColor,
                                  Color primaryControlColor,
                                  Color backgroundColor,
                                  Color textColor) {
    this(name,
         controlColor,
         primaryControlColor,
         backgroundColor,
         textColor,
         primaryControlColor,
         ColorUtil.getOpposite(primaryControlColor));
  }

  /**
   * Creates a theme with custom colors.
   *
   * @param name                    the name of this theme
   * @param controlColor            the background color for buttons, labels etc.
   * @param primaryControlColor     the color of scrollbar "knobs"
   * @param backgroundColor         the background color for viewports, tree's, tables etc.
   * @param textColor               the text color
   * @param selectedBackgroundColor the background color for selected text, selected menu items
   * @param selectedTextColor       the text color for selected text, selected menu items
   */
  public InfoNodeLookAndFeelTheme(String name,
                                  Color controlColor,
                                  Color primaryControlColor,
                                  Color backgroundColor,
                                  Color textColor,
                                  Color selectedBackgroundColor,
                                  Color selectedTextColor) {
    this(name,
         controlColor,
         primaryControlColor,
         backgroundColor,
         textColor,
         selectedBackgroundColor,
         selectedTextColor,
         1.3);
  }

  /**
   * Creates a theme with custom colors.
   *
   * @param name                    the name of this theme
   * @param controlColor            the background color for buttons, labels etc.
   * @param primaryControlColor     the color of scrollbar "knobs"
   * @param backgroundColor         the background color for viewports, tree's, tables etc.
   * @param textColor               the text color
   * @param selectedBackgroundColor the background color for selected text, selected menu items
   * @param selectedTextColor       the text color for selected text, selected menu items
   * @param shadingFactor           the shading factor is used when calculating brighter and darker control colors. A
   *                                higher factor gives brighter and darker colors.
   */
  public InfoNodeLookAndFeelTheme(String name,
                                  Color controlColor,
                                  Color primaryControlColor,
                                  Color backgroundColor,
                                  Color textColor,
                                  Color selectedBackgroundColor,
                                  Color selectedTextColor,
                                  double shadingFactor) {
    this.name = name;
    String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    for (int i = 0; i < FONT_NAMES.length; i++) {
      if (ArrayUtil.containsEqual(fontNames, FONT_NAMES[i])) {
        font = new FontUIResource(new Font(FONT_NAMES[i], Font.PLAIN, DEFAULT_FONT_SIZE));
        break;
      }
    }

    updateFonts();

    this.controlColor.setColor(controlColor);
    this.primaryControlColor.setColor(primaryControlColor);
    this.backgroundColor.setColor(backgroundColor);
    this.selectedTextBackgroundColor.setColor(selectedBackgroundColor);
    this.selectedTextColor.setColor(selectedTextColor);
    this.textColor.setColor(textColor);
    this.shadingFactor = shadingFactor;
    updateColors();
  }

  private void updateFonts() {
    boldFont = new FontUIResource(font.deriveFont(Font.BOLD));
    internalFrameTitleFont.setDefaultFont(boldFont);
    optionPaneButtonFont.setDefaultFont(boldFont);
  }

  private void updateColors() {
    focusColor.setDefaultColor(ColorUtil.blend(controlColor.getColor(), textColor.getColor(), 0.5f));

    inactiveTextColor.setDefaultColor(focusColor);

    double invShadeAmount = 1.0 / (1 + shadingFactor * 1.2);
    double invShadeAmount2 = 1.0 / (1 + shadingFactor / 2);
    double invShadeAmount3 = 1.0 / (1 + shadingFactor / 7);

    controlHighlightColor = new ColorUIResource(ColorUtil.mult(controlColor.getColor(), 1 + shadingFactor));
    controlLightShadowColor = new ColorUIResource(ColorUtil.mult(controlColor.getColor(), invShadeAmount3));
    controlShadowColor = new ColorUIResource(ColorUtil.mult(controlColor.getColor(), invShadeAmount2));
    controlDarkShadowColor = new ColorUIResource(ColorUtil.mult(controlColor.getColor(), invShadeAmount));

    primaryControlHighlightColor = controlHighlightColor;
    primaryControlShadowColor = new ColorUIResource(ColorUtil.mult(primaryControlColor.getColor(), invShadeAmount2));
    primaryControlDarkShadowColor =
    new ColorUIResource(ColorUtil.mult(primaryControlColor.getColor(), invShadeAmount));

    scrollBarBackgroundColor.setDefaultColor(controlLightShadowColor);
    scrollBarBackgroundShadowColor = new ColorUIResource(ColorUtil.mult(scrollBarBackgroundColor.getColor(),
                                                                        invShadeAmount));

    selectedMenuBackgroundColor.setDefaultColor(selectedTextBackgroundColor);
    selectedMenuForegroundColor.setDefaultColor(selectedTextColor);

    treeIconBackgroundColor.setDefaultColor(
        ColorUtil.blend(backgroundColor.getColor(), primaryControlColor.getColor(), 0.15f));

    activeInternalFrameTitleBackgroundColor.setDefaultColor(ColorUtil.blend(primaryControlColor.getColor(),
                                                                            ColorUtil.getOpposite(getTextColor()),
                                                                            0.5f));
    activeInternalFrameTitleForegroundColor.setDefaultColor(getTextColor());
    activeInternalFrameTitleGradientColor.setDefaultColor(
        ColorUtil.mult(activeInternalFrameTitleBackgroundColor.getColor(), 1.2));
    inactiveInternalFrameTitleBackgroundColor.setDefaultColor(controlLightShadowColor);
    inactiveInternalFrameTitleForegroundColor.setDefaultColor(getTextColor());
    inactiveInternalFrameTitleGradientColor.setDefaultColor(
        ColorUtil.mult(inactiveInternalFrameTitleBackgroundColor.getColor(), 1.2));

    menuBarBorder.setDefaultBorder(new BorderUIResource(
        new EtchedLineBorder(false, false, true, false, controlHighlightColor, controlDarkShadowColor)));
    popupMenuBorder.setDefaultBorder(
        new BorderUIResource(new PopupMenuBorder(controlHighlightColor, controlDarkShadowColor)));
    textFieldBorder.setDefaultBorder(
        new BorderUIResource(new CompoundBorder(new LineBorder(controlDarkShadowColor), new EmptyBorder(1, 2, 1, 2))));

    tableHeaderCellBorder.setDefaultBorder(new BorderUIResource(new CompoundBorder(new CompoundBorder(
        new EdgeBorder(controlDarkShadowColor, false, true, false, true),
        new HighlightBorder(false, controlHighlightColor)),
                                                                                   new EmptyBorder(1, 4, 1, 4))));

    listFocusedItemBorder.setDefaultBorder(new CompoundBorder(new LineBorder(focusColor.getColor()),
                                                              new EmptyBorder(0, 3, 0, 3)));
  }

  /**
   * Returns the theme name.
   *
   * @return the theme name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the shading factor. The shading factor is used when calculating brighter and darker control colors. A
   * higher factor gives brighter and darker colors.
   *
   * @return the shading factor
   */
  public double getShadingFactor() {
    return shadingFactor;
  }

  /**
   * Sets the shading factor. The shading factor is used when calculating brighter and darker control colors. A higher
   * factor gives brighter and darker colors.
   *
   * @param shadingFactor the shading factor
   */
  public void setShadingFactor(double shadingFactor) {
    this.shadingFactor = shadingFactor;
    updateColors();
  }

  /**
   * Returns the base font. This font is used as default font for all text.
   *
   * @return returns the base font
   */
  public FontUIResource getFont() {
    return font;
  }

  /**
   * Sets the base font. This font is used as default font for all text.
   *
   * @param font the base font
   */
  public void setFont(FontUIResource font) {
    this.font = font;
    updateFonts();
  }

  /**
   * Gets the background color used for {@link javax.swing.JComponent}.
   *
   * @return the background color used for {@link javax.swing.JComponent}
   */
  public ColorUIResource getControlColor() {
    return controlColor.getColor();
  }

  /**
   * Gets the color of scrollbar "knobs" etc.
   *
   * @return the color of scrollbar "knobs" etc,
   */
  public ColorUIResource getPrimaryControlColor() {
    return primaryControlColor.getColor();
  }

  /**
   * Gets the background color for {@link javax.swing.JViewport}, {@link javax.swing.JTree}, {@link javax.swing.JTable}
   * etc.
   *
   * @return the background color for {@link javax.swing.JViewport}, {@link javax.swing.JTree}, {@link
   *         javax.swing.JTable} etc.
   */
  public ColorUIResource getBackgroundColor() {
    return backgroundColor.getColor();
  }

  /**
   * Gets the text color.
   *
   * @return the text color
   */
  public ColorUIResource getTextColor() {
    return textColor.getColor();
  }

  /**
   * Gets the selected text background color.
   *
   * @return the selected text background color
   */
  public ColorUIResource getSelectedTextBackgroundColor() {
    return selectedTextBackgroundColor.getColor();
  }

  /**
   * Gets the control focus marker color.
   *
   * @return the control focus marker color
   */
  public ColorUIResource getFocusColor() {
    return focusColor.getColor();
  }

  /**
   * Gets the selected text color.
   *
   * @return the selected text color
   */
  public ColorUIResource getSelectedTextColor() {
    return selectedTextColor.getColor();
  }

  /**
   * Gets the background color for {@link javax.swing.JToolTip}.
   *
   * @return the background color for {@link javax.swing.JToolTip}
   */
  public ColorUIResource getTooltipBackgroundColor() {
    return tooltipBackgroundColor.getColor();
  }

  /**
   * Gets the desktop color used in {@link javax.swing.JDesktopPane} etc.
   *
   * @return the desktop color used in {@link javax.swing.JDesktopPane} etc.
   */
  public ColorUIResource getDesktopColor() {
    return desktopColor.getColor();
  }

  /**
   * Gets the background color used for collapse and expand icons in a {@link javax.swing.JTree}.
   *
   * @return the background color used for collapse and expand icons in a {@link javax.swing.JTree}
   */
  public ColorUIResource getTreeIconBackgroundColor() {
    return treeIconBackgroundColor.getColor();
  }

  /**
   * Gets the background color used for selected {@link javax.swing.JMenuItem}'s.
   *
   * @return the background color used for selected {@link javax.swing.JMenuItem}'s
   */
  public ColorUIResource getSelectedMenuBackgroundColor() {
    return selectedMenuBackgroundColor.getColor();
  }

  /**
   * Gets the foreground color used for selected {@link javax.swing.JMenuItem}'s.
   *
   * @return the foreground color used for selected {@link javax.swing.JMenuItem}'s
   */
  public ColorUIResource getSelectedMenuForegroundColor() {
    return selectedMenuForegroundColor.getColor();
  }

  /**
   * Gets the color used for inactive text.
   *
   * @return the color used for inactive text
   */
  public ColorUIResource getInactiveTextColor() {
    return inactiveTextColor.getColor();
  }

  /**
   * Gets the control highlight color. By default this is a color a little brighter than the control color.
   *
   * @return the control highlight color
   */
  public ColorUIResource getControlHighlightColor() {
    return controlHighlightColor;
  }

  /**
   * Gets the control light shadow color. By default this is a color a little darker than the control color.
   *
   * @return the control light shadow color
   */
  public ColorUIResource getControlLightShadowColor() {
    return controlLightShadowColor;
  }

  /**
   * Gets the control shadow color. By default this is a color a little darker than the control light shadow color.
   *
   * @return the control shadow color
   */
  public ColorUIResource getControlShadowColor() {
    return controlShadowColor;
  }

  /**
   * Gets the control dark shadow color. By default this is a color a little darker than the control shadow color.
   *
   * @return the control dark shadow color
   */
  public ColorUIResource getControlDarkShadowColor() {
    return controlDarkShadowColor;
  }

  /**
   * Gets the primary control highlight color. By default this color is the same as the control highlight color..
   *
   * @return the primary control highlight color
   */
  public ColorUIResource getPrimaryControlHighlightColor() {
    return primaryControlHighlightColor;
  }

  /**
   * Gets the primary control shadow color. By default this is a color a little darker than the primary control color.
   *
   * @return the primary control shadow color
   */
  public ColorUIResource getPrimaryControlShadowColor() {
    return primaryControlShadowColor;
  }

  /**
   * Gets the primary control dark shadow color. By default this is a color a little darker than the primary control
   * shadow color.
   *
   * @return the primary control dark shadow color
   */
  public ColorUIResource getPrimaryControlDarkShadowColor() {
    return primaryControlDarkShadowColor;
  }

  /**
   * Gets the background color for {@link javax.swing.JScrollBar}'s.
   *
   * @return the background color for {@link javax.swing.JScrollBar}'s
   */
  public ColorUIResource getScrollBarBackgroundColor() {
    return scrollBarBackgroundColor.getColor();
  }

  /**
   * Gets the background shadow color for {@link javax.swing.JScrollBar}'s. By default this is a color a little darker
   * than the scroll bar background color.
   *
   * @return the background color for {@link javax.swing.JScrollBar}'s.
   */
  public ColorUIResource getScrollBarBackgroundShadowColor() {
    return scrollBarBackgroundShadowColor;
  }

  /**
   * Gets the background color for active {@link javax.swing.JInternalFrame}'s.
   *
   * @return the background color for active {@link javax.swing.JInternalFrame}'s
   */
  public ColorUIResource getActiveInternalFrameTitleBackgroundColor() {
    return activeInternalFrameTitleBackgroundColor.getColor();
  }

  /**
   * Gets the foreground color for active {@link javax.swing.JInternalFrame}'s.
   *
   * @return the foreground color for active {@link javax.swing.JInternalFrame}'s
   */
  public ColorUIResource getActiveInternalFrameTitleForegroundColor() {
    return activeInternalFrameTitleForegroundColor.getColor();
  }

  /**
   * Gets the gradient color for active {@link javax.swing.JInternalFrame}'s.
   *
   * @return the gradient color for active {@link javax.swing.JInternalFrame}'s
   */
  public ColorUIResource getActiveInternalFrameTitleGradientColor() {
    return activeInternalFrameTitleGradientColor.getColor();
  }

  /**
   * Gets the background color for inactive {@link javax.swing.JInternalFrame}'s.
   *
   * @return the background color for inactive {@link javax.swing.JInternalFrame}'s
   */
  public ColorUIResource getInactiveInternalFrameTitleBackgroundColor() {
    return inactiveInternalFrameTitleBackgroundColor.getColor();
  }

  /**
   * Gets the foreground color for inactive {@link javax.swing.JInternalFrame}'s.
   *
   * @return the foreground color for inactive {@link javax.swing.JInternalFrame}'s
   */
  public ColorUIResource getInactiveInternalFrameTitleForegroundColor() {
    return inactiveInternalFrameTitleForegroundColor.getColor();
  }

  /**
   * Gets the gradient color for inactive {@link javax.swing.JInternalFrame}'s.
   *
   * @return the gradient color for inactive {@link javax.swing.JInternalFrame}'s
   */
  public ColorUIResource getInactiveInternalFrameTitleGradientColor() {
    return inactiveInternalFrameTitleGradientColor.getColor();
  }

  /**
   * Gets the border around cells in {@link javax.swing.table.JTableHeader}'s.
   *
   * @return the border around cells in {@link javax.swing.table.JTableHeader}'s
   */
  public BorderUIResource getTableHeaderCellBorder() {
    return tableHeaderCellBorder.getBorder();
  }

  /**
   * Gets the icon to the left in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @return the icon to the left in the title bar of {@link javax.swing.JInternalFrame}'s
   */
  public IconUIResource getInternalFrameIcon() {
    return internalFrameIcon;
  }

  /**
   * Sets the icon to the left in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @param internalFrameIcon the icon
   */
  public void setInternalFrameIcon(IconUIResource internalFrameIcon) {
    this.internalFrameIcon = internalFrameIcon;
  }

  /**
   * Gets the icon used in the minimize button in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @return the icon used in the minimize button in the title bar of {@link javax.swing.JInternalFrame}'s
   */
  public IconUIResource getInternalFrameMinimizeIcon() {
    return internalFrameMinimizeIcon;
  }

  /**
   * Sets the icon used in the minimize button in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @param internalFrameMinimizeIcon the icon
   */
  public void setInternalFrameMinimizeIcon(IconUIResource internalFrameMinimizeIcon) {
    this.internalFrameMinimizeIcon = internalFrameMinimizeIcon;
  }

  /**
   * Gets the icon used in the maximize button in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @return the icon used in the minimize button in the title bar of {@link javax.swing.JInternalFrame}'s
   */
  public IconUIResource getInternalFrameMaximizeIcon() {
    return internalFrameMaximizeIcon;
  }

  /**
   * Sets the icon used in the maximize button in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @param internalFrameMaximizeIcon the icon
   */
  public void setInternalFrameMaximizeIcon(IconUIResource internalFrameMaximizeIcon) {
    this.internalFrameMaximizeIcon = internalFrameMaximizeIcon;
  }

  /**
   * Gets the icon used in the close button in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @return the icon used in the close button in the title bar of {@link javax.swing.JInternalFrame}'s
   */
  public IconUIResource getInternalFrameCloseIcon() {
    return internalFrameCloseIcon;
  }

  /**
   * Sets the icon used in the close button in the title bar of {@link javax.swing.JInternalFrame}'s.
   *
   * @param internalFrameCloseIcon the icon
   */
  public void setInternalFrameCloseIcon(IconUIResource internalFrameCloseIcon) {
    this.internalFrameCloseIcon = internalFrameCloseIcon;
  }

  /**
   * Gets the border used around {@link javax.swing.JInternalFrame}'s.
   *
   * @return the border used around {@link javax.swing.JInternalFrame}'s
   */
  public BorderUIResource getInternalFrameBorder() {
    return internalFrameBorder;
  }

  /**
   * Sets the border used around {@link javax.swing.JInternalFrame}'s.
   *
   * @param internalFrameBorder the border used around {@link javax.swing.JInternalFrame}'s
   */
  public void setInternalFrameBorder(BorderUIResource internalFrameBorder) {
    this.internalFrameBorder = internalFrameBorder;
  }

  /**
   * Gets the font used in the title of {@link javax.swing.JInternalFrame}'s. Defaults to the text font with bold
   * style.
   *
   * @return the font used in the title of {@link javax.swing.JInternalFrame}'s
   */
  public FontUIResource getInternalFrameTitleFont() {
    return internalFrameTitleFont.getFont();
  }

  /**
   * Sets the font used in the title of {@link javax.swing.JInternalFrame}'s. Defaults to the text font with bold
   * style.
   *
   * @param internalFrameTitleFont the font
   */
  public void setInternalFrameTitleFont(FontUIResource internalFrameTitleFont) {
    this.internalFrameTitleFont.setFont(internalFrameTitleFont);
  }

  /**
   * Sets the background color for {@link javax.swing.JComponent}'s.
   *
   * @param color the control color
   */
  public void setControlColor(Color color) {
    this.controlColor.setColor(color);
    updateColors();
  }

  /**
   * Sets the primary control background color used in scroll bar knobs etc.
   *
   * @param c the primary control background color
   */
  public void setPrimaryControlColor(Color c) {
    primaryControlColor.setColor(c);
    updateColors();
  }

  /**
   * Sets the background color used in {@link javax.swing.JViewport}, {@link javax.swing.JTree}, {@link
   * javax.swing.JTable} etc.
   *
   * @param c the background color used in {@link javax.swing.JViewport}, {@link javax.swing.JTree}, {@link
   *          javax.swing.JTable} etc.
   */
  public void setBackgroundColor(Color c) {
    backgroundColor.setColor(c);
    updateColors();
  }

  /**
   * Sets the text color.
   *
   * @param c the text color
   */
  public void setTextColor(Color c) {
    textColor.setColor(c);
    updateColors();
  }

  /**
   * Gets the font used in {@link javax.swing.JOptionPane} buttons. Defaults to the text font with bold style.
   *
   * @return the font used in {@link javax.swing.JOptionPane} buttons
   */
  public FontUIResource getOptionPaneButtonFont() {
    return optionPaneButtonFont.getFont();
  }

  /**
   * Sets the font used in {@link javax.swing.JOptionPane} buttons. Defaults to the text font with bold style.
   *
   * @param optionPaneButtonFont the font used in {@link javax.swing.JOptionPane} buttons
   */
  public void setOptionPaneButtonFont(FontUIResource optionPaneButtonFont) {
    this.optionPaneButtonFont.setFont(optionPaneButtonFont);
  }

  /**
   * Gets the size of the {@link javax.swing.JSplitPane} divider.
   *
   * @return the size of the {@link javax.swing.JSplitPane} divider
   */
  public int getSplitPaneDividerSize() {
    return splitPaneDividerSize;
  }

  /**
   * Sets the size of the {@link javax.swing.JSplitPane} divider.
   *
   * @param splitPaneDividerSize the size of the {@link javax.swing.JSplitPane} divider
   */
  public void setSplitPaneDividerSize(int splitPaneDividerSize) {
    this.splitPaneDividerSize = splitPaneDividerSize;
  }

  /**
   * Gets the border used around {@link javax.swing.JTextField} (including spinners etc.).
   *
   * @return the border used around {@link javax.swing.JTextField}
   */
  public BorderUIResource getTextFieldBorder() {
    return textFieldBorder.getBorder();
  }

  /**
   * Sets the border used around {@link javax.swing.JTextField} (including spinners etc.).
   *
   * @param textFieldBorder the border used around {@link javax.swing.JTextField}
   */
  public void setTextFieldBorder(BorderUIResource textFieldBorder) {
    this.textFieldBorder.setBorder(textFieldBorder);
  }

  /**
   * Gets the icon used with open nodes in a {@link javax.swing.JTree}.
   *
   * @return the icon used with open nodes in a {@link javax.swing.JTree}
   */
  public IconUIResource getTreeOpenIcon() {
    return treeOpenIcon;
  }

  /**
   * Sets the icon used with open nodes in a {@link javax.swing.JTree}.
   *
   * @param treeOpenIcon the icon used with open nodes in a {@link javax.swing.JTree}
   */
  public void setTreeOpenIcon(IconUIResource treeOpenIcon) {
    this.treeOpenIcon = treeOpenIcon;
  }

  /**
   * Gets the icon used with closed nodes in a {@link javax.swing.JTree}.
   *
   * @return the icon used with closed nodes in a {@link javax.swing.JTree}
   */
  public IconUIResource getTreeClosedIcon() {
    return treeClosedIcon;
  }

  /**
   * Sets the icon used with closed nodes in a {@link javax.swing.JTree}.
   *
   * @param treeClosedIcon the icon used with closed nodes in a {@link javax.swing.JTree}
   */
  public void setTreeClosedIcon(IconUIResource treeClosedIcon) {
    this.treeClosedIcon = treeClosedIcon;
  }

  /**
   * Gets the icon used with leaf nodes in a {@link javax.swing.JTree}.
   *
   * @return the icon used with leaf nodes in a {@link javax.swing.JTree}
   */
  public IconUIResource getTreeLeafIcon() {
    return treeLeafIcon;
  }

  /**
   * Sets the icon used with leaf nodes in a {@link javax.swing.JTree}.
   *
   * @param treeLeafIcon the icon used with leaf nodes in a {@link javax.swing.JTree}
   */
  public void setTreeLeafIcon(IconUIResource treeLeafIcon) {
    this.treeLeafIcon = treeLeafIcon;
  }

  /**
   * Gets the border used around {@link javax.swing.JMenuBar}'s.
   *
   * @return the border used around {@link javax.swing.JMenuBar}'s
   */
  public BorderUIResource getMenuBarBorder() {
    return menuBarBorder.getBorder();
  }

  /**
   * Sets the border used around {@link javax.swing.JMenuBar}'s.
   *
   * @param menuBarBorder the border used around {@link javax.swing.JMenuBar}'s
   */
  public void setMenuBarBorder(BorderUIResource menuBarBorder) {
    this.menuBarBorder.setBorder(menuBarBorder);
  }

  /**
   * Sets the selected text background color.
   *
   * @param selectedTextBackgroundColor the selected text background color
   */
  public void setSelectedTextBackgroundColor(Color selectedTextBackgroundColor) {
    this.selectedTextBackgroundColor.setColor(selectedTextBackgroundColor);
    updateColors();
  }

  /**
   * Sets the focus marker color.
   *
   * @param focusColor the focus marker color
   */
  public void setFocusColor(Color focusColor) {
    this.focusColor.setColor(focusColor);
  }

  /**
   * Sets the selected text color.
   *
   * @param selectedTextColor the selected text color
   */
  public void setSelectedTextColor(Color selectedTextColor) {
    this.selectedTextColor.setColor(selectedTextColor);
  }

  /**
   * Sets the tooltip background color.
   *
   * @param tooltipBackgroundColor the tooltip background color
   */
  public void setTooltipBackgroundColor(Color tooltipBackgroundColor) {
    this.tooltipBackgroundColor.setColor(tooltipBackgroundColor);
  }

  /**
   * Sets the background color for a {@link javax.swing.JDesktopPane}.
   *
   * @param desktopColor the background color for a {@link javax.swing.JDesktopPane}
   */
  public void setDesktopColor(Color desktopColor) {
    this.desktopColor.setColor(desktopColor);
    updateColors();
  }

  /**
   * Sets the background color for the expand/collapse icons in a {@link javax.swing.JTree}.
   *
   * @param treeIconBackgroundColor the background color for the expand/collapse icons in a {@link javax.swing.JTree}
   */
  public void setTreeIconBackgroundColor(Color treeIconBackgroundColor) {
    this.treeIconBackgroundColor.setColor(treeIconBackgroundColor);
  }

  /**
   * Sets the background color for a selected menu item.
   *
   * @param selectedMenuBackgroundColor the background color for a selected menu item
   */
  public void setSelectedMenuBackgroundColor(Color selectedMenuBackgroundColor) {
    this.selectedMenuBackgroundColor.setColor(selectedMenuBackgroundColor);
    updateColors();
  }

  /**
   * Sets the foreground color for a selected menu item.
   *
   * @param selectedMenuForegroundColor the foreground color for a selected menu item
   */
  public void setSelectedMenuForegroundColor(Color selectedMenuForegroundColor) {
    this.selectedMenuForegroundColor.setColor(selectedMenuForegroundColor);
  }

  /**
   * Sets the inactive text color.
   *
   * @param inactiveTextColor the inactive text color
   */
  public void setInactiveTextColor(Color inactiveTextColor) {
    this.inactiveTextColor.setColor(inactiveTextColor);
  }

  /**
   * Sets the {@link javax.swing.JScrollBar} background color.
   *
   * @param scrollBarBackgroundColor the {@link javax.swing.JScrollBar} background color
   */
  public void setScrollBarBackgroundColor(Color scrollBarBackgroundColor) {
    this.scrollBarBackgroundColor.setColor(scrollBarBackgroundColor);
    updateColors();
  }

  /**
   * Sets the background color for the title of an active {@link javax.swing.JInternalFrame}.
   *
   * @param activeInternalFrameTitleBackgroundColor
   *         the background color for the title of an active {@link javax.swing.JInternalFrame}
   */
  public void setActiveInternalFrameTitleBackgroundColor(Color activeInternalFrameTitleBackgroundColor) {
    this.activeInternalFrameTitleBackgroundColor.setColor(activeInternalFrameTitleBackgroundColor);
    updateColors();
  }

  /**
   * Sets the foreground color for the title of an active {@link javax.swing.JInternalFrame}.
   *
   * @param activeInternalFrameTitleForegroundColor
   *         the background color for the title of an active {@link javax.swing.JInternalFrame}
   */
  public void setActiveInternalFrameTitleForegroundColor(Color activeInternalFrameTitleForegroundColor) {
    this.activeInternalFrameTitleForegroundColor.setColor(activeInternalFrameTitleForegroundColor);
    updateColors();
  }

  /**
   * Sets the gradient color for the title of an active {@link javax.swing.JInternalFrame}.
   *
   * @param activeInternalFrameTitleGradientColor
   *         the gradient color for the title of an active {@link javax.swing.JInternalFrame}
   */
  public void setActiveInternalFrameTitleGradientColor(Color activeInternalFrameTitleGradientColor) {
    this.activeInternalFrameTitleGradientColor.setColor(activeInternalFrameTitleGradientColor);
    updateColors();
  }

  /**
   * Sets the background color for the title of an inactive {@link javax.swing.JInternalFrame}.
   *
   * @param inactiveInternalFrameTitleBackgroundColor
   *         the background color for the title of an inactive {@link javax.swing.JInternalFrame}
   */
  public void setInactiveInternalFrameTitleBackgroundColor(Color inactiveInternalFrameTitleBackgroundColor) {
    this.inactiveInternalFrameTitleBackgroundColor.setColor(inactiveInternalFrameTitleBackgroundColor);
    updateColors();
  }

  /**
   * Sets the foreground color for the title of an inactive {@link javax.swing.JInternalFrame}.
   *
   * @param inactiveInternalFrameTitleForegroundColor
   *         the background color for the title of an active {@link javax.swing.JInternalFrame}
   */
  public void setInactiveInternalFrameTitleForegroundColor(Color inactiveInternalFrameTitleForegroundColor) {
    this.inactiveInternalFrameTitleForegroundColor.setColor(inactiveInternalFrameTitleForegroundColor);
    updateColors();
  }

  /**
   * Sets the gradient color for the title of an inactive {@link javax.swing.JInternalFrame}.
   *
   * @param inactiveInternalFrameTitleGradientColor
   *         the gradient color for the title of an inactive {@link javax.swing.JInternalFrame}
   */
  public void setInactiveInternalFrameTitleGradientColor(Color inactiveInternalFrameTitleGradientColor) {
    this.inactiveInternalFrameTitleGradientColor.setColor(inactiveInternalFrameTitleGradientColor);
    updateColors();
  }

  /**
   * Sets the title font of an {@link javax.swing.JInternalFrame}.
   *
   * @param frameTitleFont the title font of an {@link javax.swing.JInternalFrame}
   */
  public void setInternalFrameTitleFont(Font frameTitleFont) {
    this.internalFrameTitleFont.setFont(frameTitleFont);
  }

  /**
   * Sets the button font for a {@link javax.swing.JOptionPane}. Default to the text font with bold style.
   *
   * @param optionPaneButtonFont the button font for a {@link javax.swing.JOptionPane}
   */
  public void setOptionPaneButtonFont(Font optionPaneButtonFont) {
    this.optionPaneButtonFont.setFont(optionPaneButtonFont);
  }

  /**
   * Sets the border for the cells of a {@link javax.swing.table.JTableHeader}.
   *
   * @param tableHeaderCellBorder the border for the cells of a {@link javax.swing.table.JTableHeader}
   */
  public void setTableHeaderCellBorder(BorderUIResource tableHeaderCellBorder) {
    this.tableHeaderCellBorder.setBorder(tableHeaderCellBorder);
  }

  /**
   * Gets the width of a {@link javax.swing.JScrollBar}.
   *
   * @return the width of a {@link javax.swing.JScrollBar}
   */
  public int getScrollBarWidth() {
    return scrollBarWidth;
  }

  /**
   * Sets the width of a {@link javax.swing.JScrollBar}.
   *
   * @param scrollBarWidth the width of a {@link javax.swing.JScrollBar}
   */
  public void setScrollBarWidth(int scrollBarWidth) {
    this.scrollBarWidth = scrollBarWidth;
  }

  /**
   * Gets the margin of a {@link javax.swing.JButton}.
   *
   * @return the margin of a {@link javax.swing.JButton}
   */
  public InsetsUIResource getButtonMargin() {
    return buttonMargin;
  }

  /**
   * Sets the margin of a {@link javax.swing.JButton}.
   *
   * @param buttonMargin the margin of a {@link javax.swing.JButton}
   */
  public void setButtonMargin(InsetsUIResource buttonMargin) {
    this.buttonMargin = buttonMargin;
  }

  /**
   * Gets the border of a {@link javax.swing.JPopupMenu}.
   *
   * @return the border of a {@link javax.swing.JPopupMenu}
   */
  public BorderUIResource getPopupMenuBorder() {
    return popupMenuBorder.getBorder();
  }

  /**
   * Sets the border of a {@link javax.swing.JPopupMenu}.
   *
   * @param popupMenuBorder the border of a {@link javax.swing.JPopupMenu}
   */
  public void setPopupMenuBorder(BorderUIResource popupMenuBorder) {
    this.popupMenuBorder.setBorder(popupMenuBorder);
  }

  /**
   * Gets the icon used in the iconify button in the title of a {@link javax.swing.JInternalFrame}.
   *
   * @return the icon used in the iconify button in the title of a {@link javax.swing.JInternalFrame}
   */
  public IconUIResource getInternalFrameIconifyIcon() {
    return internalFrameIconifyIcon;
  }

  /**
   * Sets the icon used in the iconify button in the title of a {@link javax.swing.JInternalFrame}.
   *
   * @param internalFrameIconifyIcon the icon used in the iconify button in the title of a {@link
   *                                 javax.swing.JInternalFrame}
   */
  public void setInternalFrameIconifyIcon(IconUIResource internalFrameIconifyIcon) {
    this.internalFrameIconifyIcon = internalFrameIconifyIcon;
  }

  /**
   * Gets the background color used in {@link javax.swing.JToolTip}.
   *
   * @return the background color used in {@link javax.swing.JToolTip}
   */
  public ColorUIResource getTooltipForegroundColor() {
    return tooltipForegroundColor.getColor();
  }

  /**
   * Sets the foreground color used in {@link javax.swing.JToolTip}.
   *
   * @param tooltipForegroundColor the foreground color used in {@link javax.swing.JToolTip}
   */
  public void setTooltipForegroundColor(ColorUIResource tooltipForegroundColor) {
    this.tooltipForegroundColor.setColor(tooltipForegroundColor);
    updateColors();
  }

  /**
   * Gets the border used around list items in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s.
   *
   * @return the border used around list items in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s
   */
  public BorderUIResource getListItemBorder() {
    return listItemBorder.getBorder();
  }

  /**
   * Sets the border used around list items in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s.
   *
   * @param listItemBorder the border used around list items in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s
   */
  public void setListItemBorder(BorderUIResource listItemBorder) {
    this.listItemBorder.setBorder(listItemBorder);
  }

  /**
   * Gets the border used around the focused list item in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s.
   *
   * @return the border used around the focused list item in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s
   */
  public BorderUIResource getListFocusedItemBorder() {
    return listFocusedItemBorder.getBorder();
  }

  /**
   * Sets the border used around the focused list item in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s.
   *
   * @param listFocusedItemBorder the border used around the focused list item in {@link javax.swing.JList}'s and {@link javax.swing.JComboBox}'s
   */
  public void setListFocusedItemBorder(BorderUIResource listFocusedItemBorder) {
    this.listFocusedItemBorder.setBorder(listFocusedItemBorder);
  }

}
