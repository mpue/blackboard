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


// $Id: PaneUI.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.theme.internal.laftheme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.infonode.gui.InsetsUtil;
import net.infonode.gui.draggable.DraggableComponentBox;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelContentPanel;
import net.infonode.util.Direction;

public class PaneUI {
  private static final boolean PAINT_TAB_AREA = true;

  private static final boolean PAINT_CONTENT_AREA = true;

  private static final boolean PAINT_TAB = true;

  private static final boolean TEXT_ICON_GAP_COMPENSATE = true;

  private static final int DEFAULT_SELECTED_INDEX = 3;

  private static final int DEFAULT_TAB_COUNT = 7;

  private static final int EXTRA_SIZE = 2;

  private static final String EMPTY_STRING = "";

  // Order is IMPORTANT!!!
  private static final Direction[] DIRECTIONS = new Direction[]{Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};

  private final Insets[] areaInsets = new Insets[DIRECTIONS.length];

  private final Insets[] normalInsets = new Insets[DIRECTIONS.length];

  private final Insets[] selectedInsets = new Insets[DIRECTIONS.length];

  private final Insets[] adjustedContentInsets = new Insets[DIRECTIONS.length];

  private final Insets[] adjustedContentInsetsTabAreaHidden = new Insets[DIRECTIONS.length];

  private final Insets[] contentInsets = new Insets[DIRECTIONS.length];

  private final Dimension[] minimumSizes = new Dimension[DIRECTIONS.length];

  private final Dimension[] tabMinimumSizes = new Dimension[DIRECTIONS.length];

  private final int[] spacings = new int[DIRECTIONS.length];

  private final int[] raiseds = new int[DIRECTIONS.length];

  private final Insets[] tabInsets = new Insets[DIRECTIONS.length];

  private final Color[] contentTabAreaBorderColors = new Color[DIRECTIONS.length];

  private final boolean[] swapWidthHeights = new boolean[DIRECTIONS.length];

  private boolean tabAreaNotVisibleFix = false;

  private int scrollOffset = 0;

  private int textIconGap;

  private final PaneUIListener listener;

  private static ComponentCache componentCache = new ComponentCache();

  private final PaneHandler paneHandler = new PaneHandler(new PaneHandlerListener() {
    public void updating() {
      setEnabled(false);
      listener.updating();
    }

    public void updated() {
      doInit();
      setEnabled(true);
      listener.updated();
    }
  });

  private Tab hoveredTab;

  private final TabData tabData = new TabData();

  private boolean tabAreaOpaque;

  private boolean contentOpaque;

  private boolean opaque;

  private boolean tabAreaComponentsOpaque;

  private boolean enabled = true;

  public PaneUI(final PaneUIListener listener) {
    this.listener = listener;
  }

  public void init() {
    paneHandler.update();
  }

  private void doInit() {
    initPreCommonValues();

    for (int i = 0; i < DIRECTIONS.length; i++) {
      PanePainter pane = paneHandler.getPainter(DIRECTIONS[i]);
      initValues(pane, i, DIRECTIONS[i]);
      reset(pane);
    }

    initPostCommonValues();
  }

