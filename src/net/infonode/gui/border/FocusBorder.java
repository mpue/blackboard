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


// $Id: FocusBorder.java,v 1.2 2011-08-26 15:10:47 mpue Exp $
package net.infonode.gui.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicGraphicsUtils;

import net.infonode.gui.UIManagerUtil;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class FocusBorder implements Border, Serializable {
  private static final long serialVersionUID = 1;

  private static final Insets INSETS = new Insets(1, 1, 1, 1);

  private final Component component;

  private boolean enabled = true;

  public FocusBorder(final Component focusComponent) {
    this.component = focusComponent;
    focusComponent.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        if (enabled)
          focusComponent.repaint();
      }

      public void focusLost(FocusEvent e) {
        if (enabled)
          focusComponent.repaint();
      }
    });
  }

  public Insets getBorderInsets(Component c) {
    return INSETS;
  }

  public boolean isBorderOpaque() {
    return false;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    if (enabled != this.enabled) {
      this.enabled = enabled;
    }
  }

  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    if (enabled && component.hasFocus()) {
      g.setColor(UIManagerUtil.getColor("Button.focus", "TabbedPane.focus"));

      if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"))
        BasicGraphicsUtils.drawDashedRect(g, x, y, width, height);
      else
        g.drawRect(x, y, width - 1, height - 1);
    }
  }
}
