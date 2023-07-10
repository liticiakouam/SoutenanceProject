package com.liticia.soutenanceApp.security;

import com.liticia.soutenanceApp.model.Role;
import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.RoleRepository;
import com.liticia.soutenanceApp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Role role = roleRepository.findByUsersId(user.getId()).get();
//        List<Role> authorities = new ArrayList<>();
//        authorities.add(role.get());
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
        System.out.println(authorities);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassWord())
                .authorities(authorities)
                .build();
        return new AuthUser(user.getId(), user.getEmail(), user.getPassWord(), authorities);

    }

//    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
//        Collection < ? extends GrantedAuthority> mapRoles = roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//        return mapRoles;
//    }
}