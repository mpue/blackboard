package org.pmedv.blackboard.components;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Connections", propOrder = { "pin" })

public class Connections {

	private ArrayList<Pin> pin;
	
	public Connections() {
		pin = new ArrayList<Pin>();
	}

	/**
	 * @return the pins
	 */
	public ArrayList<Pin> getPin() {
		return pin;
	}

	/**
	 * @param pins the pins to set
	 */
	public void setPin(ArrayList<Pin> pins) {
		this.pin = pins;
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("connections : [");		
		int count = 0;
		for (Pin p : pin) {
			s.append(p);
			if (count< pin.size() - 1)
				s.append(", ");
			count++;
		}		
		s.append("]");
		return s.toString();
	}
}
