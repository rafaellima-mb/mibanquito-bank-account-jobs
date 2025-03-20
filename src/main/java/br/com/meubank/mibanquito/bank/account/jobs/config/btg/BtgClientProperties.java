package br.com.meubank.mibanquito.bank.account.jobs.config.btg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "app.btg")
public record BtgClientProperties(
        Mbpay mbpay,
        Mbex mbex
) {
    @ConstructorBinding
    public BtgClientProperties {}
    public record Mbpay(
            String balanceUrl,
            String authUrl,
            String clientId,
            String clientSecret,
            String accountId
    ) {}

    public record Mbex(
            String balanceUrl,
            String authUrl,
            String clientId,
            String clientSecret,
            String accountId
    ) {}
}
