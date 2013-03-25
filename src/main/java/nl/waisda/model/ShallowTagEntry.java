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

import nl.waisda.domain.TagEntry;

/** Meant for JSON generation during game updates. */
public class ShallowTagEntry {

	private int id;
	private String tag;
	private int score;
	private String dictionary;
	private boolean pioneer;
	private int gameTime;
	private String matchingTag;
	private String matchingTagOwnerName;
	private boolean specialMatch;

	public ShallowTagEntry(int id, String tag, int score, String dictionary,
			boolean pioneer, int gameTime, String matchingTag,
			String matchingTagOwnerName, boolean specialMatch) {
		this.id = id;
		this.tag = tag;
		this.score = score;
		this.dictionary = dictionary;
		this.pioneer = pioneer;
		this.gameTime = gameTime;
		this.matchingTag = matchingTag;
		this.matchingTagOwnerName = matchingTagOwnerName;
		this.specialMatch = specialMatch;
	}

	public static ShallowTagEntry fromTagEntry(TagEntry tag) {
		TagEntry match = tag.getMatchingTagEntry();
		ShallowTagEntry shallow = new ShallowTagEntry(tag.getId(),
				tag.getTag(), tag.getScore(), tag.getDictionary(),
				tag.isPioneer(), tag.getGameTime(),
				match != null ? match.getTag() : null, match != null ? match.getOwner().getName() : null,
				tag.isSpecialMatch());
		return shallow;
	}

	public int getId() {
		return id;
	}

	public String getTag() {
		return tag;
	}

	public int getScore() {
		return score;
	}

	public String getDictionary() {
		return dictionary;
	}

	public boolean isPioneer() {
		return pioneer;
	}

	public int getGameTime() {
		return gameTime;
	}

	public String getMatchingTag() {
		return matchingTag;
	}

	public String getMatchingTagOwner() {
		return matchingTagOwnerName;
	}

    public boolean isSpecialMatch() {
        return specialMatch;
    }

    public void setSpecialMatch(boolean specialMatch) {
        this.specialMatch = specialMatch;
    }
}
