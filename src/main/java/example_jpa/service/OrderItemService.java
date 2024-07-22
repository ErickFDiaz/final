package example_jpa.service;

import java.util.List;

import example_jpa.model.OrderItem;
import example_jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class OrderItemService {
	EntityManager entityManager = JPAUtil.getEntityManager();

	public Long add(OrderItem orderItem) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(orderItem);
			entityManager.getTransaction().commit();
			return orderItem.getOrderItemId();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	public OrderItem update(OrderItem orderItem) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(orderItem);
			entityManager.getTransaction().commit();
			return orderItem;
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	public OrderItem find(Long id) {
		try {
			return entityManager.find(OrderItem.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<OrderItem> list() {
		try {
			return entityManager.createQuery("SELECT oi FROM OrderItem oi").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<OrderItem> listByOrder(Long orderId) {
	    try {
	        List<OrderItem> list = entityManager.createQuery("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId", OrderItem.class)
	                                            .setParameter("orderId", orderId)
	                                            .getResultList();
	        return list;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
