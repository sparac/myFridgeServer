package hr.fer.ztel.myFridge.DAO;

import java.util.List;

import hr.fer.ztel.myFridge.data.UserFood;

public interface IUserFoodDAO extends IGenericDAO<UserFood, Integer> {

	public List<UserFood> findByUserId(Integer id);
	
	public List<UserFood> findAllNotificationCandidates();
}
