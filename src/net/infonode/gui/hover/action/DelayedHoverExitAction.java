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


// $Id: DelayedHoverExitAction.java,v 1.3 2011-09-07 19:56:09 mpue Exp $

package net.infonode.gui.hover.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Timer;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;

/**
 * @author johan
 */
public class DelayedHoverExitAction implements HoverListener {

  private HashMap timers = new HashMap();
  private HashMap exitEvents = new HashMap();

  private HoverListener action;
  private int delay;

  public DelayedHoverExitAction(HoverListener action, int delay) {
    this.action = action;
    this.delay = delay;
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public HoverListener getHoverAction() {
    return action;
  }

  public void forceExit(Component component) {
    if (timers.containsKey(component)) {
      ((Timer) timers.get(component)).stop();
      timers.remove(component);
      HoverEvent event = (HoverEvent) exitEvents.get(component);
      exitEvents.remove(component);
      action.mouseExited(event);
    }
  }

  public void mouseEntered(HoverEvent event) {
    final Component c = event.getSource();

    if (timers.containsKey(c))
      ((Timer) timers.get(c)).stop();
    else {
      Timer t = new Timer(delay, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          forceExit(c);
        }
      });
      t.setRepeats(false);
      timers.put(c, t);

      action.mouseEntered(event);
    }
  }

  public void mouseExited(HoverEvent event) {
    exitEvents.put(event.getSource(), event);
    ((Timer) timers.get(event.getSource())).restart();
  }
}