package br.com.meubank.mibanquito.bank.account.jobs.integration;

import br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccount;
import br.com.meubank.mibanquito.bank.account.jobs.exception.ThirdPartyIntegrationException;
import br.com.meubank.mibanquito.btgclient.client.AccountClient;
import br.com.meubank.mibanquito.btgclient.exception.ClientException;
import br.com.meubank.mibanquito.btgclient.exception.ValidationException;
import br.com.meubank.mibanquito.btgclient.model.balance.BalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;


@Component
public class BtgIntegration {
    final Logger logger = LoggerFactory.getLogger(BtgIntegration.class);
    private static final String MB_PAY = "MB_PAY";
    private final AccountClient btgMbpayAccountClient;
    private final AccountClient btgMbexAccountClient;

    public BtgIntegration(
            @Qualifier("btgMbpayAccountClient") AccountClient btgMbpayAccountClient,
            @Qualifier("btgMbexAccountClient") AccountClient btgMbexAccountClient) {
        this.btgMbpayAccountClient = btgMbpayAccountClient;
        this.btgMbexAccountClient = btgMbexAccountClient;
    }

    public void requestAsyncTransactions(
            PlatformExternalAccount account, LocalDate targetDate) {
        try {
            if (MB_PAY.equals(account.accountOwner())) {
                btgMbpayAccountClient.asyncRequestTransactions(account.partnerId(), targetDate.toString());
            } else {
                btgMbexAccountClient.asyncRequestTransactions(account.partnerId(), targetDate.toString());
            }

        } catch (Exception e) {
            logger.error(
                    "error request financialmovements accountId={} partnerId={} targetDate={}",
                    account.id(),
                    account.partnerId(),
                    targetDate);
            throw new ThirdPartyIntegrationException(e.getMessage());
        }
    }

    public BalanceResponse getEndOfTheDayBalanceByAccount(
            PlatformExternalAccount account, String targetDate) {
        try {
            BalanceResponse response;
            if (MB_PAY.equals(account.accountOwner())) {
                response =
                        btgMbpayAccountClient.getEndOfTheDayBalanceByAccountId(
                                account.partnerId(), targetDate);
            } else {
                response =
                        btgMbexAccountClient.getEndOfTheDayBalanceByAccountId(
                                account.partnerId(), targetDate);
            }
            return response;
        } catch (IOException | RuntimeException | ValidationException | ClientException e) {
            logger.error(
                    "error getting current balance accountOwner={} partnerId={} targetDate={}",
                    account.accountOwner(),
                    account.partnerId(),
                    targetDate);
            throw new ThirdPartyIntegrationException(e.getMessage());
        }
    }
}
