package org.pmedv.core.beans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Files", propOrder = {		
    "files"
})
public class FileList {
	
	public static final int MAX_RECENT_FILES = 5;
	
	private ArrayList<String> files;
	
	public FileList() {
		files = new ArrayList<String>();
	}
	
	/**
	 * @return the files
	 */
	public ArrayList<String> getFiles() {
		return files;
	}

	
	/**
	 * @param Files the files to set
	 */
	public void setFiles(ArrayList<String> Files) {
		this.files = Files;
	}
	

}
