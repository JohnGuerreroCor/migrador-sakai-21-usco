package org.usco.lcms.service;

import org.usco.lcms.modelo.TipoUsuario;

public interface UsuarioService{

	/**
	 * Realiza la migración de usuarios desde el repositorio externo a la base
	 * de datos de SAKAI
	 * 
	 * @param tipoUsuario
	 * @param periodo
	 * @param sincronizacion
	 * @return cantidad de usuarios migrados/sincronizados
	 */
	public int[] migrarUsuario(TipoUsuario tipoUsuario, String periodo, boolean sincronizacion, int minutos);
	
	public int migrarTeachingAssistant(TipoUsuario tipoUsuario, String periodo, boolean sincronizacion, int minutos);
}
