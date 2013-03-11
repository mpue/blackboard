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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils {

	private static final ObjectUtils INSTANCE = new ObjectUtils();
	
	public Object deepCopy(Object o) {

		try {
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(bos);
				oos.writeObject(o);
				oos.flush();
				ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
				return  ois.readObject();
			}
			finally {
				oos.close();
				ois.close();
			}
		}
		catch (ClassNotFoundException cnfe) {
			// Impossible, since both sides deal in the same loaded classes.
			return null;
		}
		catch (IOException ioe) {
			// This has to be "impossible", given that oos and ois wrap a *byte
			// array*.
			return null;
		}
	}

	public static ObjectUtils getInstance() {
		return INSTANCE;
	}
	
}
