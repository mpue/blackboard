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


// $Id: Int4.java,v 1.2 2011-08-26 15:10:48 mpue Exp $
package net.infonode.util.math;

/**
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
final public class Int4 {
  private int a;
  private int b;
  private int c;
  private int d;

  public Int4() {
    this(0, 0, 0, 0);
  }

  public Int4(Int4 i) {
    this(i.a, i.b, i.c, i.d);
  }

  public Int4(int a, int b, int c, int d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

  public int getA() {
    return a;
  }

  public void setA(int a) {
    this.a = a;
  }

  public int getB() {
    return b;
  }

  public void setB(int b) {
    this.b = b;
  }

  public int getC() {
    return c;
  }

  public void setC(int c) {
    this.c = c;
  }

  public int getD() {
    return d;
  }

  public void setD(int d) {
    this.d = d;
  }

  public Int4 set(Int4 i) {
    a = i.a;
    b = i.b;
    c = i.c;
    d = i.d;
    return this;
  }

  public Int4 add(Int4 i) {
    a += i.a;
    b += i.b;
    c += i.c;
    d += i.d;
    return this;
  }

  public Int4 sub(Int4 i) {
    a -= i.a;
    b -= i.b;
    c -= i.c;
    d -= i.d;
    return this;
  }

  public Int4 div(long value) {
    a /= value;
    b /= value;
    c /= value;
    d /= value;
    return this;
  }

  public Int4 mul(long value) {
    a *= value;
    b *= value;
    c *= value;
    d *= value;
    return this;
  }

  public String toString() {
    return a + ", " + b + ", " + c + ", " + d;
  }

}
