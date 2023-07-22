package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.User;
import br.com.trier.springvespertino.repositories.UserRepository;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@InjectMocks
	private UserServiceImpl userService;
	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("Teste inserir usuário")
	void insertUserTest() {
		User usuario = new User(1, "insert", "insert", "insert", "ADMIN" );
		when(userRepository.save(any())).thenReturn(usuario);
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
		var user = userService.insert(usuario);
		assertEquals(1, usuario.getId());
		assertEquals("insert", usuario.getName());
		assertEquals("insert", usuario.getEmail());
		assertEquals("insert", usuario.getPassword());
	}

	@Test
	@DisplayName("Teste inserir usuário com email existente")
	void emailExistenteTest() {
		User usuario = new User(1, "insert", "insert", "insert", "ADMIN" );
		User usuarioEmail = new User(2, "insert2", "insert", "insert2", "ADMIN" );
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEmail));
		var exception = assertThrows(
				IntegrityViolation.class, () -> userService.insert(usuario));
		assertEquals("Email já existente: insert", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste listar todos sem possuir usuários cadastrados")
	void listAllUsersEmptyTest() {
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.listAll());
		assertEquals("Nenhum usuário cadastrado", exception.getMessage());
	}
	
	@Test
	@DisplayName("Teste alterar usuário inexistente")
	void updateUsersNonExistsTest() {
		var usuarioAltera = new User(1,"altera", "altera", "altera", "ADMIN");
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.update(usuarioAltera));
		assertEquals("O usuário 1 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste deletar user")
	void deleteUserTest() {
		User user = new User(1,"altera", "altera", "altera", "ADMIN");
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		userService.delete(1);
		verify(userRepository, times(1)).delete(any(User.class));
	}

	@Test
	@DisplayName("Teste deletar usuário inexistente")
	void deleteNonExistingUserTest() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.delete(1));
		assertEquals("O usuário 1 não existe", exception.getMessage());
		verify(userRepository, never()).delete(any(User.class));
	}
	@Test
	@DisplayName("Teste atualizar usuário inexistente")
	void updateNonExistingUserTest() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		var exception = assertThrows(
				ObjectNotFound.class, () -> userService.update(new User(1,"altera", "altera", "altera", "ADMIN")));
		assertEquals("O usuário 1 não existe", exception.getMessage());
		verify(userRepository, never()).save(any(User.class));
	}
}
