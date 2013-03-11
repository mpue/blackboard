package org.pmedv.core.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * @author Matthias Pueski (14.11.2010)
 *
 */

public class DotMatrixDisplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5903831884069470082L;
	
	
	public DotMatrixDisplay() {
		super();
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setBackground(Color.BLACK);
		g2d.setColor(Color.BLUE);
		
		g.fillOval(10, 10, 5, 5);
		
		
		
		
		super.paintComponent(g);
	}
	
	
}
