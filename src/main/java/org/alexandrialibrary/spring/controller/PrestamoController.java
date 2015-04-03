package org.alexandrialibrary.spring.controller;

import java.util.List;
import java.util.Locale;

import org.alexandrialibrary.spring.bean.Ejemplar;
import org.alexandrialibrary.spring.bean.Libro;
import org.alexandrialibrary.spring.bean.Prestamo;
import org.alexandrialibrary.spring.bean.Usuario;
import org.alexandrialibrary.spring.dao.LibroDAO;
import org.alexandrialibrary.spring.dao.PrestamoDAO;
import org.alexandrialibrary.spring.dao.UsuarioDAO;
import org.alexandrialibrary.spring.editor.EjemplarEditor;
import org.alexandrialibrary.spring.editor.UsuarioEditor;
import org.alexandrialibrary.spring.util.PrestamoFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController extends AbstractController {
	
	@Autowired
	private PrestamoDAO prestamoDAO;

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Autowired
	private LibroDAO libroDAO;

	@Autowired
	private PrestamoFormValidator validator;
	
	@Autowired
	private UsuarioEditor usuarioEditor;

	@Autowired
	private EjemplarEditor ejemplarEditor;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Usuario.class, this.usuarioEditor);
        binder.registerCustomEditor(Ejemplar.class, this.ejemplarEditor);
    }

	/**
	 * Listado de préstamos
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		logger.info("Iniciamos prestamo/index [GET]");
		List<Prestamo> prestamos = prestamoDAO.getAllPrestamos();
		model.addAttribute("prestamos", prestamos);

		logger.info("Pasamos el listado de prestamos a la vista.");
		return "prestamo/index";
	}

	/**
	 * Formulario de nuevo préstamo [GET]
	 * 
	 * Se pueden añadir a la URL las IDs de Usuario y/o Libro. Ejemplo:
	 * 
	 * prestamo/nuevo?usuario=1
	 * prestamo/nuevo?usuario=1&libro=2
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/nuevo", method = RequestMethod.GET)
	public String nuevo(@RequestParam(value = "usuario", required = false) Long usuario_id, 
			@RequestParam(value = "libro", required = false) Long libro_isbn, Model model) {
		logger.info("Iniciamos prestamo/nuevo [GET]");
		
		Prestamo prestamo = new Prestamo();
		if (usuario_id != null) {
			// Si nos especifican un usuario
			Usuario usuario = usuarioDAO.getUsuario(usuario_id);
			prestamo.setUsuario(usuario);
		}
		
		if (libro_isbn != null) {
			// Si nos especifican un libro
			model.addAttribute("libro_isbn", libro_isbn);
		}
		
		model.addAttribute("prestamo", prestamo);
		
		List<Usuario> usuarios = usuarioDAO.getAllUsuarios();
		model.addAttribute("usuarios", usuarios);
		
		List<Libro> libros = libroDAO.getAllLibros();
		model.addAttribute("libros", libros);
		
		logger.info("Pasamos un prestamo nuevo a la vista, para vincularlo al form.");
		return "prestamo/nuevo";
	}

	/**
	 * Formulario de nuevo préstamo [POST]
	 * 
	 * @param prestamo
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/nuevo", method = RequestMethod.POST)
	public String nuevo(@ModelAttribute("prestamo") Prestamo prestamo,
			BindingResult result, Model model, Locale locale, final RedirectAttributes redirectAttributes) {
		logger.info("Iniciamos prestamo/nuevo [POST]");

		validator.validate(prestamo, result);
		if (result.hasErrors()) {
			logger.info("Formulario con errores, mostramos de nuevo el formulario.");
			
			List<Usuario> usuarios = usuarioDAO.getAllUsuarios();
			model.addAttribute("usuarios", usuarios);
			
			List<Libro> libros = libroDAO.getAllLibros();
			model.addAttribute("libros", libros);
			
			return "prestamo/nuevo";
		}

		logger.info("Formulario correcto! Guardamos el prestamo.");
		
		prestamoDAO.save(prestamo, locale);
		
		redirectAttributes.addFlashAttribute("success", 
				String.format("El préstamo del libro <strong>%s</strong> para el usuario <strong>%s %s</strong> se ha realizado con éxito.", 
				prestamo.getEjemplar().getLibro().getTitulo(), prestamo.getUsuario().getNombre(), 
				prestamo.getUsuario().getApellidos()));

		logger.info("Redireccionamos a prestamo/listado [GET]");
		return "redirect:/prestamo/";
	}
	
	/**
	 * Devolver un préstamo concreto, estableciendo la fecha de devolución como "ahora" [GET]
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/devolver/{id}", method = RequestMethod.GET)
	public String devolver(@PathVariable Long id, final RedirectAttributes redirectAttributes) {
		logger.info("Iniciamos prestamo/devolver/{id} [GET]");
		
		Prestamo prestamo = prestamoDAO.getPrestamo(id);
		
		if (!prestamo.isDevuelto()) {			
			// Si no está devuelto, lo actualizamos (se establecerá la fecha de devolución)
			logger.info("Actualizamos la información del préstamo estableciendo la devolución.");
			
			prestamoDAO.update(prestamo);
			
			redirectAttributes.addFlashAttribute("success", 
					String.format("El préstamo del libro <strong>%s</strong> para el usuario <strong>%s %s</strong> se ha devuelto con éxito.", 
					prestamo.getEjemplar().getLibro().getTitulo(), prestamo.getUsuario().getNombre(), 
					prestamo.getUsuario().getApellidos()));
		}

		logger.info("Redireccionamos a prestamo/listado [GET]");
		return "redirect:/prestamo/";
	}

}
