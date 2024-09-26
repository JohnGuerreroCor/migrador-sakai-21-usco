package org.usco.lcms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JDBCTemplateDaoUtil {

	@Autowired
	@Qualifier("dataSourceMysql")
	DataSource dataSource;

	public String getString(String sql) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<String> strLst = jdbcTemplate.query(sql, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
		return strLst.size() > 0 ? strLst.get(0) : null;
	}
}
