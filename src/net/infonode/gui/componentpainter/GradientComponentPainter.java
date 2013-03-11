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


// $Id: GradientComponentPainter.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui.componentpainter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.lang.ref.SoftReference;

import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.util.ColorUtil;
import net.infonode.util.Direction;
import net.infonode.util.ImageUtils;

/**
 * A painter that paints an gradient area specified by four corner colors.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class GradientComponentPainter extends AbstractComponentPainter {
  private static final long serialVersionUID = 1;

  private final ColorProvider[] colorProviders = new ColorProvider[4];
  private transient Color[] colors;
  private final int size = 128;
  private transient SoftReference[] images;
  private transient boolean hasAlpha;

  /**
   * Constructor.
   *
   * @param topLeftColor     the top left corner color
   * @param topRightColor    the top right corner color
   * @param bottomLeftColor  the bottom left corner color
   * @param bottomRightColor the bottom right corner color
   */
  public GradientComponentPainter(Color topLeftColor, Color topRightColor, Color bottomLeftColor,
                                  Color bottomRightColor) {
    this(new FixedColorProvider(topLeftColor), new FixedColorProvider(topRightColor), new FixedColorProvider(
        bottomLeftColor),
        new FixedColorProvider(bottomRightColor));
  }

  /**
   * Constructor.
   *
   * @param topLeftColor     the top left corner color provider
   * @param topRightColor    the top right corner color provider
   * @param bottomLeftColor  the bottom left corner color provider
   * @param bottomRightColor the bottom right corner color provider
   */
  public GradientComponentPainter(ColorProvider topLeftColor, ColorProvider topRightColor, ColorProvider bottomLeftColor,
                                  ColorProvider bottomRightColor) {
    colorProviders[0] = topLeftColor;
    colorProviders[1] = topRightColor;
    colorProviders[2] = bottomLeftColor;
    colorProviders[3] = bottomRightColor;
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
    updateColors(component);

    if (colors[0] != null && colors[1] != null && colors[2] != null && colors[3] != null) {
      if (colors[0].equals(colors[2]) && colors[1].equals(colors[3]) && colors[0].equals(colors[1])) {
        g.setColor(colors[0]);
        g.fillRect(x, y, width, height);
      }
      else {
        int imageIndex = direction.getValue() + (horizontalFlip ? 4 : 0) + (verticalFlip ? 8 : 0);
        SoftReference ref = images[imageIndex];
        Image image = ref == null ? null : (Image) ref.get();

        if (image == null) {
          image = createGradientImage(fixColors(direction, horizontalFlip, verticalFlip));
          images[imageIndex] = new SoftReference(image);
        }

        g.drawImage(image, x, y, width, height, null);
      }
    }

  }

  /*
   * private Image createLineImage(Direction direction, Color c1, Color c2) {
   * BufferedImage image = new BufferedImage(direction.isHorizontal() ? size : 1,
   * direction.isHorizontal() ? 1 : size, BufferedImage.TYPE_INT_RGB);
   * 
   * int dx = direction == Direction.RIGHT ? 1 : direction == Direction.LEFT ? -1 :
   * 0; int dy = direction == Direction.DOWN ? 1 : direction == Direction.UP ? -1 :
   * 0;
   * 
   * int x = direction == Direction.LEFT ? size - 1 : 0; int y = direction ==
   * Direction.UP ? size - 1 : 0;
   * 
   * for (int i = 0; i < size; i++) { Color c = ColorUtil.blend(c1, c2, (double) i /
   * size); image.setRGB(x, y, c.getRGB()); x += dx; y += dy; }
   * 
   * return image; }
   */
  /*  private static void drawLines(Direction direction, Graphics g, int x, int y, int width, int height, Color c1, Color c2) {
    int size = direction.isHorizontal() ? width : height;
    Int4 c = ImageUtils.toInt4(c1);
    Int4 dc = ImageUtils.toInt4(c2).sub(ImageUtils.toInt4(c1)).div(size);

    for (int i = 0; i < size; i++) {
      g.setColor(ImageUtils.toColor(c));

      if (direction == Direction.RIGHT)
        g.drawLine(x + i, y, x + i, y + height - 1);
      else if (direction == Direction.DOWN)
        g.drawLine(x, y + i, x + width - 1, y + i);
      else if (direction == Direction.LEFT)
        g.drawLine(x + width - 1 - i, y, x + width - 1 - i, y + height - 1);
      else
        g.drawLine(x, y + height - 1 - i, x + width - 1, y + height - 1 - i);

      c.add(dc);
    }
  }*/

  private Color[] fixColors(Direction direction, boolean horizontalFlip, boolean verticalFlip) {
    Color[] c = new Color[4];
    Color t;

    if (horizontalFlip) {
      c[0] = colors[1];
      c[1] = colors[0];
      c[2] = colors[3];
      c[3] = colors[2];
    }
    else {
      c[0] = colors[0];
      c[1] = colors[1];
      c[2] = colors[2];
      c[3] = colors[3];
    }

    if (verticalFlip) {
      t = c[2];
      c[2] = c[0];
      c[0] = t;

      t = c[3];
      c[3] = c[1];
      c[1] = t;
    }

    if (direction == Direction.RIGHT) {
      return c;
    }
    else if (direction == Direction.DOWN) {
      t = c[0];
      c[0] = c[2];
      c[2] = c[3];
      c[3] = c[1];
      c[1] = t;
    }
    else if (direction == Direction.LEFT) {
      t = c[0];
      c[0] = c[3];
      c[3] = t;

      t = c[1];
      c[1] = c[2];
      c[2] = t;
    }
    else if (direction == Direction.UP) {
      t = c[0];
      c[0] = c[1];
      c[1] = c[3];
      c[3] = c[2];
      c[2] = t;
    }

    return c;
  }

  private Image createGradientImage(Color[] colors) {
    return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(size,
        size,
        ImageUtils.createGradientPixels(colors,
            size,
            size),
            0,
            size));
  }

  private void updateColors(Component component) {
    if (images == null) {
      images = new SoftReference[16];
    }

    if (colors == null)
      colors = new Color[4];

    for (int i = 0; i < colors.length; i++) {
      Color c = colorProviders[i].getColor(component);

      if (c != null && !c.equals(colors[i])) {
        for (int j = 0; j < images.length; j++) {
          images[j] = null;
        }
      }

      colors[i] = c;
      hasAlpha |= c != null && c.getAlpha() != 255;
    }
  }

  public boolean isOpaque(Component component) {
    updateColors(component);
    return !hasAlpha;
  }

  public Color getColor(Component component) {
    updateColors(component);
    return ColorUtil.blend(ColorUtil.blend(colors[0], colors[1], 0.5),
        ColorUtil.blend(colors[2], colors[3], 0.5),
        0.5);
  }
}
