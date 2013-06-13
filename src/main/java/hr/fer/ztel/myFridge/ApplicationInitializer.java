package hr.fer.ztel.myFridge;

import hr.fer.ztel.myFridge.scheduling.TaskScheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Class that starts and stops the TaskScheduler object on application initialization
 * and destruction.
 * @author suncana
 *
 */
public class ApplicationInitializer implements ServletContextListener {

	private TaskScheduler taskScheduler;

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		String param = contextEvent.getServletContext().getInitParameter("notification.task.interval");

		//checking if the notification task interval parameter is valid
		if (param == null || !param.matches("^\\d+$")) {
			throw new IllegalArgumentException("Missing or invalid 'notification.task.interval' init param");
		}

		long executionInterval = Long.parseLong(param);

		taskScheduler = new TaskScheduler();
		taskScheduler.start(executionInterval);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		if (taskScheduler != null) {
			taskScheduler.stop();
		}
	}
}
