package example_jpa.util;

import jakarta.persistence.EntityManager;

public class ConecctionDemo {

	public static void main(String[] args) {
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		
		if(entityManager != null)
			System.out.println("Se conecto");
		else
			System.out.println("error");
	}
}
