package com.learning.learn03.Security;

import com.learning.learn03.config.CustomUserDetailsService;
import com.learning.learn03.models.Account;
import com.learning.learn03.repositories.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AccountRepository accountRepository;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService userDetailsService, AccountRepository accountRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        Account account = accountRepository.findByUserName(email)
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(account.getUserName());

        if (jwtService.isTokenValid(token, userDetails) && account.getAccountAuthId() != null) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}