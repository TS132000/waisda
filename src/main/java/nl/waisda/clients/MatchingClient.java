package nl.waisda.clients;

import java.net.URISyntaxException;
import java.util.List;

public interface MatchingClient {
	RDF fetchConceptsWithPrefLabelContaining(String tag) throws URISyntaxException;
}
