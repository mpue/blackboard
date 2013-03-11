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


// $Id: LookAndFeelDockingTheme.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.properties.ViewTitleBarProperties;
import net.infonode.docking.properties.WindowBarProperties;
import net.infonode.docking.theme.internal.laftheme.TitleBarUI;
import net.infonode.docking.theme.internal.laftheme.TitleBarUIListener;
import net.infonode.gui.InsetsUtil;
import net.infonode.gui.colorprovider.BackgroundColorProvider;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.gui.componentpainter.CompoundComponentPainter;
import net.infonode.gui.componentpainter.SolidColorComponentPainter;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMapManager;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.tabbedpanel.theme.LookAndFeelTheme;
import net.infonode.tabbedpanel.titledtab.TitledTabSizePolicy;
import net.infonode.util.Direction;

/**
 * <p>
 * An <strong>experimental</strong> theme that tries to replicate the look of
 * the active look and feel. This may or may not work depending on the look and
 * feel used.
 * </p>
 *
 * <p>
 * This is a theme that tries to replicate the look using the active look and
 * feel. The tab windows will resemble the JTabbedPane look and the view title
 * bars will resemble the JInternalFrame's title bar. Note that vertical title
 * bars might not look very nice. The theme uses heavyweight AWT components
 * internally so the {@link #dispose()} method must be called when the theme is
 * no longer needed, otherwise the native resources will not be disposed.
 * </p>
 *
 * <p>
 * The theme uses the hover mechanism so that tab hover effects can be
 * replicated. Only title bars above or below the view's content are supported.
 * </p>
 *
 * <p>
 * <strong>This theme is considered to be experimental and is not guaranteed to
 * be an exact replica of the active look and feel. It is also not guaranteed to
 * work together with the active look and feel. The theme may be changed,
 * removed etc in future versions. No support is given for the theme. This theme
 * doesn't work well with Aqua Look and Feel on Macintosh.</strong>
 * </p>
 *
 * @author johan
 * @version $Revision: 1.3 $
 * @see net.infonode.tabbedpanel.theme.LookAndFeelTheme
 * @since IDW 1.4.0
 */
public class LookAndFeelDockingTheme extends DockingWindowsTheme {

  private static LookAndFeelTheme tpTheme;

  private static RootWindowProperties rootProps = new RootWindowProperties();

  private RootWindowProperties themeRootProps = new RootWindowProperties();

  private static TitleBarUI titleBarUI;

  private static int themeCounter = 0;

  private boolean disposed = false;

  public LookAndFeelDockingTheme() {
    if (themeCounter == 0) {
      tpTheme = new LookAndFeelTheme();

      titleBarUI = new TitleBarUI(new TitleBarUIListener() {
        public void updating() {
        }

        public void updated() {
          initTheme(false);
        }
      }, false);

      titleBarUI.init();

      initTheme(true);
    }

    themeCounter++;

    themeRootProps.addSuperObject(rootProps);
  }

  /**
   * Gets the theme name
   *
   * @return name
   */
  public String getName() {
    return "Look and Feel Theme";
  }

  /**
   * Gets the theme RootWindowProperties
   *
   * @return the RootWindowProperties
   */
  public RootWindowProperties getRootWindowProperties() {
    return themeRootProps;
  }

  /**
   * <p>
   * Disposes this theme.
   * </p>
   *
   * <p>
   * This method must be called in order to dispose the heavyweight AWT
   * components used internally.
   * </p>
   */
  public void dispose() {
    if (!disposed) {
      disposed = true;

      themeCounter--;

      if (themeCounter == 0) {
        titleBarUI.dispose();

        PropertyMapManager.runBatch(new Runnable() {
          public void run() {
            rootProps.getTabWindowProperties().getTabbedPanelProperties().removeSuperObject(
                tpTheme.getTabbedPanelProperties());
            rootProps.getTabWindowProperties().getTabProperties().getTitledTabProperties().removeSuperObject(
                tpTheme.getTitledTabProperties());
            rootProps.getMap().clear(true);
          }
        });

        tpTheme.dispose();
      }
    }
  }

