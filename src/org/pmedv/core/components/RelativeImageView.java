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
GNU Lesser General Public License

RelativeImageView
Copyright (C) 2001-2002  Frits Jalvingh & Howard Kistler
changes to RelativeImageView
Copyright (C) 2003-2004  Karsten Pawlik

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.pmedv.core.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description: creates a view on an image.
 * 
 * This code was modeled after an article on <a
 * href="http://www.javaworld.com/javaworld/javatips/jw-javatip109.html">
 * JavaWorld</a> by Bob Kenworthy.
 * 
 * @author <a href="mailto:jal@grimor.com">Frits Jalvingh</a>
 * @author Karsten Pawlik
 */
@SuppressWarnings("unused")
public class RelativeImageView extends View implements ImageObserver, MouseListener, MouseMotionListener {

	private static final Log	log						= LogFactory.getLog(RelativeImageView.class);

	public static final String	TOP						= "top";
	public static final String	TEXTTOP					= "texttop";
	public static final String	MIDDLE					= "middle";
	public static final String	ABSMIDDLE				= "absmiddle";
	public static final String	CENTER					= "center";
	public static final String	BOTTOM					= "bottom";
	public static final String	IMAGE_CACHE_PROPERTY	= "imageCache";

	private static Icon			sPendingImageIcon;
	private static Icon			sMissingImageIcon;
	private static final String	PENDING_IMAGE_SRC		= "ImagePendingHK.gif";
	private static final String	MISSING_IMAGE_SRC		= "ImageMissingHK.gif";
	private static final int	DEFAULT_WIDTH			= 32;
	private static final int	DEFAULT_HEIGHT			= 32;
	private static final int	DEFAULT_BORDER			= 1;

	private AttributeSet		attr;
	private Element				fElement;
	private Image				fImage;
	private int					fHeight;
	private int					fWidth;
	private Container			fContainer;
	private Rectangle			fBounds;
	private Component			fComponent;
	private Point				fGrowBase;
	private boolean				fGrowProportionally;
	private boolean				bLoading;

	/**
	 * Constructor Creates a new view that represents an IMG element.
	 * 
	 * @param elem
	 *            the element to create a view for
	 */
	public RelativeImageView(Element elem) {

		super(elem);
		initialize(elem);
		StyleSheet sheet = getStyleSheet();
		attr = sheet.getViewAttributes(this);
		log.debug("new RelativeImageView created.");
	}

	/**
	 * initialization method.
	 * 
	 * @param elem
	 *            an element
	 */

	@SuppressWarnings("rawtypes")
	private void initialize(Element elem) {

		synchronized (this) {
			bLoading = true;
			fWidth = 0;
			fHeight = 0;
		}

		int width = 0;
		int height = 0;
		boolean customWidth = false;
		boolean customHeight = false;

		try {
			fElement = elem;
			// request image from document's cache
			AttributeSet myAttr = elem.getAttributes();
			if (isURL()) {
				URL src = getSourceURL();
				if (src != null) {
					Dictionary cache = (Dictionary) getDocument().getProperty(IMAGE_CACHE_PROPERTY);
					if (cache != null) {
						fImage = (Image) cache.get(src);
					}
					else {
						fImage = Toolkit.getDefaultToolkit().getImage(src);
					}
				}
			}
			else {
				// load image from relative path
				String src = (String) fElement.getAttributes().getAttribute(HTML.Attribute.SRC);
				try {
					src = processSrcPath(src);
				}
				catch (Throwable e) {
					log.warn("exception thrown while processing src path. " + e.fillInStackTrace());
					src = (String) fElement.getAttributes().getAttribute(HTML.Attribute.SRC);
				}
				log.warn("trying to load the image from src " + src);
				fImage = Toolkit.getDefaultToolkit().createImage(src);
				try {
					waitForImage();
				}
				catch (InterruptedException ie) {
					fImage = null;
					log.warn("loading image from relative path failed.");
					// possibly replace with the ImageBroken icon, if that's
					// what is happening
				}
			}

			// get height & width from params or image or defaults
			height = getIntAttr(HTML.Attribute.HEIGHT, -1);
			customHeight = (height > 0);
			if (!customHeight && fImage != null) {
				height = fImage.getHeight(this);
			}
			if (height <= 0) {
				height = DEFAULT_HEIGHT;
			}

			width = getIntAttr(HTML.Attribute.WIDTH, -1);
			customWidth = (width > 0);
			if (!customWidth && fImage != null) {
				width = fImage.getWidth(this);
			}

			if (width <= 0) {
				width = DEFAULT_WIDTH;
			}

			if (fImage != null) {
				if (customHeight && customWidth) {
					Toolkit.getDefaultToolkit().prepareImage(fImage, height, width, this);
				}
				else {
					Toolkit.getDefaultToolkit().prepareImage(fImage, -1, -1, this);
				}
			}
		}
		finally {
			synchronized (this) {
				bLoading = false;
				if (customHeight || fHeight == 0) {
					fHeight = height;
				}

				if (customWidth || fWidth == 0) {
					fWidth = width;
				}
			}
		}
	}

