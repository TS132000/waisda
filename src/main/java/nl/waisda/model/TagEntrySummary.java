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

package nl.waisda.model;

import java.util.List;

import nl.waisda.domain.TagEntry;


/**
 * Summary of a game, containing various statistics on the numbers of tags
 * entered.
 */
public class TagEntrySummary {

	/** The total number of tags. */
	private int countTags;

	/** The number of tags that did not match in any way. */
	private int countEmptyTags;

	/** The number of tags that matched another player's tag. */
	private int countMatchingTags;

	/** The number of tags that matched another player's tag and were pioneers. */
	private int countPioneerTags;

	/** The number of dictionary matches, for each dictionary type. */
	private int countDictionaryMatches;

	private TagEntrySummary() {
	}

	/** Creates a tag entry summary from a list of tag entries. */
	public static TagEntrySummary fromEntries(List<TagEntry> entries) {
		TagEntrySummary summary = new TagEntrySummary();
		for (TagEntry tag : entries) {
			summary.countTags++;
			if (tag.getMatchingTagEntry() != null) {
				summary.countMatchingTags++;
				if (tag.isPioneer()) {
					summary.countPioneerTags++;
				}
			}

			String t = tag.getDictionary();
			if (t != null) {
				summary.countDictionaryMatches++;
			}
			if (t == null && tag.getMatchingTagEntry() == null) {
				summary.countEmptyTags++;
			}
		}
		return summary;
	}

	public int getCountTags() {
		return countTags;
	}

	public int getCountEmptyTags() {
		return countEmptyTags;
	}

	public int getCountMatchingTags() {
		return countMatchingTags;
	}

	public int getCountPioneerTags() {
		return countPioneerTags;
	}

	public int getCountDictionaryMatches() {
		return countDictionaryMatches;
	}

}
