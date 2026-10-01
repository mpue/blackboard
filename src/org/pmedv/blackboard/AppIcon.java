package org.pmedv.blackboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.pmedv.core.app.AbstractApplication;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.NativeWindowIcon;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * BlackBoard application icon. Linux follows the desktop color scheme;
 * Windows uses the opposite of the taskbar theme
 * ({@code SystemUsesLightTheme}) for contrast. GNOME matches a
 * {@code .desktop} file via WM_CLASS.
 */
public final class AppIcon {

	public static final String APP_ID = "org.pmedv.blackboard";

	private static List<Image> cached;
	private static Boolean cachedDark;

	private AppIcon() {
	}

	/** Call from {@code main} after AWT is up, before the first JFrame is shown. */
	public static void applyEarly() {
		NativeWindowIcon.applyAwtAppId(APP_ID);
		if (!NativeWindowIcon.isLinux()) {
			return;
		}
		try {
			installGnomeLauncher();
		}
		catch (Exception ignored) {
		}
	}

	public static void apply(Window window) {
		NativeWindowIcon.apply(window, APP_ID, images());
	}

	public static List<Image> images() {
		boolean dark = useDarkIcon();
		if (cached != null && cachedDark != null && cachedDark == dark) {
			return cached;
		}
		cachedDark = dark;
		cached = new ArrayList<>();
		int[] sizes = { 16, 24, 32, 48 };
		for (int size : sizes) {
			BufferedImage img = paint(dark, size);
			if (img != null) {
				cached.add(img);
			}
		}
		return cached;
	}

	private static boolean useDarkIcon() {
		if (NativeWindowIcon.isWindows()) {
			return !windowsSystemDark();
		}
		return linuxOsDark();
	}

	private static boolean linuxOsDark() {
		try {
			String scheme = gsettings("color-scheme").toLowerCase();
			if (scheme.contains("prefer-dark")) {
				return true;
			}
			if (scheme.contains("prefer-light")) {
				return false;
			}
			return gsettings("gtk-theme").toLowerCase().contains("dark");
		}
		catch (Exception ignored) {
			return false;
		}
	}

	private static String gsettings(String key) throws Exception {
		Process p = new ProcessBuilder("gsettings", "get", "org.gnome.desktop.interface", key).start();
		return new String(p.getInputStream().readAllBytes()).trim();
	}

	private static boolean windowsSystemDark() {
		try {
			String path = "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize";
			if (Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, path, "SystemUsesLightTheme")) {
				return Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, path, "SystemUsesLightTheme") == 0;
			}
		}
		catch (Exception ignored) {
		}
		return false;
	}

	private static void installGnomeLauncher() throws Exception {
		File installDir = AbstractApplication.detectInstallDir();

		if (!AppContext.isRunningFromJar()) {
			return;
		}

		File startScript = new File(installDir, "Linux_Start.sh");
		File template = new File(installDir, APP_ID + ".desktop");

		if (!startScript.isFile() || !template.isFile()) {
			return;
		}

		boolean dark = useDarkIcon();

		File iconDir = new File(System.getProperty("user.home"), ".local/share/blackboard");
		File icon = new File(iconDir, dark ? "icon-dark.png" : "icon-light.png");

		BufferedImage image = paint(dark, 48);
		if (image != null && (iconDir.isDirectory() || iconDir.mkdirs())) {
			ImageIO.write(image, "png", icon);
		}

		File applicationsDir = new File(System.getProperty("user.home"), ".local/share/applications");
		if (!applicationsDir.isDirectory() && !applicationsDir.mkdirs()) {
			return;
		}

		File desktop = new File(applicationsDir, APP_ID + ".desktop");
		String text = Files.readString(template.toPath());
		String exec =
				"Exec=/bin/bash \"" + startScript.getAbsolutePath() + "\" %f\n" +
				"Path=" + installDir.getAbsolutePath();
		if (icon.isFile()) {
			exec += "\nIcon=" + icon.getAbsolutePath();
		}
		Files.writeString(desktop.toPath(), text.replace("Exec=Linux_Start.sh %f", exec));
	}

	private static BufferedImage paint(boolean dark, int size) {
		try (InputStream in = openSvg(dark)) {
			if (in == null) {
				return null;
			}
			FlatSVGIcon svg = new FlatSVGIcon(in);
			int src = svg.getIconWidth() > 0 ? svg.getIconWidth() : 300;
			int pad = 1;
			int inner = Math.max(1, size - pad * 2);
			BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.BLACK);
			g.fillOval(pad, pad, inner, inner);
			g.translate(pad, pad);
			g.scale(inner / (double) src, inner / (double) src);
			svg.paintIcon(null, g, 0, 0);
			g.dispose();
			return img;
		}
		catch (Exception ignored) {
			return null;
		}
	}

	private static InputStream openSvg(boolean dark) throws Exception {
		String name = dark ? "icons/svg/bb_dark.svg" : "icons/svg/bb.svg";
		InputStream in = AppIcon.class.getClassLoader().getResourceAsStream(name);
		if (in != null) {
			return in;
		}
		File installDir = AbstractApplication.detectInstallDir();
		File[] candidates = {
				new File("resources/" + name),
				new File(name),
				new File(installDir, name)
		};
		for (File candidate : candidates) {
			if (candidate.isFile()) {
				return new FileInputStream(candidate);
			}
		}
		return null;
	}
}
