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


// $Id: DynamicUIManager.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DynamicUIManager {
  private static final DynamicUIManager instance = new DynamicUIManager();

  private ArrayList listeners = new ArrayList(2);
  private ArrayList prioritizedListeners = new ArrayList(2);

  private String[] properties = {"win.3d.backgroundColor",
                                 "win.3d.highlightColor",
                                 "win.3d.lightColor",
                                 "win.3d.shadowColor",
                                 "win.frame.activeCaptionColor",
                                 "win.frame.activeCaptionGradientColor",
                                 "win.frame.captionTextColor",
                                 "win.frame.activeBorderColor",
                                 "win.mdi.backgroundColor",
                                 "win.desktop.backgroundColor",
                                 "win.frame.inactiveCaptionColor",
                                 "win.frame.inactiveCaptionGradientColor",
                                 "win.frame.inactiveCaptionTextColor",
                                 "win.frame.inactiveBorderColor",
                                 "win.menu.backgroundColor",
                                 "win.menu.textColor",
                                 "win.frame.textColor?????",
                                 "win.item.highlightColor",
                                 "win.item.highlightTextColor",
                                 "win.tooltip.backgroundColor",
                                 "win.tooltip.textColor",
                                 "win.frame.backgroundColor",
                                 "win.frame.textColor",
                                 "win.item.hotTrackedColor"};
  private Toolkit currentToolkit;
  private boolean propertyChangePending;

  public static DynamicUIManager getInstance() {
    return instance;
  }

  private DynamicUIManager() {
    final PropertyChangeListener l = new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        handlePropertyChanges();
      }
    };

    UIManager.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("lookAndFeel")) {
          setupPropertyListener(l);
          fireLookAndFeelChanging();
          fireLookAndFeelChanged();
        }
      }
    });
    UIManager.getDefaults().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        if (!(event.getNewValue() instanceof Class))
          handlePropertyChanges();
      }
    });

    setupPropertyListener(l);
  }

  private void setupPropertyListener(PropertyChangeListener l) {
    if (currentToolkit != null)
      for (int i = 0; i < properties.length; i++)
        currentToolkit.removePropertyChangeListener(properties[i], l);

    currentToolkit = Toolkit.getDefaultToolkit();
    for (int i = 0; i < properties.length; i++) {
      currentToolkit.addPropertyChangeListener(properties[i], l);
    }
  }

  public void addListener(DynamicUIManagerListener l) {
    listeners.add(l);
  }

  public void removeListener(DynamicUIManagerListener l) {
    listeners.remove(l);
  }

  public void addPrioritizedListener(DynamicUIManagerListener l) {
    prioritizedListeners.add(l);
  }

  public void removePrioritizedListener(DynamicUIManagerListener l) {
    prioritizedListeners.remove(l);
  }

  private void fireLookAndFeelChanging() {
    Object l[] = prioritizedListeners.toArray();
    Object l2[] = listeners.toArray();

    for (int i = 0; i < l.length; i++)
      ((DynamicUIManagerListener) l[i]).lookAndFeelChanging();

    for (int i = 0; i < l2.length; i++)
      ((DynamicUIManagerListener) l2[i]).lookAndFeelChanging();
  }

  private void fireLookAndFeelChanged() {
    Object l[] = prioritizedListeners.toArray();
    Object l2[] = listeners.toArray();

    for (int i = 0; i < l.length; i++)
      ((DynamicUIManagerListener) l[i]).lookAndFeelChanged();

    for (int i = 0; i < l2.length; i++)
      ((DynamicUIManagerListener) l2[i]).lookAndFeelChanged();
  }

  private void handlePropertyChanges() {
    if (!propertyChangePending) {
      propertyChangePending = true;

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          propertyChangePending = false;

          firePropertyChanged();
        }
      });

      firePropertyChanging();
    }
  }

  private void firePropertyChanging() {
    Object l[] = prioritizedListeners.toArray();
    Object l2[] = listeners.toArray();

    for (int i = 0; i < l.length; i++)
      ((DynamicUIManagerListener) l[i]).propertiesChanging();

    for (int i = 0; i < l2.length; i++)
      ((DynamicUIManagerListener) l2[i]).propertiesChanging();
  }

  private void firePropertyChanged() {
    Object l[] = prioritizedListeners.toArray();
    Object l2[] = listeners.toArray();

    for (int i = 0; i < l.length; i++)
      ((DynamicUIManagerListener) l[i]).propertiesChanged();

    for (int i = 0; i < l2.length; i++)
      ((DynamicUIManagerListener) l2[i]).propertiesChanged();
  }
}

