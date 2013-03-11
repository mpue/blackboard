/**

	BlackBoard breadboard designer
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

package org.pmedv.blackboard.panels;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.pmedv.blackboard.components.BoardEditor;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */
@SuppressWarnings("serial")
public class CenterPanel extends JPanel {
	
	private BoardEditor boardEditor;
	
	public CenterPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		centerPanel = new JPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow, $lcgap, default, $lcgap, default:grow",
			"default:grow, $lgap, default, $lgap, default:grow"));

		//======== centerPanel ========
		{
			centerPanel.setLayout(new FlowLayout());
		}
		add(centerPanel, cc.xy(3, 3));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel centerPanel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	
	/**
	 * @return the centerPanel
	 */
	public JPanel getCenterPanel() {
	
		return centerPanel;
	}

	
	/**
	 * @return the boardEditor
	 */
	public BoardEditor getBoardEditor() {
	
		return boardEditor;
	}

	
	/**
	 * @param boardEditor the boardEditor to set
	 */
	public void setBoardEditor(BoardEditor boardEditor) {
	
		this.boardEditor = boardEditor;
	}



}
