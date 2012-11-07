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

import nl.waisda.domain.Game;
import nl.waisda.domain.TagEntry;
import nl.waisda.domain.User;
import nl.waisda.domain.UserScore;



public class Recap {

	private Game game;

	private User owner;

	private int ownerScore;

	private int ownerPosition;

	private List<TagEntry> tagEntries;

	private TagEntrySummary summary;

	private List<UserScore> participants;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public int getOwnerScore() {
		return ownerScore;
	}

	public void setOwnerScore(int ownerScore) {
		this.ownerScore = ownerScore;
	}

	public int getOwnerPosition() {
		return ownerPosition;
	}

	public void setOwnerPosition(int ownerPosition) {
		this.ownerPosition = ownerPosition;
	}

	public List<TagEntry> getTagEntries() {
		return tagEntries;
	}

	public void setTagEntries(List<TagEntry> tagEntries) {
		this.tagEntries = tagEntries;
	}

	public TagEntrySummary getSummary() {
		return summary;
	}

	public void setSummary(TagEntrySummary summary) {
		this.summary = summary;
	}

	public List<UserScore> getParticipants() {
		return participants;
	}

	public void setParticipants(List<UserScore> participants) {
		this.participants = participants;
	}

	public boolean isEmpty() {
		return this.tagEntries.isEmpty();
	}

}
