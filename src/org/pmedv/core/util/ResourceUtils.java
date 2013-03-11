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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ByteArrayResource;

/**
 * Static utility class for handling resources
 * 
 * @author Matthias Pueski
 *
 */
public class ResourceUtils {
	
	
	/**
	 * Writes a given <code>InputStream</code> to a file.
	 * 
	 * @param is
	 * @param out
	 */
	@SuppressWarnings("unused")
	public static void writeStreamToFile(InputStream is, File out) {
		
	    byte[] b;

	    try {
	    	
	    	FileOutputStream fos = new FileOutputStream(out);
	    	
			b = new byte[is.available()];

			for (int n;(n = is.read(b)) != -1;) {			    	
		    	fos.write(b);			        
		    }
			
			fos.close();
			
		} 
	    catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Creates a {@link ByteArrayResource} from an input stream
	 * 
	 * @param is  the stream to read from
	 * @return	  the resource
	 */
	@SuppressWarnings("unused")
	public static ByteArrayResource createResourceFromStream(InputStream is) {
						
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
	    byte[] b;

	    try {
			b = new byte[is.available()];

			for (int n;(n = is.read(b)) != -1;) {			    	
		    	bos.write(b);			        
		    }
			
		} 
	    catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ByteArrayResource(bos.toByteArray());
		
	}
	
	/**
	 * Configures a factory from a properties file read from an input stream
	 * 
	 * @param is       the stream to read the properties from 
	 * @param factory  the factory to configure
	 */
	public static Properties configureFactoryFromInputStream(InputStream is, XmlBeanFactory factory ) {
		
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		
		Properties p = new Properties();

		try {
			p.load(is);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		cfg.setProperties(p);
		cfg.postProcessBeanFactory(factory);
		
		return p;
	}
	
    /**
     * Create an ImageIcon from the image at the specified path
     * 
     * @param path The path of the image
     * 
     * @return The image icon, or null if the image doesn't exist
     */
    public static ImageIcon createImageIcon(String path)
    {
    	
        URL u = Thread.currentThread().getContextClassLoader().getResource(path);
    	
    	// TODO : Check if that works with webstart
    	
        // URL u = ClassLoader.getSystemResource(realPath);//UIUtils.class.getResource(path);
        
        if(u == null)
            return null;
        return new ImageIcon(u);
    }
    
    /**
     * Prints a stackstrace into a string
     * 
     * @param t The throwable to print the trace for
     * 
     * @return a string containing the stacktrace
     */
    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

}
