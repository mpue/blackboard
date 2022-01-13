package org.pmedv.blackboard.commands;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.JFileChooser;

import net.infonode.docking.View;

import org.jdesktop.swingx.JXImageView;
import org.pmedv.core.commands.AbstractOpenEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.util.ErrorUtils;
import org.springframework.context.ApplicationContext;

public class OpenImageCommand extends AbstractOpenEditorCommand {

	private double scale;
	
	public OpenImageCommand() {
		putValue(Action.NAME, resources.getResourceByKey("OpenImageCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.openimage"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("OpenImageCommand.description"));
	}

	@Override
	public void execute(ActionEvent e) {

		final ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);

		String path = System.getProperty("user.home");
		if (AppContext.getLastSelectedFolder() != null) {
			path = AppContext.getLastSelectedFolder();
		}
		JFileChooser fc = new JFileChooser(path);
		fc.setDialogTitle(resources.getResourceByKey("OpenImageCommand.name"));
		int result = fc.showOpenDialog(win);
		if (result == JFileChooser.APPROVE_OPTION) {
			if (fc.getSelectedFile() == null)
				return;
			File file = fc.getSelectedFile();
			AppContext.setLastSelectedFolder(file.getParentFile().getAbsolutePath());

			final JXImageView imageView = new JXImageView();

			MediaTracker tracker = new MediaTracker(win);

			Image openedImage = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
			tracker.addImage(openedImage, 1);
			try {
				tracker.waitForAll();
			}
			catch (InterruptedException ie) {
				ErrorUtils.showErrorDialog(ie);
				return;
			}

			imageView.setImage(openedImage);
			imageView.setScale(1.0);
			
			imageView.addMouseWheelListener(new MouseWheelListener() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					int notches = e.getWheelRotation();
					
					scale += 0.1 * notches;
					
					if (scale < 0.1)
						scale = 0.1;
					if (scale > 4) 
						scale = 4;
					
					imageView.setScale(scale);
				}

			});
			
			View view = new View(file.getName(), null, imageView);
			openEditor(view, file.getName().hashCode());

		}
		else {
			return;
		}

	}

}
