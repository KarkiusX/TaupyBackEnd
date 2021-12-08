package com.example.Taupyk.lt.Controllers;

import com.example.Taupyk.lt.DTO.UserDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://127.0.0.1:3000", allowCredentials = "true")
@CrossOrigin(origins = "https://taupyk-cia.herokuapp.com/", allowCredentials = "true")
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
        List<CustomUser> userWithSameName = userRepository.findAll().stream().filter(u -> u.getUsername() == customUser.getUsername()).collect(Collectors.toList());
        if(userWithSameName.size() > 0)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customUser);
        }
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleRepository.findRoleByName("USER"));
        if(customUser.getUsername().contains("Karolis"))
            roleSet.add(roleRepository.findRoleByName("ADMIN"));
        CustomUser newUser = new CustomUser(new User(customUser.getUsername(), encoder.encode(customUser.getPassword()), new ArrayList<>()), customUser.getEmail(), roleSet);
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping(path = "/names/{name}")
    public boolean isValidName(@PathVariable String name)
    {

        long usersWithSameName = userRepository.findAll().stream().filter(us -> us.getName().equals(name)).count();


        if(usersWithSameName > 0)
            return false;

        return true;
    }
    @GetMapping(path = "/user/{userid}")
    public ResponseEntity GetUser(@PathVariable long userid)
    {

        Optional<CustomUser> user = userRepository.findAll().stream().filter(us -> us.getUId() == userid).findFirst();

        if(!user.isPresent())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(user.get());
    }
    @GetMapping(path = "/email/{email}")
    public boolean isValidEmail(@PathVariable String name)
    {

        long usersWithSameName = userRepository.findAll().stream().filter(us -> us.getEmail().equals(name)).count();

        if(usersWithSameName > 0)
            return false;

        return true;
    }
    @PostMapping(path = "/login/")
    public ResponseEntity loginUser(@RequestBody UserLoginDto customUser, HttpServletResponse response)
    {

        Optional<CustomUser> userExist = userRepository.findAll().stream().filter(user -> user.getEmail().equals(customUser.getEmail())).findFirst();

        if(userExist.isPresent())
        {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userExist.get().getUsername(), customUser.getPassword())
                    );
            final CustomUser user = (CustomUser) userService.loadUserByUsername(userExist.get().getUsername());
            final String jwt = jwtToken.generateToken(user);

            if(customUser.isstayLoggedIn())
            {
                Cookie jwtTokenCookie = new Cookie("auth", jwt);

                jwtTokenCookie.setMaxAge(432000);
                //jwtTokenCookie.setSecure(true);
                jwtTokenCookie.setHttpOnly(false);
                jwtTokenCookie.setPath("/");
                response.addCookie(jwtTokenCookie);
            }

            return ResponseEntity.ok(jwt);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping(path = "/authorize")
    public ResponseEntity tryAuthorize()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null)
        {
            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            customUser.getRoles().forEach(role -> System.out.println(role.getName().hashCode()));
            List<String> roles = customUser.getRoles().stream().map(role -> role.getName()).filter(role -> role.equals("ADMIN")).collect(Collectors.toList());
            return ResponseEntity.ok(new UserDto(customUser.getUId(), customUser.getUsername(), (roles.size() > 0)));
        }
        return ResponseEntity.ok().build();
    }

}
