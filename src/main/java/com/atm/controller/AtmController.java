package com.atm.controller;

import java.util.Random;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atm.model.Account;
import com.atm.model.AccountResponse;
import com.atm.model.ExceptionResponse;
import com.atm.model.Withdraw;
import com.atm.service.AtmMachineService;

@RestController
@RequestMapping(value = "/api")
public class AtmController {

	final static Logger LOGGER = LoggerFactory.getLogger(AtmController.class);

	@Autowired
	private AtmMachineService atmMachineService;
	
	@GetMapping("/accountBalance")
	public ResponseEntity<AccountResponse> getAccountDeatils(@RequestBody Withdraw acc) {
		Account response = atmMachineService.getDetails(acc.getAccountNumber(), acc.getPin());
		AccountResponse returnedvalues = new AccountResponse();
		returnedvalues.setAccountNumber(response.getAccountNumber());
		returnedvalues.setBalance(response.getBalance());
		returnedvalues.setMessage("Success!");
		return new ResponseEntity<>(returnedvalues, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<Account> createAccount(@Valid @RequestBody Account acc) {
		acc.setAccountNumber((acc.getAccountNumber() == 0) ? ((long) Math.floor(Math.random() * 9000000000L))
				: acc.getAccountNumber());
		if (acc.getPin() == 0) {
			Random random = new Random();
			acc.setPin(random.nextInt(10000));
		}
		acc = atmMachineService.createAccount(acc);
		Account createdAcc = new Account();
		createdAcc.setAccountNumber(acc.getAccountNumber());
		createdAcc.setBalance(acc.getBalance());
		createdAcc.setPin(acc.getPin());
		return new ResponseEntity<>(createdAcc, HttpStatus.OK);
	}

	@PutMapping("/withdraw")
	public ResponseEntity<?> withdrawAmount(@RequestBody Withdraw acc) throws Exception {
		AccountResponse response = atmMachineService.withdrawAmount(acc);
		if (!response.getMessage().equalsIgnoreCase("Success!")) {
			ExceptionResponse error = new ExceptionResponse();
			error.setErrorMessage(response.getMessage());
			error.setRequestedURI("/api/withdraw");
			return new ResponseEntity<>(error, HttpStatus.OK);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