	/**
	 * Determines if path is in the form of a URL
	 */
	private boolean isURL() {

		String src = (String) fElement.getAttributes().getAttribute(HTML.Attribute.SRC);
		return src.toLowerCase().startsWith("file") || src.toLowerCase().startsWith("http");
	}

	/**
	 * Checks to see if the absolute path is availabe thru an application global
	 * static variable or thru a system variable. If so, appends the relative
	 * path to the absolute path and returns the String.
	 */
	private String processSrcPath(String src) {

		String val = src;
		File imageFile = new File(src);

		if (imageFile.isAbsolute())
			return src;

		boolean found = false;
		Document doc = getDocument();
		if (doc != null) {
			String pv = (String) (doc.getProperty("de.xeinfach.kafenio.docsource"));
			log.debug("de.xeinfach.kafenio.docsource: " + pv);
			if (pv != null) {
				File f = new File(pv, src);
				val = (new File(f.getParent(), imageFile.getPath().toString())).toString();
				found = true;
			}
		}
		/** @todo investigate this - double !found check. */
		if (!found) {

			String fileHtmlPath = new File(".").getAbsolutePath();
			val = (new File(fileHtmlPath, imageFile.getPath())).toString();
			found = true;
		}
		if (!found) {
			String imagePath = System.getProperty("system.image.path.key");
			if (imagePath != null) {
				val = (new File(imagePath, imageFile.getPath())).toString();
			}
		}
		return val;

	}

	/**
	 * Method insures that the image is loaded and not a broken reference
	 */
	private void waitForImage() throws InterruptedException {

		int w = fImage.getWidth(this);
		int h = fImage.getHeight(this);

		while (true) {
			int flags = Toolkit.getDefaultToolkit().checkImage(fImage, w, h, this);
			if (((flags & ERROR) != 0) || ((flags & ABORT) != 0)) {
				throw new InterruptedException();
			}
			else if ((flags & (ALLBITS | FRAMEBITS)) != 0) {
				return;
			}
			Thread.sleep(10);
		}

	}

	/**
	 * Fetches the attributes to use when rendering. This is implemented to
	 * multiplex the attributes specified in the model with a StyleSheet.
	 * 
	 * @return returns an AttributeSet containing the attributes.
	 */
	public AttributeSet getAttributes() {

		return attr;
	}

	/**
	 * Method tests whether the image within a link
	 */
	boolean isLink() {

		AttributeSet anchorAttr = (AttributeSet) fElement.getAttributes().getAttribute(HTML.Tag.A);
		if (anchorAttr != null) {
			return anchorAttr.isDefined(HTML.Attribute.HREF);
		}
		return false;
	}

	/**
	 * Method returns the size of the border to use
	 * 
	 * @return returns the border width as int.
	 */
	public int getBorder() {

		return getIntAttr(HTML.Attribute.BORDER, isLink() ? DEFAULT_BORDER : 0);
	}

	/**
	 * Method returns the amount of extra space to add along an axis
	 * 
	 * @param axis
	 *            value of axis
	 * @return returns the space for the given axis value.
	 */
	public int getSpace(int axis) {

		return getIntAttr((axis == X_AXIS) ? HTML.Attribute.HSPACE : HTML.Attribute.VSPACE, 0);
	}

	/**
	 * Method returns the border's color, or null if this is not a link
	 * 
	 * @return the border color.
	 */
	public Color getBorderColor() {

		StyledDocument doc = (StyledDocument) getDocument();
		return doc.getForeground(getAttributes());
	}

