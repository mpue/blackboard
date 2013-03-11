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
package org.pmedv.blackboard.test;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.pmedv.blackboard.components.Part;


public class WritePartTest {

	private static final String output = "D:\\devel\\j2ee_workspace\\BlackBoard\\resources\\parts\\transistor.xml";
	
	
	public static void main(String[] args) throws Exception {

		Part p = new Part();
		
		p.setName("Transistor");
		p.setDescription("A transistor");
		p.setPackageType("T0-220");
		p.setHeight(48);
		p.setWidth(48);
		p.setImageName("transistor.png");
		
		Marshaller m = (Marshaller)JAXBContext.newInstance(Part.class).createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		m.marshal(p, new File(output));
		
		
		
	}
	
}
