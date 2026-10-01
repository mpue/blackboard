package org.pmedv.blackboard;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;
import com.jthemedetecor.OsThemeDetector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;


public final class LookAndFeelUtil {

    private static final Log log = LogFactory.getLog(LookAndFeelUtil.class);

    private LookAndFeelUtil() {} // static-only helper

    public static String getLookAndFeelOptions() {
        return "System,FlatDarcula,FlatLight,Nimbus,SkyBlue";
    }

    public static String getLookAndFeelDefault() {
        return "System";
    }

    public static void applyLookAndFeel(String laf) {
        try {
            LookAndFeel selectedLaf = selectLookAndFeel(laf);
            applyCustomTweaks(selectedLaf);
            UIManager.setLookAndFeel(selectedLaf);
        } catch (Exception e) {
            log.warn("Failed to set chosen look and feel, falling back to system.", e);
            fallbackToSystem();
        }
    }

    private static void applyCustomTweaks(LookAndFeel laf) {
        if (laf instanceof NimbusLookAndFeel || laf instanceof FlatDarculaLaf) {
            UIManager.put("Table.stripedRowBackgroundColor", Color.DARK_GRAY);
        } else {
            UIManager.put("Table.stripedRowBackgroundColor", Color.LIGHT_GRAY);
        }

        if (laf instanceof NimbusLookAndFeel) {
            UIManager.put("control", new Color(128, 128, 128));
            UIManager.put("info", new Color(128,128,128));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115,164,209));
            UIManager.put("nimbusGreen", new Color(176,179,50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
            UIManager.put("nimbusOrange", new Color(191,98,4));
            UIManager.put("nimbusRed", new Color(169,46,34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
            UIManager.put("text", new Color(230, 230, 230));
        }
    }

    private static void fallbackToSystem() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            log.error("Failed to apply system LAF — nothing more to fall back to.");
        }
    }

    private static LookAndFeel selectLookAndFeel(String laf) {

        // 1. No value or "System" → use OS detector
        if (laf == null || laf.isBlank() || laf.equalsIgnoreCase("System")) {
            return selectFromOsTheme();
        }

        // 2. Known custom mappings
        switch (laf) {
            case "Nimbus":
                return new NimbusLookAndFeel();

            case "FlatDarcula":
                return new FlatDarculaLaf();

            case "FlatLight":
                return new FlatLightLaf();

            case "SkyBlue":
                Plastic3DLookAndFeel.setPlasticTheme(new SkyBluer());
                com.jgoodies.looks.Options.setPopupDropShadowEnabled(true);
                return new Plastic3DLookAndFeel();
        }

        // 3. Unknown value → OS theme detector
        return selectFromOsTheme();
    }

    private static LookAndFeel selectFromOsTheme() {
        if (isGnomeDark()) {
            return new FlatDarculaLaf();
        }

        try {
            OsThemeDetector detector = OsThemeDetector.getDetector();
            boolean dark = detector.isDark();
            return dark ? new FlatDarculaLaf() : new FlatLightLaf();
        } catch (Exception e) {
            log.warn("OS theme detection failed, falling back to system LAF.");
            return instantiateSystemLaf();
        }
    }

    private static LookAndFeel instantiateSystemLaf() {
        try {
            return (LookAndFeel) Class.forName(
                    UIManager.getSystemLookAndFeelClassName()
            ).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load system LAF", e);
        }
    }

    private static boolean isGnomeDark() {
        try {
            // Read the GTK theme (e.g., "Adwaita-dark")
            Process p = new ProcessBuilder("gsettings",
                    "get", "org.gnome.desktop.interface", "gtk-theme").start();
            String gtkTheme = new String(p.getInputStream().readAllBytes()).trim();

            if (gtkTheme.toLowerCase().contains("dark")) {
                return true; // GTK theme is a dark variant
            }

            // GNOME 42+ supports: color-scheme 'prefer-dark'
            Process p2 = new ProcessBuilder("gsettings",
                    "get", "org.gnome.desktop.interface", "color-scheme").start();
            String colorScheme = new String(p2.getInputStream().readAllBytes()).trim();

            if (colorScheme.toLowerCase().contains("dark")) {
                return true;
            }

        } catch (Exception ignored) {}

        return false;
    }
}
