package org.usco.lcms.dao.provider.impl;

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
import org.usco.lcms.dao.ProviderUtilDao;

@Repository
public class ProviderUtilDaoJDBCTemplateImpl implements ProviderUtilDao {

    @Autowired
    @Qualifier("dataSourceMssql")
    DataSource dataSource;

    @Value("${WEP_PERIODO_ACTUAL}")
    private String WEP_PERIODO_ACTUAL;

    @Value("${WEP_LCMS_SINCRONIZADOR_ACTIVO}")
    private String WEP_LCMS_SINCRONIZADOR_ACTIVO;

    @Value("${WEP_LCMS_SINCRONIZADOR_MIGRACION}")
    private String WEP_LCMS_SINCRONIZADOR_MIGRACION;

    public String getCalendario() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT wep_valor FROM web_parametro WHERE wep_nombre = '" + WEP_PERIODO_ACTUAL + "'";
        List<String> strLst = jdbcTemplate.query(sql, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        return strLst.size() > 0 ? strLst.get(0) : null;
    }

    @Override
    public boolean isEncendido() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT wep_valor FROM web_parametro WHERE wep_nombre = '" + WEP_LCMS_SINCRONIZADOR_ACTIVO + "'";
        System.out.println(sql);
        List<String> strLst = jdbcTemplate.query(sql, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });

        if (strLst.size() > 0 && strLst.get(0).equals("1"))
            return true;
        else
            return false;

    }

    @Override
    public boolean isMigracion() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT wep_valor FROM web_parametro WHERE wep_nombre = '" + WEP_LCMS_SINCRONIZADOR_MIGRACION
                + "'";
        System.out.println(sql);
        List<String> strLst = jdbcTemplate.query(sql, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });

        if (strLst.size() > 0 && strLst.get(0).equals("1"))
            return true;
        else
            return false;
    }

}
