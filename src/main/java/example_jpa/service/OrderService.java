package example_jpa.service;

import java.util.List;

import example_jpa.model.Order;
import example_jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class OrderService {
	 EntityManager entityManager = JPAUtil.getEntityManager();

	    public Long add(Order order) {
	        try {
	            entityManager.getTransaction().begin();
	            entityManager.persist(order);
	            entityManager.getTransaction().commit();
	            return order.getOrderId();
	        } catch (Exception e) {
	            e.printStackTrace();
	            entityManager.getTransaction().rollback();
	            return null;
	        }
	    }

	    public Order update(Order order) {
	        try {
	            entityManager.getTransaction().begin();
	            entityManager.merge(order);
	            entityManager.getTransaction().commit();
	            return order;
	        } catch (Exception e) {
	            e.printStackTrace();
	            entityManager.getTransaction().rollback();
	            return null;
	        }
	    }

	    public Order find(Long id) {
	        try {
	            return entityManager.find(Order.class, id);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public List<Order> list() {
	        try {
	            return entityManager.createQuery("SELECT o FROM Order o").getResultList();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
