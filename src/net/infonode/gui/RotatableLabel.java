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


// $Id: RotatableLabel.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.plaf.LabelUI;

import net.infonode.util.Direction;

public class RotatableLabel extends JLabel {
  private RotatableLabelUI ui = new RotatableLabelUI(Direction.RIGHT);

  public RotatableLabel(String text) {
    super(text);
    init();
  }

  public RotatableLabel(String text, Icon icon) {
    super(text, icon, LEFT);
    init();
  }

  private void init() {
    super.setUI(ui);
    super.setOpaque(false);
  }

  public Direction getDirection() {
    return ui.getDirection();
  }

  public void setDirection(Direction direction) {
    if (ui.getDirection() != direction) {
      ui.setDirection(direction);
      revalidate();
    }
  }

  public void setMirror(boolean mirror) {
    ui.setMirror(mirror);
    revalidate();
  }

  public boolean isMirror() {
    return ui.isMirror();
  }

  public void setUI(LabelUI ui) {
    // Ignore
  }

  private boolean isVertical() {
    return !ui.getDirection().isHorizontal();
  }

  private Dimension rotateDimension(Dimension dim) {
    return dim == null ? null : isVertical() ? new Dimension(dim.height, dim.width) : dim;
  }

  public Dimension getPreferredSize() {
    return rotateDimension(super.getPreferredSize());
  }

  public Dimension getMinimumSize() {
    return rotateDimension(super.getMinimumSize());
  }

  public Dimension getMaximumSize() {
    return rotateDimension(super.getMaximumSize());
  }

  public void setMinimumSize(Dimension minimumSize) {
    super.setMinimumSize(rotateDimension(minimumSize));
  }

  public void setMaximumSize(Dimension maximumSize) {
    super.setMaximumSize(rotateDimension(maximumSize));
  }

  public void setPreferredSize(Dimension preferredSize) {
    super.setPreferredSize(rotateDimension(preferredSize));
  }

  public void setOpaque(boolean opaque) {
  }

}
