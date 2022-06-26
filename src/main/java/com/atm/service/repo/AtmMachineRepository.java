package com.atm.service.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.atm.model.Account;

public interface AtmMachineRepository extends MongoRepository<Account, Long> {

	Account findByAccountNumber(long accountNumber);
}
