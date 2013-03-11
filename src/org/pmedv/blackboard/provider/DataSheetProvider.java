/**

	BlackBoard breadboard designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 */
package org.pmedv.blackboard.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.beans.DatasheetBean;
import org.pmedv.blackboard.beans.DatasheetList;
import org.pmedv.core.context.AppContext;

/**
 * <p>
 * The <code>DataSheetProvider</code> provides access to the
 * {@link DatasheetList}.
 * </p>
 * <p>
 * It allows to load and store the {@link DatasheetList} as well as adding and
 * removing {@link DatasheetBean} objects.
 * </p>
 * <p>
 * This is a spring managed bean and is handled as a singleton.
 * </p>
 * 
 * @author
 * 
 * TODO : Make this provider inherit from {@link AbstractElementProvider} and
 * store {@link DatasheetBean} objects into single files.
 * 
 */
public class DataSheetProvider {

	private static final Log log = LogFactory.getLog(DataSheetProvider.class);

	private DatasheetList datasheetList;

	private final File sheetsDir;
	private final File sheetFile;

	public DataSheetProvider() {
		File workDir = new File(System.getProperty("user.home") + "/." + AppContext.getName());
		log.info("Working directory " + workDir.getAbsolutePath());
		sheetsDir = new File(workDir, "datasheets");
		sheetFile = new File(sheetsDir, "sheets.xml");
	}

	public void loadSheets() throws JAXBException, IOException {
		if (!sheetFile.exists()) {
			createDefaultSheetList(sheetFile);
		}
		else {
			loadSheetList(sheetFile);
		}
	}

	private void loadSheetList(File sheetFile) throws JAXBException {
		Unmarshaller u = (Unmarshaller) JAXBContext.newInstance(DatasheetList.class).createUnmarshaller();
		DatasheetList sheetList = (DatasheetList) u.unmarshal(sheetFile);
		this.datasheetList = sheetList;
	}

	private void createDefaultSheetList(File sheetFile) throws JAXBException, IOException {

		datasheetList = new DatasheetList();

		ArrayList<DatasheetBean> sheets = new ArrayList<DatasheetBean>();
		datasheetList.setDatasheets(sheets);

		Marshaller m = (Marshaller) JAXBContext.newInstance(DatasheetList.class).createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(datasheetList, new FileOutputStream(sheetFile));

	}

	public void addSheet(DatasheetBean sheet) {
		datasheetList.getDatasheets().add(sheet);
	}

	public void removeSheet(DatasheetBean sheet) {
		datasheetList.getDatasheets().remove(sheet);
	}

	public void storeSheetList() throws Exception {
		if (!sheetFile.exists()) {
			createDefaultSheetList(sheetFile);
		}
		else {
			Marshaller m = (Marshaller) JAXBContext.newInstance(DatasheetList.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(datasheetList, new FileOutputStream(sheetFile));
		}

	}

	public DatasheetList getDatasheetList() {
		return datasheetList;
	}

	public void setDatasheets(DatasheetList datasheets) {
		this.datasheetList = datasheets;
	}

}
