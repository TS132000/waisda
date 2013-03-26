package nl.waisda.services;

import nl.waisda.domain.TagEntry;
import nl.waisda.model.GlobalStats;

/**
 * Interface, required because the service impl now implements InitializingBean too, which
 * confuses the autowirer
 * @author Danny Sedney (sdengineering77@gmail.com)
 */
public interface ScoringServiceIF {
	public static int MAX_LOOKBACK_TIME = 10000;

    void updateDictionary(TagEntry tagEntry);
	void updateMatchAndStore(TagEntry tagEntry, boolean updateReverseMatches);
	GlobalStats getGlobalStats();

	boolean isSpecialDictionaryMatch(final String dictionaryName);

}
