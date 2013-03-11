package org.pmedv.blackboard.spice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.provider.BaseElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Model", propOrder = {  "name","content", "type" })

public class Model extends BaseElement {
	
	private String content;
	private SpiceType type;
	private String name;
	
	@XmlTransient
	private boolean shouldImport = false;
	
	public Model() {		
	}
	
	public Model(String name, String content, SpiceType type) {
		super();
		this.name = name;
		this.content = content;
		this.type = type;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the type
	 */
	public SpiceType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(SpiceType type) {
		this.type = type;
	}
	
	/**
	 * @return the shouldImport
	 */
	public boolean isShouldImport() {
		return shouldImport;
	}

	/**
	 * @param shouldImport the shouldImport to set
	 */
	public void setShouldImport(boolean shouldImport) {
		this.shouldImport = shouldImport;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj != null && obj instanceof Model) {
			if (this == obj) {
				return true;				
			}
			Model other = (Model)obj;
			if (other.name == null || name == null)
				return false;
			return other.name.equals(this.name);			
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuffer data = new StringBuffer();
		data.append("SPICE model : "+name);
		data.append("\n");
		data.append("TextType : "+type);
		data.append("\n");
		data.append(content);
		data.append("\n");
		return data.toString();
	}
	
	
}
