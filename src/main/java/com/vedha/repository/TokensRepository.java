package com.vedha.repository;

import com.vedha.entity.TokensEntity;
import com.vedha.util.TokenTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokensRepository extends JpaRepository<TokensEntity, Long> {

    @Query("""
            select t from TokensEntity t inner join UsersEntity u on  t.user.id = u.id where u.id = :userId and (t.expired = false or t.revoked = false)
            """)
    List<TokensEntity> findAllValidTokensByUser(Long userId);

    Optional<TokensEntity> findByToken(String token);

    Optional<TokensEntity> findByTokenAndTokenType(String refreshToken, TokenTypes tokenType);
}
