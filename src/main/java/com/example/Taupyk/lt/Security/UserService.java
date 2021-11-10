package com.example.Taupyk.lt.Security;

import com.example.Taupyk.lt.Models.CustomUser;
import com.example.Taupyk.lt.Repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private CustomUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = userRepository.findCustomUserByName(username);
        if(customUser != null)
            return customUser;
        else
            throw new UsernameNotFoundException("User not found");
    }

}
