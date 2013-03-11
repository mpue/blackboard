package org.pmedv.blackboard.app;

import org.pmedv.blackboard.components.BoardEditor;

public class UnsavedResource {

	private String name;
	private String path;
	private Boolean save;
	private BoardEditor editor;

	public UnsavedResource(BoardEditor editor) {
		
		if (editor.getCurrentFile() != null) {
			this.name = editor.getCurrentFile().getName();
			this.path = editor.getCurrentFile().getAbsolutePath();			
		}
		else {
			this.name = editor.getView().getViewProperties().getTitle();
		}
		
		this.save = true;
		this.editor = editor;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the save
	 */
	public Boolean getSave() {
		return save;
	}
	/**
	 * @param save the save to set
	 */
	public void setSave(Boolean save) {
		this.save = save;
	}

	/**
	 * @return the editor
	 */
	public BoardEditor getEditor() {
		return editor;
	}
	
	
	
}
