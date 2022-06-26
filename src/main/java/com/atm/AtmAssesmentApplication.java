package com.atm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.atm.model.Account;
import com.atm.service.repo.AtmMachineRepository;

@SpringBootApplication
public class AtmAssesmentApplication implements CommandLineRunner {

	@Autowired
	private AtmMachineRepository atmMachineRepository;

	public static void main(String[] args) {
		SpringApplication.run(AtmAssesmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Account account1 = new Account(123456789, 1234, 200.00, 800.00);
		Account account2 = new Account(987654321, 4321, 150.00, 1230.00);
		atmMachineRepository.deleteAll();
		atmMachineRepository.save(account1);
		atmMachineRepository.save(account2);
	}

}
