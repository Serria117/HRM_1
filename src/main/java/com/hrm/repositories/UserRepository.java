package com.hrm.repositories;

import com.hrm.dto.user.UserDto;
import com.hrm.dto.user.UserProjection.UserViewDto;
import com.hrm.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Query(nativeQuery = true, value = "select BIN_TO_UUID(u.id) as id, u.username as username, r.roleName as roleName, u.fullName as fullName,\n" +
            "       u.email as email, u.phone as phone, d.departmentName as departmentName,\n" +
            "       u.address as address, u.bankAccount as bankAccount, u.bankFullName as bankFullName, u.bankShortName as bankShortName\n" +
            "from user u\n" +
            "left join department d on u.id = d.mng_user_id\n" +
            "inner join user_role ur on u.id = ur.user_id\n" +
            "inner join roles r on ur.role_id = r.id")
    Page<UserViewDto> getAllUserDepartmentNodeleted(Pageable pageable);

    @Query(nativeQuery = true, value = " select *" +
            " from user_role ur " +
            " inner join  user u on ur.user_id = u.id" +
            " inner join roles r on ur.role_id = r.id" +
            " where ur.role_id != 1 and ur.role_id != 4")
    List<AppUser> getListEpl();
}
