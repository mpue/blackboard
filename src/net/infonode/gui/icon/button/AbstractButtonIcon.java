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


// $Id: AbstractButtonIcon.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui.icon.button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.UIManager;

import net.infonode.gui.ComponentUtil;
import net.infonode.util.ColorUtil;

public abstract class AbstractButtonIcon implements Icon, Serializable {
  private static final long serialVersionUID = 1;

  private int size = 10;
  private Color defaultColor = null;
  private boolean shadowEnabled = true;
  private float shadowStrength = 0.3f;
  private boolean enabled = true;

  public AbstractButtonIcon() {
  }

  public AbstractButtonIcon(Color color) {
    this.defaultColor = color;
  }

  public AbstractButtonIcon(Color color, int size) {
    this(color);
    this.size = size;
  }

  public AbstractButtonIcon(int size) {
    this(size, true);
  }

  public AbstractButtonIcon(int size, boolean enabled) {
    this();
    this.size = size;
    this.enabled = enabled;
  }

  public int getIconWidth() {
    return size;
  }

  public int getIconHeight() {
    return size;
  }

  public boolean isShadowEnabled() {
    return shadowEnabled;
  }

  public void setShadowEnabled(boolean shadowEnabled) {
    this.shadowEnabled = shadowEnabled;
  }

  public float getShadowStrength() {
    return shadowStrength;
  }

  public void setShadowStrength(float shadowStrength) {
    this.shadowStrength = shadowStrength;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    Color oldColor = g.getColor();
    Color color = defaultColor == null ?
                  (enabled ? c.getForeground() : UIManager.getColor("Button.disabledForeground")) :
                  defaultColor;
    if (color == null)
      color = ColorUtil.blend(ComponentUtil.getBackgroundColor(c), c.getForeground(), 0.5f);

    if (shadowEnabled) {
      Color background = ComponentUtil.getBackgroundColor(c);
      g.setColor(ColorUtil.blend(background == null ? Color.BLACK : background, Color.BLACK, shadowStrength));
      paintIcon(c, g, x + 2, y + 2, x + size - 1, y + size - 1, true);
      g.setColor(color);
      paintIcon(c, g, x + 1, y + 1, x + size - 2, y + size - 2, false);
    }
    else {
      g.setColor(color);
      paintIcon(c, g, x, y, x + size - 1, y + size - 1, false);
    }

    g.setColor(oldColor);
  }

  protected void paintIcon(Component c, Graphics g, int x1, int y1, int x2, int y2, boolean isShadow) {
    paintIcon(c, g, x1, y1, x2, y2);
  }

  protected void paintIcon(Component c, Graphics g, int x1, int y1, int x2, int y2) {
  }
}
