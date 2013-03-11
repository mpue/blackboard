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
package org.pmedv.core.provider;

import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.beans.ApplicationPerspective;
import org.pmedv.core.beans.ApplicationPerspectiveList;

public class ApplicationPerspectiveProviderImpl implements ApplicationPerspectiveProvider {
	
	private static final Log log = LogFactory.getLog(ApplicationPerspectiveProvider.class);
	
	private ArrayList<ApplicationPerspective> perspectives;
	
	public ApplicationPerspectiveProviderImpl() {
		
		ApplicationPerspectiveList list = null;
		
		JAXBContext c;
		
		try {
			c = JAXBContext.newInstance(ApplicationPerspectiveList.class);
			Unmarshaller u = c.createUnmarshaller();
			
			list  = (ApplicationPerspectiveList)u.unmarshal(getClass().getClassLoader().getResourceAsStream("perspectives.xml"));
			
		} 
		catch (JAXBException e) {			
			log.error("could not deserialize perspectives.");
			throw new RuntimeException("could not load perspectives.");									
		} 
		catch (Exception e) {
			log.error("could not load perspectives.");
			throw new RuntimeException("could not load perspectives.");											
		}

		this.perspectives = list.getPerspectives();
		
	}

	@Override
	public ArrayList<ApplicationPerspective> getPerspectives() {
		return perspectives;
	}
	
	


}
