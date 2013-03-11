package org.pmedv.core.beans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecentFiles", propOrder = {		
    "recentFiles"
})
public class RecentFileList {
	
	public static final int MAX_RECENT_FILES = 5;
	
	private ArrayList<String> recentFiles;
	
	public RecentFileList() {
		recentFiles = new ArrayList<String>();
	}
	
	/**
	 * @return the recentFiles
	 */
	public ArrayList<String> getRecentFiles() {
		return recentFiles;
	}

	
	/**
	 * @param recentFiles the recentFiles to set
	 */
	public void setRecentFiles(ArrayList<String> recentFiles) {
		this.recentFiles = recentFiles;
	}
	

}
