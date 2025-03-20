package br.com.meubank.mibanquito.bank.account.jobs.repository;

import br.com.meubank.mibanquito.bank.account.jobs.domain.PlatformExternalAccount;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static br.com.meubank.mibanquito.jooq.Tables.PLATFORM_EXTERNAL_ACCOUNT;

@Repository
public class PlatformExternalAccountRepository {

    public final DSLContext db;

    public PlatformExternalAccountRepository(DSLContext db) {
        this.db = db;
    }

    public List<String> getEvents() {
        var record = db.fetch("SELECT 1 FROM dual");
        return record.getValues("1", String.class);
    }

    public List<PlatformExternalAccount> findAllByPartner(String partner) {
        return db.select(
                        PLATFORM_EXTERNAL_ACCOUNT.ID,
                        PLATFORM_EXTERNAL_ACCOUNT.PARTNER,
                        PLATFORM_EXTERNAL_ACCOUNT.PARTNER_ID,
                        PLATFORM_EXTERNAL_ACCOUNT.ACCOUNT_OWNER)
                .from(PLATFORM_EXTERNAL_ACCOUNT)
                .where(PLATFORM_EXTERNAL_ACCOUNT.PARTNER.eq(partner)
                        .and(PLATFORM_EXTERNAL_ACCOUNT.PARTNER_ID.isNotNull()))
                .fetchInto(PlatformExternalAccount.class);
    }
}
