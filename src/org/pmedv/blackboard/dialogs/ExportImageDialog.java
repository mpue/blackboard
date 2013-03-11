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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.panels.ExportImagePanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ImageUtils;

/**
 * This is a preview dialog in order to export the layout of the current
 * selected editor to disk. The user may choose the path, and the render options
 * of the image. Currently the image may be exported as grascale, inverted and flipped
 * horizontally.
 * 
 * @author Matthias Pueski (19.11.2010)
 *
 */
public class ExportImageDialog extends AbstractNiceDialog {

	private static final long serialVersionUID = 3401546785363352512L;

	private ExportImagePanel exportPanel;

	private BoardEditor editor;

	private boolean grayscale = false;
	private boolean invert = false;
	private boolean flip = false;
	private BufferedImage preview;
	private BufferedImage image;

	private ImageIcon icon;
	private JLabel label;

	public ExportImageDialog(String title, String subTitle, ImageIcon icon) {
		super(title, subTitle, icon, true, false, true, true,AppContext.getContext().getBean(ApplicationWindow.class),null);
	}

	@Override
	protected void initializeComponents() {

		exportPanel = new ExportImagePanel();
		exportPanel.getFileTextField().setParent(this);

		editor = EditorUtils.getCurrentActiveEditor();

		setSize(new Dimension(640, 550));
		getContentPanel().add(exportPanel);

		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				result = OPTION_OK;

				if (flip)
					image = ImageUtils.flipHorizontal(image);
				if (grayscale)
					image = ImageUtils.convertToGrayscale(image);
				if (invert)
					image = ImageUtils.invert(image);

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

		exportPanel.getGrayscaleCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grayscale = exportPanel.getGrayscaleCheckbox().isSelected();
				updatePreview();
			}
		});
		exportPanel.getInvertCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				invert = exportPanel.getInvertCheckbox().isSelected();
				updatePreview();
			}
		});
		exportPanel.getMirrorCheckbox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				flip = exportPanel.getMirrorCheckbox().isSelected();
				updatePreview();
			}
		});

		exportPanel.getFileTextField().getFileChooser().setDialogTitle(resources.getResourceByKey("ExportImageDialog.saveMsg"));
		exportPanel.getFileTextField().getFileChooser().setFileFilter(new ImageFilter());
		
		setLocationRelativeTo(getRootPane());

		icon = new ImageIcon();
		label = new JLabel(icon);
		exportPanel.getPreviewPanel().add(label, BorderLayout.CENTER);

		updatePreview();
	}

	private void updatePreview() {

		if (editor != null) {

			image = ImageUtils.createImageFromComponent(editor);
			preview = ImageUtils.scale(image, 400, 300);

			if (grayscale)
				preview = ImageUtils.convertToGrayscale(preview);
			if (invert)
				preview = ImageUtils.invert(preview);
			if (flip)
				preview = ImageUtils.flipHorizontal(preview);

			icon.setImage(preview);
			label.invalidate();
			label.repaint();

		}

	}
	
	private static class ImageFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			if ( f.getName().endsWith(".png") ||
				 f.getName().endsWith(".PNG") ) 
				return true;
			else
				return false;
		}

		@Override
		public String getDescription() {
			return resources.getResourceByKey("msg.filefilter.imgfiles")+"(*.png *.PNG)";
		}
	}

	public String getSelectedPath() {
		return exportPanel.getFileTextField().getPathField().getText();
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

}
