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


// $Id: TitleBarUI.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.theme.internal.laftheme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.beans.PropertyVetoException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.infonode.docking.View;
import net.infonode.docking.internal.ViewTitleBar;
import net.infonode.gui.DimensionProvider;
import net.infonode.gui.DynamicUIManager;
import net.infonode.gui.DynamicUIManagerListener;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.tabbedpanel.theme.internal.laftheme.SizeIcon;
import net.infonode.util.ColorUtil;
import net.infonode.util.Direction;

public class TitleBarUI {
  private static final int NUM_FADE_COLORS = 25;

  private static final int BUTTON_OFFSET = 2;

  private static final int RIGHT_INSET = 4;

  private final boolean showing = true;

  private boolean enabled;

  private final DynamicUIManagerListener uiListener = new DynamicUIManagerListener() {

    public void lookAndFeelChanged() {
      doUpdate();
    }

    public void propertiesChanging() {
      listener.updating();
    }

    public void propertiesChanged() {
      doUpdate();
    }

    public void lookAndFeelChanging() {
      listener.updating();
    }

  };

  private class IFrame extends JInternalFrame {
    public IFrame() {
    }

    public void updateUI() {
      super.updateUI();

      setClosable(false);
      setIconifiable(false);
      setMaximizable(false);
      setFocusable(false);
    }

    public void setSelectedActivated(boolean selected) {
      try {
        setSelected(selected);
      }
      catch (PropertyVetoException e) {
        e.printStackTrace();
      }
    }

    public boolean isShowing() {
      return showing;
    }
  }

  private final Color[] fadeSelectedColors = new Color[NUM_FADE_COLORS];

  private final Color[] fadeNormalColors = new Color[NUM_FADE_COLORS];

  private final IFrame iFrame = new IFrame();

  private JFrame frame;

  private Dimension reportedMinimumSize;

  private Dimension minimumSize;

  private Insets iFrameInsets;

  private Color inactiveBackgroundColor;

  private Color activeBackgroundColor;

  private Color foundBackgroundColor;

  private boolean skipIFrame = false;

  private final ComponentPainter activeComponentPainter = new ComponentPainter() {
    public void paint(Component component, Graphics g, int x, int y, int width, int height) {
    }

    public void paint(Component component,
                      Graphics g,
                      int x,
                      int y,
                      int width,
                      int height,
                      Direction direction,
                      boolean horizontalFlip,
                      boolean verticalFlip) {
      g.translate(-x, -y);
      paintTitleBar(component, g, true, width, height, Direction.UP);
      g.translate(x, y);
    }

    public boolean isOpaque(Component component) {
      return false;
    }

    public Color getColor(Component component) {
      return getActiveBackgroundColor();
    }
  };

  private final ComponentPainter inactiveComponentPainter = new ComponentPainter() {
    public void paint(Component component, Graphics g, int x, int y, int width, int height) {
    }

    public void paint(Component component,
                      Graphics g,
                      int x,
                      int y,
                      int width,
                      int height,
                      Direction direction,
                      boolean horizontalFlip,
                      boolean verticalFlip) {
      g.translate(-x, -y);
      paintTitleBar(component, g, false, width, height, Direction.UP);
      g.translate(x, y);
    }

    public boolean isOpaque(Component component) {
      return false;
    }

    public Color getColor(Component component) {
      return getInactiveBackgroundColor();
    }
  };

  private final TitleBarUIListener listener;

