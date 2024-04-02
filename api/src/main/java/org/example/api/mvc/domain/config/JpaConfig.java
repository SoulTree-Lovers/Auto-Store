package org.example.api.mvc.domain.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EntityScan(basePackages = "org.example.api")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.example.api.mvc.domain.product.repository")
@EnableJpaAuditing // Entity의 @CreatedDate, @LastModifiedDate 사용 가능하도록
@ComponentScan(basePackages = "org.example.api")
@RequiredArgsConstructor
class JpaConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true); // Set this to true if you want Hibernate to generate DDL scripts
        vendorAdapter.setShowSql(true); // Set this to true to show SQL queries in the console

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("org.example.api.mvc.domain.product.repository");
        entityManagerFactoryBean.setPersistenceUnitName("hello");
        entityManagerFactoryBean.setEntityManagerFactoryInterface(EntityManagerFactory.class);
        // Set JPA properties if needed
        // entityManagerFactoryBean.setJpaProperties(jpaProperties());
        // Set packages to scan for entities
        // Set JPA vendor adapter (e.g., Hibernate)
        // entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}



