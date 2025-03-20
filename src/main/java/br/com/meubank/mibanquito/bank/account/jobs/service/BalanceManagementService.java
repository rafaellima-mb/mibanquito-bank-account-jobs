package br.com.meubank.mibanquito.bank.account.jobs.service;

import br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccount;
import br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccountBalance;
import br.com.meubank.mibanquito.bank.account.jobs.integration.BtgIntegration;
import br.com.meubank.mibanquito.bank.account.jobs.repository.PlatformExternalAccountBalanceRepository;
import br.com.meubank.mibanquito.bank.account.jobs.repository.PlatformExternalAccountRepository;
import br.com.meubank.mibanquito.btgclient.model.balance.BalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import static br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccount.BTG;
import static br.com.meubank.mibanquito.btgclient.util.ValueConverter.fromCents;

@Service
public class BalanceManagementService {
    private final Logger log = LoggerFactory.getLogger(BalanceManagementService.class);
    private final PlatformExternalAccountRepository platformRepository;
    private final PlatformExternalAccountBalanceRepository platformExternalAccountBalanceRepository;
    private final BtgIntegration btgIntegration;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public BalanceManagementService(PlatformExternalAccountRepository platformRepository,
                                    PlatformExternalAccountBalanceRepository platformExternalAccountBalanceRepository,
                                    BtgIntegration btgIntegration) {
        this.platformRepository = platformRepository;
        this.platformExternalAccountBalanceRepository = platformExternalAccountBalanceRepository;
        this.btgIntegration = btgIntegration;
    }

    public void fetchAndSave(LocalDate targetDate) {
        var out = new ArrayList<PlatformExternalAccountBalance>();
        var accounts = platformRepository.findAllByPartner(BTG);
        accounts.forEach(
                account -> {
                    log.info("Loading balance for account {} date {}", account.id(), targetDate);
                    var balance = findEndOfTheDayBalance(account, targetDate);
                    log.info("Btg returned balance {}", balance.getBalance());
                    PlatformExternalAccountBalance platformExternalAccountBalance =
                            platformExternalAccountBalanceRepository
                                    .findByPlatformExternalAccountIdAndSearchDate(UUID.fromString(account.id()), targetDate)
                                    .orElse(newBalance(account, balance));
                    platformExternalAccountBalance = new PlatformExternalAccountBalance(
                            platformExternalAccountBalance.id(),
                            platformExternalAccountBalance.platformExternalAccountId(),
                            platformExternalAccountBalance.partner(),
                            fromCents(String.valueOf(balance.getAvailableBalance())),
                            fromCents(String.valueOf(balance.getBlockedAmount())),
                            fromCents(String.valueOf(balance.getBalance())),
                            platformExternalAccountBalance.searchDate()
                    );
                    out.add(platformExternalAccountBalance);
                    log.info("Loading balance for account {} date {}", account.id(), targetDate);
                });
        platformExternalAccountBalanceRepository.saveAll(out);
    }

    private static PlatformExternalAccountBalance newBalance(PlatformExternalAccount account, BalanceResponse.Balance balance) {
        return new PlatformExternalAccountBalance(account.id(), account.partner(), LocalDate.parse(balance.getSearchDate(), FORMATTER));
    }

    private BalanceResponse.Balance findEndOfTheDayBalance(
            PlatformExternalAccount account, LocalDate targetDate) {
        return btgIntegration.getEndOfTheDayBalanceByAccount(account, targetDate.toString()).getBalance();
    }
}
