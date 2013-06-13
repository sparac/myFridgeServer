package hr.fer.ztel.myFridge.DAO;

import hr.fer.ztel.myFridge.data.Food;

public interface IFoodDAO extends IGenericDAO<Food, Integer> {
	public Food findByBarcode(String barcode);

}
