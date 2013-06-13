package hr.fer.ztel.myFridge.DAO;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements IGenericDAO<T, ID> {

	protected Session getSession() {
		return HibernateUtil.getSession();
	}

	@Override
	public void save(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.saveOrUpdate(entity);
	}

	@Override
	public void update(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.update(entity);
	}

	@Override
	public void delete(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.delete(entity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findMany(Query query) {
		List<T> t;
		t = query.list();
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findOne(Query query) {
		T t;
		t = (T) query.uniqueResult();
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findByID(Class<T> clazz, Integer id) {
		Session hibernateSession = this.getSession();
		T t = null;
		t = (T) hibernateSession.get(clazz, id);
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(Class<T> clazz) {
		Session hibernateSession = this.getSession();
		List<T> T = null;
		Query query = hibernateSession.createQuery("from " + clazz.getName());
		T = query.list();
		return T;
	}
}