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


//$Id: ReleaseInfo.java,v 1.2 2011-08-26 15:10:42 mpue Exp $
package net.infonode.util;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class that represents release information for a product
 *
 * @author $Author: mpue $
 * @version $Revision: 1.2 $
 */
public class ReleaseInfo implements Serializable {
  private static final long serialVersionUID = 1;

  private String productName;
  private String productVendor;
  private String license;
  private long buildTime;
  private ProductVersion productVersion;
  private URL homepage;

  /**
   * Constructs a release info object
   *
   * @param name      product name
   * @param vendor    vendor name
   * @param buildTime time of nuild in millis
   * @param version   product version
   * @param license   the product license
   * @param homepage  URL to the product homepage
   */
  public ReleaseInfo(String name,
                     String vendor,
                     long buildTime,
                     ProductVersion version,
                     String license,
                     String homepage) {
    this.productName = name;
    this.productVendor = vendor;
    this.buildTime = buildTime;
    this.productVersion = version;
    this.license = license;

    try {
      this.homepage = new URL(homepage);
    }
    catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets the product name
   *
   * @return Product name
   */
  public String getProductName() {
    return productName;
  }

  /**
   * Gets the product vendor
   *
   * @return Product vendor
   */
  public String getProductVendor() {
    return productVendor;
  }

  /**
   * Gets the product license
   *
   * @return Product license
   */
  public String getLicense() {
    return license;
  }

  /**
   * Gets the build time in millis
   *
   * @return Build time in millis
   */
  public long getBuildTime() {
    return buildTime;
  }

  /**
   * Gets the product version
   *
   * @return Product version
   */
  public ProductVersion getProductVersion() {
    return productVersion;
  }

  /**
   * Gets the URL for the product homepage.
   *
   * @return the URL for the product homepage
   */
  public URL getHomepage() {
    return homepage;
  }

}
