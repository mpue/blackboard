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
package org.pmedv.blackboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.beans.BoardBean;
import org.pmedv.blackboard.beans.BoxBean;
import org.pmedv.blackboard.beans.DiodeBean;
import org.pmedv.blackboard.beans.EllipseBean;
import org.pmedv.blackboard.beans.LayerBean;
import org.pmedv.blackboard.beans.LineBean;
import org.pmedv.blackboard.beans.PartBean;
import org.pmedv.blackboard.beans.ResistorBean;
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.beans.TextPartBean;
import org.pmedv.blackboard.components.Box;
import org.pmedv.blackboard.components.Diode;
import org.pmedv.blackboard.components.Ellipse;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Resistor;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.models.BoardEditorModel.BoardType;
import org.pmedv.blackboard.tools.PartFactory;
import org.pmedv.core.beans.RecentFileList;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.util.ErrorUtils;
import org.pmedv.core.util.ZipUtils;

/**
 * A convenience class containing static utility methods for editor
 * input/output.
 * 
 * @author Matthias Pueski
 * 
 */
public class IOUtils {
	
	private static final Log log = LogFactory.getLog(IOUtils.class);
	
	public static final int BOARD_DEFAULT_WIDTH = 1024;
	public static final int BOARD_DEFAULT_HEIGHT = 640;
	
	private static final Unmarshaller partUnmarshaller;
	private static final Marshaller partMarshaller;
	
	static {
		try {
			partUnmarshaller = (Unmarshaller) JAXBContext.newInstance(Part.class).createUnmarshaller();
		}
		catch (JAXBException e) {
			throw new RuntimeException("Unable to create marshaller for "+Part.class);
		}
		
		try {
			partMarshaller = (Marshaller) JAXBContext.newInstance(Part.class).createMarshaller();
		}
		catch (JAXBException e) {
			throw new RuntimeException("Unable to create unmarshaller for "+Part.class);
		}
	}

