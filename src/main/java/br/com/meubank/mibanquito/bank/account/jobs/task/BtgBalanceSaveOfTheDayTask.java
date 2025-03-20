package br.com.meubank.mibanquito.bank.account.jobs.task;

import br.com.meubank.mibanquito.bank.account.jobs.service.BalanceManagementService;
import br.com.meubank.mibanquito.jobrunner.task.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BtgBalanceSaveOfTheDayTask implements BaseTask {
    private final BalanceManagementService service;

    @Autowired
    public BtgBalanceSaveOfTheDayTask(BalanceManagementService service) {
        this.service = service;
    }

    public void run() {
        var targetDate = LocalDate.now().minusDays(1);
        service.fetchAndSave(targetDate);
    }
}
