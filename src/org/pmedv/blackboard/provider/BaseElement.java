package org.pmedv.blackboard.provider;

public abstract class BaseElement {

	protected String name;
	protected String filename;
	
	public BaseElement() {
		super();
	}

	public BaseElement(String name) {
		super();
		this.name = name;
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
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof BaseElement) {
			BaseElement el = (BaseElement)obj;
			return el.getName().equals(name);
		}
		return false;
	}
	
}
