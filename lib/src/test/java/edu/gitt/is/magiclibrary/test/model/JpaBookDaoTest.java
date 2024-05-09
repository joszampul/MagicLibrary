/**
 * 
 */
package edu.gitt.is.magiclibrary.test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.gitt.is.magiclibrary.model.JpaBookDao;
import edu.gitt.is.magiclibrary.model.entities.Book;
import edu.gitt.is.magiclibrary.model.entities.Item;

//Commit para verificar push de joszampul

/**
 * 
 * <p>Test para probar JpaBookDao, clase para manejar los libros de la biblioteca</p>
 * <p>La clase Book ya est� disponible y se usa en este test</p>
 * @author Isabel Rom�n
 */
class JpaBookDaoTest {
	/**
	 * Para trazar el c�digo {@link java.util.logging}
	 */
	private static final Logger log = Logger.getLogger(JpaBookDaoTest.class.getName());
	
	static Book book1;
	static Book book2;
	static Book book11;
	static JpaBookDao undertest;
	

	/**
	 * {@link org.junit.jupiter.api.BeforeAll}
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		  log.info("--------------------------Entro en setUpBeforeClass---------------");
		  undertest = new JpaBookDao();
		  log.info("JpaBookDao bajo test creado");
		  book1 = new Book("Ingenier�a del Software","Ian Sommerville", new Date(111,0,1), "miisbn", 500);
		  log.info("Libro 1 creado: "+book1);
		  book2 = new Book("Ingenier�a del Software: un enfoque pr�ctico","Ian Roger S. Pressman", new Date(110,0,1), "otroisbn", 200);
		  log.info("Libro 2 creado: "+book2);
		  /**
		   * este libro es el mismo que el primero, para algunas pruebas de replicados
		   */
		  book11 = new Book("Ingenier�a del Software","Ian Sommerville", new Date(111,0,1), "miisbn", 500);
		  log.info("Libro 1 replicado: "+book11);
		  
		 }

	/**
	 * {@link org.junit.jupiter.api.AfterAll}
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * {@link org.junit.jupiter.api.BeforeEach}
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * {@link org.junit.jupiter.api.AfterEach}
	 * <p>Cada test parte de la misma situaci�n en la BBDD, dos libros y ning�n ejemplar</p>
	 * <p>Como los test pueden a�adir ejemplares para comenzar otro hay que eliminar los ejemplares previamente</p>
	 * @throws Exception
	 */
    @AfterEach
    void setUpAfterEach() throws Exception{
    	log.info('\n'+"------Antes de ejecutar un nuevo test elimino todos los libros de la BBDD-----"+'\n');
    	List<Book> books = undertest.findAll();
    	books.forEach(book->undertest.delete(book));
    	books = undertest.findAll();
    	assertTrue(books.size()==0,"Deber�a haber borrado todos los libros, pero hay "+books.size());
    }

	/**
	 * <p>Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#findById(String)}.</p>
	 * {@link org.junit.jupiter.api.Test}
	 */
	@Test
	@DisplayName("Verifica el m�todo para buscar por identificador")
	final void testFindById() {
		log.info("Entro en el m�todo para probar el m�todo findById");
		log.info("Persisto el libro 1 "+book1);
		undertest.save(book1);
		
		Optional<Book> book = undertest.findById(book1.getId());
		
		assertTrue(book.isPresent(),"Error en buscar libro por id");
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#findAll()}.
	 * {@link org.junit.jupiter.api.Test}
	 */
	@Test
	@DisplayName("Verifica el m�todo para buscar todos")
	final void testFindAll() {
		log.info("Entro en el m�todo para probar el m�todo findAll");
		log.info("Persisto el libro 1 "+book1);
		undertest.save(book1);
		
		log.info("Persisto el libro 2 "+book2);
		undertest.save(book2);
		
		List<Book> books = undertest.findAll();
		log.info("Busco los libros que hay y encuentro "+books.size());
		assertTrue(books.size()==2,"He metido dos libros pero hay "+books.size());

	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#save(gitt.is.magiclibrary.model.Book)}.
	 * Verifica que si se guarda un libro luego se puede recuperar
	 * {@link org.junit.jupiter.api.Test}
	 */
	@Test
	@DisplayName("Verifica la introducci�n de un libro por primera vez")
	final void testSave() {
		log.info("Entro en el m�todo para probar el m�todo save");
		undertest.save(book1);
		log.info("Persisto "+book1);
		
	
		Optional<Book> recuperado=undertest.findById(book1.getId());
		
		if(recuperado.isPresent()){
			
			log.info("Recupero "+recuperado);
		
			assertEquals(recuperado.get().getAuthor(),"Ian Sommerville");
			assertEquals(recuperado.get().getName(),"Ingenier�a del Software");
			assertEquals(recuperado.get().getPages(),500);
			assertEquals(recuperado.get().getPublishedAt(),new Date(111,0,1));
			assertEquals(recuperado.get().getIsbn(),"miisbn");	
		}else {
			fail("No estaba el libro");
		}
		
	}
	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#save(gitt.is.magiclibrary.model.Book)}.
	 * Verifica que si se intenta guarda un libro dos veces no se duplica (no puede haber dos libros con los mismos datos)
	 * {@link org.junit.jupiter.api.Test}
	 */
	@Test
	@DisplayName("Verifica que la aplicaci�n no permite almacenar dos libros con los mismos datos")
	final void testResave() {
		log.info("Entro en el m�todo para probar que el m�todo save asegura que no se duplica");
		undertest.save(book1);
		log.info("Persisto "+book1);
		undertest.save(book11);
		log.info("Persisto la r�plica "+book11);
	
		
		List<Book> books = undertest.findAll();
		assertTrue(books.size()==1,"He metido el mismo libro dos veces "+books.size());
		
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#update(gitt.is.magiclibrary.model.Book)}.
	 * {@link org.junit.jupiter.api.Test}
	 */
	@Test
	@DisplayName("Verifica el m�todo para actualizar los datos de un libro")
	final void testUpdate() {
		log.info("Entro en el m�todo para probar el m�todo update()");
		log.info("Persisto el libro 1 "+book1);
		undertest.save(book1);
		
		book1.setName("joszampul");
		
		undertest.update(book1);
		
		Optional<Book> recuperado = undertest.findById(book1.getId());	
		
		assertEquals(recuperado.get().getName(), book1.getName(),"Error al actualizar campos de un libro");
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#delete(gitt.is.magiclibrary.model.Book)}.
	 * {@link org.junit.jupiter.api.Test}
	 */
	@Test
	@DisplayName("Verifica el m�todo para eliminar un libro")
	final void testDeleteBook() {
		log.info("Entro en el m�todo para probar el m�todo delete");
		
		undertest.save(book1);
		log.info("Persisto "+book1);
		Optional<Book> recuperado = undertest.findById(book1.getId());	
		if(recuperado.isPresent()){
			log.info("El libro est�, lo voy a eliminar");		
			
			undertest.delete(recuperado.get());
		}else {
			fail("No se ha recuperado bien el libro");
		}
		recuperado = undertest.findById(book1.getId());	
		assertFalse(recuperado.isPresent(),"El libro lo hab�a borrado, no puedo recuperarlo");
			
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#delete(java.lang.String)}.
	 * {@link org.junit.jupiter.api.Test}
	 */
	@Test
	final void testDeleteLong() {
		log.info("Entro en el m�todo para probar el m�todo delete(String id)");
		log.info("Persisto el libro 1 "+book1);
		undertest.save(book1);
		
		undertest.delete(book1.getId());
		
		Optional<Book> recuperado = undertest.findById(book1.getId());	
		
		assertFalse(recuperado.isPresent(),"Error en borrar libro dado un id");
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaBookDao#findBookByAuthor(java.lang.String)}.
	 * {@link org.junit.jupiter.api.Test}
	 */
	
	@Test
	@DisplayName("Verifica el m�todo para buscar por autor")
	final void testFindBookByAuthor() {
		log.info("Entro en el m�todo para probar el m�todo findBookByAuthor");
		
		undertest.save(book1);
		log.info("Persisto "+book1);
		Optional<Book> recuperado = undertest.findBookByAuthor(book1.getAuthor());	
		assertTrue(recuperado.isPresent());
		assertEquals(recuperado.get().getAuthor(),book1.getAuthor(),"No tienen el mismo autor");
	}
	

}
