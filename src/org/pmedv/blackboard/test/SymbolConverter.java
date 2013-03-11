package org.pmedv.blackboard.test;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.pmedv.blackboard.beans.SymbolBean;

public class SymbolConverter {

	public static void main(String[] args) throws Exception {
		
//		SymbolProvider provider = new SymbolProvider();		
//		provider.loadSymbols();
//		
//		for (SymbolBean sb : provider.getSymbols()) {
//			
//			Marshaller m = JAXBContext.newInstance(SymbolBean.class).createMarshaller();
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//			
//			m.marshal(sb, new FileOutputStream(new File(provider.getSymbolsDir(),sb.getName()+".xml")));
//			
//			System.out.println(sb.getName());
//			
//		}
//		
//		System.out.println("Done.");

		class MySchemaOutputResolver extends SchemaOutputResolver {
		    public Result createOutput( String namespaceUri, String suggestedFileName ) throws IOException {
		        return new StreamResult(new File(".",suggestedFileName));
		    }
		}

		JAXBContext context = JAXBContext.newInstance(SymbolBean.class);
		context.generateSchema(new MySchemaOutputResolver());		
		
	}
	
}