  public void dispose() {
    enabled = false;
    paneHandler.dispose();
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  private void initPreCommonValues() {
    {
      // Hack for some look and feels
      tabAreaNotVisibleFix = UIManager.getLookAndFeel().getClass().getName().indexOf(".WindowsLookAndFeel") > -1;
    }

    {
      // Icon text gap
      textIconGap = UIManager.getInt("TabbedPane.textIconGap");
      if (textIconGap <= 0)
        textIconGap = 4;
    }

    {
      // Opaque
      opaque = paneHandler.getPainter(Direction.UP).isOpaque();

      Boolean contentOp = (Boolean) UIManager.get("TabbedPane.contentOpaque");
      if (contentOp == null)
        contentOpaque = opaque;
      else
        contentOpaque = contentOp.booleanValue();

      tabAreaOpaque = opaque;

      tabAreaComponentsOpaque = false;
    }
  }

  private void initPostCommonValues() {
    {
      // Scroll offset
      for (int i = 0; i < DIRECTIONS.length; i++)
        scrollOffset = Math.max(scrollOffset, Math.max(minimumSizes[i].width, minimumSizes[i].height));
    }
  }

  private void initValues(PanePainter pane, int index, Direction direction) {
    estimateSwappedTabDirection(pane, index, direction);

    reset(pane);

    pane.setSize(1000, 1000);

    boolean upDown = !direction.isHorizontal();

    for (int i = 0; i < DEFAULT_TAB_COUNT; i++) {
      pane.addTab(EMPTY_STRING, getComponent());
    }

    pane.setSelectedIndex(DEFAULT_SELECTED_INDEX);

    pane.doValidation();

    {
      // Tab insets if any from UImanager
      Insets insets = UIManager.getInsets("TabbedPane.tabInsets");
      if (insets == null)
        insets = new Insets(0, 0, 0, 0);

      if (!upDown)
        tabInsets[index] = new Insets(0, insets.left, 0, insets.right);
      else
        tabInsets[index] = InsetsUtil.EMPTY_INSETS;
    }

    {
      // Raised
      Rectangle bounds = pane.getBoundsAt(0);
      Rectangle bounds2 = pane.getBoundsAt(pane.getSelectedIndex());

      if (direction == Direction.UP)
        raiseds[index] = Math.max(0, bounds.y - bounds2.y);
      else if (direction == Direction.LEFT)
        raiseds[index] = Math.max(0, bounds.x - bounds2.x);
      else if (direction == Direction.DOWN)
        raiseds[index] = raiseds[getDirectionIndex(Direction.UP)];
      else
        raiseds[index] = raiseds[getDirectionIndex(Direction.LEFT)];
    }

    {
      // Spacing
      Insets normal = getCalculatedInsets(pane, 0, false, direction);
      Insets selected = getCalculatedInsets(pane, 0, true, direction);

      if (upDown)
        spacings[index] = normal.left + normal.right - selected.left - selected.right;
      else
        spacings[index] = normal.top + normal.bottom - selected.top - selected.bottom;
    }

    {
      // Normal insets
      normalInsets[index] = getCalculatedInsets(pane, 0, false, direction);
    }

    {
      // Selected insets
      Insets insets = getCalculatedInsets(pane, 0, true, direction);
      int spacing = spacings[index];
      int spaceFirst = spacing / 2;
      int spaceAfter = spacing / 2 + spacing % 2;

      if (direction == Direction.UP) {
        insets.bottom = normalInsets[index].bottom;
        insets.top = normalInsets[index].top;
        insets.left += spaceFirst;
        insets.right += spaceAfter;
      }
      else if (direction == Direction.LEFT) {
        insets.right = normalInsets[index].right;
        insets.left = normalInsets[index].left;
        insets.top += spaceFirst;
        insets.bottom += spaceAfter;
      }
      else if (direction == Direction.RIGHT) {
        insets.right = normalInsets[index].right;
        insets.left = normalInsets[index].left;
        insets.top += spaceFirst;
        insets.bottom += spaceAfter;
      }
      else {
        insets.bottom = normalInsets[index].bottom;
        insets.top = normalInsets[index].top;
        insets.left += spaceFirst;
        insets.right += spaceAfter;
      }

      selectedInsets[index] = insets;
    }

    {
      // Content insets
      JPanel c = new JPanel();
      pane.addTab(EMPTY_STRING, c);
      pane.setSelectedIndex(pane.getTabCount() - 1);
      pane.doValidation();

      Point l = SwingUtilities.convertPoint(c.getParent(), c.getLocation(), pane);

      Rectangle bounds = pane.getBoundsAt(0);
      int top = 0;
      int left = 0;
      int bottom = 0;
      int right = 0;

      if (direction == Direction.UP) {
        top = l.y - bounds.height - bounds.y;
        left = l.x;
        bottom = pane.getHeight() - l.y - c.getHeight();
        right = pane.getWidth() - l.x - c.getWidth();
      }
      else if (direction == Direction.DOWN) {
        top = l.y;
        left = l.x;
        bottom = pane.getHeight() - c.getHeight() - l.y - (pane.getHeight() - bounds.y);
        right = pane.getWidth() - l.x - c.getWidth();
      }
      else if (direction == Direction.LEFT) {
        top = l.y;
        left = l.x - bounds.width - bounds.x;
        bottom = pane.getHeight() - l.y - c.getHeight();
        right = pane.getWidth() - l.x - c.getWidth();
      }
      else {
        top = l.y;
        left = l.x;
        bottom = pane.getHeight() - l.y - c.getHeight();
        right = pane.getWidth() - c.getWidth() - l.x - (pane.getWidth() - bounds.x);
      }

      contentInsets[index] = new Insets(top, left, bottom, right);

      Insets i = contentInsets[0];
      Insets i2 = InsetsUtil.rotate(direction.getNextCW(), i);
      Insets adjustedInsets = InsetsUtil.max(i, i2);
      adjustedContentInsets[index] = adjustedInsets;
      adjustedContentInsetsTabAreaHidden[index] = new Insets(
          direction == Direction.UP ? adjustedInsets.left : adjustedInsets.top,
                                    direction == Direction.LEFT ?
                                                                 adjustedInsets.top : adjustedInsets.left, direction == Direction.DOWN ? adjustedInsets.right
                                                                                                                                       : adjustedInsets.bottom, direction == Direction.RIGHT ?
                                                                                                                                                                                              adjustedInsets.bottom :
                                                                                                                                                                                                adjustedInsets.right);

      pane.removeTabAt(pane.getTabCount() - 1);
      pane.setSelectedIndex(DEFAULT_SELECTED_INDEX);

      pane.doValidation();
    }

    {
      // Minimum sizes
      Rectangle bounds = pane.getBoundsAt(DEFAULT_SELECTED_INDEX);
      tabMinimumSizes[index] = new Dimension(bounds.width, bounds.height);
      minimumSizes[index] = new Dimension(bounds.width - tabInsets[index].left - tabInsets[index].right, bounds.height
          - tabInsets[index].top - tabInsets[index].bottom);
    }

    calculateAreaInsets(pane, index, direction);

    estimateContentTabAreaBorderColor(pane, index, direction);
  }

  private void calculateAreaInsets(PanePainter pane, int index, Direction direction) {
    {
      // Area insets
      pane.setSelectedIndex(0);
      Rectangle selectedBounds = pane.getBoundsAt(0);
      pane.setSelectedIndex(DEFAULT_SELECTED_INDEX);

      Rectangle normalBounds = pane.getBoundsAt(0);
      int left = 0;
      int top = 0;
      int bottom = 0;
      int right = 0;

      if (direction == Direction.UP) {
        left = Math.min(selectedBounds.x, normalBounds.x);
        top = Math.min(selectedBounds.y, normalBounds.y);
        bottom = 0;
        // right = left;
      }
      else if (direction == Direction.DOWN) {
        left = Math.min(selectedBounds.x, normalBounds.x);
        top = 0;
        bottom = pane.getHeight() - Math.max(selectedBounds.y + selectedBounds.height,
            normalBounds.y + normalBounds.height);
        // right = left;
      }
      else if (direction == Direction.LEFT) {
        top = Math.min(selectedBounds.y, normalBounds.y);
        left = Math.min(selectedBounds.x, normalBounds.x);
        right = 0;
        // bottom = top;
      }
      else {
        top = Math.min(selectedBounds.y, normalBounds.y);
        left = 0;
        right = pane.getWidth() - Math.max(selectedBounds.x + selectedBounds.width,
            normalBounds.x + normalBounds.width);
        // bottom = top;
      }

      Dimension size = pane.getSize();

      reset(pane);

      for (int i = 0; i < 4; i++)
        pane.addTab(EMPTY_STRING, SizeIcon.EMPTY, getComponent());

      pane.setSelectedIndex(-1);

      pane.setSize(pane.getMinimumSize());
      pane.doValidation();

      if (!direction.isHorizontal()) {
        int width = pane.getWidth() - 1;

        boolean found = false;

        while (!found) {
          width++;
          pane.setSize(width, pane.getHeight());

          pane.doValidation();
          found = pane.getBoundsAt(0).y == pane.getBoundsAt(3).y;
        }

        Rectangle endBounds = pane.getBoundsAt(3);
        right = pane.getWidth() - endBounds.x - endBounds.width - spacings[index];
      }
      else {
        int height = pane.getHeight() - 1;

        boolean found = false;

        while (!found) {
          height++;
          pane.setSize(pane.getWidth(), height);

          pane.doValidation();
          found = pane.getBoundsAt(0).x == pane.getBoundsAt(3).x;
        }

        Rectangle endBounds = pane.getBoundsAt(3);
        bottom = pane.getHeight() - endBounds.y - endBounds.height - spacings[index];
      }

      areaInsets[index] = new Insets(top, left, bottom, right);

      pane.setSize(size);

      pane.doValidation();
    }
  }

  private void estimateContentTabAreaBorderColor(PanePainter pane, int index, final Direction direction) {
    Dimension preSize = pane.getSize();

    reset(pane);

    pane.addTab(EMPTY_STRING, SizeIcon.EMPTY, getComponent());

    pane.setSelectedIndex(-1);

    Dimension size = pane.getMinimumSize();

    if (direction.isHorizontal())
      pane.setSize(size.width, size.height * 2);
    else
      pane.setSize(size.width * 2, size.height);

    pane.doValidation();

    Rectangle tabBounds = pane.getBoundsAt(0);

    BufferedImage img = new BufferedImage(pane.getWidth(), pane.getHeight(), BufferedImage.TYPE_INT_ARGB);

    int x = 0;
    int y = 0;

    if (direction == Direction.UP) {
      x = tabBounds.x + (tabBounds.width / 2);
      y = pane.getHeight() - contentInsets[index].top - contentInsets[index].bottom - 1;
    }
    else if (direction == Direction.DOWN) {
      x = tabBounds.x + (tabBounds.width / 2);
      y = contentInsets[index].top + contentInsets[index].bottom;
    }
    else if (direction == Direction.LEFT) {
      x = pane.getWidth() - contentInsets[index].left - contentInsets[index].right - 1;
      y = tabBounds.y + (tabBounds.height / 2);
    }
    else {
      x += contentInsets[index].left + contentInsets[index].right;
      y = tabBounds.y + (tabBounds.height / 2);
    }

    final int px = x;
    final int py = y;

    RGBImageFilter colorFilter = new RGBImageFilter() {
      public int filterRGB(int x, int y, int rgb) {
        if (px == x && py == y) {
          int r = (rgb >> 16) & 0xff;
          int g = (rgb >> 8) & 0xff;
          int b = (rgb) & 0xff;
          int a = (rgb >> 24) & 0xff;
          contentTabAreaBorderColors[getDirectionIndex(direction.getOpposite())] = new Color(r, g, b, a);
        }

        return rgb;
      }
    };

    FilteredImageSource source = new FilteredImageSource(img.getSource(), colorFilter);
    pane.paint(img.getGraphics());

    BufferedImage img2 = new BufferedImage(pane.getWidth(), pane.getHeight(), BufferedImage.TYPE_INT_ARGB);
    img2.getGraphics().drawImage(Toolkit.getDefaultToolkit().createImage(source), 0, 0, null);

    pane.setSize(preSize);

    pane.doValidation();
  }

  private void estimateSwappedTabDirection(PanePainter pane, int index, final Direction direction) {
    reset(pane);

    SizeIcon icon = new SizeIcon(80, 80);
    SizeIcon icon2 = new SizeIcon(160, 80);

    pane.addTab(EMPTY_STRING, icon, getComponent());
    pane.doValidation();

    Rectangle bounds = pane.getBoundsAt(0);
    pane.setIconAt(0, icon2);
    pane.doValidation();
    Rectangle bounds2 = pane.getBoundsAt(0);

    swapWidthHeights[index] = bounds2.height > 1.5 * bounds.height;
  }

  public boolean isContentOpaque() {
    return contentOpaque;
  }

  public boolean isOpaque() {
    return opaque;
  }

  public boolean isTabAreaComponentsOpaque() {
    return tabAreaComponentsOpaque;
  }

  public boolean isTabAreaOpaque() {
    return tabAreaOpaque;
  }

  public Font getFont() {
    return paneHandler.getPainter(Direction.UP).getFont();
  }

  public boolean isSwapWidthHeight(Direction d) {
    return swapWidthHeights[getDirectionIndex(d)];
  }

  public Insets getNormalInsets(Direction d) {
    return normalInsets[getDirectionIndex(d)];
  }

  public Insets getSelectedInsets(Direction d) {
    return selectedInsets[getDirectionIndex(d)];
  }

  public Insets getNormalTabInsets(Direction areaOrientation, Direction tabDirection) {
    return getRealTabInsets(areaOrientation, tabDirection, getNormalInsets(areaOrientation));
  }

  public Insets getSelectedTabInsets(Direction areaOrientation, Direction tabDirection) {
    return getRealTabInsets(areaOrientation, tabDirection, getSelectedInsets(areaOrientation));
  }

  private Insets getRealTabInsets(Direction areaOrientation, Direction tabDirection, Insets insets) {
    insets = InsetsUtil.rotate(tabDirection, insets);

    if (swapWidthHeights[getDirectionIndex(areaOrientation)]) {
      insets = InsetsUtil.rotate(areaOrientation.getNextCCW(), insets);
    }

    return insets;
  }

  public Insets getContentInsets(Direction d, boolean tabAreaVisible) {
    return tabAreaVisible ?
                           adjustedContentInsets[getDirectionIndex(d)] : adjustedContentInsetsTabAreaHidden[getDirectionIndex(d)];
  }

  public Insets getTabAreaInsets(Direction d) {
    return areaInsets[getDirectionIndex(d)];
  }

  public Dimension getTabExternalMinSize(Direction d) {
    return minimumSizes[getDirectionIndex(d)];
  }

  public Insets getTabInsets(Direction d) {
    return tabInsets[getDirectionIndex(d)];
  }

  public int getTabSpacing(Direction d) {
    return spacings[getDirectionIndex(d)];
  }

  public int getSelectedRaised(Direction d) {
    return raiseds[getDirectionIndex(d)];
  }

  public Color getContentTabAreaBorderColor(Direction d) {
    return contentTabAreaBorderColors[getDirectionIndex(d)];
  }

  public int getTabSpacing() {
    return 0;
  }

  public int getTextIconGap() {
    return textIconGap;
  }

  public int getScrollOffset() {
    return scrollOffset;
  }

  private int getWidthCompensate(Direction d) {
    if (swapWidthHeights[getDirectionIndex(d)])
      return 0;

    return TEXT_ICON_GAP_COMPENSATE ? getTextIconGap() : 0;
  }

  private int getHeightCompensate(Direction d) {
    if (!swapWidthHeights[getDirectionIndex(d)])
      return 0;

    return TEXT_ICON_GAP_COMPENSATE ? getTextIconGap() : 0;
  }

  private int getDirectionIndex(Direction d) {
    for (int i = 0; i < DIRECTIONS.length; i++)
      if (DIRECTIONS[i] == d)
        return i;

    return 0;
  }

  private Insets getCalculatedInsets(PanePainter pane, int index, boolean selected, Direction direction) {
    Rectangle b = pane.getBoundsAt(index);
    final int sizer = b.height + b.width;

    Icon icon = pane.getIconAt(index);

    pane.setIconAt(index, new SizeIcon(sizer, sizer));

    if (selected)
      pane.setSelectedIndex(index);

    Rectangle bounds = pane.getBoundsAt(index);

    pane.setIconAt(index, icon);
    pane.setSelectedIndex(DEFAULT_SELECTED_INDEX);

    int height = bounds.height - sizer - getHeightCompensate(direction);
    int width = bounds.width - sizer - getWidthCompensate(direction);
    int top = height / 2;
    int left = width / 2 + width % 2;
    int bottom = height / 2 + height % 2;
    int right = width / 2;

    return new Insets(top, left, bottom, right);
  }

  public void setHoveredTab(Tab tab) {
    if (enabled) {
      if (tab != hoveredTab) {
        if (hoveredTab != null && hoveredTab.getTabbedPanel() != null)
          findDraggableComponentBox(hoveredTab).getParent().repaint();

        hoveredTab = tab;

        if (hoveredTab != null && hoveredTab.getTabbedPanel() != null)
          findDraggableComponentBox(hoveredTab).getParent().repaint();
      }
    }
  }

  public void paintTabArea(TabbedPanel tp, Graphics g, int x, int y, int width, int height) {
    if (enabled) {
      if (tp.isTabAreaVisible()) {
        tabData.initialize(tp);

        PanePainter pane = paneHandler.getPainter(tabData.getAreaOrientation());

        initTabLocations(pane);
        Insets aInsets = getTabAreaInsets(tabData.getAreaOrientation());

        if (tp.getTabCount() > 0) {
          // Adjust x, y
          if (tabData.getAreaOrientation() == Direction.DOWN) {
            y += tabData.getTabbedPanelHeight() - height;
          }
          else if (tabData.getAreaOrientation() == Direction.RIGHT) {
            x += tabData.getTabbedPanelWidth() - width;
          }

          width = x < 0 ? width + x : width;
          height = y < 0 ? height + y : height;

          x = Math.max(0, x);
          y = Math.max(0, y);

          if (tabData.isHorizontalLayout())
            pane.setSize(tabData.getTabbedPanelSize().width, getTabbedPanelExtraSize());
          else
            pane.setSize(getTabbedPanelExtraSize(), tabData.getTabbedPanelHeight());

          if (PAINT_TAB_AREA && !(pane.getTabCount() == 0 && tabData.getTabCount() > 0)) {
            Shape originalClip = g.getClip();

            int tx = -x
            - (tabData.getAreaOrientation() == Direction.RIGHT ?
                                                                -tabData.getTabbedPanelWidth() + getTabbedPanelExtraSize() : 0);
            int ty = -y
            - (tabData.getAreaOrientation() == Direction.DOWN ?
                                                               -tabData.getTabbedPanelHeight() + getTabbedPanelExtraSize() : 0);

            Rectangle firstVisibleRect = (Rectangle) tabData.getVisibleTabRects().get(0);
            Rectangle lastVisibleRect = (Rectangle) tabData.getVisibleTabRects().get(tabData.getTabCount() - 1);
            Tab lastTab = (Tab) tabData.getTabList().get(tabData.getTabCount() - 1);

            if (tabData.isHorizontalLayout()) {
              int extraWidth = lastTab.getWidth() == lastVisibleRect.width ? 0 : 2 * tabData.getTabbedPanelSize()
                                                                           .width
                                                                           - tabData.getTabAreaWidth();
              pane.setSize(pane.getWidth() + extraWidth, pane.getHeight());

              pane.doValidation();

              // Before tabs
              g.clipRect(0, 0, aInsets.left + (firstVisibleRect.width > 0 && firstVisibleRect.x == 0 ? 1 : 0), height);
              pane.paint(g, tx, ty);
              g.setClip(originalClip);

              // After tabs
              tx -= extraWidth;

              int clipExtraWidth = extraWidth == 0 ? 1 : 0;
              g.clipRect(aInsets.left + tabData.getTabAreaWidth() - clipExtraWidth, 0, width - aInsets.left - tabData.getTabAreaWidth()
                  + clipExtraWidth, height);
              pane.paint(g, tx, ty);
              g.setClip(originalClip);
            }
            else {
              int extraHeight = lastTab.getHeight() == lastVisibleRect.height ? 0 : 2 * tabData.getTabbedPanelSize()
                                                                              .height
                                                                              - tabData.getTabAreaHeight();
              pane.setSize(pane.getWidth(), pane.getHeight() + extraHeight);

              pane.doValidation();

              // Before tabs
              g.clipRect(0, 0, width, aInsets.top + (firstVisibleRect.height > 0 && firstVisibleRect.y == 0 ? 1 : 0));
              pane.paint(g, tx, ty);
              g.setClip(originalClip);

              // After tabs
              ty -= extraHeight;

              int clipExtraHeight = extraHeight == 0 ? 1 : 0;
              g.clipRect(0, aInsets.top + tabData.getTabAreaHeight() - clipExtraHeight, width, height - aInsets.top
                  - tabData.getTabAreaHeight() + clipExtraHeight);
              pane.paint(g, tx, ty);
              g.setClip(originalClip);
            }
          }

          // First and last tab
          paintTabs(pane, tabData, g, x, y, width, height, true);

          tabData.reset();

          reset(pane);
        }
      }
    }
  }

  private void paintTabs(PanePainter pane, TabData tabData, Graphics g, int x, int y, int width, int height, boolean first) {
    if (enabled) {
      if (PAINT_TAB) {
        Tab lastTab = (Tab) tabData.getTabList().get(tabData.getTabList().size() - 1);
        Rectangle lastVisibleRect = (Rectangle) tabData.getVisibleTabRects().get(tabData.getTabCount() - 1);

        // Fix post/pre tabs
        initPaintableTabLocations(pane);

        Insets aInsets = getTabAreaInsets(tabData.getAreaOrientation());

        Point l = getLocationInTabbedPanel(lastTab, tabData.getTabbedPanel());

        if (tabData.isHorizontalLayout()) {
          int w = aInsets.left + aInsets.right + Math.max(0, tabData.getTabAreaWidth() - l.x - lastVisibleRect.width) + EXTRA_SIZE;

          for (int i = 0; i < tabData.getTabList().size(); i++)
            w += ((Tab) tabData.getTabList().get(i)).getWidth();

          pane.setSize(w, getTabbedPanelExtraSize());
        }
        else {
          int h = aInsets.top + aInsets.bottom + Math.max(0,
              tabData.getTabAreaHeight() - l.y - lastVisibleRect.height) + EXTRA_SIZE;

          for (int i = 0; i < tabData.getTabList().size(); i++)
            h += ((Tab) tabData.getTabList().get(i)).getHeight();

          pane.setSize(getTabbedPanelExtraSize(), h);
        }

        pane.doValidation();

        int index = tabData.getPreTab() == null ? 0 : tabData.getTabCount() > 1 ? 1 : 0;

        Shape originalClip = g.getClip();

        int tx = -x - (tabData.getAreaOrientation() == Direction.RIGHT ?
                                                                        -tabData.getTabbedPanelWidth() + getTabbedPanelExtraSize() : 0);
        int ty = -y - (tabData.getAreaOrientation() == Direction.DOWN ?
                                                                       -tabData.getTabbedPanelHeight() + getTabbedPanelExtraSize() : 0);

        Rectangle visibleRect = (Rectangle) tabData.getVisibleTabRects().get(index);
        Tab tab = (Tab) tabData.getTabList().get(index);

        if (tabData.isHorizontalLayout()) {
          tx -= (tabData.getPreTab() != null ? tab.getX() - tabData.getPreTab().getX() + visibleRect.x : visibleRect.x);
          g.clipRect(aInsets.left, 0, tabData.getTabAreaWidth(), height);
        }
        else {
          ty -= (tabData.getPreTab() != null ? tab.getY() - tabData.getPreTab().getY() + visibleRect.y : visibleRect.y);
          g.clipRect(0, aInsets.top, width, tabData.getTabAreaHeight());
        }

        applyFocusAndHover(pane, true);
        pane.paint(g, tx, ty);
        applyFocusAndHover(pane, false);

        g.setClip(originalClip);
      }
    }
  }

  private int getTabbedPanelExtraSize() {
    Insets insets = getContentInsets(tabData.getAreaOrientation(), tabData.getTabbedPanel().isTabAreaVisible());

    if (tabData.isHorizontalLayout())
      return tabData.getTabAreaHeight() + insets.top + insets.bottom + EXTRA_SIZE;
    else
      return tabData.getTabAreaWidth() + insets.left + insets.right + EXTRA_SIZE;
  }

  public void paintContentArea(TabbedPanelContentPanel p, Graphics g, int x, int y, int width, int height) {
    if (enabled) {
      if (PAINT_CONTENT_AREA) {
        tabData.initialize(p.getTabbedPanel());
        PanePainter pane = paneHandler.getPainter(tabData.getAreaOrientation());

        initTabLocations(pane);

        int tx = 0;
        int ty = 0;

        if (tabData.getTabbedPanel().hasContentArea()) {
          Point l = getLocationInTabbedPanel(p, tabData.getTabbedPanel());

          int yComp = 0;
          int xComp = 0;

          if (/* !tabData.getTabbedPanel().hasContentArea() || */(pane.getTabCount() == 0 && tabData.getTabCount() > 0)) {
            if (tabData.getAreaOrientation() == Direction.UP) {
              yComp = tabData.getTabAreaHeight();
            }
            else if (tabData.getAreaOrientation() == Direction.DOWN) {
              yComp = -tabData.getTabAreaHeight();
            }
            else if (tabData.getAreaOrientation() == Direction.LEFT) {
              xComp = tabData.getTabAreaWidth();
            }
            else {
              xComp = -tabData.getTabAreaWidth();
            }
          }

          tx = -l.x + (xComp > 0 ? xComp : 0);
          ty = -l.y + (yComp > 0 ? yComp : 0);

          int extraWidth = 0;
          int extraHeight = 0;

          if (tabAreaNotVisibleFix && !tabData.getTabbedPanel().isTabAreaVisible()) {
            extraWidth = !tabData.isHorizontalLayout() ? tabMinimumSizes[getDirectionIndex(
                tabData.getAreaOrientation())].width
                - raiseds[getDirectionIndex(tabData.getAreaOrientation())]
                          + (tabData.getAreaOrientation() == Direction.LEFT ? areaInsets[getDirectionIndex(
                              Direction.LEFT)].left
                              : areaInsets[getDirectionIndex(Direction.RIGHT)].right) : 0;
                          extraHeight = tabData.isHorizontalLayout() ? tabMinimumSizes[getDirectionIndex(
                              tabData.getAreaOrientation())].height
                              - raiseds[getDirectionIndex(tabData.getAreaOrientation())]
                                        + (tabData.getAreaOrientation() == Direction.UP ? areaInsets[getDirectionIndex(
                                            Direction.UP)].top
                                            : areaInsets[getDirectionIndex(Direction.DOWN)].bottom) : 0;
          }

          tx -= tabData.getAreaOrientation() == Direction.LEFT ? extraWidth : 0;
          ty -= tabData.getAreaOrientation() == Direction.UP ? extraHeight : 0;

          pane.setSize(tabData.getTabbedPanelSize().width - Math.abs(xComp) + extraWidth, tabData.getTabbedPanelSize()
              .height
              - Math.abs(yComp) + extraHeight);

          pane.doValidation();
        }
        else {
          if (tabData.isHorizontalLayout()) {
            pane.setSize(p.getWidth(), p.getHeight() + tabData.getTabAreaHeight());
          }
          else {
            pane.setSize(p.getWidth() + tabData.getTabAreaWidth(), p.getHeight());
          }

          pane.doValidation();

          if (tabData.getAreaOrientation() == Direction.UP)
            ty -= tabData.getTabAreaHeight();
          else if (tabData.getAreaOrientation() == Direction.LEFT)
            tx -= tabData.getTabAreaWidth();
        }

        pane.paint(g, tx, ty);

        tabData.reset();

        reset(pane);
      }
    }
  }

  private Component getComponent() {
    return componentCache.getComponent();
  }

  private void reset(PanePainter pane) {
    pane.removeAllTabs();
    componentCache.reset();
  }

  private Point getLocationInTabbedPanel(Component c, TabbedPanel tp) {
    Point l = SwingUtilities.convertPoint(c.getParent(), c.getLocation(), tp);
    Insets tpInsets = tp.getInsets();
    l.x -= tpInsets.left;
    l.y -= tpInsets.top;

    return l;
  }

  private void initPaintableTabLocations(PanePainter pane) {
    reset(pane);

    if (tabData.getPreTab() != null) {
      tabData.getTabList().add(0, tabData.getPreTab());
      tabData.getVisibleTabRects().add(0, new Rectangle(0, 0, 0, 0));
    }

    if (tabData.getPostTab() != null) {
      tabData.getTabList().add(tabData.getPostTab());
      tabData.getVisibleTabRects().add(new Rectangle(0, 0, 0, 0));
    }

    int size = 0;
    int selectedIndex = -1;

    for (int i = 0; i < tabData.getTabCount(); i++) {
      final Tab tab = (Tab) tabData.getTabList().get(i);

      SizeIcon icon = new SizeIcon(getInternalTabWidth(tab) - getWidthCompensate(tabData.getAreaOrientation()), getInternalTabHeight(
          tab)
          - getHeightCompensate(
              tabData.getAreaOrientation()), isSwapWidthHeight(
                  tabData.getAreaOrientation()));

      pane.addTab(EMPTY_STRING, icon, getComponent());

      if (tab.isHighlighted())
        selectedIndex = pane.getTabCount() - 1;

      if (!tab.isEnabled()) {
        pane.setEnabledAt(i, false);
        pane.setDisabledIconAt(i, icon);
      }

      size += tabData.isHorizontalLayout() ? tab.getWidth() : tab.getHeight();
    }

    pane.setSelectedIndex(selectedIndex);
    pane.doValidation();
  }

  private void applyFocusAndHover(PanePainter pane, boolean active) {
    if (active) {
      for (int i = 0; i < tabData.getTabCount(); i++) {
        Tab tab = (Tab) tabData.getTabList().get(i);

        if (tab.getFocusableComponent() != null && tab.getFocusableComponent().hasFocus()) {
          pane.setMouseEntered(true);
          pane.setFocusActive(true);
          break;
        }
      }

      if (hoveredTab != null) {
        for (int i = 0; i < tabData.getTabCount(); i++) {
          Tab tab = (Tab) tabData.getTabList().get(i);

          if (tab == hoveredTab) {
            pane.setMouseEntered(true);
            pane.setHoveredTab(i);
            break;
          }
        }
      }
    }
    else {
      pane.setFocusActive(false);
      pane.setMouseEntered(false);
    }
  }

  private int getInternalTabWidth(Tab tab) {
    Direction areaOrientation = tab.getTabbedPanel().getProperties().getTabAreaOrientation();
    Insets insets = tab.isHighlighted() ? getSelectedInsets(areaOrientation) : getNormalInsets(areaOrientation);
    int width = tab.getWidth();

    width -= insets.left + insets.right;

    if (areaOrientation == Direction.LEFT || areaOrientation == Direction.RIGHT)
      width -= getSelectedRaised(areaOrientation);

    return width;
  }

  private int getInternalTabHeight(Tab tab) {
    Direction areaOrientation = tab.getTabbedPanel().getProperties().getTabAreaOrientation();
    Insets insets = tab.isHighlighted() ? getSelectedInsets(areaOrientation) : getNormalInsets(areaOrientation);
    int height = tab.getHeight();

    height -= insets.top + insets.bottom;

    if (areaOrientation == Direction.UP || areaOrientation == Direction.DOWN)
      height -= getSelectedRaised(areaOrientation);

    return height;
  }

  private void initTabLocations(PanePainter pane) {
    findPaintableTabs();

    Dimension minSize = getTabExternalMinSize(tabData.getAreaOrientation());
    Insets aInsets = getTabAreaInsets(tabData.getAreaOrientation());

    int selectedIndex = -1;

    if (tabData.getTabbedPanel().isTabAreaVisible()) {
      for (int i = 0; i < tabData.getTabCount(); i++) {
        final Tab tab = (Tab) tabData.getTabList().get(i);
        final Rectangle visibleRect = (Rectangle) tabData.getVisibleTabRects().get(i);
        Insets insets = getTabInsets(tabData.getAreaOrientation());

        int iconWidth = Math.max(-insets.left - insets.right,
            getInternalTabWidth(tab) - (tab.getWidth() - visibleRect.width));

        int iconHeight = Math.max(-insets.top - insets.bottom,
            getInternalTabHeight(tab) - (tab.getHeight() - visibleRect.height));

        Point l = getLocationInTabbedPanel(tab, tabData.getTabbedPanel());

        if ((tabData.isHorizontalLayout() && (visibleRect.width >= minSize.width || minSize.width < tabData.getTabbedPanelWidth() - l.x
            - aInsets.right))
            || (!tabData.isHorizontalLayout() && (visibleRect.height >= minSize.height || minSize.height < tabData.getTabbedPanelHeight()
                - l.y - aInsets.bottom))) {
          final int iWidth = iconWidth;
          final int iHeight = iconHeight;

          SizeIcon icon = new SizeIcon(iWidth - getWidthCompensate(tabData.getAreaOrientation()), iHeight
              - getHeightCompensate(
                  tabData.getAreaOrientation()), isSwapWidthHeight(
                      tabData.getAreaOrientation()));

          int j = pane.getTabCount();
          pane.addTab(EMPTY_STRING, icon, getComponent());

          if (i == tabData.getSelectedTabPainterIndex()) {
            selectedIndex = j;
          }

          if (!tab.isEnabled()) {
            pane.setEnabledAt(j, false);
            pane.setDisabledIconAt(j, icon);
          }
        }
      }
    }
    else if (tabAreaNotVisibleFix) {
      pane.addTab(EMPTY_STRING, componentCache.getComponent());
    }

    if (pane.getTabCount() > 0)
      pane.setSelectedIndex(selectedIndex);

    pane.doValidation();
  }

  private void findPaintableTabs() {
    Tab firstTab = null;
    Rectangle firstVisibleRect = null;
    Tab previousTab = null;

    int i = 0;
    boolean tabsFound = false;

    if (tabData.getTabbedPanel().isTabAreaVisible()) {
      while (i < tabData.getTabbedPanel().getTabCount()) {
        Tab tab = tabData.getTabbedPanel().getTabAt(i);
        Rectangle r = tab.getVisibleRect();

        if (i == 0) {
          firstTab = tab;
          firstVisibleRect = r;
        }

        i++;

        if (r.width > 0 && r.height > 0) {
          tabsFound = true;
          tabData.getTabList().add(tab);
          tabData.getVisibleTabRects().add(r);

          if (tabData.getTabCount() == 1)
            tabData.setPreTab(previousTab);

          if (tab.isHighlighted()) {
            tabData.setSelectedTabPainterIndex(tabData.getTabCount() - 1);
          }
        }
        else if (tabData.getTabList().size() > 0 && (r.width == 0 || r.height == 0))
          tabData.setPostTab(tab);

        if (tabsFound
            && r.x == 0
            && r.y == 0
            && ((tabData.isHorizontalLayout() && r.width < tab.getWidth()) || (!tabData.isHorizontalLayout() && r.height < tab.getHeight()))) {
          break;
        }

        previousTab = tab;
      }

      if (firstTab != null) {
        // TODO: Ugly!
        Component box = findDraggableComponentBox(firstTab);

        if (box != null) {
          if (tabData.isHorizontalLayout()) {
            tabData.setTabAreaWidth(box.getWidth());
            tabData.setTabAreaHeight(box.getParent().getHeight());
          }
          else {
            tabData.setTabAreaWidth(box.getParent().getWidth());
            tabData.setTabAreaHeight(box.getHeight());
          }
        }

        if (tabData.getTabCount() == 0) {
          tabData.getTabList().add(firstTab);
          tabData.getVisibleTabRects().add(firstVisibleRect);
        }
      }
    }
  }

  private Component findDraggableComponentBox(Component c) {
    if (c == null || c instanceof DraggableComponentBox)
      return c;

    return findDraggableComponentBox(c.getParent());
  }
}