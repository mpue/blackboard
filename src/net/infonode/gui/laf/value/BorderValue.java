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


// $Id: BorderValue.java,v 1.2 2011-08-26 15:10:46 mpue Exp $
package net.infonode.gui.laf.value;

import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class BorderValue {
  private BorderUIResource defaultBorder;
  private BorderUIResource border;

  public BorderValue() {
  }

  public BorderValue(Border defaultBorder) {
    this.defaultBorder = new BorderUIResource(defaultBorder);
  }

  public BorderUIResource getDefaultBorder() {
    return defaultBorder;
  }

  public void setDefaultBorder(Border defaultBorder) {
    setDefaultBorder(new BorderUIResource(defaultBorder));
  }

  public void setDefaultBorder(BorderUIResource defaultBorder) {
    this.defaultBorder = defaultBorder;
  }

  public BorderUIResource getBorder() {
    return border == null ? defaultBorder : border;
  }

  public void setBorder(BorderUIResource border) {
    this.border = border;
  }
}
