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
import java.util.List;

import javax.persistence.TypedQuery;

import nl.waisda.domain.User;
import nl.waisda.domain.UserScore;
import nl.waisda.model.TopScores;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class UserRepository extends AbstractRepository<User> {

	public UserRepository() {
		super(User.class);
	}
	
	@Transactional
	@Override
	public void store(User user) {
		super.store(user);
	}
	
	public User getUserByCredentials(String emailaddress, String password) {
		TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.email = :email AND t.password = :password", User.class);
		query.setParameter("email", emailaddress);
		query.setParameter("password", password);
		return getSingleResult(query);
	}

	public User getUserByEmail(String emailaddress) {
		TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
		query.setParameter("email", emailaddress);
		return getSingleResult(query);
	}

	public User getUserByName(String name) {
		TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.name = :name", User.class);
		query.setParameter("name", name);
		return getSingleResult(query);
	}

	/** Warning: User objects returned only have their id, name and email set! */
	public TopScores getTopScores() {
		String q = "select u.id, u.email, u.name, sum(t.score) from User u "
				+ "inner join TagEntry t on t.owner_id = u.id "
				+ "where u.email is not null and t.creationDate >= date(now() - interval 6 day) "
				+ "group by u.id having sum(t.score) > 0 order by sum(score) desc";
		@SuppressWarnings("unchecked")
		List<Object[]> results = getEntityManager().createNativeQuery(q)
				.getResultList();
		ArrayList<UserScore> scores = new ArrayList<UserScore>(results.size());
		int i = 0;
		for (Object[] row : results) {
			User u = new User();
			u.setId((int) (Integer) row[0]);
			u.setEmail((String) row[1]);
			u.setName((String) row[2]);
			int score = ((Number) row[3]).intValue();
			UserScore us = new UserScore(u, score);
			us.setPosition(i);
			scores.add(us);
			i++;
		}
		return new TopScores(scores);
	}

}
