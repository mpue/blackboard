package org.pmedv.blackboard.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NGSpiceTest {

	public static void main(String[] args) throws Exception {
		
		final String circuitLoc = "./spice/simple_rc.cir";
		
		String[] command = { "./spice/ngspice.exe", "-p", };
		
		Process proc = Runtime.getRuntime().exec(command);
		
		StringBuffer outputReport = new StringBuffer();
		StringBuffer errorBuffer  = new StringBuffer();
		
		int inBuffer, errBuffer;
		
        InputStream is = proc.getInputStream();
        InputStream es = proc.getErrorStream();
        
        OutputStream os = proc.getOutputStream();
        

        try {
            while ((inBuffer = is.read()) != -1) {
                outputReport.append((char) inBuffer);
            }

            while ((errBuffer = es.read()) != -1) {
                errorBuffer.append((char) errBuffer);
            }

        } 
        catch (IOException e) {
        	e.printStackTrace();
        }
        
        System.out.println(outputReport);
        System.out.println(errorBuffer);
		
		
		
		
	}
	
}
