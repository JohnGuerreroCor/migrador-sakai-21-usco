package org.usco.lcms.modelo;

public class Matricula {

	long curso;
	String estudiante;
	String identificacion;
	/**
	 * Tipo de matricula 1->adición 2-> normal 4->cancelación
	 */
	short tipo;

	public Matricula() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param curso
	 * @param estudiante
	 * @param identificacion
	 * @param tipo
	 */
	public Matricula(long curso, String estudiante, String identificacion, short tipo) {
		super();
		this.curso = curso;
		this.estudiante = estudiante;
		this.identificacion = identificacion;
		this.tipo = tipo;
	}

	public long getCurso() {
		return curso;
	}

	public void setCurso(long curso) {
		this.curso = curso;
	}

	public String getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(String estudiante) {
		this.estudiante = estudiante;
	}

	/**
	 * @return the identificacion
	 */
	public String getIdentificacion() {
		return identificacion;
	}

	/**
	 * @param identificacion
	 *            the identificacion to set
	 */
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	/**
	 * @return the tipo
	 */
	public short getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(short tipo) {
		this.tipo = tipo;
	}

	public String toString() {
		return "{'curso':" + curso + ",'estudiante':'" + estudiante + "'}";
	}

}
