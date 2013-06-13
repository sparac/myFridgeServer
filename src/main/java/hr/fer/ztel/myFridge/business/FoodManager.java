package hr.fer.ztel.myFridge.business;

import java.io.IOException;

import hr.fer.ztel.myFridge.DAO.DaoException;
import hr.fer.ztel.myFridge.DAO.FoodDAOImpl;
import hr.fer.ztel.myFridge.DAO.HibernateUtil;
import hr.fer.ztel.myFridge.DAO.IFoodDAO;
import hr.fer.ztel.myFridge.data.Food;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

public class FoodManager {

	private static final Logger log = Logger.getLogger(FoodManager.class.getName());
	private static final String MAIN_DATA_SEARCH_URI = "http://online.konzum.hr/search?search_term=";

	public Food searchFoodByBarcode(String barcode) throws DaoException {

		log.info("Searching for item with barcode " + barcode);

		Food food;
		IFoodDAO foodDAO = new FoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			food = foodDAO.findByBarcode(barcode);

			if (food == null) {

				food = findFoodOnline(barcode);

				if (food != null) {
					log.info("Item with barcode " + barcode + " found online: " + food.getName());
					log.debug(food.toString());
					foodDAO.save(food);
				} else {
					log.info("Item with barcode " + barcode + " not found online");
				}
			} else {
				log.info("Item with barcode " + barcode + " found in DB: " + food.getName());
			}

			HibernateUtil.commitTransaction();
		} catch (IOException | HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return food;
	}

	public Food searchFoodbyId(Integer fId) throws DaoException {
		Food food;
		IFoodDAO foodDAO = new FoodDAOImpl();
		try {
			HibernateUtil.beginTransaction();
			food = foodDAO.findByID(Food.class, fId);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollbackTransaction();
			throw new DaoException(e);
		}
		return food;

	}

	private Food findFoodOnline(String barcode) throws IOException {
		log.info("Item with barcode " + barcode + " not found in DB");
		
		Food food = null;
		Food detailsFood = null;

		String searchMainDataResult = BarcodeSearchKonzum.getSourcePage(MAIN_DATA_SEARCH_URI + barcode);
		
		//various checks on search and parse results
		if(searchMainDataResult == null || searchMainDataResult.isEmpty()) {
			return null;
		}
		food = BarcodeSearchKonzum.foodMainDataExtraction(searchMainDataResult, barcode);

		String detailsUri = BarcodeSearchKonzum.detailsUriExtraction(searchMainDataResult);
		
		if(detailsUri == null || detailsUri.isEmpty()) {
			return null;
		}
		String searchDetailsResult = BarcodeSearchKonzum.getSourcePage(detailsUri);
		
		if(searchDetailsResult == null || searchDetailsResult.isEmpty()) {
			return null;
		}
		detailsFood = BarcodeSearchKonzum.foodDetailExtraction(searchDetailsResult);

		if (food != null && detailsFood!= null) {
			food.setDescription(detailsFood.getDescription());
			food.setImageLarge(detailsFood.getImageLarge());
		}

		return food;
	}

}
