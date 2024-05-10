/**
 * Controlador principal para arrancar la aplicaci�n Magic Library, construye la vista principal y los controladores para cada tipo de entidad
 */
package edu.gitt.is.magiclibrary.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gitt.is.magiclibrary.view.MLView;

/**
 * <p>Clase principal de la aplicaci�n</p>
 * @author Isabel Rom�n
 *
 */
public class MagicLibrary {
	
	private static BookListener bookListener;
	private static ItemListener itemListener;
	/**
	 * <p>Con esta operaci�n se indica a la aplicaci�n donde est� el fichero de propiedades de trazado</p>
	 */
	
	
	static {
	      String path = MagicLibrary.class.getClassLoader()
	                                  .getResource("log.properties")
	                                  .getFile();
	      System.setProperty("java.util.logging.config.file", path);
	     
	}
	
	private static Logger log=Logger.getLogger(MagicLibrary.class.getName());
	
	/**
	 * @param args Actualmente el programa no analiza los par�metros de entrada al main
	 */
	public static void main(String[] args) {
		
		log.info("Arranco la aplicaci�n y creo el controlador para manejar entidades desde la interfaz");
		log.log(Level.INFO, "Arranco la aplicaci�n y creo el controlador para manejar entidades desde la interfaz");

		bookListener=new BookListener();
		log.finest("Acabo de crear un objeto de tipo "+BookListener.class.getName());
		MLView.getFrameManager().setEntityMenu("Book", bookListener);
		
		itemListener=new ItemListener();
		log.finest("Acabo de crear un objeto de tipo "+ItemListener.class.getName());
		MLView.getFrameManager().setEntityMenu("Item", itemListener);
		log.fine("Terminando el m�todo main");
	}

}
