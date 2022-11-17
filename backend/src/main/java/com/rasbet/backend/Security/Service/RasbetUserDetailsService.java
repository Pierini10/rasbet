package com.rasbet.backend.Security.Service;

import com.rasbet.backend.Database.UserDB;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RasbetUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        String password = UserDB.getPasswordByEmail(email);
        if(password.equals("")){
            throw new UsernameNotFoundException("Email" + email + "is not associated with any user.");
        }
        return User.withUsername(email)
                .password(password)
                .roles("USER")
                .build();
    }
}
