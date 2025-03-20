package br.com.meubank.mibanquito.bank.account.jobs.service;

import br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccount;
import br.com.meubank.mibanquito.bank.account.jobs.exception.ThirdPartyIntegrationException;
import br.com.meubank.mibanquito.bank.account.jobs.integration.BtgIntegration;
import br.com.meubank.mibanquito.bank.account.jobs.repository.PlatformExternalAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccount.BTG;

@Service
public class FinancialMovementAsyncService {
    private final Logger logger = LoggerFactory.getLogger(FinancialMovementAsyncService.class);

    private final BtgIntegration btgIntegration;
    private final PlatformExternalAccountRepository platformRepository;


    public FinancialMovementAsyncService(
            BtgIntegration btgIntegration,
            PlatformExternalAccountRepository platformRepository) {
        this.btgIntegration = btgIntegration;
        this.platformRepository = platformRepository;
    }

    public void requestFinancialMovementsAsync(
            PlatformExternalAccount account, LocalDate targetDate) {
        logger.info(
                "request movements async partnerId={} targetDate={}", account.partnerId(), targetDate);

        try {
            btgIntegration.requestAsyncTransactions(account, targetDate);
        } catch (Exception ex) {
            logger.error(
                    "error requesting movements partnerId={} targetDate={}",
                    account.partnerId(),
                    targetDate,
                    ex);
            throw new ThirdPartyIntegrationException(ex.getMessage());
        }
    }

    public void runTask(LocalDate targetDate) {
        var accounts = platformRepository.findAllByPartner(BTG).stream().toList();

        accounts.stream()
                .parallel()
                .forEach(account -> requestFinancialMovementsAsync(account, targetDate));
    }
}
