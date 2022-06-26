package com.atm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.atm.controller.AtmController;
import com.atm.model.Account;
import com.atm.model.AccountResponse;
import com.atm.model.Denomination;
import com.atm.model.Withdraw;
import com.atm.service.AtmMachineService;
import com.atm.service.repo.AtmMachineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AtmAssesmentApplicationTests {

	@InjectMocks
	AtmController atmController;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	AtmMachineService atmMachineService;

	 @MockBean(name="atmMachineRepository")
	AtmMachineRepository atmMachineRepository;

	@Test
	void testGetAccountDeatilsSuccess() throws Exception {
		Withdraw withdraw = new Withdraw();
		withdraw.setAccountNumber(123);
		withdraw.setPin(123);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(withdraw);
		Account account = new Account();
		account.setAccountNumber(123);
		account.setBalance(123);
		account.setPin(123);
		Mockito.when(atmMachineRepository.findByAccountNumber(withdraw.getAccountNumber())).thenReturn(account);
		Mockito.when(atmMachineService.getDetails(withdraw.getAccountNumber(),withdraw.getPin())).thenReturn(account);
		mockMvc.perform(get("/api/accountBalance").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	void testWithdrawAmountSuccess() throws Exception {
		Withdraw withdraw = new Withdraw();
		withdraw.setAccountNumber(123456);
		withdraw.setPin(1234);
		withdraw.setWithdrawAmount(100);
		Account account = new Account(123456, 1234, 200, 1000.00);
		Mockito.when(atmMachineRepository.findByAccountNumber(Mockito.anyLong())).thenReturn(account);
		Mockito.when(atmMachineService.getDetails(withdraw.getAccountNumber(), withdraw.getPin())).thenReturn(account);
		Denomination denomination = new Denomination(0, 0, 0, 2);
		AccountResponse accountResponse = new AccountResponse(123456,
				account.getBalance() - withdraw.getWithdrawAmount(), denomination, "Success!");

		Mockito.when(atmMachineService.withdrawAmount(withdraw)).thenReturn(accountResponse);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(withdraw);
		mockMvc.perform(put("/api/withdraw").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

}
