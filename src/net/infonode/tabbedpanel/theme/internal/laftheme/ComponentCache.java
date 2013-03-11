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


// $Id: ComponentCache.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.tabbedpanel.theme.internal.laftheme;

import java.awt.Component;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import javax.swing.JComponent;

public class ComponentCache {
  private ArrayList cache = new ArrayList(10);

  private int index = 0;

  ComponentCache() {
    for (int i = 0; i < 1; i++)
      cache.add(new SoftReference(createComponent()));
  }

  void reset() {
    index = 0;
  }

  Component getComponent() {
    JComponent c = null;

    if (index == cache.size()) {
      c = createComponent();
      cache.add(new SoftReference(c));
    }
    else {
      c = (JComponent) ((SoftReference) cache.get(index)).get();
      if (c == null) {
        cache.remove(index);
        c = createComponent();
        cache.add(new SoftReference(c));
      }
    }

    index++;
    c.setOpaque(false);

    return c;
  }

  private JComponent createComponent() {
    return new JComponent() {
    };
  }
}
