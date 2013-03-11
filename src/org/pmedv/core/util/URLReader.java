/**

	BlackBoard BreadBoard Designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {
	/**
	 * Reads the InputStream from an URL and returs the content as string 
	 * 
	 * @param url the URL to get data from
	 * 
	 * @return the http response text from this URL
	 */
	public static String getContent(String urlString) throws IllegalArgumentException {
		
		if (urlString == null)
			throw new IllegalArgumentException("Url must not be null.");
		
		try {
			URL url = new URL(urlString);
			
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();

			InputStreamReader isr = new InputStreamReader(in);
			Reader inReader = new BufferedReader(isr);

			StringBuffer buf = new StringBuffer();
			int ch;
			while ((ch = inReader.read()) > -1) {
				buf.append((char)ch);
			}
			inReader.close();
			
			return buf.toString();
		} 
		catch (IOException e) {
			return "unable to retrieve content from "+urlString;
		}
	}  	

}
