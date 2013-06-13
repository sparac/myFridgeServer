package hr.fer.ztel.myFridge.DAO;

import java.util.List;

import org.hibernate.Query;

import hr.fer.ztel.myFridge.data.UserFood;

public class UserFoodDAOImpl extends GenericDAOImpl<UserFood, Integer> implements IUserFoodDAO {

	@Override
	public List<UserFood> findByUserId(Integer uId) {

		List<UserFood> userFoods = null;
		String sql = "SELECT uf FROM UserFood uf WHERE uf.user.id = :uId";
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("uId", uId);
		userFoods = findMany(query);
		return userFoods;
	}

	@Override
	public List<UserFood> findAllNotificationCandidates() {
		List<UserFood> userFoods = null;
		String sql = "SELECT uf FROM UserFood uf WHERE uf.user.notifications = true AND uf.isNotified = false";
		Query query = HibernateUtil.getSession().createQuery(sql);
		userFoods = findMany(query);
		return userFoods;
	}
}
