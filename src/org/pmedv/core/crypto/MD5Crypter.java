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
package org.pmedv.core.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Basic MD5 password encryption class
 * 
 * @author mpue
 *
 */

public class MD5Crypter {

	/**
	 * Creates a MD5-hash from a password String 
	 * 
	 * @param password
	 * @return password-hash as byte array
	 * 
	 */
	
	public static byte[] createMD5key (String password) {
		byte[] myPasswordArray = password.getBytes(); 
		
		byte[] result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");		
			md.update(myPasswordArray);
			 result = md.digest();
			
		}
		catch(NoSuchAlgorithmException no) {
			no.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Converts a byte array to an hex string
	 * @param b
	 * @return the string in hex format
	 */

	public static String toHexString(byte b) {
	    int value = (b & 0x7F) + (b < 0 ? 128 : 0);
	    String ret = (value < 16 ? "0" : "");
	    ret += Integer.toHexString(value).toUpperCase();
	    return ret;
	}	
	
	/**
	 * Creates an MD5-String from an byte array 
	 * 
	 * @param myPasswordArray
	 * @return
	 */
	
	public static String createMD5String (byte[] myPasswordArray) {
				
		byte[] result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");		
			md.update(myPasswordArray);
			result = md.digest();
			
		}
		catch(NoSuchAlgorithmException no) {
			no.printStackTrace();
		}
		
		String myKey = "";
		
		for (int i=0;i<result.length;i++) {
			myKey += toHexString(result[i]);
		}
		
		return myKey;
	}

	/**
	 * Checks equality of two MD5-keys
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	
	public static boolean checkMD5match(byte[] key1, byte[] key2) {
		if (key1.length != key2.length) return false;

		for (int i=0;i<key1.length;i++) {
			if (key1[i] != key2[i]) return false;
		}
 		
		return true;
		
	}

}
