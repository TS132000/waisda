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

import nl.waisda.domain.ResetPassword;

public class ResetPasswordForm {

	private int id;
	private String plainTextKey;
	private ResetPassword resetPassword;
	private AuthForm auth = new AuthForm();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlainTextKey() {
		return plainTextKey;
	}

	public void setPlainTextKey(String plainTextKey) {
		this.plainTextKey = plainTextKey;
	}

	public ResetPassword getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(ResetPassword resetPassword) {
		this.resetPassword = resetPassword;
	}

	public AuthForm getAuth() {
		return auth;
	}

	public void setAuth(AuthForm auth) {
		this.auth = auth;
	}

}
