package com.nsn.dubbo.dubboinvoker.web.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author donghao
 */
@Configuration
@MapperScan(basePackages = {"com.nsn.dubbo.dubboinvoker.dal.mapper","com.nsn.dubbo.dubboinvoker.dal.dao"})
@EnableTransactionManagement
public class MybatisConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	/**
	 * 配合注解完成事物管理
	 * 
	 * @return
	 */
	@Bean
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
