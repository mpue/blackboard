package org.pmedv.core.preferences;

import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.core.beans.FileObject;
import org.pmedv.core.context.AppContext;

/**
 * <p>
 * Simple one-dimensional preferences mechanism, needed to store and load
 * application user preferences.
 * </p>
 * <p>
 * This mechanism simply takes a HashMap and serializes it to disk.
 * </p>
 * 
 * @author Matthias Pueski (04.07.2011)
 * 
 */
public class Preferences {

	public static HashMap<String, Object> values = new HashMap<String, Object>();

	private static final Log log = LogFactory.getLog(Preferences.class);

	static {
		load();
	}

	/**
	 * Sets the preferences to the default value if no entry exists.
	 */
	private static void setDefaultPreferences() {
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.maxRecentFiles", new Integer(5));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.showNames", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.showValues", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.invertMouse", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.layerPanelPlacement", new String("left"));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.layerPanelPlacement.optionValues", new String("left,right"));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.lookAndFeel", new String("SkyBlue"));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.lookAndFeel.optionValues", new String("Nimbus,SkyBlue,System,FlatDarcula"));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.useLayerColor", new Boolean(true));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.realisticWires", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.autoHideLayers", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.dotGrid", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.showPinNames", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.showPinNumbers", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.showOrigins", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.showTooltips", new Boolean(true));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.dawContinuousLines", new Boolean(true));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.drawPins", new Boolean(false));
		setDefaultPreference("org.pmedv.blackboard.BoardDesignerPerspective.showMidpoints", new Boolean(false));
	}

	/**
	 * Set a preference value to the given default if the preference key does
	 * not exist.
	 * 
	 * @param key Key of the preference
	 * @param defaultValue value to put if the key does not exist.
	 */
	private static void setDefaultPreference(String key, Object defaultValue) {
		if (!values.containsKey(key))
			values.put(key, defaultValue);
	}

	/**
	 * Loads the preferences from user home directory. If no preferences exist,
	 * they will be created and written to disk.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void load() {
		String prefsFileLocation = System.getProperty("user.home") + "/." + AppContext.getName() + "/preferences.xml";
		File prefsFile = new File(prefsFileLocation);

		// No preferences file found: Write defaults and store preferences.
		if (!prefsFile.exists()) {
			log.info("No preferences found, using defaults.");
			setDefaultPreferences(); // Write default values
			store();
		}
		else {
			XMLDecoder dec = null;

			try {
				dec = new XMLDecoder(new BufferedInputStream(new FileInputStream(prefsFileLocation)));
				values = (HashMap) dec.readObject();
				dec.close();
			}
			catch (FileNotFoundException e) {
				log.error("Could not load preferences.");
			}

			setDefaultPreferences(); // Write defaults for missing keys.
		}
	}

	/**
	 * Stores the preferences to disk
	 */
	public static void store() {

		String prefsFile = System.getProperty("user.home") + "/." + AppContext.getName() + "/preferences.xml";

		XMLEncoder enc;

		try {
			enc = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(new File(prefsFile))));
			enc.setPersistenceDelegate(FileObject.class, new DefaultPersistenceDelegate(new String[] { "absolutePath" }));
			enc.writeObject(values);
			enc.close();

		}
		catch (FileNotFoundException e) {
			log.error("could not persist preferences.");
		}
	}
}
