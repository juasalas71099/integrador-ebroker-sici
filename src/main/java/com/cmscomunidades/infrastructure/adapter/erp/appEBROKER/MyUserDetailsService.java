package com.cmscomunidades.infrastructure.adapter.erp.appEBROKER;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Aquí buscas al usuario en BD o en memoria
        if (!username.equals("admin")) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.withUsername("admin")
                .password("{noop}password") // {noop} indica sin codificación
                .roles("USER")
                .build();
    }
}

