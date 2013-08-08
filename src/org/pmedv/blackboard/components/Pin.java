package org.pmedv.blackboard.components;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pin", propOrder = { "x", "y","num","name","orientation" })

public class Pin implements Comparable<Pin> {

	private int x;
	private int y;
	
	private Integer num = 0;
	private String name;
	private PinCaptionOrientation orientation;
	@XmlTransient
	private int netIndex;
	
	public Pin() {
		this.orientation = PinCaptionOrientation.BOTTOM_RIGHT;
	}
	
	public Pin(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
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
	 * @return the orientation
	 */
	public PinCaptionOrientation getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(PinCaptionOrientation orientation) {
		this.orientation = orientation;
	}

	public int getNetIndex() {
		return netIndex;
	}

	public void setNetIndex(int netIndex) {
		this.netIndex = netIndex;
	}

	@Override
	public String toString() {		
		return name+" "+num+" ("+x+","+y+")";
	}

	@Override
	public int compareTo(Pin o) {
		return num.compareTo(o.getNum());
	}
	
	
	
}
