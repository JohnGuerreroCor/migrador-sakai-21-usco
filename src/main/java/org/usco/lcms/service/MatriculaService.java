package org.usco.lcms.service;

public interface MatriculaService {
	
	/**
	 * Realiza la migración de los cursos desde un repositorio a la base de datos de SAKAI
	 * @param periodo
	 * @param sincronizacion
	 * @param minutos
	 * @return cantidad de cursos migrados
	 */
	public int migrarCursosMatriculados(String periodo, boolean sincronizacion, int minutos);
}
