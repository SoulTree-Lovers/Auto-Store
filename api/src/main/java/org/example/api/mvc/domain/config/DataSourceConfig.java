package org.example.api.mvc.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3320/autostore");
        dataSource.setUrl("jdbc:mysql://autostore.cle2yiiugd0z.ap-southeast-2.rds.amazonaws.com:3306/autostoreDB?serverTimezone=Asia/Seoul&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root1234!!");
        return dataSource;
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        return builder.setType(EmbeddedDatabaseType.HSQL).build();
    }
}
