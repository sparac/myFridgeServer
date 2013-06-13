package hr.fer.ztel.myFridge.DAO;

import org.hibernate.Query;
import hr.fer.ztel.myFridge.data.Food;

public class FoodDAOImpl extends GenericDAOImpl<Food, Integer> implements IFoodDAO {

	@Override
	public Food findByBarcode(String barcode) {

		Food foodItem;
		String sql = "SELECT f FROM Food f WHERE f.barcode = :barcode";
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("barcode", barcode);

		foodItem = findOne(query);
		return foodItem;
	}

}
