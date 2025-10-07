package com.learning.learn03.services.Implements;

import com.learning.learn03.Security.JwtService;
import com.learning.learn03.base.BaseService;
import com.learning.learn03.dtos.AuthenticationRequestDto;
import com.learning.learn03.dtos.AuthenticationResponseDto;
import com.learning.learn03.services.IAuthenticationService;
import com.learning.learn03.models.Account;
import com.learning.learn03.models.Role;
import com.learning.learn03.models.User;
import com.learning.learn03.models.UserStatus;
import com.learning.learn03.repositories.AccountRepository;
import com.learning.learn03.repositories.RoleRepository;
import com.learning.learn03.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl extends BaseService<User, Integer> implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    protected AuthenticationServiceImpl(JpaRepository<User, Integer> repository, AuthenticationManager authenticationManager,
                                        AccountRepository accountRepository, UserRepository userRepository, RoleRepository roleRepository,
                                        JwtService jwtService, PasswordEncoder passwordEncoder
    ) {
        super(repository);
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }


    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto request) {
        final Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final UserDetails userDetails = (UserDetails) auth.getPrincipal();

        final Account account = accountRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        if (account.getStatus().equals(UserStatus.Approved)) {

            account.setAccountId(UUID.randomUUID());
            accountRepository.save(account);

            final String token = jwtService.generateAccessToken(account.getUser());

            final String refreshToken = jwtService.generateRefreshToken(account.getUser());

            return AuthenticationResponseDto.builder().accessToken(token)
                    .refreshToken(refreshToken).tokenType("Barrier ")
                    .role(account.getRole().getRoleName()).build();
        }

        throw new AccessDeniedException("Your account is not active!");
    }

    @Override
    public void changeRole(String username, String roleName) {
        Account account = accountRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        Role role = roleRepository.findByRoleName(roleName.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        if (account.getUser().getRoles().contains(role)) {
            account.setRole(role);
            accountRepository.save(account);
        } else throw new AccessDeniedException("Don't have permission to change to this role!");
    }

    @Override
    public void addRoleToPerson(String role, Integer userId) {
        Role founded = roleRepository.findByRoleName(role.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.getRoles().add(founded);
        userRepository.save(user);
    }

    @Override
    public void activeAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(UserStatus.Approved);
        accountRepository.save(account);
    }

    @Override
    public void inactiveAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        account.setStatus(UserStatus.Pending);
        accountRepository.save(account);
    }

    @Override
    public List<Role> getUserRoles(Principal principal) {
        String username = principal.getName();
        Account account = accountRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        return account.getUser().getRoles();
    }


    @Override
    protected void prePersist(User user) {
        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("User already exists!");
        }
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
    }

    @Override
    protected void postPersist(User user) {
        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        Account account = Account.builder()
                .userName(user.getEmail())
                .password(passwordEncoder.encode(user.getPhoneNumber()))
                .status(UserStatus.Pending)
                .user(user)
                .role(role)
                .build();

        user.setAccount(account);
        accountRepository.save(account);
    }

}
