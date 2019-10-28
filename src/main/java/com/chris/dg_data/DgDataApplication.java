package com.chris.dg_data;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class DgDataApplication {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.other")
	public DataSource dateSource() {
		return new DruidDataSource();
	}

	public static void main(String[] args) {
		SpringApplication.run(DgDataApplication.class, args);
	}


}
