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


// $Id: TextIconListCellRenderer.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.infonode.gui.icon.IconUtil;

/**
 * @author johan
 */
public class TextIconListCellRenderer extends DefaultListCellRenderer {
  private ListCellRenderer renderer;
  private Icon emptyIcon;
  private int width;
  private int gap = -1;

  public TextIconListCellRenderer(ListCellRenderer renderer) {
    this.renderer = renderer;
  }

  public void calculateMaximumIconWidth(Object[] list) {
    width = IconUtil.getMaxIconWidth(list);
    emptyIcon = width == 0 ? null : new Icon() {
      public int getIconHeight() {
        return 1;
      }

      public int getIconWidth() {
        return width;
      }

      public void paintIcon(Component c, Graphics g, int x, int y) {
      }
    };
  }

  public void setRenderer(ListCellRenderer renderer) {
    this.renderer = renderer;
  }

  public Component getListCellRendererComponent(JList list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {
    if (index == -1)
      return null;

    JLabel label = (JLabel) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    if (gap < 0)
      gap = label.getIconTextGap();
    
    Icon icon = IconUtil.getIcon(value);

    if (icon == null) {
      label.setIcon(emptyIcon);
    }
    else {
      label.setIcon(icon);
      label.setIconTextGap(gap + width - icon.getIconWidth());
    }

    return label;
  }
}