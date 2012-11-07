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

package nl.waisda.forms;

import nl.waisda.domain.User;

import org.apache.commons.lang.StringUtils;


public class AuthForm implements ApplyForm<User>, FillForm<User> {

	private User existingUser;

	private String name;
	private String password;
	private String repeatPassword;

	public User getExistingUser() {
		return existingUser;
	}

	public void setExistingUser(User existingUser) {
		this.existingUser = existingUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	
	public boolean filledPassword() {
		return !StringUtils.isBlank(getPassword()) 
			|| !StringUtils.isBlank(getRepeatPassword());
	}

	@Override
	public void applyTo(User user) {
		user.setName(name);
		if (password.length() > 0) {
			user.setPlainTextPassword(password);
		}
	}

	public void fillFrom(User user) {
		name = user.getName();
		existingUser = user;
	}

}
