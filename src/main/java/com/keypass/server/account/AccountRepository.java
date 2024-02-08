package com.keypass.server.account;

import com.keypass.server.account.dto.AccountRequestDto;
import com.keypass.server.account.dto.AccountUpdateRequestDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsernameOrEmail(String username, String email);

//    @Modifying
//    @Query("UPDATE Account acc SET " +
//            "acc.firstName = :firstName, " +
//            "acc.lastName = :lastName, " +
//            "acc.username = :username, " +
//            "acc.email = :email " +
//            "WHERE acc.id = :userId")
//    int updateAccount(@Param("userId") UUID userId,
//                      @Param("firstName") String firstName,
//                      @Param("lastName") String lastName,
//                      @Param("username") String username,
//                      @Param("email") String email);

    @Modifying
    @Query("UPDATE Account acc SET " +
            "acc.firstName = :#{#accountRequestDto.firstName()}, " +
            "acc.lastName = :#{#accountRequestDto.lastName()}, " +
            "acc.username = :#{#accountRequestDto.username()}," +
            "acc.email = :#{#accountRequestDto.email()} " +
            "WHERE acc.id = :userId")
    int updateAccountById(@Param("userId") UUID userId,  @Param("accountRequestDto") AccountUpdateRequestDto accountRequestDto);
}
