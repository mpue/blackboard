package org.pmedv.blackboard.test;

/**
 * @author Matthias Pueski (23.10.2010)
 *
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.RepaintManager;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class PrintPreviewS2 extends JPanel implements ActionListener {
	static final double INITIAL_SCALE_DIVISOR = 1.5; // scale factor == 1 / 1.5

	Component targetComponent;
	PageFormat pageFormat = new PageFormat();
	double xScaleDivisor = INITIAL_SCALE_DIVISOR;
	double yScaleDivisor = INITIAL_SCALE_DIVISOR;
	BufferedImage pcImage;

	JPanel hold = new JPanel();
	PreviewPage prp;

	ButtonGroup pf = new ButtonGroup();
	JRadioButton pf1;
	JRadioButton pf2;
	JLabel xsl = new JLabel("Xscale div by:", JLabel.LEFT);
	JLabel ysl = new JLabel("Yscale div by:", JLabel.LEFT);
	JButton ftp = new JButton("Fit to Page");
	JCheckBox cp = new JCheckBox("Constrain Proportions");
	JButton preview = new JButton("PREVIEW");
	JButton print = new JButton("PRINT");

	JSpinner xsp, ysp;
	SpinnerNumberModel snmx, snmy;

	JFrame workFrame;

	Color bgColor = Color.white;

	int pcw, pch;
	double wh, hw;

	public PrintPreviewS2(Component pc) {
		setBackground(bgColor);

		targetComponent = pc;

		// for a JTable, we can't use simple component.paint(g) call
		// because it doesn't paint table header !!
		if (pc instanceof JTable) {
			TableModel tm = ((JTable) pc).getModel();
			JTable workTable = new JTable(tm); // make pure clone
			targetComponent = getTableComponent(workTable);
		}

		pcImage = new BufferedImage(pcw = targetComponent.getWidth(), pch = targetComponent.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = pcImage.createGraphics();
		targetComponent.paint(g);
		g.dispose();
		wh = (double) pcw / (double) pch;
		hw = (double) pch / (double) pcw;
		/*
		 * workFrame is used in getTableComponent() method only for visualizing
		 * the table component and its header
		 */
		if (workFrame != null) { // if you don't use table clone here,
			workFrame.dispose(); // calling dispose() delete the table
		} // from original app window

		pageFormat.setOrientation(PageFormat.PORTRAIT);
		prp = new PreviewPage();

		snmx = new SpinnerNumberModel(1.5, 0.1, 10.0, 0.1);
		snmy = new SpinnerNumberModel(1.5, 0.1, 10.0, 0.1);
		xsp = new JSpinner(snmx);
		ysp = new JSpinner(snmy);

		pf1 = new JRadioButton("Portrait");
		pf1.setActionCommand("1");
		pf1.setSelected(true);
		pf2 = new JRadioButton("Landscape");
		pf2.setActionCommand("2");
		pf.add(pf1);
		pf.add(pf2);
		pf1.setBackground(bgColor);
		pf2.setBackground(bgColor);

		cp.setBackground(bgColor);

		preview.addActionListener(this);
		print.addActionListener(this);

		prp.setBackground(bgColor);
		hold.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		hold.setBackground(bgColor);
		hold.setLayout(new GridBagLayout());

		GridBagConstraints c1 = new GridBagConstraints();

		c1.insets = new Insets(15, 45, 0, 5);
		c1 = buildConstraints(c1, 0, 0, 2, 1, 0.0, 0.0);
		hold.add(pf1, c1);

		c1.insets = new Insets(2, 45, 0, 5);
		c1 = buildConstraints(c1, 0, 1, 2, 1, 0.0, 0.0);
		hold.add(pf2, c1);

		c1.insets = new Insets(25, 5, 0, 5);
		c1 = buildConstraints(c1, 0, 2, 1, 1, 0.0, 0.0);
		hold.add(xsl, c1);

		c1.insets = new Insets(25, 5, 0, 35);
		c1 = buildConstraints(c1, 1, 2, 1, 1, 0.0, 0.0);
		hold.add(xsp, c1);

		c1.insets = new Insets(5, 5, 0, 5);
		c1 = buildConstraints(c1, 0, 3, 1, 1, 0.0, 0.0);
		hold.add(ysl, c1);

		c1.insets = new Insets(15, 5, 0, 35);
		c1 = buildConstraints(c1, 1, 3, 1, 1, 0.0, 0.0);
		hold.add(ysp, c1);

		c1.insets = new Insets(0, 25, 0, 5);
		c1 = buildConstraints(c1, 0, 4, 2, 1, 0.0, 0.0);
		hold.add(cp, c1);

		c1.insets = new Insets(20, 35, 0, 35);
		c1 = buildConstraints(c1, 0, 5, 2, 1, 0.0, 0.0);
		hold.add(ftp, c1);

		c1.insets = new Insets(25, 35, 0, 35);
		c1 = buildConstraints(c1, 0, 6, 2, 1, 0.0, 0.0);
		hold.add(preview, c1);

		c1.insets = new Insets(5, 35, 25, 35);
		c1 = buildConstraints(c1, 0, 7, 2, 1, 0.0, 0.0);
		hold.add(print, c1);

		add(hold);
		add(prp);
	}

	Component getTableComponent(JTable table) {
		Box box = new Box(BoxLayout.Y_AXIS);
		JTableHeader jth = table.getTableHeader();

		Dimension dh = jth.getPreferredSize();
		Dimension dt = table.getPreferredSize();
		Dimension db = new Dimension(dh.width, dh.height + dt.height);
		box.setPreferredSize(db);

		jth.setBorder(new LineBorder(Color.black, 1) {
			public Insets getBorderInsets(Component c) {
				return new Insets(2, 2, 2, 2);
			}
		});

		table.setBorder(new PartialLineBorder(false, true, false, false));

		box.add(jth);
		box.add(table);

		// visualize table for getting non-zero sizes(width, height)
		workFrame = new JFrame();
		workFrame.getContentPane().add(box);
		workFrame.pack();
		workFrame.setVisible(true);

		return box;
	}

	GridBagConstraints buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, double wx, double wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.fill = GridBagConstraints.BOTH;
		return gbc;
	}

	public class PreviewPage extends JPanel {
		int x1, y1, l1, h1, x2, y2;
		Image image;

		public PreviewPage() {
			setPreferredSize(new Dimension(460, 460));
			setBorder(BorderFactory.createLineBorder(Color.black, 2));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			// PORTRAIT
			if (pageFormat.getOrientation() == PageFormat.PORTRAIT) {
				g.setColor(Color.black);
				g.drawRect(60, 10, 340, 440);
				x1 = (int) Math.rint(((double) pageFormat.getImageableX() / 72) * 40);
				y1 = (int) Math.rint(((double) pageFormat.getImageableY() / 72) * 40);
				l1 = (int) Math.rint(((double) pageFormat.getImageableWidth() / 72) * 40);
				h1 = (int) Math.rint(((double) pageFormat.getImageableHeight() / 72) * 40);
				g.setColor(Color.red);
				g.drawRect(x1 + 60, y1 + 10, l1, h1);
				// setScales(); // commenting-out suppresses too frequent paint
				// updates
				x2 = (int) Math.rint((double) l1 / xScaleDivisor);
				y2 = (int) Math.rint(((double) l1 * hw) / yScaleDivisor);
				image = pcImage.getScaledInstance(x2, y2, Image.SCALE_AREA_AVERAGING);
				g.drawImage(image, x1 + 60, y1 + 10, this);
			}
			// LANDSCAPE
			else {
				g.setColor(Color.black);
				g.drawRect(10, 60, 440, 340);
				x1 = (int) Math.rint(((double) pageFormat.getImageableX() / 72) * 40);
				y1 = (int) Math.rint(((double) pageFormat.getImageableY() / 72) * 40);
				l1 = (int) Math.rint(((double) pageFormat.getImageableWidth() / 72) * 40);
				h1 = (int) Math.rint(((double) pageFormat.getImageableHeight() / 72) * 40);
				g.setColor(Color.red);
				g.drawRect(x1 + 10, y1 + 60, l1, h1);
				// setScales();
				x2 = (int) Math.rint((double) l1 / xScaleDivisor);
				y2 = (int) Math.rint(((double) l1 * hw) / yScaleDivisor);
				image = pcImage.getScaledInstance(x2, y2, Image.SCALE_AREA_AVERAGING);
				g.drawImage(image, x1 + 10, y1 + 60, this);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ftp) {
		}
		if (e.getSource() == preview) {
			setProperties();
		}
		if (e.getSource() == print) {
			doPrint();
		}
	}

	public void setProperties() {
		if (pf1.isSelected()) {
			pageFormat.setOrientation(PageFormat.PORTRAIT);
		}
		else if (pf2.isSelected()) {
			pageFormat.setOrientation(PageFormat.LANDSCAPE);
		}
		setScales();
		prp.repaint();
	}

	public void setScales() {
		try {
			xScaleDivisor = ((Double) xsp.getValue()).doubleValue();
			yScaleDivisor = ((Double) ysp.getValue()).doubleValue();
		}
		catch (NumberFormatException e) {
		}
	}

	public void doPrint() {
		PrintThis();
	}

	public void PrintThis() {
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		Book book = new Book();
		book.append(new PrintPage(), pageFormat);
		printerJob.setPageable(book);
		boolean doPrint = printerJob.printDialog();
		if (doPrint) {
			try {
				printerJob.print();
			}
			catch (PrinterException exception) {
				System.err.println("Printing error: " + exception);
			}
		}
	}

	// public class PrintPage implements Printable{
	class PrintPage implements Printable {

		public int print(Graphics g, PageFormat format, int pageIndex) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.translate(format.getImageableX(), format.getImageableY());
			// disableDoubleBuffering(mp);
			System.out.println("get i x " + format.getImageableX());
			System.out.println("get i x " + format.getImageableY());
			System.out.println("getx: " + format.getImageableWidth());
			System.out.println("getx: " + format.getImageableHeight());
			// scale to fill the page
			double dw = format.getImageableWidth();
			double dh = format.getImageableHeight();
			setScales();
			double xScale = dw / (1028 / xScaleDivisor);
			double yScale = dh / (768 / yScaleDivisor);
			double scale = Math.min(xScale, yScale);
			System.out.println("" + scale);
			g2D.scale(xScale, yScale);
			targetComponent.paint(g);
			// enableDoubleBuffering(mp);
			return Printable.PAGE_EXISTS;
		}

		public void disableDoubleBuffering(Component c) {
			RepaintManager currentManager = RepaintManager.currentManager(c);
			currentManager.setDoubleBufferingEnabled(false);
		}

		public void enableDoubleBuffering(Component c) {
			RepaintManager currentManager = RepaintManager.currentManager(c);
			currentManager.setDoubleBufferingEnabled(true);
		}
	}

	public static void main(String[] args) {
		JFrame frame1 = new JFrame();
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con1 = frame1.getContentPane();

		JFrame frame2 = new JFrame();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con2 = frame2.getContentPane();

		JTable table = new JTable(20, 7);
		JScrollPane jsp = new JScrollPane(table);

		con1.add(jsp, BorderLayout.CENTER);
		frame1.pack();
		frame1.setVisible(true);

		PrintPreviewS2 pp = new PrintPreviewS2(table);
		con2.add(pp, BorderLayout.CENTER);
		frame2.pack();
		frame2.setVisible(true);
		frame2.toFront();
	}
}

class PartialLineBorder extends AbstractBorder {
	boolean top, left, bottom, right;

	public PartialLineBorder(boolean t, boolean l, boolean b, boolean r) {
		top = t;
		left = l;
		bottom = b;
		right = r;
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(2, 2, 2, 2);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1.0f));

		if (top) {
			g2.drawLine(x, y, x + width, y);
		}
		if (left) {
			g2.drawLine(x, y, x, y + height);
		}
		if (bottom) {
			g2.drawLine(x, y + height, x + width, y + height);
		}
		if (right) {
			g2.drawLine(x + width, y, x + width, y + height);
		}
	}
}