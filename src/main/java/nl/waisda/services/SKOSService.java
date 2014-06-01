package nl.waisda.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nl.waisda.clients.Description;
import nl.waisda.clients.RDF;
import nl.waisda.clients.SKOSClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class SKOSService implements SKOSServiceIF {
	private static final Logger LOG = Logger.getLogger(SKOSService.class);
	private static final String SLASH = "/";
	private static final String EMPTY = "";

	@Value("${waisda.matcher.skos.start.about.tag}")
	private String rdfAboutStart = EMPTY;
	
	@Resource(name="prefixesAboutTagPerSkosService")
	private Map<String, List<String>> prefixesAboutTagPerSkosService = Collections.emptyMap();

	@Autowired
	private SKOSClient skosMatchingClient;
	
	private boolean rdfContainsDescriptions(RDF rdf) {
		return rdf != null && rdf.getDescriptions() != null;
	}

	@Override
	public List<String> getDictionariesContaining(String normalizedTag) {
		List<String> dictionaries = new ArrayList<String>();

		try {
			RDF rdf = skosMatchingClient.fetchConceptsWithPrefLabelContaining(normalizedTag);

			if (rdfContainsDescriptions(rdf)) {

				List<Description> descriptions = rdf.getDescriptions();

				for (Description description : descriptions) {
					if (description.containsAttrAboutWhichStartsWith(rdfAboutStart)) {
						if (description.containsInSchemeTagWithResourceAttr()) {
							String resource = description.getInScheme().getResource();
							String[] resourceParts = resource.split(SLASH);
							if (resourceParts.length > 1) {
								String dictionary = resourceParts[resourceParts.length - 1];
								dictionaries.add(dictionary);
							}
						}
					}
				}
			}
		} catch (URISyntaxException e) {
			LOG.error(
					"Could not fetch concepts with skos matching client, an empty dictionary list will no be returned",
					e);
		}catch (RestClientException e){
			LOG.error(
					"Could not make a connection, an empty dictionary list will no be returned",
					e);
		}

		return dictionaries;
	}
	
	@Override
	public List<String> getDictionariesContaining(String normalizedTag, List<String> skosServiceUrls) {
		LOG.debug(String.format("NormalizedTag: %s, skosServiceUrls: %s", normalizedTag, skosServiceUrls ));
		
		List<String> dictionaries = new ArrayList<String>();

		for (String skosServiceUrl : skosServiceUrls) {
			LOG.debug(String.format("Using skosService: %s", skosServiceUrl));

			try {

				RDF rdf;
				rdf = skosMatchingClient.fetchConceptsWithPrefLabelContaining(normalizedTag, skosServiceUrl);

				if (rdfContainsDescriptions(rdf)) {

					List<Description> descriptions = rdf.getDescriptions(); 

					for (Description description : descriptions) {
						
						List<String> prefixesAboutTag = prefixesAboutTagPerSkosService.get(skosServiceUrl);
						LOG.debug(String.format("Using prefixesAboutTag: %s", prefixesAboutTag));
						
						if(description.containsAttrAboutWhichStartsWith(prefixesAboutTag)){
							if (description.containsInSchemeTagWithResourceAttr()) {
								String resource = description.getInScheme().getResource();
								String[] resourceParts = resource.split(SLASH);
								if (resourceParts.length > 1) {
									String dictionary = resourceParts[resourceParts.length - 1];
									dictionaries.add(dictionary);
								}
							}
						}
					}
				}
			} catch (URISyntaxException e) {
				LOG.error(
						"Could not fetch concepts with skos matching client, an empty dictionary list will now be returned",
						e);
			} catch (RestClientException e) {
				LOG.error("Could not make a connection, an empty dictionary list will no be returned", e);
			}
		}

		LOG.debug(String.format("Found dictionaries: %s", dictionaries));
		return dictionaries;
	}

	public void setRdfAboutStart(String rdfAboutStart) {
		this.rdfAboutStart = rdfAboutStart;
	}
}
