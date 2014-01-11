package nl.waisda.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import nl.waisda.clients.Description;
import nl.waisda.clients.RDF;
import nl.waisda.clients.SKOSClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class SKOSService {
	private static final Logger LOG = Logger.getLogger(SKOSService.class);
	private static final String SLASH = "/";
	private static final String EMPTY = "";

	@Value("${waisda.matcher.skos.start.about.tag}")
	private String rdfAboutStart = EMPTY;
	
	@Autowired
	private SKOSClient skosMatchingClient;

	public List<String> getDictionariesContaining(String normalizedTag) {
		List<String> dictionaries = new ArrayList<String>();

		try {
			RDF rdf;
			rdf = skosMatchingClient.fetchConceptsWithPrefLabelContaining(normalizedTag);

			if (rdf != null && rdf.getDescriptions() != null) {

				List<Description> descriptions = rdf.getDescriptions();

				for (Description description : descriptions) {
					if (description.getAbout() != null && description.getAbout().startsWith(rdfAboutStart)) {
						if (description.getInScheme() != null && description.getInScheme().getResource() != null) {
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

	public void setRdfAboutStart(String rdfAboutStart) {
		this.rdfAboutStart = rdfAboutStart;
	}
}
