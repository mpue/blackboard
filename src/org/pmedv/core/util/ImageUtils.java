package org.pmedv.core.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class contains a collection of helper an convenience methods for image
 * processing.
 * 
 * @author Matthias Pueski
 */
public class ImageUtils {

	private static final Log log = LogFactory.getLog(ImageUtils.class);

	/**
	 * Returns the int rgb value for a rgb color triple
	 * 
	 * @param r red value
	 * @param g green value
	 * @param b blue value
	 * 
	 * @return the int rgb value for the color triple
	 */
	public static int intRGBValue(int r, int g, int b) {
		// With empty alpha (Bit 24..31 = 255)
		return (255 << 24) | (r << 16) | (g << 8) | b;
	}

	/**
	 * Creates a {@link BufferedImage} from the contents of a {@link JPanel}
	 * 
	 * @param panel the panel to create the image from
	 * 
	 * @return The {@link BufferedImage} containing the image of the panel
	 * 
	 */
	public static BufferedImage createImageFromComponent(JComponent component) {
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		component.printAll(g2d);
		return image;
	}

	/**
	 * Scales a {@link BufferedImage}
	 * 
	 * @param source the source image to scale
	 * @param scale the scale factor
	 * 
	 * @return a scaled image intance
	 */
	public static BufferedImage scale(BufferedImage source, double scale) {
		return scale(source, scale, scale);
	}

	/**
	 * Scales a {@link BufferedImage} by the scale factors scalex and scaley
	 * 
	 * @param source the source image to scale
	 * @param scalex the x scale factor
	 * @param scaley the y scale factor
	 * 
	 * @return a scaled image instance
	 */
	public static BufferedImage scale(BufferedImage source, double scalex, double scaley) {

		AffineTransform tx = new AffineTransform();
		tx.scale(scalex, scaley);

		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(source, null);

	}

	/**
	 * Creates a watermarked text on a given {@link BufferedImage}
	 * 
	 * @param image the image to create the watermark on
	 * @param text the text to use as a watermark
	 * @param x the x position of the text on the image
	 * @param y the y position of the text on the image
	 * @return the watermarked image
	 */
	public static BufferedImage createWaterMark(BufferedImage image, String text, int x, int y) {

		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.drawImage(image, 0, 0, null);

		// Modify the image and add a watermark

		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(alpha);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawString(text, x, y);

		// Free graphic resources and write image to output stream

		g2d.dispose();

		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		g2d.drawImage(output, 0, 0, null);

		return output;

	}

	/**
	 * Embosses a {@link BufferedImage}
	 * 
	 * @param source the source image to emboss
	 * 
	 * @return the embossed image
	 */
	public static BufferedImage emboss(BufferedImage source) {
		Kernel kernel = new Kernel(3, 3, new float[] { -2, 0, 0, 0, 1, 0, 0, 0, 2 });
		BufferedImageOp op = new ConvolveOp(kernel);
		return op.filter(source, null);
	}

	/**
	 * Sharpens a {@link BufferedImage}
	 * 
	 * @param source the source image to sharpen
	 * 
	 * @return the sharpened image
	 */
	public static BufferedImage sharpen(BufferedImage source) {
		Kernel kernel = new Kernel(3, 3, new float[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 });
		BufferedImageOp op = new ConvolveOp(kernel);
		return op.filter(source, null);
	}

	/**
	 * Blurs a {@link BufferedImage}
	 * 
	 * @param source the source image to blur
	 * 
	 * @return the blurred image
	 */
	public static BufferedImage blur(BufferedImage source) {
		Kernel kernel = new Kernel(3, 3, new float[] { 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f,
				1f / 9f });
		BufferedImageOp op = new ConvolveOp(kernel);
		return op.filter(source, null);
	}

	/**
	 * Inverts all rgb values of given image (gives an inverted image)
	 * 
	 * @param image the image to invert
	 * @return the inverted image
	 */
	public static BufferedImage invert(BufferedImage image) {

		byte[] invert = new byte[256];
		for (int i = 0; i < 256; i++)
			invert[i] = (byte) (255 - i);
		BufferedImageOp op = new LookupOp(new ByteLookupTable(0, invert), null);

		return op.filter(image, null);

	}

	/**
	 * Converts a buffered image to grayscale
	 * 
	 * @param source the source image to convert
	 * @return the grayscaled image
	 */
	public static BufferedImage convertToGrayscale(BufferedImage source) {

		BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		return op.filter(source, null);
	}

