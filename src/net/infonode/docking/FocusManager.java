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


// $Id: FocusManager.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
class FocusManager {
  private static final FocusManager INSTANCE = new FocusManager();

  private int ignoreFocusChanges;
  private Timer focusTimer = new Timer(20, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      updateFocus();
      focusUpdateTriggered = false;
    }
  });
  private boolean focusUpdateTriggered;
  private ArrayList lastFocusedWindows = new ArrayList();
  private Component focusedComponent;
  private PropertyChangeListener focusListener = new PropertyChangeListener() {
    public void propertyChange(PropertyChangeEvent evt) {
      if (ignoreFocusChanges > 0)
        return;

      ignoreFocusChanges++;

      try {
        triggerFocusUpdate();
      }
      finally {
        ignoreFocusChanges--;
      }
    }

    private void triggerFocusUpdate() {
      if (focusUpdateTriggered)
        return;

      focusUpdateTriggered = true;
      focusTimer.setRepeats(false);
      focusTimer.start();
    }

  };

  private FocusManager() {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", focusListener);
    updateFocus();
  }

  static FocusManager getInstance() {
    return INSTANCE;
  }

  private void updateFocus() {
    focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
//    System.out.println("Focus: " + System.identityHashCode(focusedComponent) + ", " + focusedComponent);
    ArrayList oldFocusedWindows = lastFocusedWindows;
    lastFocusedWindows = new ArrayList();
    updateWindows(focusedComponent, focusedComponent, oldFocusedWindows);

    for (int i = 0; i < oldFocusedWindows.size(); i++) {
      RootWindow w = (RootWindow) ((Reference) oldFocusedWindows.get(i)).get();

      if (w != null)
        w.setFocusedView(null);
    }

  }

  void pinFocus(Runnable runnable) {
    ignoreFocusChanges++;
    final Component c = focusedComponent;

    try {
      runnable.run();
    }
    finally {
      if (--ignoreFocusChanges == 0 && c != null) {
        c.requestFocusInWindow();

        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                c.requestFocusInWindow();
              }
            });
          }
        });
      }
    }
  }

  void startIgnoreFocusChanges() {
    ignoreFocusChanges++;
  }

  void stopIgnoreFocusChanges() {
    if (--ignoreFocusChanges == 0) {
      updateFocus();
    }
  }

  static void focusWindow(final DockingWindow window) {
    if (window == null)
      return;

    window.restoreFocus();

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            window.restoreFocus();
          }
        });
      }
    });
  }

  private static View getViewContaining(Component component) {
    return component == null ?
           null : component instanceof View ? (View) component : getViewContaining(component.getParent());
  }

  private void updateWindows(Component focusedComponent, Component component, ArrayList oldFocusedWindows) {
    while (true) {
      View view = getViewContaining(component);

      if (view == null)
        break;

      view.setLastFocusedComponent(focusedComponent);
      RootWindow rw = view.getRootWindow();

      if (rw == null)
        break;

      rw.setFocusedView(view);
      lastFocusedWindows.add(new WeakReference(rw));
      component = rw;

      for (int i = 0; i < oldFocusedWindows.size(); i++) {
        if (((Reference) oldFocusedWindows.get(i)).get() == component)
          oldFocusedWindows.remove(i);
      }
    }
  }

}
