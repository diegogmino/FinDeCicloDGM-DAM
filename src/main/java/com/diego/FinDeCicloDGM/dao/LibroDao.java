package com.diego.FinDeCicloDGM.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.diego.FinDeCiclo.pojos.Libro;
import com.diego.FinDeCiclo.pojos.Usuario;


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

	public static void anhadirLibroUsuario(Libro libro, Usuario usuario) {
		
		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

		Session session = sf.openSession();

		session.getTransaction().begin();
			
			usuario.anhadirLibro(libro);
			session.saveOrUpdate(usuario);
		
		session.getTransaction().commit();

		session.close();
		sf.close();

	}
	
	public static Libro buscarLibroISBN(String isbn) {
		
		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

		Session session = sf.openSession();

		session.getTransaction().begin();

			Query query = session.createQuery("SELECT l FROM Libro l WHERE isbn = :cod ");
			List<Libro> libros = query.setParameter("cod", Long.parseLong(isbn)).getResultList();
		
		session.getTransaction().commit();

		session.close();
		sf.close();
		
		if(!libros.isEmpty()) {
			return libros.get(0);
		} else {
			Libro libro = new Libro();
			return libro;
		}
		
	}
	
	public static List<Libro> buscarLibroTitulo(String titulo) {
		
		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

		Session session = sf.openSession();

		session.getTransaction().begin();

			Query query = session.createQuery("SELECT l FROM Libro l WHERE titulo LIKE :tit ");
			List<Libro> libros = query.setParameter("tit", titulo).getResultList();
		
		session.getTransaction().commit();

		session.close();
		sf.close();
		
		return libros;
		
	}
	
	public static List<Libro> buscarLibroAutor(String autor) {
		
		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

		Session session = sf.openSession();

		session.getTransaction().begin();

			Query query = session.createQuery("SELECT l FROM Libro l WHERE autor LIKE :aut ");
			List<Libro> libros = query.setParameter("aut", autor).getResultList();
		
		session.getTransaction().commit();

		session.close();
		sf.close();
		
		return libros;
		
	}
	
	public static List<Libro> buscarLibroGenero(String genero) {

		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

		Session session = sf.openSession();

		session.getTransaction().begin();

			Query query = session.createQuery("SELECT l FROM Libro l WHERE genero LIKE :gen ");
			List<Libro> libros = query.setParameter("gen", genero).getResultList();
		
		session.getTransaction().commit();

		session.close();
		sf.close();
		
		return libros;
		
	}
	
	public static List<Libro> buscarTodos() {

		StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

		Session session = sf.openSession();

		session.getTransaction().begin();

			Query query = session.createQuery("SELECT l FROM Libro l");
			List<Libro> libros = query.getResultList();
		
		session.getTransaction().commit();

		session.close();
		sf.close();
		
		return libros;
		
	}

}