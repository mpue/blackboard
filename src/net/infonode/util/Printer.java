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


// $Id: Printer.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.util;

import java.io.PrintStream;

public class Printer {
  private PrintStream out;
  private String indent = "";
  private boolean newLine = true;

  public Printer() {
    this(System.out);
  }

  public Printer(PrintStream out) {
    this.out = out;
  }

  public void beginSection() {
    indent += "  ";
  }

  public void beginSection(String title) {
    println(title);
    indent += "  ";
  }

  public void endSection() {
    indent = indent.substring(2);
  }

  public void print(String str) {
    if (newLine)
      out.print(indent);

    out.print(str);
    newLine = false;
  }

  public void println(String str) {
    if (newLine)
      out.print(indent);

    out.println(str);
    newLine = true;
  }

  public void println() {
    if (newLine)
      out.print(indent);

    out.println();
    newLine = true;
  }
}
