package nl.waisda.services;

import java.util.List;

public interface SKOSServiceIF {

	List<String> getDictionariesContaining(String normalizedTag);

	List<String> getDictionariesContaining(String normalizedTag, List<String> skosServiceUrls);

}
