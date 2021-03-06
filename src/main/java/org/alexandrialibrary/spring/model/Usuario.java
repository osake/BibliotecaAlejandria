package org.alexandrialibrary.spring.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Clase Usuario del modelo. Corresponde a la tabla 'usuario'.
 *
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Atributos de Usuario, que a su vez serán campos de la tabla 'usuario'.
	 * 
	 * Las anotaciones indican a JPA (Java Persistence API) qué papel representa cada uno.
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "dni", unique = true) // Indica que es una clave única
	private String dni;

	@Column(name = "email", unique = true) // Indica que es una clave única
	private String email;

	@Column(name = "direccion")
	private String direccion;

	@OneToMany(targetEntity = Prestamo.class, mappedBy = "usuario", cascade={CascadeType.ALL}, orphanRemoval = true)
	private List<Prestamo> prestamos;


	/* -------- */
	public Usuario() {
		super();
	}
	
	public Usuario(String nombre) {
		this.nombre = nombre;
	}

	public Usuario(String nombre, String apellidos, String dni, String email, String direccion) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.email = email;
		this.direccion = direccion;		
	}

	public String toString() {
		return this.nombre + " " + this.apellidos;
	}
	/* -------- */
	
	/*
	 * Getters/Setters
	 */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public List<Prestamo> getPrestamos() {
		return prestamos;
	}

	public void setPrestamos(List<Prestamo> prestamos) {
		this.prestamos = prestamos;
	}
	
	/**
	 * Se recorre todos sus préstamos y devolverá true si alguno está pendiente.
	 */
	public boolean getHasPrestamosPendientes() {
		for (Prestamo prestamo : prestamos) {
			if (!prestamo.isDevuelto()) {
				return true;
			}
		}
		return false;
	}

}
