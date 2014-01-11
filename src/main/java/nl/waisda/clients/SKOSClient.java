package nl.waisda.clients;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("skosRestClient")
public class SKOSClient implements MatchingClient {
	
	@Value("${waisda.matcher.skos.restservice.url}")
	private String conceptsServiceUrl;

	@Override
	public RDF fetchConceptsWithPrefLabelContaining(String tag) throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		URI uri = new URI(conceptsServiceUrl + tag);
		ResponseEntity<RDF> result = restTemplate.getForEntity(uri, RDF.class);
		return result.getBody();
	}
}
