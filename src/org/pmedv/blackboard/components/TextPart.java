package org.pmedv.blackboard.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.pmedv.blackboard.TextUtil;
import org.pmedv.blackboard.beans.TextPartBean;

/**
 * @author Matthias Pueski (16.06.2011)
 * 
 */
public class TextPart extends Part {
	
	public enum TextType {
		TEXT,
		NAME,
		VALUE,
		SPICE_DIRECTIVE,
		CONTROL
	}
	
	private String text;
	private Font font;
	private TextType type;
	
	public TextPart(String text, Font font, Color c, int xLoc, int yLoc, int rotation) {
		this.text = text;
		this.setxLoc(xLoc);
		this.setyLoc(yLoc);
		this.oldXLoc = xLoc;
		this.oldYLoc = yLoc;
		this.color = c;
		this.image = TextUtil.createImageFromFont(text, font, color);
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.font = font;
		this.rotation = rotation;
		this.type = TextType.TEXT;
	}
	
	public TextPart(TextPartBean textBean) {
		this.text = textBean.getText();
		this.setxLoc(textBean.getXLoc());
		this.setyLoc(textBean.getYLoc());
		this.oldXLoc = getxLoc();
		this.oldYLoc = getyLoc();
		this.color = new Color(textBean.getColor().getRed(),textBean.getColor().getGreen(),textBean.getColor().getBlue());
		this.font = new Font(textBean.getFont(), Font.PLAIN, textBean.getFontsize());
		this.image = TextUtil.createImageFromFont(text, font, color);
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.name = textBean.getName();
		this.rotation = textBean.getRotation();
		this.type = textBean.getType();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {		
		TextPart textPart = new TextPart(text, font, color, getxLoc(), getyLoc(), rotation);
		textPart.setType(getType());
		return (textPart);
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		this.image = TextUtil.createImageFromFont(text, font, color);

		int ninetyDegreeSteps = getRotation() / 90;
		
		for (int i = 0; i < ninetyDegreeSteps;i++)
			rotateCW();
		
		this.width = image.getWidth();
		this.height = image.getHeight();			
	}
	
	public String toSVG() {
		// FontRenderContext erzeugen (das Bild ist nur da, um ein
		// Graphics-Objekt zu erhalten)
		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		bi.getGraphics().setColor(this.color);
		FontRenderContext frc = ((Graphics2D) bi.getGraphics()).getFontRenderContext();
		// PathIterator fuer die Glyphen erzeugen
		GlyphVector gv = font.createGlyphVector(frc, text);
		PathIterator p = gv.getOutline().getPathIterator(null);

		// BoundingBox holen (fuer die SVG-ViewBox)
		Rectangle2D r = gv.getLogicalBounds();

		// SVG-Kopf erzeugen
		String s = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n";
		s += "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1 Tiny//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd\">\n";
		s += "<svg version=\"1.1\" baseProfile=\"tiny\" xmlns=\"http://www.w3.org/2000/svg\" ";
		s += "width=\"21cm\" viewBox=\"" + r.getMinX() + " " + r.getMinY() + " " + r.getWidth() + " " + r.getHeight() + "\">\n";
		s += "<path fill=\"rgb("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")\"  d=\"";

		// einzelne Segmente des PathIterators in SVG-Kommandos umwandeln
		double[] d = new double[6];
		while (!p.isDone()) {
			int type = p.currentSegment(d);
			if (type == PathIterator.SEG_MOVETO)
				s += "M" + d[0] + "," + d[1] + " ";
			if (type == PathIterator.SEG_LINETO)
				s += "L" + d[0] + "," + d[1] + " ";
			if (type == PathIterator.SEG_CUBICTO)
				s += "C" + d[0] + "," + d[1] + " " + d[2] + "," + d[3] + " " + d[4] + "," + d[5] + " ";
			if (type == PathIterator.SEG_QUADTO)
				s += "Q" + d[0] + "," + d[1] + " " + d[2] + "," + d[3] + " ";
			if (type == PathIterator.SEG_CLOSE)
				s += "Z";
			p.next();
		}

		// SVG abschliessen
		s += "\"/>\n</svg>";
		
		return s;
	}
	
	@Override
	public int getWidth() {
		return image.getWidth();
	}
	
	@Override
	public int getHeight() {
		return image.getHeight();
	}
	
	@Override
	public void setColor(Color color) {
		super.setColor(color);
		this.color = color;
		this.image = TextUtil.createImageFromFont(text, font, color);
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	public TextType getType() {
		return type;
	}

	public void setType(TextType type) {
		this.type = type;
	}
	
	
}
