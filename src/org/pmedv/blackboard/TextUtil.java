package org.pmedv.blackboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.pmedv.core.components.JLabelAA;

/**
 * @author Matthias Pueski (16.06.2011)
 * 
 */
public final class TextUtil {
	
	public static BufferedImage createImageFromFont(String s, Font f, Color c) {
		if (s == null)
			return null;

		JLabelAA label = new JLabelAA(s);
		FontMetrics fm = label.getFontMetrics(f);
		int height = fm.getHeight();
		int width = fm.stringWidth(s);

		label.setText("<html>" + s + "</html>");

		label.setSize(width, height);
		label.setFont(f);
		label.setForeground(c);

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		BufferedImage si;
		try {
			si = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
		catch (Exception e) {
			si = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
		Graphics2D g = (Graphics2D) si.getGraphics();
		g.setColor(c);
		label.paint(g);
		g.dispose();
		return si;
	}
	
}
