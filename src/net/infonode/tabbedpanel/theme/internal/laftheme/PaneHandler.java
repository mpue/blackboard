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


// $Id: PaneHandler.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.theme.internal.laftheme;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.infonode.gui.DynamicUIManager;
import net.infonode.gui.DynamicUIManagerListener;
import net.infonode.util.Direction;

public class PaneHandler {
  private JFrame frame;

  private PanePainter[] panePainters;

  private PaneHandlerListener listener;

  private DynamicUIManagerListener uiListener = new DynamicUIManagerListener() {

    public void lookAndFeelChanged() {
      doUpdate();
    }

    public void propertiesChanging() {
      listener.updating();
    }

    public void propertiesChanged() {
      doUpdate();
    }

    public void lookAndFeelChanging() {
      listener.updating();
    }

  };

  PaneHandler(PaneHandlerListener listener) {
    this.listener = listener;

    DynamicUIManager.getInstance().addPrioritizedListener(uiListener);

    Direction[] directions = Direction.getDirections();
    panePainters = new PanePainter[directions.length];

    JPanel panel = new JPanel(null);
    for (int i = 0; i < directions.length; i++) {
      panePainters[i] = new PanePainter(directions[i]);
      panel.add(panePainters[i]);
      panePainters[i].setBounds(0, 0, 600, 600);
    }

    frame = new JFrame();
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.pack();
  }

  void dispose() {
    if (frame != null) {
      DynamicUIManager.getInstance().removePrioritizedListener(uiListener);
      frame.removeAll();
      frame.dispose();
      frame = null;
    }
  }

  PanePainter getPainter(Direction d) {
    for (int i = 0; i < panePainters.length; i++)
      if (panePainters[i].getDirection() == d)
        return panePainters[i];

    return null;
  }

  JFrame getFrame() {
    return frame;
  }

  void update() {
    listener.updating();

    doUpdate();
  }

  private void doUpdate() {
    SwingUtilities.updateComponentTreeUI(frame);

    // SwingUtilities.invokeLater(new Runnable() {
    // public void run() {
    listener.updated();
    // }
    // });
  }
}
