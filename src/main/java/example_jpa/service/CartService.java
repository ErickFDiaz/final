package example_jpa.service;

import java.util.ArrayList;
import java.util.List;

import example_jpa.model.Cart;
import example_jpa.model.User;
import example_jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class CartService {
	EntityManager entityManager = JPAUtil.getEntityManager();

	public Long add(Cart cart) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(cart);
			entityManager.getTransaction().commit();
			return cart.getCartId();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	public Cart update(Cart cart) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(cart);
			entityManager.getTransaction().commit();
			return cart;
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cart> list() {
		try {
			List<Cart> carts = new ArrayList<>();
			Query query = entityManager.createQuery("Select c from Cart c");
			carts = query.getResultList();
			return carts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Cart find(Long id) {
		try {
			Cart cart = entityManager.find(Cart.class, id);
			return cart;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void delete(Long id) {
		try {
			Cart cart = entityManager.find(Cart.class, id);
			entityManager.getTransaction().begin();
			entityManager.remove(cart);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public Cart findByUser(User user) {
		try {
			Query query = entityManager.createQuery("SELECT c FROM Cart c WHERE c.user = :user");
			query.setParameter("user", user);
			return (Cart) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
