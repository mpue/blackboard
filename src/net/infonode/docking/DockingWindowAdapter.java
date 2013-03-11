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


// $Id: DockingWindowAdapter.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.docking;

/**
 * Adapter class which implements the {@link DockingWindowListener} methods with empty bodies.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.1.0
 */
public class DockingWindowAdapter implements DockingWindowListener {
  public void windowShown(DockingWindow window) {
  }

  public void windowHidden(DockingWindow window) {
  }

  public void viewFocusChanged(View previouslyFocusedView, View focusedView) {
  }

  public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow) {
  }

  public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow) {
  }

  public void windowClosing(DockingWindow window) throws OperationAbortedException {
  }

  public void windowClosed(DockingWindow window) {
  }

  public void windowUndocking(DockingWindow window) throws OperationAbortedException {
  }

  public void windowUndocked(DockingWindow window) {
  }

  public void windowDocking(DockingWindow window) throws OperationAbortedException {
  }

  public void windowDocked(DockingWindow window) {
  }

  public void windowMinimized(DockingWindow window) {
  }

  public void windowMaximized(DockingWindow window) {
  }

  public void windowRestored(DockingWindow window) {
  }

  public void windowMaximizing(DockingWindow window) throws OperationAbortedException {
  }

  public void windowMinimizing(DockingWindow window) throws OperationAbortedException {
  }

  public void windowRestoring(DockingWindow window) throws OperationAbortedException {
  }

}
