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
package org.pmedv.blackboard.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import org.pmedv.blackboard.filter.PNGFilter;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.models.BoardEditorModel.BoardType;
import org.pmedv.blackboard.panels.BoardPropertiesPanel;
import org.pmedv.blackboard.tools.BoardGen;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;

/**
 * This dialog determines the properties of a board being created.
 * 
 * 
 * @author Matthias Pueski (10.07.2011)
 * 
 */
public class BoardPropertiesDialog extends AbstractNiceDialog {
	
	private static final long serialVersionUID = -2659334064189002289L;
	
	// Dimensions of this dialog
	private static final int DIALOG_WIDTH = 400;
	private static final int DIALOG_HEIGHT = 320;
	
	private BoardPropertiesPanel propertiesPanel;
	private BoardEditorModel model;
	
	private static final int UNIT = 16;
	private static final float INCH = 2.54f;

	public BoardPropertiesDialog(String title, String subTitle, ImageIcon icon, BoardEditorModel model) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), model);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeComponents() {
		
		propertiesPanel = new BoardPropertiesPanel();
		propertiesPanel.getFileField().getFileChooser().setFileFilter(new PNGFilter());
		setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
		setResizable(false);		
		getContentPanel().add(propertiesPanel);
		propertiesPanel.getTypeCombo().setRenderer(new BoardTypeComboBoxRenderer());
		propertiesPanel.getFileField().setEnabled(false);
		
		if (getUserObject() != null && getUserObject() instanceof BoardEditorModel) {
			this.model = (BoardEditorModel) getUserObject();
			propertiesPanel.getBoardWidthSpinner().setValue(model.getWidth());
			propertiesPanel.getBoardHeightSpinner().setValue(model.getHeight());
			propertiesPanel.getTypeCombo().setSelectedItem(model.getType());
			if (model.getType().equals(BoardType.CUSTOM)) {
				propertiesPanel.getFileField().setEnabled(true);
				propertiesPanel.getFileField().getPathField().setText(model.getBackgroundImageLocation());
			}
		}
		
		initListeners();
	}

	private void initListeners() {
		getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer width = (Integer) propertiesPanel.getBoardWidthSpinner().getValue();
				Integer height = (Integer) propertiesPanel.getBoardHeightSpinner().getValue();
				
				String unit = (String)propertiesPanel.getUnitComboBox().getSelectedItem();
				
				if (unit.equalsIgnoreCase("mm")) {
					width  = (int) ((width/INCH)*UNIT);
					height = (int) ((height/INCH)*UNIT);
				}
				
				if (model != null) {
					model.setWidth(width);
					model.setHeight(height);
					BoardType bt = (BoardType) propertiesPanel.getTypeCombo().getSelectedItem();
					model.setType(bt);
					if (bt.equals(BoardType.CUSTOM)) {
						try {
							File backgroundImageFile = new File(propertiesPanel.getFileField().getPathField().getText());
							BufferedImage bi = ImageIO.read(backgroundImageFile);
							model.setBackgroundImage(bi);
							model.setBackgroundImageLocation(backgroundImageFile.getAbsolutePath());
						}
						catch (IOException e1) {
							ErrorUtils.showErrorDialog(e1);
						}
					}
					else {
						model.setBackgroundImage(BoardGen.generateBoard(model.getWidth(), model.getHeight(), model.getType(), false));
						model.setBackgroundImageLocation(null);
					}
				}
				result = OPTION_OK;
				setVisible(false);
				dispose();
			}
		});
		getCancelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = OPTION_CANCEL;
				setVisible(false);
				dispose();
			}
		});
		propertiesPanel.getTypeCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				propertiesPanel.getFileField().setEnabled(
						propertiesPanel.getTypeCombo().getSelectedItem().equals(BoardType.CUSTOM));
			}
		});
		propertiesPanel.getUnitComboBox().addActionListener(new ActionListener() {

			// TODO : Do something about it!
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String unit = (String)propertiesPanel.getUnitComboBox().getSelectedItem();
				
				if (unit.equalsIgnoreCase("mm")) {

					
				}
				else {
					

				}
				
			}
		});
	}

	private static class BoardTypeComboBoxRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = -4883209423337213750L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			BoardTypeComboBoxRenderer renderer = (BoardTypeComboBoxRenderer) super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			if (value instanceof BoardType) {
				BoardType bt = (BoardType) value;
				switch (bt) {
					case BLANK:
						renderer.setText(resources.getResourceByKey("BoardType.blank"));
						break;
					case CUSTOM:
						renderer.setText(resources.getResourceByKey("BoardType.custom"));
						break;
					case HOLES:
						renderer.setText(resources.getResourceByKey("BoardType.holes"));
						break;
					case SCHEMATICS:
						renderer.setText(resources.getResourceByKey("BoardType.schematics"));
						break;						
					case STRIPES:
						renderer.setText(resources.getResourceByKey("BoardType.stripes"));
						break;
					case HOLESSTRIPS:
						renderer.setText(resources.getResourceByKey("BoardType.holesAndStrip"));
						break;	
					default:
						break;
				}
			}
			return renderer;
		}
	}
}
