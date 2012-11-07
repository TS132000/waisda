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

public class RegisterForm implements ApplyForm<User> {

	private String email;
	private AuthForm auth = new AuthForm();
	private boolean agreeTos;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AuthForm getAuth() {
		return auth;
	}

	public void setAuth(AuthForm auth) {
		this.auth = auth;
	}

	public boolean isAgreeTos() {
		return agreeTos;
	}

	public void setAgreeTos(boolean agreeTos) {
		this.agreeTos = agreeTos;
	}

	@Override
	public void applyTo(User user) {
		user.setEmail(email);
		auth.applyTo(user);
		user.setCreationDate();
	}

}
