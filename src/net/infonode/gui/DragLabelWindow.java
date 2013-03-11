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


// $Id: DragLabelWindow.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JLabel;

/**
 * @author johan
 */
public class DragLabelWindow extends Dialog {
  private JLabel label = new JLabel() {
    public Dimension getMinimumSize() {
      return getPreferredSize();
    }
  };

  public DragLabelWindow(Dialog d) {
    super(d);
    init();
  }

  public DragLabelWindow(Frame f) {
    super(f);
    init();
  }

  private void init() {
    setLayout(new BorderLayout());
    add(label, BorderLayout.NORTH);
    setUndecorated(true);
    label.setOpaque(true);
  }

  public JLabel getLabel() {
    return label;
  }

  public void setCursor(Cursor c) {
    label.setCursor(c);
  }

  public void setVisible(boolean visible) {
    if (visible != isVisible()) {
      if (visible) {
        setSize(0, 0);
        pack();
        setSize(getPreferredSize());
      }

      //label.setCursor(visible ? DragSource.DefaultMoveDrop : Cursor.getDefaultCursor());
    }

    super.setVisible(visible);
  }
}