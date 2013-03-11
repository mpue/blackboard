/**

	BlackBoard BreadBoard Designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
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
package org.pmedv.core.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class GradientPanel extends JPanel {

	private static final long serialVersionUID = -2102194843247135579L;

	private Color startColor;
	private Color endColor;

	public GradientPanel() {
		this(Color.GRAY, Color.WHITE);
	}
	
	public GradientPanel(LayoutManager mgr,Color startColor, Color endColor) {
		super(mgr);
		this.startColor = startColor;
		this.endColor = endColor;
	}

	public GradientPanel(Color startColor, Color endColor) {
		super();
		this.startColor = startColor;
		this.endColor = endColor;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int panelHeight = getHeight();
		int panelWidth = getWidth();
		GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, panelWidth, panelHeight, endColor);
		if (g instanceof Graphics2D) {
			Graphics2D graphics2D = (Graphics2D) g;
			graphics2D.setPaint(gradientPaint);
			graphics2D.fillRect(0, 0, panelWidth, panelHeight);
		}
	}
}
