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

package nl.waisda.domain;


public class UserScore implements Comparable<UserScore> {

	private User user;
	private int score;
	private int position;
	private int countTags;
	private int countMatches;

	public UserScore(User user) {
		this.user = user;
	}

	public UserScore(User user, int score) {
		this.user = user;
		this.score = score;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int gameScore) {
		this.score = gameScore;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getCountTags() {
		return countTags;
	}

	public void setCountTags(int countTags) {
		this.countTags = countTags;
	}

	public int getCountMatches() {
		return countMatches;
	}

	public void setCountMatches(int countMatches) {
		this.countMatches = countMatches;
	}

	public void count(TagEntry tag) {
		score += tag.getScore();
		countTags++;
		if (tag.getMatchingTagEntry() != null) {
			countMatches++;
		}
	}

	@Override
	public int compareTo(UserScore other) {
		return other.score - this.score;
	}

}
