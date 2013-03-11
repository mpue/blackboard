/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author Kristopher T. Beck
 */
public class ImageConverter {

    public static void main(String[] args) {
        String command = "gm convert -level 10, 0.2 ";
        JFrame view = new JFrame();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            File[] files = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (pathname.getPath().endsWith(".jpg")) {
                        return true;
                    }
                    return false;
                }
            });
            Runtime rt = Runtime.getRuntime();
            String str;
            for(int i = 0; i < files.length; i++){
                try {
                   
                    str = command + files[i].getName()
                            + " " + files[i].getName();
                    String[] cmds = str.split(" ");
                    Process p = rt.exec(cmds);
                    p.waitFor();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
