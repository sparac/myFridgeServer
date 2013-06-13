package hr.fer.ztel.myFridge.DAO;

import hr.fer.ztel.myFridge.data.User;
import org.hibernate.Query;

public class UserDAOImpl extends GenericDAOImpl<User, Integer> implements IUserDAO {

	@Override
	public User findByEMail(String eMail) {
		User user = null;
		String sql = "SELECT u FROM User u WHERE u.eMail = :eMail";
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("eMail", eMail);

		user = findOne(query);
		return user;
	}

	@Override
	public User findByUserName(String userName) {
		User user = null;
		String sql = "SELECT u FROM User u WHERE u.username = :userName";
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("userName", userName);

		user = findOne(query);
		return user;
	}

}
