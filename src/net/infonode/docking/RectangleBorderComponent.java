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


// $Id: RectangleBorderComponent.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
class RectangleBorderComponent extends JComponent {
  private int lineWidth;

  RectangleBorderComponent(int lineWidth) {
    this.lineWidth = lineWidth;
    setOpaque(false);
  }

  void setLineWidth(int lineWidth) {
    this.lineWidth = lineWidth;
  }

  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.setXORMode(Color.WHITE);
    g.fillRect(lineWidth, 0, getWidth() - 2 * lineWidth, lineWidth);
    g.fillRect(lineWidth, getHeight() - 1 - lineWidth, getWidth() - 2 * lineWidth, lineWidth);
    g.fillRect(0, 0, lineWidth, getHeight() - 1);
    g.fillRect(getWidth() - lineWidth, 0, lineWidth, getHeight() - 1);
    g.setPaintMode();
  }
}
