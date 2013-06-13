package hr.fer.ztel.myFridge.business;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.DAO.FoodDAOImpl;
import hr.fer.ztel.myFridge.DAO.HibernateUtil;
import hr.fer.ztel.myFridge.DAO.IFoodDAO;
import hr.fer.ztel.myFridge.DAO.IUserDAO;
import hr.fer.ztel.myFridge.DAO.IUserFoodDAO;
import hr.fer.ztel.myFridge.DAO.UserDAOImpl;
import hr.fer.ztel.myFridge.DAO.UserFoodDAOImpl;
import hr.fer.ztel.myFridge.data.Food;
import hr.fer.ztel.myFridge.data.User;
import hr.fer.ztel.myFridge.data.UserFood;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

public class UserFoodManager {

	public UserFood searchUserFoodbyId(Integer userFoodId) throws DaoException {

		UserFood userFood = null;
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			userFood = userFoodDAO.findByID(UserFood.class, userFoodId);
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return userFood;
	}

	public List<UserFood> searchUserFoodbyUserId(Integer uId) throws DaoException {

		List<UserFood> userFoods = null;
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			userFoods = userFoodDAO.findByUserId(uId);
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return userFoods;
	}

	public UserFood addUserFoodByBarcode(User user, String barcode, Date date, Integer validAfterOpening)
			throws DaoException {

		UserFood userFood = new UserFood();

		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		IFoodDAO foodDAO = new FoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			Food food = foodDAO.findByBarcode(barcode);

			userFood.setFood(food);
			userFood.setUser(user);
			userFood.setDateExpiry(date);
			userFood.setValidAfterOpening(validAfterOpening);

			userFoodDAO.save(userFood);

			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return userFood;
	}

	public UserFood updateUserFood(UserFood userFood, Date dateOpened) throws DaoException {

		userFood.setDateOpened(dateOpened);
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			userFoodDAO.update(userFood);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return userFood;

	}

	public boolean deleteUserFood(Integer userFoodId) throws DaoException {
		UserFood userFood = searchUserFoodbyId(userFoodId);
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			userFoodDAO.delete(userFood);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return true;
	}

	public boolean deleteAllUserFood(Integer userId) throws DaoException {
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			List<UserFood> userFoods = userFoodDAO.findByUserId(userId);

			for (UserFood userFood : userFoods) {
				userFoodDAO.delete(userFood);
			}
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return true;
	}

	public List<UserFood> retrieveAllUserFood(String username) throws DaoException {
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		IUserDAO userDAO = new UserDAOImpl();
		List<UserFood> userFoods = null;
		try {
			HibernateUtil.beginTransaction();
			User user = userDAO.findByUserName(username);
			userFoods = userFoodDAO.findByUserId(user.getId());
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return userFoods;
	}

	public List<UserFood> retrieveAllNotificationCandidates() throws DaoException {
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();
		List<UserFood> userFoods = null;
		try {
			HibernateUtil.beginTransaction();
			userFoods = userFoodDAO.findAllNotificationCandidates();
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return userFoods;
	}

	public boolean updateUserFood(UserFood userFood) throws DaoException {
		IUserFoodDAO userFoodDAO = new UserFoodDAOImpl();

		try {
			HibernateUtil.beginTransaction();
			userFoodDAO.update(userFood);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return true;
	}
}
