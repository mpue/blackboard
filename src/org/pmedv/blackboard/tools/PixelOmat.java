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
package org.pmedv.blackboard.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.blackboard.tools.PixelEditor.Pixel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;
import org.pmedv.core.util.ErrorUtils;

public class PixelOmat extends JFrame {

	private static final long serialVersionUID = -1517460878252360072L;

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	public PixelOmat() {

		final PixelEditor pixelEditor = new PixelEditor(32, 32);

		JMenuBar menuBar = new JMenuBar();

		JMenuItem loadItem = new JMenuItem(new AbstractAction("Load") {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));

				fc.setDialogTitle("Open image");
				fc.setApproveButtonText("Open");

				int result = fc.showOpenDialog(PixelOmat.this);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					File selectedFile = fc.getSelectedFile();

					try {
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile));
						Pixel[][] pixels = (Pixel[][]) ois.readObject();
						pixelEditor.setPixels(pixels);
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}
		});

		JMenuItem loadImageItem = new JMenuItem(new AbstractAction("Load image") {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));

				fc.setDialogTitle("Open image");
				fc.setApproveButtonText("Open");

				int result = fc.showOpenDialog(PixelOmat.this);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					File selectedFile = fc.getSelectedFile();

					try {
						
						BufferedImage bi = ImageIO.read(new FileInputStream(selectedFile));
						
						if (bi.getWidth() > 32 || bi.getHeight() > 32) {
							IllegalArgumentException ex = new IllegalArgumentException("Image dimensions must be <= 32x32.");
							ErrorUtils.showErrorDialog(ex);
							return;
						}
						
						Pixel[][] pixels = new Pixel[32][32];
						
						for (int x = 0; x < 32;x++) {
							for (int y = 0; y < 32;y++) {								
								Color c = new Color(bi.getRGB(x, y));
								int alpha = (bi.getRGB(x, y) >> 24) & 0xff;
								if (alpha > 0)
									pixels[x][y] = new Pixel(c);
							}							
						}
						
						pixelEditor.setPixels(pixels);
					}
					catch (Exception e1) {
						ErrorUtils.showErrorDialog(e1);
					}

				}

			}
		});
		
		
		JMenuItem saveItem = new JMenuItem(new AbstractAction("Save") {

			@Override
			public void actionPerformed(ActionEvent e) {

				final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));

				fc.setDialogTitle("Save image");
				fc.setApproveButtonText("Save");

				int result = fc.showOpenDialog(PixelOmat.this);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					File selectedFile = fc.getSelectedFile();

					try {
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile));
						oos.writeObject(pixelEditor.getPixels());

					}
					catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}
		});

		JMenuItem exportItem = new JMenuItem(new AbstractAction("Export") {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));

				fc.setDialogTitle("Save image");
				fc.setApproveButtonText("Save");

				int result = fc.showOpenDialog(PixelOmat.this);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					File selectedFile = fc.getSelectedFile();
					try {
						ImageIO.write(pixelEditor.getImage(), "PNG", selectedFile);
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		});

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		fileMenu.add(loadItem);
		fileMenu.add(loadImageItem);
		fileMenu.add(saveItem);		
		fileMenu.add(exportItem);

		JMenuItem colorItem = new JMenuItem(new AbstractAction("Color") {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelEditor.triggerColorSelect();
			}
		});

		JMenuItem clearItem = new JMenuItem(new AbstractAction("Clear") {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelEditor.clear();
			}
		});

		JMenuItem fillItem = new JMenuItem(new AbstractAction("Fill") {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelEditor.fill();
			}
		});

		JMenuItem pickColorItem = new JMenuItem(new AbstractAction("Pick Color") {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelEditor.setPickingColor(true);
				pixelEditor.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
		});		
		
		JMenuItem addAsPartItem = new JMenuItem(new AbstractAction("Add as part") {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAsPart(pixelEditor.getImage());
			}
		});
		
		JButton pushUpItem = new JButton(new AbstractAction("Push up") {
			@Override
			public void actionPerformed(ActionEvent e) {
		
				for (int i = 0; i < pixelEditor.pixels.length;i++) {
					
					Pixel p = pixelEditor.pixels[i][0];

			        for (int j = 0; j < pixelEditor.pixels[i].length - 1; j++) {
			            // Move each pixel up one spot
			        	pixelEditor.pixels[i][j] = pixelEditor.pixels[i][j + 1]; 
			        }
					
			        // At the end of the array, put what was stored.
			        pixelEditor.pixels[i][pixelEditor.pixels[i].length -1] = p;							        
				}
				
				pixelEditor.repaint();
			}
		});
		
		
		JButton pushLeftItem = new JButton(new AbstractAction("Push left") {
			@Override
			public void actionPerformed(ActionEvent e) {
		
				for (int i = 0; i < pixelEditor.pixels.length;i++) {
					
					Pixel p = pixelEditor.pixels[0][i];

			        for (int j = 0; j < pixelEditor.pixels[i].length - 1; j++) {
			            // Move each pixel up one spot
			        	pixelEditor.pixels[j][i] = pixelEditor.pixels[j + 1][i]; 
			        }
					
			        // At the end of the array, put what was stored.
			        pixelEditor.pixels[pixelEditor.pixels[i].length -1][i] = p;							        
				}
				
				
				pixelEditor.repaint();
			}
		});
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic('E');

		editMenu.add(colorItem);
		editMenu.add(clearItem);
		editMenu.add(fillItem);
		editMenu.add(pickColorItem);
		editMenu.add(addAsPartItem);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);

		setIconImage(AppContext.getContext().getBean(ApplicationWindow.class).getIconImage());
		setTitle("Pixel-O-Mat");
		setSize(new Dimension(600, 600));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		add(pixelEditor, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(pushLeftItem);
		buttonPanel.add(pushUpItem);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		setJMenuBar(menuBar);
		setLocationRelativeTo(AppContext.getContext().getBean(ApplicationWindow.class));

	}
	
	private void addAsPart(BufferedImage image) { 
		final ApplicationWindow win = AppContext.getContext().getBean(ApplicationWindow.class);
		
		// need a name for the part
		
		String partName = JOptionPane.showInputDialog(win, resources.getResourceByKey("ConvertToPartCommand.message"));

		// no name? skip
		
		if (partName == null || partName.length() < 1)
			return;
		
		// which editor do we have?

		final BoardEditor editor = EditorUtils.getCurrentActiveEditor();
		
		int width  = 32;
		int height = 32;
			
		// store the PixelOmat image to the parts/images directory
		
		File workDir = new File(".");
		File partDir = new File(workDir + "/parts/");
		File imageDir = new File(workDir + "/parts/images/");
		File outputImage = new File(imageDir+"/"+partName+".png");
		File outputPart = new File(partDir+"/"+partName+".xml");
		
		try {
			ImageIO.write(image, "PNG", outputImage);
		}
		catch (IOException e) {
			ErrorUtils.showErrorDialog(e);
		}

		// create according part
		
		Part newPart = new Part();

		newPart.setFilename(partName+".xml");
		newPart.setImage(image);
		newPart.setIndex(0);
		newPart.setLayer(0);
		newPart.setAuthor(System.getProperty("user.name"));
		newPart.setLicense("");
		newPart.setColor(Color.WHITE);
		newPart.setDescription(partName);
		newPart.setDesignator("P");
		newPart.setHeight(height);
		newPart.setWidth(width);
		newPart.setImageName(outputImage.getName());
		newPart.setPackageType("symbol");
		newPart.setXLoc(0);
		newPart.setYLoc(0);
		newPart.setValue("");
		newPart.setName(partName);
		newPart.setResizable(false);
		newPart.setRotation(0);
		newPart.setOpacity(1.0f);
		
		// marshall to xml file

		try {
			Marshaller m = JAXBContext.newInstance(Part.class).createMarshaller();
			m.marshal(newPart, new FileOutputStream(outputPart));
		}
		catch (Exception e) {
			ErrorUtils.showErrorDialog(e);
			return;
		}

		AppContext.getContext().getBean(PartDialog.class).getModel().addPart(newPart);
			
		newPart.setXLoc(32);
		newPart.setYLoc(32);
		
		editor.getModel().getCurrentLayer().getItems().add(newPart);
		editor.setSelectedItem(newPart);
		editor.refresh();
		
	}

}
