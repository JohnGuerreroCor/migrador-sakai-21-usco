package org.usco.lcms.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//https://dzone.com/articles/spring-security-4-authenticate-and-authorize-users
//ok database
//https://dzone.com/articles/spring-security-4-authenticate-and-authorize-users
	
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("juan").password("123456").roles("USER");
		auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("dba").password("123456").roles("ADMIN","DBA");
	}
	
	//.antMatchers("/").access("hasRole('ADMIN')")
	//.antMatchers("/usuario").access("hasRole('ADMIN')")
	//.antMatchers("/", "/home").permitAll()
	//.and().formLogin().loginPage("/login")
	//.usernameParameter("ssoId").passwordParameter("password")
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  
		http.authorizeRequests() 		
	  		.antMatchers("/", "/home").permitAll()
	  		.antMatchers("/pais/**").permitAll()
	  		.antMatchers("/usuario").permitAll()
	  		.antMatchers("/admin/**").access("hasRole('ADMIN')")
	  		.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
	  		.and().formLogin().loginPage("/login")
	  		.usernameParameter("ssoId").passwordParameter("password")
	  		.and().csrf()
	  		.and().exceptionHandling().accessDeniedPage("/Access_Denied");
	}
}
