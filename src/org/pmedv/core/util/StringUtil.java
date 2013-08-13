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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.html.Tags;

/**
 * A static utility class for special string operations.
 * 
 * @author Matthias Pueski
 *
 */
public class StringUtil {

	private static final Log log = LogFactory.getLog(StringUtil.class);
	
	/**
	 * Replaces a string sequence inside a given string
	 * 
	 * @param s       The string to search in
	 * @param search  The string to search for
	 * @param replace The replacement string
	 * 
	 * @return a new string with the replaced subsequences
	 */
	public static String replace(String s, String search, String replace) {
		
		log.info("Replacing "+search+" with "+replace);
		
		try {
			StringBuffer s2 = new StringBuffer();
			int i = 0, j = 0;
			int len = search.length();
	
			while (j > -1) {
				j = s.indexOf(search, i);
	
				if (j > -1) {
					s2.append(s.substring(i, j));
					s2.append(replace);
					i = j + len;
				}
			}
			s2.append(s.substring(i, s.length()));
	
			return s2.toString();
		}
		catch (NullPointerException n) {
			return "";
		}
		
	}

	/**
	 * Converts all expressions starting with &quot;http://&quot; to 
	 * real links 
	 * 
	 * @param myContentText the text to search for links
	 * 
	 * @return the converted text
	 */
	public static String insertUrls(String myContentText) {

		int beginIndex = myContentText.indexOf("http://");

		String Output = "";

		do {

			if (beginIndex > -1) {
				int endIndex = myContentText.indexOf(" ", beginIndex);

				Output += myContentText.substring(0, beginIndex);
				String myURL = myContentText.substring(beginIndex, endIndex);
				Output += "<a href=\"" + myURL + "\" target=\"blank\">" + myURL
						+ "</a>";

				myContentText = myContentText.substring(endIndex);
				beginIndex = myContentText.indexOf("http://");

			}
		} while (beginIndex > -1);

		Output += myContentText;

		return Output;

	}
	
	/**
	 * <p>
	 * Converts a straight text to a text with line breaks depending
	 * on the length of a line.
	 * </p>
	 * <p>
	 * If the character at <b>lineWidth</b> is not a whitespace, 
	 * the function steps back inside the character array until a
	 * whitespace is found.
	 * </p>
	 * <p>
	 * If the param html is set to true, the output string is embedded between
	 * &lt;html&gt;&lt;/html&gt; tags and the line breaks are printed as &lt;br&gt;
	 * instead of \n. This is for example useful for tooltip and html panel display 
	 * of the text.
	 * <p>
	 * 
	 * @param originalText the text to insert line breaks into
	 * @param lineWidth    the maximum length of the line (the expected position of the linebreak)
	 * @param html         force html output
	 * 
	 * @return             the converted String containing line breaks
	 */
	public static String insertLineBreaks(String originalText, int lineWidth, boolean html) {
		
		if (originalText == null) return "";
		if (originalText.length() <= lineWidth) return originalText;
		
		StringBuffer text = new StringBuffer(originalText);
		
		if (html) {
			text.insert(0, Tags.HTML_OPEN);
		}
		
		for (int i = 1; i < text.length(); i++) {
			
			if (i%lineWidth == 0) {
				
				int j = i;
				
				while (text.charAt(j--) != ' ' && j > 1);
				
				// TODO : Remove hard coded line break and do some platform checking
				
				if (html)
					text.insert(j+1, Tags.NEWLINE);
				else
					text.setCharAt(j+1, '\n');
				
			}
			
		}
		
		if (html) {
			text.append(Tags.HTML_CLOSE);
		}
		
		return text.toString();
		
	}

	/**
	 * Shortens a text to a specific length and appends "..." at 
	 * the end of the text. This method can be used to shorten a long text for 
	 * tooltip displaying for example.
	 * 
	 * @param originalText the original text
	 * @param maxlength    the length of the shortened text
	 * 
	 * @return the shortened text
	 */
	public static String shortenText(String originalText, int maxlength) {
		
		if (originalText == null) return "";		
		if (originalText.length() <= maxlength) return originalText;
		
		int j = maxlength;
		
		while (originalText.charAt(j--) != ' ' );
		
		StringBuffer text = new StringBuffer();
		text.append(originalText.substring(0,j+1));
		text.append("...");
		
		return text.toString();
		
	}
	
	/**
	 * Slurps an inputStream to a String
	 * 
	 * @param in  
	 * @return
	 * @throws IOException
	 */
	public static String slurp (InputStream in) throws IOException, IllegalArgumentException {
		
		if (in == null)
			throw new IllegalArgumentException("Inputstream must not be null.");
		
	    StringBuffer out = new StringBuffer();
	    
	    byte[] b = new byte[4096];
	    
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    
	    return out.toString();
	    
	}
	
}
