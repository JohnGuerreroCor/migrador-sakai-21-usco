package org.usco.lcms.dao.provider.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.usco.lcms.dao.provider.MatriculaProviderDao;
import org.usco.lcms.modelo.Matricula;

@Repository
public class MatriculaProviderDaoJDBCTemplateImpl implements MatriculaProviderDao {

	@Autowired
	@Qualifier("dataSourceMssql")
	DataSource dataSource;

	public List<Matricula> listarMatriculados(String calendario, boolean sincronizacion, int minutos) {

		String sql = "SELECT distinct m.* FROM matricula_virtual m "
				+ " LEFT JOIN curso_virtual c ON m.cur_codigo = c.cur_codigo "
				+ " WHERE m.cal_codigo in (select cal_codigo from calendario where cal_nombre like ?)";
		Object[] parametros = null;

		if (sincronizacion) {
			sql += "AND ((m.mac_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
					+ " OR (m.mat_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate())"
					+ " OR (c.cur_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()))";
			parametros = new Object[] { calendario + "%", minutos, minutos, minutos };
		} else {
			parametros = new Object[] { calendario + "%" };
		}
		
		sql += " AND (m.us IS NOT NULL)  ORDER BY estudiante DESC";
		System.out.println(sql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Matricula> listaMatriculados = jdbcTemplate.query(sql, parametros, new RowMapper<Matricula>() {

			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Matricula matricula = new Matricula();
				matricula.setCurso(rs.getLong("cur_codigo"));
				matricula.setEstudiante(rs.getString("us"));
				
				matricula.setTipo(rs.getShort("maa_codigo"));
				return matricula;
			}
		});
		return listaMatriculados;
	}

}
