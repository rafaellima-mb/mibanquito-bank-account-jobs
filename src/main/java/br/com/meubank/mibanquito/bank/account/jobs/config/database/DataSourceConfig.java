package br.com.meubank.mibanquito.bank.account.jobs.config.database;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    private final DataSourceProperties dataSourceProperties;

    public DataSourceConfig(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(dataSourceProperties.url())
                .username(dataSourceProperties.username())
                .password(dataSourceProperties.password())
                .driverClassName(dataSourceProperties.driverClassName())
                .build();
    }
}