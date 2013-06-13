package hr.fer.ztel.myFridge.servlets;

import hr.fer.ztel.myFridge.DAO.DaoException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
public class DAOExceptionMapper implements ExceptionMapper<DaoException> {

	private static final Logger log = Logger.getLogger(DAOExceptionMapper.class.getName());

	@Override
	public Response toResponse(DaoException daoException) {

		log.error("Unexpected error, returning HTTP 500", daoException);
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

}
