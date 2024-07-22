package example_jpa.service;

import java.util.ArrayList;
import java.util.List;

import example_jpa.model.User;
import example_jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class UserService {
	
	

	EntityManager entityManager = JPAUtil.getEntityManager();

	public Long add(User user) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(user);
			entityManager.getTransaction().commit();
			return user.getUserId();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}
	
	public User authenticate(String username, String password) {
        // Aquí puedes implementar la lógica para autenticar al usuario
        // Por ejemplo, buscar en la base de datos por nombre de usuario y contraseña
        // Si el usuario es válido, devolver el objeto User, de lo contrario, devolver null
        // Esto es solo un ejemplo, asegúrate de adaptar esto a tu lógica de negocio

        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
	
	public User findByUsername(String username) {
		try {
			TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User update(User user) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(user);
			entityManager.getTransaction().commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> list() {
		try {
			List<User> users = new ArrayList<>();
			Query query = entityManager.createQuery("Select u from User u");
			users = query.getResultList();
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User find(Long id) {
		try {
			User user = entityManager.find(User.class, id);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void delete(Long id) {
		try {
			User user = entityManager.find(User.class, id);
			entityManager.getTransaction().begin();
			entityManager.remove(user);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}
}
