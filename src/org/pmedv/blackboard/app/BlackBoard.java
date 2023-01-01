/**

	BlackBoard breadboard designer
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
package org.pmedv.blackboard.app;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.app.AbstractApplication;
import com.formdev.flatlaf.util.SystemInfo;
import org.pmedv.core.preferences.Preferences;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;


/**
 * This is the main BlackBoard application.
 * 
 * @author Matthias Pueski (14.06.2011)
 *
 */
public class BlackBoard extends AbstractApplication {

	public BlackBoard(String fileLocation) {
		super(fileLocation);
	}

	private static final Log log = LogFactory.getLog(BlackBoard.class);
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {

		// macOS
		if( SystemInfo.isMacOS ) {
			// enable screen menu bar
			// (moves menu bar from JFrame window to top of screen)
			System.setProperty( "apple.laf.useScreenMenuBar", "true" );

			// application name used in screen menu bar
			// (in first menu after the "apple" menu)
			System.setProperty( "apple.awt.application.name", "BlackBoard" );

			// appearance of window title bars
			// possible values:
			//   - "system": use current macOS appearance (light or dark)
			//   - "NSAppearanceNameAqua": use light appearance
			//   - "NSAppearanceNameDarkAqua": use dark appearance
			System.setProperty( "apple.awt.application.appearance", "system" );
		}

		String fileName = null;
		
		if (args.length == 1 && args[0] != null) {
			
			if (!args[0].startsWith("-")) {
				fileName = args[0];	
			}						
		}
		
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
		
		Properties p = new Properties();
		p.load(is);
		
		if (!p.getProperty("language").equalsIgnoreCase("default")){
			Locale.setDefault(new Locale(p.getProperty("language")));
		}
		
		log.info("Initalizing application");		
		BlackBoard app = new BlackBoard(fileName);
		
	}
	
	
}
