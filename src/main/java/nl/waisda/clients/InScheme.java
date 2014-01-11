package nl.waisda.clients;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import static nl.waisda.clients.RDFConst.*;

@XmlRootElement(name = ELEMENT_IN_SCHEME, namespace  = SKOS_NS)
@XmlAccessorType(XmlAccessType.FIELD)
public class InScheme {
	@XmlAttribute(name = ATTRIBUTE_RESOURCE, namespace = RDF_NS)
	private String resource;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
}