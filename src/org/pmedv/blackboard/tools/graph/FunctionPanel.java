package org.pmedv.blackboard.tools.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JPanel;

public class FunctionPanel extends JPanel {

	public enum Mode {
		POINTS, LINE, BOTH
	}

	int deltaX;
	int deltaY;

	int pressX;
	int pressY;

	double xOffset = 0;
	double yOffset = 0;

	double xOrigin = 0;
	double yOrigin = 0;

	double scale = 1.0;

	private int width;
	private int height;

	private int maxvalue = 1000;

	private int gridsize = 25;

	private Stroke defaultStroke = new BasicStroke(0.7f);
	private Stroke axisStroke = new BasicStroke(1.2f);

	private final LinkedList<Graph> graphs = new LinkedList<Graph>();

	private Mode mode = Mode.POINTS;

	public FunctionPanel(int width, int height) {

		super(null);

		this.width = width;
		this.height = height;

		setBackground(Color.WHITE);

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				if (scale + e.getWheelRotation() >= 1) {
					scale += e.getWheelRotation();
					invalidate();
					repaint();
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				handleMouseDragged(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseReleased(e);
			}
		});

		addComponentListener(new ComponentAdapter() {
			
			
			@Override
			public void componentResized(ComponentEvent e) {
				JPanel p = (JPanel)e.getComponent();
				FunctionPanel.this.width = p.getWidth();
				FunctionPanel.this.height = p.getHeight();
				FunctionPanel.this.invalidate();
				FunctionPanel.this.repaint();
			}
			
		});
	}

	protected void handleMousePressed(MouseEvent e) {
		pressX = e.getX();
		pressY = e.getY();
	}

	protected void handleMouseReleased(MouseEvent e) {
		pressX = 0;
		pressY = 0;
		xOrigin += xOffset;
		yOrigin += yOffset;
		xOffset = 0;
		yOffset = 0;

	}

	private void handleMouseDragged(MouseEvent e) {
		deltaX = e.getX() - pressX;
		deltaY = e.getY() - pressY;

		xOffset = deltaX / scale;
		yOffset = -deltaY / scale;

		invalidate();
		repaint();

	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		HashMap<Key, Object> hints = new HashMap<Key, Object>();
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHints(hints);

		AffineTransform at = new AffineTransform();
		at.scale(1, -1);
		at.translate(width / 2, -height / 2);
		at.scale(scale, scale);

		at.translate(xOrigin + xOffset, yOrigin + yOffset);

		g2d.setTransform(at);

		g2d.setColor(Color.LIGHT_GRAY);

		g2d.setStroke(defaultStroke);

		// xgrid
		for (int y = -maxvalue; y < maxvalue; y += gridsize) {
			g2d.drawLine(-maxvalue, y, maxvalue, y);
		}

		// ygrid
		for (int x = -maxvalue; x < maxvalue; x += gridsize) {
			g2d.drawLine(x, maxvalue, x, -maxvalue); // yaxis
		}

		g2d.setColor(Color.BLUE);
		g2d.setStroke(axisStroke);
		g2d.drawLine(-maxvalue, 0, maxvalue, 0); // xaxis
		g2d.drawLine(0, maxvalue, 0, -maxvalue); // yaxis

		g2d.setStroke(defaultStroke);

		for (Graph graph : graphs) {

			g2d.setColor(graph.getColor());
			
			Point[] points = graph.getPoints();
			
			if (mode.equals(Mode.LINE)) {
				for (int p = 0; p < points.length - 1; p++) {
					g2d.drawLine(points[p].x, points[p].y, points[p + 1].x, points[p + 1].y);
				}
			} else if (mode.equals(Mode.POINTS)) {
				for (int p = 0; p < points.length; p++) {
					g2d.fillOval(points[p].x, points[p].y, 1, 1);
				}
			} else {
				for (int p = 0; p < points.length - 1; p++) {
					g2d.setColor(graph.getColor());
					g2d.drawLine(points[p].x, points[p].y, points[p + 1].x, points[p + 1].y);
					g2d.setColor(Color.RED);
					g2d.fillOval(points[p].x - 2, points[p].y - 2, 4, 4);
				}
				g2d.fillOval(points[points.length - 1].x - 2, points[points.length - 1].y - 2, 4, 4);
			}
			
			
		}
		

	}

	/**
	 * @return the maxvalue
	 */
	public int getMaxvalue() {
		return maxvalue;
	}

	/**
	 * @param maxvalue
	 *            the maxvalue to set
	 */
	public void setMaxvalue(int maxvalue) {
		this.maxvalue = maxvalue;
	}

	/**
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
		invalidate();
		repaint();
	}

	/**
	 * @return the gridsize
	 */
	public int getGridsize() {
		return gridsize;
	}

	/**
	 * @param gridsize
	 *            the gridsize to set
	 */
	public void setGridsize(int gridsize) {
		this.gridsize = gridsize;
	}
	
	public void addGraph(Graph graph) {
		graphs.add(graph);
		invalidate();
		repaint();
	}

}
