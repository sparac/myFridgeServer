package hr.fer.ztel.myFridge.DAO;

import hr.fer.ztel.myFridge.data.User;

public interface IUserDAO extends IGenericDAO<User, Integer> {

	public User findByEMail(String eMail);

	public User findByUserName(String userName);

}
