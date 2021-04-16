package com.diego.FinDeCicloDGM.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.diego.FinDeCiclo.pojos.Libro;


public class LibroDao {
	
public static boolean insertarLibro(Libro libro) {
		
		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

		Session session = sf.openSession();

		session.getTransaction().begin();
		
			session.save(libro);
		
		session.getTransaction().commit();

		session.close();
		sf.close();
		
		return true;
		
		
	}

}
