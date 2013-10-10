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
import java.io.InputStreamReader;
import java.util.Properties;

public class CheckEnv {
	
	public static Properties getEnvVars() throws Throwable {
		
		Process p = null;
		Properties envVars = new Properties();
		Runtime r = Runtime.getRuntime();
		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("windows 9") > -1) {
			p = r.exec("command.com /c set");
		} 
		else if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows 2000") > -1)	|| (OS.indexOf("windows xp") > -1)) {
			p = r.exec("cmd.exe /c set");
		} 
		else {
			p = r.exec("env");
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		String line;
		
		while ((line = br.readLine()) != null) {
			
			int idx = line.indexOf('=');
			String key = line.substring(0, idx);
			String value = line.substring(idx + 1);
			envVars.setProperty(key, value);
			
		}
		
		br.close();
		
		return envVars;
	}

	public static String getPlatform() {
	
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.indexOf("windows") > -1) {
			return "windows";
		}
		else if (os.indexOf("linux") > -1) {
			return "linux";
		}
		
		else return "mac";
		
	}
	
}
