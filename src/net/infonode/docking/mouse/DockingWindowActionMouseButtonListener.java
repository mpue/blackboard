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


// $Id: DockingWindowActionMouseButtonListener.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.docking.mouse;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.action.CloseWithAbortWindowAction;
import net.infonode.docking.action.DockingWindowAction;
import net.infonode.gui.mouse.MouseButtonListener;

/**
 * A {@link MouseButtonListener} that performs a {@link DockingWindowAction}. The action is not performed
 * if the mouse button event has been consumed.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
public class DockingWindowActionMouseButtonListener implements MouseButtonListener, Serializable {
  private static final long serialVersionUID = 1;

  private static final int MODIFIER_MASK = InputEvent.ALT_DOWN_MASK |
                                           InputEvent.SHIFT_DOWN_MASK |
                                           InputEvent.CTRL_DOWN_MASK |
                                           InputEvent.META_DOWN_MASK;

  /**
   * A listener that closes a window when its tab is clicked with the middle mouse button.
   */
  public static final MouseButtonListener MIDDLE_BUTTON_CLOSE_LISTENER =
      new DockingWindowActionMouseButtonListener(MouseEvent.BUTTON2, CloseWithAbortWindowAction.INSTANCE);

  private int eventId;
  private int button;
  private int keyMask;
  private DockingWindowAction action;
  private boolean consumeEvent;

  /**
   * Creates a listener which performs an action when a mouse button is clicked. The event is not consumed
   * when the action is performed.
   *
   * @param button when this mouse button is clicked the action is performed , must be
   *               {@link MouseEvent#BUTTON1}, {@link MouseEvent#BUTTON2} or {@link MouseEvent#BUTTON3}
   * @param action the action to perform
   */
  public DockingWindowActionMouseButtonListener(int button, DockingWindowAction action) {
    this(MouseEvent.MOUSE_CLICKED, button, action);
  }

  /**
   * Creates a listener which performs an action when a mouse button is pressed, released or clicked.
   * The event is not consumed when the action is performed.
   *
   * @param eventId the event type for which to perform the action, must be
   *                {@link MouseEvent#MOUSE_PRESSED}, {@link MouseEvent#MOUSE_RELEASED} or
   *                {@link MouseEvent#MOUSE_CLICKED}
   * @param button  when this mouse button for which the action is performed , must be
   *                {@link MouseEvent#BUTTON1}, {@link MouseEvent#BUTTON2} or {@link MouseEvent#BUTTON3}
   * @param action  the action to perform
   */
  public DockingWindowActionMouseButtonListener(int eventId, int button, DockingWindowAction action) {
    this(eventId, button, 0, action, false);
  }

  /**
   * Creates a listener which performs an action when a mouse button is pressed, released or clicked, with
   * an additional key mask.
   *
   * @param eventId      the event type for which to perform the action, must be
   *                     {@link MouseEvent#MOUSE_PRESSED}, {@link MouseEvent#MOUSE_RELEASED} or
   *                     {@link MouseEvent#MOUSE_CLICKED}
   * @param button       when this mouse button for which the action is performed , must be
   *                     {@link MouseEvent#BUTTON1}, {@link MouseEvent#BUTTON2} or {@link MouseEvent#BUTTON3}
   * @param keyMask      the keys that must be pressed for the action to be performed, must be
   *                     0 or an or'ed combination of the key down masks found in {@link InputEvent}.
   * @param action       the action to perform
   * @param consumeEvent if true the event is consumed when the action is performed
   */
  public DockingWindowActionMouseButtonListener(int eventId,
                                                int button,
                                                int keyMask,
                                                DockingWindowAction action,
                                                boolean consumeEvent) {
    this.eventId = eventId;
    this.button = button;
    this.keyMask = keyMask;
    this.action = action;
    this.consumeEvent = consumeEvent;
  }

  public void mouseButtonEvent(MouseEvent event) {
    if (event.isConsumed())
      return;

    int m = event.getModifiersEx() & MODIFIER_MASK;

    if (event.getButton() == MouseEvent.BUTTON2)
      m &= ~MouseEvent.ALT_DOWN_MASK;

    if (event.getButton() == MouseEvent.BUTTON3)
      m &= ~MouseEvent.META_DOWN_MASK;

    if (event.getID() == eventId && event.getButton() == button && m == keyMask) {
      DockingWindow window = (DockingWindow) event.getSource();
      action.perform(window);

      if (consumeEvent)
        event.consume();
    }
  }

}
