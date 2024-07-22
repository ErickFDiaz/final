package example_jpa.service;

import java.util.ArrayList;
import java.util.List;

import example_jpa.model.Product;
import example_jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class ProductService {

	EntityManager entityManager = JPAUtil.getEntityManager();

	public Long add(Product product) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(product);
			entityManager.getTransaction().commit();
			return product.getProductId();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	public Product update(Product product) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(product);
			entityManager.getTransaction().commit();
			return product;
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Product> list() {
		try {
			List<Product> products = new ArrayList<>();
			Query query = entityManager.createQuery("Select p from Product p");
			products = query.getResultList();
			return products;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Product find(Long id) {
		try {
			Product product = entityManager.find(Product.class, id);
			return product;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void delete(Long id) {
		try {
			Product product = entityManager.find(Product.class, id);
			entityManager.getTransaction().begin();
			entityManager.remove(product);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

}
