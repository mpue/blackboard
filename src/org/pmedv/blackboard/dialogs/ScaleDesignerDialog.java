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
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.panels.CenterPanel;
import org.pmedv.blackboard.panels.ScaleDesignerPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;

public class ScaleDesignerDialog extends AbstractNiceDialog {

	public ScaleDesignerDialog(String title, String subTitle, ImageIcon icon) {
		super(title, subTitle, icon, true, false, true, true, AppContext.getContext().getBean(ApplicationWindow.class), null);
	}

	private ScaleDesignerPanel scaleDesignerPanel;
	private BoardEditor boardEditor;
	private BoardEditorModel model;
	private JPanel dialogPanel;
	

	private int steps;
	private int offsetStartAngle;
	private int innerSize;
	private int outerSize;
	private int scaleAngle;
	private boolean showCaption;
	private Font captionFont;
	private int thickness;
	private int range;
	private int startValue;
	private boolean showSubSteps;
	
	@Override
	protected void initializeComponents() {
		
		setSize(new Dimension(800,750));
		setResizable(false);
		
		model = new BoardEditorModel();
		model.setWidth(750);
		model.setHeight(400);
		model.getLayers().add(new Layer(0, "default"));
		
		boardEditor = new BoardEditor(model);
		boardEditor.setGridVisible(true);
		
		scaleDesignerPanel = new ScaleDesignerPanel();
		scaleDesignerPanel.setDialog(this);
		scaleDesignerPanel.initDataBindings();
		dialogPanel = new JPanel(new BorderLayout());
		
		dialogPanel.add(scaleDesignerPanel,BorderLayout.SOUTH);
		
		CenterPanel panel = new CenterPanel();
		panel.getCenterPanel().add(boardEditor);		
		dialogPanel.add(panel,BorderLayout.NORTH);

		getContentPanel().add(dialogPanel);
		
		initValues();
		
		BoardUtil.createRoundScale(model, new Point(model.getWidth() / 2, model.getHeight() / 2), steps, range, startValue, 
				offsetStartAngle, innerSize, outerSize, scaleAngle, showCaption,
				captionFont, thickness, showSubSteps);
		boardEditor.refresh();
		
		getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				EditorUtils.getCurrentActiveEditor().getModel().getCurrentLayer().getItems().addAll(model.getLayer(0).getItems());
				EditorUtils.getCurrentActiveEditor().refresh();
				
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

	private void initValues() {
		
		steps = 20;
		offsetStartAngle = 0;
		innerSize = 70;
		outerSize = 100;
		scaleAngle = 360;
		showCaption = false;
		captionFont = new Font("Arial",Font.PLAIN,14);
		thickness = 3;
		startValue = 0;
		range = 5;
		showSubSteps = false;
		
		scaleDesignerPanel.getStepsSpinner().setValue(new Integer(steps));
		scaleDesignerPanel.getStartOffsetSpinner().setValue(new Integer(offsetStartAngle));
		scaleDesignerPanel.getInnerRadiusSpinner().setValue(new Integer(innerSize));
		scaleDesignerPanel.getOuterRadiusSpinner().setValue(new Integer(outerSize));
		scaleDesignerPanel.getScaleAngleSpinner().setValue(new Integer(scaleAngle));
		scaleDesignerPanel.getCaptionCheckBox().setSelected(showCaption);
		scaleDesignerPanel.getLineThicknessSpinner().setValue(new Integer(thickness));
		scaleDesignerPanel.getRangeSpinner().setValue(new Integer(range));
		scaleDesignerPanel.getStartValueSpinner().setValue(new Integer(startValue));
		scaleDesignerPanel.getSubStepsCheckBox().setSelected(showSubSteps);
		
	}

	public void refreshEditor() {
		model.getLayer(0).getItems().clear();
		BoardUtil.createRoundScale(model, new Point(model.getWidth()/2, model.getHeight()/2), 
				steps, range,startValue,offsetStartAngle, innerSize, outerSize, 
				scaleAngle, showCaption, captionFont, thickness, showSubSteps);
		boardEditor.refresh();
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(int steps) {
		this.steps = steps;
		refreshEditor();
	}

	/**
	 * @param offsetStartAngle the offsetStartAngle to set
	 */
	public void setOffsetStartAngle(int offsetStartAngle) {
		this.offsetStartAngle = offsetStartAngle;
		refreshEditor();
	}

	/**
	 * @param innerSize the innerSize to set
	 */
	public void setInnerSize(int innerSize) {
		this.innerSize = innerSize;
		refreshEditor();
	}

	/**
	 * @param outerSize the outerSize to set
	 */
	public void setOuterSize(int outerSize) {
		this.outerSize = outerSize;
		refreshEditor();
	}

	/**
	 * @param scaleAngle the scaleAngle to set
	 */
	public void setScaleAngle(int scaleAngle) {
		this.scaleAngle = scaleAngle;
		refreshEditor();
	}

	/**
	 * @param showCaption the showCaption to set
	 */
	public void setShowCaption(boolean showCaption) {
		this.showCaption = showCaption;
		refreshEditor();
	}

	/**
	 * @param thickness the thickness to set
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
		refreshEditor();
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
		refreshEditor();
	}

	/**
	 * @param startValue the startValue to set
	 */
	public void setStartValue(int startValue) {
		this.startValue = startValue;
		refreshEditor();
	}

	/**
	 * @param showSubSteps the showSubSteps to set
	 */
	public void setShowSubSteps(boolean showSubSteps) {
		this.showSubSteps = showSubSteps;
		refreshEditor();
	}

}