	/**
	 * Method returns the image's vertical alignment
	 * 
	 * @return vertical alignment as float
	 */
	public float getVerticalAlignment() {

		String align = (String) fElement.getAttributes().getAttribute(HTML.Attribute.ALIGN);
		if (align != null) {
			align = align.toLowerCase();
			if (align.equals(TOP) || align.equals(TEXTTOP)) {
				return 0.0f;
			}
			else if (align.equals(RelativeImageView.CENTER) || align.equals(MIDDLE) || align.equals(ABSMIDDLE)) {
				return 0.5f;
			}
		}
		return 1.0f; // default alignment is bottom
	}

	/**
	 * returns true if ImageObserver does have pixels, false if not.
	 * 
	 * @param obs
	 *            an ImageObserver.
	 * @return returns true if ImageObserver does have pixels, false if not.
	 */
	public boolean hasPixels(ImageObserver obs) {

		return ((fImage != null) && (fImage.getHeight(obs) > 0) && (fImage.getWidth(obs) > 0));
	}

	/**
	 * Method returns a URL for the image source, or null if it could not be
	 * determined
	 * 
	 * @returns a URL for the image source or null if it could not be
	 *          determined.
	 */
	private URL getSourceURL() {

		String src = (String) fElement.getAttributes().getAttribute(HTML.Attribute.SRC);
		if (src == null)
			return null;

		URL reference = ((HTMLDocument) getDocument()).getBase();
		try {
			URL u = new URL(reference, src);
			return u;
		}
		catch (MalformedURLException mue) {
			log.warn("malformed URL: " + mue.fillInStackTrace());
			return null;
		}
	}

	/**
	 * Method looks up an integer-valued attribute (not recursive!)
	 * 
	 * @param name
	 *            HTML.Attribute name
	 * @param iDefault
	 *            default int value
	 * @return returns int value of the given attribute or the default if the
	 *         given attribute was not found.
	 */
	private int getIntAttr(HTML.Attribute name, int iDefault) {

		AttributeSet myAttr = fElement.getAttributes();
		if (myAttr.isDefined(name)) {
			int i;
			String val = (String) myAttr.getAttribute(name);
			if (val == null) {
				i = iDefault;
			}
			else {
				try {
					i = Math.max(0, Integer.parseInt(val));
				}
				catch (NumberFormatException nfe) {
					log.debug("setting i to default value.");
					i = iDefault;
				}
			}
			return i;
		}
		else {
			return iDefault;
		}
	}

	/**
	 * Establishes the parent view for this view. Seize this moment to cache the
	 * AWT Container I'm in.
	 * 
	 * @param parent
	 *            the parent view
	 */
	public void setParent(View parent) {

		super.setParent(parent);
		fContainer = ((parent != null) ? getContainer() : null);
		if ((parent == null) && (fComponent != null)) {
			fComponent.getParent().remove(fComponent);
			fComponent = null;
		}
	}

	/**
	 * Attributes may have changed, so update.
	 * 
	 * @param e
	 *            a DocumentEvent
	 * @param a
	 *            a Shape
	 * @param f
	 *            a ViewFactory
	 */
	public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {

		super.changedUpdate(e, a, f);
		float align = getVerticalAlignment();

		int height = fHeight;
		int width = fWidth;

		initialize(getElement());

		boolean hChanged = fHeight != height;
		boolean wChanged = fWidth != width;
		if (hChanged || wChanged || getVerticalAlignment() != align) {
			getParent().preferenceChanged(this, hChanged, wChanged);
		}
	}

