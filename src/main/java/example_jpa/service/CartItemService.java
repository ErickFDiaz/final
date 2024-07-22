package example_jpa.service;

import java.util.ArrayList;
import java.util.List;

import example_jpa.model.Cart;
import example_jpa.model.CartItem;
import example_jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class CartItemService {
	EntityManager entityManager = JPAUtil.getEntityManager();

	public Long add(CartItem cartItem) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(cartItem);
			entityManager.getTransaction().commit();
			return cartItem.getCartItemId();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	public CartItem update(CartItem cartItem) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(cartItem);
			entityManager.getTransaction().commit();
			return cartItem;
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CartItem> list() {
		try {
			List<CartItem> cartItems = new ArrayList<>();
			Query query = entityManager.createQuery("Select ci from CartItem ci");
			cartItems = query.getResultList();
			return cartItems;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public CartItem find(Long id) {
		try {
			CartItem cartItem = entityManager.find(CartItem.class, id);
			return cartItem;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void delete(Long id) {
		try {
			CartItem cartItem = entityManager.find(CartItem.class, id);
			entityManager.getTransaction().begin();
			entityManager.remove(cartItem);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public List<CartItem> findByCart(Cart cart) {
		try {
			Query query = entityManager.createQuery("SELECT ci FROM CartItem ci WHERE ci.cart = :cart");
			query.setParameter("cart", cart);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
