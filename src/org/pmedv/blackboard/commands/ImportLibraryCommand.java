package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JFileChooser;

import org.pmedv.blackboard.dialogs.ModelImportDialog;
import org.pmedv.blackboard.panels.ModelListPanel;
import org.pmedv.blackboard.provider.ModelProvider;
import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceUtil;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;

public class ImportLibraryCommand extends AbstractCommand {

	public ImportLibraryCommand() {
		putValue(Action.NAME, resources.getResourceByKey("ImportLibraryCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.importlibrary"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("ImportLibraryCommand.description"));
	}
	
	@Override
	public void execute(ActionEvent e) {
		
		ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);

		String path = System.getProperty("user.home");
		if (AppContext.getLastSelectedFolder() != null) {
			path = AppContext.getLastSelectedFolder();
		}

		final JFileChooser fc = new JFileChooser(path);
		
		fc.setDialogTitle(resources.getResourceByKey("ImportLibraryCommand.dialog.subtitle"));
		fc.setMultiSelectionEnabled(true);

		int result = fc.showOpenDialog(win);

		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}

		if (fc.getSelectedFiles() == null || fc.getSelectedFiles().length == 0)
			return;

		File[] selectedFiles = fc.getSelectedFiles();

		AppContext.setLastSelectedFolder(selectedFiles[0].getParentFile().getAbsolutePath());
		
		ArrayList<String> raw;
		try {
			ArrayList<Model> models = new ArrayList<Model>();
			
			for (File f : selectedFiles) {
				
				raw = SpiceUtil.readRawModelsFromLib(f);
				
				if (raw.size() > 0) {
					models.addAll(SpiceUtil.convertRawModels(raw));
				}
				
				ArrayList<String> subcircuits = SpiceUtil.readRawSubcircuitsFromLib(f);
				models.addAll(SpiceUtil.convertRawSubcircuits(subcircuits));
				
			}

			ModelImportDialog dlg = new ModelImportDialog(models);
			dlg.setVisible(true);

			if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
				return;
			}
			
			if (dlg.getModels().size() > 0) {
				SpiceUtil.importModels(dlg.getModels());
			}
			
			ModelProvider provider = AppContext.getContext().getBean(ModelProvider.class);
			provider.loadElements();
			AppContext.getContext().getBean(ModelListPanel.class).getModel().setModels(provider.getElements());
			
		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
		}

		
	}

}
