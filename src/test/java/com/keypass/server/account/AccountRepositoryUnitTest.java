package com.keypass.server.account;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Test
    @DisplayName("Should be able to save account")
    void testThatShouldFindAccountByUsername() {
        // given
        underTest.save(mockedAccount);
        // when
        boolean exists = underTest.findByUsername(mockedAccount.getUsername()).isPresent();
        // then
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should be able to find account by email")
    void shouldBeAbleToFindAccountByEmail() {
        // given
        underTest.save(mockedAccount);
        // when
        boolean exists = underTest.findByEmail(mockedAccount.getEmail()).isPresent();
        // then
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should be able to find account by username or email")
    void shouldBeAbleToFindAccountByUsernameOrEmail() {
        // given
        underTest.save(mockedAccount);
        // when
        boolean exists = underTest.findByUsernameOrEmail(mockedAccount.getUsername(), mockedAccount.getEmail())
                .isPresent();
        // then
        Assertions.assertThat(exists).isTrue();
    }
}