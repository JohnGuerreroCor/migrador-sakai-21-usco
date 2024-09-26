package org.usco.lcms.dao.consumer;

import org.usco.lcms.modelo.Usuario;

public interface UsuarioConsumerDao {
	
	public void agregarUsuario(Usuario usuario);

	public void actualizarUsuario(Usuario usuario);

	public Usuario buscarUsuario(String usuario);
}
