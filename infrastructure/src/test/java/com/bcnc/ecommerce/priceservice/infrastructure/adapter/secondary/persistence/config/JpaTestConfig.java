package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository")
public class JpaTestConfig {

    /** Base de datos H2 en memoria. */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("price-service-test")          // opcional: nombre visible en logs
                .build();
    }

    /** Factory JPA que escanea únicamente las entidades de infrastructure. */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);            // crea el esquema automáticamente
        vendorAdapter.setShowSql(true);                // true para ver SQL en consola

        // Propiedades Hibernate específicas para H2
        Properties jpaProps = new Properties();
        jpaProps.put("hibernate.hbm2ddl.auto", "create-drop");   // crea y destruye tablas por test
        jpaProps.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpaProps.put("hibernate.format_sql", "true");            // pretty-print si activas showSql

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(
                "com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity");
        factory.setDataSource(dataSource);
        factory.setJpaProperties(jpaProps);
        return factory;
    }

    /** Transaction manager JPA estándar. */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
