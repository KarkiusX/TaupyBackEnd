package com.example.Taupyk.lt.Controllers;

import com.example.Taupyk.lt.DTO.UserLoginDto;
import com.example.Taupyk.lt.Models.CustomUser;
import com.example.Taupyk.lt.Models.Role;
import com.example.Taupyk.lt.Repositories.CustomUserRepository;
import com.example.Taupyk.lt.Repositories.RoleRepository;
import com.example.Taupyk.lt.Security.JwtToken;
import com.example.Taupyk.lt.DTO.UserRegisterDto;
import com.example.Taupyk.lt.Security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    public CustomUserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping(path = "/register/")
    public ResponseEntity createUser(@RequestBody UserRegisterDto customUser, HttpServletResponse response)
    {
        List<CustomUser> userWithSameName = userRepository.findAll().stream().filter(u -> u.getUsername() == customUser.getUsername()).toList();
        if(userWithSameName.size() > 0)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customUser);
        }
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleRepository.findRoleByName("USER"));
        if(customUser.getUsername().equals("Karolis"))
            roleSet.add(roleRepository.findRoleByName("ADMIN"));
        CustomUser newUser = new CustomUser(new User(customUser.getUsername(), encoder.encode(customUser.getPassword()), new ArrayList<>()), customUser.getEmail(), roleSet);
        userRepository.save(newUser);

        final String jwt = jwtToken.generateToken(newUser);

        Cookie jwtTokenCookie = new Cookie("auth", jwt);

        jwtTokenCookie.setMaxAge(86400);
        //jwtTokenCookie.setSecure(true);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setPath("api/user/");
        response.addCookie(jwtTokenCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(jwt);
    }
    @PostMapping(path = "/login/")
    public ResponseEntity loginUser(@RequestBody UserLoginDto customUser, HttpServletResponse response)
    {
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                customUser.getUsername(), customUser.getPassword())
                        );

        final CustomUser user = (CustomUser) userService.loadUserByUsername(customUser.getUsername());
        final String jwt = jwtToken.generateToken(user);

        return ResponseEntity.ok().build();

    }
}
