package org.pmedv.core.util;

import java.awt.Component;
import java.util.logging.Level;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.springframework.context.ApplicationContext;

/**
 * Helper class to display exception information in a dialog.
 * @author Thorsten Schmidt
 */
public class ErrorUtils {
	
	private static final ApplicationContext ctx = AppContext.getContext();
	private static final ApplicationWindow win = (ApplicationWindow)ctx.getBean("applicationWindow");
	
	/**
	 * Display error dialog with exception information.
	 * Default level is <code>Level.ALL</code> and default owner is <code>GWMainFrame.getFrame()</code>.
	 * @see java.util.logging.Level
	 * @param t - Throwable
	 */
	public static void showErrorDialog(Throwable t) {
		showErrorDialog(win, t);
	}
	
	/**
	 * Display error dialog with exception information and a given level.
	 * @see java.util.logging.Level
	 * @param t - Throwable
	 * @param level - Error level
	 */
	public static void showErrorDialog(Throwable t, Level level) {
		showErrorDialog(win, t, level);
	}
	
	/**
	 * Display error dialog with exception information and a given owner component.
	 * @param owner - owner component
	 * @param t - Throwable
	 */
	public static void showErrorDialog(Component owner, Throwable t) {
		showErrorDialog(owner, t, Level.ALL);
	}
	
	/**
	 * Display error dialog with exception information and a given owner component and a given level.
	 * @see java.util.logging.Level
	 * @param owner- owner component
	 * @param t - Throwable
	 * @param level - Error level
	 */
	public static void showErrorDialog(Component owner, Throwable t, Level level) {
		ErrorInfo info = createErrorInfo(t, level);
		JXErrorPane.showDialog(owner, info);
	}

	private static ErrorInfo createErrorInfo(Throwable t, Level level) {

		String message = t.getLocalizedMessage();

		ErrorInfo info = new ErrorInfo(
				null,
				message,
				null,
				t.getClass().getName(),
				t,
				level,
				null);
		return info;
	}

}
