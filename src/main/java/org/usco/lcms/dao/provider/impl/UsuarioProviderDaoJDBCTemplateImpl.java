package org.usco.lcms.dao.provider.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import java.security.SecureRandom;
import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.usco.lcms.dao.provider.UsuarioProviderDao;
import org.usco.lcms.modelo.TipoUsuario;
import org.usco.lcms.modelo.Usuario;

@Repository
public class UsuarioProviderDaoJDBCTemplateImpl implements UsuarioProviderDao {

	@Autowired
	@Qualifier("dataSourceMssql")
	DataSource dataSource;

	private String[] camposPersona = { "per_identificacion", "per_apellido", "per_nombre", "per_email_interno" };
	private String[] camposTercero = { "ter_identificacion", "ter_apellido", "ter_nombre", "ter_email" };

	public List<Usuario> listarUsuarios(final TipoUsuario tipoUsuario, String calendario, boolean sincronizacion,
			int minutos) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] parametros = null;
		String sql = "";

		Calendar fecha = Calendar.getInstance();
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minute = fecha.get(Calendar.MINUTE);

		if ((dia == 24) && (hora == 14) && (minute > 40 && minute < 55)) {
			System.out.println("***************************************************************");
			System.out.println("****************************INICIO INHABILITAR USUARIOS***********************************");
			System.out.println("***************************************************************");
			String sqlInactivarUsuarios = "Exec InhabilitarUsuarioVirtual";
			int resultadoInactivarUsuarios = jdbcTemplate.update(sqlInactivarUsuarios);
			System.out.println("Se inactivaron usuarios: " + resultadoInactivarUsuarios);
		}

		switch (tipoUsuario) {
		case ESTUDIANTE:
			sql = "select * from estudiante_virtual where cal_codigo in (select cal_codigo from calendario where cal_nombre like ?) ";

			if (sincronizacion) {
				sql += " AND ((us_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
						+ " OR (est_fecha_ingreso BETWEEN dateadd(minute,-?,getdate()) AND getdate()))";

				// sql += " OR us in ('u20181166332', 'u20181170510', 'u20181170514',
				// 'u20181170526', 'u20181170718', 'u20181170739', 'u20181170741',
				// 'u20181170744', 'u20181170745', 'u20181170746', 'u20181170754',
				// 'u20181170764', 'u20181170771', 'u20181170788', 'u20181170796',
				// 'u20181170807', 'u20181170813', 'u20181170824',
				// 'u20181170829','u20181170840', 'u20181170841', 'u20181170842',
				// 'u20181170844', 'u20181170845','u20181170850', 'u20181170856',
				// 'u20181170857')";
				/*
				 * sql +=
				 * " AND (per_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
				 * +
				 * "OR (us_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
				 * +
				 * "OR (est_fecha_ingreso BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
				 * ;
				 */
				parametros = new Object[] { calendario + "%", minutos, minutos };
			} else {
				parametros = new Object[] { calendario + "%" };
			}

			sql += " order by per_apellido, per_nombre";
			break;
		case DOCENTE:
			sql = "SELECT * from docente_virtual where cal_codigo in (select cal_codigo from calendario  where cal_nombre like ?) ";
			if (sincronizacion) {
				sql += " AND ((per_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
						+ "OR (us_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
						+ "OR (cur_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate())) ";

				parametros = new Object[] { calendario + "%", minutos, minutos, minutos };
			} else {
				parametros = new Object[] { calendario + "%" };
			}
			sql += " order by per_apellido, per_nombre";
			break;
		}
		System.out.println("*******************************+");
		System.out.println("*******************************+");
		System.out.println("************query DE DOCENTES*******************+");
		System.out.println("*******************************+");
		System.out.println(sql);
		System.out.println("*****MINUTOS**************************+" + minutos);
		System.out.println("*******************************+");
		System.out.println("*******************************+");
		List<Usuario> listaUsuarios = jdbcTemplate.query(sql, parametros, new RowMapper<Usuario>() {

			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				Usuario usuario = new Usuario();
				// tipo_usuario =>1 administrativo
				// tipo_usuario =>2 Formal estudiante
				// tipo_usuario =>3 docente
				// tipo_usuario =>5 NO Formal estudiante virtual
				// Los formales el usuario es el codigo del estudiante y los no
				// formal son el numero de identificacion

				if (rs.getInt("tipo_usuario") == 1 || rs.getInt("tipo_usuario") == 2) {
					usuario.setLogin(rs.getString("us"));
				}

				if (rs.getInt("tipo_usuario") == 5 || rs.getInt("tipo_usuario") == 3) {
					if (rs.getString("per_identificacion") == null) {
						usuario.setLogin(rs.getString("ter_identificacion"));
					} else {
						usuario.setLogin(rs.getString("per_identificacion"));
					}
				}

				if (tipoUsuario.equals(TipoUsuario.DOCENTE)) {
					iniciarUsuario(rs, usuario, camposPersona);
				} else {
					if (rs.getString("per_identificacion") == null) {
						iniciarUsuario(rs, usuario, camposTercero);
					} else {
						iniciarUsuario(rs, usuario, camposPersona);
					}
				}
				switch (tipoUsuario) {
				case ESTUDIANTE:
					usuario.setFecha(rs.getString("us_fecha_actualizacion"));
					break;
				case DOCENTE:
					usuario.setFecha(rs.getString("cur_fecha_actualizacion"));
					break;
				}

				return usuario;
			}

		});
		return listaUsuarios;
	}
	
	// MÉTODO DESENCRIPTACIÓN MD5 Y SHA1 USCO
	public static String encode(CharSequence password) {
		MessageDigest mdSha = null;
		MessageDigest mdMd5 = null;
		String result = "";
		try {
			mdSha = MessageDigest.getInstance("SHA1");
			mdMd5 = MessageDigest.getInstance("md5");
			byte[] byteshashed = mdMd5.digest(mdSha.digest(password.toString().getBytes()));

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteshashed.length; i++) {
				sb.append(Integer.toString((byteshashed[i] & 0xff) + 0x100, 16).substring(1));
			}
			result = sb.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// MÉTODO PARA HACER HASH DE UNA CONTRASEÑA CON SHA-256 Y SALT
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt.getBytes());
        byte[] hashedPassword = md.digest(encode(password).getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

     // GENERAR UNA SALT ALEATORIA
     public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
	
	

	private void iniciarUsuario(ResultSet rs, Usuario usuario, String[] campos) throws SQLException {
		usuario.setIdentificacion(rs.getString(campos[0]));
		usuario.setApellido(rs.getString(campos[1]));
		if (rs.getString(campos[2]).equals("") || rs.getString(campos[2]).equals(" ")) {
			usuario.setNombre("NN");
		} else {
			usuario.setNombre(rs.getString(campos[2]));
		}

		usuario.setEmail(rs.getString(campos[3]));
	}

}
