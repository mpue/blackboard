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


// $Id: TabWindowMover.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.docking;

import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabDragEvent;
import net.infonode.tabbedpanel.TabEvent;
import net.infonode.tabbedpanel.TabbedPanel;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
class TabWindowMover extends TabAdapter {
  private AbstractTabWindow window;
  private TabbedPanel tabbedPanel;
  private WindowDragger dragger;

  TabWindowMover(AbstractTabWindow window, TabbedPanel tabbedPanel) {
    this.window = window;
    this.tabbedPanel = tabbedPanel;
  }

  public void tabDragged(TabDragEvent event) {
    if (dragger == null) {
      DockingWindow w = ((WindowTab) event.getTab()).getWindow();

      if (!w.getWindowProperties().getDragEnabled())
        return;

      dragger = new WindowDragger(w);
      window.setDraggedTabIndex(tabbedPanel.getTabIndex(event.getTab()));
    }

/*    if (tabbedPanel.tabAreaContainsPoint(SwingUtilities.convertPoint(event.getTab(), event.getPoint(), tabbedPanel)))
      dragger.abort();
    else*/
    dragger.dragWindow(event.getMouseEvent());
  }

  public void tabDropped(TabDragEvent event) {
    if (dragger != null) {
      dragger.dropWindow(event.getMouseEvent());
      dragger = null;
    }
  }

  public void tabDragAborted(TabEvent event) {
    if (dragger != null) {
      dragger.abortDrag();
      dragger = null;
    }
  }

}
