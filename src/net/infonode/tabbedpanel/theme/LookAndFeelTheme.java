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


// $Id: LookAndFeelTheme.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.tabbedpanel.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

import net.infonode.gui.DimensionProvider;
import net.infonode.gui.InsetsUtil;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.properties.propertymap.PropertyMapManager;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelContentPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.tabbedpanel.theme.internal.laftheme.PaneUI;
import net.infonode.tabbedpanel.theme.internal.laftheme.PaneUIListener;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabBorderSizePolicy;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
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
 * This is a theme that tries to replicate the JTabbedPane's look using the
 * active look and feel. The theme uses a heavyweight AWT component internally
 * so the {@link #dispose()} method must be called when the theme is no longer
 * needed, otherwise the native resources will not be disposed.
 * </p>
 *
 * <p>
 * The theme uses the hover mechanism so that tab hover effects can be
 * replicated.
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
 * @since ITP 1.4.0
 */
public class LookAndFeelTheme extends TabbedPanelTitledTabTheme {
  private static TabbedPanelProperties tpProps = new TabbedPanelProperties();

  private static TitledTabProperties tabProps = new TitledTabProperties();

  private TabbedPanelProperties themeTpProps = new TabbedPanelProperties();

  private TitledTabProperties themeTabProps = new TitledTabProperties();

  private static int themeCounter = 0;

  private static PaneUI ui;

  private boolean disposed = false;

  /**
   * Constructs a Look and Feel Theme
   */
  public LookAndFeelTheme() {
    if (themeCounter == 0) {
      ui = new PaneUI(new PaneUIListener() {
        public void updating() {
        }

        public void updated() {
          initTheme();
        }

      });

      ui.init();
    }

    themeCounter++;

    themeTpProps.addSuperObject(tpProps);
    themeTabProps.addSuperObject(tabProps);
  }

  /**
   * Gets the name for this theme
   *
   * @return the name
   */
  public String getName() {
    return "Look and Feel Theme";
  }

  /**
   * Gets the TabbedPanelProperties for this theme
   *
   * @return the TabbedPanelProperties
   */
  public TabbedPanelProperties getTabbedPanelProperties() {
    return themeTpProps;
  }

  /**
   * Gets the TitledTabProperties for this theme
   *
   * @return the TitledTabProperties
   */
  public TitledTabProperties getTitledTabProperties() {
    return themeTabProps;
  }

  /**
   * <p>
   * Disposes this theme.
   * </p>
   *
   * <p>
   * This method must be called in order to dispose the heavyweight AWT
   * component used internally.
   * </p>
   */
  public void dispose() {
    if (!disposed) {
      disposed = true;

      themeCounter--;

      if (themeCounter == 0) {
        ui.dispose();

        PropertyMapManager.runBatch(new Runnable() {
          public void run() {
            tpProps.getMap().clear(true);
            tabProps.getMap().clear(true);
          }
        });
      }
    }
  }

  private void initTheme() {
    PropertyMapManager.runBatch(new Runnable() {
      public void run() {
        tpProps.getMap().clear(true);
        tabProps.getMap().clear(true);

        {
          tpProps.setShadowEnabled(false).setTabSpacing(ui.getTabSpacing()).setTabScrollingOffset(ui.getScrollOffset())
              .setEnsureSelectedTabVisible(true);
          tpProps.getTabAreaComponentsProperties().getComponentProperties().setBorder(null).setInsets(
              InsetsUtil.EMPTY_INSETS);

          tpProps.getTabAreaProperties().getComponentProperties().setInsets(InsetsUtil.EMPTY_INSETS).setBorder(new Border() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            }

            public Insets getBorderInsets(Component c) {
              TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
              return tp.isTabAreaVisible() ?
                     ui.getTabAreaInsets(tp.getProperties().getTabAreaOrientation()) : InsetsUtil.EMPTY_INSETS;
            }

            public boolean isBorderOpaque() {
              return false;
            }

          });

          tpProps.getTabAreaComponentsProperties().getShapedPanelProperties().setOpaque(ui.isTabAreaComponentsOpaque());

          tpProps.getTabAreaProperties().getShapedPanelProperties().setOpaque(ui.isTabAreaOpaque())
              .setComponentPainter(new ComponentPainter() {

                public void paint(Component component, Graphics g, int x, int y, int width, int height) {
                }

                public void paint(Component component, Graphics g, int x, int y, int width, int height, Direction direction,
                                  boolean horizontalFlip, boolean verticalFlip) {
                  ui.paintTabArea(TabbedUtils.getParentTabbedPanel(component), g, x, y, width, height);
                }

                public boolean isOpaque(Component component) {
                  return false;
                }

                public Color getColor(Component component) {
                  return null;
                }
              });

          tpProps.getContentPanelProperties().getShapedPanelProperties().setOpaque(ui.isContentOpaque())
              .setComponentPainter(new ComponentPainter() {
                public void paint(Component component, Graphics g, int x, int y, int width, int height) {
                }

                public void paint(Component component, Graphics g, int x, int y, int width, int height, Direction direction,
                                  boolean horizontalFlip, boolean verticalFlip) {
                  TabbedPanelContentPanel p = TabbedUtils.getParentTabbedPanelContentPanel(component);
                  ui.paintContentArea(p, g, x, y, width, height);
                }

                public boolean isOpaque(Component component) {
                  return false;
                }

                public Color getColor(Component component) {
                  return null;
                }
              });

          tpProps.getContentPanelProperties().getComponentProperties().setInsets(InsetsUtil.EMPTY_INSETS).setBorder(new Border() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            }

            public Insets getBorderInsets(Component c) {
              TabbedPanel tp = TabbedUtils.getParentTabbedPanelContentPanel(c).getTabbedPanel();
              return ui.getContentInsets(tp.getProperties().getTabAreaOrientation(), tp.isTabAreaVisible());
            }

            public boolean isBorderOpaque() {
              return false;
            }
          });
        }

        {
          tabProps.setSizePolicy(TitledTabSizePolicy.INDIVIDUAL_SIZE).setBorderSizePolicy(
              TitledTabBorderSizePolicy.EQUAL_SIZE)
              .setHighlightedRaised(ui.getSelectedRaised(Direction.UP)).setFocusMarkerEnabled(false);

          tabProps.getNormalProperties().setIconTextGap(ui.getTextIconGap()).setTextTitleComponentGap(
              ui.getTextIconGap());

          tabProps.getNormalProperties().getComponentProperties().setInsets(InsetsUtil.EMPTY_INSETS)
              .setBorder(createTabInsetsBorder(false)).setFont(ui.getFont());
          tabProps.getHighlightedProperties().getComponentProperties().setBorder(createTabInsetsBorder(true));

          tabProps.getDisabledProperties().getComponentProperties().setBorder(createTabInsetsBorder(false));

          tabProps.getNormalProperties().getShapedPanelProperties().setOpaque(false).setComponentPainter(null);

          tabProps.setMinimumSizeProvider(new DimensionProvider() {
            public Dimension getDimension(Component c) {
              return ui.getTabExternalMinSize(
                  TabbedUtils.getParentTab(c).getTabbedPanel().getProperties().getTabAreaOrientation());
            }
          });

          tabProps.setHoverListener(new HoverListener() {
            public void mouseEntered(HoverEvent event) {
              ui.setHoveredTab((Tab) event.getSource());
            }

            public void mouseExited(HoverEvent event) {
              ui.setHoveredTab(null);
            }
          });
        }
      }
    });
  }

  private Border createTabInsetsBorder(final boolean selected) {
    return new Border() {
      public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      }

      public Insets getBorderInsets(Component c) {
        TitledTab tab = (TitledTab) TabbedUtils.getParentTab(c);

        if (tab.getTabbedPanel() == null)
          return new Insets(0, 0, 0, 0);

        Direction areaOrientation = tab.getTabbedPanel().getProperties().getTabAreaOrientation();
        Direction tabDirection = selected ? tab.getProperties().getHighlightedProperties().getDirection() : tab.getProperties()
            .getNormalProperties().getDirection();

        return selected ?
               ui.getSelectedTabInsets(areaOrientation, tabDirection) :
               ui.getNormalTabInsets(areaOrientation, tabDirection);
      }

      public boolean isBorderOpaque() {
        return false;
      }
    };
  }

  public Color getBorderColor(Direction d) {
    return ui.getContentTabAreaBorderColor(d);
  }
}
