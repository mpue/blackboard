package org.pmedv.core.util;

import java.awt.Image;
import java.awt.Window;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.platform.unix.X11;

/**
 * Platform window identity (AWT app id, X11 {@code WM_CLASS}) and
 * {@link Window#setIconImages(List)}.
 */
public final class NativeWindowIcon {

	private NativeWindowIcon() {
	}

	public static void applyAwtAppId(String appId) {
		if (!isLinux() || appId == null || appId.isEmpty()) {
			return;
		}
		try {
			Class<?> cls = Class.forName("sun.awt.X11.XToolkit");
			java.lang.reflect.Field f = cls.getDeclaredField("awtAppClassName");
			f.setAccessible(true);
			f.set(null, appId);
		}
		catch (Exception ignored) {
		}
	}

	public static void apply(Window window, String appId, List<Image> icons) {
		if (window == null) {
			return;
		}
		if (icons != null && !icons.isEmpty()) {
			window.setIconImages(icons);
		}
		if (!isLinux() || appId == null || appId.isEmpty()) {
			return;
		}
		try {
			if (!window.isDisplayable()) {
				window.addNotify();
			}
			setX11WmClass(window, appId);
		}
		catch (Exception ignored) {
		}
	}

	public static boolean isLinux() {
		return System.getProperty("os.name", "").toLowerCase().contains("linux");
	}

	public static boolean isWindows() {
		return System.getProperty("os.name", "").toLowerCase().contains("win");
	}

	private static void setX11WmClass(Window window, String appId) {
		long xid = Native.getWindowID(window);
		if (xid == 0L) {
			return;
		}
		X11 x11 = X11.INSTANCE;
		X11.Display dpy = x11.XOpenDisplay(null);
		if (dpy == null) {
			return;
		}
		try {
			byte[] bytes = (appId + '\0' + appId + '\0').getBytes(StandardCharsets.ISO_8859_1);
			Memory mem = new Memory(bytes.length);
			mem.write(0, bytes, 0, bytes.length);
			x11.XChangeProperty(dpy, new X11.Window(xid), X11.XA_WM_CLASS, X11.XA_STRING, 8,
					X11.PropModeReplace, mem, bytes.length);
			x11.XFlush(dpy);
		}
		finally {
			x11.XCloseDisplay(dpy);
		}
	}
}
