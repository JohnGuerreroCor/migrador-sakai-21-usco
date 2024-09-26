package org.usco.lcms.dao.consumer.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.usco.lcms.dao.consumer.UsuarioConsumerDao;
import org.usco.lcms.modelo.Usuario;

@Repository
public class UsuarioDaoMySQLJDBCTemplateImpl implements UsuarioConsumerDao {

	@Autowired
	@Qualifier("dataSourceMysql")
	DataSource dataSource;

	public void agregarUsuario(Usuario usuario) {
		System.out.println("Se agrego el usuario"+usuario);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		try {
			String sql = "INSERT INTO SAKAI_USER (USER_ID, EMAIL, FIRST_NAME, LAST_NAME, TYPE, PW, CREATEDBY, MODIFIEDBY, CREATEDON, MODIFIEDON)";
			sql = sql + " VALUES (?, ?, ?, ?, 'registered', 'PBKDF2:dvd0fU59EWkLeJ1ArvZXkw==:c78CIgWwQkMccA70nqrPwA==', 'admin', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

			jdbcTemplate.update(sql, usuario.getLogin(), usuario.getEmail(), usuario.getNombre(),
					usuario.getApellido());

			sql = "INSERT INTO SAKAI_USER_ID_MAP (USER_ID, EID)";
			sql = sql + " VALUES (?, ?)";

			jdbcTemplate.update(sql, usuario.getLogin(), usuario.getLogin());
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}

	}

	public Usuario buscarUsuario(String usuario) {
		String sql = "SELECT USER_ID FROM SAKAI_USER WHERE USER_ID = ?";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Usuario> usuarios = jdbcTemplate.query(sql, new Object[] {usuario}, new RowMapper<Usuario>() {

			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				Usuario usuario = new Usuario();
				usuario.setLogin(rs.getString(1));
				return usuario;
			}

		});
		return usuarios.size() > 0 ? usuarios.get(0) : null;
	}

	public void actualizarUsuario(Usuario usuario) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			String sql = "UPDATE SAKAI_USER SET EMAIL=?, FIRST_NAME=?, LAST_NAME=? , MODIFIEDON=CURRENT_TIMESTAMP "
					+ "WHERE USER_ID = ?";
			jdbcTemplate.update(sql, usuario.getEmail(), usuario.getNombre(), usuario.getApellido(),
					usuario.getLogin());
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}

}
