package org.usco.lcms.dao.consumer.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.usco.lcms.dao.consumer.CursoConsumerDao;
import org.usco.lcms.modelo.ConfiguracionParcial;
import org.usco.lcms.modelo.Curso;
import org.usco.lcms.utilidad.GeneradorRealm;

@Repository
public class CursoDaoMySQLJDBCTemplateImpl implements CursoConsumerDao {

	@Autowired
	@Qualifier("dataSourceMysql")
	DataSource dataSource;

	@Value("${REALM_KEY_CURSO_MODELO}")
	private String REALM_KEY_CURSO_MODELO;

	@Value("${REALM_ID_CURSO_MODELO}")
	private String REALM_ID_CURSO_MODELO;

	@Value("${SAKAI_INSTRUCTOR}")
	private int instructorRole;

	@Value("${SAKAI_TEACHING_ASSISTANT}")
	private int teachingAssitantRole;

	@Value("${SAKAI_STUDENT}")
	private int studentTRoleId;

	public void agregarCurso(Curso curso, boolean crearInstructor) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		System.out.println("Agregando nuevo curso...");
		String sql = "";
		try {
			// ------------------------------------------------------------
			// PASO 1 CREAR REALM
			// ------------------------------------------------------------
			System.out.println("Generando REALM_ID...");
			String REALM_ID = GeneradorRealm.generateRealmId();
			System.out.println("REALM_ID generado [" + REALM_ID + "]");

			System.out.println("Consultando consecutivo SAKAI_REALM");
			sql = "SELECT MAX(REALM_KEY) + 1 FROM SAKAI_REALM";
			String REALM_KEY = jdbcTemplate.query(sql, new RowMapper<String>() {

				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString(1);
				}

			}).get(0);

