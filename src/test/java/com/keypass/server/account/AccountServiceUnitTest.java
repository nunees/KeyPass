package com.keypass.server.account;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService underTest;

    private UUID userId;

    private Account account;

    @BeforeEach
    void setUp(){
        userId =  UUID.randomUUID();
        account = Account.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .username("johndoe123")
                .email("johndoe@test.com")
                .password(new BCryptPasswordEncoder().encode("123123"))
                .build();
    }

    @Test
    @DisplayName("Test that create a new account")
    @Order(1)
    void testThatCreateANewAccount() {
        //given

        //when
        underTest.create(account);

        //then
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        assertThat(capturedAccount).isEqualTo(account);
    }

    @Test
    @DisplayName("Test that get account by id")
    @Order(2)
    void testThatGetAccountById() {
        System.out.println(accountRepository.save(account));
        Account savedMock = accountRepository.findById(userId).orElseThrow();

    }

    @Test
    @Disabled
    void getAccountByUsername() {
        underTest.create(account);
        when(accountRepository.findByEmail("johndoe@test.com").get()).thenReturn(account);
    }

    @Test
    @Disabled
    void getAccountByEmail() {
    }
}