  private void initTheme(final boolean initial) {
    PropertyMapManager.runBatch(new Runnable() {
      public void run() {
        if (initial) {
          rootProps.getTabWindowProperties().getTabbedPanelProperties().addSuperObject(
              tpTheme.getTabbedPanelProperties());
          rootProps.getTabWindowProperties().getTabProperties().getTitledTabProperties().addSuperObject(
              tpTheme.getTitledTabProperties());
        }

        rootProps.getMap().clear(true);

        {
          // Window bar
          WindowBarProperties barProps = rootProps.getWindowBarProperties();
          // barProps.setContinuousLayoutEnabled(false);
          barProps.getTabWindowProperties().getTabProperties().getTitledTabProperties().setSizePolicy(
              TitledTabSizePolicy.EQUAL_SIZE);
          barProps.getComponentProperties().setBorder(null);

          final Border contentBorder = tpTheme.getTabbedPanelProperties().getContentPanelProperties()
              .getComponentProperties()
              .getBorder();

          ComponentPainter barContentPainter = tpTheme.getTabbedPanelProperties().getContentPanelProperties()
              .getShapedPanelProperties()
              .getComponentPainter();
          barProps.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties()
              .getShapedPanelProperties()
              .setOpaque(true).setComponentPainter(
              new CompoundComponentPainter(new SolidColorComponentPainter(BackgroundColorProvider.INSTANCE),
                                           barContentPainter));

          barProps.getTabWindowProperties().getTabbedPanelProperties().getTabAreaComponentsProperties()
              .getComponentProperties()
              .setBorder(null);
          barProps.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties()
              .getComponentProperties()
              .setBorder(new Border() {
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                }

                public Insets getBorderInsets(Component c) {
                  Insets insets = (Insets) contentBorder.getBorderInsets(c).clone();

                  Direction areaOrientation = TabbedUtils.getParentTabbedPanelContentPanel(c).getTabbedPanel()
                      .getProperties()
                      .getTabAreaOrientation();
                  int minResizeEdgeInset = InsetsUtil.maxInset(insets);

                  if (areaOrientation == Direction.UP)
                    insets.bottom = Math.max(minResizeEdgeInset, insets.bottom);
                  else if (areaOrientation == Direction.DOWN)
                    insets.top = Math.max(minResizeEdgeInset, insets.top);
                  else if (areaOrientation == Direction.LEFT)
                    insets.right = Math.max(minResizeEdgeInset, insets.right);
                  else
                    insets.left = Math.max(minResizeEdgeInset, insets.left);

                  return insets;
                }

                public boolean isBorderOpaque() {
                  return false;
                }
              }).setInsets(new Insets(0, 0, 0, 0)).setBackgroundColor(null);
        }

        {
          // Window area
          final Color top = tpTheme.getBorderColor(Direction.UP);
          final Color left = tpTheme.getBorderColor(Direction.LEFT);
          final Color bottom = tpTheme.getBorderColor(Direction.DOWN);
          final Color right = tpTheme.getBorderColor(Direction.RIGHT);

          final Insets borderInsets = new Insets(top == null ? 0 : 1,
                                                 left == null ? 0 : 1,
                                                 bottom == null ? 0 : 1,
                                                 right == null ? 0 : 1);
          final boolean borderOpaque =
              (top != null && top.getAlpha() == 255) || (left != null && left.getAlpha() == 255)
              || (bottom != null && bottom.getAlpha() == 255) || (right != null && right.getAlpha() == 255);

          final Border windowAreaBorder = new Border() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
              if (top != null) {
                g.setColor(top);
                g.drawLine(x, y, x + width - (right == null ? 1 : 2), y);
              }

              if (right != null) {
                g.setColor(right);
                g.drawLine(x + width - 1, y, x + width - 1, y + height - (bottom == null ? 1 : 2));
              }

              if (bottom != null) {
                g.setColor(bottom);
                g.drawLine(left == null ? x : x + 1, y + height - 1, x + width - 1, y + height - 1);
              }

              if (left != null) {
                g.setColor(left);
                g.drawLine(x, top == null ? y : y + 1, x, y + height - 1);
              }
            }

            public Insets getBorderInsets(Component c) {
              return borderInsets;
            }

            public boolean isBorderOpaque() {
              return borderOpaque;
            }
          };

          rootProps.getWindowAreaProperties().setInsets(new Insets(2, 2, 2, 2)).setBorder(windowAreaBorder)
              .setBackgroundColor(null);
          rootProps.getWindowAreaShapedPanelProperties().setComponentPainter(
              new SolidColorComponentPainter(BackgroundColorProvider.INSTANCE)).setOpaque(true);
        }

        {
          // View title bar
          ViewTitleBarProperties titleBarProps = rootProps.getViewProperties().getViewTitleBarProperties();
          titleBarProps.getNormalProperties().getShapedPanelProperties().setDirection(
              titleBarUI.getRenderingDirection());

          // Normal painter
          ComponentPainter normalPainter = titleBarUI.getInactiveComponentPainter();
          if (normalPainter == null)
            titleBarProps.getNormalProperties().getShapedPanelProperties().getMap().removeValue(
                ShapedPanelProperties.COMPONENT_PAINTER);
          else
            titleBarProps.getNormalProperties().getShapedPanelProperties().setComponentPainter(normalPainter);

          // Focused painter
          ComponentPainter focusedPainter = titleBarUI.getActiveComponentPainter();
          if (focusedPainter == null)
            titleBarProps.getFocusedProperties().getShapedPanelProperties().getMap().removeValue(
                ShapedPanelProperties.COMPONENT_PAINTER);
          else
            titleBarProps.getFocusedProperties().getShapedPanelProperties().setComponentPainter(focusedPainter);

          titleBarProps.setMinimumSizeProvider(titleBarUI.getSizeDimensionProvider()).getNormalProperties()
              .setTitleVisible(!titleBarUI.isRenderingTitle()).setIconVisible(!titleBarUI.isRenderingIcon());

          titleBarProps.getFocusedProperties().getComponentProperties().setInsets(titleBarUI.getInsets());
          titleBarProps.getNormalProperties().getComponentProperties().setInsets(titleBarUI.getInsets());

          // Background colors
          updateBackgroundColor(titleBarProps.getNormalProperties().getComponentProperties(),
                                titleBarUI.getInactiveBackgroundColor());
          updateBackgroundColor(titleBarProps.getFocusedProperties().getComponentProperties(),
                                titleBarUI.getActiveBackgroundColor());
        }

        {
          // General
          rootProps.setDragRectangleBorderWidth(3);
        }
      }
    });
  }

  private void updateBackgroundColor(ComponentProperties props, Color color) {
    if (color == null)
      props.getMap().removeValue(ComponentProperties.BACKGROUND_COLOR);
    else
      props.setBackgroundColor(color);
  }
}