	/**
	 * Paints the image.
	 * 
	 * @param g
	 *            the rendering surface to use
	 * @param a
	 *            the allocated region to render into
	 * @see View#paint
	 */
	public void paint(Graphics g, Shape a) {

		Color oldColor = g.getColor();
		fBounds = a.getBounds();
		int border = getBorder();
		int x = fBounds.x + border + getSpace(X_AXIS);
		int y = fBounds.y + border + getSpace(Y_AXIS);
		int width = fWidth;
		int height = fHeight;
		int sel = getSelectionState();

		// If no pixels yet, draw gray outline and icon
		if (!hasPixels(this)) {
			g.setColor(Color.lightGray);
			g.drawRect(x, y, width - 1, height - 1);
			g.setColor(oldColor);
			loadImageStatusIcons();
			Icon icon = ((fImage == null) ? sMissingImageIcon : sPendingImageIcon);
			if (icon != null) {
				icon.paintIcon(getContainer(), g, x, y);
			}
		}

		// Draw image
		if (fImage != null) {
			g.drawImage(fImage, x, y, width, height, this);
		}

		// If selected exactly, we need a black border & grow-box
		Color bc = getBorderColor();
		if (sel == 2) {
			// Make sure there's room for a border
			int delta = 2 - border;
			if (delta > 0) {
				x += delta;
				y += delta;
				width -= delta << 1;
				height -= delta << 1;
				border = 2;
			}
			bc = null;
			g.setColor(Color.black);
			// Draw grow box
			g.fillRect(x + width - 5, y + height - 5, 5, 5);
		}

		// Draw border
		if (border > 0) {
			if (bc != null) {
				g.setColor(bc);
			}
			// Draw a thick rectangle:
			for (int i = 1; i <= border; i++) {
				g.drawRect(x - i, y - i, width - 1 + i + i, height - 1 + i + i);
			}
			g.setColor(oldColor);
		}
	}

	/**
	 * Request that this view be repainted. Assumes the view is still at its
	 * last-drawn location.
	 * 
	 * @param delay
	 *            delay before repainting.
	 */
	protected void repaint(long delay) {

		if ((fContainer != null) && (fBounds != null)) {
			fContainer.repaint(delay, fBounds.x, fBounds.y, fBounds.width, fBounds.height);
		}
	}

