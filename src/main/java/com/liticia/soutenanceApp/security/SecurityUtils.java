package com.liticia.soutenanceApp.security;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@EnableWebSecurity
public class SecurityUtils {
    @Autowired
    private UserRepository userRepository;

    public long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Utilisateur non authentifié");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl authUser)) {
            throw new UsernameNotFoundException("Utilisateur non authentifié");
        }
        User user = userRepository.findUserByEmail(authUser.getUsername());

        return user.getId();
    }
}