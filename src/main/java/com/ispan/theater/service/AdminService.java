package com.ispan.theater.service;

import com.ispan.theater.dto.UserCredentials;
import com.ispan.theater.domain.Admin;
import com.ispan.theater.repository.AdminRepository;
import com.ispan.theater.util.JsonWebTokenUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    @Autowired
    public AdminService(@Lazy AuthenticationManager authenticationManager,@Lazy PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminname(username.trim());
        if (admin == null) {
            System.out.println("Admin not found");
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
    public String login(UserCredentials userCredentials) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.username, userCredentials.password, Collections.singleton(authority)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Admin admin = adminRepository.findByAdminname(authentication.getName());
        JSONObject inputjson = new JSONObject().put("userid", admin.getId()).put("email", admin.getUsername());
        return jsonWebTokenUtility.adminToken(inputjson.toString(),null);
    }
    public boolean checkPassword(UserCredentials userCredentials) {
        Admin admin = adminRepository.findByAdminname(userCredentials.username);
        if (admin != null) {
            if(passwordEncoder.matches(userCredentials.password, admin.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
