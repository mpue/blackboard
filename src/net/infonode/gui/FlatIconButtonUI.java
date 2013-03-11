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


// $Id: FlatIconButtonUI.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class FlatIconButtonUI extends BasicButtonUI {
  private final static FlatIconButtonUI buttonUI = new FlatIconButtonUI();

  public static ComponentUI createUI(JComponent c) {
    return buttonUI;
  }

  protected void installDefaults(AbstractButton b) {
  }

/*  public void paint(Graphics g, JComponent c) {
    AbstractButton button = (AbstractButton) c;
    Insets i = getInsets(button);
    button.getIcon().paintIcon(c, g, i.left, i.top);
    paintIcon(g, c, new Rectangle(i.left, i.top, c.getWidth(), c.getHeight()));
  }

  public Dimension getPreferredSize(JComponent c) {
    AbstractButton button = (AbstractButton) c;
    Insets i = getInsets(button);
    return new Dimension(i.left + i.right + button.getIcon().getIconWidth(),
                         i.top + i.bottom + button.getIcon().getIconWidth());
  }

  private Insets getInsets(AbstractButton button) {
    return InsetsUtil.add(button.getInsets(), button.getMargin());
  }
*/
}
