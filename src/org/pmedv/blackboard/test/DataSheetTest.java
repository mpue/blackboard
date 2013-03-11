package org.pmedv.blackboard.test;

import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.provider.DataSheetProvider;

public class DataSheetTest {

	public static void main(String[] args) throws Exception {
		
		DataSheetProvider p = new DataSheetProvider();
		p.loadSheets();
		
		for (DatasheetBean d : p.getDatasheetList().getDatasheets()) {
			System.out.println(d.getName());
		}
		
//		DatasheetBean d = new DatasheetBean("ATMega128", "Datasheet for ATMega128", "c:\\sheets\\atmega128.pdf");
		
//		DatasheetBean d1 = new DatasheetBean("ATMega8", "Datasheet for ATMega8", "c:\\sheets\\atmega8.pdf");
//		DatasheetBean d2 = new DatasheetBean("ATMega16", "Datasheet for ATMega16", "c:\\sheets\\atmega16.pdf");
//		DatasheetBean d3 = new DatasheetBean("ATMega32", "Datasheet for ATMega32", "c:\\sheets\\atmega32.pdf");
//		
//		p.addSheet(d1);
//		p.addSheet(d2);
		
// 		p.addSheet(d);
//		p.storeSheetList();
		
	}
	
}
