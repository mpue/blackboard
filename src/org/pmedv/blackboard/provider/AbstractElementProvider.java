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
package org.pmedv.blackboard.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.context.AppContext;

/**
 * <p>
 * The <code>AbstractElementProvider</code> provides access to a serializable type {@link BaseElement}.
 * </p>
 * <p>
 * It allows to load and store the desired element class to xml via JAXB.
 * </p>
 * 
 * @author Matthias Pueski 
 *
 */
public class AbstractElementProvider<T extends BaseElement> {

	private static final Log log = LogFactory.getLog(AbstractElementProvider.class);

	private final ArrayList<T> elements;	
	private File elementsXmlDir;
	private String elementsDirName;
	
	private final Unmarshaller unmarshaller;
	private final Marshaller marshaller;
	
	public AbstractElementProvider(Class clazz, String elementsBaseDir) {

		elements = new ArrayList<T>();
		
		try {
			marshaller = JAXBContext.newInstance(clazz).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		catch (JAXBException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to create marshaller for "+clazz);
		}

		try {
			unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
		}
		catch (JAXBException e) {
			throw new RuntimeException("Unable to create unmarshaller for "+clazz);
		}
		
		this.elementsDirName = elementsBaseDir;
		final File workDir = new File(".");
		log.info("Working directory " + workDir.getAbsolutePath());
		elementsXmlDir = new File(System.getProperty("user.home"), "."+AppContext.getName()+"/"+ elementsDirName+"/");
	}

	@SuppressWarnings("unchecked")
	public void loadElements() {
		
		elements.clear();

		File[] fileList = elementsXmlDir.listFiles();
		
		for (int i = 0; i < fileList.length;i++) {
			
			if (fileList[i].getName().endsWith(".xml")) {
				
				T t = null;
				
				try {
					t = (T) unmarshaller.unmarshal(new FileInputStream(fileList[i]));				
					t.setFilename(fileList[i].getName());
				}
				catch (FileNotFoundException e) {
					log.error("File not found " + fileList[i]);
					continue;
				}
				catch (JAXBException e) {
					log.error("unable to unmarshall symbol from "+fileList[i]);
					e.printStackTrace();
					continue;
				}
				elements.add(t);
			}
			
		}

	}

	public void addElement(T t) throws Exception {
		if (!elements.contains(t)) {
			elements.add(t);
			storeElement(t);
		}
	}

	public void removeElement(T t) throws Exception {
		elements.remove(t);
		File elementFile = new File(elementsXmlDir,t.getFilename());
		if (!elementFile.delete()) {
			throw new IllegalStateException("Could not remove element "+t.getFilename());
		}
	}

	public void storeElement(T t) throws Exception {
		if (t.getFilename() == null) {
			t.setFilename(t.getName()+".xml");
		}		
		FileOutputStream fos = new FileOutputStream(new File(elementsXmlDir,t.getFilename()));
		
		marshaller.marshal(t, fos);
		fos.flush();
		fos.close();
	}
	
	public void storeElementList() {

		File workDir = new File(".");
		log.info("Working directory " + workDir.getAbsolutePath());


		for (T t : elements) {			
			try {
				marshaller.marshal(t, new FileOutputStream(new File(elementsXmlDir,t.getFilename())));
			}
			catch (FileNotFoundException e) {
				log.error("File not found " + t.getName()+".xml");
				continue;
			}
			catch (JAXBException e) {
				log.error("unable to marshall element to "+ t.getFilename());
				continue;
			}			
		}

	}

	public List<T> getElements() {
		return elements;
	}

	/**
	 * @return the unmarshaller
	 */
	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}

	/**
	 * @return the marshaller
	 */
	public Marshaller getMarshaller() {
		return marshaller;
	}
	
	
	
}
