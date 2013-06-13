package hr.fer.ztel.myFridge.servlets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.business.UserFoodManager;
import hr.fer.ztel.myFridge.data.User;
import hr.fer.ztel.myFridge.data.UserFood;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

@Path("/userFood")
public class UserFoodResource {
	
	

	@Context
	private HttpServletRequest httpRequest;

	private final UserFoodManager userFoodManager = new UserFoodManager();
	private static final Logger log = Logger.getLogger(UserFoodResource.class.getName());

	@PUT
	@Path("/{barcode}/{dateExp}/{validAfterOpening}")
	@Produces(MediaType.APPLICATION_XML)
	public Response addUserFood(@PathParam("barcode") String barcode,
			@PathParam("dateExp") String dateExp,
			@PathParam("validAfterOpening") Integer validAfterOpening) throws DaoException {

		User user = (User) httpRequest.getAttribute("AUTHENTICATED_USER");
		Date date = parseDate(dateExp);
		if (date == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		log.info("Adding user food: barcode = " + barcode + ", dateExp = " + dateExp
				+ ", validAfterOpening = " + validAfterOpening);
		UserFood userFood = userFoodManager.addUserFoodByBarcode(user, barcode, date, validAfterOpening);
		
		if (userFood == null) {
			log.info("User food with " + barcode + " not added, returning HTTP RESPONSE 400 BAD REQUEST");
			return Response.status(Status.BAD_REQUEST).build();
		}
		else {
			log.info("Returning user food with barcode: " + barcode + ", HTTP RESPONSE 200 OK");
			return Response.ok().entity(userFood).build();
		}
		
	}

	@POST
	@Path("/{userFoodId}/{dateOpened}")
	public Response updateUserFood(@PathParam("userFoodId") Integer userFoodId,
			@PathParam("dateOpened") String dateOpened) throws DaoException {
		Date date = parseDate(dateOpened);

		UserFood userFood = userFoodManager.searchUserFoodbyId(userFoodId);
		User user = (User) httpRequest.getAttribute("AUTHENTICATED_USER");

		if (!userFood.getUser().getId().equals(user.getId())) {
			log.info("User id " + user.getId() + " does not match user food id: " + userFoodId
					+ ", HTTP RESPONSE 403 FORBIDDEN");
			
			return Response.status(Status.FORBIDDEN).build();
		}
		else {
			log.info("Updating user food: userFoodId = " + userFoodId + ", dateOpened = " + dateOpened);
			userFoodManager.updateUserFood(userFood, date);
			log.info("Updated user food with id: " + userFoodId + ", HTTP RESPONSE 200 OK");
			return Response.ok().build();
		}

	}

	@DELETE
	@Path("/{userFoodId}")
	public Response deleteUserFood(@PathParam("userFoodId") Integer userFoodId) throws DaoException {

		UserFood userFood = userFoodManager.searchUserFoodbyId(userFoodId);
		User user = (User) httpRequest.getAttribute("AUTHENTICATED_USER");

		if (userFood == null) {
			return Response.ok().build();
		}

		if (!userFood.getUser().getId().equals(user.getId())) {
			return Response.status(Status.FORBIDDEN).build();
		}
		else {
			log.info("Deleting user food: userFoodId = " + userFoodId);
			userFoodManager.deleteUserFood(userFoodId);
			log.info("Deleted user food with id: " + userFoodId + ", HTTP RESPONSE 200 OK");
			return Response.ok().build();
		}

	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_XML)
	public Response getAllUserFood() throws DaoException {

		User authenticatedUser = (User) httpRequest.getAttribute("AUTHENTICATED_USER");
		log.info("Retrieving all user food: user = " + authenticatedUser.getUsername());
		List<UserFood> userFoods = userFoodManager.retrieveAllUserFood(authenticatedUser.getUsername());

		GenericEntity<List<UserFood>> entity = new GenericEntity<List<UserFood>>(userFoods) {
		};

		log.info("Returning all user food with username: " + authenticatedUser.getUsername() + ", HTTP RESPONSE 200 OK");
		return Response.ok().type(MediaType.APPLICATION_XML).entity(entity).build();
	}

	@DELETE
	@Path("/all")
	public Response deleteAllUserFood() throws DaoException {

		User authenticatedUser = (User) httpRequest.getAttribute("AUTHENTICATED_USER");
		log.info("Deleting all user food: user = " + authenticatedUser.getUsername());
		userFoodManager.deleteAllUserFood(authenticatedUser.getId());
		log.info("Deleted all user food with username: " + authenticatedUser.getUsername() + ", HTTP RESPONSE 200 OK");
		
		return Response.ok().build();
	}

	private Date parseDate(String dateStr) {
		Date date = null;
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		try {
			date = dateFormatter.parse(dateStr);
		} catch (ParseException e) {
			log.warn("Failed to parse date " + dateStr, e);
		}
		return date;

	}
}
