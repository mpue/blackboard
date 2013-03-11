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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import org.pmedv.core.components.ExtendedHTMLDocument;
import org.pmedv.core.components.ExtendedHTMLEditorKit;
import org.pmedv.core.util.StringUtil;

/**
 * This is the most important dialog of the system, it tells the user about the
 * criminal subject which is responsible for the whole mess.
 * 
 * @author Matthias Pueski
 * 
 */
@SuppressWarnings("unused")
public class AboutDialog extends AbstractNiceDialog {
	private static final long serialVersionUID = 1L;
	private JTextPane aboutPane;
	private JTextPane changelogPane;
	private JTextPane creditsPane;
	private ImageIcon splashIcon;
	private JTabbedPane tabPane;

	public AboutDialog(String title, String subTitle, ImageIcon icon, JFrame owner) {
		super(title, subTitle, icon, true, false, true, false, owner, null);
	}

	protected void initializeComponents() {
		Dimension size = new Dimension(550, 550);
		setSize(size);
		setBounds(new Rectangle(size));
		Properties properties = new Properties();
		try {			
			properties.load(ClassLoader.getSystemResourceAsStream("application.properties"));
			BufferedImage splash = ImageIO.read(ClassLoader.getSystemResourceAsStream(properties.getProperty("splash")));
			splashIcon = new ImageIcon(splash);
		}
		catch (IOException e1) {
			System.err.println("Could not add splash image to dialog.");
		}
		
		String version  = properties.getProperty("version");
		
		File f = new File("build.number");		
		properties = new Properties();
		
		try {
			properties.load(new FileReader(f));
		}
		catch (IOException e1) {
			properties.setProperty("build.number", "00");
		}
 
		String buildNumber = properties.getProperty("build.number");

		
		aboutPane = new JTextPane();
		ExtendedHTMLEditorKit aboutPaneEditorKit = new ExtendedHTMLEditorKit();
		aboutPane.setEditorKit(aboutPaneEditorKit);
		ExtendedHTMLDocument aboutDoc;
		aboutDoc = (ExtendedHTMLDocument) aboutPaneEditorKit.createDefaultDocument();
		changelogPane = new JTextPane();		
		ExtendedHTMLEditorKit changelogPanePaneEditorKit = new ExtendedHTMLEditorKit();
		changelogPane.setEditorKit(changelogPanePaneEditorKit);
		ExtendedHTMLDocument changelogDoc;
		changelogDoc = (ExtendedHTMLDocument) changelogPanePaneEditorKit.createDefaultDocument();
		creditsPane = new JTextPane();		
		ExtendedHTMLEditorKit creditsPanePaneEditorKit = new ExtendedHTMLEditorKit();
		creditsPane.setEditorKit(creditsPanePaneEditorKit);
		ExtendedHTMLDocument creditsDoc;
		creditsDoc = (ExtendedHTMLDocument) creditsPanePaneEditorKit.createDefaultDocument();
				
		String message = null;
		try {
			message = StringUtil.slurp(getClass().getClassLoader().getResourceAsStream("about.html"));
			message = StringUtil.replace(message, "##VERSION##", version);
			message = StringUtil.replace(message, "##BUILD##", buildNumber);
		}
		catch (Exception e) {
			message = "Could not load content.";
		}
		
		String changelog = null;
		try {
			changelog = StringUtil.slurp(getClass().getClassLoader().getResourceAsStream("changelog.html"));
		}
		catch (Exception e) {
			changelog = "Could not load changelog.";
		}

		String credits = null;
		try {
			credits = StringUtil.slurp(getClass().getClassLoader().getResourceAsStream("credits.html"));
		}
		catch (Exception e) {
			changelog = "Could not load credits.";
		}

		
		aboutPane.setText(message);
		aboutPane.setEditable(false);
		changelogPane.setText(changelog);
		changelogPane.setEditable(false);
		creditsPane.setText(credits);
		creditsPane.setEditable(false);
		
		tabPane = new JTabbedPane();
		// tabPane.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(changelogPane);
		JScrollPane creditsScrollPane = new JScrollPane(creditsPane);
		if (splashIcon != null) {
			JPanel p = new JPanel(new BorderLayout());
			p.add(new JLabel(splashIcon), BorderLayout.CENTER);
			tabPane.addTab("BlackBoard",resources.getIcon("icon.editor"), p);
		}
		tabPane.addTab("About",resources.getIcon("icon.about"), aboutPane);
		tabPane.addTab("Changelog",resources.getIcon("icon.document"), scrollPane);
		tabPane.addTab("Credits", resources.getIcon("icon.user"),creditsScrollPane);
		getContentPanel().add(tabPane, BorderLayout.CENTER);
		getOkButton().addActionListener(new okListener());
	}

	public class okListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			actionResult = OPTION_OK;
			setVisible(false);
		}
	}
}
