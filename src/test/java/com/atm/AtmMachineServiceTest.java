package com.atm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.atm.exception.InvalidDetailsException;
import com.atm.model.Account;
import com.atm.model.AccountResponse;
import com.atm.model.Denomination;
import com.atm.model.Withdraw;
import com.atm.service.AtmMachineService;
import com.atm.service.repo.AtmMachineRepository;

@SpringBootTest
public class AtmMachineServiceTest {

	@InjectMocks
	private AtmMachineService atmMachineService;

	@Mock
	private AtmMachineRepository atmMachineRepository;

	@Test
	void testGetDetailsSuccess() {
		Account account = new Account();
		account.setAccountNumber(123456);
		account.setPin(1234);
		Account expectedaccount = new Account(123456, 1234, 200, 1000.00);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(account);
		Account actualAccount = atmMachineService.getDetails(account.getAccountNumber(), account.getPin());
		assertThat(actualAccount).usingRecursiveComparison().isEqualTo(expectedaccount);
	}

	@Test
	void testGetDetailsInvalidPinException() {
		Account account = new Account();
		account.setAccountNumber(123456);
		account.setPin(123);
		Account expectedaccount = new Account(123456, 1234, 200, 1000.00);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(expectedaccount);
		Assertions.assertThrows(InvalidDetailsException.class,
				() -> atmMachineService.getDetails(account.getAccountNumber(), account.getPin()));
	}

	@Test
	void testGetDetailsAccountNumberDoesntExisitException() {
		Account account = new Account();
		
		account.setAccountNumber(123456);
		account.setPin(1234);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(null);
		Assertions.assertThrows(InvalidDetailsException.class,
				() -> atmMachineService.getDetails(account.getAccountNumber(), account.getPin()));
	}

	@Test
	void testWithdrawAmountSuccess() {
		Withdraw withdraw = new Withdraw();
		withdraw.setAccountNumber(123456);
		withdraw.setPin(1234);
		withdraw.setWithdrawAmount(100);
		Account account = new Account(123456, 1234, 200, 1000.00);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(account);
		Mockito.when(atmMachineService.getDetails(withdraw.getAccountNumber(), withdraw.getPin())).thenReturn(account);
		Denomination denomination = new Denomination(0, 0, 0, 2);
		AccountResponse expectedAccountResponse = new AccountResponse(123456,
				account.getBalance() - withdraw.getWithdrawAmount(), denomination, "Success!");
		AccountResponse actualResponse = atmMachineService.withdrawAmount(withdraw);
		assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedAccountResponse);
	}

	@Test
	void testWithdrawAmountMaximumLimitExceedException() {
		Withdraw withdraw = new Withdraw();
		withdraw.setAccountNumber(123456);
		withdraw.setPin(1234);
		withdraw.setWithdrawAmount(10000);
		Account account = new Account(123456, 1234, 200, 10000.00);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(account);
		Mockito.when(atmMachineService.getDetails(withdraw.getAccountNumber(), withdraw.getPin())).thenReturn(account);
		AccountResponse actualResponse = atmMachineService.withdrawAmount(withdraw);
		assertEquals("Entered amount is too high. Can't withdraw more than 1000 €", actualResponse.getMessage());
	}
	
	@Test
	void testWithdrawAmountMinimumLimitExceedException() {
		Withdraw withdraw = new Withdraw();
		withdraw.setAccountNumber(123456);
		withdraw.setPin(1234);
		withdraw.setWithdrawAmount(0);
		Account account = new Account(123456, 1234, 200, 10000.00);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(account);
		Mockito.when(atmMachineService.getDetails(withdraw.getAccountNumber(), withdraw.getPin())).thenReturn(account);
		AccountResponse actualResponse = atmMachineService.withdrawAmount(withdraw);
		assertEquals("Entered amount is too low", actualResponse.getMessage());
	}
	
	@Test
	void testWithdrawAmountValidAmountException() {
		Withdraw withdraw = new Withdraw();
		withdraw.setAccountNumber(123456);
		withdraw.setPin(1234);
		withdraw.setWithdrawAmount(101);
		Account account = new Account(123456, 1234, 200, 10000.00);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(account);
		Mockito.when(atmMachineService.getDetails(withdraw.getAccountNumber(), withdraw.getPin())).thenReturn(account);
		AccountResponse actualResponse = atmMachineService.withdrawAmount(withdraw);
		assertEquals("Please enter the amount in multiples of 5 or 10", actualResponse.getMessage());
	}
	

}
