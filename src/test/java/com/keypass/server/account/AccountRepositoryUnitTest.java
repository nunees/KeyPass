package com.keypass.server.account;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataJpaTest
class AccountRepositoryUnitTest {

    @Autowired
    private AccountRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("Test that should find an account by the username")
    void testThatShouldFindAccountByUsername() {
        //given
        Account account = Account.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe123")
                .email("johndoe@test.com")
                .password(new BCryptPasswordEncoder().encode("123123"))
                .build();
        underTest.save(account);
        //when
        boolean exists =  underTest.findByEmail("johndoe@test.com").isPresent();
        //then
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findByUsernameOrEmail() {
    }
}