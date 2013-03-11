package org.pmedv.blackboard.tools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class PixelEditor extends JPanel {

	private static final long serialVersionUID = 6667794073446081315L;

	private JDialog colorDialog = null;

	private int partWidth;
	private int partHeight;

	int lastx;
	int lasty;

	int mousex;
	int mousey;

	private static final int xoffsprev = 20;
	private static final int yoffsprev = 20;
	 
	private boolean pickingColor = false;
	
	private int size = 16;

	private Color currentColor;

	Pixel[][] pixels;

	private int button;

	BufferedImage image;

	public PixelEditor(final int partWidth, final int partHeight) {
		// no layout;
		super(null);
		this.partHeight = partHeight;
		this.partWidth = partWidth;
		setSize(partHeight * size, partWidth * size);
		setBounds(0, 0, partHeight * size, partWidth * size);
		setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
		pixels = new Pixel[partWidth][partHeight];
		image = new BufferedImage(partWidth, partHeight, BufferedImage.TYPE_INT_ARGB);

		currentColor = Color.BLACK;

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				button = e.getButton();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				button = -1;
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				button = e.getButton();

				int x = e.getX() / size;
				int y = e.getY() / size;
				
				mousex = x;
				mousey = y;

				if (x > partWidth - 1 || y > partHeight - 1 || y < 0 || x < 0)
					return;

				
				if (pickingColor) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					currentColor = pixels[x][y].getColor();
					pickingColor = false;
				}
				else {
					if (e.getButton() == 1) {
						pixels[x][y] = new Pixel(currentColor);
					}
					else {
						pixels[x][y] = null;
					}					
				}

				repaint();

			}
			
		});

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {

				int x = e.getX() / size;
				int y = e.getY() / size;

				if (x > partWidth - 1 || y > partHeight - 1 || y < 0 || x < 0)
					return;

				if (lastx != x || lasty != y) {

					if (button == 1) {
						pixels[x][y] = new Pixel(currentColor);
					}
					else {
						pixels[x][y] = null;
					}

					repaint();

					lastx = x;
					lasty = y;
					
					mousex = x;
					mousey = y;

				}

			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mousex = e.getX() / size;
				mousey = e.getY() / size;
				repaint();
			}


		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, partWidth * size, partHeight * size);

		for (int x = 0; x < partWidth; x++) {
			for (int y = 0; y < partHeight; y++) {

				if (pixels[x][y] != null) {
					g2d.setColor(pixels[x][y].getColor());
					g2d.fillRect(x * size, y * size, size, size);
				}
			}
		}

		// Preview
		g2d.setColor(Color.BLACK);
		g2d.drawString("Preview : ", partWidth * size+xoffsprev, xoffsprev-5);
		
		for (int x = 0; x < partWidth; x++) {
			for (int y = 0; y < partHeight; y++) {
				if (pixels[x][y] != null) {
					g2d.setColor(pixels[x][y].getColor());
					g2d.fillRect(x + (partWidth * size + xoffsprev), y + yoffsprev, 1, 1);
				}
			}
		}
		// preview border
		g2d.setColor(Color.BLACK);
		g2d.drawRect(partWidth * size + xoffsprev, yoffsprev, partWidth, partHeight);
		
		// selected color
		
		g2d.setColor(currentColor);
		g2d.fillRect(partWidth * size + xoffsprev, yoffsprev+64, partWidth, partHeight);

		
		
		g2d.setColor(Color.GRAY);
		for (int x = 0; x <= partWidth * size; x += size) {
			g2d.drawLine(x, 0, x, partWidth * size);
		}
		for (int y = 0; y <= partHeight * size; y += size) {
			g2d.drawLine(0, y, partHeight * size, y);
		}
		
		if (mousex > partWidth - 1 || mousey > partHeight - 1 || mousex < 0 || mousey < 0)
			return;

		
		g2d.setColor(Color.RED);		
		g2d.drawRect(mousex * size, mousey * size, size, size);

	}

	/**
	 * @return the partWidth
	 */
	public int getPartWidth() {
		return partWidth;
	}

	/**
	 * @param partWidth
	 *            the partWidth to set
	 */
	public void setPartWidth(int partWidth) {
		this.partWidth = partWidth;
	}

	/**
	 * @return the partHeight
	 */
	public int getPartHeight() {
		return partHeight;
	}

	/**
	 * @param partHeight
	 *            the partHeight to set
	 */
	public void setPartHeight(int partHeight) {
		this.partHeight = partHeight;
	}

	public static final class Pixel implements Serializable {

		private static final long serialVersionUID = -5736424188186228281L;

		private Color color;

		public Pixel(Color color) {
			this.setColor(color);
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}
	}

	public void triggerColorSelect() {
		final JColorChooser chooser = new JColorChooser();

		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentColor = chooser.getColor();
				colorDialog.setVisible(false);
			}
		};

		ActionListener cancelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				colorDialog.setVisible(false);
			}
		};

		if (colorDialog == null) {
			colorDialog = JColorChooser.createDialog(getParent(), "Select color", true, chooser, okListener, cancelListener);
		}
		colorDialog.setVisible(true);
	}

	public BufferedImage getImage() {

		for (int x = 0; x < partWidth; x++) {
			for (int y = 0; y < partHeight; y++) {

				if (pixels[x][y] != null) {
					image.setRGB(x, y, pixels[x][y].getColor().getRGB());
				}
			}
		}
		return image;
	}

	/**
	 * @return the pixels
	 */
	public Pixel[][] getPixels() {
		return pixels;
	}

	/**
	 * @param pixels
	 *            the pixels to set
	 */
	public void setPixels(Pixel[][] pixels) {
		this.pixels = pixels;
		repaint();
	}

	public void clear() {
		pixels = new Pixel[partWidth][partHeight];
		repaint();
	}

	public void fill() {
		for (int x = 0; x < partWidth; x++) {
			for (int y = 0; y < partHeight; y++) {
				pixels[x][y] = new Pixel(currentColor);
			}
		}
		repaint();
	}

	public boolean isPickingColor() {
		return pickingColor;
	}

	public void setPickingColor(boolean pickingColor) {
		this.pickingColor = pickingColor;
	}
	
	
}
