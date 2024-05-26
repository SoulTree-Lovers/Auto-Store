package org.example.api.mvc.domain.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3320/autostore");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 연결 풀 설정
        dataSource.setMaximumPoolSize(20); // 최대 연결 수
        dataSource.setMinimumIdle(5); // 최소 idle 연결 수
        dataSource.setConnectionTimeout(30000); // 커넥션 타임아웃 (밀리초 단위)
        // 다양한 설정들을 필요에 따라 추가할 수 있습니다.

        return dataSource;
    }
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3320/autostore");
////        dataSource.setUrl("jdbc:mysql://autostore.cle2yiiugd0z.ap-southeast-2.rds.amazonaws.com:3306/autostoreDB?serverTimezone=Asia/Seoul&characterEncoding=UTF-8");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root");
//        dataSource.setConnectionProperties(new Properties(100));
////        dataSource.setPassword("root1234!!");
//        return dataSource;
////        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
////        return builder.setType(EmbeddedDatabaseType.HSQL).build();
//    }
}
