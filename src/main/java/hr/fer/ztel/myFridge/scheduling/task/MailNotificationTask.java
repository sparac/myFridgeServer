package hr.fer.ztel.myFridge.scheduling.task;

import java.util.Calendar;
import java.util.List;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.business.UserFoodManager;
import hr.fer.ztel.myFridge.data.UserFood;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

public class MailNotificationTask implements Runnable {

	private static final Logger log = Logger.getLogger(MailNotificationTask.class.getName());

	private static final String MAIL_SERVER_HOSTNAME = "smtp.gmail.com";
	private static final int MAIL_SERVER_PORT = 587;
	private static final int MAIL_SERVER_TIMEOUT = 30 * 1000;

	private static final String MAIL_SERVER_USERNAME = "myfridge.server@gmail.com";
	private static final String MAIL_SERVER_PASSWORD = "nLQzrgImS8CbkPyl";

	private static final String MAIL_SENDER_ADDRESS = "myfridge.server@gmail.com";
	private static final String MAIL_SENDER_DISPLAY_NAME = "myFridge";

	private static final String MESSAGE_SUBJECT_FORMAT = "myFridge notification (%1$s)";
	private static final String MESSAGE_BODY_FORMAT = "Item is about to expire:\r\n\r\nName: %1$s\r\nManufacturer: %2$s\r\nDate of expiry: %3$s\r\nDate opened: %4$s\r\nExpiry after opening (in days): %5$s";

	@Override
	public void run() {

		log.info("Task executing");

		UserFoodManager userFoodManager = new UserFoodManager();

		List<UserFood> notificationCandidates = null;
		try {
			notificationCandidates = userFoodManager.retrieveAllNotificationCandidates();
		} catch (DaoException e) {
			log.error("Failed to fetch notification candidates from DB", e);
			return;
		}

		int sentNotificationCount = 0;

		for (UserFood candidate : notificationCandidates) {

			boolean isNotificationRequired = isNotificationRequired(candidate);
			if (!isNotificationRequired) {
				continue;
			}

			boolean isNotificationSent = sendNotification(candidate);
			if (!isNotificationSent) {
				continue;
			}

			try {
				candidate.setNotified(true);
				userFoodManager.updateUserFood(candidate);
			} catch (DaoException e) {
				log.error("Failed to update userfood notification state", e);
				continue;
			}

			sentNotificationCount++;
		}

		log.info("Task finished, sent " + sentNotificationCount + " notifications");
	}

	private boolean isNotificationRequired(UserFood candidate) {
		Calendar expiryDate = Calendar.getInstance();
		expiryDate.clear();
		expiryDate.setTime(candidate.getDateExpiry());

		if (candidate.getDateOpened() != null) {
			Calendar expiryAfterOpenDate = Calendar.getInstance();
			expiryAfterOpenDate.clear();
			expiryAfterOpenDate.setTime(candidate.getDateOpened());
			expiryAfterOpenDate.add(Calendar.DAY_OF_MONTH, candidate.getValidAfterOpening());

			if (expiryAfterOpenDate.before(expiryDate)) {
				expiryDate = expiryAfterOpenDate;
			}
		}

		expiryDate.add(Calendar.DAY_OF_MONTH, -3);

		Calendar now = Calendar.getInstance();

		if (expiryDate.before(now)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean sendNotification(UserFood userFood) {

		// create email
		SimpleEmail email = new SimpleEmail();

		// define server config
		email.setHostName(MAIL_SERVER_HOSTNAME);
		email.setSmtpPort(MAIL_SERVER_PORT);
		email.setSocketTimeout(MAIL_SERVER_TIMEOUT);
		email.setSocketConnectionTimeout(MAIL_SERVER_TIMEOUT);

		email.setStartTLSEnabled(true);
		email.setStartTLSRequired(true);

		email.setAuthentication(MAIL_SERVER_USERNAME, MAIL_SERVER_PASSWORD);

		// set 'from' field
		try {
			email.setFrom(MAIL_SENDER_ADDRESS, MAIL_SENDER_DISPLAY_NAME);
		} catch (EmailException e) {
			throw new IllegalStateException("Invalid sender address: " + MAIL_SENDER_ADDRESS, e);
		}

		try {
			email.addTo(userFood.getUser().geteMail());
		} catch (EmailException e) {
			log.error("Invalid recipient address '" + userFood.getUser().geteMail() + "' - entry skipped", e);
			return false;
		}

		// set 'subject' field
		email.setSubject(String.format(MESSAGE_SUBJECT_FORMAT, System.currentTimeMillis()));

		// set content and send
		try {
			String dateOpened = "N/A";
			if(userFood.getDateOpened() != null) {
				dateOpened = userFood.getDateOpened().toString();
			}
			email.setMsg(String.format(MESSAGE_BODY_FORMAT, userFood.getFood().getName(), userFood.getFood()
					.getManufacturer(), userFood.getDateExpiry(), dateOpened, userFood
					.getValidAfterOpening()));

			email.send();

			log.debug("Mail notification sent to " + userFood.getUser().geteMail());
		} catch (EmailException e) {
			log.error("Failed to send notification to " + userFood.getUser().geteMail(), e);
			return false;
		}

		return true;
	}
}
