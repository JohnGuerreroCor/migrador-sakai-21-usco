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
import org.usco.lcms.dao.provider.CursoProviderDao;
import org.usco.lcms.modelo.ConfiguracionParcial;
import org.usco.lcms.modelo.Curso;

@Repository
public class CursoProviderDaoJDBCTemplateImpl implements CursoProviderDao {

	@Autowired
	@Qualifier("dataSourceMssql")
	DataSource dataSource;

	public List<Curso> listarCursos(String calendario, boolean sincronizacion, int minutos) {

		String sql = "SELECT * FROM curso_virtual WHERE cal_codigo in (SELECT cal_codigo " + "FROM calendario "
				+ " WHERE cal_nombre like ?) ";
		Object[] parametros = null;

		if (sincronizacion) {
			sql += "AND (asi_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate()) "
					+ "OR (cur_fecha_actualizacion BETWEEN dateadd(minute,-?,getdate()) AND getdate())";
			parametros = new Object[] { calendario + "%", minutos, minutos };
		} else {
			parametros = new Object[] { calendario + "%" };
		}
		
		sql = sql + " OR cur_codigo in (144628)";

		System.out.println("***************SQL CURSOS**************************");
		System.out.println(sql);
		System.out.println("********MINUTOS******" + minutos);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Curso> listaCursos = jdbcTemplate.query(sql, parametros, new RowMapper<Curso>() {

			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {

				Curso curso = new Curso();
				curso.setCodigo(rs.getLong("cur_codigo"));
				curso.setGrupo(rs.getString("cur_grupo"));
				curso.setUrl(rs.getString("cur_url"));
				curso.setAsignatura(rs.getLong("asi_codigo"));
				curso.setAsignaturaNombre(rs.getString("asi_nombre"));
				curso.setUnidadPersona(rs.getLong("uap_codigo"));
				curso.setPersona(rs.getLong("per_codigo"));
				
				if (rs.getString("formal").equals("1")) {
					curso.setPersonaIdentificacion(rs.getString("per_identificacion"));
				} else {
					if (rs.getString("us") != "" && rs.getString("us") != null) {
						// Usuario administratio
						curso.setPersonaIdentificacion(rs.getString("us"));
					} else {
						if (rs.getString("usd") != "" && rs.getString("usd") != null) {
							// usuario Docente
							curso.setPersonaIdentificacion(rs.getString("usd"));	
						}else{
							curso.setPersonaIdentificacion(rs.getString("per_identificacion"));
						}
					}
				}

				curso.setPersonaApellido(rs.getString("per_apellido"));
				curso.setPersonaNombre(rs.getString("per_nombre"));
				curso.setPersonaEmail(rs.getString("per_email_interno"));
				curso.setFecha(rs.getString("cur_fecha_actualizacion"));
				return curso;
			}

		});
		return listaCursos;
	}

	public List<Curso> listarCursosTeachingAssitant(String calendario, boolean sincronizacion, int minutos) {
		String sql = "SELECT DISTINCT c.uap_codigo as 'uap_codigo', eso.uap_codigo as 'assitant_de_curso', eso_actividad"
				+ ", uap.per_codigo as 'per_codigo_dueno' , uap_assitant.per_codigo as 'per_codigo', c.cur_codigo, p.per_nombre"
				+ ", p.per_apellido, p.per_identificacion, p.per_email_interno, a.asi_nombre, a.asi_codigo, cur_grupo "
				+ "FROM espacio_ocupacion eso " + "INNER JOIN curso c ON c.cur_codigo = eso.eso_actividad "
				+ "INNER JOIN uaa_personal uap ON uap.uap_codigo = c.uap_codigo "
				+ "INNER JOIN uaa_personal uap_assitant ON uap_assitant.uap_codigo = eso.uap_codigo "
				+ "INNER JOIN persona p ON p.per_codigo = uap_assitant.per_codigo "
				+ "INNER JOIN persona pdueno ON pdueno.per_codigo = uap.per_codigo "
				+ "INNER JOIN asignatura a ON a.asi_codigo = c.asi_codigo "
				+ "WHERE c.cal_codigo IN ((SELECT cal_codigo " + "FROM calendario WHERE cal_nombre like ?)) "
				+ "AND c.uap_codigo != eso.uap_codigo " + "AND p.per_apellido NOT LIKE '%NN%' ";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Curso> listaCursos = jdbcTemplate.query(sql, new Object[] { calendario + "%" }, new RowMapper<Curso>() {

			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {

				Curso curso = new Curso();
				curso.setCodigo(rs.getLong("cur_codigo"));
				curso.setGrupo(rs.getString("cur_grupo"));
				curso.setAsignatura(rs.getLong("asi_codigo"));
				curso.setAsignaturaNombre(rs.getString("asi_nombre"));
				curso.setUnidadPersona(rs.getLong("uap_codigo"));
				curso.setPersona(rs.getLong("per_codigo"));
				curso.setPersonaIdentificacion(rs.getString("per_identificacion"));
				curso.setPersonaApellido(rs.getString("per_apellido"));
				curso.setPersonaNombre(rs.getString("per_nombre"));
				curso.setPersonaEmail(rs.getString("per_email_interno"));
				return curso;
			}

		});

		return listaCursos;
	}

	public List<ConfiguracionParcial> listarConfiguraionParcialCursos(String calendario, boolean sincronizacion,
			int minutos) {
		String sql = "SELECT cue_curso, cue_tipo, cue_porcentaje, cue_nombre FROM cursoweb.curso_evaluacion   "
				+ "INNER JOIN periodo ON cursoweb.curso_evaluacion.cue_periodo = periodo.per_codigo "
				+ "WHERE periodo.per_nombre LIKE ?";
		Object[] parametros = null;

		if (sincronizacion) {
			sql += "AND (cue_fecha_actualizacion  BETWEEN dateadd(minute,?,getdate()) AND getdate())";
			parametros = new Object[] { calendario + "%", minutos };
		} else {
			parametros = new Object[] { calendario + "%" };
		}

		System.out.println(sql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<ConfiguracionParcial> listaConfiguracionParcial = jdbcTemplate.query(sql, parametros,
				new RowMapper<ConfiguracionParcial>() {

					public ConfiguracionParcial mapRow(ResultSet rs, int rowNum) throws SQLException {

						ConfiguracionParcial configuracionParcial = new ConfiguracionParcial();
						configuracionParcial.setCategoria(rs.getInt("cue_tipo"));
						configuracionParcial.setCodigoCurso(rs.getInt("cue_curso"));
						configuracionParcial.setPorcentaje(rs.getFloat("cue_porcentaje"));

						return configuracionParcial;
					}

				});
		return listaConfiguracionParcial;
	}
}