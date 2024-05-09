/**
 * 
 */
package edu.gitt.is.magiclibrary.test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.gitt.is.magiclibrary.model.*;
import edu.gitt.is.magiclibrary.model.entities.*;
import edu.gitt.is.magiclibrary.model.entities.Item.ItemState;



/**
 * 
 * <p>Test para probar la clase JpaItemDao, Dao para manejar los ejemplares de la biblioteca</p>
 * <p>Las clases Item y Book ya est�n implementadas y se manejan durante este test, al igual que JpaBookDao</p>
 * @author Isabel Rom�n
 *
 */
class JpaItemDaoTest {
	/**
	 * Para trazar el c�digo {@link java.util.logging}
	 */
	private static final Logger log = Logger.getLogger(JpaItemDaoTest.class.getName());
	static Item item1;
	static Item item2;
	static Item item3;
	static Book book1;
	static Book book2;
	static JpaItemDao undertest;
	static JpaBookDao bookdao;

	/**
	 * <p>Para las pruebas usar� dos libros, tres ejemplares y un manejador de Libros</p>
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeAll
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		log.info("Entro en setUpBefore");
		bookdao =new JpaBookDao();
		
		log.info("Creo y persisto dos libros");
		
		book1 = new Book("Ingenier�a del Software","Ian Sommerville", new Date(111,0,1), "miisbn", 500);
		bookdao.save(book1);
		log.info("Libro "+book1+" persistido");
		
		
		book2 = new Book("Ingenier�a del Software: un enfoque pr�ctico","Ian Roger S. Pressman", new Date(110,0,1), "otroisbn", 200);
		bookdao.save(book2);
		log.info("Libro "+book2+" persistido");
		
		log.info("Ejemplares con los t�tulos a�adidos");
	
		item1 = new Item(book1);
		item1.setInventoryNr("item1");				
		log.info("Item 1 creado "+item1);
		
		item2 = new Item(book2);
		item2.setInventoryNr("item2");
		log.info("Item 2 creado "+item2);
		
		item3=new Item(book2);
		item3.setInventoryNr("item3");
		log.info("Item 3 creado "+item3);
		
		undertest = new JpaItemDao();
		log.info("JpaItemDao bajo test creada");
	}
	/**
	 * <p>Cada test parte de la misma situaci�n en la BBDD, dos libros y ning�n ejemplar</p>
	 * <p>Como los test pueden a�adir ejemplares para comenzar otro hay que eliminar los ejemplares previamente</p>
	 * @throws Exception
	 */
    @AfterEach
    void setUpAfterEach() throws Exception{
    	log.info('\n'+"------Antes de ejecutar un nuevo test elimino todos los ejemplares de la BBDD-----"+'\n');
    	List<Item> items = undertest.findAll();
    	items.forEach(item->undertest.delete(item));
    	items = undertest.findAll();
    	assertTrue(items.size()==0,"Deber�a haber borrado todos los ejemplares, pero hay "+items.size());
    }

		/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaItemDao#findById(String)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	void testFindById() {
		log.info("Entro en el m�todo para probar el m�todo findById");
		log.info("Persisto el item 1 "+item1);
		undertest.save(item1);
		
		Optional<Item> item = undertest.findById(item1.getInventoryNr());
		
		assertTrue(item.isPresent(),"Error en buscar item por id");
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaItemDao#findAll()}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test

	void testFindAll() {
		log.info('\n'+"------Entro en el m�todo para probar el m�todo findAll-----"+'\n');
	
		undertest.save(item1);
		log.info("Persisto el primer ejemplar "+item1+'\n');
		List<Item> items = undertest.findAll();
		log.info("encontrados: "+items);
		assertTrue(items.size()==1,"He metido un ejemplar pero hay "+items.size());
		assertEquals(items.get(0).getItemInfo(),item1.getItemInfo(),"La informaci�n del t�tulo no coincide con la almacenada");
		
		
		undertest.save(item2);
		log.info("Persisto el segundo ejemplar "+item2+'\n');
		log.info("Busco todos los ejemplares");
		items = undertest.findAll();
		assertTrue(items.size()==2,"He metido dos ejemplares pero hay "+items.size());
		
		undertest.save(item3);
		log.info("Persisto el tercer ejemplar "+item3+'\n');
		log.info("Busco todos los ejemplares");
		items = undertest.findAll();
		assertTrue(items.size()==3,"He metido tres ejemplares pero hay "+items.size());
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaItemDao#save(gitt.is.magiclibrary.model.Item)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	void testSave() {
		log.info('\n'+"------Entro en el m�todo para probar el m�todo save-----"+'\n');
		undertest.save(item1);
		log.info("Persisto "+item1);
		Optional<Item> recuperado=undertest.findById(item1.getInventoryNr());		
		if(recuperado.isPresent()){		
			log.info("Recupero "+recuperado.get());
			
			assertEquals(recuperado.get().getStatus(),item1.getStatus(),"El estado del ejemplar recuperado no es el esperado");
			assertEquals(recuperado.get().getItemInfo(),item1.getItemInfo(), "La informaci�n del ejemplar recuperado no es la esperada");
			
		}else {
			fail("No estaba el ejemplar buscado");
		}
		//Verifico que si lo vuelvo a guardar no se duplica
		undertest.save(item1);
		
		List<Item> items= undertest.findAll();
		assertTrue(items.size()==1,"He metido un ejemplar pero hay "+items.size());
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaItemDao#update(gitt.is.magiclibrary.model.Item)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	void testUpdate() {
		log.info('\n'+"------Entro en el m�todo para probar el m�todo update-----"+'\n');
		undertest.save(item1);
		log.info("Persisto "+item1);
		item1.setStatus(ItemState.LOANED);
		undertest.update(item1);
		Optional<Item> recuperado=undertest.findById(item1.getInventoryNr());
		assertEquals(item1.getStatus(),recuperado.get().getStatus(),"El estado no ha cambiado en la BBDD");
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaItemDao#delete(gitt.is.magiclibrary.model.Item)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	void testDeleteItem() {
		log.info("Entro en el m�todo para probar el m�todo delete (Item)");
		
		undertest.save(item1);
		log.info("Persisto "+item1);
		Optional<Item> recuperado = undertest.findById(item1.getInventoryNr());	
		if(recuperado.isPresent()){
			log.info("El ejemplar est�, lo voy a eliminar");		
			
			undertest.delete(recuperado.get());
		}else {
			fail("No se ha recuperado bien el ejemplar");
		}
		
		log.info("Lo vuelvo a buscar y ahora no deber�a estar");
		
		recuperado = undertest.findById(item1.getInventoryNr());	
	
		assertFalse(recuperado.isPresent(),"El ejemplar lo hab�a borrado, no puedo recuperarlo");
	}

	/**
	 * Test method for {@link edu.gitt.is.magiclibrary.model.JpaItemDao#delete(String)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	void testDeleteString() {
		log.info('\n'+"Entro en el m�todo para probar el delete(String)"+'\n');
		
		undertest.save(item1);
		log.info("Persisto "+item1);
		Optional<Item> recuperado = undertest.findById(item1.getInventoryNr());	
		if(recuperado.isPresent()){
			log.info("El ejemplar est�, lo voy a eliminar");		
			
			undertest.delete(recuperado.get().getInventoryNr());
		}else {
			fail("No se ha recuperado bien el ejemplar");
		}
		log.info("Ahora lo vuelvo a buscar y no deber�a aparecer");
		recuperado = undertest.findById(item1.getInventoryNr());	
	
		assertFalse(recuperado.isPresent(),"El ejemplar lo hab�a borrado, no puedo recuperarlo");
	}

}
