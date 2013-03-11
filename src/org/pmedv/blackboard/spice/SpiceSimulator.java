package org.pmedv.blackboard.spice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.pmedv.blackboard.provider.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpiceSimulator", propOrder = { "path", "parameters", "defaultSimulator","type" })
public class SpiceSimulator extends BaseElement {

	public enum SimulatorType {
		INTERNAL, 
		EXTERNAL
	}
	
	private String path;
	private String parameters;
	private boolean defaultSimulator = false;
	private SimulatorType type;
	
	public SpiceSimulator() {
		this.type = SimulatorType.EXTERNAL;
	}

	public SpiceSimulator(String name, String path, String parameters) {
		super();
		this.name = name;
		this.path = path;
		this.parameters = parameters;
		this.type = SimulatorType.EXTERNAL;
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
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the defaultSimulator
	 */
	public boolean isDefaultSimulator() {
		return defaultSimulator;
	}

	/**
	 * @param defaultSimulator the defaultSimulator to set
	 */
	public void setDefaultSimulator(boolean defaultSimulator) {
		this.defaultSimulator = defaultSimulator;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj != null && obj instanceof SpiceSimulator) {
			if (this == obj) {
				return true;				
			}
			SpiceSimulator other = (SpiceSimulator)obj;
			return other.name.equals(this.name);			
		}

		return false;
		
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * @return the type
	 */
	public SimulatorType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SimulatorType type) {
		this.type = type;
	}
	
	
}
