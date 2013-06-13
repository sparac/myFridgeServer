package hr.fer.ztel.myFridge.scheduling;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hr.fer.ztel.myFridge.scheduling.task.MailNotificationTask;

import org.apache.log4j.Logger;

public class TaskScheduler {

	private static final long MIN_SCHEDULER_EXECUTION_INTERVAL = 5 * 1000;
	private static final long INITIAL_SCHEDULER_EXECUTION_DELAY = 15 * 1000;
	private static final long MAX_SCHEDULER_SHUTDOWN_DELAY = 1 * 60 * 1000;

	private static final Logger log = Logger.getLogger(TaskScheduler.class.getName());

	private ScheduledExecutorService executorService;
	private boolean isStarted = false;

	public synchronized void start(long executionInterval) {

		if (isStarted) {
			throw new IllegalStateException("Task scheduler already started");
		}

		if (executionInterval < MIN_SCHEDULER_EXECUTION_INTERVAL) {
			throw new IllegalArgumentException("executionInterval");
		}

		doStart(executionInterval);

		isStarted = true;

		log.info("Task scheduler started");
	}

	public synchronized void stop() {

		if (!isStarted) {
			return;
		}

		doStop();

		log.info("Task scheduler stopped");
	}

	private void doStart(long executionInterval) {

		MailNotificationTask mailNotificationTask = new MailNotificationTask();

		this.executorService = Executors.newSingleThreadScheduledExecutor();

		this.executorService.scheduleAtFixedRate(mailNotificationTask, INITIAL_SCHEDULER_EXECUTION_DELAY,
				executionInterval, TimeUnit.MILLISECONDS);

		log.info("MailNotificationTask scheduled, executing every " + (executionInterval / 1000) + "s");
	}

	private void doStop() {
		if (this.executorService == null) {
			return;
		}

		this.executorService.shutdown();

		try {
			this.executorService.awaitTermination(MAX_SCHEDULER_SHUTDOWN_DELAY, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ignored) {
		}

		log.info("Stopped any existing scheduled tasks");
	}
}
