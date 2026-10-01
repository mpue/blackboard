package org.pmedv.core.preferences.commands;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.LookAndFeelUtil;
import org.pmedv.blackboard.dialogs.DatasheetDialog;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.blackboard.dialogs.SpiceSimulatorManageDialog;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.preferences.dialogs.PreferencesDialog;
import org.pmedv.core.services.ResourceService;


@SuppressWarnings("deprecation")
public class EditPreferencesCommand extends AbstractCommand {

	private static final Log log = LogFactory.getLog(EditPreferencesCommand.class);
	
	private static final long serialVersionUID = -467135265995605186L;
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);

	public EditPreferencesCommand() {
		putValue(Action.NAME, resources.getResourceByKey("EditPreferencesCommand.name"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("EditPreferencesCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));		
	}
	
	@Override
	public void execute(ActionEvent e) {

		ImageIcon icon = resources.getIcon("icon.dialog.preferences");

		String oldlaf = (String) Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.lookAndFeel");
		
		PreferencesDialog dlg = new PreferencesDialog(resources.getResourceByKey("title.dialog.preferences"),
				resources.getResourceByKey("subTitle.dialog.preferences"), icon, AppContext.getContext().getBean(ApplicationWindow.class));
		dlg.setVisible(true);

		
		String laf = (String) Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.lookAndFeel");

		if (!laf.equals(oldlaf)) {
			LookAndFeelUtil.applyLookAndFeel(laf);
				
			ApplicationWindow win = AppContext.getBean(ApplicationWindow.class);
			SwingUtilities.updateComponentTreeUI(win);
			SwingUtilities.updateComponentTreeUI(AppContext.getBean(PartDialog.class));
			SwingUtilities.updateComponentTreeUI(AppContext.getBean(SpiceSimulatorManageDialog.class));
			SwingUtilities.updateComponentTreeUI(AppContext.getBean(DatasheetDialog.class));
		}

		Boolean showTooltips = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showTooltips");
		ToolTipManager.sharedInstance().setEnabled(showTooltips.booleanValue());

	}

}
