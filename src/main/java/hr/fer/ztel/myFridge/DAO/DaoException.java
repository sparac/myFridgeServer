package hr.fer.ztel.myFridge.DAO;

public class DaoException extends Exception {

	private static final long serialVersionUID = -1557825400461916016L;

	public DaoException() {
		super();
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}
}
