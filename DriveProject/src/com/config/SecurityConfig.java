package com.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {		
		auth.jdbcAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance()).dataSource(dataSource)
			.usersByUsernameQuery("select username, password, enabled from cloud.users where username = ?")
			.authoritiesByUsernameQuery("select username, rolename from cloud.users where username = ?");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
			.and().formLogin().loginPage("/login").permitAll().failureUrl("/login?error")
			.usernameParameter("username").passwordParameter("password").and().csrf().disable();
	}
	
	// This is used to add external resources like css and js
	@Override
	public void configure(WebSecurity web) {
	    web.ignoring().antMatchers("/resources/**");
	}

}
