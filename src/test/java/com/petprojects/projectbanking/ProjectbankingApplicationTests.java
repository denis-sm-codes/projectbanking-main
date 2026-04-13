package com.petprojects.projectbanking;

import com.petprojects.projectbanking.dto.request.DtoCreateUser;
import com.petprojects.projectbanking.dto.response.DtoCreatedPerson;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.UserRepository;
import com.petprojects.projectbanking.service.SupportService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AllArgsConstructor
class ProjectbankingApplicationTests {

	private final SupportService supportService;
	private final UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void createUser_shouldCreateUserAndAccount() {
		DtoCreateUser dto = new DtoCreateUser();
		dto.setFirstname("Denis");
		dto.setSecondname("Test");
		dto.setEmail("test@mail.com");

		DtoCreatedPerson result = supportService.createUser(dto);

		assertNotNull(result.getUserNumber());
		assertNotNull(result.getCountNumber());
	}

	@Test
	void findByUserNumber_shouldReturnUser() {
		User user = userRepository.findByUserNumber("0000001").orElse(null);
		assertNotNull(user);
	}
}