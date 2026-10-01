package org.pmedv.core.util;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;

import com.formdev.flatlaf.util.UIScale;

/**
 * Scales hardcoded pixel sizes and raster icons to the desktop scale.
 * Call {@link #applyFromDesktop()} once, before the look-and-feel is installed.
 */
public final class UiScale {

	private static float desktopScale = 1f;

	private UiScale() {
	}

	/** Hardcoded layout size, multiplied by {@link #factor()}. */
	public static int px(int value) {
		return Math.round(value * factor());
	}

	/** Desktop scale, at least 1. */
	public static float factor() {
		float s = Math.max(desktopScale, flatLafScale());
		return s < 1f ? 1f : s;
	}

	/**
	 * Detects GNOME / KDE / DPI scale. On Windows, Java2D already paints at that
	 * scale, so we leave Swing at 1. Otherwise {@code flatlaf.uiScale} is set
	 * so fonts and {@link #px(int)} stay in sync.
	 */
	public static void applyFromDesktop() {
		desktopScale = detectDesktopScale();
		if (desktopScale > 1.01f) {
			System.setProperty("flatlaf.uiScale", Float.toString(desktopScale));
		}
	}

	/** Raster icon at {@link #px(int)} size; unchanged when scale is 1. */
	public static ImageIcon icon(ImageIcon icon) {
		if (icon == null || icon.getIconWidth() <= 0) {
			return icon;
		}
		int w = px(icon.getIconWidth());
		int h = px(icon.getIconHeight());
		if (w == icon.getIconWidth() && h == icon.getIconHeight()) {
			return icon;
		}
		return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	private static float detectDesktopScale() {
		// Extra uiScale on top of Java2D system scaling would double the UI.
		if (java2dAppliesSystemScale()) {
			return 1f;
		}
		float s = 1f;
		s = Math.max(s, graphicsScale());
		s = Math.max(s, envFloat("GDK_SCALE"));
		s = Math.max(s, envFloat("QT_SCALE_FACTOR"));
		s = Math.max(s, qtScreenScale());
		s = Math.max(s, gsettings("org.gnome.desktop.interface", "scaling-factor"));
		s = Math.max(s, gsettings("org.gnome.desktop.interface", "text-scaling-factor"));
		s = Math.max(s, screenDpiScale());
		return s;
	}

	/** True when Java2D already paints at the monitor scale (typical on Windows). */
	private static boolean java2dAppliesSystemScale() {
		try {
			return UIScale.isSystemScalingEnabled() && graphicsScale() > 1.01f;
		}
		catch (Exception e) {
			return false;
		}
	}

	/** Scale from the default screen's AffineTransform (HiDPI). */
	private static float graphicsScale() {
		try {
			return (float) GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice()
					.getDefaultConfiguration()
					.getDefaultTransform()
					.getScaleX();
		}
		catch (Exception e) {
			return 1f;
		}
	}

	/** FlatLaf user scale, from {@code flatlaf.uiScale} or the current LAF. */
	private static float flatLafScale() {
		try {
			return UIScale.getUserScaleFactor();
		}
		catch (Exception e) {
			return 1f;
		}
	}

	/** Toolkit DPI relative to 96 (e.g. 144 dpi → 1.5). */
	private static float screenDpiScale() {
		try {
			int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
			return dpi > 96 ? dpi / 96f : 1f;
		}
		catch (Exception e) {
			return 1f;
		}
	}

	/** Float from an environment variable such as {@code GDK_SCALE}. */
	private static float envFloat(String name) {
		return parseFloat(System.getenv(name), 1f);
	}

	/** Highest factor in {@code QT_SCREEN_SCALE_FACTORS} ({@code eDP-1=1.5;HDMI-1=2}). */
	private static float qtScreenScale() {
		String value = System.getenv("QT_SCREEN_SCALE_FACTORS");
		if (value == null || value.length() == 0) {
			return 1f;
		}
		float s = 1f;
		String[] parts = value.split("[;=]");
		for (int i = 0; i < parts.length; i++) {
			s = Math.max(s, parseFloat(parts[i], 1f));
		}
		return s;
	}

	/** GNOME setting via {@code gsettings get <schema> <key>}, e.g. scaling-factor. */
	private static float gsettings(String schema, String key) {
		try {
			Process p = new ProcessBuilder("gsettings", "get", schema, key).start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = r.readLine();
			p.waitFor();
			if (line == null) {
				return 1f;
			}
			String[] parts = line.trim().replace("'", "").split("\\s+");
			return parseFloat(parts[parts.length - 1], 1f);
		}
		catch (Exception e) {
			return 1f;
		}
	}

	private static float parseFloat(String value, float fallback) {
		if (value == null || value.length() == 0) {
			return fallback;
		}
		try {
			return Float.parseFloat(value.trim());
		}
		catch (NumberFormatException e) {
			return fallback;
		}
	}
}
