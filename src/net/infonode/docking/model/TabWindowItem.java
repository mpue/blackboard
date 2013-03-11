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


// $Id: TabWindowItem.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.docking.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.internal.WriteContext;
import net.infonode.docking.properties.TabWindowProperties;
import net.infonode.properties.propertymap.PropertyMap;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class TabWindowItem extends AbstractTabWindowItem {
  public static final TabWindowProperties emptyProperties = new TabWindowProperties();

  private TabWindowProperties tabWindowProperties;
  private TabWindowProperties parentProperties = emptyProperties;

  public TabWindowItem() {
    tabWindowProperties = new TabWindowProperties(emptyProperties);
  }

  public TabWindowItem(TabWindowItem windowItem) {
    super(windowItem);
    tabWindowProperties = new TabWindowProperties(windowItem.getTabWindowProperties().getMap().copy(true, true));
    tabWindowProperties.getMap().replaceSuperMap(windowItem.getParentTabWindowProperties().getMap(),
                                                 emptyProperties.getMap());
  }

  protected DockingWindow createWindow(ViewReader viewReader, ArrayList childWindows) {
    return childWindows.size() == 0 ? null :
           viewReader.createTabWindow((DockingWindow[]) childWindows.toArray(new DockingWindow[childWindows.size()]),
                                      this);
  }

  public TabWindowProperties getTabWindowProperties() {
    return tabWindowProperties;
  }

  public void setTabWindowProperties(TabWindowProperties tabWindowProperties) {
    this.tabWindowProperties = tabWindowProperties;
  }

  public TabWindowProperties getParentTabWindowProperties() {
    return parentProperties;
  }

  public void setParentTabWindowProperties(TabWindowProperties parentProperties) {
    tabWindowProperties.getMap().replaceSuperMap(this.parentProperties.getMap(), parentProperties.getMap());
    this.parentProperties = parentProperties;
  }

  public WindowItem copy() {
    return new TabWindowItem(this);
  }

  public void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
    out.writeInt(WindowItemDecoder.TAB);
    super.write(out, context, viewWriter);
  }

  protected PropertyMap getPropertyObject() {
    return getTabWindowProperties().getMap();
  }

  public void clearWindows() {
    // Do nothing
  }

  public String toString() {
    return "TabWindow: " + super.toString();
  }

}
