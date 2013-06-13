package hr.fer.ztel.myFridge.servlets;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.DAO.HibernateUtil;
import hr.fer.ztel.myFridge.business.UserManager;
import hr.fer.ztel.myFridge.data.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

@Path("/user")
public class UserResource {

	@Context
	private HttpServletRequest httpRequest;

	private final UserManager userManager = new UserManager();
	private static final Logger log = Logger.getLogger(UserResource.class.getName());

	@GET
	public Response checkCredentials() {
		log.debug("User authenticated");
		
		User user = (User) httpRequest.getAttribute("AUTHENTICATED_USER");
		//return user
		return Response.ok().entity(user).build();
//		return Response.ok().build();
	}

	@PUT
	@Path("/register")
	@Consumes(MediaType.APPLICATION_XML)
	public Response addUser(User user) throws DaoException {
		log.info("Adding user: " + user.getUsername());
		if (!userManager.addUser(user)) {
			log.info("Returning conflict for username " + user.getUsername() + ", HTTP RESPONSE 409 CONFLICT");
			return Response.status(Status.CONFLICT).build();
		}
		log.info("Added user for username " + user.getUsername() + ", HTTP RESPONSE 200 OK");
		
		HibernateUtil.closeSession();
		return Response.ok().build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateUser(User user) throws DaoException {

		if (((User) httpRequest.getAttribute("AUTHENTICATED_USER")).getUsername().equals(
				user.getUsername())) {

			log.info("Modifying user: " + user.getUsername());
			user.setId(((User) httpRequest.getAttribute("AUTHENTICATED_USER")).getId());
			userManager.updateUser(user);
			log.info("Updated user for username " + user.getUsername() + ", HTTP RESPONSE 200 OK");
			return Response.ok().build();
		} else {
			log.info("Returning forbidden for username " + user.getUsername() + ", HTTP RESPONSE 403 FORBIDDEN");
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@DELETE
	public Response deleteUser() throws DaoException {
		User authenticatedUser = (User) httpRequest.getAttribute("AUTHENTICATED_USER");
		userManager.deleteUser(authenticatedUser);
		log.info("Deleted user for username " + authenticatedUser.getUsername() + ", HTTP RESPONSE 200 OK");
		return Response.ok().build();
	}
}
