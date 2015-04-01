package org.alexandrialibrary.spring.controller;

import java.util.Arrays;
import java.util.List;

import org.alexandrialibrary.spring.bean.Ejemplar;
import org.alexandrialibrary.spring.bean.Libro;
import org.alexandrialibrary.spring.dao.LibroDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/libro")
public class LibroController extends AbstractController {
	
	@Autowired
	private LibroDAO libroDAO;

	/**
	 * Listado de libros
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		logger.info("Iniciamos libro/index [GET]");
		List<Libro> libros = libroDAO.getAllLibros();
		model.addAttribute("libros", libros);

		logger.info("Pasamos el listado de libros a la vista.");
		return "libro/index";
	}
	
	/**
	 * Ver un libro concreto [GET]
	 * 
	 * @param isbn
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ver/{isbn}", method = RequestMethod.GET)
	public String ver(@PathVariable Long isbn, Model model) {
		logger.info("Iniciamos libro/ver/{isbn} [GET]");
		
		Libro libro = libroDAO.getLibro(isbn);		
		model.addAttribute("libro", libro);
		
		return "libro/ver";
	}
	
	/**
	 * Recibe por AJAX la petición para mostrar las IDs de los Ejemplares disponibles de un Libro en concreto
	 * 
	 * @param isbn
	 * @return
	 */
	@RequestMapping(value = "/ejemplares-libres.json", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
	public @ResponseBody long[] ejemplares(@RequestParam(value = "isbn", required = true) Long isbn) {
		logger.info("Iniciamos libro/ejemplares.json?isbn={isbn} [GET]");
		
		Libro libro = libroDAO.getLibro(isbn);
		long[] ejemplares = new long[libro.getTotalEjemplaresLibres()];
		int index = 0;
		
		for (Ejemplar ejemplar : libro.getEjemplares()) {
			if (!ejemplar.isPrestado()) {
				ejemplares[index] = ejemplar.getId();
				index++;
			}
		}
		
		logger.info("IDs de Ejemplares disponibles: " + Arrays.toString(ejemplares));

		return ejemplares;
	}
	

}
