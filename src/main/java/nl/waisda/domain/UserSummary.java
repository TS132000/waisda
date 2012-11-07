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

import java.util.Comparator;

public class UserSummary {

	public static final UserSummary GHOST = new UserSummary(0, "Previous players", 0,
			"http://www.gravatar.com/avatar/0?d=mm&s=30");

	private int id;
	private String name;
	private int gameScore;
	private String smallAvatarUrl;

	public UserSummary(int id, String name, int gameScore, String smallAvatarUrl) {
		this.id = id;
		this.name = name;
		this.gameScore = gameScore;
		this.smallAvatarUrl = smallAvatarUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGameScore() {
		return gameScore;
	}

	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}

	public String getSmallAvatarUrl() {
		return smallAvatarUrl;
	}

	public void setSmallAvatarUrl(String smallAvatarUrl) {
		this.smallAvatarUrl = smallAvatarUrl;
	}

	/**
	 * Sorteert van grote scores naar kleine scores.
	 */
	public static Comparator<UserSummary> COMPARE_BY_GAME_SCORE = new Comparator<UserSummary>() {

		@Override
		public int compare(UserSummary x, UserSummary y) {
			return y.gameScore - x.gameScore;
		}
	};

}
