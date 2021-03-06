package org.alexandrialibrary.spring.service.impl;

import java.util.List;

import org.alexandrialibrary.spring.dao.EjemplarDAO;
import org.alexandrialibrary.spring.model.Ejemplar;
import org.alexandrialibrary.spring.service.EjemplarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de Ejemplares.
 * 
 * Se anota con @Service y @Transactional para que la sesión que se comunica 
 * con Hibernate esté iniciada en este punto.
 * 
 * Por norma general la clase del Servicio llama a la clase DAO correspondiente.
 */
@Service
@Transactional
public class EjemplarServiceImpl implements EjemplarService {

	@Autowired
    private EjemplarDAO ejemplarDAO;
	
	@Override
	public List<Ejemplar> getAllEjemplares() {
		return ejemplarDAO.getAllEjemplares();
	}

	@Override
	public Ejemplar getEjemplar(long id) {
		return ejemplarDAO.getEjemplar(id);
	}

	@Override
	public long save(Ejemplar ejemplar) {
		return ejemplarDAO.save(ejemplar);
	}

	@Override
	public void update(Ejemplar ejemplar) {
		ejemplarDAO.update(ejemplar);
	}

	@Override
	public void delete(long id) {
		ejemplarDAO.delete(id);
	}

	@Override
	public List<Ejemplar> getEjemplaresLibresForIsbn(long libro_isbn) {
		return ejemplarDAO.getEjemplaresLibresForIsbn(libro_isbn);
	}

}
