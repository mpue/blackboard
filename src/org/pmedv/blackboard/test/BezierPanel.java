package org.pmedv.blackboard.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class BezierPanel extends JPanel {

	private ArrayList<Point2D.Double> controlPoints = new ArrayList<Point2D.Double>();
	
	BasicStroke stroke = new BasicStroke(1.0f);
	
	Point2D.Double selectedPoint = null;
	
	Point2D.Double P1 = new Point2D.Double(50, 75);
	Point2D.Double P2 = new Point2D.Double(150, 75);
	Point2D.Double ctrl1 = new Point2D.Double(80, 25);
	Point2D.Double ctrl2 = new Point2D.Double(80, 100);

	
	public BezierPanel() {
		super(null);
		setBackground(Color.WHITE);
		addMouseListener(new PanelMouseAdapter());
		addMouseMotionListener(new PanelMouseAdapter());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		setBackground(Color.WHITE);
		g.setColor(Color.WHITE);
		// clear all
		g.clearRect(0, 0, getWidth(), getHeight());
		// some nice anti aliasing...
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(rh);
		g2.setColor(Color.BLACK);
		g2.setStroke(stroke);
		controlPoints.add(ctrl1);
		controlPoints.add(ctrl2);
		controlPoints.add(P1);
		controlPoints.add(P2);

		
		g2.fillOval((int)P1.x, (int)P1.y, 4,4);
		g2.fillOval((int)P2.x, (int)P2.y, 4,4);
		
		g2.setColor(Color.RED);
		
		g2.fillOval((int)ctrl1.x, (int)ctrl1.y, 4,4);
		g2.fillOval((int)ctrl2.x, (int)ctrl2.y, 4,4);
		
		g2.setColor(Color.BLACK);
		
		CubicCurve2D.Double curve = new CubicCurve2D.Double( P1.x, P1.y, ctrl1.x, ctrl1.y, ctrl2.x, ctrl2.y, P2.x, P2.y);
		g2.draw(curve);
		g2.draw(curve.getBounds());
		
	}
	
	class PanelMouseAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			for (Point2D.Double point : controlPoints) {
				if ((e.getX() >= point.x - 5 && e.getX() <= point.x + 5) &&
					(e.getY() >= point.y - 5 && e.getY() <= point.y + 5)) {				
					selectedPoint = point;
					break;					
				}
				else{ 
					selectedPoint = null;					
				}			
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			
			if (selectedPoint != null) {
				selectedPoint.x = e.getX();
				selectedPoint.y = e.getY();
				invalidate();
				repaint();
				
			}
			
		}
	}
	
}
