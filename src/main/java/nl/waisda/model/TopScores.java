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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nl.waisda.domain.User;
import nl.waisda.domain.UserScore;



public class TopScores {

	private ArrayList<UserScore> topscores;

	private Map<Integer, Integer> userToPos;

	/** Demand an ArrayList for fast random access. */
	public TopScores(ArrayList<UserScore> scores) {
		this.topscores = scores;
		userToPos = new TreeMap<Integer, Integer>();
		int i = 0;
		for (UserScore u : scores) {
			userToPos.put(u.getUser().getId(), i);
			i++;
		}
	}

	public List<UserScore> getTopTen() {
		int hi = Math.min(size(), 10);
		return topscores.subList(0, hi);
	}

	/** Returns the user's position, or null if not present. */
	public Integer getPosition(User user) {
		return userToPos.get(user.getId());
	}

	public int size() {
		return topscores.size();
	}

	/**
	 * Returns the direct context for this user in this scoreboard, or null if
	 * the given user is not present.
	 */
	public List<UserScore> getContext(User user) {
		Integer pos = getPosition(user);
		if (pos == null) {
			return null;
		} else {
			int lo = Math.max(0, pos - 4); // inclusive
			int hi = Math.min(size(), pos + 5); // exclusive
			return topscores.subList(lo, hi);
		}
	}

}
