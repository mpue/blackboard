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


// $Id: HeavyWeightDragRectangle.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.docking.internal;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * @author johan
 */
public class HeavyWeightDragRectangle extends JPanel {
  private int width = 4;

  private Canvas northCanvas = new Canvas() {
    public Dimension getPreferredSize() {
      return new Dimension(width, width);
    }
  };
  private Canvas southCanvas = new Canvas() {
    public Dimension getPreferredSize() {
      return new Dimension(width, width);
    }
  };
  private Canvas eastCanvas = new Canvas() {
    public Dimension getPreferredSize() {
      return new Dimension(width, width);
    }
  };
  private Canvas westCanvas = new Canvas() {
    public Dimension getPreferredSize() {
      return new Dimension(width, width);
    }
  };

  public HeavyWeightDragRectangle() {
    super(new BorderLayout());
    setOpaque(false);

    add(northCanvas, BorderLayout.NORTH);
    add(southCanvas, BorderLayout.SOUTH);
    add(westCanvas, BorderLayout.WEST);
    add(eastCanvas, BorderLayout.EAST);

    setColor(Color.BLACK);
  }

  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);
    revalidate();
  }

  public void setBorderWidth(int width) {
    this.width = width;

    revalidate();
  }

  public void setColor(Color c) {
    northCanvas.setBackground(c);
    southCanvas.setBackground(c);
    eastCanvas.setBackground(c);
    westCanvas.setBackground(c);
  }
}