	public static File unpackBoard(File file) throws Exception {
		File tempDir = new File(System.getProperty("java.io.tmpdir") + "/blackboard/", file.getName());
		log.info("Temp directory is at : " + tempDir.getAbsolutePath());
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		ZipUtils.extractZipArchive(file.getAbsolutePath(), tempDir.getAbsolutePath());
		File tempPartDir = new File(tempDir, "parts");
		File tempImagesDir = new File(tempPartDir, "images");
		
		File workDir = new File(System.getProperty("user.home"),"."+AppContext.getName());
		log.info("Working directory " + workDir.getAbsolutePath());		
		File partDir = new File(workDir, "parts");
		File imageDir = new File(partDir, "images");
		
		if (tempPartDir.exists()) {
			for (int i = 0; i < tempPartDir.listFiles().length; i++) {
				File outputPart = new File(partDir, tempPartDir.listFiles()[i].getName());
				if (outputPart.getName().endsWith(".xml")) {
					try {
						if (!outputPart.exists()) {
							FileUtils.copyFile(tempPartDir.listFiles()[i], outputPart);
							Part p = (Part) partUnmarshaller.unmarshal(new FileInputStream(tempPartDir.listFiles()[i]));
							FileUtils.copyFile(new File(tempImagesDir, p.getImageName()), new File(imageDir, p.getImageName()));
							AppContext.getContext().getBean(PartFactory.class).addPart(outputPart.getName());
						}
						else {
							// houston we got a problem, there's already a file with the same name, it might be the same
							// part or not. No matter what, we cannot be sure and we need to give the file a new name						
							// to be absolutely sure, we need a unique name, we just add the time add the beginning of the filename							
							String timestamp = String.valueOf(System.currentTimeMillis());													
							// copy the file with a new name
							String partName = timestamp + tempPartDir.listFiles()[i].getName();
							File newOutput = new File(partDir,partName);
							FileUtils.copyFile(tempPartDir.listFiles()[i], newOutput);
							// get the part
							Part p = (Part) partUnmarshaller.unmarshal(new FileInputStream(newOutput));
							// if a part with a new name exists the image probably is also the same, wo we need a new name for it too
							String newImageName = timestamp + p.getImageName();
							FileUtils.copyFile(new File(tempImagesDir, p.getImageName()), new File(imageDir, newImageName));
							p.setImageName(newImageName);
							// persist the changes 
							partMarshaller.marshal(p, newOutput);
							// and finally add the part to the PartFactory
							AppContext.getContext().getBean(PartFactory.class).addPart(partName);
						}
						
					}
					catch (Exception e) {
						throw(e);
					}
				}
			}
		}
		
		File[] files = tempDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith("xml")) {
				return files[i];
			}
		}
		
		return null;
	}

	/**
	 * Opens a {@link BoardEditorModel} from a file.
	 * 
	 * @param file the file to open the board from
	 * 
	 * @return The {@link BoardEditorModel} based on the file
	 * @throws Exception 
	 */
	public static BoardEditorModel openBoard(File file) throws Exception {
		if (file == null) {
			throw new IllegalArgumentException("Could not open board, does the file exist?");
		}
		File tempDir = null;
		BoardBean board = null;
		try {
			Unmarshaller u = (Unmarshaller) JAXBContext.newInstance(BoardBean.class).createUnmarshaller();
			board = (BoardBean) u.unmarshal(file);
		}
		catch (JAXBException e) {
			throw e;
		}
		cleanBoard(board);
		BoardEditorModel model = null;
		try {
			if (board == null)
				throw new IllegalArgumentException("Could not load board");
			model = new BoardEditorModel();
			if (board.getHeight() < 1)
				model.setHeight(BOARD_DEFAULT_HEIGHT);
			else
				model.setHeight(board.getHeight());
			if (board.getWidth() < 1)
				model.setWidth(BOARD_DEFAULT_WIDTH);
			else
				model.setWidth(board.getWidth());
			model.setType(board.getType());
			if (board.getLayers() != null) {
				for (LayerBean lb : board.getLayers()) {
					model.getLayers().add(new Layer(lb));
				}
			}
			if (board.getLayers().size() == 0)
				model.addDefaultLayers();
			if (board.getType() != null && board.getType().equals(BoardType.CUSTOM)) {
				String name = file.getName().substring(0, file.getName().lastIndexOf("."));
				tempDir = new File(System.getProperty("java.io.tmpdir")+"/blackboard/", name);
				try {
					File backgroundImageFile = new File(tempDir, board.getBackgroundImage());
					BufferedImage bi = ImageIO.read(backgroundImageFile);
					model.setBackgroundImage(bi);
					model.setBackgroundImageLocation(backgroundImageFile.getAbsolutePath());
				}
				catch (IOException e) {
					log.info("Could not load background image.");
				}
			}
			for (LineBean lineBean : board.getLines()) {
				Line line = new Line(lineBean);
				model.getLayer(line.getLayer()).getItems().add(line);
			}
			for (TextPartBean textBean : board.getTextParts()) {
				Font f = new Font(textBean.getFont(), Font.PLAIN, textBean.getFontsize());
				Color c = null;
				if (textBean.getColor() != null)
					c = new Color(textBean.getColor().getRed(), textBean.getColor().getGreen(), textBean.getColor().getBlue());
				else
					c = Color.BLACK;
				TextPart p = new TextPart(textBean.getText(), f, c, textBean.getXLoc(), textBean.getYLoc(), textBean.getRotation());
				p.setOldXLoc(textBean.getXLoc());
				p.setOldYLoc(textBean.getYLoc());
				p.setLayer(textBean.getLayer());
				p.setIndex(textBean.getIndex());
				p.setType(textBean.getType());
				int ninetyDegreeSteps = textBean.getRotation() / 90;
				for (int i = 0; i < ninetyDegreeSteps; i++)
					p.rotateCW();
				p.setRotation(textBean.getRotation());
				model.getLayer(p.getLayer()).getItems().add(p);
			}
			for (PartBean partBean : board.getParts()) {
				Part part = AppContext.getContext().getBean(PartFactory.class).createPart(partBean.getFilename());
				// part does not exist -> proceed
				if (part == null)
					continue;
				part.setXLoc(partBean.getXLoc());
				part.setYLoc(partBean.getYLoc());
				part.setOldXLoc(part.getXLoc());
				part.setOldYLoc(part.getYLoc());
				part.setLayer(partBean.getLayer());
				part.setIndex(partBean.getIndex());
				part.setName(partBean.getName());
				part.setValue(partBean.getValue());
				
				/**
				 * Since we got only steps of 90 degrees this works here (:
				 */
				int ninetyDegreeSteps = Math.abs(partBean.getRotation()) / 90;
				for (int i = 0; i < ninetyDegreeSteps; i++) {
					if (partBean.getRotation() < 0) {
						part.rotateCCW();
					}
					else {
						part.rotateCW();	
					}				
				}				
				
				part.setRotation(partBean.getRotation());
				model.getLayer(part.getLayer()).getItems().add(part);
			}
			for (ResistorBean resistorBean : board.getResistors()) {
				Resistor resistor = new Resistor(resistorBean);
				model.getLayer(resistor.getLayer()).getItems().add(resistor);
				resistor.setIndex(resistorBean.getIndex());
			}
			for (DiodeBean diodeBean : board.getDiodes()) {
				Diode diode = new Diode(diodeBean);
				model.getLayer(diode.getLayer()).getItems().add(diode);
				diode.setIndex(diodeBean.getIndex());
			}
			for (BoxBean bb : board.getBoxes()) {
				Box box = new Box(bb);
				model.getLayer(box.getLayer()).getItems().add(box);
			}
			for (EllipseBean eb : board.getEllipses()) {
				Ellipse ellipse = new Ellipse(eb);
				model.getLayer(ellipse.getLayer()).getItems().add(ellipse);
			}
			for (SymbolBean sb : board.getSymbols()) {				
				Symbol symbol = new Symbol(sb);
				model.getLayer(sb.getLayer()).getItems().add(symbol);								
			}
		}
		catch (Exception i) {
			throw i;
		}
		return model;
	}

	/**
	 * Cleans the board from invalid lines, these lines are shorter than the
	 * raster of the editor.
	 * 
	 * @param board the {@link BoardBean} to clean before save
	 */
	private static void cleanBoard(BoardBean board) {
		for (Iterator<LineBean> it = board.getLines().iterator(); it.hasNext();) {
			LineBean line = it.next();
			if (line.getStart().equals(line.getEnd()))
				it.remove();
		}
	}

	/**
	 * Updates the {@link RecentFileList} with a new file
	 * 
	 * @param filename the name to append to the list
	 */
	public static void updateRecentFiles(String filename) {
		RecentFileList fileList = null;
		try {
			String inputDir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";
			String inputFileName = "recentFiles.xml";
			File inputFile = new File(inputDir + inputFileName);
			if (inputFile.exists()) {
				Unmarshaller u = JAXBContext.newInstance(RecentFileList.class).createUnmarshaller();
				fileList = (RecentFileList) u.unmarshal(inputFile);
			}
			if (fileList == null)
				fileList = new RecentFileList();
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
		int numRecentFiles = (Integer) Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.maxRecentFiles");
		if (fileList.getRecentFiles().size() >= numRecentFiles + 1) {
			fileList.getRecentFiles().remove(0);
		}
		if (!fileList.getRecentFiles().contains(filename))
			fileList.getRecentFiles().add(filename);
		Marshaller m;
		try {
			String outputDir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";
			String outputFileName = "recentFiles.xml";
			m = JAXBContext.newInstance(RecentFileList.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			File output = new File(outputDir + outputFileName);
			m.marshal(fileList, output);
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the board to disk
	 * 
	 * @param file the file to save the board to
	 * 
	 * @param model them {@link BoardEditorModel} to get the data from
	 * 
	 * @return boolean if save was successful
	 */
	public static boolean saveBoard(File file, BoardEditorModel model) {
		File outputFile;
		if (file.getAbsolutePath().endsWith("xml") || file.getAbsolutePath().endsWith("XML"))
			outputFile = new File(file.getAbsolutePath());
		else
			outputFile = new File(file.getAbsolutePath() + ".xml");
		BoardBean board = new BoardBean();
		board.setWidth(model.getWidth());
		board.setHeight(model.getHeight());
		board.setType(model.getType());
		for (int layer = 0; layer <= 3; layer++) {
			for (Item item : model.getLayer(layer).getItems()) {
				if (item instanceof TextPart) {
					TextPart part = (TextPart) item;
					TextPartBean tpb = new TextPartBean(part);
					board.getTextParts().add(tpb);
				}
				else if (item instanceof Part && !(item instanceof TextPart)) {
					Part part = (Part) item;
					PartBean pb = new PartBean(part);
					board.getParts().add(pb);
				}
				else if (item instanceof Line && !(item instanceof Resistor) && !(item instanceof Diode)) {
					Line line = (Line) item;
					LineBean lb = new LineBean(line);
					board.getLines().add(lb);
				}
				else if (item instanceof Resistor) {
					Resistor resistor = (Resistor) item;
					ResistorBean rb = new ResistorBean(resistor);
					board.getResistors().add(rb);
				}
				else if (item instanceof Diode) {
					Diode diode = (Diode) item;
					DiodeBean db = new DiodeBean(diode);
					board.getDiodes().add(db);
				}
				else if (item instanceof Box) {
					Box box = (Box) item;
					BoxBean bb = new BoxBean(box);
					board.getBoxes().add(bb);
				}
				else if (item instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) item;
					EllipseBean eb = new EllipseBean(ellipse);
					board.getEllipses().add(eb);
				}
				else if (item instanceof Symbol) {
					Symbol symbol = (Symbol)item;
					SymbolBean sb = new SymbolBean(symbol);
					board.getSymbols().add(sb);
				}
			}
		}
		try {
			Marshaller m = (Marshaller) JAXBContext.newInstance(BoardBean.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(board, outputFile);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Saves the board to disk
	 * 
	 * @param file the file to save the board to
	 * 
	 * @param model them {@link BoardEditorModel} to get the data from
	 * 
	 * @return boolean if save was successful
	 */
	public static boolean savePackedBoard(File file, BoardEditorModel model) throws IOException {

		File tempDir = new File(System.getProperty("java.io.tmpdir"),"blackboard");
		
		if (!tempDir.exists()) {
			if (!tempDir.mkdir()) {
				throw new IOException("Could not create temp directory, try to create it manually ("+tempDir.getAbsolutePath()+")");
			}
		}

		tempDir = new File(System.getProperty("java.io.tmpdir")+"/blackboard/",file.getName());
		if (!tempDir.exists()) {
			if (!tempDir.mkdir()) {
				throw new IOException("Could not create temp directory, try to create it manually ("+tempDir.getAbsolutePath()+")");
			}
		}
		File tempPartDir = new File(tempDir, "parts");
		if (!tempPartDir.exists()) {
			tempPartDir.mkdir();
		}
		File tempImagesDir = new File(tempPartDir, "images");
		if (!tempImagesDir.exists())
			tempImagesDir.mkdir();
		File tempXMLFile = new File(tempDir, file.getName() + ".xml");
		BoardBean board = new BoardBean();
		board.setWidth(model.getWidth());
		board.setHeight(model.getHeight());
		board.setType(model.getType());
		for (Layer l : model.getLayers())
			board.getLayers().add(new LayerBean(l));
		if (model.getBackgroundImageLocation() != null) {
			File bgImageFile = new File(model.getBackgroundImageLocation());
			File bgImageTemp = new File(tempDir, bgImageFile.getName());
			if (!bgImageTemp.exists())
				FileUtils.copyFile(bgImageFile, bgImageTemp);
			board.setBackgroundImage(bgImageFile.getName());
		}
		for (Layer layer : model.getLayers()) {
			for (Item item : layer.getItems()) {
				if (item instanceof TextPart) {
					TextPart part = (TextPart) item;
					TextPartBean tpb = new TextPartBean(part);
					board.getTextParts().add(tpb);
				}
				else if (item instanceof Part && !(item instanceof TextPart) && !(item instanceof Box)
						&& !(item instanceof Ellipse) && !(item instanceof Symbol)) {
					Part part = (Part) item;
					FileUtils.copyFile(new File(AppContext.getWorkingDir() + "/parts/" + part.getFilename()), new File(
							tempPartDir, part.getFilename()), true);
					FileUtils.copyFile(new File(AppContext.getWorkingDir() + "/parts/images/" + part.getImageName()), new File(
							tempImagesDir, part.getImageName()), true);
					PartBean pb = new PartBean(part);
					board.getParts().add(pb);
				}
				else if (item instanceof Line && !(item instanceof Resistor) && !(item instanceof Diode)) {
					Line line = (Line) item;
					LineBean lb = new LineBean(line);
					board.getLines().add(lb);
				}
				else if (item instanceof Resistor) {
					Resistor resistor = (Resistor) item;
					ResistorBean rb = new ResistorBean(resistor);
					board.getResistors().add(rb);
				}
				else if (item instanceof Diode) {
					Diode diode = (Diode) item;
					DiodeBean db = new DiodeBean(diode);
					board.getDiodes().add(db);
				}
				else if (item instanceof Box) {
					Box box = (Box) item;
					BoxBean bb = new BoxBean(box);
					board.getBoxes().add(bb);
				}
				else if (item instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) item;
					EllipseBean eb = new EllipseBean(ellipse);
					board.getEllipses().add(eb);
				}
				else if (item instanceof Symbol) {
					Symbol symbol = (Symbol)item;
					SymbolBean sb = new SymbolBean(symbol);
					board.getSymbols().add(sb);
				}
			}
		}
		try {
			Marshaller m = (Marshaller) JAXBContext.newInstance(BoardBean.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(board, tempXMLFile);
			ZipUtils.zipDir(file.getAbsolutePath(), tempDir.getAbsolutePath());			
		}
		catch (Exception e) {
			ErrorUtils.showErrorDialog(e);
			log.error("An exception occured during write : " + e.getMessage());
			return false;
		}
		return true;
	}
}
