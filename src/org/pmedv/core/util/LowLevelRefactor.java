package org.pmedv.core.util;

import java.io.File;
import java.util.LinkedList;

public class LowLevelRefactor {

	private static final String location = "c:\\devel\\vcprojects\\Fdt.Demo";
	
	
	public static void main(String[] args) {
		
		final String search = "Fdt.Demo";
		final String replace = "Sopas";
		
		LinkedList<File> files = new LinkedList<File>();
		
		System.out.println("Looking for files");
		FileUtils.findFile(files, new File(location),".cs",true, true);
		
		System.out.println("replacing "+search+" with "+replace);
		
		int count = 0;
		
		for (File file : files ) {
			System.out.println("("+(count+1)+"/"+files.size()+") Processing "+file);		
			String content = FileUtils.readFile(file);			
			content = content.replaceAll(search, replace);			
			FileUtils.writeFile(new File(file.getAbsolutePath()), content);
			count++;
		}
		
		System.out.println("done.");
		
	}
	
}
