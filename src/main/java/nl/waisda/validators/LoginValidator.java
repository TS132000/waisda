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

package nl.waisda.validators;

import nl.waisda.domain.User;
import nl.waisda.forms.LoginForm;
import nl.waisda.repositories.UserRepository;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class LoginValidator implements Validator {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return LoginForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		LoginForm form = (LoginForm) obj;

		User user = userRepo.getUserByEmail(form.getEmailaddress());
		if (user == null) {
			e.reject("LoginForm.invalidCredentials");
		}
		else {
			StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
			if (passwordEncryptor.checkPassword(form.getPassword(), user.getSaltedPassword())) {
				form.setUser(user);
			} else {
				e.reject("LoginForm.invalidCredentials");
			}
		}
	}

}
