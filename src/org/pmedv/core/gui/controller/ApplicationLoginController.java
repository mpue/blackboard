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
package org.pmedv.core.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.authentication.Authenticator;
import org.pmedv.core.dialogs.ApplicationLoginDialog;

/**
 * The ApplicationLoginController is responsible for the user 
 * authentication to the application server.
 * 
 * @author Matthias Pueski
 *
 */
public class ApplicationLoginController extends AbstractController {

	private static final Log log = LogFactory.getLog(ApplicationLoginController.class);
	
	private ApplicationLoginDialog loginDialog;

	private String username;
	private String password;	
	private boolean authenticated;
	private Authenticator authenticator;
	private ImageIcon offlineIcon;
	private ImageIcon onlineIcon;
	
	public ApplicationLoginController(Authenticator authenticator) {
		super();
		this.authenticator = authenticator;		
	}
	
	/**
	 * @return the authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @param authenticated the authenticated to set
	 */
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	@Override
	protected void initialize() {
		
		offlineIcon = resources.getIcon("icon.status.offline");
		onlineIcon = resources.getIcon("icon.status.online");

		ImageIcon loginIcon = resources.getIcon("icon.login");
		
		loginDialog = new ApplicationLoginDialog("Weberknecht CMSManager Login", "Please enter username and password.",loginIcon,window);
		
		loginDialog.getOkButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doLogin();		
				loginDialog.setVisible(false);
			}
			
		});
		
		loginDialog.getCancelButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				loginDialog.setVisible(false);
			}
			
		});
		
		LoginKeyAdapter keyAdapter = new LoginKeyAdapter();
		
		loginDialog.getOkButton().addKeyListener(keyAdapter);
		loginDialog.getLoginPanel().getUserNameField().addKeyListener(keyAdapter);
		loginDialog.getLoginPanel().getPasswordField().addKeyListener(keyAdapter);		
		loginDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);				
		loginDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				loginDialog.setVisible(false);
			}			
		});
		window.getStatusLabel().setIcon(offlineIcon);
	}
	
	public void prepareLogin() {
		loginDialog.setVisible(true);
	}

	public void doLogoff() {
		authenticated = false;
		username = null;
		password = null;
		window.getStatusLabel().setIcon(offlineIcon);
	}
	
	private void doLogin() {

		username = loginDialog.getLoginPanel().getUserNameField().getText();
		password = new String(loginDialog.getLoginPanel().getPasswordField().getPassword());
		
		try {
			if (authenticator.isAuthenticatable(username,password)) {
				authenticated = true;
				window.getStatusLabel().setIcon(onlineIcon);				
				
				Properties p = new Properties();
				
				try {
					
					InputStream is = getClass().getClassLoader().getResourceAsStream("remoting.properties");
					p.load(is);
					
					window.getCustomLabel().setText("Connected to : "+p.getProperty("host.name"));										
				}
				catch (IOException e) {
					log.error("Something went wrong whilst opening the remote properties file.");
				}

			}
			else { 
				authenticated = false;		
			}
		} 
		catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(window, e.getMessage());
		}
		
	}
	
	private class LoginKeyAdapter extends KeyAdapter{
			
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				doLogin();	
				loginDialog.setVisible(false);
			}
		
		}
	}
	
}
