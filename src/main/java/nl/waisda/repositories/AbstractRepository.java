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

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.BeanWrapperImpl;

public abstract class AbstractRepository<T> {

	@PersistenceContext(unitName = "nl.waisda")
	private EntityManager em;
	
	private Class<T> tClazz;
	
	public AbstractRepository(Class<T> tClazz) {
		this.tClazz = tClazz;
	}
	
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public List<T> listAll() {
		return getEntityManager().createQuery("SELECT t FROM " + tClazz.getSimpleName() + " t", tClazz).getResultList();
	}
	
	public List<T> listRange(int offset, int count) {
		return listRange(getEntityManager().createQuery("SELECT t FROM " + tClazz.getSimpleName() + " t", tClazz), offset, count);
	}
	
	public int getCount() {
		return ((Number)getEntityManager().createQuery("SELECT COUNT(t.id) FROM " + tClazz.getSimpleName() + " t").getSingleResult()).intValue();
	}
	
	public T getById(int id) {
		return getEntityManager().find(tClazz, id);
	}
	
	public void store(T entity) {
		for (Field field : tClazz.getDeclaredFields()) {
			if (field.getAnnotation(Id.class) != null) {
				int idValue;

				try {
					idValue = ((Integer)new BeanWrapperImpl(entity).getPropertyValue(field.getName())).intValue();
				} catch (Exception e) {
					throw new RuntimeException("The AbstractRepository.store method expects the @Id annotation on an int/Integer property.");
				}
				
				if (idValue > 0) {
					getEntityManager().merge(entity);
				}
				else {
					getEntityManager().persist(entity);
				}
				
				break;
			}
		}
	}
	
	public static <T> T getSingleResult(TypedQuery<T> query) {
		try {
			return query.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	public static <T> List<T> listRange(TypedQuery<T> query, int offset, int count) {
		assert offset >= 0 : "Argument offset must be 0 or higher.";
		assert count > 0 : "Argument count must be more then 0.";
		
		return query
			.setFirstResult(offset)
			.setMaxResults(count)
			.getResultList();
	}
}
