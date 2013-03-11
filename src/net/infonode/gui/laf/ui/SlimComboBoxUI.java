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


// $Id: SlimComboBoxUI.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.laf.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.metal.MetalComboBoxUI;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class SlimComboBoxUI extends MetalComboBoxUI {
  public static Border FOCUS_BORDER = new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(0, 3, 0, 3));
  public static Border NORMAL_BORDER = new EmptyBorder(1, 4, 1, 4);

  public static ComponentUI createUI(JComponent b) {
    return new SlimComboBoxUI();
  }

  protected ListCellRenderer createRenderer() {
    return new BasicComboBoxRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                    boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setBorder(index == -1 ? noFocusBorder : cellHasFocus ? FOCUS_BORDER : NORMAL_BORDER);
        return label;
      }
    };
  }


}
