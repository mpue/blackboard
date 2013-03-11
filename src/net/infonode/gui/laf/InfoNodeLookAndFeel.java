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


// $Id: InfoNodeLookAndFeel.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.laf;

import java.awt.Component;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import net.infonode.gui.icon.button.TreeIcon;
import net.infonode.gui.laf.ui.SlimComboBoxUI;
import net.infonode.gui.laf.ui.SlimInternalFrameUI;
import net.infonode.gui.laf.ui.SlimMenuItemUI;
import net.infonode.gui.laf.ui.SlimSplitPaneUI;
import net.infonode.util.ArrayUtil;
import net.infonode.util.ColorUtil;

/**
 * A Look and Feel that's based on Metal. It's slimmer and use other colors than the standard Metal Look and Feel.
 * Under Java 1.5 the currect Metal theme is stored when the InfoNode Look and Feel is applied, and restored when
 * another Look and Feel is set. Under Java 1.4 or earlier it is not possible to get the current theme and a
 * DefaultMetalTheme is set instead.
 * <p>
 * To set the look and feel use:
 * <pre>
 * UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
 * </pre>
 * Or, if you want to use a different theme, use:
 * <pre>
 * InfoNodeLookAndFeelTheme theme = new InfoNodeLookAndFeelTheme(...);
 * // Modify the theme colors, fonts etc.
 * UIManager.setLookAndFeel(new InfoNodeLookAndFeel(theme));
 * </pre>
 * Do not modify the theme after it has been used in the look and feel!
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class InfoNodeLookAndFeel extends MetalLookAndFeel {
  public static final UIManager.LookAndFeelInfo LOOK_AND_FEEL_INFO =
      new UIManager.LookAndFeelInfo("InfoNode", InfoNodeLookAndFeel.class.getName());

  private static MetalTheme oldMetalTheme;

  private transient InfoNodeLookAndFeelTheme theme;

  private transient DefaultMetalTheme defaultTheme = new DefaultMetalTheme() {
    public ColorUIResource getPrimaryControlHighlight() {
      return theme.getPrimaryControlHighlightColor();
    }

    public ColorUIResource getMenuBackground() {
      return theme.getControlColor();
    }

    public ColorUIResource getControlHighlight() {
      return theme.getControlHighlightColor();
    }

    public ColorUIResource getControl() {
      return theme.getControlColor();
    }

    public ColorUIResource getControlShadow() {
      return theme.getControlShadowColor();
    }

    public ColorUIResource getControlDarkShadow() {
      return theme.getControlDarkShadowColor();
    }

    // Scrollbars, popups etc.
    public ColorUIResource getPrimaryControl() {
      return theme.getPrimaryControlColor();
    }

    public ColorUIResource getPrimaryControlShadow() {
      return theme.getPrimaryControlShadowColor();
    }

    public ColorUIResource getPrimaryControlDarkShadow() {
      return theme.getPrimaryControlDarkShadowColor();
    }
    // End scrollbars

    public ColorUIResource getTextHighlightColor() {
      return theme.getSelectedTextBackgroundColor();
    }

    public ColorUIResource getMenuSelectedBackground() {
      return theme.getSelectedMenuBackgroundColor();
    }

    public ColorUIResource getWindowBackground() {
      return theme.getBackgroundColor();
    }

    protected ColorUIResource getWhite() {
      return theme.getBackgroundColor();
    }

    public ColorUIResource getDesktopColor() {
      return theme.getDesktopColor();
    }

    public ColorUIResource getHighlightedTextColor() {
      return theme.getSelectedTextColor();
    }

    protected ColorUIResource getBlack() {
      return theme.getTextColor();
    }

    public ColorUIResource getMenuForeground() {
      return theme.getTextColor();
    }

    public ColorUIResource getMenuSelectedForeground() {
      return theme.getSelectedMenuForegroundColor();
    }

    public ColorUIResource getFocusColor() {
      return theme.getFocusColor();
    }

    public ColorUIResource getControlDisabled() {
      return theme.getControlColor();
    }

    public ColorUIResource getSystemTextColor() {
      return theme.getTextColor();
    }

    public ColorUIResource getControlTextColor() {
      return theme.getTextColor();
    }

    public ColorUIResource getInactiveControlTextColor() {
      return theme.getInactiveTextColor();
    }

    public ColorUIResource getInactiveSystemTextColor() {
      return theme.getInactiveTextColor();
    }

    public ColorUIResource getUserTextColor() {
      return theme.getTextColor();
    }


    // --------------- Fonts --------------------------

    public FontUIResource getControlTextFont() {
      return getSystemTextFont();
    }

    public FontUIResource getSystemTextFont() {
      return theme.getFont();
    }

    public FontUIResource getUserTextFont() {
      return getSystemTextFont();
    }

    public FontUIResource getMenuTextFont() {
      return getSystemTextFont();
    }

    public FontUIResource getWindowTitleFont() {
      return getSystemTextFont();
    }

    public FontUIResource getSubTextFont() {
      return getSystemTextFont();
    }

  };

  /**
   * Constructor.
   */
  public InfoNodeLookAndFeel() {
    this(new InfoNodeLookAndFeelTheme());
  }

  /**
   * Constructor.
   *
   * @param theme the theme to use. Do not modify the theme after this constructor has been called!
   */
  public InfoNodeLookAndFeel(InfoNodeLookAndFeelTheme theme) {
    this.theme = theme;
  }

  /**
   * Gets the active theme
   *
   * @return the active theme
   */
  public InfoNodeLookAndFeelTheme getTheme() {
    return theme;
  }

  public void initialize() {
    super.initialize();

    if (oldMetalTheme == null) {
      // Try to obtain the old Metal theme if possible
      try {
        oldMetalTheme = (MetalTheme) MetalLookAndFeel.class.getMethod("getCurrentTheme", null).invoke(null, null);
      }
      catch (NoSuchMethodException e) {
        // Ignore
      }
      catch (IllegalAccessException e) {
        // Ignore
      }
      catch (InvocationTargetException e) {
        // Ignore
      }
    }

    setCurrentTheme(defaultTheme);
  }

  public void uninitialize() {
    setCurrentTheme(oldMetalTheme == null ? new DefaultMetalTheme() : oldMetalTheme);
    oldMetalTheme = null;
  }

  public String getName() {
    return LOOK_AND_FEEL_INFO.getName();
  }

  public String getDescription() {
    return "A slim look and feel based on Metal.";
  }

  protected void initClassDefaults(UIDefaults table) {
    super.initClassDefaults(table);

    try {
      {
        Class cl = SlimSplitPaneUI.class;
        table.put("SplitPaneUI", cl.getName());
        table.put(cl.getName(), cl);
      }

      {
        Class cl = SlimInternalFrameUI.class;
        table.put("InternalFrameUI", cl.getName());
        table.put(cl.getName(), cl);
      }

      {
        Class cl = SlimComboBoxUI.class;
        SlimComboBoxUI.NORMAL_BORDER = theme.getListItemBorder();
        SlimComboBoxUI.FOCUS_BORDER = theme.getListFocusedItemBorder();
        table.put("ComboBoxUI", cl.getName());
        table.put(cl.getName(), cl);
      }

      {
        Class cl = SlimMenuItemUI.class;
        table.put("MenuItemUI", cl.getName());
        table.put(cl.getName(), cl);
      }
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static class MyListCellRenderer extends DefaultListCellRenderer {
    private Border normalBorder;
    private Border focusBorder;

    MyListCellRenderer(Border normalBorder, Border focusBorder) {
      this.normalBorder = normalBorder;
      this.focusBorder = focusBorder;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
      JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      label.setBorder(cellHasFocus ? focusBorder : normalBorder);
      return label;
    }

    public static class UIResource extends MyListCellRenderer implements javax.swing.plaf.UIResource {
      public UIResource(Border normalBorder, Border focusBorder) {
        super(normalBorder, focusBorder);
      }
    }
  }

  protected void initComponentDefaults(UIDefaults table) {
    super.initComponentDefaults(table);

    Class iconClass = MetalLookAndFeel.class;
    UIResource menuItemBorder = new MetalBorders.MenuItemBorder() {
      public Insets getBorderInsets(Component c) {
        return new Insets(2, 0, 2, 0);
      }
    };

    Object[] defaults = {
      "SplitPane.dividerSize", new Integer(theme.getSplitPaneDividerSize()),
//      "SplitPaneDivider.border", new BorderUIResource(splitPaneDividerBorder),
//      "SplitPane.border", new BorderUIResource(splitPaneBorder),

//    "TabbedPane.contentBorderInsets", tabbedPaneContentInsets,
//    "TabbedPane.tabAreaInsets", new InsetsUIResource(0, 0, 0, 0),
//    "TabbedPane.selectedTabPadInsets", new InsetsUIResource(0, 0, 0, 0),
//    "TabbedPane.tabInsets", new InsetsUIResource(0, 0, 0, 0),
      "TabbedPane.background", theme.getControlLightShadowColor(),
      //"TabbedPane.darkShadow", new Color(160, 160, 160),

      "ComboBox.selectionBackground", theme.getSelectedMenuBackgroundColor(),
      "ComboBox.selectionForeground", theme.getSelectedMenuForegroundColor(),

      "List.cellRenderer", new UIDefaults.ActiveValue() {
        public Object createValue(UIDefaults table) {
          return new MyListCellRenderer.UIResource(theme.getListItemBorder(), theme.getListFocusedItemBorder());
        }
      },

      "ToolTip.foreground", theme.getTooltipForegroundColor(),
      "ToolTip.background", theme.getTooltipBackgroundColor(),

      "Viewport.background", theme.getBackgroundColor(),

      "ScrollBar.background", theme.getScrollBarBackgroundColor(),
      "ScrollBar.shadow", theme.getScrollBarBackgroundShadowColor(),
      "ScrollBar.width", new Integer(theme.getScrollBarWidth()),
//    "ScrollBar.border", new BorderUIResource(new LineBorder(Color.GRAY, 1)),

//    "ScrollBar.thumb", new RelativeColor(thumbColor, 0.8).getColor(),
//    "ScrollBar.thumbShadow", new RelativeColor(thumbColor, 0.7).getColor(),
//    "ScrollBar.thumbHighlight", new RelativeColor(thumbColor, 1).getColor(),

/*    "ScrollBar.thumb", new ColorUIResource(255, 255, 255),
    "ScrollBar.thumbShadow", new ColorUIResource(0, 255, 0),
    "ScrollBar.thumbHighlight", new ColorUIResource(255, 255, 255),
*/

      "Table.focusCellBackground", new ColorUIResource(ColorUtil.mult(theme.getSelectedMenuBackgroundColor(), 1.40f)),
      "Table.focusCellForeground", theme.getSelectedMenuForegroundColor(),

      "TableHeader.cellBorder", theme.getTableHeaderCellBorder(),

      "InternalFrame.activeTitleBackground", theme.getActiveInternalFrameTitleBackgroundColor(),
      "InternalFrame.activeTitleForeground", theme.getActiveInternalFrameTitleForegroundColor(),
      "InternalFrame.activeTitleGradient", theme.getActiveInternalFrameTitleGradientColor(), //ColorUtil.mult(theme.getActiveInternalFrameTitleBackgroundColor(), 1.2),
      "InternalFrame.inactiveTitleBackground", theme.getInactiveInternalFrameTitleBackgroundColor(),
      "InternalFrame.inactiveTitleForeground", theme.getInactiveInternalFrameTitleForegroundColor(),
      "InternalFrame.inactiveTitleGradient", theme.getInactiveInternalFrameTitleGradientColor(), //ColorUtil.mult(theme.getInactiveInternalFrameTitleBackgroundColor(), 1.2),
      "InternalFrame.icon", theme.getInternalFrameIcon(),
      "InternalFrame.iconifyIcon", theme.getInternalFrameIconifyIcon(),
      "InternalFrame.minimizeIcon", theme.getInternalFrameMinimizeIcon(),
      "InternalFrame.maximizeIcon", theme.getInternalFrameMaximizeIcon(),
      "InternalFrame.closeIcon", theme.getInternalFrameCloseIcon(),
      "InternalFrame.border", theme.getInternalFrameBorder(),
      "InternalFrame.titleFont", theme.getInternalFrameTitleFont(),

      "MenuBar.border", theme.getMenuBarBorder(),

      "MenuItem.border", menuItemBorder,
      "Menu.border", menuItemBorder,
//      "CheckBoxMenuItem.border", menuItemBorder,
//      "RadioButtonMenuItem.border", menuItemBorder,

      "Spinner.border", theme.getTextFieldBorder(),
      "Spinner.background", new ColorUIResource(theme.getBackgroundColor()),

      "PopupMenu.border", theme.getPopupMenuBorder(),

      "TextField.border", theme.getTextFieldBorder(),
      "FormattedTextField.border", theme.getTextFieldBorder(),

//      "Button.border", new BorderUIResource(buttonBorder),
//      "Button.disabledShadow", new ColorUIResource(Color.GREEN), //ColorUtil.blend(textColor, controlColor, 0.5f)),
      "Button.textShiftOffset", new Integer(2),
      "Button.select", theme.getControlLightShadowColor(),
//    "Button.focus", focusColor,
      "Button.margin", theme.getButtonMargin(),
      "Button.disabledText", theme.getInactiveTextColor(),
      //"Button.background", buttonBackground.getColor(),

      "ToggleButton.margin", theme.getButtonMargin(),
      "ToggleButton.select", theme.getControlLightShadowColor(),
      "ToggleButton.textShiftOffset", new Integer(2),

      "Tree.openIcon", theme.getTreeOpenIcon(),
      "Tree.closedIcon", theme.getTreeClosedIcon(),
      "Tree.leafIcon", theme.getTreeLeafIcon(),
      "Tree.collapsedIcon", new IconUIResource(
          new TreeIcon(TreeIcon.PLUS, 10, 10, true, theme.getTextColor(), theme.getTreeIconBackgroundColor())),
      "Tree.expandedIcon", new IconUIResource(
          new TreeIcon(TreeIcon.MINUS, 10, 10, true, theme.getTextColor(), theme.getTreeIconBackgroundColor())),
      "Tree.leftChildIndent", new Integer(5),
      "Tree.rightChildIndent", new Integer(11),
//    "Tree.rowHeight", new Integer(12),

      "OptionPane.errorIcon", LookAndFeel.makeIcon(iconClass, "icons/Error.gif"),
      "OptionPane.informationIcon", LookAndFeel.makeIcon(iconClass, "icons/Inform.gif"),
      "OptionPane.warningIcon", LookAndFeel.makeIcon(iconClass, "icons/Warn.gif"),
      "OptionPane.questionIcon", LookAndFeel.makeIcon(iconClass, "icons/Question.gif"),
      "OptionPane.buttonFont", theme.getOptionPaneButtonFont(),
    };

    table.putDefaults(defaults);
  }

  /**
   * Installs this look and feel with the {@link UIManager}, if it's not already installed.
   */
  public static void install() {
    if (!ArrayUtil.contains(UIManager.getInstalledLookAndFeels(), LOOK_AND_FEEL_INFO))
      UIManager.installLookAndFeel(LOOK_AND_FEEL_INFO);
  }

}
