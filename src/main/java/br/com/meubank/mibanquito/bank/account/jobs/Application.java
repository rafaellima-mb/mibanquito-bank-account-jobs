package br.com.meubank.mibanquito.bank.account.jobs;

import br.com.meubank.mibanquito.jobrunner.annotation.EnableJobRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJobRunner
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
