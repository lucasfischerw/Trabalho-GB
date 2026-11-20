package br.com.dev.usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dev.usuario.dto.UserInfoRequestDTO;
import br.com.dev.usuario.model.Usuario;
import br.com.dev.usuario.service.JwtService;
import br.com.dev.usuario.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "Operações básicas para cadastro e autenticação de usuários")
public class UsuarioController {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping
	@Operation(summary = "Cadastrar usuário", description = "Cadastra um novo usuário com senha criptografada via BCrypt")
	@ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso")
	public Usuario save(@RequestBody Usuario userInfo) {
		return userInfoService.save(userInfo);
	}

	@GetMapping("/token/{token}")
	@Operation(summary = "Validar token JWT", description = "Verifica se o token JWT informado é válido")
	@Parameters({
			@Parameter(name = "token", description = "Access Token JWT", required = true)
	})
	@ApiResponse(responseCode = "200", description = "Resultado booleano da validação do token")
	public boolean isValidToken(@PathVariable String token) {
		return userInfoService.isValidToken(token);
	}

	@PostMapping("/login")
	@Operation(summary = "Autenticar e gerar token JWT", description = "Autentica as credenciais do usuário e retorna o Access Token JWT")
	@ApiResponse(responseCode = "200", description = "Token JWT gerado com sucesso")
	public String authenticateAndGetToken(@RequestBody UserInfoRequestDTO userInfoRequestDTO) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							userInfoRequestDTO.getUsername(),
							userInfoRequestDTO.getPassword()
					)
			);
			return jwtService.generateToken(userInfoRequestDTO.getUsername());
		} catch (AuthenticationException e) {
			throw new UsernameNotFoundException("Invalid user request!");
		}
	}

	@GetMapping
	@Operation(summary = "Obter lista de usuários", description = "Obtém a lista de usuários cadastrados")
	@ApiResponse(responseCode = "200", description = "Lista de usuários")
	public List<Usuario> getAll() {
		return userInfoService.getAll();
	}
}
