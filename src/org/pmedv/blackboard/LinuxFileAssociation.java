package org.pmedv.blackboard;

import org.pmedv.core.context.AppContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class LinuxFileAssociation {

    private static final String MIME_TYPE = "application/x-blackboard";

    private LinuxFileAssociation() {
    }

    public static void install() {
        if (!isLinux() || !AppContext.isRunningFromJar()) {
            return;
        }

        try {
            installMimeType();
            updateMimeDatabase();
            setDefaultApplication();
            installFileIcon(new File("bb.png"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void installMimeType() throws Exception {
        File packagesDir = new File(
                System.getProperty("user.home"),
                ".local/share/mime/packages"
        );

        if (!packagesDir.isDirectory() && !packagesDir.mkdirs()) {
            return;
        }

        File file = new File(packagesDir, "blackboard.xml");

        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mime-info xmlns=\"http://www.freedesktop.org/standards/shared-mime-info\">\n" +
                "    <mime-type type=\"" + MIME_TYPE + "\">\n" +
                "        <comment>BlackBoard file</comment>\n" +
                "        <icon name=\"blackboard-file\"/>\n" +
                "        <glob pattern=\"*.bb\"/>\n" +
                "    </mime-type>\n" +
                "</mime-info>\n";

        Files.writeString(
                file.toPath(),
                xml,
                StandardCharsets.UTF_8
        );
    }


    private static void updateMimeDatabase() throws Exception {
        File mimeDir = new File(
                System.getProperty("user.home"),
                ".local/share/mime"
        );

        Process process = new ProcessBuilder(
                "update-mime-database",
                mimeDir.getAbsolutePath()
        )
                .redirectErrorStream(true)
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String output = new String(
                    process.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            throw new IOException(
                    "update-mime-database failed (" +
                            exitCode + "): " + output
            );
        }
    }

    private static void installFileIcon(File icoFile) throws Exception {
        BufferedImage source = ImageIO.read(icoFile);

        int[] sizes = {16, 32, 48, 64, 128};

        for (int size : sizes) {
            File dir = new File(
                    System.getProperty("user.home"),
                    ".local/share/icons/hicolor/" +
                            size + "x" + size + "/mimetypes"
            );

            if (!dir.isDirectory() && !dir.mkdirs()) {
                continue;
            }

            BufferedImage scaled = new BufferedImage(
                    size,
                    size,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = scaled.createGraphics();
            g.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC
            );
            g.drawImage(source, 0, 0, size, size, null);
            g.dispose();

            ImageIO.write(
                    scaled,
                    "png",
                    new File(dir, "blackboard-file.png")
            );
        }
    }

    private static void setDefaultApplication() throws Exception {
        Process process = new ProcessBuilder(
                "xdg-mime",
                "default",
                AppIcon.APP_ID + ".desktop",
                MIME_TYPE
        )
                .redirectErrorStream(true)
                .start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String output = new String(
                    process.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            throw new IOException(
                    "xdg-mime failed (" + exitCode + "): " + output
            );
        }
    }

    private static boolean isLinux() {
        return System.getProperty("os.name", "")
                .toLowerCase()
                .contains("linux");
    }
}