package org.pmedv.test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.pmedv.core.components.DotMatrixDisplay;

/**
 * @author Matthias Pueski (14.11.2010)
 *
 */

public class DotMatrixTest {
	
	private static JFrame frame;
	private static DotMatrixDisplay display;
	
	
	public static void main(String[] args) {
		
		frame = new JFrame("DotMatrix");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension size = new Dimension(800,600);
		frame.setSize(size);		
		
		frame.setLayout(new BorderLayout());
		
		display = new DotMatrixDisplay();
		
		frame.add(display,BorderLayout.CENTER);
		
		
		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setVisible(true);

		
		
		
	}
	

}
