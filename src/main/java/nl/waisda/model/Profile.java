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
import nl.waisda.domain.User;
import nl.waisda.domain.UserScore;



public class Profile {

	private User user;
	private List<TagEntry> pioneerMatches;
	private List<UserScore> ranking;
	private List<GameScore> recentGames;

	public Profile(User user, List<TagEntry> pioneerMatches,
			List<UserScore> ranking, List<GameScore> recentGames) {
		this.user = user;
		this.pioneerMatches = pioneerMatches;
		this.ranking = ranking;
		this.recentGames = recentGames;
	}

	public User getUser() {
		return user;
	}

	public List<TagEntry> getPioneerMatches() {
		return pioneerMatches;
	}

	public List<UserScore> getRanking() {
		return ranking;
	}

	public List<GameScore> getRecentGames() {
		return recentGames;
	}

}
