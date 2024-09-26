package org.usco.lcms.modelo;

public class Usuario {

	String login;
	String identificacion;
	String apellido;
	String nombre;
	String email;
	String fecha;
	String pwd;

	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Usuario(String login, String identificacion, String apellido, String nombre, String email, String pwd) {
		super();
		this.login = login;
		this.identificacion = identificacion;
		this.apellido = apellido;
		this.nombre = nombre;
		this.email = email;
		this.pwd = pwd;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String toString() {
		return "{'login':'" + login + "','identificacion':'" + identificacion + "','apellido':'" + apellido
				+ "','nombre':'" + nombre + "','email':'" + email + "','pwd':'" + pwd + "'}";
	}
}
