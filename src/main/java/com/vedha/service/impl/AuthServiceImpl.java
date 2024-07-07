package com.vedha.service.impl;

import com.vedha.dto.Login;
import com.vedha.dto.LoginResponse;
import com.vedha.dto.LogoutResponse;
import com.vedha.dto.Users;
import com.vedha.entity.TokensEntity;
import com.vedha.entity.UsersEntity;
import com.vedha.exception.AuthException;
import com.vedha.repository.TokensRepository;
import com.vedha.repository.UsersRepository;
import com.vedha.security.JwtService;
import com.vedha.service.AuthService;
import com.vedha.util.TokenTypes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final ModelMapper modelMapper;

    private final UsersRepository usersRepository;

    private final TokensRepository tokensRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public Users register(Users user) {

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

        return modelMapper.map(usersRepository.save(modelMapper.map(user, UsersEntity.class)), Users.class);
    }

    @Override
    public LoginResponse login(Login login) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        UsersEntity loginUser = usersRepository.findByEmail(login.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: ".concat(login.getUsername())));

        Integer integer = revokeAllUserTokens(loginUser, TokenTypes.BEARER_ACCESS);
        log.debug("Revoked {} access tokens for user {}", integer, loginUser.getEmail());

        String jwtToken = jwtService.generateToken(loginUser);
        tokensRepository.save(TokensEntity.builder().token(jwtToken).tokenType(TokenTypes.BEARER_ACCESS).expired(false).revoked(false).user(loginUser).build());

        Integer integer1 = revokeAllUserTokens(loginUser, TokenTypes.BEARER_REFRESH);
        log.debug("Revoked {} refresh tokens for user {}", integer1, loginUser.getEmail());

        String jwtRefreshToken = jwtService.generateRefreshToken(loginUser);
        tokensRepository.save(TokensEntity.builder().token(jwtRefreshToken).tokenType(TokenTypes.BEARER_REFRESH).expired(false).revoked(false).user(loginUser).build());

        return LoginResponse.builder()
                .userName(loginUser.getEmail())
                .accessToken(jwtToken)
                .issuedDate(LocalDateTime.ofInstant(jwtService.extractIssuedAt(jwtToken).toInstant(), ZoneId.systemDefault()))
                .expiryDate(LocalDateTime.ofInstant(jwtService.extractExpirationDate(jwtToken).toInstant(), ZoneId.systemDefault()))
                .refreshToken(jwtRefreshToken)
                .refreshTokenIssuedDate(LocalDateTime.ofInstant(jwtService.extractIssuedAt(jwtRefreshToken).toInstant(), ZoneId.systemDefault()))
                .refreshTokenExpiryDate(LocalDateTime.ofInstant(jwtService.extractExpirationDate(jwtRefreshToken).toInstant(), ZoneId.systemDefault()))
                .message("Login successful")
                .build();
    }

    @Override
    public LogoutResponse logout(HttpServletRequest request, HttpServletResponse response) {

        try {

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorization != null && authorization.startsWith("Bearer ")) {

                String jwtToken = authorization.substring(7);

                String userName = jwtService.extractUserName(jwtToken);

                UsersEntity loginUser = usersRepository.findByEmail(userName).orElseThrow(() -> new AuthException("User not found: ".concat(userName)));

                TokensEntity validToken = tokensRepository.findByTokenAndTokenType(jwtToken, TokenTypes.BEARER_ACCESS).orElseThrow(() -> new AuthException("Invalid token. Please login again."));

                if (validToken.isRevoked() || validToken.isExpired()) {

                    throw new AuthException("Token is expired or revoked. Please login again.");

                } else {

                    revokeAllUserTokens(loginUser, TokenTypes.BEARER_ACCESS);
                    revokeAllUserTokens(loginUser, TokenTypes.BEARER_REFRESH);

                    SecurityContextHolder.clearContext();

                    return LogoutResponse.builder().userName(userName).message("User logged out successfully.").build();
                }

            } else {

                throw new AuthException("Authorization header is missing or invalid.");
            }
        } catch (Exception e) {

            log.error("Error occurred while logout the user: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        return null;
    }

    @Override
    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        try {

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorization != null && authorization.startsWith("Bearer ")) {

                String refreshToken = authorization.substring(7);

                String userName = jwtService.extractUserName(refreshToken);
                UsersEntity loginUser = usersRepository.findByEmail(userName).orElseThrow(() -> new AuthException("User not found: ".concat(userName)));

                TokensEntity validToken = tokensRepository.findByTokenAndTokenType(refreshToken, TokenTypes.BEARER_REFRESH)
                        .orElseThrow(() -> new AuthException("Invalid refresh token. Please login again."));

                if (validToken.isRevoked() || validToken.isExpired() || !jwtService.validateToken(refreshToken, loginUser)) {

                    throw new AuthException("Refresh token is expired or revoked. Please login again.");

                } else {

                    revokeAllUserTokens(loginUser, TokenTypes.BEARER_ACCESS);

                    String jwtToken = jwtService.generateToken(loginUser);
                    tokensRepository.save(TokensEntity.builder().token(jwtToken).tokenType(TokenTypes.BEARER_ACCESS).expired(false).revoked(false).user(loginUser).build());

                    return LoginResponse.builder()
                            .userName(loginUser.getEmail())
                            .accessToken(jwtToken)
                            .issuedDate(LocalDateTime.ofInstant(jwtService.extractIssuedAt(jwtToken).toInstant(), ZoneId.systemDefault()))
                            .expiryDate(LocalDateTime.ofInstant(jwtService.extractExpirationDate(jwtToken).toInstant(), ZoneId.systemDefault()))
                            .refreshToken(refreshToken)
                            .message("Token refreshed successfully.")
                            .refreshTokenIssuedDate(LocalDateTime.ofInstant(jwtService.extractIssuedAt(refreshToken).toInstant(), ZoneId.systemDefault()))
                            .refreshTokenExpiryDate(LocalDateTime.ofInstant(jwtService.extractExpirationDate(refreshToken).toInstant(), ZoneId.systemDefault()))
                            .build();

                }

            } else {

                throw new AuthException("Authorization header is missing or invalid.");
            }

        } catch (Exception e) {

            log.error("Error occurred while refreshing the token: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        return null;
    }

    private Integer revokeAllUserTokens(UsersEntity loginUser, TokenTypes tokenType) {

        List<TokensEntity> allValidTokensByUser = tokensRepository.findAllValidTokensByUser(loginUser.getId());
        allValidTokensByUser.stream().filter(t -> t.getTokenType().equals(tokenType)).forEach(t -> {

            t.setExpired(true);
            t.setRevoked(true);
        });

        tokensRepository.saveAll(allValidTokensByUser);

        return allValidTokensByUser.size();
    }
}
