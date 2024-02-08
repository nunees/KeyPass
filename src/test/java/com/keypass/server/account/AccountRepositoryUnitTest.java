package com.keypass.server.account;

import com.keypass.server.account.dto.AccountUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@DataJpaTest
@DisplayName("Account Repository Unit Test")
@SuppressWarnings("null")
class AccountRepositoryUnitTest {

    @Autowired
    private AccountRepository underTest;

    private Account mockedAccount;

    @BeforeEach
    void setUp() {
        mockedAccount = Account.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe123")
                .email("johndoe@test.com")
                .password(new BCryptPasswordEncoder().encode("123123"))
                .build();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

//    @Test
//    @DisplayName("Should be able to save account")
//    void testThatShouldFindAccountByUsername() {
//        // given
//        underTest.save(mockedAccount);
//        // when
//        boolean exists = underTest.findByUsername(mockedAccount.getUsername()).isPresent();
//        // then
//        Assertions.assertThat(exists).isTrue();
//    }
//
//    @Test
//    @DisplayName("Should be able to find account by email")
//    void shouldBeAbleToFindAccountByEmail() {
//        // given
//        underTest.save(mockedAccount);
//        // when
//        boolean exists = underTest.findByEmail(mockedAccount.getEmail()).isPresent();
//        // then
//        Assertions.assertThat(exists).isTrue();
//    }
//
//    @Test
//    @DisplayName("Should be able to find account by username or email")
//    void shouldBeAbleToFindAccountByUsernameOrEmail() {
//        // given
//        underTest.save(mockedAccount);
//        // when
//        boolean exists = underTest.findByUsernameOrEmail(mockedAccount.getUsername(), mockedAccount.getEmail())
//                .isPresent();
//        // then
//        Assertions.assertThat(exists).isTrue();
//    }

    @Test
    @DisplayName("Should be able to update an account")
    void shouldBeAbleToUpdateAccount() {
        Account savedAccount = underTest.save(mockedAccount);

        savedAccount.setEmail("johndoe@gmail.com");

        AccountUpdateRequestDto accountUpdateRequestDto = AccountUpdateRequestDto.builder()
                .email("somerandomemail@mail.com")
                .firstName(savedAccount.getFirstName())
                .lastName(savedAccount.getLastName())
                .username(savedAccount.getUsername())
                .build();

        int updatedAccountSuccess = underTest.updateAccountById(savedAccount.getId(), accountUpdateRequestDto);

        Assertions.assertThat(updatedAccountSuccess).isNotNull();
        // Should return 0 rows affected
        Assertions.assertThat(updatedAccountSuccess).isEqualTo(1);
    }

    @Test
    @DisplayName("Should fail to update account with different id")
    void shouldFailToUpdateAccountWithDifferentId() {
        Account savedAccount = underTest.save(mockedAccount);

        savedAccount.setEmail("johndoe@gmail.com");

        AccountUpdateRequestDto accountUpdateRequestDto = AccountUpdateRequestDto.builder()
                .email(savedAccount.getEmail())
                .firstName(savedAccount.getFirstName())
                .lastName(savedAccount.getLastName())
                .username(savedAccount.getUsername())
                .build();

        int updatedAccountSucess = underTest.updateAccountById(UUID.randomUUID(), accountUpdateRequestDto);

        Assertions.assertThat(updatedAccountSucess).isNotNull();
        // Should return 0 rows affected
        Assertions.assertThat(updatedAccountSucess).isEqualTo(0);
    }
}