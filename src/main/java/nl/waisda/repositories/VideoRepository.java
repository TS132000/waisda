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

import javax.persistence.TypedQuery;

import nl.waisda.domain.Video;

import org.springframework.stereotype.Repository;

@Repository
public class VideoRepository extends AbstractRepository<Video> {

	public static final int NCHANNELS = 6;

	public VideoRepository() {
		super(Video.class);
	}

	/**
	 * Currently this method makes a random, unbiased selection. If you would
	 * like to filter certain video's, or use statistics to influence the video
	 * selection, you can do that here.
	 */
	public List<Video> getFeaturedVideos() {
		String q = "SELECT v FROM Video v WHERE v.enabled = true ORDER BY RAND()";
		// Hibernate requires the "= true"

		TypedQuery<Video> query = getEntityManager()
				.createQuery(q, Video.class);
		query.setMaxResults(NCHANNELS);
		return query.getResultList();
	}

	public int getHighscore(int videoId) {
		String q = "SELECT MAX(score) FROM (SELECT SUM(t.score) AS score "
				+ "FROM TagEntry t INNER JOIN Game g on t.game_id = g.id "
				+ "WHERE g.video_id = :videoId GROUP BY g.id) games";
		Object result = getEntityManager().createNativeQuery(q)
				.setParameter("videoId", videoId).getSingleResult();
		if (result == null) {
			return 0;
		} else {
			return ((Number) result).intValue();
		}
	}

	public List<Video> getTopVideosForTag(String normalizedTag, int maxResults) {
		String q = "SELECT t.game.video FROM TagEntry t "
				+ "WHERE t.normalizedTag = :normalizedTag "
				+ "GROUP BY t.game.video.id ORDER BY COUNT(t.game.video.id) DESC";

		TypedQuery<Video> query = getEntityManager()
				.createQuery(q, Video.class);
		query.setParameter("normalizedTag", normalizedTag);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

}
