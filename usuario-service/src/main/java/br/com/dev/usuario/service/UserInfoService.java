package br.com.dev.usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.dev.usuario.UserInfoDetails;
import br.com.dev.usuario.model.Usuario;
import br.com.dev.usuario.repository.UsuarioRepository;

@Service
public class UserInfoService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	@Lazy
	private PasswordEncoder encoder;

	@Autowired
	private JwtService jwtService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(
						"User not found with username: " + username));

		return new UserInfoDetails(user);
	}

	public Boolean isValidToken(String token) {
		try {
			String username = jwtService.extractUsername(token);

			if (username != null) {
				UserDetails userDetails = loadUserByUsername(username);
				boolean isValid = jwtService.validateToken(token, userDetails);
				return isValid;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public Usuario save(Usuario userInfo) {
		if (userInfo.getUsername() == null && userInfo.getEmail() != null) {
			userInfo.setUsername(userInfo.getEmail());
		}
		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		return repository.save(userInfo);
	}

	public List<Usuario> getAll() {
		return repository.findAll();
	}
}
