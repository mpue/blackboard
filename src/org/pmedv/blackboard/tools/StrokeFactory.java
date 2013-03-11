package org.pmedv.blackboard.tools;

import java.awt.BasicStroke;
import java.awt.Stroke;

public class StrokeFactory {

	public static enum StrokeType {
		BASIC,
		HALF_DASHED,
		DASH_DOT
	}
	
	public static Stroke createStroke(float thickness, StrokeType type) {
		switch (type) {			
			case BASIC : 
				return new BasicStroke(thickness);
			case HALF_DASHED :
				return new BasicStroke(thickness,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER, 1.0f,
						new float[] { 8.0f, 4.0f, 8.0f, 4.0f },
						0.0f);
			case DASH_DOT :
				return new BasicStroke(thickness,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER, 1.0f,
						new float[] { 8.0f, 3.0f, 2.0f, 3.0f },
						0.0f);
				
			default : return new BasicStroke();
		
		}
	}
	
}
