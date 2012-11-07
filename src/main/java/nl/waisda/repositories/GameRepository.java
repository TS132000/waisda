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

package nl.waisda.repositories;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nl.waisda.domain.Game;
import nl.waisda.domain.User;
import nl.waisda.domain.Video;
import nl.waisda.model.GameScore;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class GameRepository extends AbstractRepository<Game> {

	private Logger log = Logger.getLogger(GameRepository.class);

	public GameRepository() {
		super(Game.class);
	}

	@Transactional
	public void moveGames(User source, User target) {
		Query q = getEntityManager()
				.createQuery(
						"UPDATE Game SET initiator = :target WHERE initiator = :source");
		q.setParameter("source", source);
		q.setParameter("target", target);
		int n = q.executeUpdate();
		log.info(String.format("Moved %d games from user %d to user %d", n,
				source.getId(), target.getId()));
	}

	public List<Game> getQueuesFor(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MILLISECOND, Game.QUEUE_TIME_MS);
		Date date2 = c.getTime();

		TypedQuery<Game> query = getEntityManager().createQuery(
				"SELECT g from Game g WHERE g.start BETWEEN :d1 AND :d2",
				Game.class);
		query.setParameter("d1", date);
		query.setParameter("d2", date2);
		return query.getResultList();
	}

	public List<Game> getQueuesFor(Date date, Video video) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MILLISECOND, Game.QUEUE_TIME_MS);
		Date date2 = c.getTime();

		TypedQuery<Game> query = getEntityManager()
				.createQuery(
						"SELECT g from Game g WHERE g.video = :v AND g.start BETWEEN :d1 AND :d2",
						Game.class);
		query.setParameter("v", video);
		query.setParameter("d1", date);
		query.setParameter("d2", date2);
		return query.getResultList();
	}

	public GameScore getLastGamePlayed(int userId) {
		List<GameScore> games = getRecentGames(userId, 1);
		if (games.size() > 0) {
			return games.get(0);
		} else {
			return null;
		}
	}

	public List<GameScore> getRecentGames(int userId, int amount) {
		String q = "SELECT g, SUM(t.score), COUNT(t) FROM TagEntry t "
				+ "INNER JOIN t.game g WHERE t.owner.id = :userId "
				+ "GROUP BY g.id ORDER BY g.start DESC";
		@SuppressWarnings("unchecked")
		List<Object[]> rows = getEntityManager().createQuery(q)
				.setParameter("userId", userId).setMaxResults(amount)
				.getResultList();
		List<GameScore> gss = new ArrayList<GameScore>(rows.size());
		for (Object[] row : rows) {
			Game game = (Game) row[0];
			Long score = (Long) row[1];
			Long count = (Long) row[2];
			gss.add(new GameScore(game, score.intValue(), count.intValue()));
		}
		return gss;
	}

}
