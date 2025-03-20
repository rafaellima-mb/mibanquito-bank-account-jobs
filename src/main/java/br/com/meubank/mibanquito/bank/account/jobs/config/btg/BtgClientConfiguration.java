package br.com.meubank.mibanquito.bank.account.jobs.config.btg;

    import br.com.meubank.mibanquito.btgclient.client.AccountClient;
    import br.com.meubank.mibanquito.btgclient.dto.BtgClientConfigurationDto;
    import io.micrometer.core.instrument.MeterRegistry;
    import org.springframework.boot.context.properties.EnableConfigurationProperties;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import java.util.Optional;

    @Configuration
    @EnableConfigurationProperties(BtgClientProperties.class)
    public class BtgClientConfiguration {
      private final MeterRegistry meterRegistry;
      private final BtgClientProperties btgClientProperties;

      public BtgClientConfiguration(MeterRegistry meterRegistry, BtgClientProperties btgClientProperties) {
        this.meterRegistry = meterRegistry;
        this.btgClientProperties = btgClientProperties;
      }

      @Bean("btgMbpayAccountClient")
      public AccountClient btgMbpayAccountClient() {
        return buildClient(btgClientProperties.mbpay());
      }

      @Bean("btgMbexAccountClient")
      public AccountClient btgMbexAccountClient() {
        return buildClient(btgClientProperties.mbex());
      }

      private AccountClient buildClient(BtgClientProperties.Mbpay mbpay) {
        return new AccountClient(
            Optional.of(meterRegistry),
            BtgClientConfigurationDto.builder()
                .authUrl(mbpay.authUrl())
                .accountUrl(mbpay.balanceUrl())
                .clientId(mbpay.clientId())
                .clientSecret(mbpay.clientSecret())
                .accountId(mbpay.accountId())
                .build());
      }

      private AccountClient buildClient(BtgClientProperties.Mbex mbex) {
        return new AccountClient(
            Optional.of(meterRegistry),
            BtgClientConfigurationDto.builder()
                .authUrl(mbex.authUrl())
                .accountUrl(mbex.balanceUrl())
                .clientId(mbex.clientId())
                .clientSecret(mbex.clientSecret())
                .accountId(mbex.accountId())
                .build());
      }
    }