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

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UberValidator implements Validator, InitializingBean {

    @Autowired
    private List<Validator> validators;

    public boolean supports(Class<?> clazz) {
        return true;
    }

    public void validate(Object target, Errors errors) {
        if (validators != null) {
        	for (Validator validator : validators) {
        		if (validator != this && validator.supports(target.getClass())) {
        			validator.validate(target, errors);
        		}
        	}
        }
    }

	@Override
	public void afterPropertiesSet() throws Exception {
	}
}