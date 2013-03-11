/*
 * DeviceTransferHandler.java
 *
 * Created on May 13, 2007, 10:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author Kristopher T. Beck
 */
public class DeviceTransferHandler extends TransferHandler {
    /**
     * 
     */
    public static final String deviceMimeType = ";class=org.ktb.eda.Device";
    /**
     * 
     */
    public static final DataFlavor DEVICE_DATA_FLAVOR = new DataFlavor(Device.class, deviceMimeType);
    
    /** Creates a new instance of DeviceTransferHandler */
    public DeviceTransferHandler() {
    }
    
    public boolean importData(JComponent c, Transferable t) {
        if (hasDeviceFlavor(t.getTransferDataFlavors())) {
            try {
                Device dev = (Device)t.getTransferData(DEVICE_DATA_FLAVOR);
/*                if (c instanceof Schematic) {
                    Schematic sch = (Schematic)c;
                }
*/                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) { }
        }
        return false;
    }
    
    /**
     * Does the flavor list have a Color flavor?
     * @param flavors 
     * @return 
     */
    protected boolean hasDeviceFlavor(DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (DEVICE_DATA_FLAVOR.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Overridden to include a check for a color flavor.
     * @param c 
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return hasDeviceFlavor(flavors);
    }
    
}
