package br.com.meubank.mibanquito.bank.account.jobs.repository;

import br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccountBalance;
import br.com.meubank.mibanquito.jooq.tables.records.PlatformExternalAccountBalancesRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.meubank.mibanquito.jooq.Tables.PLATFORM_EXTERNAL_ACCOUNT_BALANCES;

@Repository
public class PlatformExternalAccountBalanceRepository {

    public final DSLContext db;

    public PlatformExternalAccountBalanceRepository(DSLContext db) {
        this.db = db;
    }

    public Optional<PlatformExternalAccountBalance> findByPlatformExternalAccountIdAndSearchDate(UUID accountId, LocalDate searchDate) {
        var fetchedOne = db.selectFrom(PLATFORM_EXTERNAL_ACCOUNT_BALANCES)
                .where(PLATFORM_EXTERNAL_ACCOUNT_BALANCES.PLATFORM_EXTERNAL_ACCOUNT_ID.eq(accountId)
                        .and(PLATFORM_EXTERNAL_ACCOUNT_BALANCES.SEARCH_DATE.eq(searchDate)))
                .fetchOne();

        return Optional.ofNullable(fetchedOne).map(r -> new PlatformExternalAccountBalance(
                r.getId(),
                r.getPlatformExternalAccountId(),
                r.getPartner(),
                r.getBalanceAvailable(),
                r.getBalanceBlocked(),
                r.getBalance(),
                r.getSearchDate()
        ));
    }

    public void saveAll(List<PlatformExternalAccountBalance> balances) {
        var records = balances.stream()
                .map(balance -> {
                    PlatformExternalAccountBalancesRecord platformExternalAccountBalancesRecord = db.newRecord(PLATFORM_EXTERNAL_ACCOUNT_BALANCES);
                    platformExternalAccountBalancesRecord.setId(balance.id());
                    platformExternalAccountBalancesRecord.setPlatformExternalAccountId(balance.platformExternalAccountId());
                    platformExternalAccountBalancesRecord.setPartner(balance.partner());
                    platformExternalAccountBalancesRecord.setBalanceAvailable(balance.balanceAvailable());
                    platformExternalAccountBalancesRecord.setBalanceBlocked(balance.balanceBlocked());
                    platformExternalAccountBalancesRecord.setBalance(balance.balance());
                    platformExternalAccountBalancesRecord.setSearchDate(balance.searchDate());
                    return platformExternalAccountBalancesRecord;
                })
                .toList();

        db.batchStore(records).execute();
    }


}
