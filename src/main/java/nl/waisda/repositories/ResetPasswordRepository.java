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

import javax.persistence.TypedQuery;

import nl.waisda.domain.ResetPassword;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class ResetPasswordRepository extends AbstractRepository<ResetPassword> {

	public ResetPasswordRepository() {
		super(ResetPassword.class);
	}
	
	@Transactional
	@Override
	public void store(ResetPassword resetPassword) {
		super.store(resetPassword);
	}

	public ResetPassword getUserByEmail(String email, String key) {
		TypedQuery<ResetPassword> query = getEntityManager().createQuery("SELECT rpw FROM ResetPassword rpw WHERE rpw.resetkey = :key AND rpw.user.email = :email", ResetPassword.class);
		query.setParameter("key", key);
		query.setParameter("email", email);
		return getSingleResult(query);
	}

}