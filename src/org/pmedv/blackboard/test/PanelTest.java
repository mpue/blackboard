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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.pmedv.blackboard.tools.PixelEditor;
import org.pmedv.blackboard.tools.PixelEditor.Pixel;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyRed;

public class PanelTest {

	private static JFrame frame;

	public static void main(String[] args) {

		Plastic3DLookAndFeel.setPlasticTheme(new SkyRed());

		try {
			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
			com.jgoodies.looks.Options.setPopupDropShadowEnabled(true);
		} catch (Exception e) {
			System.out.println("failed to set look and feel.");
		}

		final PixelEditor pixelEditor = new PixelEditor(16, 16);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pixelEditor.clear();
			}
		});

		
		JButton colorButton = new JButton("Color");
		colorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pixelEditor.triggerColorSelect();
			}
		});

		JButton fillButton = new JButton("Fill");
		fillButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pixelEditor.fill();
			}
		});
		
		
		JButton saveImageButton = new JButton("Export");

		saveImageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));

				fc.setDialogTitle("Save image");
				fc.setApproveButtonText("Save");

				int result = fc.showOpenDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					File selectedFile = fc.getSelectedFile();
					try {
						ImageIO.write(pixelEditor.getImage(), "PNG", selectedFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		JButton saveButton = new JButton("Save");

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));

				fc.setDialogTitle("Save image");
				fc.setApproveButtonText("Save");

				int result = fc.showOpenDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					File selectedFile = fc.getSelectedFile();

					try {
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile));
						oos.writeObject(pixelEditor.getPixels());

					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}
		});

		JButton loadButton = new JButton("Load");

		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(System.getProperty("user.home"));

				fc.setDialogTitle("Open image");
				fc.setApproveButtonText("Open");

				int result = fc.showOpenDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {

					if (fc.getSelectedFile() == null)
						return;

					File selectedFile = fc.getSelectedFile();

					try {
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile));
						Pixel[][] pixels = (Pixel[][]) ois.readObject();
						pixelEditor.setPixels(pixels);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}
		});

		buttonPanel.add(clearButton);		
		buttonPanel.add(colorButton);
		buttonPanel.add(fillButton);
		buttonPanel.add(saveImageButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(loadButton);

		frame = new JFrame("Pixel-O-Mat");
		frame.setSize(new Dimension(330, 380));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(pixelEditor, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setVisible(true);

	}

}
