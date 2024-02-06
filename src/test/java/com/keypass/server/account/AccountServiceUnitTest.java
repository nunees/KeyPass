package com.keypass.server.account;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// When creating tests use the context: given -> when -> then

@ExtendWith(MockitoExtension.class)
@DisplayName("Account Service Unit Test")
@RequiredArgsConstructor
class AccountServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService underTest;

    @InjectMocks
    private ModelMapper modelMapper;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .username("johndoe123")
                .email("johndoe@test.com")
                .password(new BCryptPasswordEncoder().encode("123123"))
                .enabled(true)
                .build();
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Should be able to create a new account and return")
    void shouldBeAbleToCreateANewAccountAndReturn() {
        underTest.create(account);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        assertThat(capturedAccount).isEqualTo(account);
    }

    @Test
    @DisplayName("Should be able to get a account by Id")
    void shouldBeAbleToGetAAccountById() {
        final UUID id = UUID.randomUUID();

        underTest.create(account);

        when(accountRepository.findById(id))
                .thenReturn(Optional.of(account));

        final Optional<Account> expected = underTest.getAccountById(id.toString());
        final Account storedMockedData = expected.get();

        assertThat(expected).isNotNull();
        assertThat(storedMockedData).hasFieldOrProperty("id");
    }

    @Test
    @DisplayName("Should be able to get a account by username")
    void shouldBeAbleToGetAccountByUsername() {
        underTest.create(account);

        // when
        when(accountRepository.findByUsername("johndoe123"))
                .thenReturn(Optional.of(account));

        final Account expected = underTest.getAccountByUsername("johndoe123");

        assertThat(expected).isNotNull();
        assertThat(expected).hasFieldOrProperty("id");
    }

    @Test
    @DisplayName("Should be able to get a account by email")
    void shouldBeAbleToGetAccountByEmail() {

        underTest.create(account);

        // when
        when(accountRepository.findByEmail("johndoe@test.com"))
                .thenReturn(Optional.of(account));

        final Account expected = underTest.getAccountByEmail("johndoe@test.com");

        assertThat(expected).isNotNull();
        assertThat(expected).hasFieldOrProperty("id");
    }

    @Test
    @DisplayName("Should be able to update an account")
    void shouldBeAbleToUpdateAnAccount() {

        underTest.create(account);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        underTest.getAccountById(account.getId().toString()).orElseThrow();

        account.setEmail("johndoe@gmail.com");
        account.setUsername("john_doe");

        AccountUpdateRequestDto accountUpdateRequestDto =
                new AccountUpdateRequestDto(
                        account.getFirstName(),
                        account.getLastName(),
                        account.getUsername(),
                        account.getEmail()
                );

        underTest.updateAccount(account.getId(), accountUpdateRequestDto);

        when(accountRepository.updateAccount(
                account.getId(),
                accountUpdateRequestDto.firstName(),
                accountUpdateRequestDto.lastName(),
                accountUpdateRequestDto.username(),
                accountUpdateRequestDto.email()))
                .thenReturn(1);

        final int rowsInserted = underTest.updateAccount(
                account.getId(), accountUpdateRequestDto);

        assertThat(rowsInserted).isEqualTo(1);
    }

    @Test
    @DisplayName("Should not be able to update an user with different id")
    void shouldNotBeAbleToUpdateAnUserWithDifferentId() {
        underTest.create(account);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        underTest.getAccountById(account.getId().toString()).orElseThrow();

        account.setEmail("johndoe@gmail.com");
        account.setUsername("john_doe");

        AccountUpdateRequestDto accountUpdateRequestDto =
                new AccountUpdateRequestDto(
                        account.getFirstName(),
                        account.getLastName(),
                        account.getUsername(),
                        account.getEmail()
                );

        underTest.updateAccount(UUID.randomUUID(), accountUpdateRequestDto);

        final int rowsInserted = underTest.updateAccount(
                UUID.randomUUID(), accountUpdateRequestDto);

        assertThat(rowsInserted).isEqualTo(0);
    }
}