package hr.fer.ztel.myFridge.servlets;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.DAO.HibernateUtil;
import hr.fer.ztel.myFridge.business.FoodManager;
import hr.fer.ztel.myFridge.data.Food;
import hr.fer.ztel.myFridge.data.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

@Path("/food")
public class FoodResource {

	@Context
	private HttpServletRequest httpRequest;
	private final FoodManager foodManager = new FoodManager();
	private static final Logger log = Logger.getLogger(FoodResource.class.getName());

	@GET
	@Path("/{barcode}")
	@Produces(MediaType.APPLICATION_XML)
	public Response searchItem(@PathParam("barcode") String barcode) throws DaoException {

		if(barcode == null || barcode.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		log.info("Searching food item with barcode: " + barcode + " for user "
				+ ((User) httpRequest.getAttribute("AUTHENTICATED_USER")).getUsername());
		Food foodItem = foodManager.searchFoodByBarcode(barcode);
		HibernateUtil.closeSession();
		
		if(foodItem == null) {
			log.info("Food with barcode: " + barcode + " not found, HTTP RESPONSE 400 BAD REQUEST");
			return Response.status(Status.BAD_REQUEST).build();
		}
		else {
			log.info("Returning food with barcode: " + barcode + ", HTTP RESPONSE 200 OK");
			return Response.ok().entity(foodItem).build();
		}
	}
}
