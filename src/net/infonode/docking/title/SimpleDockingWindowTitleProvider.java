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


// $Id: SimpleDockingWindowTitleProvider.java,v 1.3 2011-09-07 19:56:11 mpue Exp $
package net.infonode.docking.title;

import java.io.ObjectStreamException;
import java.io.Serializable;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.View;

/**
 * A docking window title provider that concatenates all the titles of all the views contained in a window.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
public class SimpleDockingWindowTitleProvider implements DockingWindowTitleProvider, Serializable {
  private static final long serialVersionUID = 1;

  public static final SimpleDockingWindowTitleProvider INSTANCE = new SimpleDockingWindowTitleProvider();

  /**
   * Constructor.
   */
  private SimpleDockingWindowTitleProvider() {
  }

  public String getTitle(DockingWindow window) {
    StringBuffer title = new StringBuffer(40);
    getTitle(window, title);
    return title.toString();
  }

  private void getTitle(DockingWindow window, StringBuffer title) {
    if (window == null)
      return;
    else if (window instanceof View) {
      if (title.length() > 0)
        title.append(", ");

      title.append(((View) window).getViewProperties().getTitle());
    }
    else {
      for (int i = 0; i < window.getChildWindowCount(); i++) {
        getTitle(window.getChildWindow(i), title);
      }
    }
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
