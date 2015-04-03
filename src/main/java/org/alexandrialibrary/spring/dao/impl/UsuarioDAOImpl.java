package org.alexandrialibrary.spring.dao.impl;

import java.util.List;

import org.alexandrialibrary.spring.bean.Usuario;
import org.alexandrialibrary.spring.dao.AbstractDAO;
import org.alexandrialibrary.spring.dao.UsuarioDAO;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UsuarioDAOImpl extends AbstractDAO implements UsuarioDAO {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> getAllUsuarios() {
		Criteria criteria = this.getCurrentSession().createCriteria(Usuario.class);
		List<Usuario> usuarios = criteria.list();
		for (Usuario usuario : usuarios) {
			Hibernate.initialize(usuario.getPrestamos());
		}
		return usuarios; 
	}
	 
	@Override
	public Usuario getUsuario(long id) {
		Usuario usuario = (Usuario) this.getCurrentSession().get(Usuario.class, id);
		Hibernate.initialize(usuario.getPrestamos());
		
		return usuario;
	}

	@Override
	public long save(Usuario usuario) {
		return (Long) this.getCurrentSession().save(usuario); 
	}

	@Override
	public void update(Usuario usuario) {
		this.getCurrentSession().merge(usuario);
	}

	@Override
	public void delete(long id) {
		Usuario usuario = getUsuario(id);
		this.getCurrentSession().delete(usuario);
	}

}
