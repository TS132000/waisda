package nl.waisda.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import nl.waisda.clients.Description;
import nl.waisda.clients.InScheme;
import nl.waisda.clients.RDF;
import nl.waisda.clients.SKOSClient;

import org.hibernate.property.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SKOSServiceTest {
	private static final String NORMALIZED_TAG = "test";
	private static final String VALID_ABOUT = "http://data.beeldengeluid.nl/gtaa/xyz";
	private static final String INVALID_ABOUT = "http://data.beeldengeluid.nl/expired/xyz";
	private static final String START_RESOURCE = "http://data.beeldengeluid.nl/gtaa/";
	private static final String[] DICTIONARIES = { "Dictionary1", "Dictionary2", "Dictionary3", "Dictionary4" };

	@Mock
	private SKOSClient skosMatchingClient;
	
	@InjectMocks
	SKOSService skosService = new SKOSService();

	private RDF createRDF(List<Description> descriptions) {
		RDF rdf = new RDF();
		rdf.setDescriptions(descriptions);
		return rdf;
	}

	private List<Description> createDescriptions(String about, String[] dictionaries) {
		List<Description> descriptions = new ArrayList<Description>();
		for (String dictionary : dictionaries) {
			Description description = new Description();
			description.setAbout(about);
			InScheme inScheme = createInScheme(dictionary);
			description.setInScheme(inScheme);
			descriptions.add(description);
		}
		return descriptions;
	}

	private InScheme createInScheme(String dictionary) {
		InScheme inScheme = new InScheme();
		inScheme.setResource(START_RESOURCE + dictionary);
		return inScheme;
	}

	@Test
	public void whenRDFContainsFourValidDesciptionsAllDirectoriesShouldBeFetched() throws URISyntaxException {
		skosService.setRdfAboutStart(START_RESOURCE);
		RDF rdf = createRDF(createDescriptions(VALID_ABOUT, DICTIONARIES));
		Mockito.when(skosMatchingClient.fetchConceptsWithPrefLabelContaining(NORMALIZED_TAG)).thenReturn(rdf);
		List<String> dictionaries = skosService.getDictionariesContaining(NORMALIZED_TAG);
		for(String dic: DICTIONARIES){
			Assert.assertTrue(dictionaries.contains(dic));
		}
	}

	@Test
	public void whenRDFContainsFourNonValidDesciptionsThenDirectoriesShouldBeEmpty() throws URISyntaxException {
		skosService.setRdfAboutStart(START_RESOURCE);
		RDF rdf = createRDF(createDescriptions(INVALID_ABOUT, DICTIONARIES));
		Mockito.when(skosMatchingClient.fetchConceptsWithPrefLabelContaining(NORMALIZED_TAG)).thenReturn(rdf);
		List<String> dictionaries = skosService.getDictionariesContaining(NORMALIZED_TAG);
		Assert.assertTrue(dictionaries.isEmpty());
	}

	@Test
	public void whenRDFContainsTwoValidAndTwoInvalidDesciptionsThenDirectoriesShouldContainTwoDirectories() throws URISyntaxException {
		skosService.setRdfAboutStart(START_RESOURCE);
		List<Description> descriptions =createDescriptions(VALID_ABOUT, new String[]{DICTIONARIES[0], DICTIONARIES[1]});
		descriptions.addAll(createDescriptions(INVALID_ABOUT, new String[]{DICTIONARIES[2], DICTIONARIES[3]}));
		RDF rdf = createRDF(descriptions);
		Mockito.when(skosMatchingClient.fetchConceptsWithPrefLabelContaining(NORMALIZED_TAG)).thenReturn(rdf);
		List<String> dictionaries = skosService.getDictionariesContaining(NORMALIZED_TAG);
		Assert.assertTrue(dictionaries.size() == 2);
		Assert.assertTrue(dictionaries.contains(DICTIONARIES[0]));
		Assert.assertTrue(dictionaries.contains(DICTIONARIES[1]));
	}

	@Test
	public void whenRDFContainsNoDesciptionsThenDirectoriesShouldBeEmpty() throws URISyntaxException {
		skosService.setRdfAboutStart(START_RESOURCE);
		RDF rdf = createRDF(new ArrayList<Description>());
		Mockito.when(skosMatchingClient.fetchConceptsWithPrefLabelContaining(NORMALIZED_TAG)).thenReturn(rdf);
		List<String> dictionaries = skosService.getDictionariesContaining(NORMALIZED_TAG);
		Assert.assertTrue(dictionaries.isEmpty());
	}
}
