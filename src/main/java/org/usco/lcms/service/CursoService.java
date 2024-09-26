package org.usco.lcms.service;

import java.util.List;

import org.usco.lcms.modelo.Curso;

public interface CursoService {

	/**
	 * Trae los cursos desde la fuente de datos externa y los inserta o
	 * actualiza en la base de datos de SAKAI
	 * 
	 * @param calendario
	 * @param sincronizacion
	 * @return array[2] resultado[0] = cantidad de cursos insertados -
	 *         resultado[1] cantidad de cursos modificados
	 */
	public int[] migrarCursos(String calendario, boolean sincronizacion, int minutos);
	public int[] agregarTeachingAssitant(String calendario);

}
