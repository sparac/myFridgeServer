package hr.fer.ztel.myFridge.DAO;

import java.io.*;
import java.util.*;
import org.hibernate.Query;

public interface IGenericDAO<T, ID extends Serializable> {

	public void save(T entity);

	public void update(T entity);

	public void delete(T entity);

	public List<T> findMany(Query query);

	public T findOne(Query query);

	public List<T> findAll(Class<T> clazz);

	public T findByID(Class<T> clazz, Integer id);
}
