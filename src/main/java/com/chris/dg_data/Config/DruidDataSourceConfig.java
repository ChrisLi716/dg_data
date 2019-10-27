package com.chris.dg_data.Config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)   //该注解的参数对应的类必须存在(DruidDataSource.class)，否则不解析该注解修饰的配置类；
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
//控制某个configuration是否生效

public class DruidDataSourceConfig {

	@Autowired
	private DruidDataSourceProperty druidDataSourceProperty;

	@Bean
	@Primary  //在同样的DataSource中，首先使用被标注的DataSource
	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(druidDataSourceProperty.getUrl());
		datasource.setUsername(druidDataSourceProperty.getUsername());
		datasource.setPassword(druidDataSourceProperty.getPassword());
		datasource.setDriverClassName(druidDataSourceProperty.getDriverClassName());

		datasource.setInitialSize(druidDataSourceProperty.getInitialSize());
		datasource.setMinIdle(druidDataSourceProperty.getMinIdle());
		datasource.setMaxActive(druidDataSourceProperty.getMaxActive());
		// 配置获取连接等待超时的时间
		datasource.setMaxWait(druidDataSourceProperty.getMaxWait());
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		datasource.setTimeBetweenEvictionRunsMillis(druidDataSourceProperty.getTimeBetweenEvictionRunsMillis());
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		datasource.setMinEvictableIdleTimeMillis(druidDataSourceProperty.getMinEvictableIdleTimeMillis());
		datasource.setValidationQuery(druidDataSourceProperty.getValidationQuery());
		datasource.setTestWhileIdle(druidDataSourceProperty.isTestWhileIdle());
		datasource.setTestOnBorrow(druidDataSourceProperty.isTestOnBorrow());
		datasource.setTestOnReturn(druidDataSourceProperty.isTestOnReturn());
		datasource.setPoolPreparedStatements(druidDataSourceProperty.isPoolPreparedStatements());
		datasource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceProperty.getMaxPoolPreparedStatementPerConnectionSize());
		datasource.setUseGlobalDataSourceStat(druidDataSourceProperty.isUseGlobalDataSourceStat());
		try {
			datasource.setFilters(druidDataSourceProperty.getFilters());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		datasource.setConnectionProperties(druidDataSourceProperty.getConnectionProperties());
		return datasource;
	}
}
