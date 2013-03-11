package org.pmedv.blackboard.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

public class PNGFilter extends FileFilter {

	protected static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		if ((f.getName().endsWith(".png") || 
			 f.getName().endsWith(".PNG"))) 
			return true;
		else
			return false;
	}

	@Override
	public String getDescription() {
		return resources.getResourceByKey("msg.filefilter.imgfiles")+"(*.png *.PNG)";
	}
}