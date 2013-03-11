package org.pmedv.blackboard.commands;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.dialogs.EditPartDialog;
import org.pmedv.blackboard.dialogs.PartDialog;
import org.pmedv.blackboard.tools.PartFactory;
import org.pmedv.core.commands.AbstractCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.dialogs.AbstractNiceDialog;
import org.pmedv.core.util.ErrorUtils;

public class CreatePartCommand extends AbstractCommand {

	@Override
	public void execute(ActionEvent e) {

		String title = resources.getResourceByKey("CreatePartCommand.name");
		String subTitle = resources.getResourceByKey("CreatePartCommand.dialog.subtitle");
		
		EditPartDialog dialog = new EditPartDialog(title, subTitle, null, null);
		dialog.setVisible(true);

		// cancel clicked, simply return 
		if (dialog.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
			return;
		}
		
		Part part = dialog.getPart();
		
		// setup directories
		
		File workDir = new File(".");
		File partDir = new File(workDir + "/parts/");
		File imageDir = new File(workDir + "/parts/images/");
		File outputPart = new File(partDir+"/"+part.getName()+".xml");
		
		// copy and open image

		String pathSeparator = System.getProperty("file.separator");
		
		String imageSourcePath = dialog.getEditPartPanel().getFileBrowserTextField().getPathField().getText();
		String imageFileName = imageSourcePath.substring(imageSourcePath.lastIndexOf(pathSeparator) + 1);
		
		BufferedImage bi = null;
		
		try {
			FileUtils.copyFile(new File(imageSourcePath), new File(imageDir,imageFileName));		
			bi = ImageIO.read(new File(imageDir,imageFileName));
		} 
		catch (IOException e2) {
			// could not copy or read image, show error and return 
			ErrorUtils.showErrorDialog(e2);
			return;
		}

		// create according part
		part.setFilename(part.getName()+".xml");
		part.setImage(bi);
		part.setIndex(0);
		part.setLayer(0);
		part.setColor(Color.WHITE);
		part.setImageName(imageFileName);
		part.setXLoc(0);
		part.setYLoc(0);
		part.setValue("");
		part.setResizable(false);
		part.setRotation(0);
		part.setOpacity(1.0f);
		
		// marshall to xml file
		
		StringWriter sw = new StringWriter();
		
		try {
			Marshaller m = AppContext.getContext().getBean(PartFactory.class).getMarshaller();
			m.marshal(part, new FileOutputStream(outputPart));
			m.marshal(part, sw);

		}
		catch (Exception e1) {
			ErrorUtils.showErrorDialog(e1);
			return;
		}

		part.setXmlContent(sw.toString());
		
		AppContext.getContext().getBean(PartDialog.class).getModel().addPart(part);		
		
	}
	
}
