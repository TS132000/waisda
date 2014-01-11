package nl.waisda.clients;

import static nl.waisda.clients.RDFConst.ELEMENT_RDF;
import static nl.waisda.clients.RDFConst.RDF_NS;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = ELEMENT_RDF, namespace = RDF_NS)
@XmlAccessorType(XmlAccessType.FIELD)
public class RDF {
	
	@XmlElement(name = RDFConst.ELEMENT_DESCRIPTION, namespace = RDF_NS)
	private List<Description> descriptions;

	public List<Description> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<Description> descriptions) {
		this.descriptions = descriptions;
	}
}
