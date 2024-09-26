package org.usco.lcms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.usco.lcms.dao.consumer.UsuarioConsumerDao;
import org.usco.lcms.dao.provider.UsuarioProviderDao;
import org.usco.lcms.modelo.TipoUsuario;
import org.usco.lcms.modelo.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	UsuarioProviderDao usuarioProviderDao;

	@Autowired
	UsuarioConsumerDao usuarioDao;

	public int[] migrarUsuario(TipoUsuario tipoUsuario, String periodo, boolean sincronizacion, int minutos) {

		List<Usuario> usuarios = usuarioProviderDao.listarUsuarios(tipoUsuario, periodo, sincronizacion, minutos);
		int[] cantidad = { 0, 0 };
		for (Usuario usuario : usuarios) {
			if (usuarioDao.buscarUsuario(usuario.getLogin()) == null) {
				usuarioDao.agregarUsuario(usuario);
				cantidad[0] += 1;
			} else {
				usuarioDao.actualizarUsuario(usuario);
				cantidad[1] += 1;
			}

		}

		return cantidad;
	}

	public int migrarTeachingAssistant(TipoUsuario tipoUsuario, String periodo, boolean sincronizacion, int minutos) {
		return 0;
	}
}