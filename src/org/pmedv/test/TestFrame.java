package org.pmedv.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.pmedv.blackboard.tools.graph.FunctionPanel;
import org.pmedv.core.components.AlternatingLineTable;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.LightGray;

/**
 * @author Matthias Pueski (22.05.2011)
 *
 */
@SuppressWarnings("unused")
public class TestFrame {
	
	private static JFrame frame;
	private static Dimension size;
	
	private static AlternatingLineTable table;
 
	

	private static final int BORDER = 0;
	
	public static void main(String[] args) {
		
//		Plastic3DLookAndFeel.setPlasticTheme(new LightGray());
//
//		try {			
//			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
//			com.jgoodies.looks.Options.setPopupDropShadowEnabled(true);
//		} 
//		catch (Exception e) {
//			System.out.println("failed to set look and feel.");
//		}		
		
		size = new Dimension(800,600);
		
		frame = new JFrame("Edit Layers");
		frame.setSize(size);
		frame.setBounds(new Rectangle(size));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setLayout(new BorderLayout());
		
		frame.setVisible(true);
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFileChooser fc = new JFileChooser();
				int result = fc.showOpenDialog(frame);
				
			}
		});
		

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					System.exit(0);
			}
		});
		
	}
	
	private class TableModel extends DefaultTableModel {
		
	}
	
}
