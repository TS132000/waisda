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

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import nl.waisda.domain.Participant;
import nl.waisda.domain.User;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ParticipantRepository extends AbstractRepository<Participant> {

	private Logger log = Logger.getLogger(ParticipantRepository.class);

	public ParticipantRepository() {
		super(Participant.class);
	}

	@Transactional
	@Override
	public void store(Participant entity) {
		super.store(entity);
	}

	public Participant get(int userId, int gameId) {
		TypedQuery<Participant> query = getEntityManager().createQuery(
				"SELECT p FROM Participant p "
						+ "WHERE p.user.id = :userId AND p.game.id = :gameId",
				Participant.class);
		query.setParameter("userId", userId);
		query.setParameter("gameId", gameId);
		List<Participant> results = query.getResultList();
		log.info(String.format("Found %d matching participants", results.size()));
		if (results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
	}

	@Transactional
	public void moveParticipants(User source, User target) {
		Query q = getEntityManager().createQuery(
				"UPDATE Participant SET user = :target WHERE user = :source");
		q.setParameter("source", source);
		q.setParameter("target", target);
		int n = q.executeUpdate();
		log.info(String.format("Moved %d participants from user %d to user %d",
				n, source.getId(), target.getId()));
	}

	public int countCurrentlyPlaying() {
		Query query = getEntityManager()
				.createNativeQuery(
						"select count(distinct p.user_id) from Participant p "
								+ "inner join Game g on p.game_id = g.id "
								+ "inner join Video v on g.video_id = v.id "
								+ "where g.start - interval 60 second <= now() "
								+ "and now() <= g.start + interval (v.duration / 1000) second");
		return ((Number) query.getSingleResult()).intValue();
	}

}