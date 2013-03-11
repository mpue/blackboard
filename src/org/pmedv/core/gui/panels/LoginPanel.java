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
/*
 * Created by JFormDesigner on Tue Jul 07 08:41:27 CEST 2009
 */

package org.pmedv.core.gui.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Matthias Pueski
 */

public class LoginPanel extends JPanel {
	
	private static final long serialVersionUID = 3738701580611999984L;

	public LoginPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		usernameLabel = new JLabel();
		userNameField = new JTextField();
		label1 = new JLabel();
		passwordField = new JPasswordField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"$ugap, $lcgap, default, $lcgap, 66dlu, $lcgap, default:grow, $lcgap, default, $lcgap, $ugap",
			"fill:default:grow, 2*($lgap, default), $lgap, fill:default:grow"));

		//---- usernameLabel ----
		usernameLabel.setText("Username :");
		add(usernameLabel, cc.xy(5, 3));
		add(userNameField, cc.xywh(7, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

		//---- label1 ----
		label1.setText("Password :");
		add(label1, cc.xy(5, 5));
		add(passwordField, cc.xywh(7, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	/**
	 * @return the userNameField
	 */
	public JTextField getUserNameField() {
		return userNameField;
	}

	/**
	 * @return the passwordField
	 */
	public JPasswordField getPasswordField() {
		return passwordField;
	}


	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel usernameLabel;
	private JTextField userNameField;
	private JLabel label1;
	private JPasswordField passwordField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
