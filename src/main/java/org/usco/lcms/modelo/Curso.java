package org.usco.lcms.modelo;

public class Curso {

	long codigo;
	String grupo;
	long asignatura;
	String asignaturaNombre;
	long unidadPersona;
	long persona;
	String personaIdentificacion;
	String personaApellido;
	String personaNombre;
	String personaEmail;
	String realmId;
	int realmKey;
	String fecha;
	String url;

	public Curso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Curso(long codigo, String grupo, long asignatura, String asignaturaNombre, long unidadPersona, long persona,
			String personaIdentificacion, String personaApellido, String personaNombre, String url) {
		super();
		this.codigo = codigo;
		this.grupo = grupo;
		this.asignatura = asignatura;
		this.asignaturaNombre = asignaturaNombre;
		this.unidadPersona = unidadPersona;
		this.persona = persona;
		this.personaIdentificacion = personaIdentificacion;
		this.personaApellido = personaApellido;
		this.personaNombre = personaNombre;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public long getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(long asignatura) {
		this.asignatura = asignatura;
	}

	public String getAsignaturaNombre() {
		return asignaturaNombre;
	}

	public void setAsignaturaNombre(String asignaturaNombre) {
		this.asignaturaNombre = asignaturaNombre;
	}

	public long getUnidadPersona() {
		return unidadPersona;
	}

	public void setUnidadPersona(long unidadPersona) {
		this.unidadPersona = unidadPersona;
	}

	public long getPersona() {
		return persona;
	}

	public void setPersona(long persona) {
		this.persona = persona;
	}

	public String getPersonaIdentificacion() {
		return personaIdentificacion;
	}

	public void setPersonaIdentificacion(String personaIdentificacion) {
		this.personaIdentificacion = personaIdentificacion;
	}

	public String getPersonaApellido() {
		return personaApellido;
	}

	public void setPersonaApellido(String personaApellido) {
		this.personaApellido = personaApellido;
	}

	public String getPersonaNombre() {
		return personaNombre;
	}

	public void setPersonaNombre(String personaNombre) {
		this.personaNombre = personaNombre;
	}

	/**
	 * @return the personaEmail
	 */
	public String getPersonaEmail() {
		return personaEmail;
	}

	/**
	 * @param personaEmail
	 *            the personaEmail to set
	 */
	public void setPersonaEmail(String personaEmail) {
		this.personaEmail = personaEmail;
	}

	public String toString() {
		return "{'codigo':" + codigo + ",'grupo':'" + grupo + "','asignatura':" + asignatura + ",'asignaturaNombre':'"
				+ asignaturaNombre + "','unidadPersona':" + unidadPersona + ",'persona':" + persona
				+ ",'personaIdentificacion':'" + personaIdentificacion + "','personaApellido':'" + personaApellido
				+ "','personaNombre':" + personaNombre + ",'url':'" + url + "'}";

	}

	public String getRealmId() {
		return realmId;
	}

	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}

	/**
	 * @return the realKey
	 */
	public int getRealmKey() {
		return realmKey;
	}

	/**
	 * @param realKey
	 *            the realKey to set
	 */
	public void setRealmKey(int realmKey) {
		this.realmKey = realmKey;
	}

}