	/**
	 * Determines whether the image is selected, and if it's the only thing
	 * selected.
	 * 
	 * @return 0 if not selected, 1 if selected, 2 if exclusively selected.
	 *         "Exclusive" selection is only returned when editable.
	 */
	protected int getSelectionState() {

		int p0 = fElement.getStartOffset();
		int p1 = fElement.getEndOffset();
		if (fContainer instanceof JTextComponent) {
			JTextComponent textComp = (JTextComponent) fContainer;
			int start = textComp.getSelectionStart();
			int end = textComp.getSelectionEnd();
			if ((start <= p0) && (end >= p1)) {
				if ((start == p0) && (end == p1) && isEditable()) {
					return 2;
				}
				else {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * @return returns true if container is editable, false otherwise.
	 */
	protected boolean isEditable() {

		return ((fContainer instanceof JEditorPane) && ((JEditorPane) fContainer).isEditable());
	}

	/**
	 * @return returns the text editor's highlight color.
	 */
	protected Color getHighlightColor() {

		JTextComponent textComp = (JTextComponent) fContainer;
		return textComp.getSelectionColor();
	}

	// Progressive display -------------------------------------------------

	// This can come on any thread. If we are in the process of reloading
	// the image and determining our state (loading == true) we don't fire
	// preference changed, or repaint, we just reset the fWidth/fHeight as
	// necessary and return. This is ok as we know when loading finishes
	// it will pick up the new height/width, if necessary.

	private static boolean	sIsInc		= true;
	private static int		sIncRate	= 100;

	/**
	 * updates the image.
	 * 
	 * @param img
	 *            the image to update to
	 * @param flags
	 *            flags to set
	 * @param x
	 *            y-coordinate
	 * @param y
	 *            y-coordinate
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @return returns true if update was successful, false otherwise.
	 */
	public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height) {

		if ((fImage == null) || (fImage != img)) {
			return false;
		}

		// Bail out if there was an error
		if ((flags & (ABORT | ERROR)) != 0) {
			fImage = null;
			repaint(0);
			return false;
		}

		// Resize image if necessary
		short changed = 0;
		if ((flags & ImageObserver.HEIGHT) != 0) {
			if (!getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT)) {
				changed |= 1;
			}
		}

		if ((flags & ImageObserver.WIDTH) != 0) {
			if (!getElement().getAttributes().isDefined(HTML.Attribute.WIDTH)) {
				changed |= 2;
			}
		}

		synchronized (this) {
			if ((changed & 1) == 1) {
				fWidth = width;
			}

			if ((changed & 2) == 2) {
				fHeight = height;
			}

			if (bLoading) {
				// No need to resize or repaint, still in the process of loading
				return true;
			}
		}

		if (changed != 0) {
			// May need to resize myself, asynchronously
			Document doc = getDocument();
			try {
				if (doc instanceof AbstractDocument) {
					((AbstractDocument) doc).readLock();
				}
				preferenceChanged(this, true, true);
			}
			finally {
				if (doc instanceof AbstractDocument) {
					((AbstractDocument) doc).readUnlock();
				}
			}
			return true;
		}

		// Repaint when done or when new pixels arrive
		if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
			repaint(0);
		}
		else if ((flags & SOMEBITS) != 0) {
			if (sIsInc) {
				repaint(sIncRate);
			}
		}
		return ((flags & ALLBITS) == 0);
	}

	// Layout --------------------------------------------------------------

	/**
	 * Determines the preferred span for this view along an axis.
	 * 
	 * @param axis
	 *            may be either X_AXIS or Y_AXIS
	 * @return the span the view would like to be rendered into. Typically the
	 *         view is told to render into the span that is returned, although
	 *         there is no guarantee. The parent may choose to resize or break
	 *         the view.
	 */
	public float getPreferredSpan(int axis) {

		int extra = 2 * (getBorder() + getSpace(axis));
		switch (axis) {
		case View.X_AXIS:
			return fWidth + extra;
		case View.Y_AXIS:
			return fHeight + extra;
		default:
			throw new IllegalArgumentException("Invalid axis in getPreferredSpan() : " + axis);
		}
	}

	/**
	 * Determines the desired alignment for this view along an axis. This is
	 * implemented to give the alignment to the bottom of the icon along the y
	 * axis, and the default along the x axis.
	 * 
	 * @param axis
	 *            may be either X_AXIS or Y_AXIS
	 * @return the desired alignment. This should be a value between 0.0 and 1.0
	 *         where 0 indicates alignment at the origin and 1.0 indicates
	 *         alignment to the full span away from the origin. An alignment of
	 *         0.5 would be the center of the view.
	 */
	public float getAlignment(int axis) {

		switch (axis) {
		case View.Y_AXIS:
			return getVerticalAlignment();
		default:
			return super.getAlignment(axis);
		}
	}

	/**
	 * Provides a mapping from the document model coordinate space to the
	 * coordinate space of the view mapped to it.
	 * 
	 * @param pos
	 *            the position to convert
	 * @param a
	 *            the allocated region to render into
	 * @param b
	 *            Position Bias
	 * @return the bounding box of the given position
	 * @exception BadLocationException
	 *                if the given position does not represent a valid location
	 *                in the associated document
	 * @see View#modelToView
	 */
	public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {

		int p0 = getStartOffset();
		int p1 = getEndOffset();
		if ((pos >= p0) && (pos <= p1)) {
			Rectangle r = a.getBounds();
			if (pos == p1) {
				r.x += r.width;
			}
			r.width = 0;
			return r;
		}
		return null;
	}

	/**
	 * Provides a mapping from the view coordinate space to the logical
	 * coordinate space of the model.
	 * 
	 * @param x
	 *            the X coordinate
	 * @param y
	 *            the Y coordinate
	 * @param a
	 *            the allocated region to render into
	 * @param bias
	 *            Position Bias[]
	 * @return the location within the model that best represents the given
	 *         point of view
	 * @see View#viewToModel
	 */
	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {

		Rectangle alloc = (Rectangle) a;
		if (x < (alloc.x + alloc.width)) {
			bias[0] = Position.Bias.Forward;
			return getStartOffset();
		}
		bias[0] = Position.Bias.Backward;
		return getEndOffset();
	}

	/**
	 * Change the size of this image. This alters the HEIGHT and WIDTH
	 * attributes of the Element and causes a re-layout.
	 */
	protected void resize(int width, int height) {

		if ((width == fWidth) && (height == fHeight)) {
			return;
		}
		fWidth = width;
		fHeight = height;
		// Replace attributes in document
		MutableAttributeSet myAttr = new SimpleAttributeSet();
		myAttr.addAttribute(HTML.Attribute.WIDTH, Integer.toString(width));
		myAttr.addAttribute(HTML.Attribute.HEIGHT, Integer.toString(height));
		((StyledDocument) getDocument())
				.setCharacterAttributes(fElement.getStartOffset(), fElement.getEndOffset(), myAttr, false);
	}

	// Mouse event handling ------------------------------------------------

	/**
	 * Select or grow image when clicked.
	 * 
	 * @param e
	 *            MouseEvent to handle
	 */
	public void mousePressed(MouseEvent e) {

		Dimension size = fComponent.getSize();
		if ((e.getX() >= (size.width - 7)) && (e.getY() >= (size.height - 7)) && (getSelectionState() == 2)) {
			Point loc = fComponent.getLocationOnScreen();
			fGrowBase = new Point(loc.x + e.getX() - fWidth, loc.y + e.getY() - fHeight);
			fGrowProportionally = e.isShiftDown();
		}
		else {
			fGrowBase = null;
			JTextComponent comp = (JTextComponent) fContainer;
			int start = fElement.getStartOffset();
			int end = fElement.getEndOffset();
			int mark = comp.getCaret().getMark();
			int dot = comp.getCaret().getDot();
			if (e.isShiftDown()) {

				if (mark <= start) {
					comp.moveCaretPosition(end);
				}
				else {
					comp.moveCaretPosition(start);
				}
			}
			else {
				if (mark != start) {
					comp.setCaretPosition(start);
				}
				if (dot != end) {
					comp.moveCaretPosition(end);
				}
			}
		}
	}

	/**
	 * Resize image if initial click was in grow-box:
	 * 
	 * @param e
	 *            MouseEvent to handle
	 */
	public void mouseDragged(MouseEvent e) {

		if (fGrowBase != null) {
			Point loc = fComponent.getLocationOnScreen();
			int width = Math.max(2, loc.x + e.getX() - fGrowBase.x);
			int height = Math.max(2, loc.y + e.getY() - fGrowBase.y);
			if (e.isShiftDown() && fImage != null) {
				// Make sure size is proportional to actual image size
				float imgWidth = fImage.getWidth(this);
				float imgHeight = fImage.getHeight(this);
				if ((imgWidth > 0) && (imgHeight > 0)) {
					float prop = imgHeight / imgWidth;
					float pwidth = height / prop;
					float pheight = width * prop;
					if (pwidth > width) {
						width = (int) pwidth;
					}
					else {
						height = (int) pheight;
					}
				}
			}
			resize(width, height);
		}
	}

	/**
	 * what to do when mouse is released.
	 * 
	 * @param me
	 *            MouseEvent to handle
	 */
	public void mouseReleased(MouseEvent me) {

		fGrowBase = null;
		// TODO: Should post some command to make the action undo-able
	}

	/**
	 * On double-click, open image properties dialog.
	 * 
	 * @param me
	 *            MouseEvent to handle
	 */
	public void mouseClicked(MouseEvent me) {

		if (me.getClickCount() == 2) {
			// TODO: IMPLEMENT MOUSECLICKED method
		}
	}

	/**
	 * @param me
	 *            MouseEvent to handle
	 */
	public void mouseEntered(MouseEvent me) {

	}

	/**
	 * @param me
	 *            MouseEvent to handle
	 */
	public void mouseMoved(MouseEvent me) {

	}

	/**
	 * @param me
	 *            MouseEvent to handle
	 */
	public void mouseExited(MouseEvent me) {

	}

	// Static icon accessors -----------------------------------------------

	private Icon makeIcon(final String gifFile) throws IOException {

		/*
		 * Copy resource into a byte array. This is necessary because several
		 * browsers consider Class.getResource a security risk because it can be
		 * used to load additional classes. Class.getResourceAsStream just
		 * returns raw bytes, which we can convert to an image.
		 */
		InputStream resource = RelativeImageView.class.getResourceAsStream(gifFile);
		if (resource == null)
			return null;

		BufferedInputStream in = new BufferedInputStream(resource);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int n;
		while ((n = in.read(buffer)) > 0) {
			out.write(buffer, 0, n);
		}
		in.close();
		out.flush();

		buffer = out.toByteArray();
		if (buffer.length == 0) {
			log.warn(gifFile + " is zero-length");
			return null;
		}

		return new ImageIcon(buffer);
	}

	private void loadImageStatusIcons() {

		try {
			if (sPendingImageIcon == null) {
				sPendingImageIcon = makeIcon(PENDING_IMAGE_SRC);
			}
			if (sMissingImageIcon == null) {
				sMissingImageIcon = makeIcon(MISSING_IMAGE_SRC);
			}
		}
		catch (Exception e) {
			log.error("Couldn't load image icons");
		}
	}

	/**
	 * @return returns the stylesheet for this document
	 */
	protected StyleSheet getStyleSheet() {

		HTMLDocument doc = (HTMLDocument) getDocument();
		return doc.getStyleSheet();
	}
}
