/**
 * Copyright (c) 2008-2009, Piet Blok
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the copyright holder nor the names of the
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pmedv.core.ui.transform;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.ext.MouseEventUI;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;

/**
 * A component that contains {@link WheelButton}s for the preferred scale and
 * transformation options.
 * 
 * @author Piet Blok
 */
public class TransformWheelButtons extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String tooltip = "<html><body backgroundcolor='white'>"
	    + "<h2>How to use these controls</h2>"
	    + "<p><ul>"
	    + "<li>You can use the mouse wheel<br/>"
	    + "on the whole area of a control<br/>"
	    + "to increase or decrease its value.</li>"
	    + "<li>Pressing on the current value<br/>"
	    + "will reset it to its default value.</li>"
	    + "<li>Pressing on the up arrow will<br/>"
	    + "increase the value.</li>"
	    + "<li>Pressing on the down arrow will<br/>"
	    + "decrease the value.</li>"
	    + "</ul></p>"
	    + "<p align = 'center'><b>You can disable this tool tip by unchecking the<br/>"
	    + " \"Tip\" check box </b></p>" + "</body></html>";

    private final DefaultTransformModel model;

    /**
     * Sole constructor.
     * 
     * @param layer
     *            affected layer
     * @param clientListener
     *            an additional listener that will be notified when any change
     *            occurs on any of the wheels, or {@code null}
     */
    public TransformWheelButtons(JXLayer<?> layer,
	    PropertyChangeListener clientListener) {
	super(new BorderLayout());
	if (layer.getUI() == null) {
	    model = null;
	} else {
	    model = (DefaultTransformModel) ((TransformUI)(Object) layer.getUI())
		    .getModel();
	    layer.addPropertyChangeListener(clientListener);
	    JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
	    createSliders(buttonPanel);
	    final JXLayer<JPanel> buttonLayer = new JXLayer<JPanel>(
		    buttonPanel, new MouseEventUI<JPanel>());
	    buttonLayer.getGlassPane().setToolTipText(tooltip);
	    this.add(buttonLayer, BorderLayout.CENTER);
	    JToolBar toolBar = new JToolBar();
	    this.add(toolBar, BorderLayout.PAGE_END);
	    createActions(toolBar);
	    for (int index = 0; index < buttonPanel.getComponentCount(); index++) {
		buttonPanel.getComponent(index).addPropertyChangeListener(
			WheelButton.KEY_CURRENT, clientListener);
	    }
	    tipDisplayButton = new JCheckBox(new AbstractAction("Tip") {

		private static final long serialVersionUID = 1L;

		{
		    String tip = buttonLayer.getGlassPane().getToolTipText();
		    this.putValue(Action.SELECTED_KEY, tip != null
			    && tip.length() > 0);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		    buttonLayer
			    .getGlassPane()
			    .setToolTipText(
				    (Boolean) this
					    .getValue(Action.SELECTED_KEY) ? tooltip
					    : "");
		}
	    });
	}
    }

    private  JCheckBox tipDisplayButton;

    private void createActions(JToolBar toolBar) {
	final Action preserve = new AbstractAction("Preserve aspect ratio") {

	    private static final long serialVersionUID = 1L;

	    {
		this.putValue(Action.SELECTED_KEY, model
			.isPreserveAspectRatio());
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
		model.setPreserveAspectRatio((Boolean) this
			.getValue(Action.SELECTED_KEY));
	    }
	};

	final Action scalePreferred = new AbstractAction("Scale to preferred") {

	    private static final long serialVersionUID = 1L;

	    {
		this.putValue(Action.SELECTED_KEY, model
			.isScaleToPreferredSize());
		preserve.setEnabled(!(Boolean) this
			.getValue(Action.SELECTED_KEY));
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
		model.setScaleToPreferredSize((Boolean) this
			.getValue(Action.SELECTED_KEY));
		preserve.setEnabled(!(Boolean) this
			.getValue(Action.SELECTED_KEY));
	    }
	};
	final Action mirror = new AbstractAction("Mirror") {

	    private static final long serialVersionUID = 1L;

	    {
		this.putValue(Action.SELECTED_KEY, model.isMirror());
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
		model.setMirror((Boolean) this.getValue(Action.SELECTED_KEY));
	    }
	};

	for (Action action : new Action[] { preserve, scalePreferred, mirror }) {
	    toolBar.add(new JCheckBox(action));
	}
    }

    private void createSliders(JPanel buttonPanel) {
	NumberFormat numberFormat = NumberFormat.getNumberInstance();
	// Scale.
	buttonPanel.add(new WheelButton("Scale", model.getScale(),
		WheelButton.IncrementType.Factor, Math.sqrt(2), numberFormat,
		new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			model.setScale((Double) evt.getNewValue());
		    }
		}));
	// Radians rotation
	buttonPanel.add(new WheelButton("Rotate", model.getRotation(),
		WheelButton.IncrementType.Fixed, Math.PI / 90, numberFormat,
		new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			model.setRotation((Double) evt.getNewValue());
		    }
		}));
	// Quadrant rotation
	buttonPanel.add(new WheelButton("Quadrant",
		model.getQuadrantRotation(), WheelButton.IncrementType.Fixed,
		1, numberFormat, new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			model.setQuadrantRotation(((Double) evt.getNewValue())
				.intValue());
		    }
		}));
	// Shear X
	buttonPanel.add(new WheelButton("Shear X", model.getShearX(),
		WheelButton.IncrementType.Fixed, .02, numberFormat,
		new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			model.setShearX((Double) evt.getNewValue());
		    }
		}));
	// Shear Y
	buttonPanel.add(new WheelButton("Shear Y", model.getShearY(),
		WheelButton.IncrementType.Fixed, .02, numberFormat,
		new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			model.setShearY((Double) evt.getNewValue());
		    }
		}));
    }

    public JCheckBox getTipDisplayButton() {
	return tipDisplayButton;
    }

}
