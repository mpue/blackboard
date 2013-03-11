package org.pmedv.blackboard.test;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import org.pmedv.blackboard.printing.ImagePrintable;
import org.pmedv.core.util.ErrorUtils;

/**
 * @author Matthias Pueski (23.10.2010)
 *
 */

public class PrintTest {
	
	public static void main(String[] args) {
		
		ImagePrintable printable = new ImagePrintable(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
		
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(printable);
		
		boolean doPrint = job.printDialog();

		if (doPrint) {
			
			try {
				job.print();
			}
			catch (PrinterException e) {
				ErrorUtils.showErrorDialog(e);
			}
			
		}		
		
	}
	

}