	/**
	 * Flips a {@link BufferedImage} horizontally
	 * 
	 * @param bi the {@link BufferedImage} to flip
	 * @return the flipped image
	 */
	public static BufferedImage flipHorizontal(BufferedImage bi) {

		int width = bi.getWidth();
		int height = bi.getHeight();

		BufferedImage biFlip = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		int j = width - 1;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				biFlip.setRGB(j--, y, bi.getRGB(x, y));
			}
			j = width - 1;
		}

		return biFlip;
	}

	/**
	 * Rotates a {@link BufferedImage} 90 degrees clockwise
	 * 
	 * @param bi the {@link BufferedImage} to rotate
	 * @return the rotated image
	 */
	public static BufferedImage rotate90CW(BufferedImage bi) {

		int width = bi.getWidth();
		int height = bi.getHeight();

		BufferedImage biFlip = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				biFlip.setRGB(height - 1 - j, i, bi.getRGB(i, j));

		return biFlip;
	}

	/**
	 * Rotates a {@link BufferedImage} 90 degrees counter-clockwise
	 * 
	 * @param bi the {@link BufferedImage} to rotate
	 * @return the rotated image
	 */
	public static BufferedImage rotate90CCW(BufferedImage bi) {

		int width = bi.getWidth();
		int height = bi.getHeight();

		BufferedImage biFlip = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				biFlip.setRGB(j, width - 1 - i, bi.getRGB(i, j));

		return biFlip;
	}

	/**
	 * Converts an image to a {@link BufferedImage}
	 * 
	 * @param image the image to convert
	 * @return the converted image
	 */
	public static BufferedImage toBufferedImage(Image image) {

		// Label as ImageObserver
		Label dummyObserver = new Label();

		int width = image.getWidth(dummyObserver);
		int height = image.getHeight(dummyObserver);

		// Create new image
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Draw image to buffered image
		bImage.getGraphics().drawImage(image, 0, 0, dummyObserver);

		return bImage;

	}

	/**
	 * Converts a {@link BufferedImage} to an {@link Image}
	 * 
	 * @param bufferedImage the image to convert
	 * @return the converted image
	 */
	public static Image toImage(BufferedImage bufferedImage) {

		return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
	}

	/**
	 * Loads an image safely with a <code>MediaTracker<code> object
	 * 
	 * @param name
	 * @param parent
	 * @return
	 */
	public static synchronized Image getImageFile(String name, JFrame parent) {

		MediaTracker tracker = new MediaTracker(parent);
		Image openedImage = Toolkit.getDefaultToolkit().getImage(name);
		tracker.addImage(openedImage, 1);
		try {
			tracker.waitForAll();
		}
		catch (InterruptedException ie) {
		}
		return openedImage;
	}

	/**
	 * Scales a JPG image located at the path denoted by src, the target image
	 * will be written to dest.
	 * 
	 * @param src the source path of the image to be scaled
	 * @param width the width of the destination image
	 * @param height the height of the destination image
	 * @param dest the destination of the scaled image file
	 * @throws IOException
	 */
	public static void scaleJPG(String src, int width, int height, String dest) throws IOException {

		BufferedImage bsrc = ImageIO.read(new File(src));

		log.info("got image : " + bsrc);

		int thumbWidth = width;
		int thumbHeight = height;
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = bsrc.getWidth(null);
		int imageHeight = bsrc.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		}
		else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		Image scaledImage = bsrc.getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_SMOOTH);

		BufferedImage bdest = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = bdest.createGraphics();
		g.drawImage(scaledImage, 0, 0, null);
		ImageIO.write(bdest, "JPG", new File(dest));

	}

	/**
	 * Scales an image to the given width and height, regarding the aspect ratio
	 * 
	 * @param src
	 * @param width
	 * @param height
	 * @return the scaled instance
	 */
	public static BufferedImage scale(BufferedImage src, int width, int height) {

		int thumbWidth = width;
		int thumbHeight = height;
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = src.getWidth(null);
		int imageHeight = src.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		}
		else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		Image scaledImage = src.getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_SMOOTH);

		BufferedImage bdest = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = bdest.createGraphics();
		g.drawImage(scaledImage, 0, 0, null);

		return bdest;

	}

	/**
	 * Gets the pixels from an image to an array
	 * 
	 * @param img the image to get the pixels from
	 * @param width the height of the image
	 * @param height the width of the image
	 * @return the image data as an array of pixels
	 * @throws InterruptedException
	 */
	public static int[] getArrayFromImage(Image img, int width, int height) throws InterruptedException {

		int[] pixels = new int[width * height];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels, 0, width);
		pg.grabPixels();
		return pixels;
	}

	/**
	 * Creates an image from a pixel array
	 * 
	 * @param pixels the pixel array to create the image from
	 * @param width the width of the target image
	 * @param height the height of the target image
	 * @return the new image created from the pixel array
	 */
	public static Image getImageFromArray(int[] pixels, int width, int height) {

		MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.createImage(mis);
	}

}
