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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import nl.waisda.model.Util;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Game {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"EEEE d MMMM yyyy 'at' HH:mm", Util.ENGLISH_LOCALE);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne(optional = false)
	private Video video;

	@ManyToOne(optional = true)
	private User initiator;

	@Basic(optional = true)
	private Date start;

	@Basic(optional = false)
	private int countExistingVideoTags;

	/*
	 * Business logic
	 */

	@JsonIgnore
	public int getElapsed() {
		return (int) (System.currentTimeMillis() - getStart().getTime());
	}

	public boolean acceptsNewTagEntryAt(int time) {
		return 0 <= time && time <= video.getDuration();
	}

	public String getFormattedDay() {
		DateFormat df = new SimpleDateFormat("EEEE dd-MM-yyyy",
				Util.ENGLISH_LOCALE);
		String s = df.format(start);
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	public Date getEnd() {
		Calendar c = Calendar.getInstance();
		c.setTime(getStart());
		c.add(Calendar.MILLISECOND, getVideo().getDuration());
		return c.getTime();
	}

	/*
	 * Getters and setters
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	@JsonIgnore
	public User getInitiator() {
		return initiator;
	}

	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}

	public Date getStart() {
		return start;
	}

	public String getPrettyStart() {
		String s = DATE_FORMAT.format(start);
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public int getCountExistingVideoTags() {
		return countExistingVideoTags;
	}

	public void setCountExistingVideoTags(int countExistingVideoTags) {
		this.countExistingVideoTags = countExistingVideoTags;
	}

}