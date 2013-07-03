/**

	BlackBoard BreadBoard Editor
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2012 Matthias Pueski
	
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
package org.pmedv.blackboard.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.FileUtils;

/**
 * <p>
 * The <code>PartFactory</code> is responsible for generating {@link Part}
 * objects to be placed onto the {@link BoardEditor}. The parts are located
 * inside the folder resources/parts as xml files. Each part has an according
 * image. The images are placed in the directory resources/parts/images
 * </p>
 * 
 * @author Matthias Pueski
 * 
 */
public final class PartFactory {

	private static final Log log = LogFactory.getLog(PartFactory.class);

	private final ArrayList<Part> availableParts = new ArrayList<Part>();
	private final ArrayList<String> partNames = new ArrayList<String>();

	private static final HashMap<String, Part> parts = new HashMap<String, Part>();

	private boolean loaded = false;

	private Unmarshaller partUnmarshaller;
	private Marshaller partMarshaller;

	public void init() {
		try {
			partUnmarshaller = JAXBContext.newInstance(Part.class).createUnmarshaller();
		}
		catch (JAXBException e) {
			throw new RuntimeException("Unable to create unmarshaller for part.");
		}
		try {
			partMarshaller = JAXBContext.newInstance(Part.class).createMarshaller();
			partMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		catch (JAXBException e1) {
			throw new RuntimeException("Unable to create marshaller for part.");
		}
		try {
			getAvailableParts();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a list of all available parts found inside the parts directory
	 * 
	 * @return a list of {@link Part} objects.
	 */
	public ArrayList<Part> getAvailableParts() throws Exception {
		if (loaded)
			return availableParts;
		for (String partName : getAvailablePartFiles()) {
			addPart(partName);
		}
		loaded = true;
		return availableParts;
	}

	/**
	 * Adds a new part based on a given name to the {@link PartFactory}
	 * 
	 * @param partName the filename of the part
	 * @return the newly added part
	 * @throws Exception
	 */
	public Part addPart(String partName) throws Exception {
		Part p = createPart(partName);
		availableParts.add(p);
		partNames.add(p.getName());
		parts.put(p.getName(), p);
		parts.put(p.getFilename(), p);
		return p;
	}

	/**
	 * Gets a list of all xml-part files located inside the parts directory
	 * 
	 * @return
	 */
	private ArrayList<String> getAvailablePartFiles() {
		ArrayList<String> partFiles = new ArrayList<String>();
		File workDir = new File(System.getProperty("user.home"), "." + AppContext.getName());
		log.info("Working directory " + workDir.getAbsolutePath());
		File partDir = new File(workDir + "/parts/");
		File[] files = partDir.listFiles();
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith("xml") || files[i].getName().endsWith("XML")) // sorry!
					partFiles.add(files[i].getName());
			}
		Collections.sort(partFiles);
		log.info("Found " + partFiles.size() + " parts.");
		return partFiles;
	}

	/**
	 * Creates a part based on its name, which obviously is the filename
	 * 
	 * @param name the name to create the part for
	 * 
	 * @return the ready to use {@link Part} object.
	 */
	public Part createPart(String name) throws Exception {
		
		if (name == null)
			return null;

		File workDir = new File(System.getProperty("user.home") + "/." + AppContext.getName());

		FileInputStream fis = new FileInputStream(System.getProperty("user.home") + "/." + AppContext.getName()
				+ "/parts/" + name);

		Part p = null;
		
		try {
			p = (Part) partUnmarshaller.unmarshal(fis);
		}
		catch (Exception e1) {
			throw new IllegalArgumentException("Unable to create part from file "+name);
		}

		String xmlContent = FileUtils.readFile(new File(System.getProperty("user.home"), "." + AppContext.getName()
				+ "/parts/" + name));
		p.setXmlContent(xmlContent);

		fis.close();

		boolean imageOK = false;

		try {
			fis = new FileInputStream(workDir + "/parts/images/" + p.getImageName());
			imageOK = true;
		}
		catch (Exception e) {
			fis = new FileInputStream(workDir + "/parts/images/noresource_64x64.png");
			imageOK = false;
		}

		BufferedImage image = ImageIO.read(fis);
		fis.close();
		p.setImage(image);
		p.setFilename(name);
		p.setOldXLoc(p.getXLoc());
		p.setOldYLoc(p.getYLoc());
		p.setLayer(BoardEditorModel.PART_LAYER);

		// image has failed loading, we display the noresource image and give it
		// a default size
		if (!imageOK) {
			p.setHeight(64);
			p.setWidth(64);
		}

		p.setOldWidth(p.getWidth());
		p.setOldHeight(p.getOldHeight());

		return p;

	}

	/**
	 * @return the partnames
	 */
	public ArrayList<String> getPartnames() {
		return partNames;
	}

	public Part getPart(String name) {
		return parts.get(name);
	}

	/**
	 * @return the partUnmarshaller
	 */
	public Unmarshaller getUnmarshaller() {
		return partUnmarshaller;
	}

	/**
	 * @return the partMarshaller
	 */
	public Marshaller getMarshaller() {
		return partMarshaller;
	}

}
