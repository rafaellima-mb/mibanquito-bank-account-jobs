package br.com.meubank.mibanquito.bank.account.jobs.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PlatformExternalAccountBalance(
        UUID id,
        UUID platformExternalAccountId,
        String partner,
        BigDecimal balanceAvailable,
        BigDecimal balanceBlocked,
        BigDecimal balance,
        LocalDate searchDate
) {
    public PlatformExternalAccountBalance(String platformExternalAccountId, String partner, LocalDate searchDate) {
        this(UUID.randomUUID(), UUID.fromString(platformExternalAccountId), partner, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, searchDate);
    }
}