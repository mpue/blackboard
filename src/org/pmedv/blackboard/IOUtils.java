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
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
			partMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		catch (JAXBException e) {
			throw new RuntimeException("Unable to create unmarshaller for "+Part.class);
		}
	}

	public static File unpackBoard(File file) throws Exception {
		File tempDir = new File(System.getProperty("java.io.tmpdir") + "/blackboard/", file.getName());
		log.info("Temp directory is at : " + tempDir.getAbsolutePath());
		if (tempDir.exists()) {
			FileUtils.deleteDirectory(tempDir);
		}
		if (!tempDir.mkdirs()) {
			throw new IOException("Could not create temp directory " + tempDir.getAbsolutePath());
		}
		ZipUtils.extractZipArchive(file.getAbsolutePath(), tempDir.getAbsolutePath());
		File tempPartDir = new File(tempDir, "parts");
		File tempImagesDir = new File(tempPartDir, "images");
		
		File workDir = AppContext.getCatalogDir();
		log.info("Working directory " + workDir.getAbsolutePath());		
		File partDir = new File(workDir, "parts");
		File imageDir = new File(partDir, "images");
		
		File boardXml = findBoardXml(tempDir, file);
		if (tempPartDir.exists()) {
			if (!imageDir.exists() && !imageDir.mkdirs()) {
				throw new IOException("Could not create image directory " + imageDir.getAbsolutePath());
			}
			Set<String> usedPartFiles = collectUsedPartFilenames(boardXml);
			Map<String, String> partFilenameRemap = importPackedParts(tempPartDir, tempImagesDir, partDir, imageDir, usedPartFiles);
			if (boardXml != null && !partFilenameRemap.isEmpty()) {
				replacePartFilenamesInUnpackedBoardXml(boardXml, partFilenameRemap);
			}
		}
		return boardXml;
	}

	private static File findBoardXml(File tempDir, File packedFile) {
		File expectedBoardXml = new File(tempDir, packedFile.getName() + ".xml");
		if (expectedBoardXml.isFile()) {
			return expectedBoardXml;
		}
		File[] files = tempDir.listFiles();
		if (files != null) {
			for (File f : files) {
				String filename = f.getName();
				boolean isDirectChild = f.isFile() && !filename.contains("/") && !filename.contains("\\");
				if (isDirectChild && filename.toLowerCase().endsWith(".xml")) {
					return f;
				}
			}
		}
		return null;
	}

	/**
	 * Copies packed parts into the local library. Existing files with the same
	 * content are left alone; colliding names with different content get a unique
	 * filename. Returns a map of original XML names to the names actually used.
	 */
	private static Map<String, String> importPackedParts(File tempPartDir, File tempImagesDir, File partDir, File imageDir, Set<String> usedPartFiles) throws Exception {
		Map<String, String> filenameRemap = new HashMap<String, String>();
		File[] packedFiles = tempPartDir.listFiles();
		if (packedFiles == null) {
			return filenameRemap;
		}
		PartFactory partFactory = AppContext.getContext().getBean(PartFactory.class);
		for (File packedFile : packedFiles) {
			if (!packedFile.isFile() || !packedFile.getName().toLowerCase().endsWith(".xml")) {
				continue;
			}
			String originalName = packedFile.getName();
			if (usedPartFiles != null && !usedPartFiles.contains(originalName)) {
				log.info("Skipping packed part not used on board: " + originalName);
				continue;
			}
			File localFile = new File(partDir, originalName);
			if (localFile.exists() && FileUtils.contentEquals(localFile, packedFile)) {
				continue;
			}
			String destXmlName = originalName;
			if (localFile.exists()) {
				File destXml = findNumberedCopyOrUnusedName(partDir, originalName, packedFile);
				destXmlName = destXml.getName();
				if (destXml.exists()) {
					if (partFactory.getPart(destXmlName) == null) {
						partFactory.addPart(destXmlName);
					}
					filenameRemap.put(originalName, destXmlName);
					continue;
				}
				log.info("Part filename collision for " + originalName + ", importing as " + destXmlName);
			}
			Part packedPart = unmarshalPart(packedFile);
			copyPackedPartIntoLibrary(packedFile, packedPart, destXmlName, tempImagesDir, partDir, imageDir, partFactory);
			if (!destXmlName.equals(originalName)) {
				filenameRemap.put(originalName, destXmlName);
			}
		}
		return filenameRemap;
	}

	/**
	 * Filenames of library parts still placed on the board. {@code null} means
	 * the board XML could not be read and packed parts should not be filtered.
	 */
	private static Set<String> collectUsedPartFilenames(File boardXml) {
		if (boardXml == null || !boardXml.isFile()) {
			return null;
		}
		try {
			Unmarshaller u = JAXBContext.newInstance(BoardBean.class).createUnmarshaller();
			BoardBean board = (BoardBean) u.unmarshal(boardXml);
			Set<String> names = new HashSet<String>();
			if (board != null && board.getParts() != null) {
				for (PartBean partBean : board.getParts()) {
					if (partBean.getFilename() != null && partBean.getFilename().length() > 0) {
						names.add(partBean.getFilename());
					}
				}
			}
			return names;
		}
		catch (Exception e) {
			log.warn("Could not read packed board parts list, importing all packed parts.");
			return null;
		}
	}

	/**
	 * Copies a packed part XML and its image into the local parts library.
	 * {@code destXmlName} is already resolved; this only unique-names the image
	 * if that filename is taken by a different file.
	 */
	private static void copyPackedPartIntoLibrary(File packedXml, Part packedPart, String destXmlName,
			File tempImagesDir, File partDir, File imageDir, PartFactory partFactory) throws Exception {
		boolean imageNameChanged = false;
		String imageName = packedPart.getImageName();
		if (imageName != null) {
			File packedImage = new File(tempImagesDir, imageName);
			if (packedImage.isFile()) {
				File destImage = new File(imageDir, imageName);
				if (destImage.exists() && !FileUtils.contentEquals(destImage, packedImage)) {
					destImage = findNumberedCopyOrUnusedName(imageDir, imageName, packedImage);
					packedPart.setImageName(destImage.getName());
					imageNameChanged = true;
				}
				if (!destImage.exists()) {
					FileUtils.copyFile(packedImage, destImage);
				}
			}
		}
		File destXml = new File(partDir, destXmlName);
		if (imageNameChanged) {
			partMarshaller.marshal(packedPart, destXml);
		}
		else {
			FileUtils.copyFile(packedXml, destXml);
		}
		partFactory.addPart(destXmlName);
	}

	/**
	 * For a colliding name like {@code foo.xml}, checks {@code foo_1.xml},
	 * {@code foo_2.xml}, ... in order. Returns an existing numbered copy whose
	 * bytes match {@code content}, or the first numbered name that is still unused.
	 */
	private static File findNumberedCopyOrUnusedName(File dir, String filename, File content) throws IOException {
		int dot = filename.lastIndexOf('.');
		String base = dot > 0 ? filename.substring(0, dot) : filename;
		String ext = dot > 0 ? filename.substring(dot) : "";
		int n = 1;
		while (true) {
			File candidate = new File(dir, base + "_" + n + ext);
			if (!candidate.exists()) {
				return candidate;
			}
			if (content != null && FileUtils.contentEquals(candidate, content)) {
				return candidate;
			}
			n++;
		}
	}

	private static Part unmarshalPart(File file) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		try {
			return (Part) partUnmarshaller.unmarshal(fis);
		}
		finally {
			fis.close();
		}
	}

	/**
	 * Packed boards still name parts as they were stored in the zip
	 * (e.g. {@code foo.xml}). After a filename collision the library copy is
	 * {@code foo_1.xml}; this rewrites those references in the unpacked board
	 * XML so {@code openBoard} loads the imported files.
	 */
	private static void replacePartFilenamesInUnpackedBoardXml(File boardXml, Map<String, String> packedNameToLibraryName) throws Exception {
		Unmarshaller u = JAXBContext.newInstance(BoardBean.class).createUnmarshaller();
		BoardBean board = (BoardBean) u.unmarshal(boardXml);
		if (board == null || board.getParts() == null) {
			return;
		}
		boolean changed = false;
		for (PartBean partBean : board.getParts()) {
			String libraryName = packedNameToLibraryName.get(partBean.getFilename());
			if (libraryName != null) {
				partBean.setFilename(libraryName);
				changed = true;
			}
		}
		if (!changed) {
			return;
		}
		Marshaller m = JAXBContext.newInstance(BoardBean.class).createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(board, boardXml);
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
			model.ensureCurrentLayer();
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
				model.addItem(line, line.getLayer());
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
				model.addItem(p, p.getLayer());
			}
			for (PartBean partBean : board.getParts()) {
				Part template = AppContext.getContext().getBean(PartFactory.class).getPart(partBean.getFilename());
				if (template == null)
					continue;
				Part part = (Part) template.clone();
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
				model.addItem(part, part.getLayer());
			}
			for (ResistorBean resistorBean : board.getResistors()) {
				Resistor resistor = new Resistor(resistorBean);
				model.addItem(resistor, resistor.getLayer());
				resistor.setIndex(resistorBean.getIndex());
			}
			for (DiodeBean diodeBean : board.getDiodes()) {
				Diode diode = new Diode(diodeBean);
				model.addItem(diode, diode.getLayer());
				diode.setIndex(diodeBean.getIndex());
			}
			for (BoxBean bb : board.getBoxes()) {
				Box box = new Box(bb);
				model.addItem(box, box.getLayer());
			}
			for (EllipseBean eb : board.getEllipses()) {
				Ellipse ellipse = new Ellipse(eb);
				model.addItem(ellipse, ellipse.getLayer());
			}
			for (SymbolBean sb : board.getSymbols()) {				
				Symbol symbol = new Symbol(sb);
				model.addItem(symbol, sb.getLayer());								
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
	public static synchronized void updateRecentFiles(String filename) {
		RecentFileList fileList = null;
		try {
			String inputDir = AppContext.getUserDataDir().getAbsolutePath() + "/";
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
			String outputDir = AppContext.getUserDataDir().getAbsolutePath() + "/";
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

		File blackboardTemp = new File(System.getProperty("java.io.tmpdir"), "blackboard");
		if (!blackboardTemp.exists() && !blackboardTemp.mkdir()) {
			throw new IOException("Could not create temp directory, try to create it manually ("+blackboardTemp.getAbsolutePath()+")");
		}

		File tempDir = new File(blackboardTemp, file.getName());
		if (tempDir.exists()) {
			FileUtils.deleteDirectory(tempDir);
		}
		if (!tempDir.mkdirs()) {
			throw new IOException("Could not create temp directory, try to create it manually ("+tempDir.getAbsolutePath()+")");
		}
		File tempPartDir = new File(tempDir, "parts");
		if (!tempPartDir.mkdir()) {
			throw new IOException("Could not create temp parts directory ("+tempPartDir.getAbsolutePath()+")");
		}
		File tempImagesDir = new File(tempPartDir, "images");
		if (!tempImagesDir.mkdir()) {
			throw new IOException("Could not create temp images directory ("+tempImagesDir.getAbsolutePath()+")");
		}
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
					FileUtils.copyFile(new File(AppContext.getCatalogDir(), "parts/" + part.getFilename()), new File(
							tempPartDir, part.getFilename()), true);
					FileUtils.copyFile(new File(AppContext.getCatalogDir(), "parts/images/" + part.getImageName()), new File(
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
