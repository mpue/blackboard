/**

	BlackBoard breadboard designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2012 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 */
package org.pmedv.blackboard.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.components.Pin;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.dialogs.PinPropertiesDialog;
import org.pmedv.core.dialogs.AbstractNiceDialog;

/**
 * <p>
 * The symbol <code>SymbolPreviewPanel</code> displays a preview of
 * the selected symbol, and provides some basic editing behaviour such as 
 * adding and removing {@link Pin} objects.
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
@SuppressWarnings("serial")
public class SymbolPreviewPanel extends JPanel implements MouseMotionListener {

	private Symbol symbol;
	private final Point mousePosition;
	private final Point origin;
	private Pin currentPin = null;
	
	public SymbolPreviewPanel() {
		// no layout
		super(null);
		mousePosition = new Point();
		origin = new Point();
		setBackground(Color.WHITE);
		addMouseMotionListener(this);
		addMouseListener(new LocalMouseAdapter());
	}

	@Override
	protected void paintComponent(Graphics g) {
	
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		BoardUtil.drawGrid(g2d, 8, getWidth(), getHeight(), false);
		
		AffineTransform orig = g2d.getTransform();
		
		g2d.setColor(Color.BLUE);
		g2d.drawRect(BoardUtil.snap(mousePosition.x - 8,4), BoardUtil.snap(mousePosition.y - 8,4), 16, 16);
		
		if (symbol != null) {
			AffineTransform transform = new AffineTransform();
			origin.x = BoardUtil.snap((getWidth()  / 2 - symbol.getWidth()  / 2) - symbol.getxLoc(),16);
			origin.y = BoardUtil.snap((getHeight() / 2 - symbol.getHeight() / 2) - symbol.getyLoc(),16);
			transform.translate(origin.x,origin.y);
			g2d.transform(transform);
			symbol.draw(g2d,true,true,true,false,true);			
		}
		
		g2d.setTransform(orig);		
		g2d.setColor(Color.BLACK);
		
		if (symbol != null) {
			g2d.drawString(BoardUtil.snap(mousePosition.x - origin.x - symbol.getxLoc(),16) + "," +
					BoardUtil.snap(mousePosition.y - origin.y - symbol.getyLoc(),16), 10, 10);
						
		}
		if (currentPin != null) {
			g2d.drawString(currentPin.getNum()+" : "+currentPin.getName(), 10, 20);
		}
		
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition.setLocation(e.getX(), e.getY());		
		for (Pin p : symbol.getConnections().getPin()) {
			if (BoardUtil.snap(mousePosition.x - origin.x - symbol.getxLoc(),4) == p.getX() && 
				BoardUtil.snap(mousePosition.y - origin.y - symbol.getyLoc(),4) == p.getY()) {
				currentPin = p;
				break;
			}		
			else {
				currentPin = null;
			}
		}
		
		repaint();
	}

	private class LocalMouseAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			
			if (e.getButton() == MouseEvent.BUTTON1) {
				// no pin selected
				if (currentPin == null) {
					
					Pin pin = new Pin(BoardUtil.snap(mousePosition.x - origin.x - symbol.getxLoc(),4), 
									  BoardUtil.snap(mousePosition.y - origin.y - symbol.getyLoc(),4));					
					PinPropertiesDialog dlg = new PinPropertiesDialog("Add pin", "specify the new pin", null, pin);
					dlg.setVisible(true);
					
					if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
						return;
					}
									
					symbol.getConnections().getPin().add(pin);
					repaint();
				}
				// pin selected, pop up pin properties dialog
				else {
					PinPropertiesDialog dlg = new PinPropertiesDialog("Change pin", "change the properties of the pin", null, currentPin);

					dlg.setVisible(true);
					
					if (dlg.getResult() == AbstractNiceDialog.OPTION_CANCEL) {
						return;
					}
					
				}
			}
			else if (e.getButton() == MouseEvent.BUTTON3) {
				// selected a pin with the right button, remove it
				if (currentPin != null) {
					symbol.getConnections().getPin().remove(currentPin);
					currentPin = null;
					repaint();										
				}
			}
			
			
		}
	}
	
}
