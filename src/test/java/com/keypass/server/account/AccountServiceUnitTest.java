package com.keypass.server.account;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Account Service Unit Test")
class AccountServiceUnitTest {

        @Mock
        private AccountRepository accountRepository;

        @InjectMocks
        private AccountService underTest;

        @Test
        @DisplayName("Should be able to create a new account and return")
        void shouldBeAbleToCreateANewAccountAndReturn() {
                // given
                Account account = Account.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .username("johndoe123")
                                .email("johndoe@test.com")
                                .password(new BCryptPasswordEncoder().encode("123123"))
                                .build();
                // when
                underTest.create(account);

                // then
                ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
                verify(accountRepository).save(accountArgumentCaptor.capture());
                Account capturedAccount = accountArgumentCaptor.getValue();
                assertThat(capturedAccount).isEqualTo(account);
        }

        @Test
        @DisplayName("Should be able to get a account by Id")
        void shouldBeAbleToGetAAccountById() {
                // given
                final UUID id = UUID.randomUUID();
                final Account mockAccount = Account.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .username("johndoe123")
                                .email("johndoe@test.com")
                                .password(new BCryptPasswordEncoder().encode("123123"))
                                .build();

                underTest.create(mockAccount);

                // when
                when(accountRepository.findById(id))
                                .thenReturn(Optional.of(
                                                Account.builder()
                                                                .firstName("John")
                                                                .lastName("Doe")
                                                                .username("johndoe123")
                                                                .email("johndoe@test.com")
                                                                .password(new BCryptPasswordEncoder().encode("123123"))
                                                                .build()));

                final Optional<Account> expected = underTest.getAccountById(id.toString());
                final Account storedMockedData = expected.get();

                assertThat(expected).isNotNull();
                assertThat(storedMockedData).hasFieldOrProperty("id");
        }

        @Test
        @DisplayName("Should be able to get a account by username")
        void shouldBeAbleToGetAccountByUsername() {
                final Account mockAccount = Account.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .username("johndoe123")
                                .email("johndoe@test.com")
                                .password(new BCryptPasswordEncoder().encode("123123"))
                                .build();

                underTest.create(mockAccount);

                // when
                when(accountRepository.findByUsername("johndoe123"))
                                .thenReturn(Optional.of(
                                                Account.builder()
                                                                .firstName("John")
                                                                .lastName("Doe")
                                                                .username("johndoe123")
                                                                .email("johndoe@test.com")
                                                                .password(new BCryptPasswordEncoder().encode("123123"))
                                                                .build()));

                final Account expected = underTest.getAccountByUsername("johndoe123");

                assertThat(expected).isNotNull();
                assertThat(expected).hasFieldOrProperty("id");
        }

        @Test
        @DisplayName("Should be able to get a account by email")
        void shouldBeAbleToGetAccountByEmail() {
                final Account mockAccount = Account.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .username("johndoe123")
                                .email("johndoe@test.com")
                                .password(new BCryptPasswordEncoder().encode("123123"))
                                .build();

                underTest.create(mockAccount);

                // when
                when(accountRepository.findByEmail("johndoe@test.com"))
                                .thenReturn(Optional.of(
                                                Account.builder()
                                                                .firstName("John")
                                                                .lastName("Doe")
                                                                .username("johndoe123")
                                                                .email("johndoe@test.com")
                                                                .password(new BCryptPasswordEncoder().encode("123123"))
                                                                .build()));

                final Account expected = underTest.getAccountByEmail("johndoe@test.com");

                assertThat(expected).isNotNull();
                assertThat(expected).hasFieldOrProperty("id");
        }
}