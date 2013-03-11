package org.pmedv.blackboard.panels;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.pmedv.blackboard.dialogs.ScaleDesignerDialog;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ScaleDesignerPanel extends JPanel {
	
	private JSpinner stepsSpinner;
	private JSpinner startOffsetSpinner;
	private JSpinner scaleAngleSpinner;
	private JSpinner lineThicknessSpinner;
	private JSpinner innerRadiusSpinner;
	private JSpinner outerRadiusSpinner;
	private JCheckBox captionCheckBox;
	private JSpinner startValueSpinner;
	private JSpinner rangeSpinner;
	private JCheckBox subStepsCheckBox;

	private ScaleDesignerDialog dialog;
	
	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(ScaleDesignerDialog dialog) {
		this.dialog = dialog;
	}
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	/**
	 * Create the panel.
	 */
	public ScaleDesignerPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblScaleProperties = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.scaleProperties"));
		add(lblScaleProperties, "4, 2");
		
		JSeparator separator = new JSeparator();
		add(separator, "4, 4, 7, 1");
		
		JLabel lblSteps = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.steps"));
		add(lblSteps, "4, 6");
		
		stepsSpinner = new JSpinner();
		add(stepsSpinner, "6, 6");
		
		JLabel lblInnerRadius = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.innerRadius"));
		add(lblInnerRadius, "8, 6");
		
		innerRadiusSpinner = new JSpinner();
		add(innerRadiusSpinner, "10, 6");
		
		JLabel lblStartOffsetAngle = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.startOffsetAngle"));
		add(lblStartOffsetAngle, "4, 8");
		
		startOffsetSpinner = new JSpinner();
		add(startOffsetSpinner, "6, 8");
		
		JLabel lblOuterRadius = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.outerRadius"));
		add(lblOuterRadius, "8, 8");
		
		outerRadiusSpinner = new JSpinner();
		add(outerRadiusSpinner, "10, 8");
		
		JLabel lblScaleAngle = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.scaleAngle"));
		add(lblScaleAngle, "4, 10");
		
		scaleAngleSpinner = new JSpinner();
		add(scaleAngleSpinner, "6, 10");
		
		JLabel lblStartValue = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.startValue"));
		add(lblStartValue, "8, 10");
		
		startValueSpinner = new JSpinner();
		add(startValueSpinner, "10, 10");
		
		JLabel lblLineThickness = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.lineThickness"));
		add(lblLineThickness, "4, 12");
		
		lineThicknessSpinner = new JSpinner();
		add(lineThicknessSpinner, "6, 12");
		
		JLabel lblRange = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.range"));
		add(lblRange, "8, 12");
		
		rangeSpinner = new JSpinner();
		add(rangeSpinner, "10, 12");
		
		JLabel lblShowCaption = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.showCaption"));
		add(lblShowCaption, "4, 14");
		
		captionCheckBox = new JCheckBox();
		add(captionCheckBox, "6, 14");
		
		JLabel lblSubsteps = new JLabel(resources.getResourceByKey("ScaleDesignerPanel.showSubsteps"));
		add(lblSubsteps, "8, 14");
		
		subStepsCheckBox = new JCheckBox();
		add(subStepsCheckBox, "10, 14");
		
		JSeparator separator_1 = new JSeparator();
		add(separator_1, "4, 16, 7, 1");

	}

	public JSpinner getStepsSpinner() {
		return stepsSpinner;
	}
	public JSpinner getStartOffsetSpinner() {
		return startOffsetSpinner;
	}
	public JSpinner getScaleAngleSpinner() {
		return scaleAngleSpinner;
	}
	public JSpinner getLineThicknessSpinner() {
		return lineThicknessSpinner;
	}
	public JSpinner getInnerRadiusSpinner() {
		return innerRadiusSpinner;
	}
	public JSpinner getOuterRadiusSpinner() {
		return outerRadiusSpinner;
	}
	public JCheckBox getCaptionCheckBox() {
		return captionCheckBox;
	}
	public JSpinner getStartValueSpinner() {
		return startValueSpinner;
	}
	public JSpinner getRangeSpinner() {
		return rangeSpinner;
	}
	public JCheckBox getSubStepsCheckBox() {
		return subStepsCheckBox;
	}
	
	public void initDataBindings() {
		
		BindingGroup bindingGroup = new BindingGroup();
		
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, startValueSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("startValue")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, stepsSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("steps")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, captionCheckBox,
				BeanProperty.create("selected"), dialog, BeanProperty.create("showCaption")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, startOffsetSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("offsetStartAngle")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, innerRadiusSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("innerSize")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, outerRadiusSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("outerSize")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, scaleAngleSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("scaleAngle")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, lineThicknessSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("thickness")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, rangeSpinner,
				BeanProperty.create("value"), dialog, BeanProperty.create("range")));
		bindingGroup.addBinding(Bindings.createAutoBinding(READ, subStepsCheckBox,
				BeanProperty.create("selected"), dialog, BeanProperty.create("showSubSteps")));		
		
		bindingGroup.bind();	
	}
}