			if (!REALM_KEY.equals("error")) {
				System.out.println("Consecutivo [" + REALM_KEY + "]");
				System.out.println("Guardando en SAKAI_REALM...");
				sql = "INSERT INTO SAKAI_REALM VALUES ";
				sql = sql + "(" + REALM_KEY + ",'" + "/site/" + REALM_ID + "',NULL," + instructorRole
						+ ",'admin','admin',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
				jdbcTemplate.update(sql);
				System.out.println("Registro guardado!");

				// ----------------------------------------------------------------------------
				// PASO 2 CREAR FUNCIONALIDAD DEL SITIO
				// ----------------------------------------------------------------------------
				System.out.println(
						"Generando funcionalidad del sitio desde curso modelo [" + REALM_KEY_CURSO_MODELO + "]...");
				sql = "INSERT INTO SAKAI_REALM_RL_FN (REALM_KEY, ROLE_KEY, FUNCTION_KEY)";
				sql = sql + "SELECT " + REALM_KEY + ", ROLE_KEY, FUNCTION_KEY FROM SAKAI_REALM_RL_FN WHERE REALM_KEY = "
						+ REALM_KEY_CURSO_MODELO;
				jdbcTemplate.update(sql);
				System.out.println("Funcionalidad del sitio generada!");

				// ----------------------------------------------------------------------------
				// PASO 3 CREAR GRUPOS DEL SITIO
				// ----------------------------------------------------------------------------
				System.out.println("Generando grupos del sitio...");
				sql = "INSERT INTO SAKAI_REALM_RL_GR (REALM_KEY, USER_ID, ROLE_KEY, ACTIVE, PROVIDED) " + "VALUES ('"
						+ REALM_KEY + "', '" + curso.getPersonaIdentificacion() + "', " + instructorRole + ", 1, 0)";
				/*
				 * sql = sql + "SELECT " + REALM_KEY +
				 * ", USER_ID, ROLE_KEY, ACTIVE, PROVIDED FROM SAKAI_REALM_RL_GR WHERE REALM_KEY = "
				 * + REALM_KEY_CURSO_MODELO;
				 */
				jdbcTemplate.update(sql);
				System.out.println("grupos del sitio generados!");

				// ----------------------------------------------------------------------------
				// PASO 4 CREAR DESCRIPCION DE LOS GRUPOS DEL SITIO
				// ----------------------------------------------------------------------------
				System.out.println("Generando descripcion de los grupos del sitio [" + REALM_KEY + "]...");
				sql = "INSERT INTO SAKAI_REALM_ROLE_DESC (REALM_KEY, ROLE_KEY, DESCRIPTION, PROVIDER_ONLY)";
				sql = sql + "SELECT " + REALM_KEY
						+ ", ROLE_KEY, DESCRIPTION, PROVIDER_ONLY FROM SAKAI_REALM_ROLE_DESC WHERE REALM_KEY = "
						+ REALM_KEY_CURSO_MODELO;
				jdbcTemplate.update(sql);
				System.out.println("Descripción de los grupos generados!");

				// ----------------------------------------------------------------------------
				// PASO 5 CREAR SITIO
				// ----------------------------------------------------------------------------
				System.out.println("Creando sitio para el curso...");
				sql = "INSERT INTO SAKAI_SITE (SITE_ID, TITLE, TYPE, SHORT_DESC, DESCRIPTION, ICON_URL, INFO_URL, SKIN,";
				sql = sql
						+ " PUBLISHED, JOINABLE, PUBVIEW, JOIN_ROLE, CREATEDBY, MODIFIEDBY, CREATEDON, MODIFIEDON, IS_SPECIAL,";
				sql = sql + " IS_USER, CUSTOM_PAGE_ORDERED, IS_SOFTLY_DELETED, SOFTLY_DELETED_DATE, CUR_CODIGO, CUR_URL) VALUES";
				sql = sql + "('" + REALM_ID + "','" + curso.getCodigo() + " - " + curso.getAsignaturaNombre()
						+ "','course',NULL,NULL,NULL,NULL,NULL,1,'0','1',NULL,'admin','admin',CURRENT_TIMESTAMP "
						+ ",CURRENT_TIMESTAMP,'0','0','0','0',NULL, " + curso.getCodigo() + ",'" + curso.getUrl() + "')";
				jdbcTemplate.update(sql);
				System.out.println("Sitio creado!");

				// ----------------------------------------------------------------------------
				// PASO 6 CREAR PAGINAS DEL SITIO
				// ----------------------------------------------------------------------------
				System.out.println("Creando paginas del sitio...");
				sql = "INSERT INTO SAKAI_SITE_PAGE (PAGE_ID, SITE_ID, TITLE, LAYOUT, SITE_ORDER, POPUP)";
				sql = sql + " SELECT CONCAT(SUBSTRING(LOWER(HEX(ROUND(100000000000.0 * RAND()))),1, 8),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(100000000.0 * RAND()))),2,4),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(100000000.0 * RAND()))),2,4),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(100000000.0 * RAND()))),2,4),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(1000000000000000000.0 * RAND()))),1,12)";
				sql = sql + " ) AS ID";
				sql = sql + " , '" + REALM_ID + "', TITLE, LAYOUT, SITE_ORDER, POPUP";
				sql = sql + " FROM SAKAI_SITE_PAGE";
				sql = sql + " WHERE SITE_ID = '" + REALM_ID_CURSO_MODELO + "'";
				jdbcTemplate.update(sql);
				System.out.println("Paginas creadas!");

				// ----------------------------------------------------------------------------
				// PASO 7 CREAR PROPIEDADES DE LAS PAGINAS DEL SITIO
				// ----------------------------------------------------------------------------
				System.out.println("Creando propiedades para las paginas");
				sql = "INSERT INTO SAKAI_SITE_PAGE_PROPERTY (SITE_ID, PAGE_ID, NAME, VALUE)";
				sql = sql + "SELECT SITE_ID, PAGE_ID, 'sitePage.customTitle', 'true' FROM SAKAI_SITE_PAGE";
				sql = sql + " WHERE SITE_ID = '" + REALM_ID + "' AND TITLE NOT LIKE 'Inicio'";
				sql = sql + " UNION";
				sql = sql + " SELECT SITE_ID, PAGE_ID, 'is_home_page', 'true' FROM SAKAI_SITE_PAGE";
				sql = sql + " WHERE SITE_ID = '" + REALM_ID + "' AND TITLE LIKE 'Inicio'";
				System.out.println("Propiedades creadas!");
				jdbcTemplate.update(sql);

				// ----------------------------------------------------------------------------
				// PASO 8 CREAR PROPIEDADES DEL SITIO
				// ----------------------------------------------------------------------------
				System.out.println("Creando propiedades del sitio...");
				sql = "INSERT INTO SAKAI_SITE_PROPERTY (SITE_ID, NAME, VALUE)";
				sql = sql + "SELECT '" + REALM_ID + "', NAME, VALUE";
				sql = sql + " FROM SAKAI_SITE_PROPERTY WHERE SITE_ID = '" + REALM_ID_CURSO_MODELO + "'";
				System.out.println("Propiedades creadas!");
				jdbcTemplate.update(sql);

				// ----------------------------------------------------------------------------
				// PASO 9 CREAR HERRAMIENTAS DEL SITIO
				// ----------------------------------------------------------------------------
				/// OK 20162 LA ULTIMA tools
				System.out.println("Creando herramientas del sitio...");
				sql = "INSERT INTO SAKAI_SITE_TOOL (TOOL_ID, PAGE_ID, SITE_ID, REGISTRATION, PAGE_ORDER, TITLE, LAYOUT_HINTS)";
				sql = sql + " SELECT CONCAT(SUBSTRING(LOWER(HEX(ROUND(100000000000.0 * RAND()))),1, 8),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(100000000.0 * RAND()))),2,4),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(100000000.0 * RAND()))),2,4),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(100000000.0 * RAND()))),2,4),";
				sql = sql + " '-',";
				sql = sql + " SUBSTRING(LOWER(HEX(ROUND(1000000000000000000.0 * RAND()))),1,12)";
				sql = sql + " ) AS ID,";
				sql = sql + " SAKAI_SITE_PAGE.PAGE_ID, SAKAI_SITE_PAGE.SITE_ID, SST.REGISTRATION,";
				sql = sql + " SST.PAGE_ORDER, SST.TITLE, SST.LAYOUT_HINTS";
				sql = sql + " FROM SAKAI_SITE_PAGE, SAKAI_SITE_PAGE AS SSP, SAKAI_SITE_TOOL AS SST";
				sql = sql + " WHERE SSP.PAGE_ID = SST.PAGE_ID";
				sql = sql + " AND SAKAI_SITE_PAGE.TITLE = SSP.TITLE";
				sql = sql + " AND SAKAI_SITE_PAGE.SITE_ID = '" + REALM_ID + "'";
				sql = sql + " AND SST.SITE_ID = '" + REALM_ID_CURSO_MODELO + "'";
				jdbcTemplate.update(sql);
				System.out.println("Herramientas del sitio creadas!");

				// ----------------------------------------------------------------------------
				// PERMISOS
				// ----------------------------------------------------------------------------
				jdbcTemplate.update(
						"INSERT INTO SAKAI_REALM_RL_FN VALUES (" + REALM_KEY + ", " + studentTRoleId + ", 58)");
				jdbcTemplate.update(
						"INSERT INTO SAKAI_REALM_RL_FN VALUES (" + REALM_KEY + ", " + studentTRoleId + ", 55)");
				jdbcTemplate.update(
						"INSERT INTO SAKAI_REALM_RL_FN VALUES (" + REALM_KEY + ", " + studentTRoleId + ", 54)");
				jdbcTemplate
						.update("INSERT INTO SAKAI_SITE_PROPERTY VALUES ('" + REALM_ID + "', 'can_manage_resources', 'false')");
				
				// ----------------------------------------------------------------------------
				// PASO 10 CREAR USUARIOS INSTRUCTOR DEL CURSO (SITIO)
				// ----------------------------------------------------------------------------
				if (crearInstructor) {
					System.out.println("Asignando instructor del curso...");
					sql = "INSERT INTO SAKAI_SITE_USER (SITE_ID, USER_ID, PERMISSION)";
					sql = sql + " VALUES ('" + REALM_ID + "', '" + curso.getPersonaIdentificacion() + "', -1)";
					System.out.println("[+] insert instructor " + sql);
					jdbcTemplate.update(sql);
					System.out.println("Instructor asignado!");
				}
			} else {
				System.err.println("Error al extraer el resultado!");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}

	}

	public Curso buscarCurso(String curCodigo) {
		String sql = "SELECT " + "SUBSTRING_INDEX(TITLE, '-', 1) AS codigo" + ", SUBSTRING_INDEX(TITLE, '-', -1) AS T"
				+ ", SITE_ID" + ", sr.REALM_KEY " + "FROM SAKAI_SITE ss, SAKAI_REALM sr "
				+ "WHERE SUBSTRING_INDEX(TITLE, '-', 1) = ? AND sr.REALM_ID LIKE CONCAT('%', ss.SITE_ID)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Curso> cursos = jdbcTemplate.query(sql, new Object[] { curCodigo }, new RowMapper<Curso>() {

			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {
				Curso curso = new Curso();
				String codigo = rs.getString("codigo");
				curso.setCodigo(Long.parseLong(codigo.substring(0, codigo.length() - 1)));
				curso.setAsignaturaNombre(rs.getString("T"));
				curso.setRealmId(rs.getString("SITE_ID"));
				curso.setRealmKey(rs.getInt("REALM_KEY"));
				curso.setPersonaIdentificacion(buscarInstructor(rs.getInt("REALM_KEY")));
				return curso;
			}

		});
		return cursos.size() > 0 ? cursos.get(0) : null;
	}

	public String buscarInstructor(int realmKey) {
		String sql = "SELECT USER_ID FROM SAKAI_REALM_RL_GR WHERE REALM_KEY = '" + realmKey
				+ "' AND ACTIVE = 1 AND ROLE_KEY = " + instructorRole;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<String> userIds = jdbcTemplate.query(sql, new RowMapper<String>() {

			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("USER_ID");
			}

		});
		return userIds.size() > 0 ? userIds.get(0) : "";

	}

	public void activarDesactivarUsuarioACurso(String userId, int realmKey, boolean activar) {
		String sql = "UPDATE SAKAI_REALM_RL_GR SET ACTIVE = " + (activar ? "1" : "0") + "  WHERE REALM_KEY = '"
				+ realmKey + "' AND USER_ID = '" + userId + "'";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql);
	}

	public void agregarUsuarioACurso(String userId, int realmKey, int roleId) {
		String sql = "INSERT INTO SAKAI_REALM_RL_GR (REALM_KEY, USER_ID, ROLE_KEY, ACTIVE, PROVIDED)";
		sql = sql + " VALUES (" + realmKey + ", '" + userId + "', " + roleId + ", 1, 0)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql);
	}

	public void actualizarCurso(Curso cursoNuevo, Curso cursoAnterior) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		System.out.println("Actualizando datos del sitio...");
		String sql = "UPDATE SAKAI_SITE SET TITLE = ? WHERE SUBSTRING_INDEX(TITLE, '-', 1) = ?";
		jdbcTemplate.update(sql, cursoAnterior.getCodigo() + " - " + cursoNuevo.getAsignaturaNombre(),
				cursoAnterior.getCodigo());
		System.out.println("Sitio actualizado!");
	}

	@Transactional
	public void generarItemDeCalificacion() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS CURSOS (ID VARCHAR(36));");
		jdbcTemplate.execute(
				"INSERT INTO CURSOS SELECT SITE_ID FROM SAKAI_SITE WHERE LENGTH(SITE_ID)=36 AND SITE_ID NOT IN (SELECT GRADEBOOK_UID FROM GB_GRADEBOOK_T);");
		jdbcTemplate.execute(
				"INSERT INTO GB_GRADEBOOK_T (VERSION,GRADEBOOK_UID,`NAME`,ASSIGNMENTS_DISPLAYED,COURSE_GRADE_DISPLAYED,COURSE_POINTS_DISPLAYED,TOTAL_POINTS_DISPLAYED,COURSE_AVERAGE_DISPLAYED,ALL_ASSIGNMENTS_ENTERED,LOCKED,GRADE_TYPE,CATEGORY_TYPE,COURSE_LETTER_GRADE_DISPLAYED) SELECT 1,ID,ID,1,0,0,0,0,0,0,1,3,1 FROM CURSOS;");
		jdbcTemplate.execute(
				"INSERT INTO GB_GRADE_MAP_T (OBJECT_TYPE_ID,VERSION,GRADEBOOK_ID,GB_GRADING_SCALE_T) SELECT 0,0,b.ID,s.ID FROM CURSOS AS c INNER JOIN GB_GRADEBOOK_T AS b ON b.GRADEBOOK_UID = c.ID     JOIN (SELECT ID FROM GB_GRADING_SCALE_T WHERE SCALE_UID='UniversityNumbersMapping') AS s;");
		jdbcTemplate.execute(
				"UPDATE GB_GRADEBOOK_T,GB_GRADE_MAP_T SET GB_GRADEBOOK_T.SELECTED_GRADE_MAPPING_ID=GB_GRADE_MAP_T.ID WHERE GB_GRADEBOOK_T.ID=GB_GRADE_MAP_T.GRADEBOOK_ID;");
		jdbcTemplate.execute(
				"INSERT INTO GB_GRADE_TO_PERCENT_MAPPING_T(GRADE_MAP_ID,PERCENT,LETTER_GRADE) SELECT m.ID, p.PERCENT, p.LETTER_GRADE FROM GB_GRADE_MAP_T AS m INNER JOIN GB_GRADING_SCALE_PERCENTS_T AS p ON m.GB_GRADING_SCALE_T=p.GRADING_SCALE_ID WHERE m.ID NOT IN (SELECT DISTINCT GRADE_MAP_ID FROM GB_GRADE_TO_PERCENT_MAPPING_T);");
		jdbcTemplate.execute("DROP TABLE CURSOS");
		jdbcTemplate.execute(
				"INSERT INTO GB_GRADABLE_OBJECT_T (OBJECT_TYPE_ID,VERSION,GRADEBOOK_ID,NAME,REMOVED,HIDE_IN_ALL_GRADES_TABLE) SELECT 2,0,ID,'Course Grade',0,0 FROM GB_GRADEBOOK_T WHERE ID NOT IN (SELECT DISTINCT GRADEBOOK_ID FROM GB_GRADABLE_OBJECT_T);");
		jdbcTemplate.execute(
				"INSERT INTO GB_CATEGORY_T ( GRADEBOOK_ID, VERSION, NAME, WEIGHT, DROP_LOWEST, REMOVED, IS_EXTRA_CREDIT, DROP_HIGHEST, KEEP_HIGHEST, TIPO_COHORTE ) SELECT t.ID, 0, g.Nombre, g.Porcentaje, 0, 0, 0, 0, 0, g.TIPO_COHORTE FROM GB_GRADEBOOK_T AS t JOIN ( SELECT '1. Corte I' AS Nombre, 0.3 AS Porcentaje, 6 AS TIPO_COHORTE UNION SELECT '2. Corte II' AS Nombre, 0.3 AS Porcentaje, 7 AS TIPO_COHORTE UNION SELECT '3. Corte III' AS Nombre, 0.4 AS Porcentaje, 8 AS TIPO_COHORTE ) AS g WHERE ID NOT IN (SELECT DISTINCT GRADEBOOK_ID FROM GB_CATEGORY_T)");

	}

	public String buscarParticipanteEnCurso(String userId, int realmKey, int roleId) {
		System.out.println("QUERY:: " + "SELECT USER_ID FROM SAKAI_REALM_RL_GR WHERE REALM_KEY = '" + realmKey + "' AND USER_ID = '"
				+ userId + "' AND ROLE_KEY = " + roleId);
		String sql = "SELECT USER_ID FROM SAKAI_REALM_RL_GR WHERE REALM_KEY = '" + realmKey + "' AND USER_ID = '"
				+ userId + "' AND ROLE_KEY = " + roleId;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<String> userIds = jdbcTemplate.query(sql, new RowMapper<String>() {

			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("USER_ID");
			}

		});
		return userIds.size() > 0 ? userIds.get(0) : "";
	}

	public void actualizarItemDeCalificacion(ConfiguracionParcial configuracionParcial) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		System.out.println("Actualizando configuración de calificaciones del sitio...");
		String sql = "UPDATE GB_CATEGORY_T GBC " + "INNER JOIN GB_GRADEBOOK_T GB ON GBC.GRADEBOOK_ID = GB.ID "
				+ "INNER JOIN SAKAI_SITE SS ON SS.SITE_ID = GB.GRADEBOOK_UID "
				+ "INNER JOIN SAKAI_SITE_PROPERTY SSP ON SS.SITE_ID = SSP.SITE_ID "
				+ "SET GBC.WEIGHT = ?, GBC.LOCKED=true, VALUE = 'false' "
				+ "WHERE SS.CUR_CODIGO = ? AND TIPO_COHORTE = ? AND SSP.NAME = 'can_manage_categories'";
		jdbcTemplate.update(sql, configuracionParcial.getPorcentaje() / 100, configuracionParcial.getCodigoCurso(),
				configuracionParcial.getCategoria());
		System.out.println("Configuración de calificaciones actualizada!");
	}
}