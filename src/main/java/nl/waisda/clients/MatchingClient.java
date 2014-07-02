package nl.waisda.clients;

import java.net.URISyntaxException;

public interface MatchingClient {
	RDF fetchConceptsWithPrefLabelContaining(String tag) throws URISyntaxException;
	RDF fetchConceptsWithPrefLabelContaining(String tag, String conceptsServiceUrl) throws URISyntaxException;
}
