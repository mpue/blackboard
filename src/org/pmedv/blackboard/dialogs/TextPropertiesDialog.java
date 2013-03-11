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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.components.TextPart.TextType;
import org.pmedv.blackboard.panels.TextPropertiesPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;


public class TextPropertiesDialog extends AbstractNiceDialog {

	private static final long serialVersionUID = -2659334064189002289L;

	private TextPropertiesPanel propertiesPanel;
	private TextPart part;
	
	private Integer fontSize;
	
	public TextPropertiesDialog(String title, String subTitle, ImageIcon icon, TextPart part) {
		super(title, subTitle, icon, true, false, true, true,AppContext.getContext().getBean(ApplicationWindow.class),part);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeComponents() {
	
		propertiesPanel = new TextPropertiesPanel();
		
		setSize(new Dimension(480,480));		
		getContentPanel().add(propertiesPanel);

	    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    String[] fontnames = e.getAvailableFontFamilyNames();
		
	    for (int i = 0; i < fontnames.length;i++ ) {
	    	propertiesPanel.getFontComboBox().addItem(fontnames[i]);
	    }
	    
		if (getUserObject() != null && getUserObject() instanceof TextPart) {
			this.part = (TextPart)getUserObject();			
			propertiesPanel.getFontComboBox().setSelectedItem(part.getFont().getFontName());
			propertiesPanel.getFontSizeSpinner().setValue(part.getFont().getSize());
			propertiesPanel.getTextPane().setText(part.getText());
			propertiesPanel.getTypeComboBox().setSelectedItem(part.getType());
		}
		
		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				fontSize = (Integer) propertiesPanel.getFontSizeSpinner().getValue();			
				Font font = new Font((String) propertiesPanel.getFontComboBox().getSelectedItem(), Font.PLAIN, fontSize);
				
				part.setFont(font);
				part.setText(propertiesPanel.getTextPane().getText());
				part.setType((TextType)propertiesPanel.getTypeComboBox().getSelectedItem());
				
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
		

	}

	/**
	 * @return the part
	 */
	public TextPart getPart() {
		return part;
	}

	/**
	 * @param part the part to set
	 */
	public void setPart(TextPart part) {
		this.part = part;
	}

	public Integer getFontSize() {
		return fontSize;
	}

}
