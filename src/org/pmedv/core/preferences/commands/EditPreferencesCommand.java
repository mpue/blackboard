package org.pmedv.core.preferences.commands;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.dialogs.DatasheetDialog;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.blackboard.dialogs.SpiceSimulatorManageDialog;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.preferences.dialogs.PreferencesDialog;
import org.pmedv.core.services.ResourceService;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;
//import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

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
			
			try {
				if (laf.equals("Nimbus")) {
					UIManager.setLookAndFeel(new NimbusLookAndFeel());
				  UIManager.put( "control", new Color( 128, 128, 128) );
				  UIManager.put( "info", new Color(128,128,128) );
				  UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
				  UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
				  UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
				  UIManager.put( "nimbusFocus", new Color(115,164,209) );
				  UIManager.put( "nimbusGreen", new Color(176,179,50) );
				  UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
				  UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
				  UIManager.put( "nimbusOrange", new Color(191,98,4) );
				  UIManager.put( "nimbusRed", new Color(169,46,34) );
				  UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
				  UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
				  UIManager.put( "text", new Color( 230, 230, 230) );
				}
				if (laf.equals("SkyBlue")) {
					Plastic3DLookAndFeel.setPlasticTheme(new SkyBluer());
					UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
					com.jgoodies.looks.Options.setPopupDropShadowEnabled(true);				
				}
				if (laf.equals("FlatDarcula")) {
					UIManager.setLookAndFeel(new FlatDarculaLaf());
				}
				else {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				
				ApplicationWindow win = AppContext.getBean(ApplicationWindow.class);			
				SwingUtilities.updateComponentTreeUI(win);
				SwingUtilities.updateComponentTreeUI(AppContext.getBean(PartDialog.class));
				SwingUtilities.updateComponentTreeUI(AppContext.getBean(SpiceSimulatorManageDialog.class));
				SwingUtilities.updateComponentTreeUI(AppContext.getBean(DatasheetDialog.class));
			
			}
			catch (Exception e2) {
				log.info("failed to set look and feel.");
			}
			
		}
		
		
		Boolean showTooltips = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.showTooltips");
		ToolTipManager.sharedInstance().setEnabled(showTooltips.booleanValue());

	}

}
