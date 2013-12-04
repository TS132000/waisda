/*  This file is part of Waisda 

    Copyright (c) 2012 Netherlands Institute for Sound and Vision
    https://github.com/beeldengeluid/waisda
	
    Waisda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Waisda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Waisda.  If not, see <http://www.gnu.org/licenses/>.
*/

package nl.waisda.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.tool.hbm2x.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.waisda.domain.TagEntry;
import nl.waisda.model.*;
import nl.waisda.model.Value;
import nl.waisda.repositories.ParticipantRepository;
import nl.waisda.repositories.TagEntryRepository;
import nl.waisda.repositories.UserRepository;

@Service
public class ScoringService implements ScoringServiceIF, InitializingBean {

	private static final Logger log = Logger.getLogger(ScoringService.class);

	@Autowired
	private TagEntryRepository tagEntryRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ParticipantRepository participantRepo;

	@org.springframework.beans.factory.annotation.Value("${waisda.matcher.specialdictionaries}")
	private String specialDictionaries;
	private Set<String> specialDictionaryList;

	private Value<GlobalStats> globalStatsCache;

	{
		Value<GlobalStats> fetchGlobalStats = new Value<GlobalStats>() {

			@Override
			public GlobalStats get() {
				int countTags = tagEntryRepo.countTags();
				int countMatches = tagEntryRepo.countMatches();
				List<TagCloudItem> tagCloud = tagEntryRepo.getTagCloud();
				TopScores topScores = userRepo.getTopScores();
				int currentlyPlaying = participantRepo.countCurrentlyPlaying();
				return new GlobalStats(countTags, countMatches, tagCloud,
						topScores, currentlyPlaying);
			}
		};
		globalStatsCache = new Cache<GlobalStats>(fetchGlobalStats, 10000);
	}

    @Override
    public void afterPropertiesSet() throws Exception {
        // create special dictionary names list
        if (specialDictionaryList == null) {
            specialDictionaryList = new HashSet<String>() ;
        }
        // split the onput dictionaries
        if (StringUtils.isNotEmpty(specialDictionaries)) {

            String[] specialDictionarySplit = specialDictionaries.split(",");
            for (String entry : specialDictionarySplit) {
                 specialDictionaryList.add(entry);
            }
        }
    }

    public void updateDictionary(TagEntry tagEntry) {
        List<String> dictionaryEntries = tagEntryRepo.getDictionariesContaining(tagEntry.getNormalizedTag());
        if (dictionaryEntries.size() > 0) {
            // check for special dictionary cases first
            for (String entry : dictionaryEntries) {
                if (specialDictionaryList.contains(entry)) {
                    // yes, matched!
                    tagEntry.setSpecialMatch(true);
                    tagEntry.setDictionary(entry);
                    break;
                }
            }
            // if this is no special case, just pick the first one
            if (tagEntry.getDictionary() == null) {
                // If multiple entries are found, use only the first one.
                tagEntry.setSpecialMatch(false);
                tagEntry.setDictionary(dictionaryEntries.get(0));
            }
        }
	}

    public boolean isSpecialDictionaryMatch(final String dictionaryName) {
        if (StringUtils.isNotEmpty(dictionaryName)) {
            return specialDictionaryList.contains(dictionaryName);
        }
        return false;
    }

	@Transactional
	public void updateMatchAndStore(TagEntry tagEntry,
			boolean updateReverseMatches) {

		// This method might be called as a result of a user merge. Check if we
		// already have a good match.
		TagEntry currentMatch = tagEntry.getMatchingTagEntry();
		if (currentMatch != null) {
			if (currentMatch.getOwner().getId() != tagEntry.getOwner().getId()) {
				// The old match is still okay.
				return;
			} else {
				// Old match is now a match with same owner. Try and find a new
				// one.
				tagEntry.setMatchingTagEntry(null);
			}
		}

		// Fetch all possible matches.
		List<TagEntry> matches = tagEntryRepo.getMatches(tagEntry.getGame()
				.getVideo().getId(), tagEntry.getNormalizedTag(),
				tagEntry.getGameTime());

		// First try and find a literal match from the user's history. If found,
		// awards no points.
		for (TagEntry match : matches) {
			if (match.getId() != tagEntry.getId()
					&& match.getOwner().getId() == tagEntry.getOwner().getId()
					&& match.getNormalizedTag().equals(
							tagEntry.getNormalizedTag())) {
				// Literal match with a previous entry by the same owner. Set
				// forward matching tag entry, awarding no points. Don't set
				// reverse match to allow for real matches later on.
				tagEntry.setMatchingTagEntry(match);
				break;
			}
		}

		// If no match is found yet, try and find one of another user.
		if (tagEntry.getMatchingTagEntry() == null) {
			for (TagEntry match : matches) {
				if (match.getOwner().getId() != tagEntry.getOwner().getId()) {
					tagEntry.setMatchingTagEntry(match);
					break;
				}
			}
		}

		// If still no match is found, tag entry is a pioneer and will earn more
		// points once a reverse match is found.
		if (tagEntry.getMatchingTagEntry() == null) {
			tagEntry.setPioneer(true);
		}

		tagEntry.updateScore();
		tagEntryRepo.store(tagEntry);

		if (updateReverseMatches && tagEntry.isOriginal()) {
			for (TagEntry match : matches) {
				if (match.getMatchingTagEntry() == null) {
					log.info(String.format("Awarding pioneer points to tag %d",
							match.getId()));
					match.setMatchingTagEntry(tagEntry);
					match.updateScore();
					tagEntryRepo.store(match);
				}
			}
		}
	}

	public GlobalStats getGlobalStats() {
		return globalStatsCache.get();
	}

}
