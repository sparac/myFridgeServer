package hr.fer.ztel.myFridge.security;

import org.apache.log4j.Logger;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.business.UserManager;
import hr.fer.ztel.myFridge.data.User;

public class AuthenticationService {

	private static final Logger log = Logger.getLogger(AuthenticationService.class.getName());

	public final UserManager userManager;

	public AuthenticationService() {
		this.userManager = new UserManager();
	}

	public User getAuthenticatedUser(String username, String password) throws DaoException {

		User user = this.userManager.findByUserName(username);

		if (user == null) {
			log.warn("User not found for username: " + username);
			return null;
		}

		if (!user.getPassword().equalsIgnoreCase(password)) {
			log.warn("Invalid password for username: " + username);
			return null;
		}

		return user;
	}
}
