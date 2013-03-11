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
package org.pmedv.core.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import org.pmedv.core.components.GradientPanel;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * <p>
 * This is an abstract base class for a nice Dialog, with a title, a subtitle, an icon
 * and a nice gradient header. Optionally any combination of the three buttons "New","ok" and "cancel"
 * can be turned on.Implementors must implement <code>initializeComponents</code>
 * in order to provide the dialog contents. 
 * </p>
 * Additionally an user object of any class can be provided. The object should be modified
 * through this dialog by the user. Once the user has finished editing, the user object should be set.
 * </p>
 * <p>
 * Implementors should also provide ActionListeners for the according buttons.
 * <p>
 * @author Matthias Pueski
 *
 */
public abstract class AbstractNiceDialog extends JDialog {
	
	private static final long serialVersionUID = -7552896221455776854L;
	
	protected static final ApplicationContext ctx = AppContext.getContext();
	protected static final ResourceService resources = ctx.getBean(ResourceService.class);
	protected static final ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
	
	private JButton    newButton;
	private JButton    okButton;
	private JButton    cancelButton;
	private JPanel     contentPanel;
	private JPanel 	   buttonPanel;
	
	private JLabel titleLabel;
	private JLabel subTitleLabel;
	
	protected int result;
	
	public static final int OPTION_CANCEL = 0;
	public static final int OPTION_OK = 1;
	
	public int actionResult = 0;
	
	private Icon newIcon;
	private Icon okIcon;
	private Icon cancelIcon;
	
	private Object userObject;

	public AbstractNiceDialog(String title,String subTitle,ImageIcon icon, boolean modal) {
		
		this(title,subTitle,icon,true,false,true,true);

		newIcon    = resources.getIcon("icon.newdocument");
		okIcon     = resources.getIcon("icon.apply");
		cancelIcon = resources.getIcon("icon.cancel");
		
		getRootPane().setDefaultButton(okButton);
		
	}
	
	public AbstractNiceDialog(String title, String subTitle, ImageIcon icon,boolean modal,boolean showNewButton,boolean showOkButton, boolean showCancelButton) {

		setTitle(title);
	
		newIcon    = resources.getIcon("icon.newdocument");
		okIcon     = resources.getIcon("icon.apply");
		cancelIcon = resources.getIcon("icon.cancel");
		
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(createHeader(title,subTitle,icon),BorderLayout.NORTH);
		contentPanel.add(createButtonBar(showNewButton,showOkButton,showCancelButton),BorderLayout.SOUTH);
		
		add(contentPanel);		
		initializeComponents();
		centerAndAdjustSize();
		setModal(modal);
		getRootPane().setDefaultButton(okButton);
	}
	
	public AbstractNiceDialog(String title, String subTitle, ImageIcon icon,boolean modal,boolean showNewButton,boolean showOkButton, boolean showCancelButton, JFrame frame,Object object) {

		super(frame);
		
		setTitle(title);

		newIcon    = resources.getIcon("icon.newdocument");
		okIcon     = resources.getIcon("icon.apply");
		cancelIcon = resources.getIcon("icon.cancel");
		
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(createHeader(title,subTitle,icon),BorderLayout.NORTH);
		contentPanel.add(createButtonBar(showNewButton,showOkButton,showCancelButton),BorderLayout.SOUTH);
		add(contentPanel);

		this.userObject = object;
		
		initializeComponents();
		centerAndAdjustSize();
		setModal(modal);
		getRootPane().setDefaultButton(okButton);

	}
	
	/**
	 * This is the main init method of the Dialog, place your components inside here.
	 */
	protected abstract void initializeComponents();
	
	private JPanel createHeader(String title, String subTitle, ImageIcon icon) {

		int headerHeight = 50;

		FormLayout formLayout = new FormLayout(
				"8dlu, 10dlu, fill:pref:grow, 7dlu, pref, 7dlu",
				"4dlu, 13dlu,max(" + (headerHeight - 17)
						+ "dlu;default),default");
		
		Color blue = new Color(188, 203, 234);
		GradientPanel header = new GradientPanel(formLayout,Color.WHITE,blue);
		header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		header.setBackground(Color.white);

		titleLabel = new JLabel(title);
		subTitleLabel = new JLabel(subTitle);
		JLabel iconLabel = (icon == null ? new JLabel() : new JLabel(icon));

		titleLabel.setForeground(Color.black);
		subTitleLabel.setForeground(Color.black);

		titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
		subTitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		CellConstraints cc = new CellConstraints();
		header.add(titleLabel, cc.xywh(2, 2, 2, 1));
		header.add(subTitleLabel, cc.xywh(2, 3, 2, 1));
		header.add(iconLabel, cc.xywh(5, 1, 1, 3, "right,center"));

		return (header);

	}
	
	/**
	 * Creates the button bar for this dialog.
	 * 
	 * @param showNewButton    
	 * @param showOkButton
	 * @param showCancelButton
	 * 
	 * @return the button bar
	 */
	private JPanel createButtonBar(boolean showNewButton, boolean showOkButton, boolean showCancelButton) {
		
		buttonPanel = new JPanel(new FlowLayout());

		if (showNewButton) {
			newButton = new JButton(resources.getResourceByKey("button.new"));
			newButton.setIcon(newIcon);
			buttonPanel.add(newButton);			
		}
		if (showOkButton) {
			okButton = new JButton(resources.getResourceByKey("button.ok"));
			okButton.setIcon(okIcon);
			buttonPanel.add(okButton);			
		}
		if (showCancelButton) {
			cancelButton = new JButton(resources.getResourceByKey("button.cancel"));	
			cancelButton.setIcon(cancelIcon);
			buttonPanel.add(cancelButton);			
		}
		
		return buttonPanel;
	}
	
	private void centerAndAdjustSize() {
		setLocationRelativeTo(getRootPane());		
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#createRootPane()
	 */
	protected JRootPane createRootPane() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				
				result = OPTION_CANCEL;
				
				setVisible(false);
			}
		};
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
	
	public JPanel getContentPanel() {
		return contentPanel;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getOkButton() {
		return okButton;
	}
	
	public JButton getNewButton() {
		return newButton;
	}
	
	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}
	
	/**
	 * @return the userObject
	 */
	public Object getUserObject() {
		return userObject;
	}

	/**
	 * @param userObject the userObject to set
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	/**
	 * @return the titleLabel
	 */
	public JLabel getTitleLabel() {
		return titleLabel;
	}

	/**
	 * @return the subTitleLabel
	 */
	public JLabel getSubTitleLabel() {
		return subTitleLabel;
	}

}
