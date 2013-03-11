package org.pmedv.core.beans;

/**
 * The <code>FileObject</code> is needed in order to persist file path settings
 * into the Preferences.
 * 
 * @author Matthias Pueski
 * 
 */
public class FileObject {
	private String absolutePath;

	public FileObject(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
}
