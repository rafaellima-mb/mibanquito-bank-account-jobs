package br.com.meubank.mibanquito.bank.account.jobs.task;

import br.com.meubank.mibanquito.bank.account.jobs.service.FinancialMovementAsyncService;
import br.com.meubank.mibanquito.jobrunner.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BtgFinancialMovementsPreviousDayTask implements BaseTask {
    private final Logger logger = LoggerFactory.getLogger(BtgFinancialMovementsPreviousDayTask.class);
    private final FinancialMovementAsyncService service;

    @Autowired
    public BtgFinancialMovementsPreviousDayTask(FinancialMovementAsyncService service) {
        this.service = service;
    }

    public void run() {
        var targetDate = LocalDate.now().minusDays(1);
        logger.info("starting job BtgFinancialMovementsAsyncTask PREVIOUSDAY targetDate={}", targetDate);
        service.runTask(targetDate);
    }
}
