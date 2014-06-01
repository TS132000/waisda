package nl.waisda.clients;

import static nl.waisda.clients.RDFConst.ELEMENT_DESCRIPTION;
import static nl.waisda.clients.RDFConst.ELEMENT_IN_SCHEME;
import static nl.waisda.clients.RDFConst.RDF_NS;
import static nl.waisda.clients.RDFConst.SKOS_NS;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
	
	public boolean containsAttrAboutWhichStartsWith(String beginOfAboutValue) {
		return this.getAbout() != null && this.getAbout().startsWith(beginOfAboutValue);
	}

	public boolean containsAttrAboutWhichStartsWith(List<String> prefixesAboutTag) {
		if (this.getAbout() != null && prefixesAboutTag != null) {
			for (String prefixAboutTag : prefixesAboutTag) {
				if (this.getAbout().startsWith(prefixAboutTag)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean containsInSchemeTagWithResourceAttr() {
		return this.getInScheme() != null && this.getInScheme().getResource() != null;
	}
}