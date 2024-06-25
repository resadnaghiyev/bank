package com.rashad.bank.api.auth.service;

import com.rashad.bank.api.auth.repository.UserRepository;
import com.rashad.bank.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String pin) throws UserNotFoundException {
        return userRepository.findByPin(pin)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UserNotFoundException(pin));
    }
}
