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

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping({"", "/cadastrar"})
	public Usuario save(@RequestBody Usuario userInfo) {
		return userInfoService.save(userInfo);
	}

	@GetMapping("/token/{token}")
	public boolean isValidToken(@PathVariable String token) {
		return userInfoService.isValidToken(token);
	}

	@PostMapping("/login")
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
	public List<Usuario> getAll() {
		return userInfoService.getAll();
	}
}
