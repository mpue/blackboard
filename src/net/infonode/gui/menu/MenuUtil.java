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


// $Id: MenuUtil.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.gui.menu;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import net.infonode.gui.icon.IconUtil;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class MenuUtil {
  private MenuUtil() {
  }

  public static void optimizeSeparators(JPopupMenu menu) {
    boolean lastSeparator = true;

    for (int i = 0; i < menu.getComponentCount();) {
      if (menu.getComponent(i).isVisible() && menu.getComponent(i) instanceof JMenu)
        optimizeSeparators(((JMenu) menu.getComponent(i)).getPopupMenu());

      boolean separator = menu.getComponent(i) instanceof JPopupMenu.Separator;

      if (lastSeparator && separator)
        menu.remove(i);
      else
        i++;

      lastSeparator = separator;
    }

    if (menu.getComponentCount() > 0 &&
        menu.getComponent(menu.getComponentCount() - 1) instanceof JPopupMenu.Separator)
      menu.remove(menu.getComponentCount() - 1);
  }

  public static void align(MenuElement menu) {
    MenuElement[] children = menu.getSubElements();
    final int maxWidth = IconUtil.getMaxIconWidth(children);

    for (int i = 0; i < children.length; i++) {
      if (children[i] instanceof AbstractButton) {
        AbstractButton b = (AbstractButton) children[i];
        final Icon icon = b.getIcon();
        b.setIcon(new Icon() {
          public int getIconHeight() {
            return icon == null ? 1 : icon.getIconHeight();
          }

          public int getIconWidth() {
            return maxWidth;
          }

          public void paintIcon(Component c, Graphics g, int x, int y) {
            if (icon != null)
              icon.paintIcon(c, g, x, y);
          }
        });
      }

      align(children[i]);
    }
  }

}
