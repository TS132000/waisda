package nl.waisda.clients;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static nl.waisda.clients.RDFConst.*;

@XmlRootElement(name = ELEMENT_DESCRIPTION, namespace = RDF_NS)
@XmlAccessorType(XmlAccessType.FIELD)
public class Description {
	@XmlAttribute(name  = "about", namespace= RDF_NS)
	private String about;
	
	@XmlElement(name = ELEMENT_IN_SCHEME, namespace = SKOS_NS)
	private InScheme inScheme;

	public InScheme getInScheme() {
		return inScheme;
	}

	public void setInScheme(InScheme inScheme) {
		this.inScheme = inScheme;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
}