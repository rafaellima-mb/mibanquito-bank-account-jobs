package br.com.meubank.mibanquito.bank.account.jobs.config.database;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "datasource")
public record DataSourceProperties(
        String url,
        String username,
        String password,
        String driverClassName
) {
    @ConstructorBinding
    public DataSourceProperties {}
}
