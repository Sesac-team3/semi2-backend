package com.example.sesacsemi2.global.jwt;

import com.example.sesacsemi2.global.exception.ErrorCode;
import com.example.sesacsemi2.global.exception.GlobalException;
import com.example.sesacsemi2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.sesacsemi2.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USERNAME));
		return new UserDetailsImpl(user);
	}
}
