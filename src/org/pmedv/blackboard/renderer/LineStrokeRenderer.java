package org.pmedv.blackboard.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.pmedv.blackboard.tools.StrokeFactory;
import org.pmedv.blackboard.tools.StrokeFactory.StrokeType;

@SuppressWarnings("serial")
public class LineStrokeRenderer extends DefaultListCellRenderer {

	private static final int iconWidth = 64;
	private static final int iconHeight = 16;
	
	private final Dimension size = new Dimension(48, 32);

	private final Map<Stroke, Icon> icons = new HashMap<Stroke, Icon>();
	private final Map<Stroke, Float> values = new HashMap<Stroke, Float>();
	
	public LineStrokeRenderer() {
		
		for (Float f = 1.0f;f <= 10.0f;f+=1.0f) {
			Stroke s = StrokeFactory.createStroke(f, StrokeType.BASIC);			
			icons.put(s,createIcon(f, StrokeType.BASIC));
			values.put(s, f);
			
		}
		for (Float f = 1.0f;f <= 10.0f;f+=1.0f) {
			Stroke s = StrokeFactory.createStroke(f, StrokeType.HALF_DASHED);
			icons.put(s,createIcon(f, StrokeType.HALF_DASHED));
			values.put(s, f);
		}
		for (Float f = 1.0f;f <= 10.0f;f+=1.0f) {
			Stroke s = StrokeFactory.createStroke(f, StrokeType.DASH_DOT);
			icons.put(s,createIcon(f, StrokeType.DASH_DOT));
			values.put(s, f);
		}		
	}

	private Icon createIcon(Float thickness, StrokeType type) {

		BufferedImage b = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) b.getGraphics();
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(StrokeFactory.createStroke(thickness, type));
		g2d.drawLine(0, iconHeight / 2, iconWidth, iconHeight / 2);

		return new ImageIcon(b);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		// Get the renderer component from parent class

		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		// Get icon to use for the list item value

		Icon icon = icons.get(value);

		// Set icon to display for value

		label.setIcon(icon);
		label.setText(String.valueOf(values.get(value)));
		label.setMinimumSize(size);
		label.setMaximumSize(size);
		if (!isSelected)
			label.setBackground(Color.WHITE);

		return label;
	}	
	

}
