package br.com.meubank.mibanquito.bank.account.jobs.exception;


public class ThirdPartyIntegrationException extends RuntimeException {
    public ThirdPartyIntegrationException(String message) {
        super(message);
    }
}