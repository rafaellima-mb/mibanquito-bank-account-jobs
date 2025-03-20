package br.com.meubank.mibanquito.bank.account.jobs.task;

import br.com.meubank.mibanquito.bank.account.jobs.service.FinancialMovementAsyncService;
import br.com.meubank.mibanquito.jobrunner.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BtgFinancialMovementsTask implements BaseTask {
    private final Logger logger = LoggerFactory.getLogger(BtgFinancialMovementsTask.class);
    private final FinancialMovementAsyncService service;

    @Autowired
    public BtgFinancialMovementsTask(FinancialMovementAsyncService service) {
        this.service = service;
    }

    public void run() {
        var targetDate = LocalDate.now();
        logger.info("starting job BtgFinancialMovementsAsyncTask targetDate={}", targetDate);
        service.runTask(targetDate);
    }
}
