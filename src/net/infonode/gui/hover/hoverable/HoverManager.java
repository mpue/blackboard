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


// $Id: HoverManager.java,v 1.3 2011-09-07 19:56:10 mpue Exp $

package net.infonode.gui.hover.hoverable;

import java.awt.AWTEvent;
import java.awt.AWTPermission;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import net.infonode.gui.ComponentUtil;
import net.infonode.util.ArrayUtil;

/**
 * @author johan
 */
public class HoverManager {
  private static HoverManager INSTANCE = new HoverManager();

  private final HierarchyListener hierarchyListener = new HierarchyListener() {
    public void hierarchyChanged(final HierarchyEvent e) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            if (((Component) e.getSource()).isShowing()) {
              addHoverListeners((Hoverable) e.getSource());
            } else {
              removeHoverListeners((Hoverable) e.getSource());
            }
          }
        }
      });
    }
  };

  private final MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
  };

  private final HashSet hoverableComponents = new HashSet();

  private final ArrayList enteredComponents = new ArrayList();

  private boolean enabled = true;

  private boolean hasPermission = true;

  private boolean active = true;

  private boolean gotEnterAfterExit = false;

  private boolean isDrag = false;

  private final AWTEventListener eventListener = new AWTEventListener() {
    public void eventDispatched(final AWTEvent e) {
      if (active) {
        HoverManager.this.eventDispatched(e);
      }
    }
  };

  private void eventDispatched(final AWTEvent e) {
    if (e.getSource() instanceof Component /* Fix for TrayIcon in 1.6. Only handle real components */ && e instanceof MouseEvent) {
      MouseEvent event = (MouseEvent) e;

      if (event.getID() == MouseEvent.MOUSE_PRESSED || event.getID() == MouseEvent.MOUSE_RELEASED) {
        handleButtonEvent(event);
      } else if (event.getID() == MouseEvent.MOUSE_ENTERED || event.getID() == MouseEvent.MOUSE_MOVED) {
        handleEnterEvent(event);
      } else if (event.getID() == MouseEvent.MOUSE_EXITED) {
        handleExitEvent(event);
      } else if (event.getID() == MouseEvent.MOUSE_DRAGGED) {
        isDrag = true;
      }
    }
  }

  private void handleButtonEvent(MouseEvent event) {
    if (event.getID() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseEvent.BUTTON1) {
      enabled = false;
      isDrag = false;
    } else if (!enabled && event.getID() == MouseEvent.MOUSE_RELEASED) {
      enabled = true;

      if (isDrag) {
        final Component top = ComponentUtil.getTopLevelAncestor((Component) event.getSource());
        if (top == null)
          exitAll();
        else if (!((Component) event.getSource()).contains(event.getPoint())) {
          final Point p = SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(), top);
          if (!top.contains(p.x, p.y)) {
            exitAll();
          } else if (top instanceof Container) {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                    Component c = ComponentUtil.findComponentUnderGlassPaneAt(p, top);

                    if (c != null) {
                      Point p2 = SwingUtilities.convertPoint(top, p, c);
                      eventDispatched(new MouseEvent(c, MouseEvent.MOUSE_ENTERED, 0, 0, p2.x, p2.y, 0, false));
                    }
                  }
                });
              }
            });
          }
        }
      }
    }
  }

  private void handleEnterEvent(MouseEvent event) {
    gotEnterAfterExit = true;

    ArrayList exitables = new ArrayList(enteredComponents);
    ArrayList enterables = new ArrayList();

    Component c = (Component) event.getSource();
    while (c != null) {
      if (hoverableComponents.contains(c)) {
        exitables.remove(c);
        enterables.add(c);
      }

      c = c.getParent();
    }

    if (enterables.size() > 0) {
      Object obj[] = enterables.toArray();
      for (int i = obj.length - 1; i >= 0; i--) {
        if (!((Hoverable) obj[i]).acceptHover(enterables)) {
          enterables.remove(obj[i]);
          exitables.add(obj[i]);
        }
      }
    }

    for (int i = exitables.size() - 1; i >= 0; i--) {
      dispatchExit((Hoverable) exitables.get(i));
    }

    for (int i = enterables.size() - 1; i >= 0; i--) {
      dispatchEnter((Hoverable) enterables.get(i));
    }
  }

  private void handleExitEvent(MouseEvent event) {
    gotEnterAfterExit = false;

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (!gotEnterAfterExit)
          exitAll();
      }
    });
  }

  public static HoverManager getInstance() {
    return INSTANCE;
  }

  private HoverManager() {
    try {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
        sm.checkPermission(new AWTPermission("listenToAllAWTEvents"));
    } catch (SecurityException e) {
      hasPermission = false;
    }
  }

  private void exitAll() {
    gotEnterAfterExit = false;
    Object[] obj = enteredComponents.toArray();
    for (int i = obj.length - 1; i >= 0; i--) {
      dispatchExit((Hoverable) obj[i]);
    }
  }

  public void init() {
    gotEnterAfterExit = false;
    isDrag = false;
    enabled = true;
  }

  public void setEventListeningActive(boolean active) {
    this.active = active;
  }

  public void dispatchEvent(MouseEvent event) {
    eventDispatched(event);
  }

  private void addHoverListeners(Hoverable hoverable) {
    if (hoverableComponents.add(hoverable)) {
      Component c = (Component) hoverable;
      c.addMouseListener(mouseAdapter);
      c.addMouseMotionListener(mouseAdapter);

      if (active && hoverableComponents.size() == 1) {
        try {
          Toolkit.getDefaultToolkit().addAWTEventListener(eventListener, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
          hasPermission = true;
        } catch (SecurityException e) {
          hasPermission = false;
        }
      }
    }
  }

  private void removeHoverListeners(Hoverable hoverable) {
    if (hoverableComponents.remove(hoverable)) {
      ((Component) hoverable).removeMouseListener(mouseAdapter);
      ((Component) hoverable).removeMouseMotionListener(mouseAdapter);
      dispatchExit(hoverable);

      if (hasPermission && hoverableComponents.size() == 0) {
        Toolkit.getDefaultToolkit().removeAWTEventListener(eventListener);
      }
    }
  }

  public void addHoverable(Hoverable hoverable) {
    if (hoverable instanceof Component) {
      Component c = (Component) hoverable;

      if (ArrayUtil.contains(c.getHierarchyListeners(), hierarchyListener))
        return;

      c.addHierarchyListener(hierarchyListener);

      if (c.isShowing())
        addHoverListeners(hoverable);
    }
  }

  public void removeHoverable(Hoverable hoverable) {
    Component c = (Component) hoverable;
    c.removeHierarchyListener(hierarchyListener);
    removeHoverListeners(hoverable);
  }

  public boolean isHovered(Hoverable c) {
    return enteredComponents.contains(c);
  }

  public boolean isEventListeningActive() {
    return active && hasPermission;
  }

  private void dispatchEnter(Hoverable hoverable) {
    if (enabled && !enteredComponents.contains(hoverable)) {
      enteredComponents.add(hoverable);
      hoverable.hoverEnter();
    }
  }

  private void dispatchExit(Hoverable hoverable) {
    if (enabled && enteredComponents.remove(hoverable))
      hoverable.hoverExit();
  }
}