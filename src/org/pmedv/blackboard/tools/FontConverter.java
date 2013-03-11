package org.pmedv.blackboard.tools;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;

public class FontConverter {
	public static void main(String[] args) {
		// die Schriftart
		Font f = new Font("Arial", Font.ITALIC + Font.BOLD, 32);
		// der Text
		String text = "Hallo Java-Forum!";
		// die Ausgabedatei
		String dateiname = "c:\\test.svg";

		// FontRenderContext erzeugen (das Bild ist nur da, um ein
		// Graphics-Objekt zu erhalten)
		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		FontRenderContext frc = ((Graphics2D) bi.getGraphics()).getFontRenderContext();

		// PathIterator fuer die Glyphen erzeugen
		GlyphVector gv = f.createGlyphVector(frc, text);
		PathIterator p = gv.getOutline().getPathIterator(null);

		// BoundingBox holen (fuer die SVG-ViewBox)
		Rectangle2D r = gv.getLogicalBounds();

		// SVG-Kopf erzeugen
		String s = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n";
		s += "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1 Tiny//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd\">\n";
		s += "<svg version=\"1.1\" baseProfile=\"tiny\" xmlns=\"http://www.w3.org/2000/svg\" ";
		s += "width=\"21cm\" viewBox=\"" + r.getMinX() + " " + r.getMinY() + " " + r.getWidth() + " " + r.getHeight() + "\">\n";
		s += "<path d=\"";

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

		// Datei speichern
		try {
			PrintWriter out = new PrintWriter(new FileWriter(dateiname));
			out.println(s);
			out.close();
		} catch (Exception e) {
		}

		// Ergebnis anzeigen
		System.out.println("SVG-Datei wurde geschrieben!\n\n" + s);
	}

}