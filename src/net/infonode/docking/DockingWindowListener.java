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


// $Id: DockingWindowListener.java,v 1.2 2011-08-26 15:10:41 mpue Exp $
package net.infonode.docking;

/**
 * <p>
 * A listener for {@link DockingWindow} events. All events are propagated upwards in the window tree, so
 * a listener will receive events for the window that it was added to and all descendants of that window.
 * </p>
 *
 * <p>
 * Note: New methods might be added to this interface in the future. To ensure future compatibility inherit from
 * {@link DockingWindowAdapter} instead of directly implementing this interface.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 * @since IDW 1.1.0
 */
public interface DockingWindowListener {
  /**
   * Called when a window has been added.
   *
   * @param addedToWindow the parent window that the window was added to
   * @param addedWindow   the window that was added
   * @since IDW 1.3.0
   */
  void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow);

  /**
   * Called when a window has been removed.
   *
   * @param removedFromWindow the parent window that the window was removed from
   * @param removedWindow     the window that was removed
   * @since IDW 1.3.0
   */
  void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow);

  /**
   * Called when a window is shown, for example when it is selected in a TabWindow.
   *
   * @param window the window that was shown
   * @since IDW 1.3.0
   */
  void windowShown(DockingWindow window);

  /**
   * Called when a window is hidden, for example when it is deselected in a TabWindow.
   *
   * @param window the window that was hidden
   * @since IDW 1.3.0
   */
  void windowHidden(DockingWindow window);

  /**
   * Called when the focus moves from one view to another view.
   *
   * @param previouslyFocusedView the view that had focus before the focus moved, null means no view had focus
   * @param focusedView           the view that got focus, null means no view got focus
   * @since IDW 1.3.0
   */
  void viewFocusChanged(View previouslyFocusedView, View focusedView);

  /**
   * <p>
   * Called before the window that this listener is added to, or a child window of that window, is closed.
   * </p>
   *
   * <p>
   * Note that this method is only called when {@link DockingWindow#closeWithAbort()} is called explicitly, not
   * when a window is implicitly closed as a result of another method call. Throwing an {@link OperationAbortedException}
   * will cause the close operation to be aborted.
   * </p>
   *
   * @param window the window that is closing
   * @throws OperationAbortedException if this exception is thrown the close operation will be aborted
   */
  void windowClosing(DockingWindow window) throws OperationAbortedException;

  /**
   * <p>
   * Called after the window that this listener is added to, or a child window of that window, has been closed.
   * </p>
   *
   * <p>
   * Note that this method is only called when {@link DockingWindow#close()} or {@link DockingWindow#closeWithAbort()}
   * is called explicitly, not when a window is implicitly closed as a result of another method call.
   * </p>
   *
   * @param window the window that has been closed
   */
  void windowClosed(DockingWindow window);

  /**
   * <p>
   * Called before the window that this listener is added to, or a child window of that window, is undocked.
   * </p>
   *
   * <p>
   * Note that this method is only called when {@link DockingWindow#undockWithAbort(java.awt.Point)} is called explicitly, not
   * when a window is implicitly undocked as a result of another method call. Throwing an {@link OperationAbortedException}
   * will cause the undock operation to be aborted.
   * </p>
   *
   * @param window the window that is undocking
   * @throws OperationAbortedException if this exception is thrown the undock operation will be aborted
   * @since IDW 1.4.0
   */
  void windowUndocking(DockingWindow window) throws OperationAbortedException;

  /**
   * <p>
   * Called after the window that this listener is added to, or a child window of that window, has been undocked.
   * </p>
   *
   * <p>
   * This method is called when a window is undocked using {@link DockingWindow#undock(java.awt.Point)},
   * {@link DockingWindow#undockWithAbort(java.awt.Point)} or is added to a window that is undocked.
   * </p>
   *
   * @param window the window that has been undocked
   * @since IDW 1.4.0
   */
  void windowUndocked(DockingWindow window);

  /**
   * <p>
   * Called before the window that this listener is added to, or a child window of that window, is docked.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> that this method is only called when {@link DockingWindow#dockWithAbort()} is called explicitly, not
   * when a window is implicitly docked as a result of another method call. Throwing an {@link OperationAbortedException}
   * will cause the dock operation to be aborted.
   * </p>
   *
   * @param window the window that is docking
   * @throws OperationAbortedException if this exception is thrown the dock operation will be aborted i.e. no views in the
   *                                   window will be docked
   * @since IDW 1.4.0
   */
  void windowDocking(DockingWindow window) throws OperationAbortedException;

  /**
   * <p>
   * Called when a view has been docked in the root window.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> If a window containing more than one view was docked then this method will be called for each
   * view after all views have been docked.
   * </p>
   *
   * @param window the view that has been docked
   * @since IDW 1.4.0
   */
  void windowDocked(DockingWindow window);

  /**
   * <p>
   * Called before the window that this listener is added to, or a child window of that window, is minimized.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> that this method is only called when {@link DockingWindow#minimizeWithAbort()} is called
   * explicitly, not when a window is implicitly docked as a result of another method call. Throwing an
   * {@link OperationAbortedException} will cause the minimize operation to be aborted.
   * </p>
   *
   * @param window the window that is minimizing
   * @throws OperationAbortedException if this exception is thrown the minimize operation will be aborted
   * @since IDW 1.4.0
   */
  void windowMinimizing(DockingWindow window) throws OperationAbortedException;

  /**
   * Called after the window that this listener is added to, or a child window of that window, has been minimized.
   *
   * @param window the window that has been minimized
   * @since IDW 1.4.0
   */
  void windowMinimized(DockingWindow window);

  /**
   * <p>
   * Called before the window that this listener is added to, or a child window of that window, is maximized.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> that this method is only called when {@link DockingWindow#maximizeWithAbort()} is called
   * explicitly, not when a window is implicitly docked as a result of another method call. Throwing an
   * {@link OperationAbortedException} will cause the maximize operation to be aborted.
   * </p>
   *
   * @param window the window that is maximizing
   * @throws OperationAbortedException if this exception is thrown the maximize operation will be aborted
   * @since IDW 1.4.0
   */
  void windowMaximizing(DockingWindow window) throws OperationAbortedException;

  /**
   * Called after the window that this listener is added to, or a child window of that window, has been maximized.
   *
   * @param window the window that has been maximized
   * @since IDW 1.4.0
   */
  void windowMaximized(DockingWindow window);

  /**
   * <p>
   * Called before the window that this listener is added to, or a child window of that window, is restored.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> that this method is only called when {@link DockingWindow#restoreWithAbort()} is called
   * explicitly, not when a window is implicitly restored as a result of another method call. Throwing an
   * {@link OperationAbortedException} will cause the restore operation to be aborted.
   * </p>
   *
   * @param window the window that is restoring
   * @throws OperationAbortedException if this exception is thrown the restore operation will be aborted
   * @since IDW 1.4.0
   */
  void windowRestoring(DockingWindow window) throws OperationAbortedException;

  /**
   * <p>
   * Called after the window that this listener is added to, or a child window of that window, has been restored.
   * </p>
   *
   * <p>
   * Note that this method is only called when {@link DockingWindow#restore()}
   * is called explicitly, not when a window is implicitly restored as a result of another method call.
   * </p>
   *
   * @param window the window that has been restored
   * @since IDW 1.4.0
   */
  void windowRestored(DockingWindow window);
}
