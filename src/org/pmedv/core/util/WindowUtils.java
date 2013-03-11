package org.pmedv.core.util;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JWindow;


public class WindowUtils {

	public static void center(JWindow window) {

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle r = window.getBounds();

		boolean runningWindows = false;
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int numOfDisplays = env.getScreenDevices().length;

		String OS = System.getProperty("os.name").toLowerCase();

		if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows") > -1)) {
			runningWindows = true;
		} 

		int xLoc = 0;
		int yLoc = (screen.height - r.height) / 2;

		if (numOfDisplays == 2 && !runningWindows) { // Windows gives the wrong screen size when running on multiple displays
			
			GraphicsDevice[] device = env.getScreenDevices();			
			DisplayMode mode = device[0].getDisplayMode();
			
			xLoc = (mode.getWidth() - r.width) / 2;
		} 
		else {
			xLoc = (screen.width - r.width) / 2;
		}

		window.setLocation(xLoc, yLoc);
	}
	
	public static Dimension getDefaultScreenSize() {

		Dimension size = new Dimension();
		
		boolean runningWindows = false;
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int numOfDisplays = env.getScreenDevices().length;

		String OS = System.getProperty("os.name").toLowerCase();

		if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows") > -1)) {
			runningWindows = true;
		} 

		GraphicsDevice[] device = env.getScreenDevices();			
		DisplayMode mode = device[0].getDisplayMode();
		
		if (numOfDisplays == 2 && !runningWindows) { // Windows gives the wrong screen size when running on multiple displays
			size.width = mode.getWidth();
			size.height = mode.getHeight();
		}
		
		else {
			size.width = mode.getWidth()/2;
			size.height = mode.getHeight();			
		}

		return size;
	}
	
}
