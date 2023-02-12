package com.hrm.repositories;

import com.hrm.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID>
{
    @EntityGraph(attributePaths = {"roles.appAuthorities"})
    @Query("select a from AppUser a where a.username = ?1")
    AppUser findByUsername(String username);

    @Query("select (count(a) > 0) from AppUser a where a.username = ?1")
    boolean existByName(String username);

    @Query("select (count(a) > 0) from AppUser a where a.email = ?1")
    boolean existByEmail(String email);

    @Modifying @Transactional
    @Query("UPDATE AppUser set refreshToken = :token, refreshTokenExpiration = :expiration where username = :username")
    void updateRefreshToken(String username, String token, LocalDateTime expiration);

    @Query("select a from AppUser a where a.verificationCode = ?1")
    AppUser findByVerificationCode(String verificationCode);

    @EntityGraph(attributePaths = {"roles.appAuthorities"})
    @Query("select a from AppUser a where a.isDeleted = false order by a.createdTime, a.username")
    Page<AppUser> findAllNonDeleted(Pageable pageable);

}
