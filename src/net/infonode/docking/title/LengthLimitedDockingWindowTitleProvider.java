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


// $Id: LengthLimitedDockingWindowTitleProvider.java,v 1.3 2011-09-07 19:56:11 mpue Exp $
package net.infonode.docking.title;

import java.io.Serializable;
import java.util.ArrayList;

import net.infonode.docking.AbstractTabWindow;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.View;

/**
 * A docking window title provider that constructs a window title from the views inside the window. It adds view
 * titles until the window title reaches a specified length. If not all view titles fit into the window title,
 * primarily titles from view inside selected tabs are used.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
public class LengthLimitedDockingWindowTitleProvider implements DockingWindowTitleProvider, Serializable {
  private static final long serialVersionUID = 1;

  private int maxLength;

  /**
   * Constructor.
   *
   * @param maxLength if the title exceeds this length no more view titles are added to it
   */
  public LengthLimitedDockingWindowTitleProvider(int maxLength) {
    this.maxLength = maxLength;
  }

  public String getTitle(DockingWindow window) {
    ArrayList viewTitles = new ArrayList();
    ArrayList viewPrimary = new ArrayList();
    getViews(window, viewTitles, viewPrimary, true);

    int length = 0;

    for (int i = 0; i < viewTitles.size(); i++) {
      if (((Boolean) viewPrimary.get(i)).booleanValue()) {
        length += ((String) viewTitles.get(i)).length();
      }
    }

    StringBuffer title = new StringBuffer(40);
    int count = 0;

    for (int i = 0; i < viewTitles.size() && title.length() < maxLength; i++) {
      boolean primary = ((Boolean) viewPrimary.get(i)).booleanValue();

      if (primary || length < maxLength) {
        if (title.length() > 0)
          title.append(", ");

        title.append((String) viewTitles.get(i));
        count++;

        if (!primary)
          length += ((String) viewTitles.get(i)).length();
      }
    }

    if (count < viewTitles.size())
      title.append(", ...");

    return title.toString();
  }

  private void getViews(DockingWindow window, ArrayList viewTitles, ArrayList viewPrimary, boolean primary) {
    if (window == null)
      return;
    else if (window instanceof View) {
      viewTitles.add(((View) window).getViewProperties().getTitle());
      viewPrimary.add(Boolean.valueOf(primary));
    }
    else if (window instanceof AbstractTabWindow) {
      DockingWindow selected = ((AbstractTabWindow) window).getSelectedWindow();

      for (int i = 0; i < window.getChildWindowCount(); i++) {
        getViews(window.getChildWindow(i), viewTitles, viewPrimary, selected == window.getChildWindow(i) && primary);
      }
    }
    else {
      for (int i = 0; i < window.getChildWindowCount(); i++) {
        getViews(window.getChildWindow(i), viewTitles, viewPrimary, primary);
      }
    }
  }

}
