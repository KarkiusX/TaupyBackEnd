package com.example.Taupyk.lt.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "User")
public class CustomUser implements UserDetails {

    @Transient
    @Autowired
    private PasswordEncoder encoder;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long UId;

    private String name;

    private String password;

    private String email;

    @Transient
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = {
                    @JoinColumn(name = "user_uid")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_uid") })
    private Set<Role> roles;

    public CustomUser(final User _user, final String email, final Set<Role> role) {
        this.user = _user;
        this.name = _user.getUsername();
        this.password = _user.getPassword();
        this.email = email;
        this.roles = role;

    }
    public CustomUser()
    {

    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void loadUser()
    {
        user = new User(name, password, new HashSet<>());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();
        getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @PostLoad
    private void Load()
    {
        loadUser();
    }

    @Override
    public String getUsername() {
        if (this.user == null) {
            return null;
        }
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.isCredentialsNonExpired();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public String getName() {
        return name;
    }

    public long getUId() {
        return UId;
    }

    public void setUId(long UId) {
        this.UId = UId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
