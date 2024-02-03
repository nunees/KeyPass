package com.keypass.server.account;

import com.keypass.server.utils.JsonConverter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Account Controller Integration Test")
@SuppressWarnings("null")
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private Account mockedAccount;

    private AccountRequestDto accountRequestDto;

    @BeforeEach
    public void setUp() {
        mockedAccount = Account.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe123")
                .email("johndoe@test.com")
                .password("123123")
                .build();

        accountRequestDto = AccountRequestDto.builder()
                .firstName(mockedAccount.getFirstName())
                .lastName(mockedAccount.getLastName())
                .username(mockedAccount.getUsername())
                .email(mockedAccount.getEmail())
                .password(mockedAccount.getPassword())
                .build();
    }

    @Test
    @DisplayName("Test controller that successfully creates a new account [POST]")
    @Order(1)
    void testControllerThatSuccessfullyCreatesNewAccount() throws Exception {
        String jsonRequestBody = JsonConverter.convertToJson(accountRequestDto);

        this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Test controller that fails to create a new account with an existing username [POST]")
    @Order(2)
    void testControllerThatFailsToCreateNewAccountWithExistingUsername() throws Exception {

        accountRepository.save(mockedAccount);

        mockedAccount.setEmail("otheremail@test.com");

        String jsonRequestBody = JsonConverter.convertToJson(accountRequestDto);

        ResultActions secondInsertionResult = (ResultActions) this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestBody));

        secondInsertionResult.andExpect(status().isBadRequest());
    }

}