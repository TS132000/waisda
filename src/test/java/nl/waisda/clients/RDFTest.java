package nl.waisda.clients;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

public class RDFTest {
	private static final String XML_RESOURCE = "concepts_preflabel_dood.xml";

	@Test
	public void testMapping() {
		List<String> expectedAbouts = new ArrayList<String>();
		expectedAbouts.add("http://data.beeldengeluid.nl/gtaa/25164");
		expectedAbouts.add("http://data.beeldengeluid.nl/gtaa/215866");
		expectedAbouts.add("http://data.beeldengeluid.nl/expired/174948");
		expectedAbouts.add("http://data.beeldengeluid.nl/expired/208359");

		List<String> expectedResources = new ArrayList<String>();
		expectedResources.add("http://data.beeldengeluid.nl/gtaa/Onderwerpen");
		expectedResources.add("http://data.beeldengeluid.nl/gtaa/OnderwerpenBenG");
		expectedResources.add("http://data.beeldengeluid.nl/expired/TVtrefwoorden_OUD");
		expectedResources.add("http://data.beeldengeluid.nl/expired/RVDtrefwoorden_OUD");

		try {
			JAXBContext jc;
			jc = JAXBContext.newInstance(RDF.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			InputStream xmlInputStream = this.getClass().getResourceAsStream(XML_RESOURCE);
			RDF rdf = (RDF) unmarshaller.unmarshal(xmlInputStream);

			Assert.assertEquals(4, rdf.getDescriptions().size());

			for (Description description : rdf.getDescriptions()) {
				Assert.assertTrue(expectedAbouts.contains(description.getAbout()));
				expectedAbouts.remove(description.getAbout());

				String resource = description.getInScheme().getResource();
				Assert.assertTrue(expectedResources.contains(resource));
				expectedResources.remove(resource);

			}

			Assert.assertTrue(expectedAbouts.isEmpty());
			Assert.assertTrue(expectedResources.isEmpty());

		} catch (JAXBException e) {
			fail(e.getMessage());
		}
	}

}
