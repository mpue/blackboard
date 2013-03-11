package org.pmedv.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Matthias Pueski (05.06.2011)
 *
 */
public class PartLoaderTest {
	
	private static ArrayList<String> getAvailablePartFiles() {
		
		ArrayList<String> partFiles = new ArrayList<String>();
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("parts");
		System.out.println("Loading parts from "+url.getPath());
		
		File partDir = new File(url.getPath());
		
		File[] files = partDir.listFiles();
		
		if (files != null)
			for (int i=0; i < files.length;i++) {
				if (files[i].getName().endsWith("xml") || files[i].getName().endsWith("XML")) {
					System.out.println("Found "+files[i].getName());
					partFiles.add(files[i].getName());
				}
			}
		
		Collections.sort(partFiles);
		
		return partFiles;
		
	}
	
	public static void main(String[] args) {		
		System.out.println("Found "+getAvailablePartFiles().size()+" parts.");		
	}
	
}
