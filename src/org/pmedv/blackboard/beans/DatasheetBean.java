package org.pmedv.blackboard.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * A <code>Datasheetbean</code> is a container for a datasheet 
 * description. It holds a reference to a datasheet, which is
 * listed in a datasheet catalogue. It can also be attached to 
 * a specific part.
 * 
 * @author Matthias Pueski
 *
 */

@XmlType(propOrder = { "name", "description", "location" })
@XmlAccessorType(XmlAccessType.FIELD)
public class DatasheetBean implements Comparable<DatasheetBean> {

	// the name of the datasheet, should be the part name
	private String name;
	// a human readable description for a datasheet
	private String description;
	// the location of the datasheet file
	private String location;
	
	public DatasheetBean() {		
	}
	
	public DatasheetBean(String name, String description, String location) {
		this.name = name;
		this.description = description;
		this.location = location;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj != null) {
			
			if (obj.getClass().equals(this.getClass())) {
				
				DatasheetBean dsb = (DatasheetBean)obj;
				
				return (dsb.getName().equals(name) && 
						dsb.getDescription().equals(description) && 
						dsb.getLocation().equals(location));
				
			}
			
		}
		
		return false;
	}
	@Override
	public int compareTo(DatasheetBean o) {
		return o.getName().compareTo(name);
	}
	
}
