package org.usco.lcms.dao.provider;

import java.util.List;

import org.usco.lcms.modelo.TipoUsuario;
import org.usco.lcms.modelo.Usuario;

public interface UsuarioProviderDao {

	List<Usuario> listarUsuarios(TipoUsuario tipoUsuario, String calendario, boolean sincronizacion, int minutos);
}
