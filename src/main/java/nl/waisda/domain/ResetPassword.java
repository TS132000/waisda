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

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.jasypt.util.password.StrongPasswordEncryptor;

@Entity
public class ResetPassword {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne(optional = false)
	private User user;

	@Transient
	private String plainTextKey;

	@Basic(optional = false)
	@Column(unique = true, name = "resetKey")
	private String saltedKey;

	@Basic(optional = false)
	private Date creationDate;

	@Basic(optional = true)
	private Date resetDate;

	public ResetPassword() {

	}

	public ResetPassword(User user, String plainTextKey) {
		setUser(user);
		setPlainTextKey(plainTextKey);
		setCreationDate();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/** Only available if this object was created with a plain text key. */
	public String getPlainTextKey() {
		return plainTextKey;
	}

	public void setPlainTextKey(String plainTextKey) {
		this.plainTextKey = plainTextKey;

		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		setSaltedKey(passwordEncryptor.encryptPassword(plainTextKey));
	}

	public String getSaltedKey() {
		return saltedKey;
	}

	public void setSaltedKey(String saltedKey) {
		this.saltedKey = saltedKey;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setCreationDate() {
		setCreationDate(new Date());
	}

	public Date getResetDate() {
		return resetDate;
	}

	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}

	public void setResetDate() {
		setResetDate(new Date());
	}

	public boolean isPending() {
		return resetDate == null;
	}

	public boolean hasExpired() {
		long oneDay = 24 * 60 * 60 * 1000;
		return new Date().getTime() - getCreationDate().getTime() > oneDay;
	}

	public boolean isPlainTextKeyCorrect(String key) {
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		return encryptor.checkPassword(key, getSaltedKey());
	}

}
