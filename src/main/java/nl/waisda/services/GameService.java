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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.waisda.domain.Game;
import nl.waisda.domain.TagEntry;
import nl.waisda.domain.User;
import nl.waisda.domain.Video;
import nl.waisda.exceptions.NotFoundException;
import nl.waisda.model.Recap;
import nl.waisda.model.TagEntrySummary;
import nl.waisda.repositories.GameRepository;
import nl.waisda.repositories.TagEntryRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GameService {

	private Logger log = Logger.getLogger(GameService.class);

	@Autowired
	private GameRepository gameRepo;

	@Autowired
	private TagEntryRepository tagRepo;

	@Transactional
	public Game createGame(User user, Video video, Integer scoreToBeat) throws NotFoundException {
		if (!video.isEnabled()) {
			log.info(String.format("Ignoring request for new game for disabled video %d", video.getId()));
			throw new NotFoundException();
		}

		Game game = new Game();

		game.setInitiator(user);
		game.setScoreToBeat(scoreToBeat);

		game.setStart(new Date());
		game.setVideo(video);

		gameRepo.store(game);

		log.info(String.format(
				"Created new game %d for video %d and user %d",
				game.getId(), video.getId(), user.getId()));

		return game;
	}

	public Game getGameById(int gameId) {
		return gameRepo.getById(gameId);
	}

	public Recap getRecap(Game game, User owner) {
		// Prepare all the information needed for the Recap.
		List<TagEntry> entries = tagRepo.getEntries(game.getId());
		List<TagEntry> ownerEntries = new ArrayList<TagEntry>(entries.size());
		int ownerScore = 0;

		for (TagEntry tag : entries) {
			User u = tag.getOwner();

			if (u.getId() == owner.getId()) {
				ownerEntries.add(tag);
				ownerScore += tag.getScore();
			}
		}

		Recap recap = new Recap();
		recap.setGame(game);
		recap.setOwner(owner);
		recap.setOwnerScore(ownerScore);
		recap.setTagEntries(ownerEntries);
		recap.setSummary(TagEntrySummary.fromEntries(ownerEntries));
		return recap;
	}

}