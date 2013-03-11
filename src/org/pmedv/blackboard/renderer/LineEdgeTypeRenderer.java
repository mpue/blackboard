/**

	BlackBoard BreadBoard Designer
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
package org.pmedv.blackboard.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.pmedv.blackboard.components.LineEdgeType;

/**
 * The <code>LineEdgeTypeRenderer</code> renders the different available edges
 * of a line inside the {@link JComboBox} for the line start and line end type.
 * 
 * @author Matthias Pueski
 *
 */
public final class LineEdgeTypeRenderer extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = -8083866383250888479L;

	public static enum Direction {
		FROM_LEFT,
		FROM_RIGHT
	}
	
	private final Dimension size = new Dimension(32, 32);
	
	private final int iconWidth = 32;
	private final int iconHeight = 16;

	private static BasicStroke defaultStroke = new BasicStroke(1.5f);

	private final Map<Object, Icon> icons = new HashMap<Object, Icon>();

	public LineEdgeTypeRenderer(Direction direction) {
		icons.put(LineEdgeType.ROUND_DOT, createIcon(LineEdgeType.ROUND_DOT,direction));
		icons.put(LineEdgeType.SIMPLE_ARROW, createIcon(LineEdgeType.SIMPLE_ARROW,direction));
		icons.put(LineEdgeType.STRAIGHT, createIcon(LineEdgeType.STRAIGHT,direction));
	}

	private Icon createIcon(LineEdgeType type, Direction direction) {

		BufferedImage b = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) b.getGraphics();
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);

		g2d.setColor(Color.BLACK);
		g2d.setStroke(defaultStroke);

		switch (type) {
			case ROUND_DOT:
				g2d.drawLine(0, iconHeight / 2, iconWidth, iconHeight / 2);
				if (direction == Direction.FROM_LEFT) {
					g2d.fillOval(0, iconHeight / 4, iconHeight / 2, iconHeight / 2);
				}
				else {
					g2d.fillOval(iconWidth-iconHeight/2, iconHeight / 4, iconHeight / 2, iconHeight / 2);
				}
	
				break;
			case SIMPLE_ARROW:
				if (direction == Direction.FROM_LEFT) {
					g2d.drawLine(0, iconHeight / 2, iconWidth / 2, 0);
					g2d.drawLine(0, iconHeight / 2, iconWidth / 2, iconHeight);					
				}
				else {
					g2d.drawLine(iconWidth,iconHeight/2,iconWidth/2,0);
					g2d.drawLine(iconWidth,iconHeight/2,iconWidth/2,iconHeight);															
				}

				g2d.drawLine(0, iconHeight / 2, iconWidth, iconHeight / 2);
				break;
			case STRAIGHT:
				g2d.drawLine(0, iconHeight / 2, iconWidth, iconHeight / 2);
				break;
		}

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
		label.setText(null);
		label.setMinimumSize(size);
		label.setMaximumSize(size);
		if (!isSelected)
			label.setBackground(Color.WHITE);

		return label;
	}


}
