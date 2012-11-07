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

public class GlobalStats {


	private final int totalTags;
	private final int totalMatches;
	private List<TagCloudItem> tagCloud;
	private TopScores topScores;
	private final int currentlyPlaying;

	public GlobalStats(int totalTags, int totalMatches,
			List<TagCloudItem> tagCloud, TopScores topScores,
			int currentlyPlaying) {
		this.totalTags = totalTags;
		this.totalMatches = totalMatches;
		this.tagCloud = tagCloud;
		this.topScores = topScores;
		this.currentlyPlaying = currentlyPlaying;
	}

	public int getTotalTags() {
		return totalTags;
	}

	public int getTotalMatches() {
		return totalMatches;
	}

	public static int round(int n) {
		if (n < 100) {
			return n;
		} else if (n < 1000) {
			return n / 10 * 10;
		} else if (n < 10000) {
			return n / 100 * 100;
		} else {
			return n / 1000 * 1000;
		}
	}

	public List<TagCloudItem> getTagCloud() {
		return tagCloud;
	}

	public TopScores getTopScores() {
		return topScores;
	}

	public int getCurrentlyPlaying() {
		return currentlyPlaying;
	}

}
