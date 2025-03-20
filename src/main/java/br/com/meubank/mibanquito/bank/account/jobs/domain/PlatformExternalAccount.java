package br.com.meubank.mibanquito.bank.account.jobs.domain;

public record PlatformExternalAccount(
        String id,
        String partner,
        String partnerId,
        String accountOwner
) {
    public static final String BTG = "BTG";
}
