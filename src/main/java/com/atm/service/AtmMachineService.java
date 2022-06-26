package com.atm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.atm.exception.InvalidDetailsException;
import com.atm.model.Account;
import com.atm.model.AccountResponse;
import com.atm.model.Denomination;
import com.atm.model.Withdraw;
import com.atm.service.repo.AtmMachineRepository;

@Component
@Service
public class AtmMachineService {

	final static Logger LOGGER = LoggerFactory.getLogger(AtmMachineService.class);

	private double ATM_BALANCE = 1500.00;

	@Autowired
	private AtmMachineRepository atmMachineRepository;

	public Account createAccount(Account acc) {
		return atmMachineRepository.save(acc);
	}

	public Account getDetails(long accountNumber, int pin) {
		Integer checkPin = pin;
		Long checkAccNumber = accountNumber;
		Account accResponse = new Account();
		if (checkPin.equals(null) || checkAccNumber.equals(null)) {
			LOGGER.error("Invalid Account Number or Pin");
			throw new InvalidDetailsException("Invalid Account Number or Pin");
		} else {
			if (atmMachineRepository.findByAccountNumber(accountNumber) == null) {
				LOGGER.error("Account Number Doesnt Exisit In DB");
				throw new InvalidDetailsException("Account Number Doesnt Exisit In DB");
			}

			else {
				accResponse = atmMachineRepository.findByAccountNumber(accountNumber);
				if (accResponse.getPin() == pin) {
					accResponse.setAccountNumber(accountNumber);
					accResponse.setBalance(accResponse.getBalance());
				} else {
					LOGGER.error("Invalid Account Number or Pin");
					throw new InvalidDetailsException("Invalid Account Number or Pin");
				}
			}
		}
		return accResponse;
	}

	public AccountResponse withdrawAmount(Withdraw withdraw) {
		double balance = 0;
		AccountResponse accresponse = new AccountResponse();
		Account acc = getDetails(withdraw.getAccountNumber(), withdraw.getPin());
		balance = acc.getBalance();
		// check for withdraw amount greater than 0
		if (withdraw.getWithdrawAmount() <= 0) {
			LOGGER.error("Entered amount is too low" + acc.getAccountNumber());
			accresponse.setMessage("Entered amount is too low");
			acc.setBalance(0);
			return accresponse;
		}
		// check to limit withdrawal amount per transaction
		else if(withdraw.getWithdrawAmount() > 1000) {
			LOGGER.error("Entered amount is too high to proceess the transaction. Can't withdraw more than 1000 €" + acc.getAccountNumber());
			accresponse.setMessage("Entered amount is too high. Can't withdraw more than 1000 €");
			acc.setBalance(0);
			return accresponse;
		}
		// check for withdraw amount for multiple of 5
		else if (withdraw.getWithdrawAmount() % 5 != 0) {
			LOGGER.error("Please enter the amount in multiples of 5");
			accresponse.setMessage("Please enter the amount in multiples of 5 or 10");
			return accresponse;
		}
		// check for balance in atm
		else if (withdraw.getWithdrawAmount() > ATM_BALANCE) {
			LOGGER.error("Don't have enough funds in ATM machine, Sorry! ");
			accresponse.setMessage("Don't have enough funds in ATM machine, Sorry! ");
			return accresponse;
		}
		// check for customer balance
		else if (withdraw.getWithdrawAmount() > balance) {
			LOGGER.error("Don't have enough funds in your Account");
			accresponse.setMessage("Don't have enough funds in your Account");
			return accresponse;
		}
		Denomination denomination = getDenomination(withdraw.getWithdrawAmount());
		balance = balance - withdraw.getWithdrawAmount();
		ATM_BALANCE = ATM_BALANCE - withdraw.getWithdrawAmount();
		acc.setBalance(balance);
		atmMachineRepository.save(acc);
		accresponse.setAccountNumber(acc.getAccountNumber());
		accresponse.setBalance(acc.getBalance());
		accresponse.setMessage("Success!");
		accresponse.setDenomination(denomination);
		return accresponse;
	}

	private Denomination getDenomination(double withdrawAmount) {
		int moneyValue = (int) withdrawAmount;
		Denomination denomination = new Denomination();
		int[] noteValues = { 50, 20, 10, 5 };
		if (moneyValue > 1000) {
			System.out.println("ATM Cash Limit exceeds.");
		} else {
			for (int i = 0; i < noteValues.length && moneyValue != 0; i++) {
				if (moneyValue >= noteValues[i])
					LOGGER.info("No of " + noteValues[i] + "'s" + " :" + moneyValue / noteValues[i]);

				switch (noteValues[i]) {
				case 50:
					denomination.setFifties(moneyValue / noteValues[i]);
					break;
				case 20:
					denomination.setTwenties(moneyValue / noteValues[i]);
					break;
				case 10:
					denomination.setTens(moneyValue / noteValues[i]);
					break;
				case 5:
					denomination.setFives(moneyValue / noteValues[i]);
					break;

				default:
					break;
				}
				moneyValue = moneyValue % noteValues[i];
			}
		}
		return denomination;
	}

}