  public TitleBarUI(TitleBarUIListener listener, boolean enabled) {
    this.enabled = enabled;
    this.listener = listener;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void dispose() {
    DynamicUIManager.getInstance().removePrioritizedListener(uiListener);
    frame.removeAll();
    frame.dispose();
  }

  public void init() {
    DynamicUIManager.getInstance().addPrioritizedListener(uiListener);

    frame = new JFrame();

    frame.getContentPane().setLayout(null);
    frame.getContentPane().add(iFrame);
    frame.pack();

    listener.updating();
    update();
  }

  private void doUpdate() {
    setEnabled(false);
    SwingUtilities.updateComponentTreeUI(frame);

    update();
  }

  private void update() {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        iFrame.setClosable(false);
        iFrame.setMaximizable(false);
        iFrame.setIconifiable(false);
        iFrame.setBounds(0, 0, 50, 50);
        iFrame.setResizable(false);

        iFrame.setVisible(true);
        iFrame.setTitle(" ");

        iFrame.setFrameIcon(SizeIcon.EMPTY);

        {
          // Insets
          iFrameInsets = (Insets) iFrame.getInsets().clone();
          if (UIManager.getLookAndFeel().getClass().getName().indexOf(".MotifLookAndFeel") != -1) {
            iFrameInsets.left += 19;
          }
        }

        {
          // Size
          reportedMinimumSize = iFrame.getPreferredSize();
          minimumSize = new Dimension(Math.max(0, reportedMinimumSize.width - iFrameInsets.left - iFrameInsets.right),
              reportedMinimumSize.height - iFrameInsets.top - iFrameInsets.bottom);
        }

        String lafName = UIManager.getLookAndFeel().getClass().getName();
        skipIFrame = lafName.indexOf("GTKLookAndFeel") != -1
        || (lafName.indexOf(".WindowsLookAndFeel") != -1 || UIManager.getLookAndFeel().getClass().getName()
            .indexOf(".Office2003LookAndFeel") != -1) && Toolkit.getDefaultToolkit().getDesktopProperty(
            "win.xpstyle.themeActive") != null;

        estimateBackgroundColors();

        setEnabled(true);
        listener.updated();
      }

    });
  }

  private void estimateBackgroundColors() {
    activeBackgroundColor = estimateBackgroundColor(true);

    inactiveBackgroundColor = estimateBackgroundColor(false);

    double factor = 255 / fadeSelectedColors.length;

    for (int i = 0; i < fadeSelectedColors.length; i++) {
      if (activeBackgroundColor != null)
        fadeSelectedColors[i] = new Color(activeBackgroundColor.getRed(), activeBackgroundColor.getGreen(),
            activeBackgroundColor.getBlue(), (int) ((i + 1) * factor));
      if (inactiveBackgroundColor != null)
        fadeNormalColors[i] =
          new Color(inactiveBackgroundColor.getRed(), inactiveBackgroundColor.getGreen(), inactiveBackgroundColor
              .getBlue(), (int) ((i + 1) * factor));
    }
  }

  private Color estimateBackgroundColor(boolean selected) {
    setSize(400);

    iFrame.setSelectedActivated(selected);

    BufferedImage img = new BufferedImage(iFrame.getWidth(), iFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);

    int x = iFrame.getWidth() - iFrameInsets.right - 3;
    int y = iFrameInsets.top + 3;

    final int px = x;
    final int py = y;

    RGBImageFilter colorFilter = new RGBImageFilter() {
      public int filterRGB(int x, int y, int rgb) {
        if (px == x && py == y) {
          int r = (rgb >> 16) & 0xff;
          int g = (rgb >> 8) & 0xff;
          int b = (rgb) & 0xff;
          int a = (rgb >> 24) & 0xff;

          foundBackgroundColor = new Color(r, g, b, a);
        }

        return rgb;
      }
    };

    FilteredImageSource source = new FilteredImageSource(img.getSource(), colorFilter);
    iFrame.paint(img.getGraphics());

    BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    img2.getGraphics().drawImage(Toolkit.getDefaultToolkit().createImage(source), 0, 0, null);

    return foundBackgroundColor;
  }

  public DimensionProvider getSizeDimensionProvider() {
    return skipIFrame ? null : new DimensionProvider() {
      public Dimension getDimension(Component c) {
        return minimumSize;
      }
    };
  }

  public void paintTitleBar(Component c, Graphics g, boolean selected, int width, int height, Direction d) {
    if (enabled) {
      View view = findView(c);
      if (view == null)
        return;

      setTitleAndIcon(view.getViewProperties().getViewTitleBarProperties().getNormalProperties().getTitle(),
          view.getViewProperties()
          .getViewTitleBarProperties().getNormalProperties().getIcon());

      iFrame.setSelectedActivated(selected);

      setSize(width);

      Shape clip = g.getClip();

      g.clipRect(0, 0, width, reportedMinimumSize.height - iFrameInsets.top - iFrameInsets.bottom);
      g.translate(-iFrameInsets.left, -iFrameInsets.top);
      iFrame.paint(g);
      g.translate(iFrameInsets.left, iFrameInsets.top);
      g.setClip(clip);

      paintSolidButtonBackground(c, g, selected);
    }
  }

  private void paintSolidButtonBackground(Component c, Graphics g, boolean selected) {
    ViewTitleBar bar = (ViewTitleBar) c;

    JComponent[] comps = bar.getRightTitleComponents();
    if (comps.length > 0) {
      int width = 0;

      for (int i = 0; i < comps.length; i++) {
        if (comps[i].isVisible())
          width += comps[i].getWidth();
      }

      Color background = selected ? activeBackgroundColor : inactiveBackgroundColor;
      Color[] fadeColors = selected ? fadeSelectedColors : fadeNormalColors;

      for (int i = 0; i < fadeColors.length; i++) {
        g.setColor(fadeColors[i]);
        int xPos = c.getWidth() - width - (fadeColors.length - i) - RIGHT_INSET;
        g.drawLine(xPos, BUTTON_OFFSET, xPos, c.getHeight() - 2 * BUTTON_OFFSET);
      }

      g.setColor(background);
      g.fillRect(c.getWidth() - width - RIGHT_INSET,
          BUTTON_OFFSET,
          width + RIGHT_INSET,
          c.getHeight() - 2 * BUTTON_OFFSET);
    }
  }

  private void setTitleAndIcon(String title, Icon icon) {
    iFrame.setTitle(title);
    iFrame.setFrameIcon(icon == null ? SizeIcon.EMPTY : icon);
  }

  private View findView(Component c) {
    if (c == null || c instanceof View)
      return (View) c;

    return findView(c.getParent());
  }

  private void setSize(int width) {
    iFrame.setSize(width + iFrameInsets.left + iFrameInsets.right, reportedMinimumSize.height);
    iFrame.invalidate();
    iFrame.validate();
  }

  public boolean isRenderingIcon() {
    return !skipIFrame;
  }

  public boolean isRenderingTitle() {
    return !skipIFrame;
  }

  public Direction getRenderingDirection() {
    return Direction.RIGHT;
  }

  public ComponentPainter getInactiveComponentPainter() {
    if (!skipIFrame)
      return inactiveComponentPainter;

    Color bkg = UIManager.getColor("InternalFrame.inactiveTitleBackground");
    if (bkg == null)
      bkg = inactiveBackgroundColor;

    return createComponentPainter(bkg, UIManager.getColor("InternalFrame.inactiveTitleGradient"));
  }

  public ComponentPainter getActiveComponentPainter() {
    if (!skipIFrame)
      return activeComponentPainter;

    Color bkg = UIManager.getColor("InternalFrame.activeTitleBackground");
    if (bkg == null)
      bkg = activeBackgroundColor;

    return createComponentPainter(bkg, UIManager.getColor("InternalFrame.activeTitleGradient"));
  }

  public Insets getInsets() {
    return skipIFrame ? new Insets(2, 2, 2, 2) : new Insets(0, 0, 0, RIGHT_INSET);
  }

  public Color getInactiveBackgroundColor() {
    return inactiveBackgroundColor;
  }

  public Color getActiveBackgroundColor() {
    return activeBackgroundColor;
  }

  private ComponentPainter createComponentPainter(final Color background, final Color gradient) {
    final Color avgColor = ColorUtil.blend(background, gradient, 0.5);
    final ComponentPainter painter = createGradientSegmentPainter(background, gradient, true);

    return new ComponentPainter() {
      public void paint(Component component, Graphics g, int x, int y, int width, int height) {
      }

      public void paint(Component component,
                        Graphics g,
                        int x,
                        int y,
                        int width,
                        int height,
                        Direction direction,
                        boolean horizontalFlip,
                        boolean verticalFlip) {
        g.setColor(gradient);
        g.drawLine(x, y, x + width - 1, y);
        g.drawLine(x, y, x, y + height - 1);

        g.setColor(avgColor);
        g.drawRect(x + 1, y + 1, width - 3, height - 3);

        painter.paint(component, g, x + 2, y + 2, width - 4, height - 4, direction, horizontalFlip, verticalFlip);

        g.setColor(background);
        g.drawLine(x + 1, height - 1 + y, x + width - 1, height - 1 + y);
        g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
      }

      public boolean isOpaque(Component component) {
        return painter.isOpaque(component);
      }

      public Color getColor(Component component) {
        return painter.getColor(component);
      }
    };
  }

  private ComponentPainter createGradientSegmentPainter(Color background, Color gradient, boolean flip) {
    if (background != null) {
      gradient = gradient == null ? background : gradient;
      background = ColorUtil.mult(background, flip ? 1.05 : 0.90);
      gradient = ColorUtil.mult(gradient, flip ? 0.90 : 1.05);

      return new GradientComponentPainter(background, background, gradient, gradient);
    }

    return null;
  }
}
