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


// $Id: AbstractConstChangeNotifyMap.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.util.collection.notifymap;

import net.infonode.util.ValueChange;
import net.infonode.util.collection.map.SingleValueMap;
import net.infonode.util.collection.map.base.ConstMap;
import net.infonode.util.signal.Signal;
import net.infonode.util.signal.SignalHook;
import net.infonode.util.signal.SignalListener;

abstract public class AbstractConstChangeNotifyMap implements ConstChangeNotifyMap {
  private Signal changeSignal = new Signal() {
    protected void firstListenerAdded() {
      AbstractConstChangeNotifyMap.this.firstListenerAdded();
    }

    protected void lastListenerRemoved() {
      AbstractConstChangeNotifyMap.this.lastListenerRemoved();
    }

    protected synchronized void removeListener(int index) {
      super.removeListener(index);
      listenerRemoved();
    }

    public synchronized void addListener(SignalListener listener) {
      super.addListener(listener);
      listenerAdded();
    }
  };

  protected void firstListenerAdded() {
  }

  protected void lastListenerRemoved() {
  }

  protected void listenerRemoved() {
  }

  protected void listenerAdded() {
  }

  public SignalHook getChangeSignal() {
    return changeSignal.getHook();
  }

  protected Signal getChangeSignalInternal() {
    return changeSignal;
  }

  protected void fireEntryRemoved(Object key, Object value) {
    fireEntryChanged(key, value, null);
  }

  protected void fireEntryChanged(Object key, Object oldValue, Object newValue) {
    fireEntriesChanged(new SingleValueMap(key, new ValueChange(oldValue, newValue)));
  }

  protected void fireEntriesChanged(ConstMap changes) {
    if (!changes.isEmpty())
      changeSignal.emit(changes);
  }

}
