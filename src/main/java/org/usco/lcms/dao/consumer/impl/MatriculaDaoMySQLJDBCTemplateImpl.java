package org.usco.lcms.dao.consumer.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.usco.lcms.dao.JDBCTemplateDaoUtil;
import org.usco.lcms.dao.consumer.MatriculaConsumerDao;

@Repository
public class MatriculaDaoMySQLJDBCTemplateImpl implements MatriculaConsumerDao {

	@Autowired
	@Qualifier("dataSourceMysql")
	DataSource dataSource;

	@Autowired
	JDBCTemplateDaoUtil jdbcTemplateDaoUtil;

	@Value("${SAKAI_STUDENT}")
	private int studentTRoleId;

	public void matricularEstudiante(long cursoCodigo, String estudianteCodigo) {
		String sql = "SELECT SITE_ID FROM SAKAI_SITE";
		sql = sql + " WHERE TITLE LIKE '" + cursoCodigo + "%'";
		String SITE_ID = jdbcTemplateDaoUtil.getString(sql);
		if (SITE_ID == null) {
			throw new RuntimeException("Curso con código " + cursoCodigo + " no encontrado");
		}

		sql = "SELECT REALM_KEY FROM SAKAI_REALM";
		sql = sql + " WHERE REALM_ID LIKE '%" + SITE_ID + "%'";
		String REALM_KEY = jdbcTemplateDaoUtil.getString(sql);

		sql = "SELECT USER_ID FROM SAKAI_USER";
		sql = sql + " WHERE USER_ID = '" + estudianteCodigo + "'";
		String USER_ID = jdbcTemplateDaoUtil.getString(sql);
		if (USER_ID == null) {
			throw new RuntimeException("Estudiante con código " + estudianteCodigo + " no encontrado");
		}

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		sql = "INSERT INTO SAKAI_SITE_USER (SITE_ID, USER_ID, PERMISSION)";
		sql = sql + " VALUES ('" + SITE_ID + "', '" + USER_ID + "', 1)";
		jdbcTemplate.update(sql);

		sql = "INSERT INTO SAKAI_REALM_RL_GR (REALM_KEY, USER_ID, ROLE_KEY, ACTIVE, PROVIDED)";
		sql = sql + " VALUES (" + REALM_KEY + ", '" + USER_ID + "', " + studentTRoleId + ", 1, 0)";
		jdbcTemplate.update(sql);
	}

}
