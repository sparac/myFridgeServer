package hr.fer.ztel.myFridge.business;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.DAO.HibernateUtil;
import hr.fer.ztel.myFridge.DAO.IUserDAO;
import hr.fer.ztel.myFridge.DAO.UserDAOImpl;
import hr.fer.ztel.myFridge.data.User;
import hr.fer.ztel.myFridge.servlets.UserFoodResource;

public class UserManager {

	private static final Logger log = Logger.getLogger(UserFoodResource.class.getName());

	public User findById(Integer uId) throws DaoException {

		User user = null;
		IUserDAO userDAO = new UserDAOImpl();

		try {
			HibernateUtil.beginTransaction();
			user = userDAO.findByID(User.class, uId);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}

		return user;
	}

	public User findByUserName(String userName) throws DaoException {

		User user = null;
		IUserDAO userDAO = new UserDAOImpl();

		try {
			HibernateUtil.beginTransaction();
			user = userDAO.findByUserName(userName);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return user;
	}

	public User findUserbyEMail(String eMail) throws DaoException {
		User user = null;
		IUserDAO userDAO = new UserDAOImpl();

		try {
			HibernateUtil.beginTransaction();
			user = userDAO.findByEMail(eMail);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return user;
	}

	public boolean addUser(String username, String eMail, String password) throws DaoException {

		User user = new User();
		user.setUsername(username);
		user.seteMail(eMail);
		user.setPassword(password);

		return addUser(user);
	}

	public boolean addUser(User user) throws DaoException {
		log.debug("Checking if user already exists");
		if ((findByUserName(user.getUsername()) == null) && (findUserbyEMail(user.geteMail()) == null)) {

			IUserDAO userDAO = new UserDAOImpl();

			try {
				HibernateUtil.beginTransaction();

				userDAO.save(user);
				HibernateUtil.commitTransaction();
			} catch (HibernateException e) {
				HibernateUtil.rollbackTransaction();
				throw new DaoException(e);
			}

			log.debug("User added");
			return true;

		} else {
			log.error("User already exists");
			return false;
		}
	}

	public boolean deleteUser(User user) throws DaoException {
		IUserDAO userDAO = new UserDAOImpl();

		try {
			HibernateUtil.beginTransaction();
			userDAO.delete(user);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return true;
	}

	public boolean updateUser(User user) throws DaoException {
		IUserDAO userDAO = new UserDAOImpl();

		try {
			HibernateUtil.beginTransaction();
			userDAO.update(user);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return true;
	}
	
	

}
