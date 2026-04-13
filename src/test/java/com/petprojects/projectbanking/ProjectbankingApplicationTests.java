package com.petprojects.projectbanking;

import com.petprojects.projectbanking.dto.request.DtoCreateUser;
import com.petprojects.projectbanking.dto.request.DtoTransaction;
import com.petprojects.projectbanking.dto.response.DtoCreatedPerson;
import com.petprojects.projectbanking.model.Account;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.AccountRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import com.petprojects.projectbanking.security.UserPrincipal;
import com.petprojects.projectbanking.service.SupportService;
import com.petprojects.projectbanking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AllArgsConstructor
class ProjectbankingApplicationTests {

	@Autowired
	private final SupportService supportService;

	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final TransactionService transactionService;

	@Autowired
	private final AccountRepository accountRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void createUser_shouldCreateUserAndAccount() {
		DtoCreateUser dto = DtoCreateUser.builder()
				.firstname("Denis")
				.secondname("Test")
				.email("unique@mail.com")
				.build();

		DtoCreatedPerson result = supportService.createUser(dto);

		assertNotNull(result.getUserNumber());
		assertNotNull(result.getCountNumber());
	}

	@Test
	void findByUserNumber_shouldReturnUser() {
		User user = userRepository.findByUserNumber("0000001").orElse(null);
		assertNotNull(user);
	}

	@Test
	void createUser_shouldFail_whenEmailExists() {
		String email = "duplicate@mail.com";
		DtoCreateUser dto = DtoCreateUser.builder()
				.firstname("User1").secondname("Test").email(email).build();

		supportService.createUser(dto); // Создаем первого

		DtoCreateUser dto2 = DtoCreateUser.builder()
				.firstname("User2").secondname("Test").email(email).build();

		assertThrows(RuntimeException.class, () -> supportService.createUser(dto2));
	}

	@Test
	void findByUserNumber_NotFound() {
		assertFalse(userRepository.findByUserNumber("9999999").isPresent());
	}

	@Test
	void makeTransaction_shouldTransferMoneySuccessfully() {
		DtoCreateUser user1Dto = DtoCreateUser.builder()
				.firstname("Sender")
				.secondname("Test")
				.email("sender@mail.com")
				.build();
		DtoCreatedPerson senderDto = supportService.createUser(user1Dto);

		DtoCreateUser user2Dto = DtoCreateUser.builder()
				.firstname("Receiver")
				.secondname("Test")
				.email("receiver@mail.com")
				.build();
		DtoCreatedPerson receiverDto = supportService.createUser(user2Dto);

		UserPrincipal principal = new UserPrincipal(
				senderDto.getUserNumber(),
				senderDto.getCountNumber(),
				"USER"
		);

		UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContextHolder.getContext().setAuthentication(auth);

		BigDecimal transferAmount = BigDecimal.valueOf(500);
		DtoTransaction transactionRequest = new DtoTransaction();
		transactionRequest.setToAccountNumber(receiverDto.getCountNumber());
		transactionRequest.setToAccountName("Receiver"); // Важно: совпадает с firstname получателя
		transactionRequest.setAmount(transferAmount);

		transactionService.makeTransaction(transactionRequest);

		Account senderAcc = accountRepository.findByCountNumber(senderDto.getCountNumber()).get();
		Account receiverAcc = accountRepository.findByCountNumber(receiverDto.getCountNumber()).get();

		assertEquals(0, BigDecimal.valueOf(500).compareTo(senderAcc.getBalance()), "Баланс отправителя должен уменьшиться");

		assertEquals(0, BigDecimal.valueOf(1500).compareTo(receiverAcc.getBalance()), "Баланс получателя должен увеличиться");
	}

	@Test
	void makeTransaction_shouldFail_whenInsufficientFunds() {
		DtoCreatedPerson sender = supportService.createUser(DtoCreateUser.builder()
				.firstname("Poor")
				.secondname("Man")
				.email("poor@mail.com").build());

		UserPrincipal principal = new UserPrincipal(sender.getUserNumber(), sender.getCountNumber(), "USER");
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, null));

		DtoTransaction richRequest = new DtoTransaction();
		richRequest.setToAccountNumber("SOME_ACC");
		richRequest.setAmount(BigDecimal.valueOf(5000));

		assertThrows(RuntimeException.class, () -> transactionService.makeTransaction(richRequest));
	}
}