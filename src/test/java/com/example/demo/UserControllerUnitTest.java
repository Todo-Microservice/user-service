package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.example.demo.controller.UserController;
import com.example.demo.model.User;
import com.example.demo.respository.UserRepository;

@ExtendWith(MockitoExtension.class)
@Tag("unittest")
class UserControllerUnitTest {
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("getUserTest")
	void getUserTest() {
		// used if we don't have @ExtendWith(MockitoExtension.class) on the class
		// MockitoAnnotations.openMocks(this);
		
		// given
		int id = 1;
		User dummy = new User();
		dummy.setId(1);
		dummy.setUsername("akash");
		when(userRepository.findById(id)).thenReturn(Optional.of(dummy));
		
		// when
		ResponseEntity<?> userResponse = userController.getUser(id);
		User user = (User) userResponse.getBody();
		
		// then
		assertThat(user.getId()).isNotNull();
		assertThat(user.getUsername()).isEqualTo("akash");
	}

}
