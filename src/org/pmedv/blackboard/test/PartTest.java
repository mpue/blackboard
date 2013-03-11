package org.pmedv.blackboard.test;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Pin;

public class PartTest {
	
	public static void main(String[] args) throws Exception {
		
		Part p = new Part();
		
		p.getConnections().getPin().add(new Pin(16,16));
		p.getConnections().getPin().add(new Pin(8,16));
		p.getConnections().getPin().add(new Pin(24,16));
		p.getConnections().getPin().add(new Pin(16,32));
		
		System.out.println(p.getConnections());
		
		Marshaller m = JAXBContext.newInstance(Part.class).createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		StringWriter s = new StringWriter();
		
		m.marshal(p, s);

		System.out.println(s.toString());
		
	}

}
