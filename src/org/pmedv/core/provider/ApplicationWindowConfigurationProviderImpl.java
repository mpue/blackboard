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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.beans.ApplicationWindowConfiguration;
import org.pmedv.core.context.AppContext;

public class ApplicationWindowConfigurationProviderImpl implements ApplicationWindowConfigurationProvider {

	private static final Log log = LogFactory.getLog(ApplicationWindowConfigurationProviderImpl.class);

	private ApplicationWindowConfiguration config;

	public ApplicationWindowConfiguration getConfig() {
		return config;
	}

	public ApplicationWindowConfigurationProviderImpl() {

		try {
			
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			
			Properties p = new Properties();
			p.load(is);
			
			String inputDir = System.getProperty("user.home") + "/."+p.getProperty("app.name")+"/";
			String inputFile = "appWindowConfig.xml";	
			
			JAXBContext c = JAXBContext.newInstance(ApplicationWindowConfiguration.class);
			Unmarshaller u = c.createUnmarshaller();

			ApplicationWindowConfiguration appWindowConfig = (ApplicationWindowConfiguration) u
					.unmarshal(new FileInputStream(new File(inputDir+inputFile)));

			config = appWindowConfig;
		}
		catch (JAXBException e) {
			e.printStackTrace();
			log.error("could not deserialize application window configuration.");
			throw new RuntimeException("could not deserialize application window configuration.");
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("could not load application window configuration, creating new.");
			
			ApplicationWindowConfiguration c = new ApplicationWindowConfiguration();
			config = c;
			
		}
	}

	@Override
	public void storeConfig() {

		Marshaller m;
		
		try {			
			String outputDir = System.getProperty("user.home") + "/."+AppContext.getName()+"/";
			String outputFileName = "appWindowConfig.xml";			
			m = JAXBContext.newInstance(ApplicationWindowConfiguration.class).createMarshaller();			
			File output = new File(outputDir+outputFileName);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(getConfig(), output);
		} 
		catch (JAXBException e) {
			log.error("Could not write application window configuration.");
		}
		
	}

}
