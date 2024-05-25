package com.luca.moviewebscraper.webscraperdata;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DataConfig {
	
	@Bean
	public DataSource dataSource() {
		
		HikariConfig config=new HikariConfig();
		
		
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/movie_reviews");
		config.setUsername("tiso");
		config.setPassword("password");
		config.setDriverClassName("org.postgresql.Driver");
		return new HikariDataSource(config);
	}
	
	

}
