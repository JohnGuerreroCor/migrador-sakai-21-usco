package org.usco.lcms;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ComponentScan(basePackages = { "org.usco.lcms.dao", "org.usco.lcms.service" })
@PropertySource(value = { "file:/etc/migrador/application.properties" })
@ContextConfiguration
public class SpringContextTestConfig {

	@Autowired
	Environment env;

	@Bean(name = "dataSourceMysql")
	public DataSource dataSourceMysql() throws Exception {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getRequiredProperty("db.mysql.driver"));
		dataSource.setUrl(env.getRequiredProperty("db.mysql.url"));
		dataSource.setUsername(env.getRequiredProperty("db.mysql.usuario"));
		dataSource.setPassword(env.getRequiredProperty("db.mysql.password"));
		return dataSource;
	}

	@Bean(name = "dataSourceMssql")
	public DataSource dataSourceMssql() throws Exception {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getRequiredProperty("db.mssql.driver"));
		dataSource.setUrl(env.getRequiredProperty("db.mssql.url"));
		dataSource.setUsername(env.getRequiredProperty("db.mssql.usuario"));
		dataSource.setPassword(env.getRequiredProperty("db.mssql.password"));
		return dataSource;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}